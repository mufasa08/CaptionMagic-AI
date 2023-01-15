#!/usr/bin/env bash

set -e

function is_tag_or_commit {
    TYPE=$(git cat-file -t "$1")
    # shellcheck disable=SC2166
    [ "$TYPE" = "tag" -o "$TYPE" = "commit" ]
    return $?
}

# shellcheck disable=SC2166
if [ $# != 1 -a $# != 2 ]; then
    >&2 echo "Usage: list_changes.sh <from-commit> [<to-commit>]"
    exit 1
fi

FROM=$1
TO=$2
if [ $# -lt 2 ]; then
    TO=HEAD
fi

if ! is_tag_or_commit "$FROM"; then
    >&2 echo "$FROM is not valid commit-ish name"
    exit 1
fi
if ! is_tag_or_commit $TO; then
    >&2 echo "$TO is not valid commit-ish name"
    exit 1
fi

git log --pretty=format:"%s >> %b" "$FROM"..$TO | \
    grep "Merge pull request #" | \
    sed -E 's/^Merge pull request #([0-9]+).*>>(.*)/#\1\2/' | \
    sort
