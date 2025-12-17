package com.molights.tournament.demo;

import com.molights.tournament.*;
import java.util.Scanner;
import java.util.List;
import java.io.IOException;

/**
 * Demo CLI application for the Mo-Lights Tournament System.
 * Provides an interactive menu for managing Swiss-style tournaments.
 */
public class Main {
    
    private static Scanner scanner = new Scanner(System.in);
    private static Tournament tournament = null;
    
    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("  Swiss Tournament System");
        System.out.println("=================================\n");
        
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");
            System.out.println();
            
            switch (choice) {
                case 1:
                    createNewTournament();
                    break;
                case 2:
                    addParticipants();
                    break;
                case 3:
                    startTournament();
                    break;
                case 4:
                    generateNextRound();
                    break;
                case 5:
                    enterRoundResults();
                    break;
                case 6:
                    viewStandings();
                    break;
                case 7:
                    viewAllMatches();
                    break;
                case 8:
                    saveTournament();
                    break;
                case 9:
                    loadTournament();
                    break;
                case 0:
                    running = false;
                    System.out.println("Exiting... Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    private static void displayMenu() {
        System.out.println("\n========== MENU ==========");
        System.out.println("1. Create New Tournament");
        System.out.println("2. Add Participants");
        System.out.println("3. Start Tournament");
        System.out.println("4. Generate Next Round");
        System.out.println("5. Enter Round Results");
        System.out.println("6. View Standings");
        System.out.println("7. View All Matches");
        System.out.println("8. Save Tournament");
        System.out.println("9. Load Tournament");
        System.out.println("0. Exit");
        System.out.println("==========================");
    }
    
    private static void createNewTournament() {
        System.out.print("Enter tournament name: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println("Tournament name cannot be empty.");
            return;
        }
        
        tournament = new Tournament(name);
        System.out.println("Tournament '" + name + "' created successfully!");
    }
    
    private static void addParticipants() {
        if (tournament == null) {
            System.out.println("Please create a tournament first.");
            return;
        }
        
        if (tournament.getCurrentRound() > 0) {
            System.out.println("Cannot add participants after tournament has started.");
            return;
        }
        
        System.out.println("\n=== Add Participants ===");
        System.out.println("Enter participants (type 'done' when finished)");
        
        int participantCount = tournament.getParticipants().size();
        
        while (true) {
            int id = participantCount + 1;
            System.out.print("Participant #" + id + " name (or 'done'): ");
            String name = scanner.nextLine().trim();
            
            if (name.equalsIgnoreCase("done")) {
                break;
            }
            
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Try again.");
                continue;
            }
            
            // Prompt for status
            System.out.println("Select status for " + name + ":");
            System.out.println("1. LOW");
            System.out.println("2. MEDIUM");
            System.out.println("3. HIGH");
            int statusChoice = getIntInput("Enter status (1-3): ");
            
            ParticipantStatus status;
            switch (statusChoice) {
                case 1:
                    status = ParticipantStatus.LOW;
                    break;
                case 2:
                    status = ParticipantStatus.MEDIUM;
                    break;
                case 3:
                    status = ParticipantStatus.HIGH;
                    break;
                default:
                    System.out.println("Invalid choice. Defaulting to LOW.");
                    status = ParticipantStatus.LOW;
            }
            
            try {
                Participant participant = new Participant(id, name, status);
                tournament.addParticipant(participant);
                System.out.println("Added: " + name + " (Status: " + status + ")");
                participantCount++;
            } catch (Exception e) {
                System.out.println("Error adding participant: " + e.getMessage());
            }
        }
        
        System.out.println("\nTotal participants: " + tournament.getParticipants().size());
    }
    
    private static void startTournament() {
        if (tournament == null) {
            System.out.println("Please create a tournament first.");
            return;
        }
        
        try {
            tournament.startTournament();
            System.out.println("Tournament started!");
            System.out.println("Total rounds: " + tournament.getTotalRounds());
        } catch (Exception e) {
            System.out.println("Error starting tournament: " + e.getMessage());
        }
    }
    
    private static void generateNextRound() {
        if (tournament == null) {
            System.out.println("Please create a tournament first.");
            return;
        }
        
        if (tournament.getTotalRounds() == 0) {
            System.out.println("Please start the tournament first.");
            return;
        }
        
        if (tournament.isComplete()) {
            System.out.println("Tournament is complete! All rounds have been played.");
            return;
        }
        
        try {
            tournament.generateNextRound();
        } catch (Exception e) {
            System.out.println("Error generating round: " + e.getMessage());
        }
    }
    
