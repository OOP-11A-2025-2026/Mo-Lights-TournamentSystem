# Quick Reference Card

## 🚀 Quick Start

### Build & Run (with Maven)
```bash
mvn clean package
java -jar target/tournament-system-1.0.0-demo.jar
```

### Build & Run (without Maven)
```bash
javac -d bin src/main/java/com/molights/tournament/*.java
javac -d bin -cp bin src/main/java/com/molights/tournament/demo/*.java
java -cp bin com.molights.tournament.demo.Main
```

## 📚 Core API

### Create Tournament
```java
Tournament t = new Tournament("Tournament Name");
```

### Add Participants
```java
t.addParticipant(new Participant(1, "Name", ParticipantStatus.HIGH));
// Status: LOW, MEDIUM, HIGH
```

### Start & Run
```java
t.startTournament();              // Initialize
t.generateNextRound();            // Create pairings
t.autoGenerateRoundResults();     // Auto results
t.displayStandings();             // Show standings
```

### Manual Results
```java
List<Match> matches = t.getRoundMatches(1);
matches.get(0).setResult(Match.MatchResult.WIN_PLAYER1);
// Results: WIN_PLAYER1, WIN_PLAYER2, DRAW
```

### Get Data
```java
List<Participant> standings = t.getStandings();
List<Match> matches = t.getMatches();
int currentRound = t.getCurrentRound();
boolean done = t.isComplete();
```

### Save/Load
```java
t.saveToFile("tournament.txt");
Tournament loaded = Tournament.loadFromFile("tournament.txt");
```

## 📦 Import Statement

```java
import com.molights.tournament.*;
```

## 🎯 Complete Example

```java
import com.molights.tournament.*;

public class QuickDemo {
    public static void main(String[] args) {
        Tournament t = new Tournament("Quick Demo");
        
        t.addParticipant(new Participant(1, "Alice", ParticipantStatus.HIGH));
        t.addParticipant(new Participant(2, "Bob", ParticipantStatus.LOW));
        
        t.startTournament();
        
        while (!t.isComplete()) {
            t.generateNextRound();
            t.autoGenerateRoundResults();
        }
        
        t.displayStandings();
    }
}
```

## 🔧 Maven Commands

| Command | Purpose |
|---------|---------|
| `mvn clean` | Remove previous builds |
| `mvn compile` | Compile source code |
| `mvn package` | Create JAR files |
| `mvn install` | Install to local repo |
| `mvn test` | Run unit tests |

## 📁 Key Files

| File | Purpose |
|------|---------|
| `pom.xml` | Maven configuration |
| `src/main/java/com/molights/tournament/` | Core library |
| `src/main/java/com/molights/tournament/demo/` | Demo app |
| `target/*.jar` | Compiled JARs |
| `LIBRARY_USAGE.md` | Full API docs |
| `Example.java` | Code examples |

## 🎲 Probability System

| Matchup | Player 1 | Player 2 | Draw |
|---------|----------|----------|------|
| Same Status | 45% | 45% | 10% |
| HIGH vs LOW | 55% | 30% | 15% |
| HIGH vs MEDIUM | 50% | 35% | 15% |
| MEDIUM vs LOW | 50% | 35% | 15% |

## 🏆 Tiebreaker Order

1. Score (points)
2. Buchholz (sum of opponents' scores)
3. Number of wins
4. ID (for consistency)

## 📖 Documentation Files

- `README.md` - Project overview
- `LIBRARY_USAGE.md` - Detailed API guide
- `BUILD_INSTRUCTIONS.md` - How to build
- `MIGRATION_SUMMARY.md` - What changed
- `QUICK_REFERENCE.md` - This file
- `Example.java` - Working code samples

## 💡 Common Tasks

### Add to Your Project (Maven)
```xml
<dependency>
    <groupId>com.molights</groupId>
    <artifactId>tournament-system</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Add to Your Project (JAR)
```bash
javac -cp tournament-system-1.0.0.jar YourApp.java
java -cp .:tournament-system-1.0.0.jar YourApp
```

### Generate JavaDoc
```bash
mvn javadoc:javadoc
# Output: target/site/apidocs/index.html
```

## 🐛 Troubleshooting

| Problem | Solution |
|---------|----------|
| "mvn: command not found" | Install Maven or use manual compilation |
| "package does not exist" | Check import: `import com.molights.tournament.*;` |
| "Main class not found" | Use full path: `com.molights.tournament.demo.Main` |
| Build fails | Run `mvn clean` first |

## 📞 Need Help?

1. Check `LIBRARY_USAGE.md` for detailed docs
2. See `Example.java` for working code
3. Read `BUILD_INSTRUCTIONS.md` for build help
4. Review `README.md` for overview

---

**Quick Tip:** Start with `Example.java` to see the library in action!

