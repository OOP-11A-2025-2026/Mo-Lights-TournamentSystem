# Deployment Guide: Using Tournament System in Other Projects

## 📦 Step 1: Move to Separate Repository

### Create New Repository on GitHub

```bash
# 1. Create new repo on GitHub: "Mo-Lights-TournamentSystem" (don't initialize)

# 2. Navigate to the tournament system folder
cd /Users/mark/Desktop/GitHub/Mo-Lights-Chess/Mo-Lights-TournamentSystem

# 3. Initialize git (if not already)
git init

# 4. Add all files
git add .

# 5. Commit
git commit -m "Initial commit: Tournament System Library v1.0.0"

# 6. Add remote (replace YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/Mo-Lights-TournamentSystem.git

# 7. Push to GitHub
git branch -M main
git push -u origin main

# 8. Create a release tag
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

## 🔗 Step 2: Use in Other Projects

You have **4 options** to include this library in other projects:

---

## Option 1: Git Submodule (Recommended for Active Development)

### Add as Submodule to Your Chess Project

```bash
# In your main chess project
cd /Users/mark/Desktop/GitHub/Mo-Lights-Chess

# Add tournament system as submodule
git submodule add https://github.com/YOUR_USERNAME/Mo-Lights-TournamentSystem.git libs/tournament-system

# Update submodule
git submodule update --init --recursive

# Commit the submodule
git add .gitmodules libs/tournament-system
git commit -m "Add tournament system as submodule"
```

### Use the Library

**Option A: Copy source files to your project:**
```bash
cp -r libs/tournament-system/src/main/java/com/molights/tournament src/main/java/com/molights/
```

**Option B: Add to classpath:**
```bash
# Compile the tournament system first
cd libs/tournament-system
javac -d target/classes src/main/java/com/molights/tournament/*.java
cd ../..

# Then compile your project with it
javac -cp "libs/tournament-system/target/classes:." -d bin src/**/*.java
```

**Option C: Maven dependency (if using Maven):**

Add to your `pom.xml`:
```xml
<dependencies>
    <dependency>
        <groupId>com.molights</groupId>
        <artifactId>tournament-system</artifactId>
        <version>1.0.0</version>
        <scope>system</scope>
        <systemPath>${project.basedir}/libs/tournament-system/target/tournament-system-1.0.0.jar</systemPath>
    </dependency>
</dependencies>
```

---

## Option 2: Local Maven Install (Best for Maven Projects)

### Install to Local Maven Repository

```bash
# In tournament system directory
cd Mo-Lights-TournamentSystem

# Install Maven (if not installed)
brew install maven  # macOS

# Build and install
mvn clean install
```

This installs the JAR to your local Maven repository: `~/.m2/repository/`

### Use in Any Maven Project

Add to your project's `pom.xml`:
```xml
<dependencies>
    <dependency>
        <groupId>com.molights</groupId>
        <artifactId>tournament-system</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### Update the Library

```bash
# In tournament system directory
git pull
mvn clean install

# Your other projects automatically get the update
```

---

## Option 3: Copy JAR File (Simple, Portable)

### Build the JAR

```bash
cd Mo-Lights-TournamentSystem

# Option A: With Maven
mvn clean package
# Creates: target/tournament-system-1.0.0.jar

# Option B: Without Maven
javac -d bin src/main/java/com/molights/tournament/*.java
cd bin
jar cvf ../tournament-system-1.0.0.jar com/molights/tournament/*.class
cd ..
```

### Copy to Your Projects

```bash
# Create a libs folder in your chess project
mkdir -p /Users/mark/Desktop/GitHub/Mo-Lights-Chess/libs

# Copy the JAR
cp target/tournament-system-1.0.0.jar /Users/mark/Desktop/GitHub/Mo-Lights-Chess/libs/

# Use in compilation
javac -cp "libs/tournament-system-1.0.0.jar:." -d bin src/**/*.java

# Use in execution
java -cp "libs/tournament-system-1.0.0.jar:bin" YourMainClass
```

### Maven Project Using JAR

Add to `pom.xml`:
```xml
<dependencies>
    <dependency>
        <groupId>com.molights</groupId>
        <artifactId>tournament-system</artifactId>
        <version>1.0.0</version>
        <scope>system</scope>
        <systemPath>${project.basedir}/libs/tournament-system-1.0.0.jar</systemPath>
    </dependency>
</dependencies>
```

---

## Option 4: Copy Source Files (Quick & Simple)

### Copy the Package

```bash
# From tournament system
cd Mo-Lights-TournamentSystem/src/main/java

# To your chess project (adjust path as needed)
cp -r com/molights/tournament /Users/mark/Desktop/GitHub/Mo-Lights-Chess/src/main/java/com/molights/
```

