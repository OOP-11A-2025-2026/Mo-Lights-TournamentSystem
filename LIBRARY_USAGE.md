# Mo-Lights Tournament System - Library Usage Guide

## 🎉 Your Tournament System is Now a Library!

The Mo-Lights Tournament System has been restructured as a professional Java library with proper Maven support.

## 📦 What Was Created

### Project Structure

```
Mo-Lights-TournamentSystem/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── molights/
│                   └── tournament/          # 📚 CORE LIBRARY
│                       ├── Tournament.java
│                       ├── Match.java
│                       ├── Participant.java
│                       ├── ParticipantStatus.java
│                       └── demo/            # 🎮 DEMO APP (separate)
│                           └── Main.java
├── pom.xml                                  # Maven configuration
├── .gitignore                               # Updated for Maven
└── README.md                                # Updated documentation
```

### Build Artifacts

When you run `mvn clean install`, Maven will create:

1. **`tournament-system-1.0.0.jar`** - Core library (excludes demo)
2. **`tournament-system-1.0.0-demo.jar`** - Executable demo with Main class
3. **`tournament-system-1.0.0-sources.jar`** - Source code
4. **`tournament-system-1.0.0-javadoc.jar`** - API documentation

## 🚀 How to Build

### Prerequisites

Install Maven if you haven't already:

**macOS:**
```bash
brew install maven
```

**Linux:**
```bash
sudo apt-get install maven
```

**Windows:**
Download from https://maven.apache.org/download.cgi

### Build Commands

```bash
cd Mo-Lights-TournamentSystem

# Clean and compile
mvn clean compile

# Create JARs
mvn clean package

# Install to local Maven repository
mvn clean install
```

## 💻 Using the Library

### Option 1: As a Maven Dependency

After running `mvn install`, add to your project's `pom.xml`:

```xml
<dependency>
    <groupId>com.molights</groupId>
    <artifactId>tournament-system</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Option 2: Direct JAR Import

Add `target/tournament-system-1.0.0.jar` to your project's classpath.

### Option 3: Copy Source Files

Copy the package `com.molights.tournament` to your source directory.

## 📝 Code Examples

### Basic Tournament

```java
import com.molights.tournament.*;

public class MyTournament {
    public static void main(String[] args) {
        // Create tournament
        Tournament tournament = new Tournament("Spring Championship");
        
        // Add participants
        tournament.addParticipant(new Participant(1, "Alice", ParticipantStatus.HIGH));
        tournament.addParticipant(new Participant(2, "Bob", ParticipantStatus.MEDIUM));
        tournament.addParticipant(new Participant(3, "Charlie", ParticipantStatus.MEDIUM));
        tournament.addParticipant(new Participant(4, "Diana", ParticipantStatus.LOW));
        
        // Start tournament
        tournament.startTournament();
        
        // Run all rounds
        while (!tournament.isComplete()) {
            tournament.generateNextRound();
            tournament.autoGenerateRoundResults();
        }
        
        // Display results
        tournament.displayStandings();
    }
}
```

### Manual Result Entry

```java
import com.molights.tournament.*;
import java.util.List;

Tournament tournament = new Tournament("My Tournament");
// ... add participants and start ...

tournament.generateNextRound();

// Get matches for current round
List<Match> matches = tournament.getRoundMatches(tournament.getCurrentRound());

// Manually set results
for (Match match : matches) {
    if (!match.isBye()) {
        match.setResult(Match.MatchResult.WIN_PLAYER1);
    }
}

tournament.displayStandings();
```

### Custom Integration

```java
import com.molights.tournament.*;
import java.util.List;

Tournament tournament = new Tournament("Chess Club Weekly");

// Add participants from your system
for (Player player : myPlayerList) {
    ParticipantStatus status = mapPlayerRating(player.getRating());
    tournament.addParticipant(new Participant(
        player.getId(), 
        player.getName(), 
        status
    ));
}

// Start and run tournament
tournament.startTournament();

for (int round = 0; round < tournament.getTotalRounds(); round++) {
    tournament.generateNextRound();
    
    // Get pairings for your system
    List<Match> matches = tournament.getRoundMatches(round + 1);
    
    // Your custom logic here...
    displayPairingsInYourUI(matches);
    
    // Collect results from your UI
    collectResultsFromYourUI(matches);
}

