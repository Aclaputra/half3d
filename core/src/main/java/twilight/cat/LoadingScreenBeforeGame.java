package twilight.cat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LoadingScreenBeforeGame implements Screen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private ProgressBar progressBar;
    private AssetManager assetManager;

    private boolean loadingFinished;

    public LoadingScreenBeforeGame(Main game) {
        this.game = game;
        assetManager = new AssetManager();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label loadingLabel = new Label("Loading Game...", skin);
        progressBar = new ProgressBar(0, 1, 0.01f, false, skin);

        table.add(loadingLabel).padBottom(20).row();
        table.add(progressBar).width(300).row();

        // **Load Game Assets**
        assetManager.load("drop_pack/background.png", Texture.class);
        assetManager.load("drop_pack/bucket.png", Texture.class);


        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                loadingFinished = true;
            }
        }, 1);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        progressBar.setValue(assetManager.getProgress());

        if (assetManager.update() && loadingFinished) {
            game.setScreen(new GameScreen(game)); // Switch to GameScreen
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
