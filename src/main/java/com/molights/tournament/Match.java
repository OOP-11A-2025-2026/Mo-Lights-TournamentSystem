package com.molights.tournament;

import java.util.Random;

/**
 * Represents a match between two participants in a Swiss tournament.
 * Handles match results, BYE matches, and status-based probability calculations.
 */
public class Match {
    
    /**
     * Possible outcomes of a match
     */
    public enum MatchResult {
        WIN_PLAYER1,
        WIN_PLAYER2,
        DRAW,
        NOT_PLAYED
    }
    
    private Participant player1;
    private Participant player2;
    private MatchResult result;
    private int roundNumber;
    private boolean isBye;
    
    /**
     * Creates a regular match between two players.
     * 
     * @param player1 First player
     * @param player2 Second player
     * @param roundNumber Round number this match belongs to
     * @throws IllegalArgumentException if players are null or the same
     */
    public Match(Participant player1, Participant player2, int roundNumber) {
        if (player1 == null || player2 == null) {
            throw new IllegalArgumentException("Players cannot be null");
        }
        if (player1.equals(player2)) {
            throw new IllegalArgumentException("A player cannot play against themselves");
        }
        this.player1 = player1;
        this.player2 = player2;
        this.roundNumber = roundNumber;
        this.result = MatchResult.NOT_PLAYED;
        this.isBye = false;
    }
    
