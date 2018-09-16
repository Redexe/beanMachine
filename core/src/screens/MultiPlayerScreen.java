package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Pool;

import engine.GameEvent;
import engine.GameStatue;
import engine.GridData;
import engine.InGameScreen;
import engine.LineAnimate;
import engine.Player;
import engine.Row;
import managers.PartFactory;
import managers.ScreenManager;
import objects.GridActor;
import objects.Maneuver;
import objects.NpcGame;
import objects.PartDisplay;
import objects.PuzzleElement;
import objects.Tetromino;

import static objects.PuzzleElement.PartData.*;

public class MultiPlayerScreen  extends GameScreen implements InGameScreen , Player {

    final Array<GameEvent> events = new Array<GameEvent>(true,5, GameEvent.class);
    boolean gameOver =  false;
    private AssetManager assetManager;
    private Stage uiStage;
    private Skin skin;
    private boolean started;
    private int level;
    private int lines;
    private int score;
    boolean quickFall;
    private Action gameAction;
    private boolean hard;
    private int lastBarrier;
    private GridActor grid;
    private Array<Player> players;
    private Array<Integer> hits;

    public MultiPlayerScreen(){

    }

    @Override
    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    @Override
    public void setStage(Stage stage) {
        this.uiStage = stage;
    }

    @Override
    public void show() {
        if(!started){
//            uiStage.setDebugAll(false);
            start();
            started = true;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        uiStage.getViewport().update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void pauseGame() {

    }

    @Override
    public void start() {
        if(started) return;

        hits = new Array<Integer>();
        players = new Array<Player>(true,2,Player.class);
        players.add(this);

        uiStage.setDebugAll(false);

        final Array<PuzzleElement.PartData> parts = new Array<PuzzleElement.PartData>(true,7,PuzzleElement.PartData.class);
        parts.add(O_Shape);
        parts.add(I_Shape);
        parts.add(J_Shape);
        parts.add(L_Shape);
        parts.add(T_Shape);
        parts.add(S_Shape);
        parts.add(Z_Shape);

//

//
//        for(int i = 0; i < 11;i++)
//            futureShapes.add(new Tetromino(O_Shape));

        final TextureAtlas textureAtlas = new TextureAtlas("defaultSet/tileset.atlas");
        final PartFactory partFactory = new PartFactory(10,textureAtlas);

        final int col = 10;
        final int row = 24;
        final float fullHeight = GameScreen.TILE_SIZE*row;
        final float thirdWidth = GameScreen.TILE_SIZE*col;
        final Table player1Table = new Table();
        final Table player2Table = new Table();
        final Table mainTable = new Table();
        final Array<Integer[][]> playerMatches = new Array<Integer[][]>(true, 5,Integer[][].class);
        final Array<Integer[][]> npcMatches = new Array<Integer[][]>(true, 5,Integer[][].class);

        final Label scoreLbl = new Label("Score "+score,skin,"fancy");
        final Label linesLbl = new Label("Lines "+lines,skin,"fancy");
        final Label levelLbl = new Label("Level "+level,skin,"fancy");

        mainTable.center();
        mainTable.setFillParent(true);
        mainTable.defaults().pad(15);

        final Pool<Row> rowPool = new Pool<Row>(){
            @Override
            protected Row newObject() {
                return new Row(col,row);
            }
        };

        final NpcGame game = new NpcGame(textureAtlas,  parts, GameScreen.TILE_SIZE, thirdWidth, fullHeight, col, row) {

            @Override
            protected void matchEvent(Integer[][] rows) {
                npcMatches.add(rows);
            }

            @Override
            protected void outOfBounds(int x, int y, byte[][] data, int rotation) {

            }

            @Override
            protected void outOfBounds(int x, int y, PuzzleElement.PartData partData, int rotation) {

            }

            @Override
            protected void fallCollision(Tetromino tetromino, float px, float py) {
//              2.)


            }
        };

        final PartDisplay nextPartDisplay = new PartDisplay(textureAtlas);
        final PartDisplay heldPartDisplay = new PartDisplay(textureAtlas);
        final GridData data = new GridData(col, row) {
            @Override
            public void dataChange(int x, int y, byte value) {
                grid.enable(x,y,value);
            }
        };


        final Label[][] weights = new Label[row][col];
        for(int r = 0; r > data.getRow(); r++)
            for(int c = 0; c > data.getCol(); c++)
                weights[r][c] = new Label("0", skin);

        final GameStatue gameStatue  = new GameStatue() {
            public int oldMode;
            public int newMode;
            public int mode = 0;
            @Override
            public int getMode() {
                return mode;
            }

            @Override
            public void setMode(int mode) {
               this. mode = mode;
            }

            @Override
            public void changeMode(int newMode) {
                this. newMode = newMode;
            }

            @Override
            public void modeChanged(int oldMode, int newMode) {
                this. oldMode = oldMode;
                this. newMode = newMode;
            }
        };

        /**
         * Player one
         */
        grid = new GridActor(data, textureAtlas,weights) {

            @Override
            public void spectacularManeuver(Maneuver maneuver, int value, boolean b2b) {

            }

            @Override
            protected void matchEvent(Integer[][] rows) {
                playerMatches.add(rows);
            }

            @Override
            protected void outOfBounds(int x, int y, byte[][] d, int rotation) {

                final Table table = new Table(skin);
                table.setFillParent(true);
                table.center();
                table.add(new Label("GameOver, Nigger",skin,"fancy")).colspan(2);
                table.row();
                TextButton retryBtn = new TextButton("Play again",skin);
                TextButton mainBtn = new TextButton("Main menu",skin);
                table.add(retryBtn);
                table.add(mainBtn);
                uiStage.addActor(table);
                gameOver = true;


                retryBtn.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {

                        lines = 0;
                        score = 0;
                        level = 0;

                        scoreLbl.setText("Score "+score);
                        linesLbl.setText("Lines "+lines);
                        levelLbl.setText("Level "+level);

                        grid.reset();
                        table.remove();
                        gameOver = false;
                        uiStage.addAction(gameAction);

                    }
                });

                mainBtn.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {

                        ScreenManager manager = ScreenManager.getInstance();
                        ArrayMap<String, Class<?>> data = new ArrayMap<String, Class<?>>();
                        manager.loadScreen(data,new MainMenu());


                    }
                });
            }

