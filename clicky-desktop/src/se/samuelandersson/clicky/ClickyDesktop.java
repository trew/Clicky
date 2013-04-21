package se.samuelandersson.clicky;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class ClickyDesktop
{
    public static void main(String[] args)
    {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Clicky";
        cfg.useGL20 = false;
        cfg.width = 1280;
        cfg.height = 720;
        cfg.useCPUSynch = true;
        cfg.vSyncEnabled = true;

        new LwjglApplication(new Clicky(), cfg);
    }
}
