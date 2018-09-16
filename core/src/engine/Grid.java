package engine;

import com.badlogic.gdx.utils.Array;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Grid extends Array<Row> implements Callable<double[][]>, Serializable{

    public static ExecutorService executorService;

    private double[][] weight;
    private double[] temp;
    private int row;
    private int col;
    private int cap;

    public Grid(){

    }

    public Grid(int col, int row, int bufferRows){
        super(true,row,Row.class);

//        this.col = col;
//        this.row = row;
//        this.weight = new double[row][col];
//        this.temp = new double[row];
//        this.cap = row - bufferRows;
//        for(int i = 0; i < row; i++)
//            super.add(new Row(row,col));
//
//        if(executorService == null) executorService = Executors.newFixedThreadPool(1);


    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public int getCap() {
        return cap;
    }

    private double[][] calculateWeights(){
        calculateRowWeights();
//        calculateColWeights();
//        calculateHoleWeights();
        return weight;
    }

    public Future<double[][]> computeWeights(){
        return executorService.submit(this);
    }

    public int getMaxHeight() {
        int max = 0;
        for(int i = 0; i < col; i++) {
            int height = getColumnHeight(i);
            if (height > max) max = height;
        }
        return max;

    }

    private void calculateRowWeights() {
        for(int r = 0; r < row; r++){
            for(int c = 0; c < col; c++){
                weight[r][c] = 0;

                if(get(r).get(c) == 0)
                    weight[r][c] = (row - r);


            }
        }
    }

    public int getColumnHeight(int x) {
        int height = 0;
        for(int y = 0; y < row; y++){
            if(get(y).get(x) > 0){
                y = height;
            }
        }

        return height;
    }

    public int getRowWidth(int y) {
        return get(y).getSize();
    }

    public double rateBoard() {
        final int width = col;
        final int maxHeight = getMaxHeight();

        int sumHeight = 0;
        int holes = 0;

        // Count the holes, and sum up the heights
        for (int x=0; x<width; x++) {
            final int colHeight = getColumnHeight(x);
            sumHeight += colHeight;

            int y = colHeight - 2;	// addr of first possible hole

            while (y>=0) {
                if  (get(y).get(x) == 0) {
                    holes++;
                }
                y--;
            }
        }

        double avgHeight = ((double)sumHeight)/width;

        // Add up the counts to make an overall score
        // The weights, 8, 40, etc., are just made up numbers that appear to work
        return (8*maxHeight + 40*avgHeight + 1.25*holes);
    }




//    /**
//     Returns the number of filled blocks in
//     the given row.
//     */
//    public int getRowWidth(int y) {
//
//
//        private void calculateColWeights() {
//        for(int r = 0; r < row; r++){
//            int rowWeight = get(r).getRowWeight();
//            for(int c = 0; c < col; c++){
//
//                Byte center = get(r).get(c);
//                if(center == 0){
//                    weight[r][c] += rowWeight;
//                }else{
//                   for(int y = 0; y < 4; y++){
//                       Byte value = get(r).get(c-1);
//                       if(value > 0){
//                           weight[r][c]+= 25*4;
//                       }
//                       value = get(r).get(c+1);
//                       if(value > 0){
//                           weight[r][c]+= 25*4;
//                       }
//                   }
//
//                    weight[r][c] += rowWeight;
//                }
//
//
//
//            }
//        }
//    }

    private void calculateHoleWeights() {
        for(int r = 0; r < row; r++){
            for(int c = 0; c < col; c++){

                Byte value = get(r).get(c);
                if(value == 0){
                    int above = r + 1;

                    if(above >= cap) continue;
                        value = get(above).get(c);

                    if(value > 0){
                        weight[r][c] += c / 100;
                    }


                }

            }
        }

    }


    @Override
    public double[][] call() {
        return calculateWeights();
    }

    public Grid clone(Grid cloneGrid) {
        try {
            cloneGrid = (Grid) clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cloneGrid;
    }
}
