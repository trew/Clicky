package se.samuelandersson.clicky.game;

import java.text.DecimalFormat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import se.samuelandersson.clicky.Clicky;
import se.samuelandersson.clicky.manager.SoundManager.GameSound;
import se.samuelandersson.clicky.util.MathHelper;

public class ClickyGame extends InputListener implements Disposable
{
    private final Clicky app;
    private final Stage stage;

    private TweenManager tweenManager;

    private boolean paused;
    private boolean started;
    private boolean gameOver;

    private BallManager ballManager;

    private int score = 0;
    private int clicks = 0;
    private int hits = 0;
    int misses = 0;
    int missedClicks = 0;
    private int level = 1;

    private Array<Level> levels;
    private float timer;

    private Array<GameListener> gameListeners;
    private ClickListener listener;

    public ClickyGame(Clicky app, Stage stage)
    {
        this.app = app;
        this.stage = stage;
        gameListeners = new Array<GameListener>();
        ballManager = new BallManager(this);
        tweenManager = new TweenManager();
        Tween.registerAccessor(Ball.class, ballManager);
    }

    public void initialize()
    {
        reset();
        levels = new Array<Level>(10);
        levels.add(new Level(1.0f, 1.0f, 10));
        levels.add(new Level(0.9f, 1.2f, 11));
        levels.add(new Level(0.8f, 1.4f, 12));
        levels.add(new Level(0.7f, 1.6f, 13));
        levels.add(new Level(0.6f, 1.8f, 14));
        levels.add(new Level(0.6f, 1.9f, 15));
        levels.add(new Level(0.5f, 2.1f, 16));
        levels.add(new Level(0.49f, 2.2f, 17));
        levels.add(new Level(0.48f, 2.3f, 18));
        levels.add(new Level(0.45f, 2.5f, 20));
        getStage().addListener(this);
        listener = new ClickListener(Buttons.LEFT)
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (paused || gameOver)
                    return;
                clicks++;
                if (event.getTarget() instanceof Ball) {
                    Ball b = (Ball) event.getTarget();
                    ballManager.clicked(b);
                    hits++;
                    app.getSoundManager().play(GameSound.SWOOP);
                    levels.get(level).totalBalls--;
                    if (levels.get(level).totalBalls == 0) {
                        if (level < 9) {
                            level++;
                            fireLevelChanged();
                        } else
                            gameOver();
                    }
                    addScore(b, new Vector2(x, y));
                } else {
                    missedClicks++;
                    misses++;
                }
            }

            @Override
            public boolean isOver(Actor actor, float x, float y)
            {
                if (actor instanceof Group) {
                    return true;
                }
                return super.isOver(actor, x, y);
            }
        };
        getStage().addCaptureListener(listener);
    }

    public void reset()
    {
        timer = 0;
        score = 0;
        clicks = 0;
        hits = 0;
        misses = 0;
        missedClicks = 0;
        level = 0;
        started = false;
        gameOver = false;
        tweenManager.killAll();
    }

    @Override
    public void dispose()
    {
        getStage().removeListener(this);
        gameListeners.clear();
        tweenManager.killAll();
    }

    public void start()
    {
        started = true;
        fireGameStarted();
    }

    public void update(float delta)
    {
        tweenManager.update(delta);
        if (!started) {
            return;
        }

        timer += delta;
        while (timer > levels.get(level).ballDelay) {
            ballManager.addRandomBall(levels.get(level));
            timer -= levels.get(level).ballDelay;
        }
    }

    public void render(SpriteBatch batch)
    {
        if (Clicky.DEBUGDRAW) {
            drawDebug(batch);
        }
    }

    private void addScore(Ball target, Vector2 mousePos)
    {
        float timeAlive = target.getTimeAlive();
        float radius = target.getWidth() / 2;
        Vector2 bPos = new Vector2(target.getX() + radius, target.getY()
                + radius);
        float distanceToMid = bPos.dst(mousePos);
        distanceToMid = MathHelper.convert(distanceToMid, 0, radius, 0.5f, 1);

        float base = (100 - MathHelper.clamp(radius, 0.1f, 99.9f));
        int toAdd = (int) (base / timeAlive / distanceToMid);
        score += toAdd;
        DecimalFormat f = new DecimalFormat("00.00");
        System.out.println("score += base(" + f.format(base) + ") / timeAlive("
                + f.format(timeAlive) + ") / distanceToMid("
                + f.format(distanceToMid) + ") = " + toAdd);
        fireScoreAdded(toAdd, target);
    }

    public void gameOver()
    {
        ballManager.removeAll();
        gameOver = true;
        started = false;
        if (Highscores.qualifiesForHighscore(getFinalScore()) >= 0) {
            fireNewHighscore(getFinalScore());
        }
        fireGameOver();
    }

    @Override
    public boolean keyUp(InputEvent e, int key)
    {
        if (key == Keys.P) {
            paused = !paused;
            return true;
        }
        return false;
    }

    public void drawDebug(SpriteBatch batch)
    {
        // build string
        StringBuilder builder = new StringBuilder();

        builder.append("FPS: " + Gdx.graphics.getFramesPerSecond() + "\n");

        /* Get camera position */
        DecimalFormat f = new DecimalFormat("00000");
        float camX = getStage().getCamera().position.x;
        float camY = getStage().getCamera().position.y;
        builder.append("Camera: " + f.format(camX) + ", " + f.format(camY)
                + "\n");

        builder.append("Clicks: " + clicks + "\n");
        builder.append("Hits: " + hits + "\n");
        builder.append("Misses: " + misses + "\n");
        builder.append("Missed Clicks: " + missedClicks + "\n");

        // draw it
        batch.begin();
        app.getDebugFont().drawMultiLine(batch, builder.toString(), 5,
                Gdx.graphics.getHeight() - 5);
        batch.end();
    }

    /* *** GET-/SETTERS *** */

    public TweenManager getTweenManager()
    {
        return tweenManager;
    }

    public Clicky getApp()
    {
        return app;
    }

    public boolean isPaused()
    {
        return paused;
    }

    public boolean isStarted()
    {
        return started;
    }

    public void setPaused(boolean pause)
    {
        paused = pause;
    }

    public final Stage getStage()
    {
        return stage;
    }

    public int getClicks()
    {
        return clicks;
    }

    public int getHits()
    {
        return hits;
    }

    public int getMisses()
    {
        return misses;
    }

    public int getMissedClicks()
    {
        return missedClicks;
    }

    public float getScore()
    {
        return score;
    }

    public int getFinalScore()
    {
        return (int) (score * hits / (float) clicks);
    }

    public int getLevel()
    {
        return level + 1;
    }

    public boolean isGameOver()
    {
        return gameOver;
    }

    public void addListener(GameListener listener)
    {
        gameListeners.add(listener);
    }

    public void removeListener(GameListener listener)
    {
        gameListeners.removeValue(listener, true);
    }

    public void removeAllListeners()
    {
        gameListeners.clear();
    }

    protected void fireGameStarted()
    {
        for (GameListener l : gameListeners) {
            l.gameStarted();
        }
    }

    protected void fireGameOver()
    {
        for (GameListener l : gameListeners) {
            l.gameOver();
        }
    }

    protected void fireLevelChanged()
    {
        for (GameListener l : gameListeners) {
            l.levelChanged(level);
        }
    }

    protected void fireScoreAdded(int added, Ball target)
    {
        for (GameListener l : gameListeners) {
            l.scoreAdded(added, target);
        }
    }

    protected void fireNewHighscore(int score)
    {
        for (GameListener l : gameListeners) {
            l.newHighscore(score);
        }
    }
}
