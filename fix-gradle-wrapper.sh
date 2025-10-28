#!/bin/bash

# Script to fix Gradle wrapper JAR file issue
# This script downloads the correct gradle-wrapper.jar file

echo "🔧 Fixing Gradle Wrapper..."
echo ""

# Navigate to project root
cd "$(dirname "$0")"

# Check if gradle wrapper directory exists
if [ ! -d "gradle/wrapper" ]; then
    echo "❌ Error: gradle/wrapper directory not found!"
    exit 1
fi

# Remove the incorrect .txt file if it exists
if [ -f "gradle/wrapper/gradle-wrapper.jar.txt" ]; then
    echo "📝 Removing incorrect gradle-wrapper.jar.txt..."
    rm gradle/wrapper/gradle-wrapper.jar.txt
fi

# Download the correct gradle-wrapper.jar
echo "⬇️  Downloading gradle-wrapper.jar..."
curl -L -o gradle/wrapper/gradle-wrapper.jar \
    https://raw.githubusercontent.com/gradle/gradle/master/gradle/wrapper/gradle-wrapper.jar

# Check if download was successful
if [ -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "✅ gradle-wrapper.jar downloaded successfully!"
    
    # Verify it's a JAR file
    if file gradle/wrapper/gradle-wrapper.jar | grep -q "Java archive"; then
        echo "✅ Verified: File is a valid JAR archive"
    else
        echo "⚠️  Warning: File may not be a valid JAR archive"
    fi
else
    echo "❌ Error: Failed to download gradle-wrapper.jar"
    exit 1
fi

# Make gradlew executable
echo "🔐 Making gradlew executable..."
chmod +x gradlew

# Test the wrapper
echo ""
echo "🧪 Testing Gradle wrapper..."
if ./gradlew --version > /dev/null 2>&1; then
    echo "✅ Gradle wrapper is working!"
    echo ""
    ./gradlew --version
else
    echo "❌ Gradle wrapper test failed"
    echo "Try running: ./gradlew --version"
    exit 1
fi

echo ""
echo "✨ Gradle wrapper fixed successfully!"
echo ""
echo "Next steps:"
echo "1. Test locally: ./gradlew build"
echo "2. Commit changes: git add gradle/wrapper/gradle-wrapper.jar"
echo "3. Push to GitHub: git push"
echo ""
