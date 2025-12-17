#!/bin/bash
# Build script for Mo-Lights Tournament System
# Creates a JAR file you can use in other projects

set -e  # Exit on error

echo "🔨 Building Mo-Lights Tournament System..."

# Create output directory
mkdir -p target/classes

# Compile all source files
echo "📦 Compiling source files..."
javac -d target/classes src/main/java/com/molights/tournament/*.java

# Create JAR
echo "📦 Creating JAR file..."
cd target/classes
jar cvf ../tournament-system-1.0.0.jar com/molights/tournament/*.class
cd ../..

echo ""
echo "✅ Build successful!"
echo ""
echo "📦 JAR created: target/tournament-system-1.0.0.jar"
echo ""
echo "🎯 To use in other projects:"
echo "   1. Copy to your project: cp target/tournament-system-1.0.0.jar /path/to/your/project/libs/"
echo "   2. Compile with it: javac -cp \"libs/tournament-system-1.0.0.jar\" YourFile.java"
echo "   3. Run with it: java -cp \"libs/tournament-system-1.0.0.jar:.\" YourMainClass"
echo ""
echo "📚 See DEPLOYMENT_GUIDE.md for more options"


