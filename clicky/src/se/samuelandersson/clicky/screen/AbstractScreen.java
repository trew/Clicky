package se.samuelandersson.clicky.screen;

import se.samuelandersson.clicky.Clicky;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class AbstractScreen implements InputProcessor
{
    public static int VIEWPORT_WIDTH = 1280;
    public static final int VIEWPORT_HEIGHT = 720;

    private static final String SKIN_PATH = "skin/uiskin.json";

    static protected Clicky app;
    protected final Stage stage;

    private BitmapFont font;
    private SpriteBatch batch;
    private Table table;
    private TextureAtlas atlas;
    private Skin skin;

    protected OrthographicCamera stageCamera;

    protected InputMultiplexer inputMultiplexer;

    protected AbstractScreen(Clicky app)
    {
        AbstractScreen.app = app;
        this.font = new BitmapFont();
        this.batch = new SpriteBatch();
        this.stage = new Stage(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, true);
        this.stageCamera = new OrthographicCamera(VIEWPORT_WIDTH,
                VIEWPORT_HEIGHT);
        stage.setCamera(stageCamera);
    }

    protected String getName()
    {
        return getClass().getSimpleName();
    }

    public int getWidth()
    {
        return VIEWPORT_WIDTH;
    }

    public int getHeight()
    {
        return VIEWPORT_HEIGHT;
    }

    public abstract void update(float delta);

    public void render()
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }

    public void postRender()
    {
        if (Clicky.DEBUG) {
            Table.drawDebug(stage);
        }
    }

    public void resize(int width, int height)
    {
        VIEWPORT_WIDTH = (int) (720 * (width / (float) height));
        stageCamera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        stage.setViewport(width, height, true);
        stageCamera.update();
    }

    public abstract void initialize();

    public void show()
    {
        Gdx.input.setInputProcessor(getInputMultiplexer());
    }

    public void hide()
    {
    }

    public void dispose()
    {
        if (font != null)
            font.dispose();
        if (skin != null)
            skin.dispose();
        if (atlas != null)
            atlas.dispose();
        if (stage != null)
            stage.dispose();
        if (batch != null)
            batch.dispose();
    }

    public Clicky getApp()
    {
        return app;
    }

    public Stage getStage()
    {
        return stage;
    }

    public BitmapFont getFont()
    {
        if (font == null)
            font = new BitmapFont();
        return font;
    }

    public SpriteBatch getBatch()
    {
        if (batch == null)
            batch = new SpriteBatch();
        return batch;
    }

    public TextureAtlas getAtlas()
    {
        return app.getAtlas();
    }

    protected Skin getSkin()
    {
        if (skin == null) {
            skin = new Skin(Gdx.files.internal(SKIN_PATH));
        }
        return skin;
    }

    protected Table getTable()
    {
        if (table == null) {
            table = new Table(getSkin());
            table.setFillParent(true);
            if (Clicky.DEBUG)
                table.debug();
            stage.addActor(table);
        }
        return table;
    }

    public InputMultiplexer getInputMultiplexer()
    {
        if (inputMultiplexer == null) {
            inputMultiplexer = new InputMultiplexer();
            inputMultiplexer.addProcessor(stage);
            inputMultiplexer.addProcessor(this);
        }
        return inputMultiplexer;
    }

    @Override
    public boolean keyDown(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }

}
