package engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.Comparator;
import java.util.concurrent.Future;

import engine.genericAI.FutureMove;
import objects.GridActor;
import objects.PuzzleElement;
import objects.Tetromino;
import screens.GridTools;

;

public enum AIState implements State<AI> {
    WAIT() {
        @Override
        public void enter(AI entity) {

        }

        @Override
        public void update(AI entity) {
            if( entity.getNpcGamesStatus().getMode() == GameStatue.falling_cycle &&
                    entity.getGridActor().getTetromino() != null){

                entity.getStateMachine().changeState(AIState.EVALUATE);
            }
        }

        @Override
        public void exit(AI entity) {

        }

        @Override
        public boolean onMessage(AI entity, Telegram telegram) {
            return false;
        }
    },
    FIND_FIT() {
        @Override
        public void enter(AI entity) {

        }

        @Override
        public void update(AI entity) {

        }

        @Override
        public void exit(AI entity) {

        }

        @Override
        public boolean onMessage(AI entity, Telegram telegram) {
            return false;
        }
    },
    TRY_COMBO() {
        @Override
        public void enter(AI entity) {

        }

        @Override
        public void update(AI entity) {

        }

        @Override
        public void exit(AI entity) {

        }

        @Override
        public boolean onMessage(AI entity, Telegram telegram) {
            return false;
        }
    },
    FIND_SINGLE() {
        @Override
        public void enter(AI entity) {

        }

        @Override
        public void update(AI entity) {

        }

        @Override
        public void exit(AI entity) {

        }

        @Override
        public boolean onMessage(AI entity, Telegram telegram) {
            return false;
        }
    },
    FIND_DOUBLE(){
        @Override
        public void enter(AI entity) {

        }

        @Override
        public void update(AI entity) {

        }

        @Override
        public void exit(AI entity) {

        }

        @Override
        public boolean onMessage(AI entity, Telegram telegram) {
            return false;
        }
    },
    FIND_TRIPPLE(){
        @Override
        public void enter(AI entity) {

        }

        @Override
        public void update(AI entity) {

        }

        @Override
        public void exit(AI entity) {

        }

        @Override
        public boolean onMessage(AI entity, Telegram telegram) {
            return false;
        }
    },
    FIND_QUAD(){
        @Override
        public void enter(AI entity) {

        }

        @Override
        public void update(AI entity) {

        }

        @Override
        public void exit(AI entity) {

        }

        @Override
        public boolean onMessage(AI entity, Telegram telegram) {
            return false;
        }
    },
    EVALUATE_GRID_WEIGHTS(){
        Future<double[][]> future;
        double[][] weights = null;
        @Override
        public void enter(AI entity) {

            final GridActor gridActor = entity.getGridActor();
            final GridData gridData = gridActor.getGridData();
            setBoardWeights(gridData);

//            future = grid.computeWeights();


        }

        @Override
        public void update(AI entity) {

            if(entity.getNpcGamesStatus().getMode() == GameStatue.falling_cycle)
                entity.getStateMachine().changeState(AIState.EVALUATE);

        }

        @Override
        public void exit(AI entity) {

        }

        @Override
        public boolean onMessage(AI entity, Telegram telegram) {
            return false;
        }
    },
    EVALUATE_NEXT_GENOME(){
        @Override
        public void enter(AI entity) {

        }

        @Override
        public void update(AI entity) {

            int genomeIndex = entity.getCurrentGenome();


        }

        @Override
        public void exit(AI entity) {

        }

        @Override
        public boolean onMessage(AI entity, Telegram telegram) {
            return false;
        }
    },
    EVALUATE(){
        Array<FutureMove> moves = new Array<FutureMove>();
        float moveDelay = 0.2f;
        float time = 0;
        int mode = 0;



        @Override
        public void enter(AI entity) {
            final GridActor gridActor = entity.getGridActor();
            final GridData gridData = entity.getGridActor().getGridData();
            final Tetromino tetrominoShape = gridActor.getTetromino();
            moves = getAllMoves(gridData,tetrominoShape, gridActor.isOneColor(),gridActor.getBarrierOffset());
            moves.sort(moveSort);
        }

        @Override
        public void update(AI entity) {
            final GridActor gridActor = entity.getGridActor();
            final FutureMove bestMove = futureMoves.first();



            if(bestMove != null){

                entity.setTargetLocation(new Vector3(bestMove.x,bestMove.y,bestMove.rotation));
                gridActor.drawAiDebug(entity.getTargetLocation());
                entity.getStateMachine().changeState(AIState.MOVE_TO_TARGET);

            }

        }

        @Override
        public void exit(AI entity) {

        }

        @Override
        public boolean onMessage(AI entity, Telegram telegram) {
            return false;
        }
    },
    MOVE_TO_TARGET(){
        float mTime = 0, mDelay = 0.01f;
        float rTime = 0, rDelay = 0.1f;
        @Override
        public void enter(AI entity) {

        }

        @Override
        public void update(AI entity) {
            final GridActor grid = entity.getGridActor();
            final Tetromino shape = grid.getTetromino();
            if(shape == null) return;

            final GridLocation gridLocation = entity.getTargetLocation();
            final Vector2 position = gridLocation.position;
            final float rotation = gridLocation.orientation;
            boolean correctRotation = false;
            mTime+= Gdx.graphics.getDeltaTime();
            rTime+= Gdx.graphics.getDeltaTime();

            if(rTime > rDelay){
                correctRotation = rotate(grid,rotation);
                rTime -= rDelay;
            }

            if(mTime > mDelay){
                if(correctRotation)
                    if(change(grid,position,shape.data,rotation))
                        entity.getStateMachine().changeState(AIState.WAIT);
                mTime -= mDelay;
            }



        }
        private boolean rotate(GridActor grid, float targetRotation) {
            int currentRotation = grid.getShapeRotation();

            if(targetRotation > currentRotation){
                grid.rotateLeft();
            } else if(targetRotation < currentRotation){
                grid.rotateRight();
            }

            return currentRotation == targetRotation;
        }
        private boolean change(GridActor grid, Vector2 position, PuzzleElement.PartData shape, float rotation) {
            final byte[][] rotationShape = shape.getData()[grid.getShapeRotation()];
            final int rotationWidth = rotationShape[0].length;

//
//            if (grid.getTetromino().x > position.x) {
//
//                if (!grid.absCollision((int)grid.getTetromino().x - 1, (int)grid.getTetromino().y,rotationShape )) {
//                    grid.getTetromino().x--;
//                }
//
//            } else if (grid.getTetromino().x  < position.x) {
//
//                if (!grid.absCollision((int)grid.getTetromino().x + 1, (int)grid.getTetromino().y, rotationShape )) {
//                    grid.getTetromino().x++;
//                }
//
//            } else if (grid.getTetromino().x == position.x && grid.getShapeRotation() == rotation) {

                /**
                 * \
                 * DROP
                 */

//                for (int row = grid.getRow() - 1; row > 0; row--) {
//                    if (!grid.absCollision(grid.px, grid.py - 1, rotationShape))
//                        grid.py -= 1;
//                    else {
//                        return true;
//                    }

//                }



//            }


            return false;
        }

        @Override
        public void exit(AI entity) {

        }

        @Override
        public boolean onMessage(AI entity, Telegram telegram) {
            return false;
        }
    },
    ;
    private static Tetromino tempTet = new Tetromino();
    private static Array<FutureMove> getAllMoves(GridData gridData, Tetromino tetromino, boolean oneColor, int barrierOffset) {
        futureMovePool.freeAll(futureMoves);
        futureMoves.clear();
//      Try all rotations

        tempTet.setData(tetromino);
        int col = gridData.getCol();
        int row = gridData.getRow() - 2;

        for(int rot = 0; rot < 4 ; rot++) {
            for(int x = 0; x < col; x++ ) {
                for(int y = row; y >= 0; y-- ) {

                    tempTet.setRotation(rot);
//                    tempTet.setX(x);
//                    tempTet.setY(y);

                    boolean fits = gridData.canLand(tempTet);

                    if(fits){

                        double holePenalty = 0;
                        int holes = gridData.getHoles();
                        double rating = gridData.addShape(tempTet,oneColor);
                        setBoardWeights(gridData);
                        int after = gridData.getHoles();
                        int holesNow = holes - after;

                        if(holesNow<0){
                            holePenalty = holesNow*100;
                        }

                        FutureMove futureMove = futureMovePool.obtain();
//                        futureMove.rotation = tempTet.rotation;
//                        futureMove.x = (int) tempTet.x;
//                        futureMove.y = (int) tempTet.y;
                        futureMove.score = rating - holePenalty;
                        futureMoves.add(futureMove);
                        gridData.removeShape(tempTet);
                    }
                }
            }
        }

        return futureMoves;
    }

