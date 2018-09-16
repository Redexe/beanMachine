package objects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import engine.Row;
import screens.GameScreen;

public class RowGroup extends Table {

    private float rowPosition;
    private float oldPositionY;
    private float time;
    private float lifeSpan = 0.2f;
    private boolean moving,barrier;
    private int basicache;


    public RowGroup() {
//       setDebug(true,true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public boolean isMoving() {
        return moving;
    }

    public void tweenTo(float target) {
        oldPositionY = getY();
        rowPosition = target;
        moving = true;
    }

    public void empty() {
        for(Actor actor : getChildren()){
            if(actor instanceof Cell){
                Cell cell = (Cell) actor;
                for(Actor cellActor : cell.getChildren()){
                    if(cellActor instanceof Image){
                        Image image = (Image) cellActor;
                        image.setDrawable(null);
                    }
                }

            }
        }
    }

    public boolean isBarrier() {
        return barrier;
    }

    public void setBarrier(boolean barrier) {
        this.barrier = barrier;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }
}
