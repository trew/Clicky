package se.samuelandersson.clicky.screen;

import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import se.samuelandersson.clicky.Clicky;
import se.samuelandersson.clicky.manager.MusicManager.Song;
import se.samuelandersson.clicky.manager.SoundManager.GameSound;
import se.samuelandersson.clicky.util.Resolution;

public class OptionsScreen extends AbstractScreen
{

    private CheckBox soundEffects;
    private CheckBox musicEnabled;
    private CheckBox fullscreenEnabled;
    private TextButton cancelButton;
    private TextButton applyButton;
    private Slider soundVolumeSlider;
    private Slider musicVolumeSlider;
    private Label musicVolumeLabel;
    private Label soundVolumeLabel;

    private ScrollPane resolutionPane;
    private List resolutionList;
    private Resolution[] resolutions;

    private Table table;

    public OptionsScreen(Clicky game)
    {
        super(game);
    }

    @Override
    public void initialize()
    {
        /* ********* CREATE ACTORS ******** */

        soundEffects = new CheckBox("", getSkin());
        soundVolumeSlider = new Slider(0f, 1f, 0.1f, false, getSkin());
        soundVolumeLabel = new Label("", getSkin());

        musicEnabled = new CheckBox("", getSkin());
        musicVolumeSlider = new Slider(0f, 1f, 0.1f, false, getSkin());
        musicVolumeLabel = new Label("", getSkin());

        fullscreenEnabled = new CheckBox("", getSkin());

        resolutions = Resolution.getResolutions();
        resolutionList = new List(resolutions, getSkin());
        resolutionPane = new ScrollPane(resolutionList, getSkin());
        resolutionPane.setFadeScrollBars(false);
        resolutionPane.setFlickScroll(true);
        resolutionPane.setScrollingDisabled(true, false);

        cancelButton = new TextButton("Back", getSkin());
        applyButton = new TextButton("Apply", getSkin());

        /* ********** ADD LISTENERS ********* */
        soundVolumeSlider.addListener(new ChangeListener()
        {

            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                updateSoundVolumeLabel(((Slider) actor).getValue());
            }
        });
        musicVolumeSlider.addListener(new ChangeListener()
        {

            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                updateMusicVolumeLabel(((Slider) actor).getValue());
            }
        });
        cancelButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                loadOptionsValues();
                app.getSoundManager().play(GameSound.CLICK);
                app.setScreen(app.getMenuScreen());
            }
        });
        applyButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                app.getSoundManager().play(GameSound.CLICK);
                applyChanges();
                loadOptionsValues();
            }
        });
        /* ********** SETUP TABLE *********** */

        table = super.getTable();
        table.padTop(100);
        table.defaults().spaceBottom(20);
        table.columnDefaults(0).spaceRight(20);
        table.columnDefaults(1).spaceRight(5);

        table.add("Options").colspan(4);

        table.row();
        table.add("Sound Effects").right();
        table.add(soundEffects).left();
        table.add(soundVolumeSlider);
        table.add(soundVolumeLabel).width(40f).padLeft(15f);

        table.row();
        table.add("Music").right();
        table.add(musicEnabled).left();
        table.add(musicVolumeSlider);
        table.add(musicVolumeLabel).width(40f).padLeft(15f);

        table.row();
        table.add("Fullscreen").right();
        table.add(fullscreenEnabled).left();

        table.row().height(125);
        table.add("Resolution").right();
        table.add(resolutionPane).colspan(3).left().fill();

        table.row();
        table.add(applyButton).size(200, 40).colspan(2);
        table.add(cancelButton).size(200, 40).colspan(2).right();

    }

    @Override
    public void show()
    {
        super.show();

        app.getMusicManager().play(Song.MENU);

        /* ********** SET VALUES TO ACTORS ******** */

        loadOptionsValues();
    }

    /**
     * Update the volume label.
     * 
     * @param volume
     *            0.0f - 1.0f
     */
    private void updateSoundVolumeLabel(float volume)
    {
        soundVolumeLabel.setText(String.format(Locale.US, "%1.0f%%",
                volume * 100));
    }

    private void updateMusicVolumeLabel(float volume)
    {
        musicVolumeLabel.setText(String.format(Locale.US, "%1.0f%%",
                volume * 100));
    }

    private void applyChanges()
    {
        // Music enabled
        app.getMusicManager().setEnabled(musicEnabled.isChecked());
        app.getMusicManager().play(Song.MENU);
        app.getPreferences().setMusicEnabled(musicEnabled.isChecked());

        // Music volume
        app.getMusicManager().setVolume(musicVolumeSlider.getValue());
        app.getPreferences().setMusicVolume(musicVolumeSlider.getValue());

        // SFX enabled
        app.getSoundManager().setEnabled(soundEffects.isChecked());
        app.getPreferences().setSoundEnabled(soundEffects.isChecked());

        // SFX volume
        app.getSoundManager().setVolume(soundVolumeSlider.getValue());
        app.getPreferences().setSoundVolume(soundVolumeSlider.getValue());

        // Fullscreen enabled
        if (Gdx.graphics.supportsDisplayModeChange()) {
            app.getPreferences().setFullscreenEnabled(
                    fullscreenEnabled.isChecked());

            Resolution res = resolutions[resolutionList.getSelectedIndex()];
            app.getPreferences().setResolution(res);
            Gdx.graphics.setDisplayMode(res.getWidth(), res.getHeight(),
                    fullscreenEnabled.isChecked());

        } else {
            // TODO: Show displaymode-change-error
        }
    }

    private void loadOptionsValues()
    {
        soundEffects.setChecked(app.getPreferences().isSoundEnabled());
        float soundVolume = app.getPreferences().getSoundVolume();
        soundVolumeSlider.setValue(soundVolume);
        updateSoundVolumeLabel(soundVolume);

        musicEnabled.setChecked(app.getPreferences().isMusicEnabled());
        float musicVolume = app.getPreferences().getMusicVolume();
        musicVolumeSlider.setValue(musicVolume);
        updateMusicVolumeLabel(musicVolume);

        boolean fullscreen = app.getPreferences().isFullscreenEnabled();
        fullscreenEnabled.setChecked(fullscreen);

        Resolution res = app.getPreferences().getResolution();
        resolutionList.setSelection(res.toString());
    }

    @Override
    public void render()
    {
        super.render();
        stage.draw();
    }

    @Override
    public void update(float delta)
    {
        stage.act(delta);
    }

}
