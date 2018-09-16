package objects;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

import engine.AI;
import engine.AIState;
import engine.GameEvent;
import engine.GameStatue;
import engine.GridData;
import engine.LineAnimate;
import engine.NpcGameStatus;
import engine.Player;
import managers.PartFactory;

public abstract class NpcGame extends Action implements Player {

    final Array<Integer[][]> matches = new Array<Integer[][]>(true, 5,Integer[][].class);
    final Array<Tetromino> futureShapes = new Array<Tetromino>(true, 50, Tetromino.class);
    final Array<GameEvent> events = new Array<GameEvent>(true, 5, GameEvent.class);

    private final Array<PuzzleElement.PartData> parts;
    private GridActor grid;
    private NpcGameStatus npcGamesStatus;
    private AI ai;
    private int lines;
    private int score;
    private int level;
    private boolean gameOver;
    private GridData data;
    private int lastBarrier;
    private Player[] players;
    private Array<Integer> hits;
    private PartFactory partFactory;

    protected NpcGame(TextureAtlas textureAtlas, final Array<PuzzleElement.PartData> parts, final int tileSize, float thirdWidth, float fullHeight, int col, int row) {
        this.parts = parts;
        initGi(textureAtlas, tileSize, thirdWidth, fullHeight, col, row);
        ai.getStateMachine().changeState(AIState.EVALUATE_GRID_WEIGHTS);
        hits = new Array<Integer>();

    }

    public void start(){


    }

    private void initGi(final TextureAtlas textureAtlas, final int tileSize, float thirdWidth, float fullHeight, int col, int row) {
        data = new GridData(col, row) {
            @Override
            public void dataChange(int x, int y, byte value) {
                grid.enable(x,y,value);
            }
        };



        npcGamesStatus = new NpcGameStatus() {
            @Override
            public void modeChanged(int oldMode, int newMode) {
                if(newMode == GameStatue.falling_cycle)
                    ai.getStateMachine().changeState(AIState.EVALUATE_GRID_WEIGHTS);
            }
        };

        partFactory = new PartFactory(50,textureAtlas);
        grid = new GridActor(data, textureAtlas, null) {

            @Override
            public void spectacularManeuver(Maneuver maneuver, int value, boolean b2b) {

            }

            @Override
            protected void matchEvent(Integer[][] rows) {
                matches.add(rows);
                NpcGame.this.matchEvent(rows);
            }

            @Override
            protected void outOfBounds(int x, int y, byte[][] data, int rotation) {
                NpcGame.this.outOfBounds(x,y,data,rotation);
            }

            @Override
            protected void outOfBounds(int x, int y, PuzzleElement.PartData partData, int rotation) {
                NpcGame.this.outOfBounds(x,y,partData,rotation);
            }

            @Override
            public void fallCollision(Tetromino tetromino, float px, float py) {
                //                ai.getStateMachine().changeState(AIState.WAIT);
//                AI must wait, this cycle occurs every time a block is added to the grid
                grid.getGridData().addShape(tetromino,isOneColor());
                grid.setNewPiece(partFactory);
                npcGamesStatus.setMode(GameStatue.check_cycle);

                NpcGame.this.fallCollision(tetromino,px,py);
            }

            @Override
            protected void createSplashText(Integer total) {
//                final Image image = new Image(textAtlas.findRegion(String.valueOf(count))){
//                    @Override
//                    public void act(float delta) {
//                        super.act(delta);
//                    }
//                };
//                image.setPosition(grid.getWidth()/2 - image.getWidth() / 2,grid.getHeight()/2 - image.getHeight() / 2);
//                grid.addActor(image);
//                image.toFront();
            }

            @Override
            protected void createSplashBar(Integer line, Integer[][][] lineGroups, ShapeRenderer shapeRenderer) {

            }

            @Override
            protected void animateLineRemoval(Integer[][][] lines, GridActor gridActor) {


                events.add(new LineAnimate(textureAtlas,lines, gridActor,null) {
                    @Override
                    public void complete(Integer[][][] lines) {
                        matches.clear();
                    }
                });
            }
        };
        grid.setSize(thirdWidth, fullHeight);
        grid.setNewPiece(partFactory);

        ai = new AI();
        ai.setGridActor(grid);
        ai.setNpcGamesStatus(npcGamesStatus);


        grid.updateGrid();
        // FIXME: 7/27/2018 Call externally

    }

    public GridActor getGrid() {
        return grid;
    }

    public boolean act(float delta) {

        switch (npcGamesStatus.getMode()) {
            case GameStatue.falling_cycle:
                runFallCycle(delta, grid, npcGamesStatus, ai);
                break;
            case GameStatue.check_cycle:
                runCheckCycle(delta, grid, npcGamesStatus, ai, matches);
                break;
            case GameStatue.clear_cycle:
                if (runClearCycle(delta, grid, npcGamesStatus, ai, matches))
                    matches.clear();
                break;
            case GameStatue.wait:
                runWaitCycle(delta, grid, npcGamesStatus, ai);
                break;
            case GameStatue.gameOver:
                break;
            case GameStatue.newCycle:

                grid.setNewPiece(partFactory);
                resetPiece();
                futureShapes.insert(0,new Tetromino(parts.random()));
                npcGamesStatus.setMode(GameStatue.falling_cycle);

                break;
        }
        ai.act(delta);
        return gameOver;
    }

