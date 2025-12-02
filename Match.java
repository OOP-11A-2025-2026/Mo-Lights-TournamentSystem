import java.util.Random;

public class Match {
    
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
    
    // Regular match between two players
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
    
    // BYE match (player gets free point)
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
     * Generate random result based on probability distribution:
     * - 45% chance Player 1 wins
     * - 45% chance Player 2 wins
     * - 10% chance Draw
     */
    public void generateRandomResult() {
        if (isBye) {
            throw new IllegalStateException("Cannot generate random result for BYE match");
        }
        
        Random random = new Random();
        int outcome = random.nextInt(100); // 0-99
        
        if (outcome < 45) {
            // 0-44: Player 1 wins (45%)
            setResult(MatchResult.WIN_PLAYER1);
        } else if (outcome < 90) {
            // 45-89: Player 2 wins (45%)
            setResult(MatchResult.WIN_PLAYER2);
        } else {
            // 90-99: Draw (10%)
            setResult(MatchResult.DRAW);
        }
    }
    
    /**
     * Manually set the result of the match
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
     * Apply the match result to both participants
     * Updates scores, statistics, and opponent lists
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
     * Check if the match has been played
     */
    public boolean isPlayed() {
        return result != MatchResult.NOT_PLAYED;
    }
    
    /**
     * Get a formatted string representation for display
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
     * Get formatted string for file output
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

