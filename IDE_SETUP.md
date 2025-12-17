# IDE Setup Guide

## Resolving "Package Does Not Match" Linter Errors

If you're seeing linter errors like "The declared package does not match the expected package", this is because your IDE doesn't recognize the Maven project structure yet. These are **false positives** - the code is correct!

## Fix for Different IDEs

### IntelliJ IDEA

1. **Open as Maven Project:**
   - File → Open
   - Select the `Mo-Lights-TournamentSystem` folder
   - Choose "Open as Project"
   - IntelliJ should auto-detect the `pom.xml` and configure itself

2. **Manual Configuration (if needed):**
   - Right-click on `pom.xml` → "Add as Maven Project"
   - File → Project Structure → Modules
   - Mark `src/main/java` as "Sources"
   - Mark `src/test/java` as "Tests"

3. **Reimport Maven:**
   - Right-click `pom.xml` → Maven → Reload Project

### Eclipse

1. **Import Maven Project:**
   - File → Import → Maven → Existing Maven Projects
   - Select the `Mo-Lights-TournamentSystem` folder
   - Finish

2. **Update Project:**
   - Right-click project → Maven → Update Project
   - Check "Force Update of Snapshots/Releases"
   - OK

### VS Code

1. **Install Extensions:**
   - Java Extension Pack (Microsoft)
   - Maven for Java

2. **Open Folder:**
   - File → Open Folder
   - Select `Mo-Lights-TournamentSystem`

3. **Reload Java Projects:**
   - Ctrl+Shift+P (or Cmd+Shift+P on Mac)
   - Type "Java: Clean Java Language Server Workspace"
   - Reload window

### Cursor / VS Code Fork

1. **Open the project folder:**
   ```bash
   cd Mo-Lights-TournamentSystem
   cursor .  # or code .
   ```

2. **Ensure Java extensions are installed:**
   - Extension Pack for Java
   - Maven for Java

3. **Reload window:**
   - Cmd/Ctrl + Shift + P → "Developer: Reload Window"

## Manual Compilation (Bypass IDE)

If IDE issues persist, you can always compile manually:

### With Maven:
```bash
cd Mo-Lights-TournamentSystem
mvn clean compile
```

This will show **real** compilation errors (if any). If Maven compiles successfully, the IDE errors are just configuration issues.

### Without Maven:
```bash
cd Mo-Lights-TournamentSystem
javac -d target/classes src/main/java/com/molights/tournament/*.java
javac -d target/classes -cp target/classes src/main/java/com/molights/tournament/demo/*.java
```

## Verify Everything Works

Run this test to verify the code is correct:

```bash
# Compile
javac -d bin src/main/java/com/molights/tournament/*.java
javac -d bin -cp bin src/main/java/com/molights/tournament/demo/*.java

# Run demo
java -cp bin com.molights.tournament.demo.Main
```

If this works, **there are no actual errors** - just IDE configuration issues.

## Common Issues

### "Cannot resolve symbol" errors

**Cause:** IDE hasn't indexed the Maven source directories.

**Fix:** 
- IntelliJ: File → Invalidate Caches → Invalidate and Restart
- Eclipse: Project → Clean
- VS Code: Reload Java Projects (Command Palette)

### "Package does not match expected package"

**Cause:** IDE thinks files are in the root, not in `src/main/java`.

**Fix:** Mark `src/main/java` as the source root in your IDE settings.

### "java: cannot find symbol"

**Cause:** Compilation classpath issue.

**Fix:** Run `mvn clean compile` to verify actual compilation works.

## Expected Project Structure

Your IDE should recognize this structure:

```
Mo-Lights-TournamentSystem/
├── src/
│   └── main/
│       └── java/               ← SOURCE ROOT
│           └── com/
│               └── molights/
│                   └── tournament/
│                       ├── Tournament.java
│                       ├── Match.java
│                       ├── Participant.java
│                       ├── ParticipantStatus.java
│                       └── demo/
│                           └── Main.java
└── pom.xml
```

The key is that `src/main/java` must be marked as a **source root** in your IDE.

## Still Having Issues?

If you're still seeing errors after trying the above:

1. **Close the IDE completely**
2. **Delete IDE-specific folders:**
   ```bash
   rm -rf .idea/      # IntelliJ
   rm -rf .classpath .project .settings/  # Eclipse
   rm -rf .vscode/    # VS Code (if you want fresh settings)
   ```
3. **Clean Maven:**
   ```bash
   mvn clean
   ```
4. **Reopen the project** and let the IDE re-import it

## Verification Command

Run this to prove the code compiles correctly:

```bash
mvn clean verify
```

If this succeeds with `BUILD SUCCESS`, your code is perfect - any remaining errors are purely IDE configuration issues that don't affect the actual build.

---

**Bottom Line:** The linter errors you're seeing are **NOT real errors**. They're just IDE configuration warnings. The code is structurally correct and will compile fine with Maven or javac.

