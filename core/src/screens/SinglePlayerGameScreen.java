package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Pool;

import java.util.HashMap;

import engine.FontUtils;
import engine.GameEvent;
import engine.GameStatue;
import engine.GridData;
import engine.InGameScreen;
import engine.LineAnimate;
import engine.Row;
import managers.PartFactory;
import managers.ScreenManager;
import managers.SimpleDirectionGestureDetector;
import network.GridDisplay;
import network.Hit;
import network.PartUpdate;
import network.Pip;
import objects.BarSplash;
import objects.CommandRequest;
import objects.GridActor;
import objects.Maneuver;
import objects.PuzzleElement;
import objects.Tetromino;

public class SinglePlayerGameScreen extends GameScreen implements InGameScreen {

    private final byte[][] bumpers = new byte[][]{
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {5,2,0,0,0,0,0,0,2,5},
            {5,2,0,0,0,0,0,0,2,5},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {5,2,0,0,0,0,0,0,2,5},
            {5,2,0,0,0,0,0,0,2,5},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {5,2,0,0,0,0,0,0,2,5},
            {5,2,0,0,0,0,0,0,2,5},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {5,2,0,0,0,0,0,0,2,5},
            {5,2,0,0,0,0,0,0,2,5},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {5,2,0,0,0,0,0,0,2,5},
            {5,2,0,0,0,0,0,0,2,5},

    };

