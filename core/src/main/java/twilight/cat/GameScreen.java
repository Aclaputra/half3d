package twilight.cat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends ScreenAdapter {
    private static final float PLAYER_SPEED = 200f;

    private Main game;
    private SpriteBatch batch;
    private Texture playerTexture;
    private Vector2 playerPosition;
    private OrthographicCamera camera;
    private Viewport viewport;

    public GameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        playerTexture = new Texture(Gdx.files.internal("drop_pack/bucket.png")); // Ensure you have a player texture

        playerPosition = new Vector2(100, 100);

        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera); // Adjust to your needs
        viewport.apply();

        camera.position.set(400, 300, 0);
    }

    @Override
    public void render(float delta) {
        handleInput(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(playerTexture, playerPosition.x, playerPosition.y, 64, 64);
        batch.end();
    }

    private void handleInput(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerPosition.x -= PLAYER_SPEED * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerPosition.x += PLAYER_SPEED * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            playerPosition.y += PLAYER_SPEED * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playerPosition.y -= PLAYER_SPEED * delta;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        batch.dispose();
        playerTexture.dispose();
    }
}
