package network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import managers.PartFactory;
import objects.GridActor;
import objects.PuzzleElement;
import objects.Tetromino;
import screens.GameScreen;

public class GridDisplay extends Actor {

    private final TextureAtlas textureAtlas;
    private final int row;
    private final int col;
    private final TextureRegion backGround;
    private Tetromino tetromino;
    private byte[][] cell;
    private boolean started;

    public GridDisplay(int col, int row,TextureAtlas textureAtlas) {
        this.col = col;
        this.row = row;
        this.textureAtlas = textureAtlas;
        cell = new byte[row][col];
        Texture imgTexture = new Texture(Gdx.files.internal("defaultSet/cell.png"));
        imgTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        backGround = new TextureRegion(imgTexture);
        backGround.setRegion(0,0,imgTexture.getWidth()*col,imgTexture.getHeight()*(row-2));
        tetromino = new Tetromino();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(backGround,getX(),getY(),getWidth(),getHeight() - (GameScreen.TILE_SIZE * 2));
        super.draw(batch, parentAlpha);

        drawBoard(batch,parentAlpha);
        if(tetromino != null){
            if(tetromino.getData() != null){
                drawPiece(batch,parentAlpha);
            }
        }

    }

    private void drawBoard(Batch batch, float parentAlpha) {
        for(int row = 0; row < cell.length; row++ ){
            for(int col = 0; col < cell[row].length; col++ ){
                if(cell[row][col] == 0) continue;
                TextureAtlas.AtlasRegion region = GridActor.regions.get(Integer.valueOf(String.valueOf(cell[row][col])));
                if(region == null) continue;
                batch.draw(region,getX()+col*GameScreen.TILE_SIZE,getY()+row*GameScreen.TILE_SIZE,GameScreen.TILE_SIZE,GameScreen.TILE_SIZE);
            }
        }
    }

    private void drawPiece(Batch batch, float parentAlpha) {
        float py = tetromino.getY();
        float px = tetromino.getX();
        float tileSize = GameScreen.TILE_SIZE;

        byte[][] data = tetromino.getShape();

//        int width = tetromino.getWidth()   * GameScreen.TILE_SIZE  / 2 ;
//        int height = tetromino.getHeight() * GameScreen.TILE_SIZE  / 2 ;

        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[y].length; x++) {

                byte value = data[y][x];
                if (value == 0) continue;
                // TODO: 8/1/2018 Make option

                float positionX = (px * tileSize) + (x * tileSize);
                float positionY = (py * tileSize) + (y * tileSize);

                batch.draw(GridActor.regions.get((int) value),  getX()+positionX, getY()+positionY,tileSize,tileSize);


            }
        }
    }

    public void setPiece(byte piece, byte x, byte y, byte rotation) {
        tetromino.setData(PartFactory.allShapes[piece]);
        tetromino.setAbsX(x);
        tetromino.setAbsY(y);
        tetromino.setRotation(rotation);

    }

    public void update(byte[][] cell) {
        this.cell = cell;
    }

    public void setPieceAt(byte x, byte y, byte value) {
        this.cell[y][x] = value;
    }
}