    private static boolean absCollision(GridData gridData,int tx, int ty, byte[][] dat) {

        int collision = 0;
        for(int y = 0; y < dat.length; y++){
            for(int x = 0; x <dat[y].length; x++){

                if(dat[y][x] > 0) {

                    int b_x = tx + x;
                    int b_y = ty + y;
                    byte value = gridData.getDataValueAt(b_x,b_y);
                    if( value != 0 ){
                        collision++;
                    }


                }


            }
        }

        return collision > 0;
    }

    private static boolean isValid(int b_x, int b_y, GridData gridData) {
        boolean inBounds = !(0 < b_x || b_x >= gridData.getCol() || 0 < b_y || b_y >= gridData.getRow());
        return inBounds && gridData.getDataValueAt(b_x, b_y) == 0;
    }




    public static Pool<FutureMove> futureMovePool = new Pool<FutureMove>() {
        @Override
        protected FutureMove newObject() {
            return new FutureMove();
        }
    };
    // features
    private static int TOTAL_HEIGHT = 0;
    private static int SUM_HEIGHT_DIFFS = 1; //total difference between neighbouring columns
    private static int LINES_CLEARED = 2;
    private static int NUM_HOLES = 3;
    private static int BARRIER_HEIGHT = 4;
    private static int NUM_WEIGHTS = 5;
    private static int POPULATION_SIZE = 1000;
    private static int GENOME = 1;
    private static double[][] allWeights = new double[POPULATION_SIZE][NUM_WEIGHTS];
    private static double[] allFitness = new double[POPULATION_SIZE];
    private static int count = 0;