            @Override
            protected void outOfBounds(int x, int y, PuzzleElement.PartData pd, int rotation) {

                final Table table = new Table(skin);
                table.setFillParent(true);
                table.center();
                table.add(new Label("GameOver, Nigger",skin,"fancy")).colspan(2);
                table.row();
                TextButton retryBtn = new TextButton("Play again",skin);
                TextButton mainBtn = new TextButton("Main menu",skin);
                table.add(retryBtn);
                table.add(mainBtn);
                uiStage.addActor(table);
                gameOver = true;

                retryBtn.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {

                        lines = 0;
                        score = 0;
                        level = 0;

                        scoreLbl.setText("Score "+score);
                        linesLbl.setText("Lines "+lines);
                        levelLbl.setText("Level "+level);

                        grid.reset();
                        table.remove();
                        gameOver = false;
                        uiStage.addAction(gameAction);

                    }
                });

                mainBtn.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {

                        ScreenManager manager = ScreenManager.getInstance();
                        ArrayMap<String, Class<?>> data = new ArrayMap<String, Class<?>>();
                        manager.loadScreen(data,new MainMenu());


                    }
                });
            }

            @Override
            public void fallCollision(Tetromino tetromino, float x, float y) {

                grid.getGridData().addShape(tetromino,isOneColor());
                if(gameOver){
                    gameStatue.setMode(GameStatue.gameOver);
                }else{
                    grid.setNewPiece(partFactory);
                    gameStatue.setMode(GameStatue.check_cycle);
                }

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
                events.add(new LineAnimate(textureAtlas,lines, gridActor,rowPool) {
                    @Override
                    public void complete(Integer[][][] lines) {
                        playerMatches.clear();
                    }
                });
            }
        };
        grid.setSize(thirdWidth,fullHeight);

        mainTable.add(grid).size(thirdWidth,fullHeight);
        mainTable.add(player1Table);
        grid.setOneColor(false);
        /**
         * Player two
         */

        mainTable.add(player2Table);

        /**
         * Player 2 future shapes
         */
        final PartDisplay[] partDisplay = new PartDisplay[11];
        final Table futureTable = new Table();
        partDisplay[0] = new PartDisplay(textureAtlas);
        futureTable.add( partDisplay[0] ).size(100);
        futureTable.row();

        for(int i = 0; i < partDisplay.length-1; i++){
            partDisplay[i] = new PartDisplay(textureAtlas).setPart(partFactory.get(i));
            futureTable.add( partDisplay[i] ).size(75);
            futureTable.row();
        }


        player1Table.add(futureTable);


        uiStage.addActor(mainTable);

        Table rightButtonTable = new Table();
        rightButtonTable.defaults().size(100,100);
        rightButtonTable.setFillParent(true);
        rightButtonTable.bottom().right();

        Table leftButtonTable = new Table();
        leftButtonTable.defaults().size(100,100);
        leftButtonTable.setFillParent(true);
        leftButtonTable.bottom().left();


        TextButton leftButton  = new TextButton("L",skin);
        TextButton rightButton = new TextButton("R",skin);
        TextButton rotateRight = new TextButton("RR",skin);
        TextButton rotateLeft  = new TextButton("RL",skin);
        final TextButton hardDrop  = new TextButton("DROP",skin);
        TextButton hold  = new TextButton("HOLD",skin);

        rightButtonTable.add(hardDrop);
        leftButtonTable.add(hold);

        rightButtonTable.row();
        leftButtonTable.row();

        rightButtonTable.add(rotateRight);
        leftButtonTable.add(rotateLeft);

        rightButtonTable.row();
        leftButtonTable.row();


        leftButtonTable.add(leftButton);
        rightButtonTable.add(rightButton);

        uiStage.addActor(leftButtonTable);
        uiStage.addActor(rightButtonTable);


        Table infoTable = new Table();
        infoTable.setFillParent(true);
        infoTable.top().right();

        infoTable.add(levelLbl).width(100);
        infoTable.row();
        infoTable.add(scoreLbl).width(100);
        infoTable.row();
        infoTable.add(linesLbl).width(100);
        infoTable.row();
        infoTable.add(nextPartDisplay).size(100,100);
        infoTable.row();
        infoTable.add(heldPartDisplay).size(100,100);

        uiStage.addActor(infoTable);

        level = 0;
        leftButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(gameStatue.getMode() == GameStatue.falling_cycle)
                    if(!grid.collision(-1,0))
                        grid.moveLeft();
            }
        });

        rightButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(gameStatue.getMode() == GameStatue.falling_cycle)
                    if(!grid.collision(1,0))
                        grid.moveRight();
            }
        });
        rotateLeft.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(gameStatue.getMode() == GameStatue.falling_cycle)
                    grid.rotateLeft();
            }
        });

        rotateRight.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(gameStatue.getMode() == GameStatue.falling_cycle)
                    grid.rotateRight();
            }
        });

        hold.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        hardDrop.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(hard){
                    if(gameStatue.getMode() == GameStatue.falling_cycle) {
                        boolean collision;
                        do {
                            collision = grid.collision(0,-1);
                            if(!collision)
                                grid.getTetromino().moveBy(0,-1);

                        }while(!collision);
                    }
                }

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if(!hard){
                    quickFall = true;
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if(!hard){
                    quickFall = false;
                }
            }


        });


        uiStage.addAction(new Action() {
            @Override
            public boolean act(float delta) {

                if(Gdx.input.isKeyJustPressed(Input.Keys.A)){
                    if(gameStatue.getMode() == GameStatue.falling_cycle)
                        if(!grid.collision(-1,0))
                            grid.moveLeft();
                }
                if(Gdx.input.isKeyJustPressed(Input.Keys.D)){
                    if(gameStatue.getMode() == GameStatue.falling_cycle)
                        if(!grid.collision(1,0))
                            grid.moveRight();
                }
                if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
                    if(gameStatue.getMode() == GameStatue.falling_cycle)
                        grid.rotateRight();
                }
                if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
                    if(gameStatue.getMode() == GameStatue.falling_cycle)
                        grid.rotateLeft();
                }
                if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
                    if(gameStatue.getMode() == GameStatue.falling_cycle) {
                        boolean collision;
                        do {
                            collision = grid.collision(0,-1);
                            if(!collision)
                                grid.getTetromino().moveBy(0,-1);

                        }while(!collision);
                    }

                }

                if(quickFall){
                    if(gameStatue.getMode() == GameStatue.falling_cycle){
                        if(!grid.collision(0,-1))
                            grid.getTetromino().moveBy(0,-1);
                    }else{
                        quickFall = false;
                    }
                }

                return false;
            }
        });




