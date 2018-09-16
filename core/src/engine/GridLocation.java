package engine;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GridLocation implements Location<Vector2> {

    final Vector2 position = new Vector2();
    int orientation;

    public GridLocation () {

        this.orientation = 0;
    }

    @Override
    public Vector2 getPosition () {
        return position;
    }

    @Override
    public float getOrientation () {
        return orientation;
    }

    @Override
    public void setOrientation (float orientation) {
        this.orientation = (int) orientation;
    }

    @Override
    public Location<Vector2> newLocation () {
        return new GridLocation();
    }

    @Override
    public float vectorToAngle (Vector2 vector) {
        return (float)Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector (Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float)Math.cos(angle);
        return outVector;
    }

    public void setPosition(Vector2 position) {
        this.position.set(position);
    }

    public GridLocation setPosition(Vector3 position) {
        this.position.set(position.x,position.y);
        setOrientation(position.z);
        return this;
    }




}