    private void resetPiece() {

        if(grid.blocked()){

//            events.add(new BarrierAnimate(true,grid.getGridData().getRow(),grid) {
//                @Override
//                public void complete() {
//                    npcGamesStatus.setMode(GameStatue.gameOver);
//                    grid.reset();
//                    lose();
//                }
//            });
            npcGamesStatus.setMode(GameStatue.wait);

        }else
            npcGamesStatus.setMode(GameStatue.falling_cycle);
    }

    private void processHits() {

        int hitValue = 0;
        for(Integer hit : hits)
            hitValue+=hit;

        if(hitValue > 0){
            grid.getBarrier().addBarrierValue(hitValue);
        }
//            addBarrierOffset(true,grid,hitValue);

        hits.clear();
    }

//

    private void runWaitCycle(float delta, GridActor grid, NpcGameStatus npcGamesStatus, AI ai) {
        if( events.size == 0 )
            npcGamesStatus.setMode(GameStatue.newCycle);

        processHits();

        for(int i = 0; i < events.size; i++){
            GameEvent gameEvent = events.get(i);
            if(!gameEvent.isDone())
                gameEvent.act(delta);
            else
                events.removeIndex(i);
        };
    }
    Array<Integer> ignoreRows = new Array<Integer>();
    private boolean runClearCycle(float delta, GridActor grid, NpcGameStatus npcGamesStatus, AI ai, Array<Integer[][]> matches) {
        int barrier = 0;
        int line = 0;

        ignoreRows.clear();

        for (Integer[][] rows : matches) {

            for (Integer[] row : rows) {

                for (Integer roeIndex : row) {

                    int lineValue = 40;
                    switch (rows.length) {
                        case 1:
                            lineValue = 40;
                            break;
                        case 2:
                            lineValue = 100;
                            barrier++;
                            break;
                        case 3:
                            lineValue = 300;
                            break;
                        case 4:
                            lineValue = 1200;
                            barrier+=2;
                            break;
                    }


                    lines += rows.length;
                    score += (lineValue * (level + 1));

                }
            }

        }



        /**
         * VS Assault by line
         */
//        barrier++;
//        makeHit(barrier);
//        int barrierOffset = grid.getBarrierOffset();
//        barrierOffset -= barrier;
////            addBarrierOffset(false, grid,barrierOffset);
//        lastBarrier = lines;


        grid.removeRow(null,matches.toArray());
        npcGamesStatus.setMode(GameStatue.wait);
        ai.getStateMachine().changeState(AIState.EVALUATE_GRID_WEIGHTS);
        matches.clear();
        return true;
    }

//    private void addBarrierOffset(boolean add, final GridActor grid, final int barrier) {
//        events.add(new BarrierAnimate(add,grid.getBarrierOffset() + barrier,grid) {
//            @Override
//            public void complete() {
//
//            }
//        });
//    }
//
//    private void setBarrierOffset(boolean add, GridActor grid, int barrier) {
//        events.add(new BarrierAnimate(add,barrier,grid) {
//            @Override
//            public void complete() {
//
//            }
//        });
//    }

    private void runCheckCycle(float delta, GridActor grid, NpcGameStatus npcGamesStatus, AI ai, Array<Integer[][]> matches) {
        grid.checkMatches(delta);

        if (matches.size > 0)
            npcGamesStatus.setMode(GameStatue.clear_cycle);
        else
            npcGamesStatus.setMode(GameStatue.wait);

    }
    private void runFallCycle(float delta, GridActor grid, NpcGameStatus state, AI ai) {
        boolean collision = grid.fall(delta);
        if (collision) state.changeMode(GameStatue.check_cycle);
    }

    public void gameOver() {
        npcGamesStatus.setMode(GameStatue.gameOver);
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }
    @Override
    public void takeHit(int hit) {
        if(hit == 0) throw new GdxRuntimeException("hit value was zero");

        hits.add(hit);
    }

    @Override
    public void makeHit(int hit) {
        for(Player player :players){
            if(player != this){
                player.takeHit(hit);
            }
        }
    }

    @Override
    public void win() {
        for(Player player :players){
            if(player != this){
                player.lose();
            }
        }
    }

    @Override
    public void lose() {

    }

    protected abstract void matchEvent(Integer[][] rows);
    protected abstract void outOfBounds(int x, int y, byte[][] data, int rotation);
    protected abstract void outOfBounds(int x, int y, PuzzleElement.PartData partData, int rotation) ;
    protected abstract void fallCollision(Tetromino tetromino, float px, float py) ;


}