    /**
     * Creates a BYE match (player gets free point).
     * 
     * @param player Player receiving the BYE
     * @param roundNumber Round number this BYE belongs to
     * @throws IllegalArgumentException if player is null
     */
    public Match(Participant player, int roundNumber) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        this.player1 = player;
        this.player2 = null;
        this.roundNumber = roundNumber;
        this.result = MatchResult.WIN_PLAYER1;
        this.isBye = true;
    }
    
    // Getters
    public Participant getPlayer1() {
        return player1;
    }
    
    public Participant getPlayer2() {
        return player2;
    }
    
    public MatchResult getResult() {
        return result;
    }
    
    public int getRoundNumber() {
        return roundNumber;
    }
    
    public boolean isBye() {
        return isBye;
    }
    
    /**
     * Generate random result based on probability distribution considering participant status:
     * <ul>
     *   <li>Same status: 45% / 45% / 10% draw</li>
     *   <li>HIGH-LOW: 55% high / 30% low / 15% draw</li>
     *   <li>HIGH-MEDIUM: 50% high / 35% medium / 15% draw</li>
     *   <li>MEDIUM-LOW: 50% medium / 35% low / 15% draw</li>
     * </ul>
     * 
     * @throws IllegalStateException if this is a BYE match
     */
    public void generateRandomResult() {
        if (isBye) {
            throw new IllegalStateException("Cannot generate random result for BYE match");
        }
        
        Random random = new Random();
        int outcome = random.nextInt(100); // 0-99
        
        ParticipantStatus status1 = player1.getStatus();
        ParticipantStatus status2 = player2.getStatus();
        
        // Determine probabilities based on status matchup
        int player1WinChance;
        int drawChance;
        
        if (status1 == status2) {
            // Same status: 45% / 45% / 10%
            player1WinChance = 45;
            drawChance = 10;
        } else if (status1 == ParticipantStatus.HIGH && status2 == ParticipantStatus.LOW) {
            // HIGH vs LOW: 55% / 30% / 15%
            player1WinChance = 55;
            drawChance = 15;
        } else if (status1 == ParticipantStatus.LOW && status2 == ParticipantStatus.HIGH) {
            // LOW vs HIGH: 30% / 55% / 15%
            player1WinChance = 30;
            drawChance = 15;
        } else if (status1 == ParticipantStatus.HIGH && status2 == ParticipantStatus.MEDIUM) {
            // HIGH vs MEDIUM: 50% / 35% / 15%
            player1WinChance = 50;
            drawChance = 15;
        } else if (status1 == ParticipantStatus.MEDIUM && status2 == ParticipantStatus.HIGH) {
            // MEDIUM vs HIGH: 35% / 50% / 15%
            player1WinChance = 35;
            drawChance = 15;
        } else if (status1 == ParticipantStatus.MEDIUM && status2 == ParticipantStatus.LOW) {
            // MEDIUM vs LOW: 50% / 35% / 15%
            player1WinChance = 50;
            drawChance = 15;
        } else if (status1 == ParticipantStatus.LOW && status2 == ParticipantStatus.MEDIUM) {
            // LOW vs MEDIUM: 35% / 50% / 15%
            player1WinChance = 35;
            drawChance = 15;
        } else {
            // Default fallback (should never happen)
            player1WinChance = 45;
            drawChance = 10;
        }
        
        int player2WinThreshold = player1WinChance + (100 - player1WinChance - drawChance);
        
        if (outcome < player1WinChance) {
            // Player 1 wins
            setResult(MatchResult.WIN_PLAYER1);
        } else if (outcome < player2WinThreshold) {
            // Player 2 wins
            setResult(MatchResult.WIN_PLAYER2);
        } else {
            // Draw
            setResult(MatchResult.DRAW);
        }
    }
    
    /**
     * Manually set the result of the match.
     * Automatically applies result to both participants.
     * 
     * @param result The match result
     * @throws IllegalArgumentException if result is null
     * @throws IllegalStateException if trying to set non-WIN_PLAYER1 result for BYE match
     */
    public void setResult(MatchResult result) {
        if (result == null) {
            throw new IllegalArgumentException("Result cannot be null");
        }
        
        if (isBye && result != MatchResult.WIN_PLAYER1) {
            throw new IllegalStateException("BYE match result must be WIN_PLAYER1");
        }
        
        this.result = result;
        applyResultToParticipants();
    }
    
    /**
     * Apply the match result to both participants.
     * Updates scores, statistics, and opponent lists.
     */
    private void applyResultToParticipants() {
        if (result == MatchResult.NOT_PLAYED) {
            return; // Don't apply if match hasn't been played
        }
        
        if (isBye) {
            // BYE handling is done in Tournament class via byeParticipant()
            return;
        }
        
        // Add opponents to each other's lists
        player1.addOpponent(player2);
        player2.addOpponent(player1);
        
        // Apply results based on outcome
        switch (result) {
            case WIN_PLAYER1:
                player1.addPoint(1.0);
                player1.addWin();
                player2.addLoss();
                break;
                
            case WIN_PLAYER2:
                player2.addPoint(1.0);
                player2.addWin();
                player1.addLoss();
                break;
                
            case DRAW:
                player1.addPoint(0.5);
                player2.addPoint(0.5);
                player1.addDraw();
                player2.addDraw();
                break;
                
            case NOT_PLAYED:
                // Should not reach here due to check above
                break;
        }
    }
    
    /**
     * Check if the match has been played.
     * 
     * @return true if match has a result, false otherwise
     */
    public boolean isPlayed() {
        return result != MatchResult.NOT_PLAYED;
    }
    
    /**
     * Get a formatted string representation for display.
     * 
     * @return Formatted match string
     */
    @Override
    public String toString() {
        if (isBye) {
            return String.format("Round %d: %s (BYE)", roundNumber, player1.getName());
        }
        
        String resultStr;
        switch (result) {
            case WIN_PLAYER1:
                resultStr = String.format("%s wins", player1.getName());
                break;
            case WIN_PLAYER2:
                resultStr = String.format("%s wins", player2.getName());
                break;
            case DRAW:
                resultStr = "Draw";
                break;
            case NOT_PLAYED:
                resultStr = "Not played yet";
                break;
            default:
                resultStr = "Unknown";
        }
        
        return String.format("Round %d: %s vs %s => %s", 
                           roundNumber, player1.getName(), player2.getName(), resultStr);
    }
    
    /**
     * Get formatted string for file output.
     * 
     * @return Formatted match string for file saving
     */
    public String toFileString() {
        if (isBye) {
            return String.format("%s (BYE) => WIN", player1.getName());
        }
        
        String resultStr;
        switch (result) {
            case WIN_PLAYER1:
                resultStr = "1-0";
                break;
            case WIN_PLAYER2:
                resultStr = "0-1";
                break;
            case DRAW:
                resultStr = "0.5-0.5";
                break;
            case NOT_PLAYED:
                resultStr = "NOT_PLAYED";
                break;
            default:
                resultStr = "UNKNOWN";
        }
        
        return String.format("%s vs %s => %s", player1.getName(), player2.getName(), resultStr);
    }
}

