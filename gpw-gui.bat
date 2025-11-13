@echo off
REM GPW GUI Launcher Script for Windows
REM Launches the pronounceable password generator GUI
REM By Claude AI Sonnet 4.5

REM Get the directory where this script is located
set SCRIPT_DIR=%~dp0

REM Find the gpw jar file
set JAR_FILE=%SCRIPT_DIR%gpw-1.0.2.jar

REM Check if jar exists
if not exist "%JAR_FILE%" (
    echo Error: Cannot find %JAR_FILE%
    echo Please ensure the jar file is in the same directory as this script.
    exit /b 1
)

REM Check if java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo Error: Java is not installed or not in PATH
    echo Please install Java 11 or higher from https://adoptium.net/
    exit /b 1
)

REM Check Java version (require 11+)
for /f tokens^=2-5^ delims^=.-_+^" %%j in ('java -version 2^>^&1') do (
    set JAVA_VERSION=%%j
    goto :check_version
)

:check_version
if %JAVA_VERSION% LSS 11 (
    echo Error: Java 11 or higher is required
    java -version 2>&1 | findstr /C:"version"
    echo Please install Java 11 or higher from https://adoptium.net/
    exit /b 1
)

REM Run the GUI
start javaw -cp "%JAR_FILE%" gpwgui.GpwGui
