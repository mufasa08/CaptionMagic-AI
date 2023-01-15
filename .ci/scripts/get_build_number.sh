#!/usr/bin/env bash

BRANCH="origin/build-number"
FILE="build-number"

git fetch --prune

(git show "$BRANCH:$FILE" | tr -d '\n')
