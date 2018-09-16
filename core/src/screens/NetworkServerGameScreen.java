package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Pool;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.HashMap;

import engine.FontUtils;
import engine.GameConfig;
import engine.GameEvent;
import engine.GameStatue;
import engine.GridData;
import engine.InGameScreen;
import engine.LineAnimate;
import engine.Player;
import engine.Row;
import managers.GameServer;
import managers.PartFactory;
import managers.ScreenManager;
import network.GridDisplay;
import network.Hit;
import network.PartUpdate;
import network.Pip;
import objects.BarSplash;
import objects.CommandRequest;
import objects.GridActor;
import objects.Maneuver;
import objects.PartDisplay;
import objects.PuzzleElement;
import objects.Tetromino;

import static objects.PuzzleElement.PartData.I_Shape;
import static objects.PuzzleElement.PartData.J_Shape;
import static objects.PuzzleElement.PartData.L_Shape;
import static objects.PuzzleElement.PartData.O_Shape;
import static objects.PuzzleElement.PartData.S_Shape;
import static objects.PuzzleElement.PartData.T_Shape;
import static objects.PuzzleElement.PartData.Z_Shape;

class NetworkServerGameScreen extends GameScreen implements InGameScreen, Player {


    private final byte[][] comboTest = new byte[][]{
            {1,1,1,1,1,1,0,0,1,1},
            {1,1,1,1,1,1,0,0,1,1},
            {1,1,1,1,1,1,0,0,1,1},
            {1,1,1,1,1,1,0,0,1,1},
            {1,1,1,1,1,1,0,0,1,1},
            {1,1,1,1,1,1,0,0,1,1},
            {1,1,1,1,1,1,0,0,1,1},
            {1,1,1,1,1,1,0,0,1,1},
            {1,1,1,1,1,1,0,0,1,1},
            {1,1,1,1,1,1,0,0,1,1},
            {1,1,1,1,1,1,0,0,1,1},
            {1,1,1,1,1,1,0,0,1,1},
    };
    private final byte[][] tetrisTest = new byte[][]{
            {1,1,1,1,1,1,1,0,1,1},
            {1,1,1,1,1,1,1,0,1,1},
            {1,1,1,1,1,1,1,0,1,1},
            {1,1,1,1,1,1,1,0,1,1},
            {1,1,1,1,1,1,1,0,1,1},
            {1,1,1,1,1,1,1,0,1,1},
            {1,1,1,1,1,1,1,0,1,1},
            {1,1,1,1,1,1,1,0,1,1},
            {1,1,1,1,1,1,1,0,1,1},
            {1,1,1,1,1,1,1,0,1,1},
            {1,1,1,1,1,1,1,0,1,1},
            {1,1,1,1,1,1,1,0,1,1},
    };
    private final byte[][] tSinTest = new byte[][]{
            {1,2,3,4,5,6,0,8,8,10},
            {1,2,3,4,5,0,0,0,8,10},
            {1,2,3,4,5,0,0,8,8,10},
            {1,2,3,4,5,6,0,8,8,10},
            {1,2,3,4,5,0,0,0,8,10},
            {1,2,3,4,5,0,0,8,8,10},
            {1,2,3,4,5,6,0,8,8,10},
            {1,2,3,4,5,0,0,0,8,10},
            {1,2,3,4,5,0,0,8,8,10},
            {1,2,3,4,5,6,0,8,8,10},
            {1,2,3,4,5,0,0,0,8,10},
            {1,2,3,4,5,0,0,8,8,10},
    };


    private final GameConfig gameConfig;
    private HashMap<Byte, GridDisplay> networkPlayerGrids = new HashMap<Byte, GridDisplay>();
    private boolean begin;
    private Table playerGridsTable;
    private TextureAtlas textureAtlas;
    private Table mainTable;
    private int losers;
    private float landDelay = 0.5f;
    private boolean dropped;
    private float landTime;
    private int combo;
    private boolean hadTetris;
    private Label.LabelStyle goldSplashStyle;
    private boolean tetrisB2B;
    private float updateTetrominoTime = 0;
    private AssetManager assetManager;
    private Stage uiStage;
    private Skin skin;
    private boolean built;
    private final int falling_cycle = 0;
    private int level;
    private int lines;
    private int score;

