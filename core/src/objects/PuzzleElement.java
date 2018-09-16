package objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.io.FileInputStream;

@Deprecated
public class PuzzleElement {

    public static final byte[][] I_P = new byte [][]{
            {0,0,0,0,0},
            {1,2,3,4,5},
            {0,0,0,0,0},
            {0,0,0,0,0},
            {0,0,0,0,0},
    };

    public static final byte[][][] pill = new byte[][][]{
            {
                    { 0,0,0 },
                    { 0,1,0 },
                    { 0,2,0 },
            },
            {
                    { 0,0,0 },
                    { 2,1,0 },
                    { 0,0,0 },
            },
            {
                    { 0,2,0 },
                    { 0,1,0 },
                    { 0,0,0 },
            },
            {
                    { 0,0,0 },
                    { 0,1,2 },
                    { 0,0,0 },
            },
    };

    public static final byte[][][] star = new byte[][][]{
            {
                    { 0,4,0 },
                    { 3,1,5 },
                    { 0,2,0 },
            },
            {
                    { 0,3,0 },
                    { 2,1,4 },
                    { 0,5,0 },
            },
            {
                    { 0,2,0 },
                    { 5,1,3 },
                    { 0,4,0 },
            },
            {
                    { 0,5,0 },
                    { 4,1,2 },
                    { 0,3,0 },
            },
    };
    public static final byte[][][] AI_O = new byte[][][]{
            {

                    { 1,2, },
                    { 3,4, },



            },
            {

                    { 3,1,},
                    { 4,2,},

            },
            {

                    { 3,4},
                    { 2,1},

            },
            {
                    { 2,3 },
                    { 1,4 },
            },
            {//     4
                    { 1,1,2,2 },//x,y,width,height//
                    { 1,1,2,2 },//x,y,width,height//
                    { 1,1,2,2 },//x,y,width,height//
                    { 1,1,2,2 },//x,y,width,height
            }

    };
    public static final byte[][][] O = new byte[][][]{
            {
                    { 0,0,0,0 },
                    { 0,1,2,0 },
                    { 0,3,4,0 },
                    { 0,0,0,0 },


            },
            {
                    { 0,0,0,0 },
                    { 0,3,1,0 },
                    { 0,4,2,0 },
                    { 0,0,0,0 },
            },
            {
                    { 0,0,0,0 },
                    { 0,3,4,0 },
                    { 0,2,1,0 },
                    { 0,0,0,0 },
            },
            {
                    { 0,0,0,0 },
                    { 0,2,3,0 },
                    { 0,1,4,0 },
                    { 0,0,0,0 },
            },
            {//     4
                    { 1,1,2,2 },//x,y,width,height//
                    { 1,1,2,2 },//x,y,width,height//
                    { 1,1,2,2 },//x,y,width,height//
                    { 1,1,2,2 },//x,y,width,height
            }

    };

    public static final byte[][][] I = new byte[][][]{
            {
                    { 0,0,0,0 },
                    { 1,2,3,4 },
                    { 0,0,0,0 },
                    { 0,0,0,0 },
            },
            {

                    { 0,0,1,0 },
                    { 0,0,2,0 },
                    { 0,0,3,0 },
                    { 0,0,4,0 },

            },
            {
                    { 0,0,0,0 },
                    { 0,0,0,0 },
                    { 4,3,2,1 },
                    { 0,0,0,0 },
            },
            {

                    { 0,4,0,0 },
                    { 0,3,0,0 },
                    { 0,2,0,0 },
                    { 0,1,0,0 },

            },
            {//     4
                    { 0,1,4,1 },//x,y,width,height//
                    { 2,0,1,4 },//x,y,width,height//
                    { 0,2,4,1 },//x,y,width,height//
                    { 1,0,1,4 },//x,y,width,height
            }

    };

    public static final byte[][][] T = new byte[][][]{
            {
                    {0,0,0,0},
                    {1,2,3,0},
                    {0,4,0,0},
                    {0,0,0,0},

            },
            {

                    {0,1,0,0},
                    {4,2,0,0},
                    {0,3,0,0},
                    {0,0,0,0},

            },
            {
                    {0,4,0,0},
                    {3,2,1,0},
                    {0,0,0,0},
                    {0,0,0,0},
            },
            {
                    {0,3,0,0},
                    {0,2,4,0},
                    {0,1,0,0},
                    {0,0,0,0},
            },
            {//    // x,y,w,h //
                    { 0,1,3,2 },//x,y,width,height//
                    { 0,0,2,3 },//x,y,width,height//
                    { 0,0,3,2 },//x,y,width,height//
                    { 1,0,2,3 },//x,y,width,height
            }

    };

