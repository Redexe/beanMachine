package engine.genericAI;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Pool;

public class FutureMove implements Pool.Poolable {
    public int x,y,rotation;
    public double score;

    @Override
    public String toString() {
        Json json = new Json();
        return json.toJson(this,this.getClass());
    }

    @Override
    public void reset() {
        x = 0;
        y = 0;
        rotation = 0;
        score = -1;
    }
}
