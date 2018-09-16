package engine;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.reflect.ArrayReflection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import objects.GridActor;
import objects.PuzzleElement;
import objects.Tetromino;

public abstract class GridData  implements Serializable{


    private int actualRows;
    private Row[] items;
    private final double[][] weight;
    private final byte[] height;
    private final int[][] auto;
    private final int rows;
    private final int cols;
    private boolean dirty;
    private int holes;

    public GridData(int col, int row) {
        this.cols = col;
        this.rows = row;

        items = new Row[row];
        for(int r = 0; r < row; r++ )
            items[r] = new Row(row,col);
        weight =  new double[row][col];
        height =  new byte[col];
        auto =  new int[row][col];
        actualRows = rows;
    }

    public GridData(){
        this(10,24);
    }

    public abstract void dataChange(int x, int y, byte value);


    public int getRow() {
        return rows;
    }

    public int getCol() {
        return cols;
    }

    public byte safeGetDataValueAt(int x, int y) {
        if( x < 0 || x >= cols || y < 0 || y >= rows ) return Byte.MAX_VALUE;
        return items[y].get(x);
    }


    public Byte getByteValueAt(int x, int y) {
        return items[y].get(x);
    }

    public byte getDataValueAt(int x, int y) {
        return items[y].get(x);
    }

    public byte getDataHeightAt(int x) {
        return height[x];
    }

    public double getDataWeightAt(int x, int y) {

        return weight[y][x];
    }

    public void setDataValueAt(int x, int y, byte b) {
        items[y].getData()[x] = b;
        dataChange(x,y,b);
    }

    public void setDataHeightAt(int x, int y) {
        height[x] = (byte) y;
    }

    public void setDataWeightAt(int x, int y, double b) {
        weight[y][x] = b;
    }

    public void addDataWeightAt(int x, int y, double value) {
        weight[y][x] += value;
    }

