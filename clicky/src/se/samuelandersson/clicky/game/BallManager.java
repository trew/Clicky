package se.samuelandersson.clicky.game;

import java.util.Date;
import java.util.Random;

import se.samuelandersson.clicky.util.MathHelper;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class BallManager implements TweenAccessor<Ball>
{
    static public final int TWEEN_SCALE = 0;
    static public final int TWEEN_SIZE = 1;

    private final ClickyGame game;
    private Array<Ball> balls;

    private Random rnd;
    private TextureRegion ballRegion;

    private TweenCallback removeBallCB;

    private float maxSize = 40; // FIXME magic number

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

    public void addRandomBall(float lifeTime)
    {
        float margin = 20; // FIXME magic number
        float x = MathHelper.convert(rnd.nextFloat(), 0, 1, margin,
                Gdx.graphics.getWidth() - margin);
        float y = MathHelper.convert(rnd.nextFloat(), 0, 1, margin,
                Gdx.graphics.getHeight() - margin);

        Ball b = createBall(x, y, lifeTime);
        balls.add(b);
        game.getStage().addActor(b);
    }

    public Ball createBall(float x, float y, float lifeTime)
    {
        final Ball b = new Ball(x, y, lifeTime, this);

        // Create two tweens, one increasing the size and one decreasing it
        // When both have passed, remove the ball and increase the miss-count.
        Tween inc = Tween.to(b, TWEEN_SIZE, lifeTime / 2).target(maxSize)
                .setUserData(b).ease(TweenEquations.easeInOutSine);
        Tween dec = Tween.to(b, TWEEN_SIZE, lifeTime / 2).target(0)
                .setUserData(b).ease(TweenEquations.easeInOutSine);
        dec.setCallback(new TweenCallback()
        {
            @Override
            public void onEvent(int type, BaseTween<?> source)
            {
                game.addClickedLabel(b.getX(), b.getY(), "Miss!", Color.RED);
                removeBall(b);
                game.misses++;
            }
        });

        Timeline tl = Timeline.createSequence().setUserData(b);
        tl.push(inc).push(dec);
        tl.start(game.getTweenManager());
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
        game.getTweenManager().killTarget(b);
        Tween.to(b, TWEEN_SCALE, 0.2f).target(0).setUserData(b)
                .setCallback(removeBallCB).ease(TweenEquations.easeInBack)
                .start(game.getTweenManager());
    }

    public void removeAll()
    {
        for (Ball b : balls) {
            b.kill();
            game.getTweenManager().killTarget(b);
            Tween.to(b, TWEEN_SCALE, 2).target(10).setUserData(b)
                    .setCallback(removeBallCB).ease(TweenEquations.easeInBack)
                    .start(game.getTweenManager());
        }
    }

    @Override
    public int getValues(Ball target, int tweenType, float[] returnValues)
    {
        if (tweenType == TWEEN_SCALE) {
            returnValues[0] = target.getScaleX();
            returnValues[1] = target.getScaleY();
            return 2;
        } else if (tweenType == TWEEN_SIZE) {
            returnValues[0] = target.getWidth();
            return 1;
        }
        return 0;
    }

    @Override
    public void setValues(Ball target, int tweenType, float[] newValues)
    {
        if (tweenType == TWEEN_SCALE) {
            target.setScale(newValues[0], newValues[1]);
            target.setPosition(target.getOriginalX(), target.getOriginalY());
        } else if (tweenType == TWEEN_SIZE) {
            if (newValues[0] < 0)
                return;
            target.setSize(newValues[0], newValues[0]);
            target.setPosition(target.getOriginalX(), target.getOriginalY());
            target.invalidate(); // because of setSize()
        }
    }

    public TextureRegion getBallRegion()
    {
        return ballRegion;
    }
}
