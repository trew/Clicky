package se.samuelandersson.clicky.game;

public class Rules
{
    public float ballDelayDec;
    public float initialballDelay;
    public float maxBallRadius;
    public float ballLifeTime;
    public float maxMisses;

    public float currentBallDelay;

    public void reset()
    {
        ballDelayDec = 0.003f;
        currentBallDelay = initialballDelay = 0.8f;
        maxMisses = 10;
        ballLifeTime = 2.5f;
        maxBallRadius = 40;
    }
}