//
//
        players.add(game);
        game.setPlayers(players.toArray());
        uiStage.addAction(game);

        mainTable.add(game.getGrid());

        Action beanMachine = new Action() {
            boolean gameOver = false;

            @Override
            public boolean act(float delta) {

                switch (gameStatue.getMode()) {
                    case GameStatue.falling_cycle:

                        break;
                    case GameStatue.check_cycle:

                        break;
                    case GameStatue.clear_cycle:

                        break;
                    case GameStatue.wait:

                        break;
                    case GameStatue.gameOver:

                        break;
                    case GameStatue.newCycle:

                        break;
                }

                return gameOver;
            }
        };


        Action tetris = new Action() {
            boolean gameOver = false;

            @Override
            public boolean act(float delta) {

//                System.out.println("Barrier "+game.getGrid().getBarrierOffset());


                switch (gameStatue.getMode()) {
                    case GameStatue.falling_cycle:

                        runFallCycle(delta, grid, gameStatue);

                        break;
                    case GameStatue.check_cycle:
                        runCheckCycle(delta, grid, gameStatue, playerMatches, gameStatue);

                        break;
                    case GameStatue.clear_cycle:
                        if (runClearCycle(delta, grid, gameStatue, playerMatches, levelLbl, linesLbl, scoreLbl, rowPool, game))
                            playerMatches.clear();

                        break;
                    case GameStatue.wait:
                        runWaitCycle(delta, grid, gameStatue);
                        break;
                    case GameStatue.gameOver:

                        break;
                    case GameStatue.newCycle:
                        if (events.size != 0) gameStatue.setMode(GameStatue.wait);

                        else {
                            grid.setNewPiece(partFactory);
                            if(grid.blocked()){
                                gameStatue.setMode(GameStatue.wait);

                            }else
                                gameStatue.setMode(GameStatue.falling_cycle);
                            for (int i = 0; i < partDisplay.length - 1; i++) {
                                Tetromino p = partFactory.get(i);
                                partDisplay[i].setPart(p);
                            }
                        }

                        break;
                }

                return gameOver;
            }
        };