    boolean gameOver = false;
    private GridActor grid;
    private boolean quickFall;
    private boolean hard;
    final Array<GameEvent> events = new Array<GameEvent>(true, 5, GameEvent.class);
    private PartDisplay[] partDisplay;
    private PartDisplay heldPartDisplay;
    private Action tetras;
    private GameServer gameServer;

    public NetworkServerGameScreen(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
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

    }

    private void createLabelStyle() {
        goldSplashStyle = new Label.LabelStyle();
        goldSplashStyle.font = FontUtils.generateFont(16, 1, 1, 1, Color.GOLD, Color.GOLDENROD, Color.BLACK,Color.YELLOW, "font/zephyrea.ttf");
    }

    private void network() {
        if (GameServer.getGameServer().isServer()) {
            startServer();
        } else {
            startClient();
        }
    }

    private void startServer() {
        final Server server = GameServer.getGameServer().getServer();
        server.addListener(new Listener() {
            public void connected(Connection connection) {

            }

            public void disconnected(Connection connection) {

            }

            public void received(Connection connection, Object object) {

                if (object instanceof PartUpdate) {
                    final PartUpdate partUpdate = (PartUpdate) object;
                    networkPlayerGrids.get(partUpdate.id).setPiece(partUpdate.part, partUpdate.x, partUpdate.y, partUpdate.rotation);
                    server.sendToAllExceptTCP(connection.getID(), partUpdate);
                }
                if (object instanceof CommandRequest) {

                    final CommandRequest commandRequest = (CommandRequest) object;
                    final String command = commandRequest.command;

                    if (command.contains("state")) {
                        if (command.contains("start")) {
                            uiStage.addAction(tetras);
                        }
                        if (command.contains("lose")) {
                            losers++;
                            if(losers >= gameConfig.players-1)
                                win();
                        }

                    }

                }
                if (object instanceof Pip) {
                    final Pip pip = (Pip) object;
                    GridDisplay display = networkPlayerGrids.get(pip.id);
                    display.setPieceAt(pip.x, pip.y, pip.value);
                    server.sendToAllExceptTCP(connection.getID(), pip);
                }
                if (object instanceof Hit) {
                    final Hit hit = (Hit) object;
                    takeHit(hit.hitValue);
                    server.sendToAllExceptTCP(connection.getID(), hit);
                }
            }

            public void idle(Connection connection) {

            }

        });
    }

