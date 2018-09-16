package engine;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Array;

import objects.Barrier;
import objects.GridActor;
import screens.GameScreen;
@Deprecated
public abstract class BarrierAnimate implements GameEvent {
    private GridActor gridActor;
    private boolean complete;
    private float lifeSpan = 1f;
    private boolean started;
    private float barrierTime;
    private float barrierHeightStart,barrierHeightEnd;

    private BarrierAnimate(int augment,GridActor gridActor) {
        this.gridActor = gridActor;

        barrierHeightStart = gridActor.getBarrierOffset() * GameScreen.TILE_SIZE;
        barrierHeightEnd = (gridActor.getBarrierOffset() + augment) * GameScreen.TILE_SIZE;

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

        if(started){
            barrierTime+=delta;
            float progress = Math.min(1f, barrierTime / lifeSpan);

            float newY = Interpolation.circleIn.apply(barrierHeightStart, barrierHeightEnd, progress);
            gridActor.getBarrier().setHeight(newY);

            for(int i = 0; i < rows; i++){
                newY = newY +  gridActor.getRowGroup()[i].getY() + GameScreen.TILE_SIZE ;
                gridActor.getRowGroup()[i].setY(newY);
            }

            if(progress >=1){
//                gridActor.setBarrierOffset((int) (barrierHeightEnd / GameScreen.TILE_SIZE));
//                complete = true;
//                updateMove();
//                complete();

            }

        }

    }

    private void updateMove() {
        gridActor.updateGrid();

    }

    public abstract void complete();

}
