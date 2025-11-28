import java.util.ArrayList;
import java.util.List;

public class Participant
{
    private int id;
    private String name;
    private double currentScore;
    private List<Participant> opponents; 
    private boolean wasParticipantByed;

    //additional statistics  
    private int winCount;
    private int drawCount;
    private int lossCount;

    // create a participant when they enter a tornament 
    public Participant(int id, String name)
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


    //setters
    public void setName(String name)
    {
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("name must not be empty");
        }
        this.name = name;
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

    // for Buchholz tiebreak
    public double getOpponentsSumScore()
    {
        double sum = 0;
        for(Participant opponent : this.opponents)
        {
            sum+=opponent.getScore();
        }
        return sum;
    }

    //BYE-ing participant once and adding a point
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