package com.molights.tournament;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a participant in a Swiss tournament.
 * Tracks scores, statistics, opponents, and status level.
 */
public class Participant
{
    private int id;
    private String name;
    private double currentScore;
    private List<Participant> opponents; 
    private boolean wasParticipantByed;
    private ParticipantStatus status;

    //additional statistics  
    private int winCount;
    private int drawCount;
    private int lossCount;

    /**
     * Creates a new participant for the tournament.
     * 
     * @param id Unique identifier for the participant (must be positive)
     * @param name Name of the participant (cannot be empty)
     * @param status Skill level status (LOW, MEDIUM, or HIGH)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Participant(int id, String name, ParticipantStatus status)
    {
        if(id <= 0)
        {
            throw new IllegalArgumentException("id must be > 0");
        }
        this.id = id;
        if(name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("name must not be empty");
        }
        this.name = name;
        if(status == null)
        {
            throw new IllegalArgumentException("status cannot be null");
        }
        this.status = status;
        this.currentScore = 0;
        this.opponents = new ArrayList<>();
        this.wasParticipantByed = false;
        this.winCount = 0;
        this.lossCount = 0;
        this.drawCount = 0;
    }
    

    //getters
    public int getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public double getScore()
    {
        return this.currentScore;
    }

    public List<Participant> getOpponents()
    {
        return this.opponents;
    }

    public boolean getWasParticipantByed()
    {
        return this.wasParticipantByed;
    }

    public int getWinCount()
    {
        return this.winCount;
    }
    public int getDrawCount()
    {
        return this.drawCount;
    }
    public int getLossCount()
    {
        return this.lossCount;
    }

    public ParticipantStatus getStatus()
    {
        return this.status;
    }


    //setters
    public void setName(String name)
    {
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("name must not be empty");
        }
        this.name = name;
    }

    public void setStatus(ParticipantStatus status)
    {
        if (status == null)
        {
            throw new IllegalArgumentException("status cannot be null");
        }
        this.status = status;
    }

    public void addWin()
    {
        this.winCount++;
    }
    public void addDraw()
    {
        this.drawCount++;
    }
    public void addLoss()
    {
        this.lossCount++;
    }

    
    public void addPoint(double point)
    {
        if(point < 0)
        {
            throw new IllegalArgumentException("You must add positive number of points");
        }
        this.currentScore += point;
    }
    
    public void addOpponent(Participant opponent)
    {
        if(opponent == null) return;

        if(!this.hasPlayedWith(opponent))
        {
            this.opponents.add(opponent);
        }
    }

    public boolean hasPlayedWith(Participant opponent) 
    {
        return opponents.contains(opponent);
    }

    /**
     * Calculates the Buchholz score (sum of opponents' scores).
     * Used as a tiebreaker in Swiss tournaments.
     * 
     * @return Sum of all opponents' scores
     */
    public double getOpponentsSumScore()
    {
        double sum = 0;
        for(Participant opponent : this.opponents)
        {
            sum+=opponent.getScore();
        }
        return sum;
    }

    /**
     * Awards a BYE to this participant.
     * Can only be awarded once per participant.
     * Awards 1 point and counts as a win.
     * 
     * @throws IllegalStateException if participant already received a BYE
     */
    public void byeParticipant()
    {
        if (this.wasParticipantByed) 
        {
            throw new IllegalStateException("Participant already received a BYE.");
        }
        this.wasParticipantByed = true;
        this.addPoint(1.0);
        this.addWin();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID - ").append(this.id).append(" | ");
        sb.append(this.name).append(" | ");
        sb.append(this.currentScore).append("\n");
        return sb.toString();
    }


}

