#!/usr/bin/env sh

set -exu

# Extract information from tag
TAG_NUM_SEPARATORS=$(echo "$GITHUB_REF_NAME" | grep -o '-' | wc -l)
if [ "$TAG_NUM_SEPARATORS" -lt "2" ]; then
    TAG_NUM_SEPARATORS=2
fi

TAG_VARIATION=$(echo "$GITHUB_REF_NAME" | cut -d '-' -f 1-$((TAG_NUM_SEPARATORS-1)))
VERSION_NAME=$(echo "$GITHUB_REF_NAME" | cut -d '-' -f $TAG_NUM_SEPARATORS | cut -c 2-)
VERSION_CODE=$(echo "$GITHUB_REF_NAME" | cut -d '-' -f $((TAG_NUM_SEPARATORS+1)))

case "$TAG_VARIATION" in
    "production" ) export PRODUCT_FLAVOR="Production" ;;
    "staging"        ) export PRODUCT_FLAVOR="Staging" ;;
    *                )
      export PRODUCT_FLAVOR="Development"
      ;;
esac

case "$PRODUCT_FLAVOR" in
    Production) export MINIFY_PRODUCTION_MODE="true" ;;
    *         ) export MINIFY_PRODUCTION_MODE="false" ;;
esac

{
  echo "TAG_VARIATION=$TAG_VARIATION";
  echo "VERSION_NAME=$VERSION_NAME";
  echo "VERSION_CODE=$VERSION_CODE";
  echo "PRODUCT_FLAVOR=$PRODUCT_FLAVOR";
  echo "MINIFY_PRODUCTION_MODE=$MINIFY_PRODUCTION_MODE";
} >> "$GITHUB_ENV"
