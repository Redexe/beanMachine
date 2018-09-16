package engine;

import com.badlogic.gdx.utils.Array;

import org.apache.commons.lang.SerializationUtils;

import engine.genericAI.FutureMove;
import objects.GridActor;
import objects.PuzzleElement;

public class LearningAI {

//    private Array<Genome> genomes = new Array<Genome>();
//    int populationSize = 50;
//    int currentGenome = -1;
//    int generation = 0;
//    //rate of mutation
//    float mutationRate = 0.05f;
//    //helps calculate mutation
//    float mutationStep = 0.2f;
//    int movesTaken = 0;
//    /**
//     * Creates the initial population of genomes, each with random genes.
//     */
//    void createInitialPopulation() {
//        for (int i = 0; i < populationSize; i++) {
//            Genome genome = new Genome();
//            genomes.add(genome);
//        }
//        evaluateNextGenome();
//    }
//
//    /**
//     * Evaluates the next genome in the population. If there is none, evolves the population.
//     */
//    void evaluateNextGenome() {
//        //increment index in genome array
//        currentGenome++;
//        //If there is none, evolves the population.
//        if (currentGenome == genomes.size) {
//            evolve();
//        }
//        movesTaken = 0;
//        makeNextMove();
//    }
//
//    /**
//     * Evolves the entire population and goes to the next generation.
//     */
//    void evolve() {
//
//        console.log("Generation " + generation + " evaluated.");
//        //reset current genome for new generation
//        currentGenome = 0;
//        //increment generation
//        generation++;
//        //resets the game
//        reset();
//        //gets the current game state
//        roundState = getState();
//        //sorts genomes in decreasing order of fitness values
//        genomes.sort(function(a, b) {
//            return b.fitness - a.fitness;
//        });
//        //add a copy of the fittest genome to the elites list
//        archive.elites.push(clone(genomes[0]));
//        console.log("Elite's fitness: " + genomes[0].fitness);
//
//        //remove the tail end of genomes, focus on the fittest
//        while(genomes.length > populationSize / 2) {
//            genomes.pop();
//        }
//        //sum of the fitness for each genome
//        var totalFitness = 0;
//        for (var i = 0; i < genomes.length; i++) {
//            totalFitness += genomes[i].fitness;
//        }
//
//        //get a random index from genome array
//        function getRandomGenome() {
//            return genomes[randomWeightedNumBetween(0, genomes.length - 1)];
//        }
//        //create children array
//        var children = [];
//        //add the fittest genome to array
//        children.push(clone(genomes[0]));
//        //add population sized amount of children
//        while (children.length < populationSize) {
//            //crossover between two random genomes to make a child
//            children.push(makeChild(getRandomGenome(), getRandomGenome()));
//        }
//        //create new genome array
//        genomes = [];
//        //to store all the children in
//        genomes = genomes.concat(children);
//        //store this in our archive
//        archive.genomes = clone(genomes);
//        //and set current gen
//        archive.currentGeneration = clone(generation);
//        console.log(JSON.stringify(archive));
//        //store archive, thanks JS localstorage! (short term memory)
//        localStorage.setItem("archive", JSON.stringify(archive));
//    }
//
//    float randomChoice(float propOne, float propTwo) {
//        if (Math.round(Math.random()) == 0) {
//            return propOne;
//        } else {
//            return propTwo;
//        }
//    }
//
//    /**
//     * Creates a child genome from the given parent genomes, and then attempts to mutate the child genome.
//     * @param  {Genome} mum The first parent genome.
//     * @param  {Genome} dad The second parent genome.
//     * @return {Genome}     The child genome.
//     */
//    Genome makeChild( Genome mum, Genome dad) {
//        //init the child given two genomes (its 7 parameters + initial fitness value)
//       final Genome child = new Genome(Math.random(),
//                //all these params are randomly selected between the mom and dad genome
//                ,randomChoice(mum.getRowsCleared(), dad.getRowsCleared()),
//                ,randomChoice(mum.getWeightedHeight(), dad.getWeightedHeight()),
//                ,randomChoice(mum.getCumulativeHeight(), dad.getCumulativeHeight()),
//                ,randomChoice(mum.getRelativeHeight(), dad.getRelativeHeight()),
//                ,randomChoice(mum.getHoles(), dad.getHoles()),
//                ,randomChoice(mum.getRoughness(), dad.getRoughness()),
//                , -1);
//
//        //mutation time!
//
//        //we mutate each parameter using our mutationstep
//        if (Math.random() < mutationRate) {
//            child.setRowsCleared((float) (child.getRowsCleared() + Math.random() * mutationStep * 2 - mutationStep));
//        }
//        if (Math.random() < mutationRate) {
//            child.setWeightedHeight((float) (child.getWeightedHeight() + Math.random() * mutationStep * 2 - mutationStep));
//        }
//        if (Math.random() < mutationRate) {
//            child.setCumulativeHeight((float) (child.getCumulativeHeight() + Math.random() * mutationStep * 2 - mutationStep));
//        }
//        if (Math.random() < mutationRate) {
//            child.setRelativeHeight((float) (child.getRelativeHeight() + Math.random() * mutationStep * 2 - mutationStep));
//        }
//        if (Math.random() < mutationRate) {
//            child.setHoles((float) (child.getHoles() + Math.random() * mutationStep * 2 - mutationStep));
//        }
//        if (Math.random() < mutationRate) {
//            child.setRoughness((float) (child.getRoughness() + Math.random() * mutationStep * 2 - mutationStep));
//        }
//        return child;
//    }
//
//    /**
//     * Returns an array of all the possible moves that could occur in the current state, rated by the parameters of the current genome.
//     * @return {Array} An array of all the possible moves that could occur.
//     */
//    int getAllPossibleMoves(Grid grid, Array<Genome> genomes) {
//        Array<GridLocation> possibleMoves = new Array<GridLocation>(true,50,GridLocation.class);
//        var possibleMoveRatings = [];
//        int iterations = 0;
//        //for each possible rotation
//        for (int rots = 0; rots < 4; rots++) {
//
//            var oldX = [];
//            //for each iteration
//            for (int t = -5; t <= 5; t++) {
//                iterations++;
//
//                //rotate shape
//                for (int j = 0; j < rots; j++) {
//                    rotateShape();
//                }
//                //move left
//                if (t < 0) {
//                    for (int l = 0; l < Math.abs(t); l++) {
//                        moveLeft();
//                    }
//                    //move right
//                } else if (t > 0) {
//                    for (int r = 0; r < t; r++) {
//                        moveRight();
//                    }
//                }
//                Genome genome;
//                //if the shape has moved at all
//                if (!contains(oldX, currentShape.x)) {
//                    //move it down
//                    Result moveDownResults = moveDown();
//                    while (moveDownResults.isMoved()) {
//                        moveDownResults = moveDown();
//                    }
//                    //set the 7 parameters of a genome
//                    genome = new Genome(
//                            moveDownResults.rowsCleared,
//                            Math.pow(getHeight(grid), 1.5),
//                            getCumulativeHeight(grid),
//                            getRelativeHeight(grid),
//                            getHoles(grid),
//                            getRoughness(grid));
// 				}
//                    //rate each move
//                    int rating = 0;
//                    rating += genome.getRowsCleared() *genomes.get(currentGenome).getRowsCleared();
//                    rating += genome.getWeightedHeight() *genomes.get(currentGenome).getWeightedHeight();
//                    rating += genome.getCumulativeHeight() *genomes.get(currentGenome).getCumulativeHeight();
//                    rating += genome.getRelativeHeight() *genomes.get(currentGenome).getRelativeHeight();
//                    rating += genome.getHoles() *genomes.get(currentGenome).getHoles();
//                    rating += genome.getRoughness() *genomes.get(currentGenome).getRoughness();
//                    //if the move loses the game, lower its rating
//                    if (moveDownResults.lose) {
//                        rating -= 500;
//                    }
//                    //push all possible moves, with their associated ratings and parameter values to an array
//                    possibleMoves.push({rotations: rots, translation: t, rating: rating, algorithm: algorithm});
//                    //update the position of old X value
//                    oldX.push(currentShape.x);
//                }
//            }
//        }
//
//        //return array of all possible moves
//        return possibleMoves;
//    }
//
//    /**
//     * Returns the highest rated move in the given array of moves.
//     * @param  {Array} moves An array of possible moves to choose from.
//     * @return {Move}       The highest rated move from the moveset.
//     */
//    int getHighestRatedMove(moves) {
//        //start these values off small
//        int maxRating = Integer.MIN_VALUE;
//        int maxMove = -1;
//        Array<Integer> ties = new Array<Integer>();
//        //iterate through the list of moves
//        for (int index = 0; index < moves.length; index++) {
//            //if the current moves rating is higher than our maxrating
//            if (moves[index].rating > maxRating) {
//                //update our max values to include this moves values
//                maxRating = moves[index].rating;
//                maxMove = index;
//                //store index of this move
//                ties = [index];
//            } else if (moves[index].rating == maxRating) {
//                //if it ties with the max rating
//                //add the index to the ties array
//                ties.push(index);
//            }
//        }
//        //eventually we'll set the highest move value to this move var
//        int move = moves[ties[0]];
//        //and set the number of ties
//        move.algorithm.ties = ties.length;
//        return move;
//    }
//
//    /**
//     * Makes a move, which is decided upon using the parameters in the current genome.
//     */
//    function makeNextMove() {
//        //increment number of moves taken
//        movesTaken++;
//        //if its over the limit of moves
//        if (movesTaken > moveLimit) {
//            //update this genomes fitness value using the game score
//            genomes[currentGenome].fitness = clone(score);
//            //and evaluates the next genome
//            evaluateNextGenome();
//        } else {
//            //time to make a move
//
//            //we're going to re-draw, so lets store the old drawing
//            var oldDraw = clone(draw);
//            draw = false;
//            //get all the possible moves
//            var possibleMoves = getAllPossibleMoves();
//            //lets store the current state since we will update it
//            var lastState = getState();
//            //whats the next shape to play
//            nextShape();
//            //for each possible move
//            for (var i = 0; i < possibleMoves.length; i++) {
//                //get the best move. so were checking all the possible moves, for each possible move. moveception.
//                var nextMove = getHighestRatedMove(getAllPossibleMoves());
//                //add that rating to an array of highest rates moves
//                possibleMoves[i].rating += nextMove.rating;
//            }
//            //load current state
//            loadState(lastState);
//            //get the highest rated move ever
//            var move = getHighestRatedMove(possibleMoves);
//            //then rotate the shape as it says too
//            for (var rotations = 0; rotations < move.rotations; rotations++) {
//                rotateShape();
//            }
//            //and move left as it says
//            if (move.translation < 0) {
//                for (var lefts = 0; lefts < Math.abs(move.translation); lefts++) {
//                    moveLeft();
//                }
//                //and right as it says
//            } else if (move.translation > 0) {
//                for (var rights = 0; rights < move.translation; rights++) {
//                    moveRight();
//                }
//            }
//            //update our move algorithm
//            if (inspectMoveSelection) {
//                moveAlgorithm = move.algorithm;
//            }
//            //and set the old drawing to the current
//            draw = oldDraw;
//            //output the state to the screen
//            output();
//            //and update the score
//            updateScore();
//        }
//    }
//
//    /**
//     * Returns the cumulative height of all the columns.
//     * @return {Number} The cumulative height.
//     */
//    int getCumulativeHeight(Grid grid) {
//        removeShape();
//        int[] peaks = new int[]{20,20,20,20,20,20,20,20,20,20};
//        for (int row = 0; row < grid.size; row++) {
//            for (int col = 0; col < grid.get(row).size; col++) {
//                if (grid.get(row).get(col) != 0 && peaks[col] == 20) {
//                    peaks[col] = row;
//                }
//            }
//        }
//        int totalHeight = 0;
//        for (int i = 0; i < peaks.length; i++) {
//            totalHeight += 20 - peaks[i];
//        }
//        applyShape();
//        return totalHeight;
//    }
//
//    /**
//     * Returns the number of holes in the grid.
//     * @return {Number} The number of holes.
//     */
//    int getHoles(Grid grid) {
//        removeShape();
//        int[] peaks = new int[]{20,20,20,20,20,20,20,20,20,20};
//        for (int row = 0; row < grid.size; row++) {
//            for (int col = 0; col < grid.get(row).size; col++) {
//                if (grid.get(row).get(col) != 0 && peaks[col] == 20) {
//                    peaks[col] = row;
//                }
//            }
//        }
//        int holes = 0;
//        for (int row = 0; row < grid.size; row++) {
//            for (int col = 0; col < grid.get(row).size; col++) {
//                if (grid.get(row).get(col) == 0) {
//                    holes++;
//                }
//            }
//        }
//        applyShape();
//        return holes;
//    }
//
//    /**
//     * Returns an array that replaces all the holes in the grid with -1.
//     * @return {Array} The modified grid array.
//     */
//    int getHolesArray(Grid grid) {
//
//        int[] peaks = new int[]{20,20,20,20,20,20,20,20,20,20};
//        for (int row = 0; row < grid.size; row++) {
//            for (int col = 0; col < grid.get(row).size; col++) {
//                if (grid.get(row).get(col) != 0 && peaks[col] == 20) {
//                    peaks[col] = row;
//                }
//            }
//        }
//        for (int x = 0; x < peaks.length; x++) {
//            for (int y = peaks[x]; y < grid.size; y++) {
//                if (grid.get(y).get(x) == 0) {
//                    array[y][x] = -1;
//                }
//            }
//        }
//
//        return array;
//    }
//
//    /**
//     * Returns the roughness of the grid.
//     * @return {Number} The roughness of the grid.
//     */
//    void getRoughness(Grid grid) {
//
//        int[] peaks = new int[]{20,20,20,20,20,20,20,20,20,20};
//        for (int row = 0; row < grid.size; row++) {
//            for (int col = 0; col < grid.get(row).size; col++) {
//                if (grid.get(row).get(col) != 0 && peaks[col] == 20) {
//                    peaks[col] = row;
//                }
//            }
//        }
//        int roughness = 0;
//        int[] differences = new int[peaks.length];
//        for (int i = 0; i < peaks.length - 1; i++) {
//            roughness += Math.abs(peaks[i] - peaks[i + 1]);
//            differences[i] = Math.abs(peaks[i] - peaks[i + 1]);
//        }
//
//        return roughness;
//    }
//
//    /**
//     * Returns the range of heights of the columns on the grid.
//     * @return {Number} The relative height.
//     */
//    function getRelativeHeight(Grid grid) {
//
//        int[] peaks = new int[]{20,20,20,20,20,20,20,20,20,20};
//        for (int row = 0; row < grid.size; row++) {
//            for (int col = 0; col < grid.get(row).size; col++) {
//                if (grid.get(row).get(col) != 0 && peaks[col] == 20) {
//                    peaks[col] = row;
//                }
//            }
//        }
//
//        return Math.max.apply(Math, peaks) - Math.min.apply(Math, peaks);
//    }
//
//    /**
//     * Returns the height of the biggest column on the grid.
//     * @return {Number} The absolute height.
//     */
//    double getHeight(Grid grid) {
//
//        int[] peaks = new int[]{20,20,20,20,20,20,20,20,20,20};
//        for (int row = 0; row < grid.size; row++) {
//            for (int col = 0; col < grid.get(row).size; col++) {
//                if (grid.get(row).get(col) != 0 && peaks[col] == 20) {
//                    peaks[col] = row;
//                }
//            }
//        }
//
//        return 20 - Math.min.apply(Math, peaks);
//    }
//
//    /**
//     * Loads the archive given.
//     * @param  {String} archiveString The stringified archive.
//     */
//    function loadArchive(archiveString) {
//        archive = fullyEvolvedArchive;
//        genomes = clone(archive.genomes);
//        populationSize = archive.populationSize;
//        generation = archive.currentGeneration;
//        currentGenome = 0;
//        reset();
//        roundState = getState();
//        console.log("Archive loaded!");
//    }
//

