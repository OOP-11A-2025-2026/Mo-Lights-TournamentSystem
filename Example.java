import com.molights.tournament.*;
import java.io.IOException;
import java.util.List;

/**
 * Example usage of the Mo-Lights Tournament System Library
 * This demonstrates how to use the library programmatically in your own application.
 */
public class Example {
    
    public static void main(String[] args) {
        System.out.println("=== Mo-Lights Tournament System - Example Usage ===\n");
        
        // Example 1: Basic Tournament
        basicTournamentExample();
        
        // Example 2: Manual Result Entry
        manualResultExample();
        
        // Example 3: Save and Load
        saveLoadExample();
    }
    
    /**
     * Example 1: Create a basic tournament and auto-generate all results
     */
    public static void basicTournamentExample() {
        System.out.println("\n--- Example 1: Basic Tournament ---");
        
        // Create a new tournament
        Tournament tournament = new Tournament("Spring Championship 2024");
        
        // Add participants with different status levels
        tournament.addParticipant(new Participant(1, "Alice Johnson", ParticipantStatus.HIGH));
        tournament.addParticipant(new Participant(2, "Bob Smith", ParticipantStatus.MEDIUM));
        tournament.addParticipant(new Participant(3, "Charlie Brown", ParticipantStatus.MEDIUM));
        tournament.addParticipant(new Participant(4, "Diana Prince", ParticipantStatus.LOW));
        tournament.addParticipant(new Participant(5, "Eve Wilson", ParticipantStatus.HIGH));
        tournament.addParticipant(new Participant(6, "Frank Miller", ParticipantStatus.LOW));
        
        // Start the tournament (calculates rounds automatically)
        tournament.startTournament();
        System.out.println("Tournament started with " + tournament.getTotalRounds() + " rounds\n");
        
        // Play all rounds with auto-generated results
        while (!tournament.isComplete()) {
            tournament.generateNextRound();
            tournament.autoGenerateRoundResults();
            System.out.println();
        }
        
        // Display final standings
        tournament.displayStandings();
        
        // Get winner programmatically
        List<Participant> standings = tournament.getStandings();
        Participant winner = standings.get(0);
        System.out.println("\n🏆 Champion: " + winner.getName() + 
                         " with " + winner.getScore() + " points!");
    }
    
    /**
     * Example 2: Manual result entry for each match
     */
    public static void manualResultExample() {
        System.out.println("\n\n--- Example 2: Manual Result Entry ---");
        
        Tournament tournament = new Tournament("Manual Entry Demo");
        
        // Add 4 participants
        tournament.addParticipant(new Participant(1, "Player A", ParticipantStatus.MEDIUM));
        tournament.addParticipant(new Participant(2, "Player B", ParticipantStatus.MEDIUM));
        tournament.addParticipant(new Participant(3, "Player C", ParticipantStatus.MEDIUM));
        tournament.addParticipant(new Participant(4, "Player D", ParticipantStatus.MEDIUM));
        
        tournament.startTournament();
        
        // Generate first round
        tournament.generateNextRound();
        
        // Manually set results
        List<Match> round1Matches = tournament.getRoundMatches(1);
        System.out.println("\nSetting results for Round 1:");
        
        // Match 1: Player A wins
        round1Matches.get(0).setResult(Match.MatchResult.WIN_PLAYER1);
        System.out.println("  " + round1Matches.get(0).toString());
        
        // Match 2: Draw
        round1Matches.get(1).setResult(Match.MatchResult.DRAW);
        System.out.println("  " + round1Matches.get(1).toString());
        
        // Display standings after round 1
        tournament.displayStandings();
    }
    
    /**
     * Example 3: Save and load tournament
     */
    public static void saveLoadExample() {
        System.out.println("\n\n--- Example 3: Save and Load ---");
        
        // Create and run a tournament
        Tournament tournament = new Tournament("Save Test Tournament");
        tournament.addParticipant(new Participant(1, "Saver 1", ParticipantStatus.HIGH));
        tournament.addParticipant(new Participant(2, "Saver 2", ParticipantStatus.LOW));
        tournament.startTournament();
        tournament.generateNextRound();
        tournament.autoGenerateRoundResults();
        
        // Save to file
        String filename = "example-tournament.txt";
        try {
            tournament.saveToFile(filename);
            System.out.println("✅ Tournament saved to " + filename);
        } catch (IOException e) {
            System.err.println("❌ Error saving: " + e.getMessage());
        }
        
        // Load from file
        try {
            Tournament loaded = Tournament.loadFromFile(filename);
            System.out.println("✅ Tournament loaded from " + filename);
            loaded.displayStandings();
        } catch (IOException e) {
            System.err.println("❌ Error loading: " + e.getMessage());
        }
    }
}

