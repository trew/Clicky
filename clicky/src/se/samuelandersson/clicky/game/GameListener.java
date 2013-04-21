package se.samuelandersson.clicky.game;

public interface GameListener
{
    void gameStarted();

    void gameOver();

    void levelChanged(int toLevel);

    void scoreAdded(int scoreAdded, Ball target);

    void newHighscore(int score);
}
