@echo off
REM Simple build script using javac

set JAVA_SOURCE=src\main\java
set TEST_SOURCE=src\test\java
set BUILD_DIR=build\classes
set TEST_BUILD_DIR=build\test-classes

echo Compiling main classes...
if not exist "%BUILD_DIR%" mkdir "%BUILD_DIR%"
javac -d "%BUILD_DIR%" "%JAVA_SOURCE%\com\pricing\*.java"

if %ERRORLEVEL% neq 0 (
    echo ERROR: Main compilation failed
    exit /b 1
)
echo.
echo Compilation successful!
