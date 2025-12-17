# Build Instructions

## Quick Start (Without Maven)

If you don't have Maven installed, you can still compile and use the library:

### Compile the Library

```bash
cd Mo-Lights-TournamentSystem

# Compile the core library
javac -d bin src/main/java/com/molights/tournament/*.java

# Compile the demo application
javac -d bin -cp bin src/main/java/com/molights/tournament/demo/*.java
```

### Run the Demo

```bash
java -cp bin com.molights.tournament.demo.Main
```

### Create a JAR manually

```bash
# Create library JAR
cd bin
jar cvf ../tournament-system.jar com/molights/tournament/*.class

# Create demo JAR with manifest
cd ..
echo "Main-Class: com.molights.tournament.demo.Main" > manifest.txt
cd bin
jar cvfm ../tournament-system-demo.jar ../manifest.txt com/molights/tournament/*.class com/molights/tournament/demo/*.class
cd ..

# Run the demo JAR
java -jar tournament-system-demo.jar
```

## With Maven (Recommended)

### Install Maven

**macOS:**
```bash
brew install maven
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt-get update
sudo apt-get install maven
```

**Windows:**
Download from https://maven.apache.org/download.cgi and add to PATH

### Build Commands

```bash
cd Mo-Lights-TournamentSystem

# Clean previous builds
mvn clean

# Compile only
mvn compile

# Create all JARs (library + demo + sources + javadoc)
mvn package

# Install to local Maven repository
mvn install
```

### Output Files (in `target/` directory)

After `mvn package`:
- `tournament-system-1.0.0.jar` - Core library (use this in other projects)
- `tournament-system-1.0.0-demo.jar` - Executable demo application
- `tournament-system-1.0.0-sources.jar` - Source code
- `tournament-system-1.0.0-javadoc.jar` - API documentation

### Run the Demo

```bash
java -jar target/tournament-system-1.0.0-demo.jar
```

## Using in Your Project

### Option 1: Copy Source Files

Copy the entire package to your project:
```
src/main/java/com/molights/tournament/
├── Tournament.java
├── Match.java
├── Participant.java
└── ParticipantStatus.java
```

Then import in your code:
```java
import com.molights.tournament.*;
```

### Option 2: Use the JAR

Add `tournament-system-1.0.0.jar` to your project's classpath:

```bash
javac -cp tournament-system-1.0.0.jar YourApp.java
java -cp .:tournament-system-1.0.0.jar YourApp
```

### Option 3: Maven Dependency (after mvn install)

Add to your `pom.xml`:
```xml
<dependency>
    <groupId>com.molights</groupId>
    <artifactId>tournament-system</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Compile and Run the Example

```bash
# Compile
javac -cp src/main/java Example.java

# Run
java -cp .:src/main/java Example
```

## Troubleshooting

### "mvn: command not found"
Maven is not installed. Use the manual compilation method above or install Maven.

### "package com.molights.tournament does not exist"
Make sure you're compiling from the correct directory and the source files are in the right package structure.

### Class files in wrong location
Use the `-d` flag with `javac` to specify the output directory:
```bash
javac -d bin src/main/java/com/molights/tournament/*.java
```

## Next Steps

1. Read `LIBRARY_USAGE.md` for detailed API documentation
2. Check `Example.java` for code examples
3. Run the demo application to see it in action
4. Integrate into your own project!

---

**Note:** The old files (`Main.java`, `Tournament.java`, etc.) in the root directory are now deprecated. Use the files in `src/main/java/com/molights/tournament/` instead.

