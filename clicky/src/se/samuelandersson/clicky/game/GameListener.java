package se.samuelandersson.clicky.game;

public interface GameListener
{
    void gameStarted();

    void gameOver();

    void newHighscore(int score);
}
