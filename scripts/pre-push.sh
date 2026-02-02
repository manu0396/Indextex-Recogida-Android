#!/bin/bash

set -e

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${YELLOW}🚀 [Pre-Push] Starting Build Integrity Check...${NC}"

# ==========================================
# 1. SHARED & ANDROID CHECKS
# ==========================================
echo -e "\n${YELLOW}--- [1/3] 🤖 Verifying Android & Shared Logic ---${NC}"

# 1. Generate resources
./gradlew generateSqlDelightInterface --quiet

# 2. Run Lint and Tests for a SPECIFIC Flavor (MockDebug)
echo "   Running Lint and Unit Tests (MockDebug)..."
./gradlew lintMockDebug testMockDebugUnitTest --quiet

echo -e "${GREEN}✅ Android environment is healthy.${NC}"


# ==========================================
# 2. IOS ENVIRONMENT CHECKS (macOS Only)
# ==========================================
echo -e "\n${YELLOW}--- [2/3] 🍎 Verifying iOS Environment ---${NC}"

if [[ "$OSTYPE" == "darwin"* ]]; then
    if command -v xcodebuild >/dev/null; then
        echo "   Xcode detected. Checking iOS build..."

        if [ -f "composeApp/Podfile" ] || [ -f "iosApp/Podfile" ]; then
             echo "   Running Pod Install..."
             ./gradlew podInstall --quiet
        fi
        ./gradlew linkDebugFrameworkIosSimulatorArm64 --quiet

        echo -e "${GREEN}✅ iOS environment is healthy.${NC}"
    else
        echo -e "${RED}⚠️  Xcode not found! Skipping iOS checks.${NC}"
    fi
else
    echo -e "🐧 Not running on macOS. Skipping iOS checks."
fi

# ==========================================
# 3. SUCCESS
# ==========================================
echo -e "\n${GREEN}✅✅ All Systems Go. Pushing changes...${NC}"
