package se.samuelandersson.clicky.screen;

import se.samuelandersson.clicky.Clicky;
import se.samuelandersson.clicky.game.Highscores;
import se.samuelandersson.clicky.game.Highscores.Score;
import se.samuelandersson.clicky.manager.MusicManager.Song;
import se.samuelandersson.clicky.manager.SoundManager.GameSound;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class HighscoreScreen extends AbstractScreen
{
    private Image title;

    private TextButton backButton;

    private Table table;

    public HighscoreScreen(Clicky app)
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
        backButton = new TextButton("Back", getSkin());

        /* ****** ADD LISTENERS ****** */
        backButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                app.getSoundManager().play(GameSound.CLICK);
                app.setScreen(app.getMenuScreen());
            }
        });

        setupHighscores();
    }

    private void setupHighscores()
    {
        /* ******* SETUP TABLE ******** */
        table = super.getTable();
        table.clear();
        table.row().padTop(100f);

        Array<Score> scores = Highscores.getHighscores();
        for (Score score : scores) {
            Label l = new Label(score.getName() + " - " + score.getScore(),
                    getSkin());
            table.row();
            table.add(l);
        }

        table.row();
        table.add(backButton).size(350, 70).uniform().spaceBottom(10f);
    }

    @Override
    public void show()
    {
        super.show();
        app.getMusicManager().play(Song.MENU);
        setupHighscores();
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
