#!/usr/bin/env bash

set -ex

./gradlew --stacktrace ":app:uploadDeployGate${PRODUCT_FLAVOR}Release"