    /*
	 A simple brain function.
	 Given a board, produce a number that rates
	 that board position -- larger numbers for worse boards.
	 This version just counts the height
	 and the number of "holes" in the board.
	 See Tetris-Architecture.html for brain ideas.
	*/
    public double rateBoard(GridData gridData) {
        final int width = gridData.getCol();
        final int maxHeight = gridData.getRow() - 2;

        int sumHeight = 0;
        int holes = 0;

        // Count the holes, and sum up the heights
        for (int x=0; x<width; x++) {
            final int colHeight = gridData.getCol();
            sumHeight += colHeight;

            int y = colHeight - 2;	// addr of first possible hole

            while (y>=0) {
                if  (gridData.getDataValueAt(x,y) == 0) {
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
//
//    public FutureMove bestMove(GridActor gridActor, PuzzleElement.PartData currentPiece, int limitHeight, FutureMove futureMove) {
//        final Grid grid = gridActor.getGrid();
//
//        // Allocate a move object if necessary
//        if (futureMove==null) futureMove = new FutureMove();
//
//        double bestScore = 0.0;
//        int bestX = 0;
//        int bestY = 0;
//        int rotation = 0;
//        int bestRotation = -1;
//
//        // loop through all the rotations
//        while (true) {
//            final int yBound = limitHeight - currentPiece.getData().length+1;
//            final int xBound = grid.getCol() - currentPiece.getData()[0].length+1;
//
//            // For current rotation, try all the possible columns
//            for (int x = 0; x<xBound; x++) {
//                int y = gridActor.dropHeight(currentPiece.getData()[rotation],x);
//                if (y<yBound) {	// piece does not stick up too far
//                    int result = gridActor.add(currentPiece.getData()[rotation], x, y);
//                    if (result >= GridActor.LINE_SINGLE) {
////                        if (result == Board.PLACE_ROW_FILLED) grid.clearRows();
//                        double score = rateBoard(gridActor);
//
//                        if (score<bestScore) {
//                            bestScore = score;
//                            bestX = x;
//                            bestY = y;
//                            bestRotation = rotation;
//                        }
//                    }
//                    // back out that play, loop around for the next
//                    gridActor.undo(currentPiece.getData()[rotation],x,y);
//                }
//            }
//
//            rotation++;
//
//            if ( rotation >= 4) break;	// break if back to original rotation
//        }
//
//        if (bestRotation == -1) return(null);	// could not find a play at all!
//        else {
//
//            futureMove.x = bestX;
//            futureMove.y = bestY;
//            futureMove.rotation = rotation;
//            futureMove.score = bestScore;
//
//            return futureMove;
//        }
//    }

    public Array<FutureMove> getAllMoves(GridActor gridActor, PuzzleElement.PartData currentPiece, Array<FutureMove> futureMoves) {

//        final Grid cloneGrid = (Grid) SerializationUtils.clone(gridActor.getGrid());
//        int rotation = 0;
//        // loop through all the rotations
//        while (true) {
////            final int yBound = limitHeight;//- currentPiece.getData().length+1;
////            final int xBound = grid.getCol() - currentPiece.getData()[0].length+1;
//
//            // For current rotation, try all the possible columns
//            int y = cloneGrid.getRow() - cloneGrid.getCap();
//            for (int x = 0; x<cloneGrid.getCol(); x++) {
//                while(!gridActor.absCollision(x,y-1,currentPiece.getData()[rotation])){
//
//                    if (y<cloneGrid.getRow()) {	// piece does not stick up too far
//
//                        int result = gridActor.addSim(cloneGrid,currentPiece.getData()[rotation], x, y);
//
//                        if (result >= GridActor.PLACE_OK) {
////                        if (result == Board.PLACE_ROW_FILLED) grid.clearRows();
//                            double score = rateBoard(gridActor);
//                            FutureMove move = new FutureMove();
//                            move.score = score;
//                            move.x = x;
//                            move.y = y;
//                            move.rotation = rotation;
//                            futureMoves.add(move);
//                        }
//
//                    }
//                    y-=1;
//                }
//
//            }
//
//            rotation++;
//
//            if ( rotation > 3 ) break;	// break if back to original rotation
//        }

        return futureMoves;

    }

}


