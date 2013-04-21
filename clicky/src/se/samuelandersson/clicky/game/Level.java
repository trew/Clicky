package se.samuelandersson.clicky.game;

public class Level
{
    public float ballDelay;
    public float speed;
    public int totalBalls;

    public Level(float delay, float speed, int totalBalls)
    {
        this.ballDelay = delay;
        this.speed = speed;
        this.totalBalls = totalBalls;
    }
}
