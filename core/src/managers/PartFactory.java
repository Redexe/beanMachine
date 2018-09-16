package managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Pool;

import objects.PuzzleElement;
import objects.Tetromino;

import static objects.PuzzleElement.PartData.*;

public class PartFactory {
    public static final PuzzleElement.PartData[] allShapes;
    private final Array<Tetromino> tetraminos;
    private final Array<Tetromino> heldTetraminos;
    static {

        final Array<PuzzleElement.PartData> parts = new Array<PuzzleElement.PartData>(true,7,PuzzleElement.PartData.class);

        parts.add(O_Shape);
//        parts.add(I_Shape);
//        parts.add(J_Shape);
//        parts.add(L_Shape);
//        parts.add(T_Shape);
//        parts.add(S_Shape);
//        parts.add(Z_Shape);

        allShapes = parts.toArray();

    }

    private final Pool<Tetromino> tetraminosPool = new Pool<Tetromino>() {
        @Override
        protected Tetromino newObject() {
            int randomIndex = randomPart();
            PuzzleElement.PartData part = allShapes[randomIndex];
            part.setTexture(textures.get(part));
            return new Tetromino(part).setIndex(randomIndex);
        }
    };

    private Tetromino extraPart;
    private ArrayMap<PuzzleElement.PartData, Texture> textures;

    public PartFactory(int preLoad, final TextureAtlas textureAtlas){
        preLoad++;
        textures = new ArrayMap<PuzzleElement.PartData, Texture> ();
        tetraminos = new Array<Tetromino>(true,preLoad,Tetromino.class);
        heldTetraminos = new Array<Tetromino>(true,preLoad,Tetromino.class);
        for(int i = 0; i < preLoad; i++)
            tetraminos.add(tetraminosPool.obtain());
        final SpriteCache cache = new SpriteCache();

        Gdx.app.postRunnable(new Runnable(){
            @Override
            public void run() {
//
//                final float tileSize = GameScreen.TILE_SIZE;
////              All regions should be the same size
//                TextureAtlas.AtlasRegion dummyRegion = textureAtlas.findRegion(String.valueOf(0));
//                int width = dummyRegion.getRegionWidth();
//                int height = dummyRegion.getRegionHeight();
//
//                for(int i = 0; i < allShapes.length; i++){
//
//                    PuzzleElement.PartData p = allShapes[i];
//
//                    cache.beginCache();
//                    for(int y = 0; y < p.getData()[0].length; y++){
//                        for(int x = 0; x < p.getData()[0][y].length; x++){
//                            byte value = p.getData()[0][y][x];
//                            if(value > 0){
//
//                                TextureAtlas.AtlasRegion region = textureAtlas.findRegion(String.valueOf(value));
//                                int positionX = (x*width);
//                                int positionY = (y*height);
//                                cache.add(region,positionX,positionY);
//                                                            }
//                        }
//                    }
//                    cache.endCache();
//                }

            }
        });

    }

    public Array<Tetromino> getTetraminos() {
        return tetraminos;
    }

    public Tetromino newPiece() {
        final Tetromino tet = tetraminos.first();
        tetraminos.removeIndex(0);
        tetraminos.insert(tetraminos.size-1,tetraminosPool.obtain());
        return tet;
    }

    public void free(Tetromino tetromino) {
        tetraminosPool.free(tetromino);
    }


    private int randomPart() {
        return MathUtils.random(0,allShapes.length-1);
    }

    public Tetromino get(int index) {
        return tetraminos.get(index);
    }

    public void hold() {
        if(heldTetraminos.size == 0){
            heldTetraminos.add(tetraminos.get(0));
            tetraminos.removeIndex(0);
            if(extraPart == null) extraPart = tetraminosPool.obtain();
            tetraminos.insert(tetraminos.size-1,extraPart);

        }else{
            tetraminos.insert(0,heldTetraminos.first());
            heldTetraminos.removeIndex(0);
            tetraminos.removeValue(extraPart, true);
        }

    }

    public Tetromino held() {
        if(heldTetraminos.size == 0) return null;
        return heldTetraminos.get(0);
    }

    int partIndex = 0;

    public Tetromino iterateParts(){
        PuzzleElement.PartData part = allShapes[partIndex];
        partIndex++;
        if(partIndex >= allShapes.length)
            partIndex = 0;

        return tetraminosPool.obtain().setData(part);
    }

    public void setFirst(Tetromino first) {
        tetraminos.insert(0,first);
    }

    public int getFutureCount() {
        return tetraminos.size;
    }
}
