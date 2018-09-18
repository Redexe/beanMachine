package engine.tetrisAI;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import engine.GridData;
import engine.Row;
import engine.genericAI.Genome;
import managers.PartFactory;
import objects.GridActor;
import objects.PuzzleElement;
import objects.Tetromino;

public class Tuner {

    final ExecutorService executorService = Executors.newFixedThreadPool(1);

    private Array<Genome> genomes;
    private Array<Genome> children;
    private Comparator<? super Genome> fitness = new Comparator<Genome>() {
        @Override
        public int compare(Genome a, Genome b) {
            return (int) (a.getFitness() - b.getFitness());
        }
    };

    public Tuner() {

    }

    private double randomInteger(int min, int max){
        return Math.floor(Math.random() * (max - min) + min);
    }

    final static Array<Tetromino> workingPieces = new Array<Tetromino>(true,2,Tetromino.class);
    public static void computeFitnesses(Array<Genome> candidates, int numberOfGames, int maxNumberOfMoves, PartFactory partFactory){
        for(int  i = 0; i < candidates.size; i++){

            final Genome candidate = candidates.get(i);

//            int  ai = new AI(candidate.heightWeight, candidate.linesWeight, candidate.holesWeight, candidate.bumpinessWeight);
            int  totalScore = 0;
            for(int j = 0; j < numberOfGames; j++){
                workingPieces.clear();
                GridData grid = new GridData(){public void dataChange(int x, int y, byte value){}};

                int score = 0;
                int numberOfMoves = 0;

                while((numberOfMoves++) < maxNumberOfMoves && !grid.exceeded()){

                    GridActor.Move move = getBestMove(partFactory.newPiece(),grid,candidate);
                    if(move != null)
                        grid.addMove(move);
                    score += grid.clearLines();

                }
                totalScore += score;
            }
            candidate.setFitness(totalScore);
        }

    }

    private static Array<GridActor.Move> validTiles = new Array<GridActor.Move>(true,100, GridActor.Move.class );
    private static GridActor.Move getBestMove(Tetromino tetromino, GridData gridData, Genome currentGenome) {

        int maxHeight = 0;
        float completeLines =0;
        float holes =0;
        float aggregatedHeight =0;
        float bumpiness = 0;

        final float
                heightWeight =  currentGenome.getAggregatedHeight(),
                linesWeight = currentGenome.getCompleteLines(),
                holesWeight = currentGenome.getHoles(),
                bumpinessWeight = currentGenome.getBumpiness();

        for (int rotation = 0; rotation < 4; rotation++) {

            tetromino.setRotation(rotation);


            int x = (int) tetromino.getAbsX();

            int xOffset = tetromino.data.getX(rotation);
            int yOffset = tetromino.data.getY(rotation);

            while (x >= 0) {

                int y = (int) tetromino.getAbsY();



                do {

                    boolean open = isOpen(x, y, gridData.data(), false) == 1;
                    boolean collision = partOffsetCollision(tetromino,gridData.data(), x, y, rotation);
                    boolean canLand = partOffsetCollision(tetromino,gridData.data(), x, y - 1, rotation);

//                    boolean canLand = collision(x,y-1,rotation,partData);


                    if (open && !collision && canLand) {



                        /**
                         *  The Algorithm
                         *   return (8 * maxHeight + 40 * avgHeight + 1.25 * holes);
                         **/
                        gridData.addShape(x - xOffset , y - yOffset , rotation, tetromino.data);

                        maxHeight = gridData.getHeights();
                        completeLines = gridData.clearLines();
                        holes = gridData.holes();
                        aggregatedHeight = gridData.aggregateHeight();
                        bumpiness = gridData.bumpiness();

                        float directRate = (heightWeight * maxHeight + aggregatedHeight * (aggregatedHeight / gridData.getCol()) + holesWeight * holes + bumpinessWeight * bumpiness + linesWeight * completeLines);

                        gridData.removeShape(x - xOffset , y - yOffset , rotation, tetromino.data);

                        GridActor.Move move = new GridActor.Move();
                        move.set(x, y, rotation, tetromino.data, directRate, null);

                        validTiles.add(move);
//                      this will only get the first move at the highest level
                        break;
                    }

                    y--;

                } while (y > -4);


                x--;

            }

            while (x <= gridData.getRow()) {

                int y = (int) tetromino.getAbsY();

                do {

                    boolean open = isOpen(x, y, gridData.data(), false) == 1;
                    boolean collision = partOffsetCollision(tetromino,gridData.data(), x, y, rotation);
                    boolean canLand = partOffsetCollision(tetromino,gridData.data(), x, y - 1, rotation);

                    if (open && !collision && canLand) {

                        /**
                         *  The Algorithm
                         **/

                        gridData.addTestShape(x - xOffset , y - yOffset , rotation, tetromino.data);

                        maxHeight = gridData.getHeights();
                        completeLines =  gridData.clearLines();
                        holes =  gridData.holes();
                        aggregatedHeight =  gridData.aggregateHeight();
                        bumpiness =  gridData.bumpiness();

                        float directRate = (heightWeight * maxHeight + aggregatedHeight * (aggregatedHeight / gridData.getCol()) + holesWeight * holes + bumpinessWeight * bumpiness + linesWeight * completeLines);
                        GridActor.Move move = new GridActor.Move();
                        move.set(x, y, rotation, tetromino.data, directRate, null);
                        validTiles.add(move);
                        break;
                    }

                    y--;

                } while (y > -4);


                x++;

            }
        }

        System.out.println("Genome count "+validTiles.size);
        GridActor.Move bestMove = null;
        Double bestScore = null;
        for(GridActor.Move move : validTiles){
            if(bestScore == null || move.score < bestScore){
                bestMove = move;
                bestScore = move.score;
            }
        }
        validTiles.clear();

        return bestMove;
    }

