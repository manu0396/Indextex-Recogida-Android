#!/bin/bash

set -e

echo "--- [1/3] Generating Data Contracts and Resources ---"
./gradlew generateSqlDelightInterface :components:generateComposeResClass --quiet

echo "--- [2/3] Synchronizing CocoaPods and iOS Native Linking ---"
./gradlew :composeApp:podInstall
./gradlew :composeApp:linkDebugTestIosSimulatorArm64 --stacktrace

echo "--- [3/3] Executing MVI and Business Logic Unit Tests ---"
./gradlew :composeApp:allTests --quiet

echo "--- ✅ Build Integrity Verified Successfully ---"