    public static final byte[][][] J = new byte[][][]{
            {

                    {0,0,0,0},
                    {1,2,3,0},
                    {4,0,0,0},
                    {0,0,0,0},

            },
            {
                    { 0,3,0,0},
                    { 0,2,0,0},
                    { 0,1,4,0},
                    { 0,0,0,0},

            },
            {

                    { 0,0,4,0 },
                    { 3,2,1,0 },
                    { 0,0,0,0 },
                    { 0,0,0,0 },
            },
            {
                    { 4,1,0,0},
                    { 0,2,0,0},
                    { 0,3,0,0},
                    { 0,0,0,0},
            },
            {//    // x,y,w,h //
                    { 0,1,3,2 },//x,y,width,height//
                    { 1,0,2,3 },//x,y,width,height//
                    { 0,0,3,2 },//x,y,width,height//
                    { 0,0,2,3 },//x,y,width,height
            }


    };

    public static final byte[][][] L = new byte[][][]{
            {
                    { 0,0,0,0 },
                    { 1,2,3,0 },
                    { 0,0,4,0 },
                    { 0,0,0,0 },

            },
            {
                    { 0,3,4,0 },
                    { 0,2,0,0 },
                    { 0,1,0,0 },
                    { 0,0,0,0 },
            },
            {

                    { 4,0,0,0 },
                    { 3,2,1,0 },
                    { 0,0,0,0 },
                    { 0,0,0,0 },
            },
            {
                    { 0,1,0,0 },
                    { 0,2,0,0 },
                    { 4,3,0,0 },
                    { 0,0,0,0 },

            },
            {//    // x,y,w,h //
                    { 0,1,3,2 },//x,y,width,height//
                    { 1,0,2,3 },//x,y,width,height//
                    { 0,0,3,2 },//x,y,width,height//
                    { 0,0,2,3 },//x,y,width,height
            }

    };

    public static final byte[][][] Z = new byte[][][]{
            {
                    { 0,1,2,0 },
                    { 3,4,0,0 },
                    { 0,0,0,0 },
                    { 0,0,0,0 },
            },
            {

                    { 2,0,0,0 },
                    { 1,4,0,0 },
                    { 0,3,0,0 },
                    { 0,0,0,0 },
            },
            {

                    { 0,0,0,0 },
                    { 0,4,3,0 },
                    { 2,1,0,0 },
                    { 0,0,0,0 },

            },
            {
                    { 0,3,0,0 },
                    { 0,4,1,0 },
                    { 0,0,2,0 },
                    { 0,0,0,0 },
            },
            {//    // x,y,w,h //
                    { 0,0,3,2 },//x,y,width,height//
                    { 0,0,2,3 },//x,y,width,height//
                    { 0,1,3,2 },//x,y,width,height//
                    { 1,0,2,3 },//x,y,width,height
            }

    };
    public static final byte[][][] S = new byte[][][]{
            {
                    { 0,0,0,0 },
                    { 1,2,0,0 },
                    { 0,3,4,0 },
                    { 0,0,0,0 },

            },
            {
                    { 0,0,1,0 },
                    { 0,3,2,0 },
                    { 0,4,0,0 },
                    { 0,0,0,0 },
            },
            {
                    { 0,0,0,0 },
                    { 4,3,0,0 },
                    { 0,2,1,0 },
                    { 0,0,0,0 },

            },
            {
                    { 0,0,4,0 },
                    { 0,2,3,0 },
                    { 0,1,0,0 },
                    { 0,0,0,0 },
            },
            {//    // x,y,w,h //
                    { 0,1,3,2 },//x,y,width,height//
                    { 1,0,2,3 },//x,y,width,height//
                    { 0,1,3,2 },//x,y,width,height//
                    { 1,0,2,3 },//x,y,width,height
            }


    };


