#!/usr/bin/env bash

set -e

usage() {
    >&2 echo "Usage: create_release_tag.sh [-f|-i] <release-type> [<version>]"
    >&2 echo
    >&2 echo "    -f  fetch build number before creating tag"
    >&2 echo "    -i  fetch and increment build number before creating tag"
    exit 1
}

while [[ "$1" == "-"* ]]; do
    case $1 in
        -f) FETCH_BUILD_NUMBER=true
            ;;
        -i) INCREMENT_BUILD_NUMBER=true
            ;;
        * ) >&2 echo "Error: unknown option: $1"
            usage
            ;;
    esac
    shift
done

SCRIPT_DIR=$(dirname "$0")

# shellcheck disable=SC2166
if [ $# != 1 -a $# != 2 ]; then
    usage
fi

TYPE=$1
VERSION=$2
if [ $# -lt 2 ]; then
    VERSION=1.0.0
fi

# Increment & get build number
if [ "$FETCH_BUILD_NUMBER" = "true" ]; then
    git fetch origin build-number:build-number
fi
if [ "$INCREMENT_BUILD_NUMBER" = "true" ]; then
    "$SCRIPT_DIR"/increment_build_number.sh
fi
BUILD_NUMBER=$("$SCRIPT_DIR"/get_build_number.sh)

# Create tag message file
MESSAGE_FILE=$(uuidgen)
echo "$TYPE v$VERSION ($BUILD_NUMBER)" > "$MESSAGE_FILE"

# List changes in message file
PREV_TAG=$(git describe --tags --match "$TYPE*" --abbrev=0 2>/dev/null || true)
if [ "$PREV_TAG" != "" ]; then
    {
      echo "";
      echo "Changes since $PREV_TAG:";
      "$SCRIPT_DIR"/list_changes.sh "$PREV_TAG";
    } >> "$MESSAGE_FILE"
fi

# Show tag name and its message
TAG="$TYPE-v$VERSION-$BUILD_NUMBER"
echo "Tag name: $TAG"
echo "-----"
cat "$MESSAGE_FILE"
echo "-----"

# Create tag and push it
# shellcheck disable=SC2162
read -p "Is it okay to create this tag? [Y/n] " PROMPT_ANSWER
case $PROMPT_ANSWER in
    ""|[Yy]*)
        git tag -a -F "$MESSAGE_FILE" --cleanup=whitespace "$TAG"
        git push origin "$TAG"
        ;;
esac

# Clean up message file
rm -f "$MESSAGE_FILE"
