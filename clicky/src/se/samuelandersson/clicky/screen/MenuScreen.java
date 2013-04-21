package se.samuelandersson.clicky.screen;

import se.samuelandersson.clicky.Clicky;
import se.samuelandersson.clicky.manager.MusicManager.Song;
import se.samuelandersson.clicky.manager.SoundManager.GameSound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MenuScreen extends AbstractScreen
{
    private Image title;

    private TextButton resumeGameButton;
    private TextButton newGameButton;
    private TextButton highscoresButton;
    private TextButton optionsButton;
    private TextButton exitButton;

    private Table table;

    public MenuScreen(Clicky app)
    {
        super(app);
    }

    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);
        float art_scale = width / 1920f;
        title.setScale(art_scale);
        title.setOrigin(title.getWidth() / 2, title.getHeight() / 2);
        title.setPosition(stage.getWidth() / 2 - title.getWidth() / 2,
                stage.getHeight() / 1.2f - title.getHeight() / 2);
    }

    @Override
    public void initialize()
    {
        AtlasRegion titleRegion = getAtlas().findRegion("menu-art/title");
        Drawable titleDrawable = new TextureRegionDrawable(titleRegion);

        title = new Image(titleDrawable);
        stage.addActor(title);

        /* ****** SETUP BUTTONS ****** */
        resumeGameButton = new TextButton("Resume Game", getSkin());
        newGameButton = new TextButton("New Game", getSkin());
        highscoresButton = new TextButton("Highscores", getSkin());
        optionsButton = new TextButton("Settings", getSkin());
        exitButton = new TextButton("Exit Game", getSkin());

        /* ****** ADD LISTENERS ****** */
        resumeGameButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                app.getSoundManager().play(GameSound.CLICK);
                app.setScreen(app.getGameScreen());
            }
        });
        newGameButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                app.getSoundManager().play(GameSound.CLICK);
                if (app.getGameScreen().isRunning())
                    app.getGameScreen().reset();
                app.setScreen(app.getGameScreen());
            }
        });
        highscoresButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                app.getSoundManager().play(GameSound.CLICK);
                app.setScreen(app.getHighscoreScreen());
            }
        });

        optionsButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                app.getSoundManager().play(GameSound.CLICK);
                app.setScreen(app.getOptionsScreen());
            }
        });

        exitButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.app.exit();
            }
        });

        setupTable();
    }

    private void setupTable()
    {
        /* ******* SETUP TABLE ******** */
        table = super.getTable();
        table.clear();
        table.row().padTop(100f);

        if (app.getGameScreen().isRunning()) {
            table.add(resumeGameButton).size(350, 70).uniform()
                    .spaceBottom(10f);
            table.row();
        }

        table.add(newGameButton).size(350, 70).uniform().spaceBottom(10f);

        table.row();
        table.add(highscoresButton).size(350, 70).uniform().spaceBottom(10f);

        table.row();
        table.add(optionsButton).size(350, 70).uniform().spaceBottom(10f);

        table.row();
        table.add(exitButton).size(350, 70).uniform().spaceBottom(10f);
    }

    @Override
    public void show()
    {
        super.show();
        app.getMusicManager().play(Song.MENU);
        setupTable();
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
