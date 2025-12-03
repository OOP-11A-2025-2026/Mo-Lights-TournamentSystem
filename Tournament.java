import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tournament {
    
    private String name;
    private List<Participant> participants;
    private List<Match> matches;
    private int totalRounds;
    private int currentRound;
    
    public Tournament(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tournament name cannot be empty");
        }
        this.name = name;
        this.participants = new ArrayList<>();
        this.matches = new ArrayList<>();
        this.totalRounds = 0;
        this.currentRound = 0;
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public List<Participant> getParticipants() {
        return new ArrayList<>(participants);
    }
    
    public List<Match> getMatches() {
        return new ArrayList<>(matches);
    }
    
    public int getTotalRounds() {
        return totalRounds;
    }
    
    public int getCurrentRound() {
        return currentRound;
    }
    
    /**
     * Add a participant to the tournament
     * Can only add participants before the tournament starts
     */
    public void addParticipant(Participant participant) {
        if (participant == null) {
            throw new IllegalArgumentException("Participant cannot be null");
        }
        
        if (currentRound > 0) {
            throw new IllegalStateException("Cannot add participants after tournament has started");
        }
        
        // Check for duplicate ID
        for (Participant p : participants) {
            if (p.getId() == participant.getId()) {
                throw new IllegalArgumentException("Participant with ID " + participant.getId() + " already exists");
            }
        }
        
        participants.add(participant);
    }
    
    /**
     * Remove a participant from the tournament
     * Can only remove before tournament starts
     */
    public void removeParticipant(int participantId) {
        if (currentRound > 0) {
            throw new IllegalStateException("Cannot remove participants after tournament has started");
        }
        if (participantId <= 0) {
            throw new IllegalArgumentException("participantId must be positive");
        }

        boolean removed = participants.removeIf(p -> p.getId() == participantId);
        if (!removed) {
            throw new IllegalArgumentException("Participant with ID " + participantId + " not found");
        }
    }
    
    /**
     * Start the tournament by calculating the number of rounds
     */
    public void startTournament() {
        if (participants.size() < 2) {
            throw new IllegalStateException("Tournament needs at least 2 participants");
        }
        
        if (currentRound > 0) {
            throw new IllegalStateException("Tournament has already started");
        }
        
        // Calculate number of rounds: ceil(log2(n))
        totalRounds = (int) Math.ceil(Math.log(participants.size()) / Math.log(2));
        System.out.println("Tournament started with " + participants.size() + " participants and " + totalRounds + " rounds");
    }
    
    /**
     * Generate pairings for the next round using Swiss system
     */
    public void generateNextRound() {
        if (totalRounds == 0) {
            throw new IllegalStateException("Tournament has not been started. Call startTournament() first.");
        }
        
        if (currentRound >= totalRounds) {
            throw new IllegalStateException("All rounds have been completed");
        }
        
        currentRound++;
        System.out.println("\n=== Generating Round " + currentRound + " ===");
        
        List<Match> roundMatches = generateSwissPairings(currentRound);
        matches.addAll(roundMatches);
        
        System.out.println("Round " + currentRound + " pairings generated:");
        for (Match match : roundMatches) {
            if (match.isBye()) {
                System.out.println("  " + match.getPlayer1().getName() + " (BYE)");
            } else {
                System.out.println("  " + match.getPlayer1().getName() + " vs " + match.getPlayer2().getName());
            }
        }
    }
    
    /**
     * Full Swiss pairing algorithm
     * - Sort by score (and tiebreakers)
     * - Avoid repeat pairings
     * - Handle BYE for odd number of participants
     */
    private List<Match> generateSwissPairings(int round) {
        List<Match> roundMatches = new ArrayList<>();
        
        // Sort participants by current score (descending), then by Buchholz
        List<Participant> sortedParticipants = new ArrayList<>(participants);
        sortedParticipants.sort((p1, p2) -> {
            int scoreCompare = Double.compare(p2.getScore(), p1.getScore());
            if (scoreCompare != 0) return scoreCompare;
            return Double.compare(p2.getOpponentsSumScore(), p1.getOpponentsSumScore());
        });
        
        // Track which participants have been paired
        Set<Participant> paired = new HashSet<>();
        
        // Handle odd number of participants with BYE
        if (sortedParticipants.size() % 2 == 1) {
            // Find the lowest-ranked participant who hasn't had a BYE yet
            Participant byeParticipant = null;
            for (int i = sortedParticipants.size() - 1; i >= 0; i--) {
                Participant p = sortedParticipants.get(i);
                if (!p.getWasParticipantByed()) {
                    byeParticipant = p;
                    break;
                }
            }
            
            if (byeParticipant != null) {
                Match byeMatch = new Match(byeParticipant, round);
                byeParticipant.byeParticipant(); // Applies the BYE
                roundMatches.add(byeMatch);
                paired.add(byeParticipant);
            } else {
                // All participants have had a BYE, give it to the lowest-ranked anyway
                Participant p = sortedParticipants.get(sortedParticipants.size() - 1);
                Match byeMatch = new Match(p, round);
                p.addPoint(1.0);
                p.addWin();
                roundMatches.add(byeMatch);
                paired.add(p);
            }
        }
        
        // Pair remaining participants
        for (int i = 0; i < sortedParticipants.size(); i++) {
            Participant p1 = sortedParticipants.get(i);
            
            if (paired.contains(p1)) {
                continue;
            }
            
            // Try to find a suitable opponent
            Participant p2 = null;
            for (int j = i + 1; j < sortedParticipants.size(); j++) {
                Participant candidate = sortedParticipants.get(j);
                
                if (paired.contains(candidate)) {
                    continue;
                }
                
                // Check if they haven't played each other before
                if (!p1.hasPlayedWith(candidate)) {
                    p2 = candidate;
                    break;
                }
            }
            
            // If no unpaired opponent found who hasn't played before, pair with anyone available
            if (p2 == null) {
                for (int j = i + 1; j < sortedParticipants.size(); j++) {
                    Participant candidate = sortedParticipants.get(j);
                    if (!paired.contains(candidate)) {
                        p2 = candidate;
                        break;
                    }
                }
            }
            
            if (p2 != null) {
                Match match = new Match(p1, p2, round);
                roundMatches.add(match);
                paired.add(p1);
                paired.add(p2);
            }
        }
        
        return roundMatches;
    }
    
    /**
     * Get all matches from a specific round
     */
    public List<Match> getRoundMatches(int round) {
        if (round <= 0) {
            throw new IllegalArgumentException("Round number must be positive");
        }

        if (round > currentRound) {
            throw new IllegalArgumentException("Requested round has not been generated yet");
        }

        List<Match> roundMatches = new ArrayList<>();
        for (Match match : matches) {
            if (match.getRoundNumber() == round) {
                roundMatches.add(match);
            }
        }

        return roundMatches;
    }
    
    /**
     * Automatically generate results for all matches in the current round
     */
    public void autoGenerateRoundResults() {
        if (currentRound == 0) {
            throw new IllegalStateException("No rounds have been generated yet");
        }

        List<Match> roundMatches = getRoundMatches(currentRound);

        if (roundMatches.isEmpty()) {
            throw new IllegalStateException("No matches in current round to generate results for.");
        }

        System.out.println("\n=== Generating Random Results for Round " + currentRound + " ===");
        for (Match match : roundMatches) {
            if (!match.isBye() && !match.isPlayed()) {
                match.generateRandomResult();
                System.out.println(match.toString());
            }
        }
    }
    
    /**
     * Manually set result for a specific match
     */
    public void setMatchResult(Match match, Match.MatchResult result) {
        if (match == null) {
            throw new IllegalArgumentException("Match cannot be null");
        }
        if (!matches.contains(match)) {
            throw new IllegalArgumentException("Match does not belong to this tournament");
        }

        match.setResult(result);
    }
    
    /**
     * Get current standings sorted by score and tiebreakers
     */
    public List<Participant> getStandings() {
        List<Participant> standings = new ArrayList<>(participants);
        standings.sort((p1, p2) -> {
            // Primary: Score (descending)
            int scoreCompare = Double.compare(p2.getScore(), p1.getScore());
            if (scoreCompare != 0) return scoreCompare;
            
            // Tiebreaker 1: Buchholz (sum of opponents' scores)
            int buchholzCompare = Double.compare(p2.getOpponentsSumScore(), p1.getOpponentsSumScore());
            if (buchholzCompare != 0) return buchholzCompare;
            
            // Tiebreaker 2: Number of wins
            int winsCompare = Integer.compare(p2.getWinCount(), p1.getWinCount());
            if (winsCompare != 0) return winsCompare;
            
            // Tiebreaker 3: ID (for consistency)
            return Integer.compare(p1.getId(), p2.getId());
        });
        
        return standings;
    }
    
    /**
     * Display current standings
     */
    public void displayStandings() {
        System.out.println("\n=== CURRENT STANDINGS ===");
        System.out.println(String.format("%-5s %-4s %-20s %-8s %-8s %-12s %-10s", 
                                        "Rank", "ID", "Name", "Score", "Status", "W-D-L", "Buchholz"));
        System.out.println("=".repeat(80));
        
        List<Participant> standings = getStandings();
        int rank = 1;
        for (Participant p : standings) {
            String wdl = String.format("%d-%d-%d", p.getWinCount(), p.getDrawCount(), p.getLossCount());
            System.out.println(String.format("%-5d %-4d %-20s %-8.1f %-8s %-12s %-10.1f", 
                                            rank++, p.getId(), p.getName(), p.getScore(), 
                                            p.getStatus(), wdl, p.getOpponentsSumScore()));
        }
    }
    
    /**
     * Display all matches
     */
    public void displayAllMatches() {
        System.out.println("\n=== ALL MATCHES ===");
        for (int round = 1; round <= currentRound; round++) {
            System.out.println("\nRound " + round + ":");
            List<Match> roundMatches = getRoundMatches(round);
            for (Match match : roundMatches) {
                System.out.println("  " + match.toString());
            }
        }
    }
    
    /**
     * Check if tournament is complete
     */
    public boolean isComplete() {
        return currentRound >= totalRounds && totalRounds > 0;
    }
    
    /**
     * Save tournament to a text file
     */
    public void saveToFile(String filename) throws IOException {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Header
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            
            writer.println("TOURNAMENT: " + name);
            writer.println("PARTICIPANTS: " + participants.size());
            writer.println("ROUNDS: " + totalRounds);
            writer.println("CURRENT_ROUND: " + currentRound);
            writer.println("DATE: " + now.format(formatter));
            writer.println();
            
            // Participants
            writer.println("=== PARTICIPANTS ===");
            writer.println(String.format("%-4s | %-20s | %-8s | %-8s | %-12s", 
                                        "ID", "Name", "Score", "Status", "W-D-L"));
            writer.println("-".repeat(70));
            for (Participant p : participants) {
                String wdl = String.format("%d-%d-%d", p.getWinCount(), p.getDrawCount(), p.getLossCount());
                writer.println(String.format("%-4d | %-20s | %-8.1f | %-8s | %-12s", 
                                            p.getId(), p.getName(), p.getScore(), p.getStatus(), wdl));
            }
            writer.println();
            
            // Matches by round
            for (int round = 1; round <= currentRound; round++) {
                writer.println("=== ROUND " + round + " ===");
                List<Match> roundMatches = getRoundMatches(round);
                int matchNum = 1;
                for (Match match : roundMatches) {
                    writer.println("Match " + matchNum + ": " + match.toFileString());
                    matchNum++;
                }
                writer.println();
            }
            
            // Final standings
            if (isComplete()) {
                writer.println("=== FINAL STANDINGS ===");
            } else {
                writer.println("=== CURRENT STANDINGS ===");
            }
            writer.println(String.format("%-5s | %-4s | %-20s | %-8s | %-12s | %-10s", 
                                        "Rank", "ID", "Name", "Score", "W-D-L", "Buchholz"));
            writer.println("-".repeat(75));
            
            List<Participant> standings = getStandings();
            int rank = 1;
            for (Participant p : standings) {
                String wdl = String.format("%d-%d-%d", p.getWinCount(), p.getDrawCount(), p.getLossCount());
                writer.println(String.format("%-5d | %-4d | %-20s | %-8.1f | %-12s | %-10.1f", 
                                            rank++, p.getId(), p.getName(), p.getScore(), 
                                            wdl, p.getOpponentsSumScore()));
            }
            
            System.out.println("Tournament saved to " + filename);
        }
    }
    
    /**
     * Load tournament from a text file
     * Note: This is a simplified loader that reads the basic tournament info
     * For full reconstruction, you'd need to parse all match results
     */
    public static Tournament loadFromFile(String filename) throws IOException {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            String tournamentName = null;
            
            // Read header
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("TOURNAMENT: ")) {
                    tournamentName = line.substring("TOURNAMENT: ".length());
                    break;
                }
            }
            
            if (tournamentName == null) {
                throw new IOException("Invalid tournament file format");
            }
            
            Tournament tournament = new Tournament(tournamentName);
            
            // Read participants section
            while ((line = reader.readLine()) != null) {
                if (line.contains("=== PARTICIPANTS ===")) {
                    reader.readLine(); // Skip header line
                    reader.readLine(); // Skip separator line
                    
                    while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                        // Parse participant line: "ID | Name | Score | Status | W-D-L"
                        String[] parts = line.split("\\|");
                        if (parts.length >= 4) {
                            try {
                                int id = Integer.parseInt(parts[0].trim());
                                String name = parts[1].trim();
                                // parts[2] is score, skip for now
                                String statusStr = parts[3].trim();
                                ParticipantStatus status;
                                try {
                                    status = ParticipantStatus.valueOf(statusStr);
                                } catch (IllegalArgumentException e) {
                                    status = ParticipantStatus.LOW; // Default fallback
                                }
                                tournament.addParticipant(new Participant(id, name, status));
                            } catch (NumberFormatException e) {
                                throw new IOException("Invalid participant line: " + line, e);
                            }
                        } else if (parts.length >= 2) {
                            // Old format compatibility (no status field)
                            try {
                                int id = Integer.parseInt(parts[0].trim());
                                String name = parts[1].trim();
                                tournament.addParticipant(new Participant(id, name, ParticipantStatus.LOW));
                            } catch (NumberFormatException e) {
                                throw new IOException("Invalid participant line: " + line, e);
                            }
                        }
                    }
                    break;
                }
            }
            
            System.out.println("Tournament loaded from " + filename);
            System.out.println("Note: Match history not fully reconstructed. Only participant list loaded.");
            
            return tournament;
        }
    }
}

