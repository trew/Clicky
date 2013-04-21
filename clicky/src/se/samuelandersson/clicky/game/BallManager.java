package se.samuelandersson.clicky.game;

import java.util.Date;
import java.util.Random;

import se.samuelandersson.clicky.util.MathHelper;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class BallManager implements TweenAccessor<Ball>
{
    private final ClickyGame game;
    private Array<Ball> balls;

    private Random rnd;
    private TextureRegion ballRegion;

    private TweenCallback removeBallCB;

    public BallManager(ClickyGame game)
    {
        this.game = game;
        balls = new Array<Ball>();
        rnd = new Random(new Date().getTime());
        ballRegion = game.getApp().getAtlas().findRegion("entities/sun");
        removeBallCB = new TweenCallback()
        {

            @Override
            public void onEvent(int type, BaseTween<?> source)
            {
                if (type == TweenCallback.COMPLETE)
                    removeBall((Ball) source.getUserData());
            }
        };
    }

    public Array<Ball> getBalls()
    {
        return balls;
    }

    public void addRandomBall(Level level)
    {
        float margin = 20;
        float x = MathHelper.convert(rnd.nextFloat(), 0, 1, margin,
                Gdx.graphics.getWidth() - margin);
        float y = MathHelper.convert(rnd.nextFloat(), 0, 1, margin,
                Gdx.graphics.getHeight() - margin);

        Ball b = createBall(x, y, level);
        balls.add(b);
        game.getStage().addActor(b);
    }

    public Ball createBall(float x, float y, Level level)
    {
        Ball b = new Ball(this, level.speed);
        b.setPosition(x, y);
        b.setSize(0, 0);
        return b;
    }

    public void removeBall(Ball b)
    {
        balls.removeValue(b, true);
        b.remove();
    }

    public void clicked(Ball b)
    {
        b.kill();
        Tween.to(b, TWEEN_SCALE, 0.2f).target(0).setUserData(b)
                .setCallback(removeBallCB).ease(TweenEquations.easeInBack)
                .start(game.getTweenManager());
    }

    public void removeAll()
    {
        for (Ball b : balls) {
            b.kill();
            Tween.to(b, TWEEN_SCALE, 2).target(10).setUserData(b)
                    .setCallback(removeBallCB).ease(TweenEquations.easeInBack)
                    .start(game.getTweenManager());
        }
    }

    static public final int TWEEN_SCALE = 0;

    @Override
    public int getValues(Ball target, int tweenType, float[] returnValues)
    {
        if (tweenType == TWEEN_SCALE) {
            returnValues[0] = target.getScaleX();
            returnValues[1] = target.getScaleY();
            return 2;
        }
        return 0;
    }

    @Override
    public void setValues(Ball target, int tweenType, float[] newValues)
    {
        if (tweenType == TWEEN_SCALE) {
            target.setScale(newValues[0], newValues[1]);
        }
    }

    public TextureRegion getBallRegion()
    {
        return ballRegion;
    }
}