    private final byte[][] junkTest = new byte[][]{
            {1,1,1,1,0,1,0,0,1,1},
            {1,1,1,1,0,1,0,0,1,1},
            {1,1,1,0,0,0,0,0,0,1},
            {1,1,1,1,0,1,0,0,0,1},
            {1,1,1,1,1,0,0,0,1,1},
            {1,1,1,1,1,1,0,0,1,1},
            {1,1,1,0,0,0,0,1,1},
            {1,1,0,0,0,0,0,0,1,1},

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
    private final int rows;
    private final int cols;
    private final int linesPerLevel;

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

    private Label.LabelStyle goldSplashStyle;
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
//    private PartDisplay[] partDisplay;
//    private PartDisplay heldPartDisplay;
    private Action tetras;
    private Maneuver lastManeuver;
    private boolean hadManeuver;
    private ProgressBar levelBar;

//    private Label levelLbl;
//    private Label linesLbl;
//    private Label scoreLbl;
    private boolean usedHeld;
    private int pointsPerLevel;
    private boolean navigated;

    public SinglePlayerGameScreen(int cols, int rows, int linesPerLevel) {
        this.cols = cols;
        this.rows = rows;
        this.linesPerLevel = linesPerLevel;
        this.pointsPerLevel = 4800;
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
//        uiStage.setDebugAll(true);
    }

    @Override
    public void show() {

    }

    private void createLabelStyle() {
        goldSplashStyle = new Label.LabelStyle();
        goldSplashStyle.font = FontUtils.generateFont(20, 1, 1, 1, Color.GOLD, Color.GOLDENROD, Color.BLACK,Color.WHITE, "font/zephyrea.ttf");
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

        GameScreen.TILE_SIZE = 25;

        textureAtlas = new TextureAtlas("defaultSet/tileset.atlas");
        final TextureAtlas textAtlas = new TextureAtlas("textAtlas/text.atlas");
        final PartFactory partFactory = new PartFactory(10, textureAtlas);

        mainTable = new Table(skin);
        mainTable.defaults().pad(0);

        final Array<Integer[][]> playerMatches = new Array<Integer[][]>(true, 5, Integer[][].class);
//        scoreLbl = new Label( String.valueOf(score), goldSplashStyle);
//        linesLbl = new Label("Lines " + lines, goldSplashStyle);
//        levelLbl = new Label("Level " + level, goldSplashStyle);
        mainTable.setFillParent(true);
        mainTable.center();


//        mainTable.defaults().pad(15);

        final Pool<Row> rowPool = new Pool<Row>() {
            @Override
            protected Row newObject() {
                return new Row(cols, rows);
            }
        };

        final GridData data = new GridData(cols, rows) {
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
        grid = new GridActor(data, textureAtlas, skin,null) {

            @Override
            public void spectacularManeuver(Maneuver maneuver, int value, boolean b2b) {

                if(lastManeuver != null)
                    b2b = lastManeuver.equals(maneuver);

                showMessage((b2b)?"Another":""+maneuver.getName());
                if(b2b)  showMessage("+ "+maneuver.getPoints() / 2);

                score+=maneuver.getPoints();
                if(b2b) score += maneuver.getPoints() / 2;
                grid.setScore(String.valueOf(score));

                lastManeuver = maneuver;
                hadManeuver = true;
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


                        updateStats();

                        grid.reset();
                        table.remove();
                        gameOver = false;

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
        grid.setFrame(skin.getDrawable("gridFrame"));
        grid.setFontStyle(goldSplashStyle);
        grid.setFutures(partFactory);
        grid.setSize(cols * GameScreen.TILE_SIZE, rows * GameScreen.TILE_SIZE);

//        mainTable.add( new Label("Score ",goldSplashStyle)).align(Align.bottomLeft);
//        mainTable.add();
//        mainTable.add(scoreLbl).align(Align.bottomRight).height(25).row();

        levelBar = new ProgressBar(0,this.linesPerLevel,1f,true,skin,"game_meter");
        mainTable.add(levelBar).height((rows-2) * GameScreen.TILE_SIZE).width(8);
        mainTable.add(grid).colspan(2).size(cols * GameScreen.TILE_SIZE, rows * GameScreen.TILE_SIZE);
        grid.setOneColor(false);


        /**
         * Player one future shapes
         */

//        partDisplay = new PartDisplay[3];
//
//        final Table futureTable = new Table(skin);
//        heldPartDisplay = new PartDisplay(textureAtlas);
//        heldPartDisplay.setBackGround(skin.getDrawable("textField"));
//        futureTable.add(heldPartDisplay).size(150);
//        futureTable.row();
//
//        for (int i = 0; i < partDisplay.length; i++) {
//            partDisplay[i] = new PartDisplay(textureAtlas).setPart(partFactory.get(i));
//            partDisplay[i].setBackGround(skin.getDrawable("textField"));
//            futureTable.add(partDisplay[i]).size(75);
//            futureTable.row();
//        }
//        futureTable.row();
//        mainTable.add(futureTable);

        /**
         * Extra table
         */

        final float extraPanels = 3;
        float widthGrid = cols * GameScreen.TILE_SIZE;
        playerGridsTable = new Table(skin);

        for(int i = 0; i < extraPanels; i++){
            final Table table = new Table(skin);
            Actor actor = new Actor();
            table.add( actor ).width( widthGrid / extraPanels ).height( rows * GameScreen.TILE_SIZE );
            playerGridsTable.add(table).width( widthGrid / extraPanels );
        }

        mainTable.add(playerGridsTable).width(widthGrid);
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

        final Touchpad translationPad =  new Touchpad(5, skin);
        translationPad.getListeners().insert(0, new InputListener() {

            private Vector2 tmpVec = new Vector2();

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (translationPad.isTouched()) return false;
                restrictAlongX(event);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                restrictAlongX(event);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                restrictAlongX(event);
            }

            private void restrictAlongX(InputEvent inputEvent) {
                // convert local centerY to the stage coordinate system
                tmpVec.set(0f, translationPad.getHeight() / 2);
                translationPad.localToStageCoordinates(tmpVec);

                // set stageY to the touchpad centerY
                inputEvent.setStageY(tmpVec.y);
            }
        });
        leftButtonTable.add(translationPad).size(100);

        final Touchpad rotationPad =  new Touchpad(5, skin);
        rotationPad.getListeners().insert(0, new InputListener() {

            private Vector2 tmpVec = new Vector2();

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (rotationPad.isTouched()) return false;
                restrictAlongX(event);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                restrictAlongX(event);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                restrictAlongX(event);
            }

            private void restrictAlongX(InputEvent inputEvent) {
                // convert local centerY to the stage coordinate system
                tmpVec.set(0f, rotationPad.getHeight() / 2);
                rotationPad.localToStageCoordinates(tmpVec);

                // set stageY to the touchpad centerY
                inputEvent.setStageY(tmpVec.y);
            }
        });


        rightButtonTable.add(hardDrop);
        rightButtonTable.add(rotationPad).size(100);
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


        level = 0;
        leftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                final Tetromino tetromino = grid.getTetromino();
                moveLeft(tetromino);
            }
        });

        rightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                final Tetromino tetromino = grid.getTetromino();
                moveRight(tetromino);
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

            hold(partFactory);

//                partDisplay[0].setPart(partFactory.held());

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
            float translationTimer = 0.1f,rotationTimer = 0.5f,translationTime,rotationTime;

            @Override
            public boolean act(float delta) {


                Tetromino tetromino = grid.getTetromino();

                final boolean canControl = gameStatue.getMode() == GameStatue.falling_cycle || gameStatue.getMode() == GameStatue.lock;
                if(canControl){

                    float translationValue = translationPad.getKnobPercentX();
                    float rotationValue    = rotationPad.getKnobPercentX();

                    /*
                        Translation touch pad
                     */

                    if(translationValue > 0.5){
                        if(translationTime >= translationTimer){
                            if (!grid.absCollision(tetromino.getAbsX() + 1, tetromino.getAbsY(), tetromino.getShape()))
                                grid.moveRight();
                            translationTime -= translationTimer;
                        }
                        translationTime+=delta;
                    }else if(translationValue < -0.5){
                        if(translationTime >= translationTimer){
                            if (!grid.absCollision(tetromino.getAbsX() - 1, tetromino.getAbsY(), tetromino.getShape()))
                                grid.moveLeft();
                            translationTime -= translationTimer;
                        }
                        translationTime+=delta;
                    }else{
                        translationTime = translationTimer;
                    }

                    /*
                        Rotation touch pad
                     */

                    if(rotationValue > 0.3){
                        if(rotationTime >= rotationTimer){
                            grid.rotateRight();
                            rotationTime -= rotationTimer;
                        }
                        rotationTime+=delta;
                    }else if(rotationValue < -0.3){
                        if(rotationTime >= rotationTimer){
                            grid.rotateLeft();
                            rotationTime -= rotationTimer;
                        }
                        rotationTime+=delta;
                    }else{
                        rotationTime = rotationTimer;
                    }





                    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                        moveLeft(grid.getTetromino());
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                        moveRight(grid.getTetromino());
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
                    if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
                        grid.addPenaltyLines(1);
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
                        while (true) {
                            hard = true;
                            if (grid.moveDown(delta)){
                                landTime = landDelay;
                                grid.setLanded(true);
                                gameStatue.setMode(GameStatue.check_cycle);
                                grid.getGridData().fastAddShape(tetromino,false);
                                grid.updateHeights();
                                break;
                            }
                        }
                    }
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
                    grid.setNewPiece(partFactory.iterateParts());
                    grid.getAllMoves();
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
                    hold(partFactory);
                }

                if (quickFall) {
                    grid.moveDown(delta, GameScreen.TILE_SIZE);
                }

                return false;
            }
        });

//        ---> hud <---
        final ImageButton rotateRightBtn = new ImageButton(skin,"rotate_right");