    static double[] normalizeWeights(double[] ws) {
        double norm = 0;
        for (int i = 0; i < ws.length; i++) {
            norm += ws[i] * ws[i];
        }
        norm = Math.sqrt(norm);
        for (int i = 0; i < ws.length; i++) {
            ws[i] /= norm;
        }
        return ws;
    }

    static double generateInitialWeights() {
        double rand = 0;
        for (int j = 0; j < POPULATION_SIZE; j++) {
            double[] ws = new double[NUM_WEIGHTS];
            for (int i = 0; i < NUM_WEIGHTS; i++) {
                rand = Math.random() * 2 - 1;
                ws[i] = rand;
            }
            ws = normalizeWeights(ws);
            allWeights[j] = ws;
        }
        initialized = true;
        return  rand;
    }

    static boolean initialized;
    private static double getBoardScore (GridData gridData,Tetromino newCurrent) {
//        if (!this.addPieceToAI(x, y, newCurrent)) {
//            return MIN_SCORE;
//        }
        if(!initialized)  generateInitialWeights();

        int cellCount = gridData.getCol() * gridData.getRow();
        double score = 0;
        int[] heights = new int[cellCount];
        int maxHeight = 0;
        int COLS = gridData.getCol();
        int ROWS = gridData.getRow();

        int[] scores = new int[cellCount];

        for (int i = 0; i < COLS; i++) {
            int lastBlock = -1;
            boolean heightFound = false;
            boolean containsBlock = false;
            int numBlocks = 0;
            boolean foundBarrier = false;
            
            for (int j = 0; j < ROWS; j++) {
                if (lastBlock == 0 && gridData.getDataValueAt(i,j) > 0 && !heightFound) {
                    int height = ROWS - j;
                    heights[i] = height;
                    heightFound = true;

                    if (heights[i] > maxHeight) {
                        maxHeight = heights[i];
                    }
                }

                if (containsBlock && gridData.getDataValueAt(i,j) == 0) {
                    scores[NUM_HOLES] ++;
                    if (!foundBarrier) {
                        scores[BARRIER_HEIGHT] += numBlocks;
                        foundBarrier = true;
                    }
                }

                lastBlock = gridData.getDataValueAt(i,j);
                if (lastBlock > 0) {
                    containsBlock = true;
                    numBlocks++;
                }
            }

        }
        int linesCleared = 0;
        for (int j = 0; j < ROWS; j++) {
            boolean lineCleared = true;
            for (int i = 0; i < COLS; i++) {
                if (gridData.getDataValueAt(i,j) == 0) {
                    lineCleared = false;
                }
//                break;
            }
            if (lineCleared) {
                linesCleared++;
            }
        }
        scores[LINES_CLEARED] = linesCleared;

        for (int c = 0; c < COLS; c++) {
            scores[TOTAL_HEIGHT] += heights[c];
            if (c != COLS -1) {
                scores[SUM_HEIGHT_DIFFS] += Math.abs(heights[c] - heights[c+1]);
            }
        }

        for (int i = 0; i < NUM_WEIGHTS; i++) {
            score += scores[i] * allWeights[count][i];
        }

//        this.removePieceFromAI(x, y, newCurrent);
        return score;
    }

