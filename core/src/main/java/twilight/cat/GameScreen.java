package twilight.cat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends ScreenAdapter {
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Model model;
    private ModelInstance modelInstance;
    private Environment environment;

    private Main game;

    public GameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(5f, 5f, 100f);  // Move camera further back
        camera.lookAt(-1500f, 5f, 0f);         // Point to the model's origin
        camera.near = 0.1f;
        camera.far = 300f;
        camera.update();

        modelBatch = new ModelBatch();

        // Create environment
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        // Create a simple cube model
//        ModelBuilder modelBuilder = new ModelBuilder();
//        model = modelBuilder.createBox(2f, 2f, 2f,
//                new Material(ColorAttribute.createDiffuse(0.3f, 0.6f, 0.9f, 1f)),
//                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("drop_pack/fish.g3dj"));
        modelInstance = new ModelInstance(model);

        System.out.println(Gdx.files.internal("drop_pack/fish.g3dj").file().getAbsolutePath());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        modelBatch.begin(camera);
        modelBatch.render(modelInstance, environment);
        modelBatch.end();
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
//        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
//        batch.dispose();
//        playerTexture.dispose();
        modelBatch.dispose();
        model.dispose();
    }
}
