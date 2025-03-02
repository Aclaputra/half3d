package twilight.cat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends ScreenAdapter {
    public static final int WIDTH = 320 * 4;
    public static final int HEIGHT = 180 * 4;
    private static final float PLAYER_SPEED = 200f;
    private final SpriteBatch batch;
    private OrthographicCamera camera;


    private Main game;
    private Texture playerTexture;
    private Vector2 playerPosition;
//    private Viewport viewport;
    private IsometricRenderer renderer;

    public GameScreen(Main game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
    }

    @Override
    public void show() {
//        batch = new SpriteBatch();
//        playerTexture = new Texture(Gdx.files.internal("drop_pack/bucket.png")); // Ensure you have a player texture
//        playerPosition = new Vector2(100, 100);

        this.camera = new OrthographicCamera(WIDTH, HEIGHT);
//        this.viewport = new FitViewport(800, 600, camera); // Adjust to your needs
//        this.viewport = new FitViewport(WIDTH, HEIGHT, camera); // Adjust to your needs
//        this.viewport.apply();
//        this.camera.position.set((float) WIDTH /2 - 500, (float) HEIGHT /2, 100);
        // Target posisi tengah
        float targetX = (float) WIDTH / 2;
        float targetY = (float) HEIGHT / 2;
        float targetZ = 100f;

// Kecepatan kamera (semakin besar, semakin cepat bergerak)
        float speed = 5f;  // Sesuaikan sesuai kebutuhan

// Mendapatkan delta time
        float delta = Gdx.graphics.getDeltaTime();

// Interpolasi posisi kamera menuju target
        this.camera.position.x += (targetX - this.camera.position.x) * speed * delta;
        this.camera.position.y += (targetY - this.camera.position.y) * speed * delta * 8;
        this.camera.position.z += (targetZ - this.camera.position.z) * speed * delta;

// Memperbarui kamera
        this.renderer = new IsometricRenderer();
    }

    @Override
    public void render(float delta) {
        handleInput(delta);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.batch.setProjectionMatrix(this.camera.combined);
        this.camera.update();

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.camera.zoom -= 0.004f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.camera.zoom += 0.004f;
        }

        this.batch.begin();
//        this.batch.draw(playerTexture, playerPosition.x, playerPosition.y, 64, 64);

        this.renderer.drawGround(batch);
        this.batch.end();
    }

    private void handleInput(float delta) {
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//            playerPosition.x -= PLAYER_SPEED * delta;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            playerPosition.x += PLAYER_SPEED * delta;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
//            playerPosition.y += PLAYER_SPEED * delta;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//            playerPosition.y -= PLAYER_SPEED * delta;
//        }
    }

    @Override
    public void resize(int width, int height) {
//        this.viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        this.batch.dispose();
//        this.playerTexture.dispose();
    }
}