    static double HEIGHT_BUFF = 25f;
    static double TOP_BUFF = (byte) (HEIGHT_BUFF + 10);

    private static void setBoardWeights(GridData gridData){
//      Rate empty cells
        int ROWS = gridData.getRow() - 2;
        int COLS = gridData.getCol();
//      weights
//        float[][] weights = new float[ROWS][COLS];
        byte[] top = new byte[COLS];
        int holes = 0;
        int index;
        int maxHeight;

        for (int x = 0; x < COLS; x++) {
            index = ROWS;
            maxHeight = 0;

//          todo increase on curve
            for (int y = 0; y < ROWS; y++,index--) {

//              Make sure the slot is empty
                if(gridData.getDataValueAt(x,y) == 0){
                    gridData.setDataWeightAt(x,y,(index*(HEIGHT_BUFF+index)));
                    int value = GridTools.getAutoValue(gridData,x, y);
                    gridData.setDataAutoAt(x,y,value);
                    if(value <= 4)holes++;
//                  set highest filled space


//                    if(gap(gridData,x,y)){
//                        System.out.println("GAP");
//                        gridData.addDataWeightAt(x,y,HEIGHT_BddUFF);
//                    }



//
//                    if( gridData.getDataWeightAt(x,y) )

                    top[x] = (byte) Math.max(maxHeight,y);
                }

            }
            gridData.setHoles(holes);
//          add highest buff top, height value = ROW of target grid while the length of top = COL
//            gridData.setDataHeightAt(x,top[x],top[x]);
//            double value = gridData.getDataWeightAt(x, top[x] + 1);
//            value += TOP_BUFF;
//            gridData.setDataWeightAt(x,top[x] + 1,value);

        }
//
//        for (int x = 0; x < COLS; x++) {
//            for(int y = 0; y < ROWS; y++){
//
//                if(y >= gridData.getDataHeightAt(x,y))
//                    break;
//
//                if(gridData.getDataValueAt(x,y)==0){
//                    holes++;
//                }
//
//            }
//        }
    }

    private static int autoTile(GridData gridData, int x, int y) {
        int value = 0;
        if(gridData.safeGetDataValueAt(x,y+1) > 0){
            value+=2;
        }

        if(gridData.safeGetDataValueAt(x,y-1) > 0){
            value+=1;
        }

        if(gridData.safeGetDataValueAt(x-1,y) > 0){
            value+=8;
        }

        if(gridData.safeGetDataValueAt(x+1,y) > 0){
            value+=2;
        }

        if(gridData.safeGetDataValueAt(x+1,y+1) > 0){
            value+=32;
        }

        if(gridData.safeGetDataValueAt(x+1,y-1) > 0){
            value+=16;
        }

        if(gridData.safeGetDataValueAt(x-1,y+1) > 0){
            value+=64;
        }

        if(gridData.safeGetDataValueAt(x-1,y-1) > 0){
            value+=128;
        }

        return value;
    }


    private static double rateBoard(final GridData gridData) {
        final int width = gridData.getCol();
//      The maps highest filled block
        final int maxHeight = getHighest(gridData);

        int sumHeight = 0;
        int holes = 0;

        // Count the holes, and sum up the heights
        for (int x=0; x < width; x++) {
//          Highest block filled in this column
            final int colHeight = getHighest(gridData,x);
            sumHeight += colHeight;

        }

        int ROWS = gridData.getRow() - 2;
        int COLS = gridData.getCol();

        for (int x = 0; x < COLS; x++) {
            for (int y = 0; y < ROWS; y++) {
                int av = gridData.getDataAutoAt(x, y);
                for (Integer i : GridTools.holes){
                    if(av == i);
                        holes++;
                }


            }
        }




        double avgHeight = ((double)sumHeight)/width;

        // Add up the counts to make an overall score
        // The weights, 8, 40, etc., are just made up numbers that appear to work
        return (8*maxHeight + 40*avgHeight + 1.25*holes);
    }

    private static int getHighest(GridData gridData, int x) {
        int max = 0;
        for(int i = 0; i < gridData.getRow() - 2; i++){
            if(gridData.getDataValueAt(x,i) > 0)
                max = i+1;
        }
        return max;
    }

