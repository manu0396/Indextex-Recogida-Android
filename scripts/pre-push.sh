#!/bin/bash

set -e

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${YELLOW}🚀 [Pre-Push] Starting Build Integrity Check...${NC}"

# ==========================================
# 1. ANDROID CORE CHECKS
# ==========================================
echo -e "\n${YELLOW}--- [1/2] 🤖 Verifying Android Architecture ---${NC}"

# 1. Generate SqlDelight interfaces
echo "   Generating Database Interfaces..."
./gradlew generateSqlDelightInterface --quiet

# 2. Run Lint and Unit Tests for the active Flavor
echo "   Running Lint and Unit Tests (MockDebug)..."
./gradlew lintMockDebug testMockDebugUnitTest --quiet

echo -e "${GREEN}✅ Android environment is healthy.${NC}"

# ==========================================
# 2. SUCCESS
# ==========================================
echo -e "\n${GREEN}✅✅ All Systems Go. Pushing changes...${NC}"