// Get final standings
List<Participant> standings = tournament.getStandings();
Participant winner = standings.get(0);
System.out.println("Champion: " + winner.getName());
```

### Save and Load

```java
import com.molights.tournament.*;
import java.io.IOException;

// Save tournament
try {
    tournament.saveToFile("tournament-data.txt");
} catch (IOException e) {
    System.err.println("Failed to save: " + e.getMessage());
}

// Load tournament
try {
    Tournament loaded = Tournament.loadFromFile("tournament-data.txt");
    loaded.displayStandings();
} catch (IOException e) {
    System.err.println("Failed to load: " + e.getMessage());
}
```

## 🎮 Running the Demo Application

```bash
# After building with maven
java -jar target/tournament-system-1.0.0-demo.jar

# Or with Maven
mvn compile exec:java -Dexec.mainClass="com.molights.tournament.demo.Main"
```

## 📚 API Overview

### Tournament Class
- `addParticipant(Participant p)` - Add participant before start
- `startTournament()` - Initialize tournament with calculated rounds
- `generateNextRound()` - Create Swiss pairings for next round
- `autoGenerateRoundResults()` - Auto-generate results with probabilities
- `getRoundMatches(int round)` - Get matches for specific round
- `getStandings()` - Get sorted list of participants
- `saveToFile(String filename)` - Save tournament state
- `loadFromFile(String filename)` - Load tournament (static)
- `isComplete()` - Check if all rounds finished

### Participant Class
- `new Participant(int id, String name, ParticipantStatus status)`
- `getScore()` - Get current score
- `getOpponentsSumScore()` - Get Buchholz tiebreaker score
- `getWinCount()`, `getDrawCount()`, `getLossCount()` - Get statistics
- `hasPlayedWith(Participant p)` - Check if already played opponent

### Match Class
- `new Match(Participant p1, Participant p2, int round)` - Regular match
- `new Match(Participant p, int round)` - BYE match
- `generateRandomResult()` - Auto-generate based on status probabilities
- `setResult(MatchResult result)` - Manually set result
- `isPlayed()` - Check if match has result
- Result types: `WIN_PLAYER1`, `WIN_PLAYER2`, `DRAW`, `NOT_PLAYED`

### ParticipantStatus Enum
- `LOW` - Lower skill level (30-35% win chance vs higher)
- `MEDIUM` - Medium skill level (35-50% win chance)
- `HIGH` - High skill level (50-55% win chance vs lower)

## 🎯 Integration Examples

### With Web Framework (e.g., Spring Boot)

```java
@RestController
@RequestMapping("/api/tournament")
public class TournamentController {
    
    private Tournament tournament;
    
    @PostMapping("/create")
    public ResponseEntity<String> createTournament(@RequestBody TournamentRequest req) {
        tournament = new Tournament(req.getName());
        return ResponseEntity.ok("Tournament created");
    }
    
    @PostMapping("/participants")
    public ResponseEntity<String> addParticipant(@RequestBody ParticipantRequest req) {
        Participant p = new Participant(req.getId(), req.getName(), req.getStatus());
        tournament.addParticipant(p);
        return ResponseEntity.ok("Participant added");
    }
    
    @GetMapping("/standings")
    public List<Participant> getStandings() {
        return tournament.getStandings();
    }
}
```

### With JavaFX/Swing GUI

```java
public class TournamentGUI extends Application {
    private Tournament tournament;
    
    @Override
    public void start(Stage primaryStage) {
        tournament = new Tournament("GUI Tournament");
        
        Button generateRoundBtn = new Button("Generate Round");
        generateRoundBtn.setOnAction(e -> {
            tournament.generateNextRound();
            updateMatchDisplay();
        });
        
        // ... rest of your GUI code
    }
}
```

## 🔧 Customization

The library is designed to be extended. You can:

1. **Extend Tournament** - Add custom pairing logic
2. **Extend Participant** - Add custom player attributes
3. **Extend Match** - Add custom probability calculations
4. **Implement custom save/load** - Use JSON, XML, database, etc.

## 📄 License

Open source - available for educational and commercial use.

## 🤝 Contributing

To contribute:
1. Fork the repository
2. Make your changes
3. Submit a pull request

---

**Congratulations!** Your tournament system is now a professional, reusable library! 🎉