    private static int getHighest(GridData gridData) {
        int max = 0;
        for(int col = 0; col < gridData.getCol(); col++){
            max = Math.max(max,getHighest(gridData, col));
        }

        return max;
    }

    private static boolean collision(GridData gridData, int x, int y) {
        if(x < 0 || x >= gridData.getCol() || y < 0 || y >= gridData.getRow() - 2) return true;
        return gridData.getDataValueAt(x,y) > 0;
    }

    private static Array<FutureMove> futureMoves = new Array<FutureMove>(true,50,FutureMove.class);
    private static Comparator<? super FutureMove> moveSort = new Comparator<FutureMove>() {
        @Override
        public int compare(FutureMove a, FutureMove b) {
            return Double.compare(b.score, a.score);
        }
    };

    private static Comparator<? super Float> collisionScore = new Comparator<Float>() {
        @Override
        public int compare(Float a, Float b) {
            return (int) (a - b);
        }
    };

    private static float coverage(Vector3 loc, PuzzleElement.PartData shape, GridActor grid) {
        byte[][] dat = shape.getData()[(int) loc.z];
        float weight = 0;
        for(int y = 0; y < dat.length; y++){
            for(int x = 0; x <dat[y].length; x++){

                if(dat[y][x] > 0) {

                    int b_x = (int) (loc.x + x);
                    int b_y = (int) (loc.y + y);


                    byte value = grid.getValueAt(b_x,b_y-1);
                    if( value != 0 )
                        weight+=1;

                }


            }
        }
        return weight;
    }


    static int[] collisions = new int[4];
    private static GridLocation testCollision(GridActor grid, PuzzleElement.PartData partData) {

//        for(int y = -2; y < grid.getRow()+2; y++){
//            for(int x = -2; x < grid.getCol()+2; x++){
//
////                test rotations
//                for(int i = 0; i < collisions.length; i++){
//                    collisions[i] = 0;
//
//                    for(int sy = 0; sy < partData.getData()[i].length; sy++){
//                        for(int sx = 0; sx <partData.getData()[i][sy].length; sx++){
//
//                            if(partData.getData()[i][sy][sx] > 0) {
//
//                                int b_x = x + sx;
//                                int b_y = y + sy;
//                                byte value = grid.getValueAt(b_x,b_y);
//                                if( value > 0 )
//                                    collisions[i]++;
//
//                            }
//
//
//                        }
//                    }
//                }
//
//                int final_rotation = -1;
//                int coverage = 0;
//                int bestCoverage = 0;
//
//                for(int i = 0; i < collisions.length; i++){
//                    if(collisions[i]==0) continue;
//
//
//                    for(int sy = 0; sy < partData.getData()[i].length; sy++){
//                        for(int sx = 0; sx <partData.getData()[i][sy].length; sx++){
//
//                            if(partData.getData()[i][sy][sx] > 0) {
//
//                                int b_x = x + sx;
//                                int b_y = y + sy - 1;
//                                byte value = grid.getValueAt(b_x,b_y);
//                                if( value > 0 )
//                                    coverage++;
//
//                            }
//
//
//                        }
//                    }
//
//                    if(coverage > bestCoverage){
//                        bestCoverage = coverage;
//                        final_rotation = i;
//                    }
//                }
//
//
//                if(final_rotation != -1){
//
//                    GridLocation gridLocation = new GridLocation();
//                    gridLocation.setPosition(new Vector3(x,y,final_rotation));
//                    grid.drawAiDebug(gridLocation);
//
//                    return gridLocation;
//                }
//
//
//            }
//        }
        return null;
    }

    final static Array<Vector3> fitResults = new Array<Vector3>(true,100,Vector3.class);
    private static Array<Vector3> findFits(GridActor grid, boolean getFist) {
        int col = 0;//grid.getCol();
        int row = 0;//grid.getRow();
        final Tetromino tetromino = grid.getTetromino();
        fitResults.clear();

        //Look over entire grid (0,0) -> (width,height)
        for(int y = 0; y < row; y++ ){
            for(int x = 0; x < col; x++ ){

                //if this value == 0, it is considered open
                byte value = grid.getValueAt(x, y);
                if(value == 0){

//                  Just because the piece is open does not mean or piece will fit.
//                  not only will me check if it fits, we will get the best possible rotation.
                    int rotation = getBestRotation(grid, tetromino, x, y);


//                  -1 means the rotations were useless.
                    if(rotation == -1 ) continue;

                    Vector3 loc = new Vector3(x,y,rotation);
                    if(getFist){
                        fitResults.add(loc);
                        return fitResults;

                    } else {
                        fitResults.add(loc);
                    }

                }
            }

        }

        return fitResults;
    }


