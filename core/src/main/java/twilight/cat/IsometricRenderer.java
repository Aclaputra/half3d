package twilight.cat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class IsometricRenderer {
    public static final int TILE_WIDTH = 64;
    public static final int TILE_HEIGHT = 64;
    private Texture grass;
    private Texture sky;

    public IsometricRenderer() {
        grass = new Texture(Gdx.files.internal("isometric_tileset/Grass.png"));
        sky = new Texture(Gdx.files.internal("drop_pack/bucket.png"));
    }

    public void drawGround(SpriteBatch batch) {
        for (int row = 15; row >= 0; row--) {
//            for (int col = 15; row >= 0; row--) {
            for (int col = 15; col >= 0; col--) {
                float x = (col - row) * (TILE_WIDTH / 2f);
                float y = (col + row) * (TILE_HEIGHT / 5f);

                // set grass position
                batch.draw(grass, x, y, TILE_WIDTH, TILE_HEIGHT);
            }
        }
    }
}