    @Override
    public String toString() {
        Json json = new Json();
        return json.toJson(this,GridData.class);
    }
    public String print() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int row = rows; row >= 0; row--){
            for(int col = 0; col < cols; col++){
                stringBuilder.append("[").append(getDataValueAt(col,row)).append("]");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public void fastAddShape(Tetromino tetromino, boolean singleColor) {
        byte[][] shape = tetromino.getShape();
        int x = (int) tetromino.getAbsX();
        int y = (int) tetromino.getAbsY();

        for(int row = 0; row < shape.length; row++){
            for(int col = 0; col < shape[row].length; col++){
                if(shape[row][col] > 0){

                    // TODO: 8/1/2018 Make option

                    byte partValue = ( singleColor ) ? tetromino.getColor() : shape[row][col];
                    setDataValueAt(col + x, row + y, partValue);

                }
            }
        }

    }

    public void removeShape(int x, int y, int rotation, PuzzleElement.PartData data) {
        byte[][] shape = data.getData()[rotation];
        for(int row = 0; row < shape.length; row++){
            for(int col = 0; col < shape[row].length; col++){
                if(shape[row][col] > 0){

                    // TODO: 8/1/2018 Make option
                    setDataValueAt(col + x, row + y, (byte) 0);
                }
            }
        }

    }

    public double addTestShape(int x, int y, int rotation, PuzzleElement.PartData data) {
        byte[][] shape = data.getData()[rotation];
        for(int row = 0; row < shape.length; row++){
            for(int col = 0; col < shape[row].length; col++){
                if(shape[row][col] > 0){

                    // TODO: 8/1/2018 Make option

                    byte partValue = shape[row][col];
                    setDataValueAt(col + x, row + y, (byte) 6);
                }
            }
        }

        return 0;
    }
    public double addMove(GridActor.Move move) {
        addShape(
                move.x - move.partData.getX(move.rotation),
                move.y - move.partData.getY(move.rotation),
                move.rotation,
                move.partData);

        return 0;
    }

    public double addShape(int x, int y, int rotation, PuzzleElement.PartData data) {
        byte[][] shape = data.getData()[rotation];
        for(int row = 0; row < shape.length; row++){
            for(int col = 0; col < shape[row].length; col++){
                if(shape[row][col] > 0){

                    // TODO: 8/1/2018 Make option

                    byte partValue = shape[row][col];
                    setDataValueAt(col + x, row + y, partValue);
                }
            }
        }

        return 0;
    }

    public double addShape(Tetromino tetromino, boolean singleColor) {
        byte[][] shape = tetromino.getShape();
        int x = (int) tetromino.getX();
        int y = (int) tetromino.getY();

        double score = 0;
        double penalty = 0;
        int lines = 0;
        for(int row = 0; row < shape.length; row++){
            for(int col = 0; col < shape[row].length; col++){
                if(shape[row][col] > 0){

                    // TODO: 8/1/2018 Make option

                    byte partValue = ( singleColor ) ? tetromino.getColor() : shape[row][col];
                    setDataValueAt(col + x, row + y, partValue);
//                    cell.setLocation((col + x)*GameScreen.TILE_SIZE, (row + y)* GameScreen.TILE_SIZE);
                    score+=getDataWeightAt(col+x,row+y);
                }
            }
        }
        int count;
        for(int row  = 0; row < rows; row++){
            count = 0;
            for(int col  = 0; col < cols; col++){
               byte v = getDataValueAt(col,row);
               if(v > 0 ) count++;
            }
            if(count == cols)
                lines++;
        }

        y-=1;
        count = 0;
        for(int row = 0; row < shape.length; row++){
            for(int col = 0; col < shape[row].length; col++){
                if(shape[row][col] > 0){
                    if( y < 0) continue;

                    byte val = getDataValueAt(col + x, row + y);
                    if(val == 0){
                        count++;//getDataWeightAt(col + x, row + y);
                    }

                }
            }
        }
        score+= lines * 100;
        penalty = 50*count;
        score -= penalty;
        dirty = true;
        return score;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean fit(Tetromino tetromino) {
        byte[][] shape = tetromino.getShape();
        int x = (int) tetromino.getX();
        int y = (int) tetromino.getY();

        for(int row = 0; row < shape.length; row++){
            for(int col = 0; col < shape[row].length; col++){

                int onGridX = col + x;
                int onGridY = row + y;


                if( onGridX < 0 || onGridX >= cols || onGridY < 0 || onGridY >= rows){

                    return false;
                }

                if(shape[row][col] > 0 && getDataValueAt(onGridX,onGridY) > 0)
                    return false;


            }
        }

        return true;
    }

    public boolean canLand(Tetromino tetromino) {
//        if(fit(tetromino)){
//            tetromino.y-=1;
//            boolean fits = fit(tetromino);
//            tetromino.y+=1;
//            return fits;
//        }
        return false;
    }

    public void removeShape(Tetromino tetromino) {
        byte[][] shape = tetromino.getShape();
        int x = (int) tetromino.getX();
        int y = (int) tetromino.getY();

        for(int row = 0; row < shape.length; row++){
            for(int col = 0; col < shape[row].length; col++){
                if(shape[row][col] > 0)
                    setDataValueAt(col+x,row+y, (byte) 0);
            }
        }
        dirty = true;
    }

    public void setDataAutoAt(int x, int y, int value) {
        auto[y][x] = value;
    }

    public int getDataAutoAt(int x, int y) {
        return auto[y][x];
    }

    public void setHoles(int holes) {
        this.holes = holes;
    }

    public int getHoles() {
        return holes;
    }



    public void fill(Integer row, byte fillValue) {
        Row fillRow = items[row];
        for(int i = 0; i < fillRow.getData().length;i++){
            dataChange(i,row,fillValue);
            fillRow.getData()[i] = fillValue;
        }


    }
    public void random(Integer row) {
        Row fillRow = items[row];
        for(int i = 0; i < fillRow.getData().length;i++) {
            byte v = (byte) MathUtils.random(0, 3);
            fillRow.getData()[i] = v;
            dataChange(i,row,v);
        }
    }


    public Row getRow(int row) {
        return items[row];
    }

    public boolean getRowFull(int row) {
        Row fillRow = items[row];
        // TODO: 8/2/2018 Make list of immune tiles
        return fillRow.isFull(Row.barrierValue);
//        for(int i = 0; i < fillRow.getData().size;i++)
//            if(fillRow.getData().items[i] == 0 || fillRow.getData().items[i] == Byte.MIN_VALUE) return false;

    }

    public Row[] data() {
        return items;
    }

    public Row pop () {
        if (rows == 0) throw new IllegalStateException("Array is empty.");

        Row item = items[rows];
        items[rows] = null;
        return item;
    }

    public void insertRow(int index, Row row) {
        int size = this.rows - 1;

        if (index > size) throw new IndexOutOfBoundsException("index can't be > Rows: " + index + " > " + size);
        Row[] items = this.items;
        if (size == items.length) items = resize(Math.max(8, (int)(size * 1.75f)));
        System.arraycopy(items, index, items, index + 1, size - index);


        items[index] = row;

    }

    private Row[] resize (int newSize) {
        Row[] items = this.items;
        Row[] newItems = (Row[]) ArrayReflection.newInstance(items.getClass().getComponentType(), newSize);
        System.arraycopy(items, 0, newItems, 0, Math.min(rows, newItems.length));
        this.items = newItems;
        return newItems;
    }

    public Row removeRow (int index) {
        int size = this.rows - 1;

        if (index >= size) throw new IndexOutOfBoundsException("index can't be >= Rows: " + index + " >= " + size);
        Row[] items = this.items;
        Row value = items[index];

        System.arraycopy(items, index + 1, items, index, size - index);

        items[size] = null;
        return value;
    }



}
