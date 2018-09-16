package objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;


public class PartDisplay extends Actor {

    private final TextureAtlas textureAtlas;
    private int px,py;
    private float tileSize;
    private Tetromino tetromino;
    private Drawable backGround;

    public PartDisplay(TextureAtlas textureAtlas){
        this.textureAtlas = textureAtlas;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        backGround.draw(batch,getX(),getY(),getWidth(),getHeight());
        if (tetromino == null) return;



        byte[][] data = tetromino.getShape();
        for(int y = 0; y < data.length; y++){
            for(int x = 0; x < data[y].length; x++){

                byte value = data[y][x];
                if(value == 0) continue;

                TextureAtlas.AtlasRegion region = textureAtlas.findRegion(String.valueOf(value));
                batch.draw(region,
                        ((bounds.x * tileSize) / 2 )+( px * tileSize ) + getX() + ( x * tileSize ),
                        ((bounds.y * tileSize) / 2 )+( py * tileSize ) + getY() + ( y * tileSize ),
                        tileSize,tileSize);

            }
        }
    }

    public Tetromino getTetromino() {
        return tetromino;
    }

    @Override
    public void act(float delta) {
        if(tileSize == 0){
            tileSize = Math.min(getWidth(),getHeight()) / 5;
        }
    }

    public void setTileSize(float tileSize) {
        this.tileSize = tileSize;
    }
    Rectangle bounds = new Rectangle(0,0,0,0);
    public PartDisplay setPart(Tetromino tetromino) {
        this.tetromino = tetromino;
        if(tetromino == null) return null;

        byte[][] data = tetromino.getShape();

        bounds.set(0,0,0,0);
        for(int y = 0; y < data.length; y++){
            for(int x = 0; x < data[y].length; x++){

                byte value = data[y][x];
                if(value == 0) continue;

                Rectangle rectangle = new Rectangle(x,y,1,1);
                bounds.merge(rectangle);

            }
        }
        return this;
    }

    public void setBackGround(Drawable backGround) {
        this.backGround = backGround;
    }
}