    private static short isOpen(int x, int y, Row[] rows, boolean requireOpen) {
        if (y < 0 || y >= rows.length || x < 0 || x >= rows[y].getData().length) return -1;
        if (requireOpen)
            if (rows[y].getData()[x] == 0) return 0;
        return 1;
    }

    private static boolean partOffsetCollision(Tetromino tetromino, Row[] rows, int ax, int ay, int rotation) {

        final byte[][] dat = tetromino.getShape();

        for (int y = 0; y < dat.length; y++) {
            for (int x = 0; x < dat[y].length; x++) {

                if (dat[y][x] > 0) {

                    int b_x = ax + x - tetromino.data.getX(rotation);
                    int b_y = ay + y - tetromino.data.getY(rotation);

                    if ( b_y < 0 || b_y >= rows.length || b_x < 0 || b_x >= rows[b_y].getData().length ) return true;
                    byte value = rows[b_y].getData()[ b_x];

                    if (value > 0) {
                        return true;
                    }

                }

            }
        }

        return false;
    }

    private static boolean moveDown(Tetromino tetromino, GridData grid) {

        final float tx = tetromino.getAbsX();
        final float ty = tetromino.getAbsY() - 1;
        final int rotation = tetromino.getRotation();
        final PuzzleElement.PartData partData = tetromino.getData();

        boolean collision = grid.collision(tx, ty, rotation, partData,true);
        if (!collision)
            tetromino.setAbsY(ty);

        return collision;
    }

    private Genome[]tournamentSelectPair(Array<Genome> candidates, int ways){

        /*
            Note that the following assumes that the candidates array is
            sorted according to the fitness of each individual candidates.
            Hence it suffices to pick the least 2 indexes out of the random
            ones picked.
        */
        Integer fittestCandidateIndex1 = null;
        Integer fittestCanddiateIndex2 = null;

        for(int i = 0; i < ways; i++){
            Genome genome = candidates.random();
            int selectedIndex = candidates.indexOf(genome, true);

            if(fittestCandidateIndex1 == null || selectedIndex < fittestCandidateIndex1){
                fittestCanddiateIndex2 = fittestCandidateIndex1;
                fittestCandidateIndex1 = selectedIndex;
            }else if (fittestCanddiateIndex2 == null || selectedIndex < fittestCanddiateIndex2){
                fittestCanddiateIndex2 = selectedIndex;
            }
        }
        return new Genome[]{candidates.items[fittestCandidateIndex1], candidates.items[fittestCanddiateIndex2]};
    }

        private Genome crossOver(Genome candidate1, Genome candidate2){
            return new Genome(
                candidate1.getFitness() * candidate1.getAggregatedHeight() + candidate2.getFitness() * candidate2.getAggregatedHeight(),
                candidate1.getFitness() * candidate1.getCompleteLines() + candidate2.getFitness() * candidate2.getCompleteLines(),
                candidate1.getFitness() * candidate1.getHoles() + candidate2.getFitness() * candidate2.getHoles(),
                candidate1.getFitness() * candidate1.getBumpiness() + candidate2.getFitness() * candidate2.getBumpiness(),
                    -1f
            ).normalize();
        }

        private void mutate(Genome candidate){
            double quantity = Math.random() * 0.4 - 0.2; // plus/minus 0.2
            int random = MathUtils.random(0,4);
        switch(random){
            case 0:
                candidate.mutHeight(quantity);

                break;
            case 1:
                candidate.mutLines(quantity);
                break;
            case 2:
                candidate.mutHoles(quantity);
                break;
            case 3:
                candidate.mutBump(quantity);
                break;
        }
    }


    /*
        Population size = 100
        Rounds per candidate = 5
        Max moves per round = 200
        Theoretical fitness limit = 5 * 200 * 4 / 10 = 400
    */

    Array<Genome> candidates;
    Array<Genome> nextGeneration;
    public Genome initialize(int populationSize, PartFactory partFactory) {

        candidates = new Array<Genome>(true,populationSize,Genome.class);
        nextGeneration = new Array<Genome>(true,populationSize,Genome.class);

        // Initial population generation
        for(int i = 0; i < populationSize; i++)
            candidates.add(Genome.newGenome());

        computeFitnesses(candidates, 5, 200, partFactory);
        candidates.sort(fitness);

        int count = 0;
        while(true){
            nextGeneration.clear();
            for(int i = 0; i < 30; i++){ // 30% of population
                Genome[] pair = tournamentSelectPair(candidates, 10); // 10% of population
                //console.log('fitnesses = ' + pair[0].fitness + ',' + pair[1].fitness);
                Genome candidate = crossOver(pair[0], pair[1]);
                if(Math.random() < 0.05){// 5% chance of mutation
                    mutate(candidate);
                }

                nextGeneration.add(candidate.normalize());
            }

            computeFitnesses(nextGeneration, 5, 200, partFactory);

            candidates.clear();
            candidates.addAll(nextGeneration);
            nextGeneration.clear();

            int totalFitness = 0;
            for(Genome genome : candidates){
                totalFitness += genome.getFitness();
            }

            count++;
            break;
        }
        return candidates.first();
    }



}