### Use Directly

```java
import com.molights.tournament.*;

Tournament t = new Tournament("My Tournament");
// ... use it
```

**Pros:** Simple, no dependencies  
**Cons:** Need to manually update when library changes

---

## 📋 Comparison Table

| Method | Pros | Cons | Best For |
|--------|------|------|----------|
| **Git Submodule** | Always in sync, version control | Requires git commands | Active development |
| **Local Maven** | Clean, automatic updates | Requires Maven | Maven projects |
| **Copy JAR** | Portable, simple | Manual updates | Distribution |
| **Copy Source** | No tools needed | No versioning, manual sync | Quick prototypes |

---

## 🎯 Recommended Setup for Your Chess Project

### If Using Maven:

```bash
# 1. Install tournament system locally
cd Mo-Lights-TournamentSystem
mvn clean install

# 2. Add dependency to chess project pom.xml
```

```xml
<dependency>
    <groupId>com.molights</groupId>
    <artifactId>tournament-system</artifactId>
    <version>1.0.0</version>
</dependency>
```

### If NOT Using Maven:

```bash
# 1. Build JAR
cd Mo-Lights-TournamentSystem
mkdir -p target/classes
javac -d target/classes src/main/java/com/molights/tournament/*.java
cd target/classes
jar cvf ../tournament-system-1.0.0.jar com/
cd ../../..

# 2. Copy to chess project
mkdir -p /Users/mark/Desktop/GitHub/Mo-Lights-Chess/libs
cp Mo-Lights-TournamentSystem/target/tournament-system-1.0.0.jar /Users/mark/Desktop/GitHub/Mo-Lights-Chess/libs/

# 3. Use in chess project
javac -cp "libs/tournament-system-1.0.0.jar" -d bin src/**/*.java
```

---

## 📚 Example Integration in Chess Project

### Structure
```
Mo-Lights-Chess/
├── libs/
│   └── tournament-system-1.0.0.jar
├── src/
│   └── ChessTournament.java
└── bin/
```

### ChessTournament.java
```java
import com.molights.tournament.*;

public class ChessTournament {
    public static void main(String[] args) {
        // Create tournament
        Tournament tournament = new Tournament("Chess Club Championship");
        
        // Add chess players
        tournament.addParticipant(new Participant(1, "Magnus Carlsen", ParticipantStatus.HIGH));
        tournament.addParticipant(new Participant(2, "Hikaru Nakamura", ParticipantStatus.HIGH));
        tournament.addParticipant(new Participant(3, "Player3", ParticipantStatus.MEDIUM));
        tournament.addParticipant(new Participant(4, "Player4", ParticipantStatus.LOW));
        
        // Run tournament
        tournament.startTournament();
        
        while (!tournament.isComplete()) {
            tournament.generateNextRound();
            // Your chess game logic here
            tournament.autoGenerateRoundResults();
        }
        
        tournament.displayStandings();
    }
}
```

### Compile & Run
```bash
# Compile
javac -cp "libs/tournament-system-1.0.0.jar" -d bin src/ChessTournament.java

# Run
java -cp "libs/tournament-system-1.0.0.jar:bin" ChessTournament
```

---

## 🔄 Updating the Library

### When You Make Changes to Tournament System:

**Maven:**
```bash
cd Mo-Lights-TournamentSystem
git pull  # if using git
mvn clean install  # reinstall
# Your projects automatically see the update
```

**JAR Method:**
```bash
cd Mo-Lights-TournamentSystem
git pull
mvn clean package  # or manual jar creation
cp target/tournament-system-1.0.0.jar ../Mo-Lights-Chess/libs/
```

---

## 🌐 Optional: Publish to Maven Central

If you want to make it publicly available:

1. Create account at https://central.sonatype.org/
2. Add deployment configuration to `pom.xml`
3. Run `mvn deploy`
4. Then anyone can use:

```xml
<dependency>
    <groupId>com.molights</groupId>
    <artifactId>tournament-system</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## 🎯 Quick Commands Cheat Sheet

```bash
# Create separate repo
cd Mo-Lights-TournamentSystem
git init && git add . && git commit -m "Initial commit"
git remote add origin https://github.com/YOUR_USERNAME/Mo-Lights-TournamentSystem.git
git push -u origin main

# Install locally (Maven)
mvn clean install

# Build JAR (no Maven)
javac -d bin src/main/java/com/molights/tournament/*.java
cd bin && jar cvf ../tournament-system.jar com/ && cd ..

# Use in project
javac -cp "libs/tournament-system.jar" YourFile.java
java -cp "libs/tournament-system.jar:." YourMainClass
```

---

**Recommendation:** Use **Local Maven Install** if you're using Maven, or **Copy JAR** method if you're not. Both are clean and easy to update.


