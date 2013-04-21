package se.samuelandersson.clicky.util;

import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.utils.Array;

public class Resolution
{
    private int width;
    private int height;
    private String ratio;
    static private Resolution[] resolutions;

    public Resolution(int width, int height)
    {
        this.width = width;
        this.height = height;
        float epsilon = 0.01f;
        if (Math.abs((width / (float) height) - (4f / 3f)) < epsilon) {
            ratio = "4:3";
        } else if (Math.abs((width / (float) height) - (16f / 10f)) < epsilon) {
            ratio = "16:10";
        } else if (Math.abs((width / (float) height) - (16f / 9f)) < epsilon) {
            ratio = "16:9";
        } else {
            ratio = "";
        }
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public float getRatio()
    {
        return width / (float) height;
    }

    @Override
    public boolean equals(Object val)
    {
        if (val instanceof Resolution) {
            Resolution him = (Resolution) val;
            return him.getWidth() == getWidth()
                    && him.getHeight() == getHeight();
        }
        return false;
    }

    @Override
    public String toString()
    {
        return width + "x" + height + "  ( " + ratio + " )";
    }

    static public void loadResolutions()
    {
        Array<Resolution> allowed = new Array<Resolution>();
        // 4:3
        allowed.add(new Resolution(800, 600));
        allowed.add(new Resolution(1024, 768));
        allowed.add(new Resolution(1152, 864));
        allowed.add(new Resolution(1280, 960));
        allowed.add(new Resolution(1400, 1050));
        allowed.add(new Resolution(1440, 1080));
        allowed.add(new Resolution(1600, 1200));

        // 16:10
        allowed.add(new Resolution(1280, 800));
        allowed.add(new Resolution(1440, 900));
        allowed.add(new Resolution(1680, 1050));
        allowed.add(new Resolution(1920, 1200));

        // 16:9
        allowed.add(new Resolution(1280, 720));
        allowed.add(new Resolution(1366, 768));
        allowed.add(new Resolution(1600, 900));
        allowed.add(new Resolution(1920, 1080));

        Array<Resolution> ress = new Array<Resolution>();
        for (DisplayMode mode : Gdx.graphics.getDisplayModes()) {
            Resolution res = new Resolution(mode.width, mode.height);
            if (!ress.contains(res, false) && allowed.contains(res, false)) {
                ress.add(res);
            }
        }

        Comparator<Resolution> comp = new Comparator<Resolution>()
        {
            @Override
            public int compare(Resolution o1, Resolution o2)
            {
                if (Math.abs((o1.getWidth() / (float) o1.getHeight())
                        - (o2.getWidth() / (float) o2.getHeight())) < 0.01f) {
                    return o1.getWidth() < o2.getWidth() ? -1 : 1;
                }
                return o1.getRatio() > o2.getRatio() ? -1 : 1;
            }
        };

        ress.sort(comp);
        resolutions = ress.toArray(Resolution.class);
    }

    static public Resolution[] getResolutions()
    {
        return resolutions;
    }

}