    private void startClient() {
        Client client = GameServer.getGameServer().createClient();


        client.addListener(new Listener() {
            public void connected(Connection connection) {
            }

            public void disconnected(Connection connection) {

            }

            public void received(Connection connection, Object object) {

                if (object instanceof PartUpdate) {
                    final PartUpdate partUpdate = (PartUpdate) object;
                    GridDisplay display = networkPlayerGrids.get(partUpdate.id);
                    display.setPiece(partUpdate.part, partUpdate.x, partUpdate.y, partUpdate.rotation);

                }
                if (object instanceof CommandRequest) {

                    final CommandRequest commandRequest = (CommandRequest) object;
                    final String command = commandRequest.command;

                    if (command.contains("state")) {
                        if (command.contains("start")) {
                            uiStage.addAction(tetras);
                        }
                        if (command.contains("lose")) {
                            losers++;
                            if(losers >= gameConfig.players-1)
                                win();
                        }

                    }

                }


                if (object instanceof Pip) {
                    final Pip pip = (Pip) object;
                    GridDisplay display = networkPlayerGrids.get(pip.id);
                    display.setPieceAt(pip.x, pip.y, pip.value);
                }
                if (object instanceof Hit) {
                    final Hit hit = (Hit) object;
                    takeHit(hit.hitValue);
                }
            }

            public void idle(Connection connection) {
            }

        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.135f, .206f, .235f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        uiStage.act(delta);
        uiStage.draw();
        if(!built)
            build();
    }

    private void build() {
        createLabelStyle();
        hard = true;
        network();
        start();
        built = true;
    }

    @Override
    public void resize(int width, int height) {
        uiStage.getViewport().update(width, height);
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
        if (built) return;


//        uiStage.setDebugAll(false);

        final Array<PuzzleElement.PartData> parts = new Array<PuzzleElement.PartData>(true, 7, PuzzleElement.PartData.class);
        parts.add(O_Shape);
        parts.add(I_Shape);
        parts.add(J_Shape);
        parts.add(L_Shape);
        parts.add(T_Shape);
        parts.add(S_Shape);
        parts.add(Z_Shape);


        final int col = gameConfig.cols;
        final int row = gameConfig.rows;
        float scale = Math.min(uiStage.getViewport().getScreenWidth(), uiStage.getViewport().getScreenHeight());
        final float maxGridWidth = scale / (0.5f + ((int) gameConfig.players));

        GameScreen.TILE_SIZE = (int) maxGridWidth / (col);
        final int fullHeight = GameScreen.TILE_SIZE * row;

        textureAtlas = new TextureAtlas("defaultSet/tileset.atlas");
        final TextureAtlas textAtlas = new TextureAtlas("textAtlas/text.atlas");
        final PartFactory partFactory = new PartFactory(10, textureAtlas);

        mainTable = new Table();
        mainTable.defaults().pad(10);

        final Array<Integer[][]> playerMatches = new Array<Integer[][]>(true, 5, Integer[][].class);

        final Label scoreLbl = new Label("Score " + score, goldSplashStyle);
        final Label linesLbl = new Label("Lines " + lines, goldSplashStyle);
        final Label levelLbl = new Label("Level " + level, goldSplashStyle);
        mainTable.setFillParent(true);
        mainTable.center();

//        mainTable.defaults().pad(15);

        final Pool<Row> rowPool = new Pool<Row>() {
            @Override
            protected Row newObject() {
                return new Row(col, row);
            }
        };

        final GridData data = new GridData(col, row) {
            @Override
            public void dataChange(int x, int y, byte value) {
                grid.enable(x, y, value);
                sendGridUpdate((byte) x, (byte) y, value);
            }
        };

        final GameStatue gameStatue = new GameStatue() {
            public int oldMode;
            public int newMode;
            public int mode = 0;

            @Override
            public int getMode() {
                return mode;
            }

            @Override
            public void setMode(int mode) {
                this.mode = mode;
            }

            @Override
            public void changeMode(int newMode) {
                this.newMode = newMode;
            }

            @Override
            public void modeChanged(int oldMode, int newMode) {
                this.oldMode = oldMode;
                this.newMode = newMode;
            }
        };

        /**
         * Player one
         */
        grid = new GridActor(data, textureAtlas, null) {

            @Override
            public void spectacularManeuver(Maneuver maneuver, int value, boolean b2b) {
                showMessage(maneuver.getName(),0);
                int points = maneuver.getPoints();
                score+=points;
                levelLbl.setText(String.valueOf(score));
            }

            @Override
            protected void matchEvent(Integer[][] rows) {
                playerMatches.add(rows);
                gameStatue.setMode(GameStatue.clear_cycle);

            }

            @Override
            protected void outOfBounds(int x, int y, byte[][] d, int rotation) {
                displayLoseSplash();
                gameOver = true;
            }

            @Override
            protected void outOfBounds(int x, int y, PuzzleElement.PartData pd, int rotation) {

                final Table table = new Table(skin);
                table.setFillParent(true);
                table.center();
                table.add(new Label("GameOver, Nigger", skin, "fancy")).colspan(2);
                table.row();
                TextButton retryBtn = new TextButton("Play again", skin);
                TextButton mainBtn = new TextButton("Main menu", skin);
                table.add(retryBtn);
                table.add(mainBtn);
                uiStage.addActor(table);
                gameOver = true;
                lose();
                retryBtn.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {

                        lines = 0;
                        score = 0;
                        level = 0;

                        scoreLbl.setText("Score " + score);
                        linesLbl.setText("Lines " + lines);
                        levelLbl.setText("Level " + level);

                        grid.reset();
                        table.remove();
                        gameOver = false;
                        uiStage.addAction(new Action() {
                            boolean gameOver = false;

                            @Override
                            public boolean act(float delta) {

                                switch (gameStatue.getMode()) {
                                    case GameStatue.falling_cycle:

                                        runFallCycle(delta, grid, gameStatue);

                                        break;
                                    case GameStatue.check_cycle:
                                        runCheckCycle(delta, grid, gameStatue, playerMatches, gameStatue);

                                        break;
                                    case GameStatue.clear_cycle:
                                        runClearCycle(delta, grid, gameStatue, playerMatches, levelLbl, linesLbl, scoreLbl, rowPool);
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
                                            if (grid.blocked()) {
                                                gameStatue.setMode(GameStatue.wait);

                                            } else
                                                gameStatue.setMode(GameStatue.falling_cycle);
                                            for (int i = 0; i < partDisplay.length; i++) {
                                                Tetromino p = partFactory.get(i);
                                                partDisplay[i].setPart(p);
                                            }

                                        }

                                        break;

                                }

                                return gameOver;
                            }
                        });
                    }
                });

                mainBtn.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {

                        ScreenManager manager = ScreenManager.getInstance();
                        ArrayMap<String, Class<?>> data = new ArrayMap<String, Class<?>>();
                        manager.loadScreen(data, new MainMenu());


                    }
                });
            }

            @Override
            public void fallCollision(final Tetromino tetromino, float x, float y) {
                if(y >= grid.getGridData().getRow()-2){
                    lose();
                    gameStatue.setMode(GameStatue.gameOver);
                    return;
                }

                if(dropped){

                    grid.setNewPiece(partFactory);
                    gameStatue.setMode(GameStatue.check_cycle);
                    grid.getGridData().fastAddShape(tetromino,false);
                    dropped = false;

                }else{

                    gameStatue.setMode(GameStatue.lock);

                }


            }

            @Override
            protected void createSplashText(Integer count) {
                final Image image = new Image(textAtlas.findRegion(String.valueOf(count))) {

                    float startingY = getY();
                    float endingY = grid.getHeight() - getHeight();
                    float time = 0, lifetime = 1f;

                    @Override
                    public void act(float delta) {

                        time += delta;
                        float progress = Math.min(1, time / lifetime);
                        float newY = Interpolation.circleOut.apply(startingY, endingY, progress);
                        setY(newY);

                        if (progress >= 1){
                            remove();
                            if(tetrisB2B) showMessage("B2B + 6",2);
                        }

                        super.act(delta);
                    }
                };
                image.setScale(1);
                image.setPosition(grid.getWidth() / 2 - image.getWidth() / 2, 0);//grid.getHeight()/2 - image.getHeight() / 2);
                grid.addActor(image);
                image.toFront();
                updateSendWholeGrid();
            }

            Array<BarSplash> barSplashes = new Array<BarSplash>();

            /**
             * Called anytime a bar is removed;
             * @param line single line removed...
             * @param lineGroups lines to indicate how many bars are being removed.
             * @param shapeRenderer a shared shape renderer belonging to grid.
             */

            @Override
            protected void createSplashBar(Integer line, final Integer[][][] lineGroups, ShapeRenderer shapeRenderer) {
//              animate off
                final BarSplash barSplash = new BarSplash(shapeRenderer) {
                    @Override
                    public boolean remove() {
                        barSplashes.removeValue(this, true);
                        if (barSplashes.size == 0) {

                            /*
                                Once all the lines have animated off. Animate row drop..
                             */

                            events.add(new LineAnimate(textureAtlas, lineGroups, grid, rowPool) {
                                @Override
                                public void complete(Integer[][][] lines) {

                                }
                            });

                            playerMatches.clear();
                            gameStatue.setMode(GameStatue.wait);
                        }
                        return super.remove();
                    }
                };

                barSplash.setWidth(getGridData().getCol() * GameScreen.TILE_SIZE);
                barSplash.setHeight(GameScreen.TILE_SIZE);
                barSplash.setPosition(getX(), getY() + (line * GameScreen.TILE_SIZE));
                getStage().addActor(barSplash);
                barSplashes.add(barSplash);


                gameStatue.setMode(GameStatue.animate_removal);
            }


            @Override
            protected void animateLineRemoval(Integer[][][] lines, GridActor gridActor) {

                events.add(new LineAnimate(textureAtlas, lines, gridActor, rowPool) {
                    @Override
                    public void complete(Integer[][][] lines) {

                    }
                });
                playerMatches.clear();
            }
        };
        grid.setSize(maxGridWidth, fullHeight);


        mainTable.add(scoreLbl).align(Align.left).height(25).row();
        mainTable.add(grid).size(maxGridWidth, fullHeight);
        grid.setOneColor(false);


        /**
         * Player one future shapes
         */

        partDisplay = new PartDisplay[7];
        final Table futureTable = new Table();

        heldPartDisplay = new PartDisplay(textureAtlas);
        futureTable.add(heldPartDisplay).size(150);
        futureTable.row();