    private static void enterRoundResults() {
        if (tournament == null) {
            System.out.println("Please create a tournament first.");
            return;
        }
        
        int currentRound = tournament.getCurrentRound();
        if (currentRound == 0) {
            System.out.println("No rounds have been generated yet.");
            return;
        }
        
        List<Match> roundMatches = tournament.getRoundMatches(currentRound);
        if (roundMatches.isEmpty()) {
            System.out.println("No matches in current round.");
            return;
        }
        
        System.out.println("\n=== Enter Results for Round " + currentRound + " ===");
        System.out.println("Choose how to enter results:");
        System.out.println("1. Auto-generate all results");
        System.out.println("2. Manually enter each result");
        
        int choice = getIntInput("Your choice: ");
        
        if (choice == 1) {
            try {
                tournament.autoGenerateRoundResults();
                System.out.println("\nResults generated successfully!");
            } catch (Exception e) {
                System.out.println("Error generating results: " + e.getMessage());
            }
        } else if (choice == 2) {
            manuallyEnterResults(roundMatches);
        } else {
            System.out.println("Invalid choice.");
        }
    }
    
    private static void manuallyEnterResults(List<Match> matches) {
        for (int i = 0; i < matches.size(); i++) {
            Match match = matches.get(i);
            
            if (match.isBye()) {
                System.out.println("\nMatch " + (i + 1) + ": " + match.getPlayer1().getName() + " (BYE) - Automatic Win");
                continue;
            }
            
            if (match.isPlayed()) {
                System.out.println("\nMatch " + (i + 1) + ": Already played - " + match.toString());
                continue;
            }
            
            System.out.println("\nMatch " + (i + 1) + ": " + match.getPlayer1().getName() + 
                             " vs " + match.getPlayer2().getName());
            System.out.println("1. " + match.getPlayer1().getName() + " wins");
            System.out.println("2. " + match.getPlayer2().getName() + " wins");
            System.out.println("3. Draw");
            
            int result = getIntInput("Enter result: ");
            
            try {
                switch (result) {
                    case 1:
                        match.setResult(Match.MatchResult.WIN_PLAYER1);
                        System.out.println("Result recorded: " + match.getPlayer1().getName() + " wins");
                        break;
                    case 2:
                        match.setResult(Match.MatchResult.WIN_PLAYER2);
                        System.out.println("Result recorded: " + match.getPlayer2().getName() + " wins");
                        break;
                    case 3:
                        match.setResult(Match.MatchResult.DRAW);
                        System.out.println("Result recorded: Draw");
                        break;
                    default:
                        System.out.println("Invalid choice. Skipping this match.");
                }
            } catch (Exception e) {
                System.out.println("Error recording result: " + e.getMessage());
            }
        }
        
        System.out.println("\nAll results entered!");
    }
    
    private static void viewStandings() {
        if (tournament == null) {
            System.out.println("Please create a tournament first.");
            return;
        }
        
        if (tournament.getParticipants().isEmpty()) {
            System.out.println("No participants in tournament.");
            return;
        }
        
        tournament.displayStandings();
    }
    
    private static void viewAllMatches() {
        if (tournament == null) {
            System.out.println("Please create a tournament first.");
            return;
        }
        
        if (tournament.getMatches().isEmpty()) {
            System.out.println("No matches have been played yet.");
            return;
        }
        
        tournament.displayAllMatches();
    }
    
    private static void saveTournament() {
        if (tournament == null) {
            System.out.println("Please create a tournament first.");
            return;
        }
        
        System.out.print("Enter filename (e.g., tournament.txt): ");
        String filename = scanner.nextLine().trim();
        
        if (filename.isEmpty()) {
            filename = "tournament.txt";
        }
        
        // Add .txt extension if not present
        if (!filename.endsWith(".txt")) {
            filename += ".txt";
        }
        
        try {
            tournament.saveToFile(filename);
            System.out.println("Tournament saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving tournament: " + e.getMessage());
        }
    }
    
    private static void loadTournament() {
        System.out.print("Enter filename to load: ");
        String filename = scanner.nextLine().trim();
        
        if (filename.isEmpty()) {
            System.out.println("Filename cannot be empty.");
            return;
        }
        
        try {
            tournament = Tournament.loadFromFile(filename);
            System.out.println("Tournament loaded successfully!");
            System.out.println("You can now view standings and continue the tournament.");
        } catch (IOException e) {
            System.out.println("Error loading tournament: " + e.getMessage());
        }
    }
    
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}

