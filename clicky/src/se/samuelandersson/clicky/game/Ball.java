package se.samuelandersson.clicky.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Ball extends Image
{
    private boolean killed = false;
    private float timeAlive;

    private float sizeSpeed;

    protected Ball(BallManager manager, float speed)
    {
        super(manager.getBallRegion());
        sizeSpeed = 30 * speed;
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

    @Override
    public void act(float delta)
    {
        super.act(delta);

        if (!killed) {
            timeAlive += delta;

            float sizeSpeed = this.sizeSpeed * delta;

            size(sizeSpeed);
            float x = getX() - sizeSpeed / 2f;
            float y = getY() - sizeSpeed / 2f;
            float w = getWidth();

            setPosition(x, y);
            setOrigin(w / 2, w / 2);
            invalidate();
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
