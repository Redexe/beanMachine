package engine;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Pool;

import objects.GridActor;
import objects.RowGroup;
import screens.GameScreen;

public abstract class LineAnimate implements GameEvent {
    private final Pool<Row> rowPool;
    private final TextureAtlas textureAtlas;
    private float[] startPosition;
    private float[] endPosition;
    private float[] time;
    private boolean[] moving;
    private Integer[][][] lines;
    private GridActor gridActor;
    private boolean complete;
    private float lifeSpan = 0.3f;
    private boolean started;
    private int step;
    private boolean removing;


    public LineAnimate(TextureAtlas textureAtlas,Integer[][][] lines, GridActor gridActor, Pool<Row> rowPool) {
        this.textureAtlas = textureAtlas;
        this.lines = lines;
        this.gridActor = gridActor;
        this.rowPool =rowPool;
        startPosition = new float[gridActor.getRowGroup().length ];
        endPosition = new float[gridActor.getRowGroup().length ];
        time = new float[gridActor.getRowGroup().length ];
        moving = new boolean[gridActor.getRowGroup().length ];
        removing = true;

    }

    @Override
    public boolean isDone() {
        return complete;
    }

    @Override
    public void act(float delta) {
        if (complete) return;
        int rows = gridActor.getGridData().getRow();

/*      when a group of lines are removed. every line above that must be lowered by that amount.
        eg, a double line means evey line above that line needs to be lowered by two lines and...
        come to rest at the level of the line removed.
*/
        final Integer[][] group = lines[0];
        Integer anchor = group[step][0];

        if ( !started ) {
            for (int i = anchor; i < rows; i++) {
                startPosition[i] =  gridActor.getRowGroup()[i].getY();
                endPosition[i] =  gridActor.getRowGroup()[i].getY() - (group[step].length * GameScreen.TILE_SIZE);
                moving[i] = true;
            }
            started = true;
        }


         for (int i = anchor; i < rows; i++) {
             if( moving[i] ) {
                time[i] += delta;

                float progress = Math.min(1f, time[i] / lifeSpan);
                float newY = Interpolation.circleIn.apply(startPosition[i], endPosition[i], progress);
                 gridActor.getRowGroup()[i].setY(newY);

                if(progress >=1){
//                    reset();
                    moving[i] = false;
                    gridActor.getRowGroup()[i].setY(endPosition[i]);
                }
            }
            boolean hasMoving = false;
            for(boolean m : moving){
                 if(m){
                     hasMoving = true;
                    break;
                 }
            }
            if(!hasMoving){
//
                if(step+1 < group.length ){
                    step++;
                    started = false;
                    reset();
                } else {
                    complete = true;
                    gridActor.updateGrid();
                    complete(lines);
                    lines = null;
                }
            }
         }
    }

    public abstract void complete(Integer[][][] lines);

    private void reset() {

        for(int i = 0; i < time.length; i++) {
            time[i] = 0;
            startPosition[i] = 0;
            endPosition[i] = 0;
        }

    }


}
