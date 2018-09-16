package objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;

public class Tetromino {



    public PuzzleElement.PartData data;
    private float x, y;
    private int rotation;
    private byte color;
    private int index;

    public Tetromino( Tetromino tetromino){
        this(tetromino.data);
    }

    public Tetromino(){
        color = (byte) MathUtils.random(1,18);
    }

    public Tetromino(PuzzleElement.PartData data) {
        this.data = data;
        color = (byte) MathUtils.random(1,18);
    }

    public byte[][] getShape(){
        return data.getData()[rotation];
    }

    public Tetromino setData(Tetromino tetromin) {
        this.data = tetromin.data;
        this.x = tetromin.x;
        this.y = tetromin.y;
        this.rotation = tetromin.rotation;
        return this;
    }

    public int getRotation() {
        return rotation;
    }

    public PuzzleElement.PartData getData() {
        return data;
    }

    public Tetromino setData(PuzzleElement.PartData data) {
        this.data = data;
        return this;
    }

    public byte getColor() {
        return color;
    }

    public void setAbsX(float x) {
        this.x = x;
    }

    public void setAbsY(float y) {
        this.y = y;
    }


    public float getY() {
        return y - data.getHeight(rotation);
    }

    public float getX() {
        return x - data.getWidth(rotation);
    }

    public float getAbsX() {
        return x;
    }

    public float getAbsY() {
        return y;
    }

    public int getWidth(){
        return data.getWidth(rotation);
    }

    public int getHeight(){
        return data.getHeight(rotation);
    }



    public Tetromino setRotation(int rotation) {
        this.rotation = rotation;
        return this;
    }

    @Override
    public String toString() {
        Json json = new Json();
        return json.toJson(this,this.getClass());
    }

    public void moveBy(int x, float y) {
        this.x -= x;
        this.y -= y;
    }

    public Tetromino setIndex(int index) {
        this.index = index;
        return this;
    }

    public int getIndex() {
        return index;
    }
}