//        futureTable.add(levelLbl).width(150).height(100);
//        futureTable.row();
//        futureTable.add(scoreLbl).width(150).height(100);
//        futureTable.row();
//        futureTable.add(linesLbl).width(150).height(100);
//        futureTable.row();

//        uiStage.addActor(infoTable);

        for (int i = 0; i < partDisplay.length; i++) {
            partDisplay[i] = new PartDisplay(textureAtlas).setPart(partFactory.get(i));
            futureTable.add(partDisplay[i]).size(75);
            futureTable.row();
        }
        futureTable.row();
        mainTable.add(futureTable);

        gameServer = GameServer.getGameServer();
        for (byte i = 0; i < gameConfig.players; i++) {
            byte index = (byte) (i + 1);
            if (index == gameConfig.getPlayerOrder()) continue;

            GridDisplay gridDisplay = networkPlayerGrids.get(index);
            if (gridDisplay == null) {

                gridDisplay = new GridDisplay(gameConfig.cols, gameConfig.rows, textureAtlas);
                networkPlayerGrids.put(index, gridDisplay);
                mainTable.add(gridDisplay).size(maxGridWidth, fullHeight);

            }
        }

        uiStage.addActor(mainTable);


        Table rightButtonTable = new Table();
        rightButtonTable.defaults().size(100, 100);
        rightButtonTable.setFillParent(true);
        rightButtonTable.bottom().right();

        Table leftButtonTable = new Table();
        leftButtonTable.defaults().size(100, 100);
        leftButtonTable.setFillParent(true);
        leftButtonTable.bottom().left();

        final TextButton leftButton = new TextButton("L", skin);
        final TextButton rightButton = new TextButton("R", skin);
        final TextButton rotateRight = new TextButton("RR", skin);
        final TextButton rotateLeft = new TextButton("RL", skin);
        final TextButton hardDrop = new TextButton("DROP", skin);
        final TextButton hold = new TextButton("HOLD", skin);

        rightButtonTable.add(hardDrop);
        rightButtonTable.add();
        leftButtonTable.add(hold);
        leftButtonTable.add();

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


        level = 0;
        leftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                final Tetromino tetromino = grid.getTetromino();
                if (!grid.absCollision(tetromino.getAbsX() - 1, tetromino.getAbsY(), tetromino.getShape())) {
                    grid.moveLeft();
                    sendPartUpdate();
                }
            }
        });

        rightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                final Tetromino tetromino = grid.getTetromino();
                if (!grid.absCollision(tetromino.getAbsX() + 1, tetromino.getAbsY(), tetromino.getShape())) {
                    grid.moveRight();
                    sendPartUpdate();
                }
            }
        });
        rotateLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gameStatue.getMode() == GameStatue.falling_cycle) {
                    grid.rotateLeft();
                    sendPartUpdate();
                }
            }
        });

        rotateRight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gameStatue.getMode() == GameStatue.falling_cycle || gameStatue.getMode() == GameStatue.lock) {
                    grid.rotateRight();
                    sendPartUpdate();
                }
            }
        });

        hold.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                partFactory.hold();
                partDisplay[0].setPart(partFactory.held());
                for (int i = 1; i < partDisplay.length; i++) {
                    Tetromino p = partFactory.get(i);
                    partDisplay[i].setPart(p);
                }
            }
        });
        hardDrop.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                float delta = Gdx.graphics.getDeltaTime();
                if (hard) {
                    while (true) {
                        dropped = true;
                        if (grid.moveDown(delta))
                            break;
                    }
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if (!hard) {
                    quickFall = true;
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (!hard) {
                    quickFall = false;
                }
            }

        });


        uiStage.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                Tetromino tetromino = grid.getTetromino();

                final boolean canControl = gameStatue.getMode() == GameStatue.falling_cycle || gameStatue.getMode() == GameStatue.lock;
                if(canControl){
                    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                        if (!grid.absCollision(tetromino.getAbsX() - 1, tetromino.getAbsY(), tetromino.getShape()))
                            grid.moveLeft();
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                        if (!grid.absCollision(tetromino.getAbsX() + 1, tetromino.getAbsY(), tetromino.getShape()))
                            grid.moveRight();
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                        grid.rotateRight();
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                        grid.rotateLeft();
                    }
                    if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                        grid.moveDown(delta);
                    }
                    if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                        grid.moveUp(delta);
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
                        while (true) {
                            hard = true;
                            if (grid.moveDown(delta))
                                break;
                        }
                    }
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
                    grid.setNewPiece(partFactory.iterateParts());
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
                    partFactory.hold();
                    heldPartDisplay.setPart(partFactory.held());
                    for (int i = 0; i < partDisplay.length; i++) {
                        Tetromino p = partFactory.get(i);
                        partDisplay[i].setPart(p);
                    }

                }

                if (quickFall) {
                    grid.moveDown(delta, GameScreen.TILE_SIZE);
                }

                return false;
            }
        });


        /**
         *        ---> Network Members <---
         */