    public static int getBestFit(GridActor gridActor,PuzzleElement.PartData partData, Vector2 target){

//        int x = (int) gridActor.getTetromino().x;
//        int y = (int) gridActor.getTetromino().y;

        int targetRotation = 0;
        int highestCollision = 0;


        for(int i = 0; i < 4; i++){
//            int collisions = gridActor.testShape(partData, i, x, y);
//            if(collisions > highestCollision){
//                highestCollision = collisions;
//                targetRotation = i;
//            }
        }

        return targetRotation;
    }


    static final Array<Integer> tempIV = new Array<Integer>(true,50,Integer.class);
    public static int getBestRotation(GridActor grid,Tetromino partData, int x, int y){
        int targetRotation = -1;
        int highestCoverage = -1;
        tempIV.clear();
        int i;
        for(i = 0; i < 4; i++){
//          if there is a collision, this rotation wont work at all, no further test needed.
            if(grid.absCollision(x,y,partData.data.getData()[i]))
                continue;


            tempIV.add(i);
        }

        for( i = 0; i < tempIV.size; i++){
//          this rotation fits, we will now test for the best fit
            Integer rot = tempIV.get(i);
            int coverage = grid.coverage(x, y, partData.data.getData()[rot], false);
//          if coverage = 0, the piece is in the air. that cant be done here.
            if(coverage == 0) continue;

            if(coverage > highestCoverage){
                highestCoverage = coverage;
                targetRotation = rot;
            }
        }


        return targetRotation;
    }

//    public static boolean firstFit(GridActor grid,PuzzleElement.PartData partData){
//
//        int targetRotation = -1;
//        int highestCollision = 0;
//
//        int col = grid.getCol();
//        int row = grid.getRow();
//        int shapeRotation = grid.getShapeRotation();
//        final PuzzleElement.PartData tetrominoShape = grid.getTetrominoShape();
//        final byte[][] data = tetrominoShape.getData()[shapeRotation];
//
////      look for open slots
//        for(int y = 0; y < col; y++ ){
//            for(int x = 0; x < row; x++ ){
//
//                byte value = grid.getValueAt(x, y);
//                if(value == 0){
//                    int collision = getBestFit(grid, tetrominoShape, x, y);
//                    return target.set(x,y);
//                }
//            }
//
//        }
//
//
//        return targetRotation;
//    }

    private static Object[] getFutureCollisionWeights(int sx, int sy, GridActor gridActor, double[][] weights, byte[][] data) {
//        double collisionsScore = 0;
//        boolean collision;
//        do {
//            sy -= 1;
//            collision = gridActor.absCollision(sx,sy,data);
//            if(collision){
//                sy+=1;
//
//            }
//        }while(!collision);
//
//
//        for(int y = 0; y < data.length; y++){
//            for(int x = 0; x <data[y].length; x++){
//
//                if(data[y][x] > 0) {
//
//
//                    int b_x = sx + x;
//                    int b_y = sy + y;
//
//                    byte value = data[y][x];
//                    if(value == 0) continue;
//                    collisionsScore+=weights[b_y][b_x];
//                    for(int i = 0; i < 4; i++){
//                        if(b_y - i < 0) continue;
//                        if(gridActor.getGrid().get(b_y - i).get(b_x) == 0){
//
//                            System.out.println("Taxed "+10*i);
//                            collisionsScore-=10*i;
//
//                        }
//
//                    }
//
//                }
//
//
//            }
//        }

        // TODO: 7/22/2018  : 7/22/2018 Optimize
//        return new Object[]{collisionsScore,sy};
        return null;
    }


    public Vector3 fitIn(int x, int y, int rotation,PuzzleElement.PartData shape, GridActor grid){

        byte[][] shapeRotation = shape.getData()[rotation];

        if(!grid.absCollision(x,y,shapeRotation)){
            return new Vector3(x,y,rotation);
        }

        return null;
    }

    Array<Vector3>  tFits = new Array<Vector3>(true,4,Vector3.class);
    public Vector3[] fitIn(int gx, int gy, PuzzleElement.PartData partData, GridActor grid){
        tFits.clear();

        for(int r = 0; r < 3; r++){
            Vector3 rotation = fitIn(gx, gy,r,partData, grid);
            if(rotation != null){
                tFits.add(rotation);
            }
        }

        return tFits.toArray();
    }



}
