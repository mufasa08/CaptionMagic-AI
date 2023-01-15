#!/usr/bin/env bash

set -ex

case "$PRODUCT_FLAVOR" in
    Production | Staging) ./gradlew --stacktrace ":app:assemble${PRODUCT_FLAVOR}Release" \
    -PappVersionName="$VERSION_NAME"                    \
    -PappVersionCode="$VERSION_CODE"                    \
    -Pminify.enabled=true                               \
    -Pminify.productionMode="$MINIFY_PRODUCTION_MODE" ;;
    *         )  ./gradlew --stacktrace ":app:assemble${PRODUCT_FLAVOR}Release" \
    -Pminify.enabled=false                              \
    -Pminify.productionMode=false;;
esac
