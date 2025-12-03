# Mo-Lights Tournament System

A comprehensive Swiss-style tournament management system implemented in Java.

## 📋 Overview

This system manages Swiss-format tournaments where participants are paired based on their current standings. It features status-based probability calculations for match outcomes and includes the Buchholz tiebreaker system.

## 🎯 Features

- ✅ Swiss tournament pairing system
- ✅ Participant status levels (LOW, MEDIUM, HIGH)
- ✅ Status-based match result probabilities
- ✅ Automatic and manual result entry
- ✅ Buchholz tiebreaker system
- ✅ Tournament save/load functionality
- ✅ Detailed standings and match history
- ✅ BYE handling for odd number of participants

## 📊 System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Main Menu                             │
│  (User Interface - Main.java)                               │
└──────────────────┬──────────────────────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────────────────────┐
│                    Tournament Manager                        │
│  (Tournament.java)                                          │
│  - Participant Management                                    │
│  - Round Generation (Swiss Pairing)                         │
│  - Match Management                                          │
│  - Standings Calculation                                     │
│  - File I/O Operations                                       │
└───────┬──────────────────────┬──────────────────────────────┘
        │                      │
        ▼                      ▼
┌──────────────────┐    ┌──────────────────┐
│  Participant     │    │      Match       │
│  (Participant.java)   │   (Match.java)   │
│                  │    │                  │
│ - ID             │    │ - Player1        │
│ - Name           │    │ - Player2        │
│ - Score          │    │ - Result         │
│ - Status         │    │ - Round Number   │
│ - Statistics     │    │ - BYE handling   │
│ - Opponents      │    │ - Probability    │
└──────┬───────────┘    └──────────────────┘
       │
       ▼
┌──────────────────┐
│ ParticipantStatus│
│ (Enum)           │
│ - LOW            │
│ - MEDIUM         │
│ - HIGH           │
└──────────────────┘
```

## 🔄 Tournament Flow

```
START
  │
  ▼
┌─────────────────────┐
│ Create Tournament   │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ Add Participants    │
│ (Name + Status)     │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ Start Tournament    │
│ (Calculate Rounds)  │
└──────────┬──────────┘
           │
           ▼
    ╔═════════════════╗
    ║   Round Loop    ║
    ╚══════┬══════════╝
           │
           ▼
┌─────────────────────┐
│ Generate Round      │
│ (Swiss Pairing)     │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ Enter Results       │
│ (Auto/Manual)       │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ Update Standings    │
│ (Score + Buchholz)  │
└──────────┬──────────┘
           │
           ▼
    ┌─────────────┐
    │ More Rounds?├─── YES ──┐
    └──────┬──────┘           │
           │ NO               │
           │            ╔═════╧═════╗
           │            ║   LOOP    ║
           │            ║   BACK    ║
           │            ╚═════╤═════╝
           │                  │
           └──────────────────┘
           │
           ▼
┌─────────────────────┐
│ Final Standings     │
│ (Winner Declared)   │
└─────────────────────┘
  │
  ▼
END
```

## 🎲 Match Probability System

The system uses status-based probabilities for auto-generating results:

```
┌────────────────────────────────────────────────────────┐
│              Match Outcome Probabilities                │
├────────────────────────────────────────────────────────┤
│                                                         │
│  SAME STATUS (LOW-LOW, MED-MED, HIGH-HIGH):           │
│  ┌──────────┬──────────┬──────────┐                   │
│  │ Player 1 │ Player 2 │   Draw   │                   │
│  │   45%    │   45%    │   10%    │                   │
│  └──────────┴──────────┴──────────┘                   │
│                                                         │
│  HIGH vs LOW:                                          │
│  ┌──────────┬──────────┬──────────┐                   │
│  │   HIGH   │   LOW    │   Draw   │                   │
│  │   55%    │   30%    │   15%    │                   │
│  └──────────┴──────────┴──────────┘                   │
│                                                         │
│  HIGH vs MEDIUM:                                       │
│  ┌──────────┬──────────┬──────────┐                   │
│  │   HIGH   │  MEDIUM  │   Draw   │                   │
│  │   50%    │   35%    │   15%    │                   │
│  └──────────┴──────────┴──────────┘                   │
│                                                         │
│  MEDIUM vs LOW:                                        │
│  ┌──────────┬──────────┬──────────┐                   │
│  │  MEDIUM  │   LOW    │   Draw   │                   │
│  │   50%    │   35%    │   15%    │                   │
│  └──────────┴──────────┴──────────┘                   │
│                                                         │
└────────────────────────────────────────────────────────┘
```

## 🏆 Tiebreaker System (Buchholz)

When participants have equal scores, the **Buchholz system** is used:

```
Buchholz Score = Sum of all opponents' scores

