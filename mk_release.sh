#!/bin/bash
# Create GPW release archive
# Usage: ./create-release.sh [version]
# By Claude AI Sonnet 4.5 Nov-13-2025 

set -e  # Exit on error

# Get version from pom.xml if not provided
if [ -z "$1" ]; then
    VERSION=$(grep -m 1 '<version>' pom.xml | sed 's/.*<version>\(.*\)<\/version>.*/\1/')
    echo "No version specified, using version from pom.xml: $VERSION"
else
    VERSION="$1"
fi

RELEASE_NAME="gpw-${VERSION}"
RELEASE_DIR="bin/${RELEASE_NAME}"
JAR_FILE="target/gpw-${VERSION}.jar"

# Check if JAR exists
if [ ! -f "$JAR_FILE" ]; then
    echo "Error: JAR file not found at $JAR_FILE"
    echo "Please run 'mvn clean package' first"
    exit 1
fi

# Check if launcher scripts exist
SCRIPTS=("gpw.bash" "gpw.bat" "gpw-gui.bash" "gpw-gui.bat")
for script in "${SCRIPTS[@]}"; do
    if [ ! -f "$script" ]; then
        echo "Error: Launcher script $script not found"
        exit 1
    fi
done

echo "Creating release archives for version $VERSION..."

# Clean and create bin directory
rm -rf bin
mkdir -p "$RELEASE_DIR"

# Copy JAR and scripts
cp "$JAR_FILE" "$RELEASE_DIR/"
for script in "${SCRIPTS[@]}"; do
    cp "$script" "$RELEASE_DIR/"
done

# Create README.txt
cat > "$RELEASE_DIR/README.txt" << 'EOF'
GPW - Generate Pronounceable Passwords

INSTALLATION:
  Unix/Linux/macOS:
    1. Extract this archive
    2. sudo cp gpw*.bash gpw-*.jar /usr/local/bin/
    3. sudo chmod +x /usr/local/bin/gpw*.bash
    4. Run: gpw.bash or gpw-gui.bash

  Windows:
    1. Extract this archive
    2. Add folder to PATH or copy to a folder in PATH
    3. Run: gpw.bat or gpw-gui.bat

USAGE:
  CLI: gpw.bash [options]
  GUI: gpw-gui.bash

  Options:
    -h, --help           Show help
    -n, --number <n>     Number of passwords to generate
    -l, --length <n>     Length of passwords

ADVANCED:
  Run directly: java -jar gpw-*.jar [options]

REQUIREMENTS:
  Java 11 or higher

MORE INFO:
  https://github.com/muquit/gpw
EOF

# Create tar.gz archive
cd bin
tar czf "${RELEASE_NAME}.tar.gz" "${RELEASE_NAME}"

# Create zip archive
zip -r "${RELEASE_NAME}.zip" "${RELEASE_NAME}"
cd ..

# Copy standalone JAR to bin directory
cp "$JAR_FILE" "bin/"

echo ""
echo "✓ Release archives created successfully!"
echo ""
echo "Release files in bin/:"
echo "  bin/${RELEASE_NAME}.tar.gz  (for Linux/macOS)"
echo "  bin/${RELEASE_NAME}.zip     (for Windows)"
echo "  bin/gpw-${VERSION}.jar      (standalone)"
echo ""
echo "Ready to upload to GitHub releases!"