//        size table
        playerGridsTable = new Table(skin);
        playerGridsTable.setWidth(maxGridWidth);
        mainTable.add(playerGridsTable);

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
        tetras = new Action() {
            boolean gameOver = false;
            boolean clearedLined = false;
            @Override
            public boolean act(float delta) {
                switch (gameStatue.getMode()) {
                    case GameStatue.falling_cycle:

                        runFallCycle(delta, grid, gameStatue);
                        sendMoveUpdate(delta, 0.3f);
                        break;
                    case GameStatue.check_cycle:
                        runCheckCycle(delta, grid, gameStatue, playerMatches, gameStatue);

                        break;
                    case GameStatue.clear_cycle:
                        clearedLined = true;
                        runClearCycle(delta, grid, gameStatue, playerMatches, levelLbl, linesLbl, scoreLbl, rowPool);
                        playerMatches.clear();

                        break;
                    case GameStatue.wait:
                        runWaitCycle(delta, grid, gameStatue);
                        break;
                    case GameStatue.gameOver:
                        gameOver = true;
                        break;
                    case GameStatue.newCycle:

                        /**
                         * Check for Combo and b2bs
                         */
                        if(clearedLined){
                            ++combo;
                            if(combo > 0){
                                scoreLbl.setText(String.valueOf(combo*50));
                                showMessage("Combo bonus "+combo*50,0);
                                showMessage(combo+"+",1);
                            }

                            clearedLined = false;
                        }else combo = 0;

                        if (events.size != 0) gameStatue.setMode(GameStatue.wait);

                        else {
                            grid.setNewPiece(partFactory);

                            /**
                             *  Network aspect
                             */
                            sendPartUpdate();

                            if (grid.blocked()) {
                                gameStatue.setMode(GameStatue.wait);

                            } else
                                gameStatue.setMode(GameStatue.falling_cycle);
                            for (int i = 0; i < partDisplay.length; i++) {
                                Tetromino p = partFactory.get(i);
                                partDisplay[i].setPart(p);
                            }
                        }

                        break;
                    case GameStatue.animate_removal:
                        /**
                         * this cycle is virtually just a stop so animations can play out
                         */

                        break;
                    case GameStatue.lock:
                        landTime+= delta;
                        if(landTime >= landDelay){
                            landTime-=landDelay;
                            float ly = grid.getTetromino().getAbsY() - 1;
                            if(!grid.absCollision(grid.getTetromino().getAbsX(),ly,grid.getTetromino().getShape())){
                                grid.setLanded(false);
                                gameStatue.setMode(GameStatue.falling_cycle);

                            } else {

//                              Check for special maneuvers
                                boolean collision = tSpin();
                                if(collision){
                                    grid.maneuverLocation();
                                    grid.checkTSpin();
                                }
                                gameStatue.setMode(GameStatue.check_cycle);
                                grid.getGridData().fastAddShape(grid.getTetromino(),false);
                                landTime = 0;
                            }

                        }
                        break;
                }


                return gameOver;
            }
        };

        if (GameServer.getGameServer().isServer()) {
            final Table startTable = new Table(skin);
            startTable.setFillParent(true);
            startTable.center();

            TextButton startButton = new TextButton("Begin", skin);
            startTable.add(startButton);
            startButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!begin) {
                        send(new CommandRequest().set("state", "start", "3"));
                        uiStage.addAction(tetras);
                        begin = true;
                        startTable.remove();
                    }

                }
            });


            uiStage.addActor(startTable);

        }

