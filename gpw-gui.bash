#!/bin/bash
# GPW GUI Launcher Script
# Launches the pronounceable password generator GUI
# By Claude AI Sonnet 4.5

# Get the directory where this script is located
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Find the gpw jar file
JAR_FILE="$SCRIPT_DIR/gpw-1.0.2.jar"

# Check if jar exists
if [ ! -f "$JAR_FILE" ]; then
    echo "Error: Cannot find $JAR_FILE"
    echo "Please ensure the jar file is in the same directory as this script."
    exit 1
fi

# Check if java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    echo "Please install Java 11 or higher from https://adoptium.net/"
    exit 1
fi

# Check Java version (require 11+)
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F '.' '{print $1}')
if [ "$JAVA_VERSION" -lt 11 ]; then
    echo "Error: Java 11 or higher is required"
    echo "Current Java version: $(java -version 2>&1 | head -n 1)"
    echo "Please install Java 11 or higher from https://adoptium.net/"
    exit 1
fi

# Run the GUI
java -cp "$JAR_FILE" gpwgui.GpwGui
