package se.samuelandersson.clicky;

import se.samuelandersson.clicky.manager.MusicManager;
import se.samuelandersson.clicky.manager.PreferencesManager;
import se.samuelandersson.clicky.manager.SoundManager;
import se.samuelandersson.clicky.screen.AbstractScreen;
import se.samuelandersson.clicky.screen.GameScreen;
import se.samuelandersson.clicky.screen.HighscoreScreen;
import se.samuelandersson.clicky.screen.MenuScreen;
import se.samuelandersson.clicky.screen.OptionsScreen;
import se.samuelandersson.clicky.util.Resolution;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Clicky implements ApplicationListener
{
    public static final boolean DEBUG = false;
    public static boolean DEBUGDRAW = false;
    private static final String SKIN_PATH = "skin/uiskin.json";
    private static final String ATLAS_PATH = "images/atlas/pages.atlas";

    private TextureAtlas atlas;
    private Skin skin;
    private SoundManager soundManager;
    private MusicManager musicManager;
    private PreferencesManager prefs;

    private AbstractScreen screen;
    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private OptionsScreen optionsScreen;
    private HighscoreScreen highscoreScreen;

    private BitmapFont debugFont;

    private float accum;
    private final float deltaTime = 1 / 60f;

    @Override
    public void create()
    {
        atlas = new TextureAtlas(Gdx.files.internal(ATLAS_PATH));
        skin = new Skin(Gdx.files.internal(SKIN_PATH));

        Resolution.loadResolutions();

        prefs = new PreferencesManager();
        Resolution r = prefs.getResolution();
        Gdx.graphics.setDisplayMode(r.getWidth(), r.getHeight(),
                prefs.isFullscreenEnabled());

        soundManager = new SoundManager();
        soundManager.setEnabled(prefs.isSoundEnabled());
        soundManager.setVolume(prefs.getSoundVolume());

        musicManager = new MusicManager();
        musicManager.setEnabled(prefs.isMusicEnabled());
        musicManager.setVolume(prefs.getMusicVolume());

        setScreen(getMenuScreen());
    }

    @Override
    public void dispose()
    {
        if (screen != null) {
            screen.hide();
            screen.dispose();
        }
    }

    @Override
    public void render()
    {
        if (screen != null) {
            accum += Gdx.graphics.getDeltaTime();
            while (accum > deltaTime) {
                screen.update(deltaTime);
                accum -= deltaTime;
            }
            screen.render();
            screen.postRender();
        }
    }

    @Override
    public void resize(int width, int height)
    {
        if (screen != null)
            screen.resize(width, height);
    }

    @Override
    public void pause()
    {
    }

    @Override
    public void resume()
    {
    }

    public TextureAtlas getAtlas()
    {
        return atlas;
    }

    public Skin getSkin()
    {
        return skin;
    }

    public GameScreen getGameScreen()
    {
        if (gameScreen == null) {
            gameScreen = new GameScreen(this);
            gameScreen.initialize();
        }
        return gameScreen;
    }

    public MenuScreen getMenuScreen()
    {
        if (menuScreen == null) {
            menuScreen = new MenuScreen(this);
            menuScreen.initialize();
        }
        return menuScreen;
    }

    public HighscoreScreen getHighscoreScreen()
    {
        if (highscoreScreen == null) {
            highscoreScreen = new HighscoreScreen(this);
            highscoreScreen.initialize();
        }
        return highscoreScreen;
    }

    public OptionsScreen getOptionsScreen()
    {
        if (optionsScreen == null) {
            optionsScreen = new OptionsScreen(this);
            optionsScreen.initialize();
        }
        return optionsScreen;
    }

    public void setScreen(AbstractScreen screen)
    {
        if (screen != null)
            screen.hide();
        this.screen = screen;
        if (screen != null) {
            screen.show();
            screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    public SoundManager getSoundManager()
    {
        return soundManager;
    }

    public MusicManager getMusicManager()
    {
        return musicManager;
    }

    public PreferencesManager getPreferences()
    {
        return prefs;
    }

    public BitmapFont getDebugFont()
    {
        if (debugFont == null) {
            FileHandle debugFontHandle = Gdx.files
                    .internal("fonts/consolas.fnt");
            debugFont = new BitmapFont(debugFontHandle, false);
        }
        return debugFont;

    }
}