    public interface DrawableArray {
        void draw(Batch batch, float parentAlpha, int rotation, TextureAtlas atlas, float tileSize, float offsetX,float offsetY);
        byte[][][] getData();
        int getX(int rotation);
        int getY(int rotation);
        int getWidth(int rotation);
        int getHeight(int rotation);
        byte[][] rotate(byte[][] data);
        int[] getInternalPosition(byte[][][] shape, int rotation, int... values);
    }


    public enum PartData implements DrawableArray{
        AIO_Shape() {
            final byte[][][]  DATA = AI_O;

            @Override
            public void draw(Batch batch, float parentAlpha, int rotation, TextureAtlas atlas, float tileSize, float offsetX,float offsetY) {
                for(int y = 0; y < DATA[rotation].length;y++){
                    for(int x = 0; x < DATA[rotation][y].length;x++){
                        byte d = DATA[rotation][y][x];
                        if(d > 0){
                            Sprite element = atlas.createSprite(String.valueOf(d));
                            element.setSize(tileSize,tileSize);
                            element.setPosition(offsetX + ( x * tileSize ) + (tileSize/2),offsetY + ( y * tileSize ) + (tileSize/2));

                            element.draw(batch,parentAlpha);
                        }
                    }
                }

            }
            //            { 1,1,3,2 },//x,y,width,height//
            @Override
            public byte[][][] getData() {
                return DATA;
            }

            @Override
            public int getX(int rotation) {
                return DATA[POSITION][rotation][X_POSITION];
            }

            @Override
            public int getY(int rotation) {
                return DATA[POSITION][rotation][Y_POSITION];
            }

            @Override
            public int getWidth(int rotation) {
                return DATA[POSITION][rotation][WIDTH_POSITION];
            }

            @Override
            public int getHeight(int rotation) {
                return DATA[POSITION][rotation][HEIGHT_POSITION];
            }

        },

