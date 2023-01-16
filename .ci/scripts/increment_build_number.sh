#!/usr/bin/env bash

ROOT_DIR=$(pwd)
WORKTREE_DIR="build-number"
BRANCH="build-number"
FILE="build-number"
INITIAL_BUILD_NUMBER="499"

CODE_BRANCH_REV=$(git rev-parse --short HEAD)
CODE_BRANCH_NAME=$(git rev-parse HEAD | git name-rev --name-only --stdin)

checkout_build_number_branch() {
    # shellcheck disable=SC2164
    cd "$ROOT_DIR"
    git worktree remove --force $WORKTREE_DIR
    git fetch origin $BRANCH:refs/remotes/origin/$BRANCH
    # shellcheck disable=SC2181
    if [ $? -eq 0 ]; then
        # There is build-number remote branch

        # Create worktree with creating build-number local branch
        git worktree add -b $BRANCH $WORKTREE_DIR origin/$BRANCH || \

        # There is already build-number local branch. Create worktree with it
        { git worktree add $WORKTREE_DIR $BRANCH && \
              cd $WORKTREE_DIR                   && \
              git merge origin/$BRANCH; }
    else
        # There is no build-number remote branch yet. Create it as a new orphan branch
        git worktree add --detach $WORKTREE_DIR && \
            cd $WORKTREE_DIR                    && \
            git checkout --orphan $BRANCH       && \
            echo $INITIAL_BUILD_NUMBER > $FILE  && \
            git reset .                         && \
            git add $FILE                       && \
            git commit -m "Initial commit"      && \
            git push -u origin $BRANCH
    fi
    return $?
}

increment_build_number() {
    # shellcheck disable=SC2164
    cd "$ROOT_DIR"/$WORKTREE_DIR
    ORIGINAL_REV=$(git rev-parse HEAD)

    # Increment build number
    BUILD_NUMBER=$(cat $FILE)
    BUILD_NUMBER=$((BUILD_NUMBER+1))

    # Commit the new build number and push to remote
    echo "$BUILD_NUMBER" > $FILE && \
        git add $FILE            && \
        echo -e "Bump build number to $BUILD_NUMBER\n\nFor $CODE_BRANCH_NAME $CODE_BRANCH_REV" | git commit -F - && \
        git push -u origin $BRANCH
    RESULT=$?

    # If failed, reset local files
    if [ $RESULT -ne 0 ]; then
        git reset --hard "$ORIGINAL_REV"
    fi

    return $RESULT
}

# Repeat checkout & increment until they are successful
TRY=0
MAX_TRY=5
while :; do
    checkout_build_number_branch && \
        increment_build_number   && \
        break
    (( TRY++ ))
    if [ "$TRY" -ge $MAX_TRY ]; then
        >&2 echo "Failed to increment build number after $TRY tries. Abort"
        break
    fi
done

# Reset current working directory
# shellcheck disable=SC2164
cd "$ROOT_DIR"
git worktree remove --force $WORKTREE_DIR

# Set exit code
[ "$TRY" -lt $MAX_TRY ]