//        final ImageButton rotateLeftBtn  = new ImageButton(skin,"rotate_left");
//        final ImageButton moveRightBtn   = new ImageButton(skin,"move_right");
//        final ImageButton moveLeftBtn    = new ImageButton(skin,"move_left");
//        final ImageButton hardDropBtn    = new ImageButton(skin,"harddrop");
//        final ImageButton softDropBtn    = new ImageButton(skin,"softdrop");

        rotateRightBtn.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                navigated = true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                navigated = false;
                super.touchUp(event, x, y, pointer, button);
            }
        });



        /**
         *        ---> Network Members <---
         */

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
        final Label fpsLabel = new Label("",skin,"fancy");
        fpsLabel.setWidth(100);
        uiStage.addActor(fpsLabel);
        tetras = new Action() {



            final Vector2 unprojected = new Vector2();
            boolean gameOver = false;
            boolean clearedLined = false;
            boolean showingHud;

            Vector2 getCoord() {
                unprojected.set(Gdx.input.getX(),Gdx.input.getY());
                unprojected.set(uiStage.getViewport().unproject(unprojected));
                return unprojected;
            }

            @Override
            public boolean act(float delta) {

                if(Gdx.input.justTouched() && !navigated){

                    if(showingHud){
                        getCoord();
                        rotateRightBtn.setPosition(unprojected.x,unprojected.y);
                        rotateRightBtn.moveBy(GameScreen.TILE_SIZE*3,0);
                    }else{
                        showingHud = true;
                        uiStage.addActor(rotateRightBtn);
                        getCoord();
                        rotateRightBtn.setPosition(unprojected.x,unprojected.y);
                        rotateRightBtn.moveBy(GameScreen.TILE_SIZE*3,0);
                    }

                }

                fpsLabel.setText("FPS:"+Gdx.graphics.getFramesPerSecond());
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
                        runClearCycle(delta, grid, gameStatue, playerMatches, null, null, null, rowPool);
                        playerMatches.clear();

                        break;
                    case GameStatue.wait:
                        runWaitCycle(delta, grid, gameStatue);
                        break;
                    case GameStatue.gameOver:
                        gameOver = true;
                        break;
                    case GameStatue.newCycle:

                        usedHeld = false;
                        /**
                         * Check for Combo and b2bs
                         */

                        if(clearedLined){

                            if(!hadManeuver)
                                lastManeuver = null;

                            if(combo > 0){
                                score +=(combo*50);
                                grid.setScore(String.valueOf(score));
                                showMessage("Combo "+combo);
                            }
                            combo++;

                            clearedLined = false;
                        }else{
                            lastManeuver = null;
                            combo = 0;
                        }
                        grid.setComboStep(combo);
                        hadManeuver = false;
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
                            grid.setFutures(partFactory);
//                            for (int i = 0; i < partDisplay.length; i++) {
//                                Tetromino p = partFactory.get(i);
//                                partDisplay[i].setPart(p);
//                            }
                        }
                        grid.getAllMoves();
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



//      1.)

//      player one action, this action runs game
//        uiStage.addAction(tetras);


        byte[][] test = null;//bumpers;

        if(test != null){
            for (int i = 0; i < test.length; i++){
                for (int j = 0; j < test[i].length; j++){
                    grid.getGridData().setDataValueAt(j,i,test[i][j]);
                }
            }
        }



        gameStatue.setMode(GameStatue.newCycle);
        grid.updateGrid();
        grid.setTouchable(Touchable.enabled);

        grid.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                int touchedX = (int) (x / GameScreen.TILE_SIZE);
                int touchedY = (int) (y / GameScreen.TILE_SIZE);
                grid.setTargetCell(touchedX,touchedY);


                super.touchUp(event, x, y, pointer, button);
            }

        });

        Gdx.input.setInputProcessor( new InputMultiplexer(uiStage,new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener(){

            @Override
            public void onLeft() {
                System.out.println("Left");
            }

            @Override
            public void onRight() {
                System.out.println("Right");
            }

            @Override
            public void onUp() {
                System.out.println("Up");
            }

            @Override
            public void onDown() {
                System.out.println("Down");
            }

        })));



    }

    private boolean moveRight(Tetromino tetromino) {
        final int rotation = tetromino.getRotation();
        if (!grid.collision(tetromino.getAbsX() + 1, tetromino.getAbsY(),rotation, tetromino.getData())) {
            grid.moveRight();
            sendPartUpdate();
            return true;
        }
        return false;
    }

    private boolean moveLeft(Tetromino tetromino) {
        final int rotation = tetromino.getRotation();
        if (!grid.collision(tetromino.getAbsX() - 1, tetromino.getAbsY(),rotation, tetromino.getData())) {
            grid.moveLeft();
            sendPartUpdate();
            return true;
        }
        return false;
    }

    private void hold(PartFactory partFactory) {
        if(usedHeld) return;

        Tetromino heldTetromino = grid.heldTetromino();
//              hold piece...
        if(heldTetromino == null){
            Tetromino tetromino = grid.getTetromino();
            if(tetromino!=null) {
                grid.setHeldTetromino(tetromino);
                grid.setNewPiece(partFactory.newPiece());
                grid.resetPosition();
            }

        } else{

//                    use held piece.
            Tetromino tetromino = grid.getTetromino();
            if(tetromino!=null) {
                partFactory.setFirst(tetromino);
                grid.setTetromino(grid.heldTetromino());
                grid.setHeldTetromino(null);
                usedHeld = true;
                grid.resetPosition();
            }

            grid.setFutures(partFactory);

        }
    }

    private void updateStats() {
        grid.setScore(String.valueOf(score));
//        linesLbl.setText("Lines " + lines);
//        levelLbl.setText("Level " + level);

    }

    Array<Label> messages = new Array<Label>();
    private void showMessage(String msg) {

        Label label = labelPool.obtain();
        messages.add(label);

        label.setText(msg);
        label.setSize(TILE_SIZE*5,TILE_SIZE);

//        label.setStyle(goldSplashStyle);

        label.setY( grid.getHeight() - (GameScreen.TILE_SIZE * (3+messages.size)));
        label.setX(-label.getWidth() );

        grid.addActor(label);
        label.toFront();



    }

    Pool<Label> labelPool = new Pool<Label>(){

        @Override
        protected Label newObject() {
            return new Label("",skin,"left_message"){

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
                            messages.removeValue(this,false);
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

    }

    private void sendMoveUpdate(float delta, float delay) {

    }

    private void sendPartUpdate() {

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

                boolean b2b = false;
                if(lastManeuver != null)
                    b2b = lastManeuver.equals(Maneuver.Tetris);

                grid.spectacularManeuver(Maneuver.Tetris,0,b2b);
                penaltyLines = (b2b)? penaltyLines + 6 : penaltyLines + 4;
                break;
        }

        score += (lineValue * (level + 1));
        grid.setScore(String.valueOf(score));
//        linesLbl.setText("Lines " + this.lines + lines);
//        levelLbl.setText("Level " + level);


        if (lines > 0) {
            grid.removeRow(rowPool, matches.toArray());
            gameStatue.setMode(GameStatue.animate_removal);
            if(grid.cleanField()){
                grid.spectacularManeuver(Maneuver.ClearAll,0,false);
                penaltyLines+=10;
            }

            this.lines += lines;
            if(this.lines > linesPerLevel){
                this.lines = 0;
                showMessage("Level Up "+level);
                showMessage(String.valueOf(pointsPerLevel*level));
                level += 1;
            }
            updateStats();
            levelBar.setValue(level);

            makeHit(penaltyLines +  comboPenalty(combo));
        }

        grid.setLines(this.lines,linesPerLevel);

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


    public void takeHit(int hitValue) {
//        grid.getBarrier().addBarrierValue(hitValue);
        grid.addPenaltyLines(hitValue);
    }


    public void makeHit(int hitValue) {

        Hit hit = hitPool.obtain();
        hit.hitValue = (byte) hitValue;
        send(hit);
        hitPool.free(hit);

    }

    public void win() {

    }

    public void lose() {

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

    }



}