        O_Shape() {
            final byte[][][]  DATA = O;

            @Override
            public void draw(Batch batch, float parentAlpha, int rotation, TextureAtlas atlas, float tileSize, float offsetX,float offsetY) {
                for(int y = 0; y < DATA[rotation].length;y++){
                    for(int x = 0; x < DATA[rotation][y].length;x++){
                        byte d = DATA[rotation][y][x];
                        if(d > 0){
                            Sprite element = atlas.createSprite(String.valueOf(d));
                            element.setSize(tileSize,tileSize);
                            element.setPosition(offsetX + ( x * tileSize ) + (tileSize/2),offsetY + ( y * tileSize ) + (tileSize/2));

                            element.draw(batch,parentAlpha);
                        }
                    }
                }

            }
            //            { 1,1,3,2 },//x,y,width,height//
            @Override
            public byte[][][] getData() {
                return DATA;
            }

            @Override
            public int getX(int rotation) {
                return DATA[POSITION][rotation][X_POSITION];
            }

            @Override
            public int getY(int rotation) {
                return DATA[POSITION][rotation][Y_POSITION];
            }

            @Override
            public int getWidth(int rotation) {
                return DATA[POSITION][rotation][WIDTH_POSITION];
            }

            @Override
            public int getHeight(int rotation) {
                return DATA[POSITION][rotation][HEIGHT_POSITION];
            }

        },
        I_Shape() {

            final byte[][][]  DATA = I;

            @Override
            public void draw(Batch batch, float parentAlpha, int rotation, TextureAtlas atlas, float tileSize, float offsetX,float offsetY) {
                for(int y = 0; y < DATA[rotation].length;y++){
                    for(int x = 0; x < DATA[rotation][y].length;x++){
                        byte d = DATA[rotation][y][x];
                        if(d > 0){
                            Sprite element = atlas.createSprite(String.valueOf(d));
                            element.setSize(tileSize,tileSize);
                            element.setPosition(offsetX + ( x * tileSize ) + (tileSize/2),offsetY + ( y * tileSize ) + (tileSize/2));

                            element.draw(batch,parentAlpha);
                        }
                    }
                }

            }
//            { 1,1,3,2 },//x,y,width,height//
            @Override
            public byte[][][] getData() {
                return DATA;
            }

            @Override
            public int getX(int rotation) {
                return DATA[POSITION][rotation][X_POSITION];
            }

            @Override
            public int getY(int rotation) {
                return DATA[POSITION][rotation][Y_POSITION];
            }

            @Override
            public int getWidth(int rotation) {
                return DATA[POSITION][rotation][WIDTH_POSITION];
            }

            @Override
            public int getHeight(int rotation) {
                return DATA[POSITION][rotation][HEIGHT_POSITION];
            }

        },
        J_Shape() {
            final byte[][][]  DATA = J;

            @Override
            public void draw(Batch batch, float parentAlpha, int rotation, TextureAtlas atlas, float tileSize, float offsetX,float offsetY) {
                for(int y = 0; y < DATA[rotation].length;y++){
                    for(int x = 0; x < DATA[rotation][y].length;x++){
                        byte d = DATA[rotation][y][x];
                        if(d > 0){
                            Sprite element = atlas.createSprite(String.valueOf(d));
                            element.setSize(tileSize,tileSize);
                            element.setPosition(offsetX + ( x * tileSize ) + (tileSize/2),offsetY + ( y * tileSize ) + (tileSize/2));

                            element.draw(batch,parentAlpha);
                        }
                    }
                }

            }
            //            { 1,1,3,2 },//x,y,width,height//
            @Override
            public byte[][][] getData() {
                return DATA;
            }

            @Override
            public int getX(int rotation) {
                return DATA[POSITION][rotation][X_POSITION];
            }

            @Override
            public int getY(int rotation) {
                return DATA[POSITION][rotation][Y_POSITION];
            }

            @Override
            public int getWidth(int rotation) {
                return DATA[POSITION][rotation][WIDTH_POSITION];
            }

            @Override
            public int getHeight(int rotation) {
                return DATA[POSITION][rotation][HEIGHT_POSITION];
            }

        },
        L_Shape() {
            final byte[][][]  DATA = L;

            @Override
            public void draw(Batch batch, float parentAlpha, int rotation, TextureAtlas atlas, float tileSize, float offsetX,float offsetY) {
                for(int y = 0; y < DATA[rotation].length;y++){
                    for(int x = 0; x < DATA[rotation][y].length;x++){
                        byte d = DATA[rotation][y][x];
                        if(d > 0){
                            Sprite element = atlas.createSprite(String.valueOf(d));
                            element.setSize(tileSize,tileSize);
                            element.setPosition(offsetX + ( x * tileSize ) + (tileSize/2),offsetY + ( y * tileSize ) + (tileSize/2));

                            element.draw(batch,parentAlpha);
                        }
                    }
                }

            }
            //            { 1,1,3,2 },//x,y,width,height//
            @Override
            public byte[][][] getData() {
                return DATA;
            }

            @Override
            public int getX(int rotation) {
                return DATA[POSITION][rotation][X_POSITION];
            }

            @Override
            public int getY(int rotation) {
                return DATA[POSITION][rotation][Y_POSITION];
            }

            @Override
            public int getWidth(int rotation) {
                return DATA[POSITION][rotation][WIDTH_POSITION];
            }

            @Override
            public int getHeight(int rotation) {
                return DATA[POSITION][rotation][HEIGHT_POSITION];
            }

        },
        T_Shape() {
            final byte[][][]  DATA = T;

            @Override
            public void draw(Batch batch, float parentAlpha, int rotation, TextureAtlas atlas, float tileSize, float offsetX,float offsetY) {
                for(int y = 0; y < DATA[rotation].length;y++){
                    for(int x = 0; x < DATA[rotation][y].length;x++){
                        byte d = DATA[rotation][y][x];
                        if(d > 0){
                            Sprite element = atlas.createSprite(String.valueOf(d));
                            element.setSize(tileSize,tileSize);
                            element.setPosition(offsetX + ( x * tileSize ) + (tileSize/2),offsetY + ( y * tileSize ) + (tileSize/2));

                            element.draw(batch,parentAlpha);
                        }
                    }
                }

            }
            //            { 1,1,3,2 },//x,y,width,height//
            @Override
            public byte[][][] getData() {
                return DATA;
            }

            @Override
            public int getX(int rotation) {
                return DATA[POSITION][rotation][X_POSITION];
            }

            @Override
            public int getY(int rotation) {
                return DATA[POSITION][rotation][Y_POSITION];
            }

            @Override
            public int getWidth(int rotation) {
                return DATA[POSITION][rotation][WIDTH_POSITION];
            }

            @Override
            public int getHeight(int rotation) {
                return DATA[POSITION][rotation][HEIGHT_POSITION];
            }

        },
        S_Shape() {
            final byte[][][]  DATA = S;

            @Override
            public void draw(Batch batch, float parentAlpha, int rotation, TextureAtlas atlas, float tileSize, float offsetX,float offsetY) {
                for(int y = 0; y < DATA[rotation].length;y++){
                    for(int x = 0; x < DATA[rotation][y].length;x++){
                        byte d = DATA[rotation][y][x];
                        if(d > 0){
                            Sprite element = atlas.createSprite(String.valueOf(d));
                            element.setSize(tileSize,tileSize);
                            element.setPosition(offsetX + ( x * tileSize ) + (tileSize/2),offsetY + ( y * tileSize ) + (tileSize/2));

                            element.draw(batch,parentAlpha);
                        }
                    }
                }

            }
            //            { 1,1,3,2 },//x,y,width,height//
            @Override
            public byte[][][] getData() {
                return DATA;
            }

            @Override
            public int getX(int rotation) {
                return DATA[POSITION][rotation][X_POSITION];
            }

            @Override
            public int getY(int rotation) {
                return DATA[POSITION][rotation][Y_POSITION];
            }

            @Override
            public int getWidth(int rotation) {
                return DATA[POSITION][rotation][WIDTH_POSITION];
            }

            @Override
            public int getHeight(int rotation) {
                return DATA[POSITION][rotation][HEIGHT_POSITION];
            }

        },
        Z_Shape() {
            final byte[][][]  DATA = Z;

            @Override
            public void draw(Batch batch, float parentAlpha, int rotation, TextureAtlas atlas, float tileSize, float offsetX,float offsetY) {
                for(int y = 0; y < DATA[rotation].length;y++){
                    for(int x = 0; x < DATA[rotation][y].length;x++){
                        byte d = DATA[rotation][y][x];
                        if(d > 0){
                            Sprite element = atlas.createSprite(String.valueOf(d));
                            element.setSize(tileSize,tileSize);
                            element.setPosition(offsetX + ( x * tileSize ) + (tileSize/2),offsetY + ( y * tileSize ) + (tileSize/2));

                            element.draw(batch,parentAlpha);
                        }
                    }
                }

            }
            //            { 1,1,3,2 },//x,y,width,height//
            @Override
            public byte[][][] getData() {
                return DATA;
            }

            @Override
            public int getX(int rotation) {
                return DATA[POSITION][rotation][X_POSITION];
            }

            @Override
            public int getY(int rotation) {
                return DATA[POSITION][rotation][Y_POSITION];
            }

            @Override
            public int getWidth(int rotation) {
                return DATA[POSITION][rotation][WIDTH_POSITION];
            }

            @Override
            public int getHeight(int rotation) {
                return DATA[POSITION][rotation][HEIGHT_POSITION];
            }


        };

