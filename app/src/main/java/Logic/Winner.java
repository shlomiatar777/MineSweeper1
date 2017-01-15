package Logic;

import android.location.Location;

public class Winner {
    private String name;
    private int score;
    private Location location;

    public Winner(String name, int score, Location location)
    {
        this.name = name;
        this.score = score;
        this.location = location;
    }

    public Winner()
    {

    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}