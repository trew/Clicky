package se.samuelandersson.clicky.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Ball extends Image
{
    private boolean killed = false;
    private float timeAlive;

    private float originalX;
    private float originalY;

    protected Ball(float x, float y, float speed, BallManager manager)
    {
        super(manager.getBallRegion());
        originalX = x;
        originalY = y;
        setPosition(x, y);
        setSize(0, 0);
    }

    public void kill()
    {
        killed = true;
        setTouchable(Touchable.disabled);
    }

    public boolean isAlive()
    {
        return !killed;
    }

    public float getTimeAlive()
    {
        return timeAlive;
    }

    public float getOriginalX()
    {
        return originalX - getWidth() * getScaleX() / 2;
    }

    public float getOriginalY()
    {
        return originalY - getHeight() * getScaleY() / 2;
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);

        if (!killed) {
            timeAlive += delta;
        }
    }

    @Override
    public Actor hit(float x, float y, boolean touchable)
    {
        if (super.hit(x, y, touchable) != null) {
            float radius = getWidth() / 2;
            Vector2 mouseIn = new Vector2(x, y);
            Vector2 middlePoint = new Vector2(radius, radius);

            return mouseIn.dst(middlePoint) < radius ? this : null;
        }
        return null;
    }
}
