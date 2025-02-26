package twilight.cat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    Rectangle bucketRectangle;
    Rectangle dropRectangle;
    Vector2 touchPos;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;

    private Texture backgroundTxr;
    private Texture bucketTxr;

    private Texture dropTxr;
    private Sprite bucketSprite;
    Array<Sprite> dropSprites;
    float dropTimer;
    Sound dropSound;
    Music currentMusic;

    private int score;
    private int lostDroplet;
    private Texture fontTxr;
    private BitmapFont bitmapFont;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8,5);

        backgroundTxr = new Texture("drop_pack/background.png");
        bucketTxr = new Texture("drop_pack/bucket.png");
        bucketSprite = new Sprite(bucketTxr);
        bucketSprite.setSize(1,1);
        dropTxr = new Texture("drop_pack/drop.png");
        dropSprites = new Array<>();

        // fonts
//        if (!Gdx.files.internal("fonts/PlayfairDisplayRegular-ywLOY.ttf").exists()) {
//            throw new RuntimeException("Font file not found!");
//        }
//        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PlayfairDisplayRegular-ywLOY.ttf"));
//        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.size = 12;
//        font = generator.generateFont(parameter); // font size 12 pixels
        fontTxr = new Texture(Gdx.files.internal("drop_pack/lsans-15.png"));
        fontTxr.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        bitmapFont = new BitmapFont(Gdx.files.internal("drop_pack/lsans-15.fnt"), new TextureRegion(fontTxr));
        bitmapFont.getData().scale(3f);

        bucketRectangle = new Rectangle();
        dropRectangle = new Rectangle();

        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop_pack/drop.mp3"));
        currentMusic = Gdx.audio.newMusic(Gdx.files.internal("drop_pack/music.mp3"));

        touchPos = new Vector2();

        currentMusic.setLooping(true);
        currentMusic.setVolume(.5f);
        currentMusic.play();
    }

    private void createDroplet() {
        // create local variable for convenience
        float dropWidth = 1;
        float dropHeight = 1;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        Sprite dropSprite = new Sprite(dropTxr);
        dropSprite.setSize(dropWidth, dropHeight);
        // randomize with mathutils
        dropSprite.setX(MathUtils.random(0f, worldWidth - dropWidth)); // Randomize the drop's x position
        dropSprite.setY(worldHeight);
        dropSprites.add(dropSprite);
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() {
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime(); // retrieve the current delta
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucketSprite.translateX(speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bucketSprite.translateX(-speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            bucketSprite.translateY(speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            bucketSprite.translateY(-speed * delta);
        }

        if (Gdx.input.isTouched()) { // If the user has clicked or tapped the screen
            touchPos.set(Gdx.input.getX(), Gdx.input.getY()); // Get where the touch happened on screen
            viewport.unproject(touchPos); // Convert the units to the world units of the viewport
            bucketSprite.setCenterX(touchPos.x); // Change the horizontally centered position of the bucket
        }
    }

    private void logic() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float bucketWidth = bucketSprite.getWidth();
        float bucketHeight = bucketSprite.getHeight();

        bucketSprite.setX(MathUtils.clamp(bucketSprite.getX(),0, worldWidth - bucketWidth));
        bucketSprite.setY(MathUtils.clamp(bucketSprite.getY(), 0, worldHeight - bucketHeight));

        float delta = Gdx.graphics.getDeltaTime(); // retrieve the current delta
        // apply the bucket position and size to the bucketRectangle
        bucketRectangle.set(bucketSprite.getX(), bucketSprite.getY(), bucketWidth, bucketHeight);

        // Loop through the sprites backwards to prevent out of bounds error
        for (int i = dropSprites.size-1; i>=0; i--) {
            Sprite dropSprite = dropSprites.get(i); // Get the sprite from the list
            float dropWidth = dropSprite.getWidth();
            float dropHeight = dropSprite.getHeight();

            dropSprite.translateY(-2f * delta); // move the drop downward every frame
            dropRectangle.set(dropSprite.getX(), dropSprite.getY(), dropWidth, dropHeight);

            // if the top of the drop goes below the bottom of the view, remove it
            if (dropSprite.getY() < -dropHeight) {
                lostDroplet++;
                dropSprites.removeIndex(i);
            } else if (bucketRectangle.overlaps(dropRectangle)) { // if drop collision to the bucket
                score++;
                dropSprites.removeIndex(i);
                dropSound.play(); // play the sound
            }
        }

        dropTimer += delta;
        if (dropTimer > 1f) { // check if it has been more than a second
            dropTimer = 0; // reset
            createDroplet();
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.SKY);
        viewport.apply();;

        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
//        font.draw(spriteBatch, "Hello There", 10, 10);

        float wordWidth = viewport.getWorldWidth();
        float wordHeight = viewport.getWorldHeight();

        spriteBatch.draw(backgroundTxr,0,0, wordWidth, wordHeight); // draw the background
        bucketSprite.draw(spriteBatch);

        // draw each sprite
        for (Sprite dropSprite : dropSprites) {
            dropSprite.draw(spriteBatch);
        }

//        bitmapFont.draw(spriteBatch, "Hello, LibGDX!", 100, 200);
        bitmapFont.draw(spriteBatch, "TEST", 100, (float) Gdx.graphics.getHeight() /2);



        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        bitmapFont.dispose();
    }
}