//      1.)

//      player one action, this action runs game
//        uiStage.addAction(tetras);

        if(GameServer.getGameServer().isServer())
        for (int i = 0; i < comboTest.length; i++){
            for (int j = 0; j < comboTest[i].length; j++){
//                grid.getGridData().data().get(i).getData().set(j,tetrisTest[i][j]);
            }
        }

//        for (int i = 0; i < 10; i++)
//            grid.getGridData().getRow(i).fill((byte) 1, MathUtils.random(0,col-1));
        gameStatue.setMode(GameStatue.newCycle);

//        grid.getBarrier().addBarrierValue(5);
        grid.updateGrid();
        if (!GameServer.getGameServer().isServer()) {
            try {
                GameServer.getGameServer().createClient().update(GameServer.TIMEOUT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showMessage(String msg, int offset) {
        Label label = labelPool.obtain();
        label.setText(msg);
        label.setWidth(TILE_SIZE*3);
        label.setStyle(goldSplashStyle);
        label.toFront();
        label.setY( grid.getHeight() - (GameScreen.TILE_SIZE * (3+offset)));
        label.setX(-label.getWidth() );

        grid.addActor(label);


    }

    Pool<Label> labelPool = new Pool<Label>(){

        @Override
        protected Label newObject() {
            return new Label("",skin){

                float lifeTime = 0.5f,time, delayTime, delay = 1f;
                float startX = 0;
                float endX = 0;

                boolean started = false;
                public void act(float delta) {
                    time+=delta;
                    float progress = time / lifeTime;
                    if(!started){

                        startX = -getWidth() - grid.getWidth();
                        endX =  -getWidth();
                        started = true;
                    }

                    if(progress >= 1f){
                        delayTime+=delta;
                        if(delayTime >= delay){
                            labelPool.free(this);
                            remove();
                        }
                    }else{
                        setX(Interpolation.circleOut.apply(startX,endX,progress));
                    }

                }

                @Override
                public boolean remove() {
                    lifeTime = 0.5f;
                    time = 0;
                    delayTime = 0;
                    delay = 1f;
                    startX = 0;
                    endX = 0;
                    started = false;
                    return super.remove();
                }
            };
        }
    };

    private int comboPenalty(int combo) {
        switch(combo){
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 2;
            case 5:
                return 3;
            case 6:
                return 3;
            case 7:
                return 4;
            default:
                return 4;
        }

    }

    private boolean tSpin() {
        boolean collision = false;
        if(grid.getTetromino().getData() == PuzzleElement.PartData.T_Shape){
            Tetromino tetromino = grid.getTetromino();
            collision = grid.absCollision(tetromino.getAbsX(), tetromino.getAbsY() + 1, tetromino.getShape());

        }
        return collision;
    }

    private void displayLoseSplash() {

    }

    private void updateSendWholeGrid() {
        final int row = grid.getGridData().getRow();
        final int col = grid.getGridData().getCol();

        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                sendGridUpdate((byte) x, (byte) y, grid.getGridData().safeGetDataValueAt(x, y));
            }
        }
    }

    private void sendGridUpdate(byte x, byte y, byte value) {
        final Pip pip = pipPool.obtain();
        pip.x = x;
        pip.y = y;
        pip.value = value;
        pip.id = gameConfig.playerOrder;
        send(pip);
        pipPool.free(pip);
    }

    private void sendMoveUpdate(float delta, float delay) {
        updateTetrominoTime += delta;
        if (updateTetrominoTime >= delay) {
            final PartUpdate part = pieceUpdatePool.obtain();
            part.x = (byte) grid.getTetromino().getX();
            part.y = (byte) grid.getTetromino().getY();
            part.part = (byte) grid.getTetromino().getIndex();
            part.rotation = (byte) grid.getTetromino().getRotation();
            part.id = gameConfig.playerOrder;
            send(part);
            pieceUpdatePool.free(part);
            updateTetrominoTime -= delay;
        }
    }

    private void sendPartUpdate() {
        final PartUpdate part = pieceUpdatePool.obtain();
        part.x = (byte) grid.getTetromino().getX();
        part.y = (byte) grid.getTetromino().getY();
        part.part = (byte) grid.getTetromino().getIndex();
        part.rotation = (byte) grid.getTetromino().getRotation();
        part.id = gameConfig.playerOrder;
        send(part);
        pieceUpdatePool.free(part);
    }

    private void runWaitCycle(float delta, GridActor grid, GameStatue gameStatue) {
        if (events.size == 0)
            gameStatue.setMode(GameStatue.newCycle);

        for (int i = 0; i < events.size; i++) {
            GameEvent gameEvent = events.get(i);

            if (gameEvent.isDone())
                events.removeIndex(i);
            else
                gameEvent.act(delta);
        }

    }

    private void runClearCycle(float delta, GridActor grid, GameStatue gameStatue, Array<Integer[][]> matches, Label levelLbl, Label linesLbl, Label scoreLbl, Pool<Row> rowPool) {

        int penaltyLines = 0;
        int lines = 0;
        int lineValue = 0;
        boolean hasTetras = false;

        for (Integer[][] rows : matches) {
            for (Integer[] row : rows) {
                for (Integer roeIndex : row) {
                    lines += rows.length;
                }
            }
        }

        switch (lines) {
            case 1:
                lineValue = 40;
                penaltyLines += 0;
                break;
            case 2:
                lineValue = 100;
                penaltyLines += 1;
                break;
            case 3:
                lineValue = 300;
                penaltyLines +=2;
                break;
            case 4:
                lineValue = 1200;
                penaltyLines = (hadTetris)? penaltyLines + 6 : penaltyLines + 4;
                if(hadTetris) tetrisB2B = true;
                hadTetris = true;
                hasTetras = true;
                break;
        }

        score += (lineValue * (level + 1));
        scoreLbl.setText("Score " + score);
        linesLbl.setText("Lines " + this.lines + lines);
        levelLbl.setText("Level " + level);

        if(!hasTetras) {
            tetrisB2B = false;
            hadTetris = false;
        }

        if (lines > 0) {
            grid.removeRow(rowPool, matches.toArray());
            gameStatue.setMode(GameStatue.animate_removal);
            if(grid.cleanField()){
                cleanField();
                penaltyLines+=10;
            }

            makeHit(penaltyLines +  comboPenalty(combo));
        }

    }

    private void cleanField() {
        showMessage("Cleared Matrix",3);
    }

    private void runCheckCycle(float delta, GridActor grid, GameStatue npcGamesStatus, Array<Integer[][]> matches, GameStatue gameStatue) {
        grid.checkMatches(delta);

        if (matches.size > 0){
            npcGamesStatus.setMode(GameStatue.clear_cycle);
        }
        else{
            npcGamesStatus.setMode(GameStatue.wait);
            combo = 0;
        }

    }

    private void runFallCycle(float delta, GridActor grid, GameStatue state) {
        boolean collision = grid.fall(delta);
        if (collision) state.changeMode(GameStatue.check_cycle);

    }

    @Override
    public void restart() {

    }

    @Override
    public void quite() {

    }

    @Override
    public void takeHit(int hitValue) {
//        grid.getBarrier().addBarrierValue(hitValue);
        grid.addPenaltyLines(hitValue);
    }

    @Override
    public void makeHit(int hitValue) {

        Hit hit = hitPool.obtain();
        hit.hitValue = (byte) hitValue;
        send(hit);
        hitPool.free(hit);

    }

    @Override
    public void win() {

    }

    @Override
    public void lose() {
        final CommandRequest commandRequest = new CommandRequest().set("stage","lose",String.valueOf(gameConfig.playerOrder));
        send(commandRequest);
    }

    private Pool<Hit> hitPool = new Pool<Hit>() {
        @Override
        protected Hit newObject() {
            return new Hit();
        }
    };

    private Pool<Pip> pipPool = new Pool<Pip>() {
        @Override
        protected Pip newObject() {
            return new Pip();
        }
    };

    private Pool<PartUpdate> pieceUpdatePool = new Pool<PartUpdate>() {
        @Override
        protected PartUpdate newObject() {
            return new PartUpdate();
        }
    };

    private void send(Object object) {
        if (GameServer.getGameServer().isServer())
            GameServer.getGameServer().getServer().sendToAllTCP(object);
        else
            GameServer.getGameServer().createClient().sendTCP(object);

    }


}
