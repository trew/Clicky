package se.samuelandersson.clicky.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class MusicManager implements Disposable
{

    public enum Song
    {
        MENU("music/Sabrepulse - Explore.mp3"),
        GAME("music/Rymdkraft - Ultramumie.mp3");

        private final String fileName;

        private Song(String fileName)
        {
            this.fileName = fileName;
        }

        public String getFileName()
        {
            return fileName;
        }
    }

    private Music musicBeingPlayed;
    private Song currentSong;

    private float volume;

    private boolean enabled;

    public MusicManager()
    {
        enabled = true;
        volume = 1f;
    }

    public boolean isPlaying(Song song)
    {
        if (song.equals(currentSong))
            return musicBeingPlayed.isPlaying();
        return false;
    }

    /**
     * Stops the current song and plays the provided song from the beginning
     * 
     * @param song
     */
    public void replay(Song song)
    {
        // stop current music
        stop();

        FileHandle musicFile = Gdx.files.internal(song.getFileName());
        try {
            musicBeingPlayed = Gdx.audio.newMusic(musicFile);
        } catch (GdxRuntimeException e) { // file not found
            return;
        }
        musicBeingPlayed.setVolume(volume);
        musicBeingPlayed.setLooping(true);
        currentSong = song;
        musicBeingPlayed.play();
    }

    /**
     * Play a song. If the song is already playing, the song continues playing
     * 
     * @see #replay(Song)
     * @param song
     */
    public void play(Song song)
    {
        if (!enabled || isPlaying(song))
            return;

        replay(song);
    }

    public void stop()
    {
        if (musicBeingPlayed != null) {
            currentSong = null;
            musicBeingPlayed.stop();
            musicBeingPlayed.dispose();
        }
    }

    public float getVolume()
    {
        return volume;
    }

    public void setVolume(float volume)
    {
        if (volume < 0 || volume > 1f)
            throw new IllegalArgumentException("Volume must be within 0-1.");
        this.volume = volume;

        if (musicBeingPlayed != null)
            musicBeingPlayed.setVolume(this.volume);
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
        if (!enabled)
            stop();
    }

    @Override
    public void dispose()
    {
        stop();
    }

}
