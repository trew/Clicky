package se.samuelandersson.clicky.manager;

import se.samuelandersson.clicky.manager.SoundManager.GameSound;
import se.samuelandersson.clicky.util.LRUCache;
import se.samuelandersson.clicky.util.LRUCache.CacheEntryRemovedListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;

public class SoundManager implements
        CacheEntryRemovedListener<GameSound, Sound>, Disposable
{

    public enum GameSound
    {
        CLICK("sound/click.wav"),
        SWOOP("sound/swoop.wav");

        private String fileName;

        private GameSound(String fileName)
        {
            this.fileName = fileName;
        }

        public String getFileName()
        {
            return fileName;
        }
    }

    private float volume;

    private boolean enabled;

    private final LRUCache<GameSound, Sound> soundCache;

    public SoundManager()
    {
        enabled = true;
        volume = 1f;
        soundCache = new LRUCache<SoundManager.GameSound, Sound>(10);
        soundCache.setEntryRemovedListener(this);
    }

    public long play(GameSound sound)
    {
        return play(sound, 1, false);
    }

    public long play(GameSound sound, float volume, boolean loop)
    {
        if (!enabled)
            return -1;

        Sound soundToPlay = getSound(sound);

        if (soundToPlay != null) {
            long id = soundToPlay.play(this.volume * volume);
            soundToPlay.setLooping(id, loop);
            return id;
        }
        return -1;
    }

    public void stop(GameSound sound)
    {
        stop(sound, -1);
    }

    public void stop(GameSound sound, long id)
    {
        if (!enabled)
            return;

        Sound soundToStop = getSound(sound);

        if (soundToStop != null) {
            if (id >= 0)
                soundToStop.stop(id);
            else
                soundToStop.stop();
        }

    }

    /**
     * Tries to load the sound and return it. Returns null on failure.
     */
    public Sound getSound(GameSound gameSound)
    {
        // try to load from cache
        Sound sound = soundCache.get(gameSound);

        // load from file, save to cache
        if (sound == null) {
            FileHandle soundFile = Gdx.files.internal(gameSound.getFileName());
            try {
                sound = Gdx.audio.newSound(soundFile);
            } catch (RuntimeException e) {
                return null;
            }
            soundCache.add(gameSound, sound);
        }
        return sound;
    }

    public float getVolume()
    {
        return volume;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setVolume(float volume)
    {
        if (volume < 0f || volume > 1f) {
            throw new IllegalArgumentException("Volume must be within 0-1");
        }
        this.volume = volume;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    @Override
    public void dispose()
    {
        for (Sound sound : soundCache.retrieveAll()) {
            sound.stop();
            sound.dispose();
        }
    }

    @Override
    public void notifyEntryRemoved(GameSound key, Sound value)
    {
        value.dispose();
    }

}
