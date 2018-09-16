package engine;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.io.Serializable;
import java.util.Arrays;

public class Row implements Pool.Poolable, Serializable{
    public byte[] data;
    private final int capacity;
    private final float rowThreshold;

    public boolean remove;
    private boolean barrier;
    public static final Byte barrierValue = 20;


    public Row(int row,int col){
        data = new byte[col];
        this.rowThreshold = row / 10f;
        this.capacity = col;
        reset();

    }

    public byte get(int index) {
        if(data.length == 0) return 0;
        if (index < 0 || index >= capacity ) return Byte.MIN_VALUE;
        return data[index];
    }

    public Row fill(byte value) {
        for(int i = 0; i < capacity; i++){
            data[i] = value;
        }
        return this;
    }

    public Row fill(byte fillValue,Integer... except) {

        for(Integer i = 0; i < capacity; i++){
            if(Arrays.asList(except).contains(i))continue;
            data[i] = fillValue;
        }
        return this;
    }

    public Row fill() {
        byte value = (byte) MathUtils.random(1,19);
        for(int i = 0; i < capacity; i++){
            data[i] = value;
        }
        return this;
    }
    public Row makeBarrier() {
        for(int i = 0; i < capacity; i++){
            data[i] = barrierValue;
        }
        barrier = true;
        return this;
    }

    public boolean isFull(byte... values) {
        int count = 0;
        for(int i = 0; i < capacity; i++){
            Byte num = get(i);
            for(byte v : values)
                if( num > 0 && num != v)
                    count++;

        }
        return count == capacity;
    }

    public int getRowWeight() {
        int count = 0;
        for(int i = 0; i < capacity; i++){
            if(get(i) > 0)
                count+=10;
        }
        return count;
    }

    public byte[] getData() {
        return data;
    }

    public int getSize() {
        return data.length;
    }

    @Override
    public void reset() {
        fill((byte) 0);
        barrier = false;
    }

    public boolean isBarrier() {
        return barrier;
    }

    public void random() {
        for(int i = 0; i < capacity; i++){
            if(MathUtils.randomBoolean()) continue;
            data[i] = (byte) MathUtils.random(0,19);
        }
    }


}