Example:
  Player A (3 pts) opponents: [2.5, 3.0, 2.0] → Buchholz = 7.5
  Player B (3 pts) opponents: [1.5, 2.0, 1.0] → Buchholz = 4.5
  
  Ranking: Player A > Player B (fought stronger opponents)
```

## 🚀 Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher

### Compilation

```bash
javac *.java
```

### Running

```bash
java Main
```

## 📖 Usage Guide

### 1. Create a Tournament

```
Menu → 1. Create New Tournament
Enter tournament name: My Tournament
```

### 2. Add Participants

```
Menu → 2. Add Participants
Enter name: Player1
Select status:
  1. LOW
  2. MEDIUM
  3. HIGH
Enter status (1-3): 2
```

### 3. Start Tournament

```
Menu → 3. Start Tournament
(System calculates number of rounds)
```

### 4. Generate and Play Rounds

```
Menu → 4. Generate Next Round
(Pairs players using Swiss system)

Menu → 5. Enter Round Results
Choose:
  1. Auto-generate (uses probability system)
  2. Manual entry
```

### 5. View Standings

```
Menu → 6. View Standings
Displays: Rank, Score, Status, W-D-L, Buchholz
```

### 6. Save/Load Tournament

```
Menu → 8. Save Tournament
Menu → 9. Load Tournament
```

## 📁 File Structure

```
Mo-Lights-TournamentSystem/
│
├── Main.java                  # Entry point and UI
├── Tournament.java            # Tournament management logic
├── Participant.java           # Participant data and methods
├── ParticipantStatus.java     # Status enum (LOW/MEDIUM/HIGH)
├── Match.java                 # Match logic and probability
└── README.md                  # This file
```

## 🔧 Class Descriptions

### Main.java
- User interface and menu system
- Handles user input and command routing

### Tournament.java
- Swiss pairing algorithm
- Round generation and management
- Standings calculation
- File I/O for persistence

### Participant.java
- Stores participant information
- Tracks scores, wins, draws, losses
- Maintains opponent history
- Calculates Buchholz score

### Match.java
- Represents a single match
- Status-based probability calculations
- Result management
- BYE match handling

### ParticipantStatus.java
- Enum defining skill levels
- Used for probability calculations

## 🎓 Swiss System Pairing Rules

1. **First Round**: Random or by seeding
2. **Subsequent Rounds**:
   - Participants grouped by score
   - Highest vs 2nd highest in each group
   - Avoid repeat pairings when possible
   - Odd participant receives BYE (1 point, once per tournament)

## 💾 File Format

Tournament saves include:
- Tournament metadata (name, rounds, date)
- Participant data (ID, name, score, status, statistics)
- Match history (all rounds and results)

## 🤝 Contributing

Feel free to fork this project and submit pull requests for improvements!

## 📝 License

This project is open source and available for educational purposes.

## 👥 Authors

- Development Team: Mo-Lights Tournament System

---

**Note**: This system is designed for managing Swiss-style tournaments for games, chess, esports, or any competitive event requiring fair pairing and ranking systems.

