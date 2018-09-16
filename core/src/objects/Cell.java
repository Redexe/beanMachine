package objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import screens.GameScreen;


public class Cell extends Group {
    public byte value = 0;
    private float targetX, targetY;
    private float lifeTime = 1, time;
    private Image [] image;

    public Cell(Image... image) {
        for(int i = 0; i < image.length; i++)
            addActor(image[i]);
    }


    @Override
    public void act(float delta) {
        boolean move = targetY != getX() || targetY != getY();
//
//        if(move){
//            time+=delta;
//            float progress = Math.min(1f, time / lifeTime);
//            float newX = Interpolation.circleOut.apply(targetX, getX(), progress);
//            float newY = Interpolation.circleOut.apply(targetY, getY(), progress);
//            super.setX(newX);
//            super.setY(newY);
//        }else{
//            time = 0;
//        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}


