package objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;

import screens.GameScreen;

public abstract class Barrier extends Group{
    private TextureAtlas.AtlasRegion region;
    private final int cols;
    private final int rows;

    private int barrierValue;
    private float barrierTime,lifeSpan = 1f;
    private boolean animateing;
    private boolean complete;
    private ScaleToAction scaleAction;
    private float barrierHeightStart;
    private float barrierHeightEnd;
    private boolean update;
    private boolean interpolate;


    public Barrier(int row, int col, TextureAtlas.AtlasRegion region) {
        this.rows = row;
        this.cols = col;
        this.region = region;
        setSize(col*GameScreen.TILE_SIZE,0);
        debug();
    }

    public int getBarrierValue() {
        return barrierValue;
    }

    public void setBarrierValue(int barrierValue) {
        this.barrierValue = barrierValue;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if(update){
            barrierHeightStart = getHeight();
            barrierHeightEnd = barrierValue * GameScreen.TILE_SIZE;

            update = false;
            interpolate = true;
        }


        if(interpolate){
            System.out.println(interpolate);

            barrierTime+=delta;

            float progress = Math.min(1f, barrierTime / lifeSpan);
            float newY = Interpolation.circleIn.apply(barrierHeightStart, barrierHeightEnd, progress);
            setHeight(newY);
            change( newY , getHeight() - barrierHeightStart );
            if(progress >=1){
                barrierTime = 0;
                interpolate = false;
                complete( newY , getHeight() - barrierHeightStart);
            }
        }


    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.flush();
        if (clipBegin(getX(), getY(), getWidth(), getHeight())) {
            //do your drawing here
            for(int row = 0; row < (getHeight() / GameScreen.TILE_SIZE )+1 ;row++ ){
                for(int col = 0; col < cols;col++ ){
                    batch.draw(region, getX()+col*GameScreen.TILE_SIZE,getY()+row*GameScreen.TILE_SIZE,GameScreen.TILE_SIZE,GameScreen.TILE_SIZE);
                }
            }

            batch.flush();
            clipEnd();
        }
    }

    public void subBarrierValue(int value) {
        barrierValue-=value;
        barrierValue = Math.min(barrierValue,rows);
        update = true;
    }

    public void addBarrierValue(int value) {
        barrierValue+=value;
        barrierValue = Math.min(barrierValue,rows);
        update = true;
    }

    protected abstract void complete(float newY, float changed);
    protected abstract void change(float newY, float changed);

}
