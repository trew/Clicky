package se.samuelandersson.clicky.manager;

import se.samuelandersson.clicky.Clicky;
import se.samuelandersson.clicky.util.Resolution;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PreferencesManager
{
    private static final String PREF_SOUND_VOLUME = "sound.volume";
    private static final String PREF_MUSIC_VOLUME = "music.volume";
    private static final String PREF_MUSIC_ENABLED = "music.enabled";
    private static final String PREF_SOUND_ENABLED = "sound.enabled";
    private static final String PREF_FULLSCREEN_ENABLED = "fullscreen.enabled";
    private static final String PREF_RESOLUTION_WIDTH = "resolution.width";
    private static final String PREF_RESOLUTION_HEIGHT = "resolution.height";
    private static final String PREFS_NAME = Clicky.class.getSimpleName();

    public PreferencesManager()
    {
    }

    protected Preferences getPrefs()
    {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    public boolean isSoundEnabled()
    {
        return getPrefs().getBoolean(PREF_SOUND_ENABLED, true);
    }

    public void setSoundEnabled(boolean enabled)
    {
        getPrefs().putBoolean(PREF_SOUND_ENABLED, enabled);
        getPrefs().flush();
    }

    public boolean isMusicEnabled()
    {
        return getPrefs().getBoolean(PREF_MUSIC_ENABLED, true);
    }

    public void setMusicEnabled(boolean enabled)
    {
        getPrefs().putBoolean(PREF_MUSIC_ENABLED, enabled);
        getPrefs().flush();
    }

    public float getSoundVolume()
    {
        return getPrefs().getFloat(PREF_SOUND_VOLUME, 0.5f);
    }

    public void setSoundVolume(float vol)
    {
        getPrefs().putFloat(PREF_SOUND_VOLUME, vol);
        getPrefs().flush();
    }

    public float getMusicVolume()
    {
        return getPrefs().getFloat(PREF_MUSIC_VOLUME, 0.5f);
    }

    public void setMusicVolume(float vol)
    {
        getPrefs().putFloat(PREF_MUSIC_VOLUME, vol);
        getPrefs().flush();
    }

    public boolean isFullscreenEnabled()
    {
        return getPrefs().getBoolean(PREF_FULLSCREEN_ENABLED, false);
    }

    public void setFullscreenEnabled(boolean enabled)
    {
        getPrefs().putBoolean(PREF_FULLSCREEN_ENABLED, enabled);
        getPrefs().flush();
    }

    public Resolution getResolution()
    {
        int width = getPrefs().getInteger(PREF_RESOLUTION_WIDTH, 1280);
        int height = getPrefs().getInteger(PREF_RESOLUTION_HEIGHT, 720);
        return new Resolution(width, height);
    }

    public void setResolution(Resolution res)
    {
        getPrefs().putInteger(PREF_RESOLUTION_WIDTH, res.getWidth());
        getPrefs().putInteger(PREF_RESOLUTION_HEIGHT, res.getHeight());
        getPrefs().flush();
    }
}
