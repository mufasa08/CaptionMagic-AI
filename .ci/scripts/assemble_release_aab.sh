#!/usr/bin/env bash

set -ex

./gradlew --stacktrace ":app:bundle${PRODUCT_FLAVOR}Release" \
    -PappVersionName="$VERSION_NAME"                    \
    -PappVersionCode="$VERSION_CODE"                    \
    -Pminify.enabled=true                               \
    -Pminify.productionMode="$MINIFY_PRODUCTION_MODE"

# Compress ProGuard outputs
(cd app/build/outputs && zip -r mapping.zip mapping)
cd ../../../
