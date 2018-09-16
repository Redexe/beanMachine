package objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;

import static objects.GridActor.shapeRenderer;

public class BarSplash extends Actor{
    private final ShapeRenderer shapeRenderer;
    private float time,lifeTime = 0.2f;
    private boolean start;
    private int endWidth;
    private float startWidth;
    private float pad;

    public BarSplash(ShapeRenderer shapeRenderer){
        this.shapeRenderer = shapeRenderer;
        this.pad = 3f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if(!start){
            startWidth = getWidth();
            endWidth = 0;
            start = false;
        }
        time+=delta;
        float progress = Math.min(1f, time / lifeTime);
        float newWidth = Interpolation.circleIn.apply(startWidth,endWidth, progress);
        setWidth(newWidth);
        if(progress >= 1){
            remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);



        batch.end();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setColor(Color.PURPLE);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(getX(),getY(),getWidth(),getHeight());
        shapeRenderer.end();

        batch.begin();

//        AlphaAction action = action(AlphaAction.class);
//        action.setAlpha(0);
//        action.setDuration(duration);
//        action.setInterpolation(interpolation);

    }
}