        private final static int POSITION = 4;
        private final static int X_POSITION = 0;
        private final static int Y_POSITION = 1;
        private final static int WIDTH_POSITION = 2;
        private final static int HEIGHT_POSITION = 3;

        private Texture texture;

        PartData() {

        }

        @Override
        public byte[][] rotate(byte[][] arr) {
            byte[][] newArray = new byte[arr[0].length][arr.length];
            for(int i=0; i<arr[0].length; i++){
                for(int j=arr.length-1; j>=0; j--){
                    newArray[i][j] = arr[j][i];
                }
            }
            return newArray;
        }

        public int[] getInternalPosition(byte[][][] shape, int rotation, int... values){
            int ix = -1;
            int iy = -1;
            int height = -1;
            int width = -1;
            for(int y = 0; y < shape[rotation].length;y++){
                for(int x = 0; x < shape[rotation][y].length;x++){
                    byte d = shape[rotation][y][x];
                    if(d > 0){
                        if(ix == -1)
                            ix = x;
                        if(iy == -1)
                            iy = y;

                        width = x;
                        height = y;
                    }
                }
            }
            values[0] = ix;
            values[1] = iy;
            values[2] = width;
            values[3] = height;

            return values;
        }

        public void setTexture(Texture texture) {
            this.texture = texture;
        }

        public Texture getTexture() {
            return texture;
        }
    }

}
