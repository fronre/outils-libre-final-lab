#!/bin/bash
# Simple build script using javac and JUnit

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

JAVA_SOURCE="src/main/java"
TEST_SOURCE="src/test/java"
BUILD_DIR="build/classes"
TEST_BUILD_DIR="build/test-classes"

echo -e "${YELLOW}Compiling main classes...${NC}"
mkdir -p "$BUILD_DIR"
javac -d "$BUILD_DIR" $(find "$JAVA_SOURCE" -name "*.java")

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Main compilation successful${NC}"
else
    echo -e "${RED}✗ Main compilation failed${NC}"
    exit 1
fi

echo -e "${YELLOW}Compiling tests...${NC}"
mkdir -p "$TEST_BUILD_DIR"

# Download JUnit if needed
if [ ! -f "lib/junit-jupiter-api-5.9.3.jar" ]; then
    echo "Setting up test dependencies..."
    mkdir -p lib
    cd lib
    curl -O https://repo1.maven.org/maven2/org/junit/jupiter/junit-jupiter-api/5.9.3/junit-jupiter-api-5.9.3.jar
    curl -O https://repo1.maven.org/maven2/org/junit/jupiter/junit-jupiter-engine/5.9.3/junit-jupiter-engine-5.9.3.jar
    curl -O https://repo1.maven.org/maven2/org/opentest4j/opentest4j/1.2.0/opentest4j-1.2.0.jar
    curl -O https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.9.3/junit-platform-console-standalone-1.9.3.jar
    curl -O https://repo1.maven.org/maven2/org/assertj/assertj-core/3.24.1/assertj-core-3.24.1.jar
    cd ..
fi

CLASSPATH="$BUILD_DIR:lib/*"
javac -cp "$CLASSPATH" -d "$TEST_BUILD_DIR" $(find "$TEST_SOURCE" -name "*.java")

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Test compilation successful${NC}"
else
    echo -e "${RED}✗ Test compilation failed${NC}"
    exit 1
fi