//      1.)

//      player one action, this action runs game
        uiStage.addAction(beanMachine);

        grid.updateGrid();
        gameStatue.setMode(GameStatue.newCycle);

    }

    private void runWaitCycle(float delta, GridActor grid, GameStatue gameStatue) {
        if( events.size == 0 )
            gameStatue.setMode(GameStatue.newCycle);

        processHits();

        for(int i = 0; i < events.size; i++){
            GameEvent gameEvent = events.get(i);

            if(gameEvent.isDone())
                events.removeIndex(i);
            else
                gameEvent.act(delta);
        }

    }

    private boolean runClearCycle(float delta, GridActor grid, GameStatue gameStatue, Array<Integer[][]> matches, Label levelLbl, Label linesLbl, Label scoreLbl, Pool<Row> rowPool, NpcGame npcGame) {
        int barrier = 0;
        int line = 0;

        for (Integer[][] rows : matches) {

            for (Integer[] row : rows) {
//
                for (Integer roeIndex : row) {
                    line++;
                    int lineValue = 40;
                    switch(rows.length){
                        case 1:
                            lineValue = 40;
                            break;
                        case 2:
                            line = 100;
                            barrier++;
                            break;
                        case 3:
                            lineValue = 300;
                            break;
                        case 4:
                            lineValue = 1200;
                            line+=2;
                            break;
                    }

                    lines+=rows.length;
                    score+=(lineValue*(level+1));

                    scoreLbl.setText("Score "+score);
                    linesLbl.setText("Lines "+lines);
                    levelLbl.setText("Level "+level);
//
                }
//
            }
//
        }


        /**
         * VS Assault by line
         */


        makeHit(line);
        int barrierOffset = grid.getBarrierOffset();
        barrierOffset -= barrier;
//        addBarrierOffset(false,grid,barrierOffset);

        // FIXME: 7/28/2018 when removing, rows positions change. this makes indexes wrong raking from the top down would make a reversal unnecessary...

        grid.removeRow(rowPool,matches.toArray());
        gameStatue.setMode(GameStatue.wait);
        matches.clear();
        return true;
    }

//    private void setBarrierOffset(boolean add,GridActor grid, int barrier) {
//        if(grid.getBarrierOffset() <= 0 && !add) return;
//        events.add(new BarrierAnimate(barrier,grid) {
//            @Override
//            public void complete() {
//
//            }
//        });
//    }

    private void addBarrierOffset(boolean add, GridActor grid, int barrier) {
//        events.add(new BarrierAnimate(grid.getBarrierOffset() + barrier,grid) {
//            @Override
//            public void complete() {
//
//            }
//        });
    }

    private void runCheckCycle(float delta, GridActor grid, GameStatue npcGamesStatus, Array<Integer[][]> matches, GameStatue gameStatue) {

        int rows = grid.getGridData().getRow();
        int cols = grid.getGridData().getCol();

        grid.checkMatches(delta);


        if(matches.size != 0){
            npcGamesStatus.setMode(GameStatue.wait);
        }else {
            npcGamesStatus.setMode(GameStatue.clear_cycle);
        }
    }

    private void processHits() {

        int hitValue = 0;
        for(Integer hit : hits){
            hitValue+=hit;
        }

        if(hitValue > 0)
            addBarrierOffset(true,grid,hitValue);
        hits.clear();

    }

    private void runFallCycle(float delta,GridActor grid, GameStatue state) {
        boolean collision = grid.fall(delta);
        if(collision) state.changeMode(GameStatue.check_cycle);
    }

    private Label[][] initiateWeights(int col, int row) {
        final Label[][] labels = new Label[row][col];
        for(int y = 0; y < row; y++){
            for(int x = 0; x < col; x++){
                labels[y][x] = new Label("-1", skin);
                labels[y][x].setPosition(x,y);
            }
        }
        return labels;
    }

    @Override
    public void restart() {

    }

    @Override
    public void quite() {

    }
    
    @Override
    public void takeHit(int hit) {
//        setBarrierOffset(true,grid,hit);


    }

    @Override
    public void makeHit(int hit) {
        //      Don't send empty event
        if(hit == 0) return;

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
}
