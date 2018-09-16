package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Pool;

import org.apache.commons.lang.math.RandomUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import engine.FontUtils;
import engine.GridData;
import engine.GridLocation;
import engine.Row;

import engine.genericAI.Algorithm;
import engine.genericAI.Genome;
import managers.PartFactory;
import screens.GameScreen;

public abstract class GridActor extends Group {
    private static ObjectMap<Integer, Integer> pick_tile = new ObjectMap<Integer, Integer>();

    static {
        pick_tile.put(0, 47);
        pick_tile.put(1, 35);
        pick_tile.put(2, 47);
        pick_tile.put(3, 19);
        pick_tile.put(4, 7);
        pick_tile.put(5, 36);
        pick_tile.put(6, 19);
        pick_tile.put(7, 19);
        pick_tile.put(8, 26);
        pick_tile.put(9, 35);
        pick_tile.put(10, 47);
        pick_tile.put(11, 35);
        pick_tile.put(12, 7);
        pick_tile.put(13, 26);
        pick_tile.put(14, 18);
        pick_tile.put(15, 18);
        pick_tile.put(16, 24);
        pick_tile.put(17, 24);
        pick_tile.put(18, 3);
        pick_tile.put(19, 16);
        pick_tile.put(20, 24);
        pick_tile.put(21, 24);
        pick_tile.put(22, 21);
        pick_tile.put(23, 16);
        pick_tile.put(24, 25);
        pick_tile.put(25, 25);
        pick_tile.put(26, 22);
        pick_tile.put(27, 43);
        pick_tile.put(28, 25);
        pick_tile.put(29, 25);
        pick_tile.put(30, 44);
        pick_tile.put(31, 17);
        pick_tile.put(32, 47);
        pick_tile.put(33, 35);
        pick_tile.put(34, 47);
        pick_tile.put(35, 19);
        pick_tile.put(36, 7);
        pick_tile.put(37, 36);
        pick_tile.put(38, 19);
        pick_tile.put(39, 19);
        pick_tile.put(40, 26);
        pick_tile.put(41, 35);
        pick_tile.put(42, 23);
        pick_tile.put(43, 35);
        pick_tile.put(44, 7);
        pick_tile.put(45, 36);
        pick_tile.put(46, 7);
        pick_tile.put(47, 18);
        pick_tile.put(48, 24);
        pick_tile.put(49, 24);
        pick_tile.put(50, 3);
        pick_tile.put(51, 16);
        pick_tile.put(52, 4);
        pick_tile.put(53, 24);
        pick_tile.put(54, 4);
        pick_tile.put(55, 16);
        pick_tile.put(56, 25);
        pick_tile.put(57, 25);
        pick_tile.put(58, 3);
        pick_tile.put(59, 43);
        pick_tile.put(60, 25);
        pick_tile.put(61, 25);
        pick_tile.put(62, 44);
        pick_tile.put(63, 17);
        pick_tile.put(64, 3);
        pick_tile.put(65, 38);
        pick_tile.put(66, 11);
        pick_tile.put(67, 11);
        pick_tile.put(68, 8);
        pick_tile.put(69, 37);
        pick_tile.put(70, 11);
        pick_tile.put(71, 11);
        pick_tile.put(72, 7);
        pick_tile.put(73, 7);
        pick_tile.put(74, 15);
        pick_tile.put(75, 28);
        pick_tile.put(76, 8);
        pick_tile.put(77, 7);
        pick_tile.put(78, 35);
        pick_tile.put(79, 35);
        pick_tile.put(80, 5);
        pick_tile.put(81, 5);
        pick_tile.put(82, 13);
        pick_tile.put(83, 36);
        pick_tile.put(84, 5);
        pick_tile.put(85, 21);
        pick_tile.put(86, 13);
        pick_tile.put(87, 36);
        pick_tile.put(88, 6);
        pick_tile.put(89, 6);
        pick_tile.put(90, 14);
        pick_tile.put(91, 39);
        pick_tile.put(92, 6);
        pick_tile.put(93, 6);
        pick_tile.put(94, 37);
        pick_tile.put(95, 31);
        pick_tile.put(96, 3);
        pick_tile.put(97, 38);
        pick_tile.put(98, 11);
        pick_tile.put(99, 11);
        pick_tile.put(100, 8);
        pick_tile.put(101, 37);
        pick_tile.put(102, 11);
        pick_tile.put(103, 11);
        pick_tile.put(104, 7);
        pick_tile.put(105, 7);
        pick_tile.put(106, 15);
        pick_tile.put(107, 15);
        pick_tile.put(108, 8);
        pick_tile.put(109, 7);
        pick_tile.put(110, 35);
        pick_tile.put(111, 35);
        pick_tile.put(112, 0);
        pick_tile.put(113, 0);
        pick_tile.put(114, 42);
        pick_tile.put(115, 8);
        pick_tile.put(116, 0);
        pick_tile.put(117, 0);
        pick_tile.put(118, 42);
        pick_tile.put(119, 8);
        pick_tile.put(120, 38);
        pick_tile.put(121, 38);
        pick_tile.put(122, 47);
        pick_tile.put(123, 28);
        pick_tile.put(124, 38);
        pick_tile.put(125, 38);
        pick_tile.put(126, 4);
        pick_tile.put(127, 33);
        pick_tile.put(128, 47);
        pick_tile.put(129, 35);
        pick_tile.put(130, 47);
        pick_tile.put(131, 35);
        pick_tile.put(132, 7);
        pick_tile.put(133, 27);
        pick_tile.put(134, 19);
        pick_tile.put(135, 19);
        pick_tile.put(136, 26);
        pick_tile.put(137, 35);
        pick_tile.put(138, 23);
        pick_tile.put(139, 35);
        pick_tile.put(140, 26);
        pick_tile.put(141, 26);
        pick_tile.put(142, 18);
        pick_tile.put(143, 18);
        pick_tile.put(144, 3);
        pick_tile.put(145, 19);
        pick_tile.put(146, 3);
        pick_tile.put(147, 16);
        pick_tile.put(148, 4);
        pick_tile.put(149, 24);
        pick_tile.put(150, 4);
        pick_tile.put(151, 16);
        pick_tile.put(152, 25);
        pick_tile.put(153, 25);
        pick_tile.put(154, 22);
        pick_tile.put(155, 43);
        pick_tile.put(156, 25);
        pick_tile.put(157, 25);
        pick_tile.put(158, 44);
        pick_tile.put(159, 17);
        pick_tile.put(160, 27);
        pick_tile.put(161, 35);
        pick_tile.put(162, 47);
        pick_tile.put(163, 35);
        pick_tile.put(164, 7);
        pick_tile.put(165, 27);
        pick_tile.put(166, 19);
        pick_tile.put(167, 19);
        pick_tile.put(168, 26);
        pick_tile.put(169, 35);
        pick_tile.put(170, 23);
        pick_tile.put(171, 23);
        pick_tile.put(172, 26);
        pick_tile.put(173, 26);
        pick_tile.put(174, 18);
        pick_tile.put(175, 18);
        pick_tile.put(176, 24);
        pick_tile.put(177, 24);
        pick_tile.put(178, 22);
        pick_tile.put(179, 16);
        pick_tile.put(180, 4);
        pick_tile.put(181, 24);
        pick_tile.put(182, 21);
        pick_tile.put(183, 16);
        pick_tile.put(184, 25);
        pick_tile.put(185, 25);
        pick_tile.put(186, 22);
        pick_tile.put(187, 43);
        pick_tile.put(188, 25);
        pick_tile.put(189, 25);
        pick_tile.put(190, 44);
        pick_tile.put(191, 17);
        pick_tile.put(192, 3);
        pick_tile.put(193, 34);
        pick_tile.put(194, 11);
        pick_tile.put(195, 11);
        pick_tile.put(196, 3);
        pick_tile.put(197, 46);
        pick_tile.put(198, 11);
        pick_tile.put(199, 11);
        pick_tile.put(200, 2);
        pick_tile.put(201, 2);
        pick_tile.put(202, 45);
        pick_tile.put(203, 45);
        pick_tile.put(204, 2);
        pick_tile.put(205, 2);
        pick_tile.put(206, 10);
        pick_tile.put(207, 10);
        pick_tile.put(208, 5);
        pick_tile.put(209, 5);
        pick_tile.put(210, 46);
        pick_tile.put(211, 36);
        pick_tile.put(212, 5);
        pick_tile.put(213, 5);
        pick_tile.put(214, 13);
        pick_tile.put(215, 36);
        pick_tile.put(216, 34);
        pick_tile.put(217, 34);
        pick_tile.put(218, 46);
        pick_tile.put(219, 12);
        pick_tile.put(220, 34);
        pick_tile.put(221, 34);
        pick_tile.put(222, 30);
        pick_tile.put(223, 32);
        pick_tile.put(224, 3);
        pick_tile.put(225, 3);
        pick_tile.put(226, 11);
        pick_tile.put(227, 11);
        pick_tile.put(228, 3);
        pick_tile.put(229, 3);
        pick_tile.put(230, 11);
        pick_tile.put(231, 11);
        pick_tile.put(232, 2);
        pick_tile.put(233, 2);
        pick_tile.put(234, 45);
        pick_tile.put(235, 45);
        pick_tile.put(236, 2);
        pick_tile.put(237, 2);
        pick_tile.put(238, 10);
        pick_tile.put(239, 10);
        pick_tile.put(240, 0);
        pick_tile.put(241, 0);
        pick_tile.put(242, 42);
        pick_tile.put(243, 8);
        pick_tile.put(244, 0);
        pick_tile.put(245, 0);
        pick_tile.put(246, 42);
        pick_tile.put(247, 8);
        pick_tile.put(248, 1);
        pick_tile.put(249, 1);
        pick_tile.put(250, 29);
        pick_tile.put(251, 41);
        pick_tile.put(252, 1);
        pick_tile.put(253, 1);
        pick_tile.put(254, 40);
        pick_tile.put(255, 9);

    }

    static ShapeRenderer shapeRenderer;

    private final short NORTH = 2;
    private final short EAST = 16;
    private final short SOUTH = 64;
    private final short WEST = 8;
    private final short NORTH_WEST = 1;
    private final short NORTH_EAST = 4;
    private final short SOUTH_EAST = 128;
    private final short SOUTH_WEST = 32;

    final float mutationRate = 0.05f;
    final float mutationStep = 0.2f;

    private final Rectangle tetBounds;
    private final Array<ParticleEffectPool.PooledEffect> effects = new Array<ParticleEffectPool.PooledEffect>();
    private final Array<Integer> matchingRow = new Array<Integer>(true, 5, Integer.class);
    private final TextureAtlas textureAtlas;
    private final GridData gridData;
    private final TextureAtlas.AtlasRegion grid_frame;
    private final NinePatch sideBarA;
    private final NinePatch sideBarB;
    private final NinePatch sideBarC;
    private final Sprite ledSprite;
    private final ParticleEffectPool blastPool;
    private ProgressBar levelProgress;
    private float framePad = 100;
    private BitmapFont font;
    private Label labelTitle;
    private Label labelUsername;
    private Label labelScore;
    private Tetromino heldTetromino;
    private Tetromino heldTetroamino;
    private float levelValue;
    private String score;
    private int totalLines;
    private float lineMeterOffset;
    private float comboMeterOffset;
    private float damageMeterOffset;
    private float time;
    private float finalX;
    private float finalY;
    private int maxHeight;
    private Move bestMove;
    private static int deadZone;


    public abstract void spectacularManeuver(Maneuver maneuver, int value, boolean b2b);

    protected abstract void matchEvent(Integer[][] rows);

    protected abstract void outOfBounds(int x, int y, byte[][] data, int rotation);

    protected abstract void outOfBounds(int x, int y, PuzzleElement.PartData partData, int rotation);

    protected abstract void fallCollision(Tetromino tetromino, float px, float py);

    protected abstract void createSplashText(Integer total);

    protected abstract void createSplashBar(Integer line, Integer[][][] lineGroups, ShapeRenderer shapeRenderer);

    protected abstract void animateLineRemoval(Integer[][][] lines, GridActor gridActor);

    Pool<Rectangle> rectPool = new Pool<Rectangle>() {

        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };
    public static ArrayMap<Integer, TextureAtlas.AtlasRegion> regions = new ArrayMap<Integer, TextureAtlas.AtlasRegion>();
    private Array<Tetromino> futures = new Array<Tetromino>(true, 10, Tetromino.class);
    private Array<Integer[]> lines = new Array<Integer[]>(true, 4, Integer[].class);
    private Label[][] weightLabel;
    private Tetromino tetromino;
    private TextureAtlas.AtlasRegion bg_cell;
    private boolean shadow;
    private GridLocation atTarget;
    private boolean debugAi;
    private Vector2 targetTile, tilePosition;
    private float targetX;
    private float translationX;
    private float elapsed;
    private float lifetime = 0.05f;
    private float startX;
    private boolean move;
    private Group backgroundGroup;
    private RowGroup[] rowGroup;
    private boolean one_color;
    private Barrier barrier;
    private boolean landed = true;
    private boolean debug;
    private float speed = 1f;
    private float fallTime = 0;
    private float fallDelay = 1;
    private Array<Rectangle> tiles = new Array<Rectangle>();
    private TextureRegion backGround;
    private boolean checkTSpin;
    private Drawable frame;
    private Array<Genome> genomes;
    private int currentGenome;


    public GridActor(GridData gridData, TextureAtlas textureAtlas, Label[][] weightLabel) {
        this(gridData, textureAtlas);
        this.weightLabel = weightLabel;
    }

    public GridActor(GridData gridData, TextureAtlas textureAtlas, Skin skin, Label[][] weightLabel) {
        this(gridData, textureAtlas);
        this.weightLabel = weightLabel;
    }


    public void setFrame(Drawable frame) {
        this.frame = frame;
    }

    public GridActor(GridData gridData, TextureAtlas textureAtlas) {
        deadZone = 2;
        this.textureAtlas = textureAtlas;
        this.bg_cell = textureAtlas.findRegion("cell");
        this.gridData = gridData;
        this.targetTile = new Vector2();
        this.tilePosition = new Vector2();
        this.setSize(gridData.getCol(), gridData.getRow());
        this.initializeCells(gridData.getCol(), gridData.getRow(), textureAtlas);
        this.grid_frame = textureAtlas.findRegion("full_frame");

        this.sideBarA = textureAtlas.createPatch("filla");
        sideBarA.setColor(Color.GREEN);

        this.sideBarB = textureAtlas.createPatch("filla");
        sideBarB.setColor(Color.YELLOW);

        this.sideBarC = textureAtlas.createPatch("filla");
        sideBarC.setColor(Color.SLATE);

        TextureRegion led = textureAtlas.findRegion("led");
        ledSprite = new Sprite(led);
        ledSprite.setColor(Color.RED);


        if (shapeRenderer == null) shapeRenderer = new ShapeRenderer();
        shadow = false;
        debugAi = true;
        debug = true;
        if (debug)
            debug();
        for (int i = 0; i < 20; i++)
            regions.put(i, textureAtlas.findRegion(String.valueOf(i)));
        tetBounds = rectPool.obtain();

        ParticleEffect blastParticles = new ParticleEffect();
        blastParticles.load(Gdx.files.internal("paricleEffects/blast.p"), textureAtlas);
        blastPool = new ParticleEffectPool(blastParticles, 1, 2);
        font = FontUtils.generateFont(10, 1, 1, 1, Color.GOLD, Color.GOLDENROD, Color.BLACK, Color.WHITE, "font/zephyrea.ttf");
        genomes = new Array<Genome>(true,50,Genome.class);
        for(int i = 0;  i < 50; i++){
            genomes.add(new Genome());
        }



    }


    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    public boolean isOneColor() {
        return one_color;
    }

    public void setOneColor(boolean one_color) {
        this.one_color = one_color;
    }

    /**
     * Board set up...
     *
     * @param col          cols in row
     * @param row          rows in cols
     * @param textureAtlas atlas containing all the possible pieces...
     */
    private void initializeCells(final int col, final int row, TextureAtlas textureAtlas) {
//      setup background cell first, to the render order in proper

        backgroundGroup = new Group();
        Texture imgTexture = new Texture(Gdx.files.internal("defaultSet/cell.png"));
        imgTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        imgTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        backGround = new TextureRegion(imgTexture);
        backGround.setRegion(0, 0, imgTexture.getWidth() * col, imgTexture.getHeight() * (row - 2));


//      row groups contain the images that will eventually display the blocks
        rowGroup = new RowGroup[row];
        int halfHeight = rowGroup.length * GameScreen.TILE_SIZE / 2;
        for (int y = 0; y < row; y++) {

            rowGroup[y] = new RowGroup();
            rowGroup[y].setY(y * GameScreen.TILE_SIZE - halfHeight);
            rowGroup[y].setZIndex(0);
            rowGroup[y].setWidth(col * GameScreen.TILE_SIZE);
            rowGroup[y].setHeight(GameScreen.TILE_SIZE);

            for (int x = 0; x < col; x++) {

//              the cell holds some visual data
                final int finalX = x;
                final int finalY = y;
                final Cell c = new Cell();
                c.setTouchable(Touchable.enabled);

                final Image blockImage = new Image();
                c.setSize(GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
                blockImage.setSize(GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
                blockImage.setZIndex(2);
                c.addActor(blockImage);

                rowGroup[y].add(c).size(GameScreen.TILE_SIZE);

            }
            barrier = new Barrier(row, col, textureAtlas.findRegion("0")) {

                @Override
                protected void complete(float progress, float added) {

                    removeBarriers();
                    addBarriers();
                    updateGrid();
                }

                @Override
                protected void change(float progress, float added) {
                    for (int i = 0; i < row; i++) {
                        rowGroup[i].setY((i * GameScreen.TILE_SIZE) + added);
                    }
                }
            };
            backgroundGroup.addActor(barrier);
            backgroundGroup.addActor(rowGroup[y]);

        }
        addActor(backgroundGroup);
        setZIndex(0);

    }

    private void addBarriers() {
        final int rows = gridData.getRow();
        final int cols = gridData.getCol();
        for (int row = 0; row < barrier.getBarrierValue(); row++) {
            getGridData().insertRow(0, new Row(rows, cols).makeBarrier());
        }
        for (int row = 0; row < getGridData().data().length; row++) {
            if (row >= rows)
                getGridData().pop();
        }
    }

    private void removeBarriers() {
        boolean hasRow;
        while (true) {
            hasRow = false;
            for (int row = 0; row < getGridData().data().length; row++) {
                Row r = getGridData().data()[row];
                if (r.isBarrier()) {
                    getGridData().removeRow(row);
                    hasRow = true;
                }
            }
            if (!hasRow) break;
        }

    }

    public Barrier getBarrier() {
        return barrier;
    }

    public void enable(int x, int y, byte value) {

        x = Math.min(x, getGridData().getCol() - 1);
        y = Math.min(y, getGridData().getRow() - 1);

        Array<com.badlogic.gdx.scenes.scene2d.ui.Cell> children = rowGroup[y].getCells();
        final com.badlogic.gdx.scenes.scene2d.ui.Cell cell = children.get(x);
        Cell actor = (Cell) cell.getActor();


        final Image image = (value == 0) ? null : new Image(value == Row.barrierValue ? textureAtlas.findRegion("BLOCK") : textureAtlas.findRegion(String.valueOf(value)));
        if (image != null)
            ((Image) actor.getChildren().items[0]).setDrawable(image.getDrawable());
        else
            ((Image) actor.getChildren().items[0]).setDrawable(null);
    }

    public void resetPosition() {
        final int row = gridData.getRow();
        final int col = gridData.getCol();
        tetromino.setAbsY(row - tetromino.getHeight() - deadZone);
        tetromino.setAbsX(col / 2 - tetromino.getWidth() / 2 - 1);
        fallTime = 0;
        targetTile.set(tetromino.getAbsX(), tetromino.getAbsY());
        finalX = tetromino.getAbsX() + tetromino.getWidth() / 2;
        generatePossiblePlacements(tetromino);
    }

    public boolean blocked() {
        return false;
    }

    public RowGroup[] getRowGroup() {
        return rowGroup;
    }

    float rotationTime = 0.1f, rotationTimeElapsed;

    @Override
    public void act(float delta) {
        super.act(delta);

//        fixme dirt dog
        if (tetromino != null && move) {
            elapsed += delta;
            float progress = Math.min(1f, elapsed / lifetime);
            tetromino.setAbsX(Interpolation.circleOut.apply(startX, targetX, progress));
            if (progress == 1) {
                move = false;
                elapsed = 0;
            }
        }

//        if(tetromino != null && bestMove != null){
//
//
//            finalX = 0;//bestMove.x;
//            finalY = 0;//bestMove.y;
//
//            if(tetromino.getRotation() != bestMove.rotation){
//                rotationTimeElapsed+=delta;
//                if(rotationTimeElapsed >= rotationTime){
//                    int rotation = tetromino.getRotation();
//                    rotation+= 1;
//                    tetromino.setRotation(rotation);
//                    rotationTimeElapsed -= rotationTime;
//                }
//            }
//
//            float x  = tetromino.getAbsX() + bestMove.partData.getX(tetromino.getRotation()) ;
//            float y  = tetromino.getAbsY() + bestMove.partData.getY(tetromino.getRotation()) ;
//
//            if(x < finalX && !move ){
//                if (!absCollision(x + 1, y, tetromino.getShape())) {
//                    moveRight();
//                }
//            }
//
//            else if(x > finalX && !move){
//                if (!absCollision(x - 1, y,
//                        tetromino.getShape())) {
//                    moveLeft();
//                }
//            }
//
//
//        }


    }

    public boolean checkMatches(float delta) {
        matchingRow.clear();
        lines.clear();

        for (int y = 0; y < gridData.getRow(); y++) {
            boolean rowIsFill = gridData.getRowFull(y);

            if (rowIsFill) {
                matchingRow.add(y);
            } else {
                if (matchingRow.size > 0) {
                    lines.add(matchingRow.toArray());
                    matchingRow.clear();
                }
            }

        }
        if (lines.size > 0) {
            matchEvent(lines.toArray());
        }

        if (checkTSpin) {
            int tSpinLinesCleared = 0;
            for (Integer[] rs : lines) {
                for (Integer r : rs)
                    if (tSpinLocation.contains(r, false)) {
                        tSpinLinesCleared++;
                    }

            }

            Maneuver maneuver = null;
            if (tSpinLinesCleared == 0) maneuver = Maneuver.TSpin;
            else if (tSpinLinesCleared == 1) maneuver = Maneuver.TSpinSingle;
            else if (tSpinLinesCleared == 2) maneuver = Maneuver.TSpinDouble;
            spectacularManeuver(maneuver, tSpinLinesCleared, false);
            checkTSpin = false;
            tSpinLocation.clear();
        }

        // FIXME: 7/25/2018 Not supported yet
        return false;
    }

    private boolean isTSpin(Maneuver maneuver) {
        return (maneuver.equals(Maneuver.TSpin) ||
                maneuver.equals(Maneuver.TSpinDouble) ||
                maneuver.equals(Maneuver.TSpinSingle));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float delta = Gdx.graphics.getDeltaTime();
        batch.draw(backGround, getX(), getY(), getWidth(), getHeight() - (GameScreen.TILE_SIZE * 2));


        if (tetromino != null)
            drawPiece(batch, parentAlpha, GameScreen.TILE_SIZE, tetromino, tetromino.getAbsX(), tetromino.getAbsY());

        if (shadow)
            drawShadow(batch);

//        for(Move p : prediction){
////
////            drawShape(batch,tetromino,p.x,p.y);
//////                shapeRenderer.rect(
//////                        getX()+p[0]*GameScreen.TILE_SIZE,
//////                        getY()+p[1]*GameScreen.TILE_SIZE,
//////                        GameScreen.TILE_SIZE,
//////                        GameScreen.TILE_SIZE);
////        }

//        for(Move p : validTiles){

        for (Move p : validTiles) {
//            Move p = validTiles.first();
            drawShape(batch, p.partData.getData()[p.rotation], p.rotation, p.x, p.y);
        }


        batch.draw(grid_frame, getX() - 75, getY() - 45);

        /*
            Future column
         */

        drawFutures(batch, parentAlpha);


        /*
            held
         */

        if (heldTetromino != null) {
            float tileSize = GameScreen.TILE_SIZE / 2;
            drawPiece(batch, parentAlpha, tileSize, heldTetromino, -4 - (tetromino.getWidth() / 2) - 0.2f, (gridData.getRow() - 2) * 2);
        }

        labelScore.draw(batch, parentAlpha);
        labelScore.setPosition(getX(), getY() - 25);

        labelUsername.draw(batch, parentAlpha);
        labelUsername.setPosition(getX() + 61, getY() + getHeight());


        updateMeters();

        sideBarA.draw(batch, getX() - 57, getY() + 15, 7, getLevelValue());
        sideBarB.draw(batch, getX() - 45, getY() + 15, 7, getComboValue());
        sideBarC.draw(batch, getX() - 32, getY() + 15, 7, getIncomingValue());

        ledSprite.setPosition(getX() - 56f, getY() + 15 + meterHeight + 4.5f);
        ledSprite.setColor(Color.GREEN);
        ledSprite.draw(batch);

        ledSprite.setPosition(getX() - 44f, getY() + 15 + meterHeight + 4.5f);
        ledSprite.setColor(Color.YELLOW);
        ledSprite.draw(batch);

        ledSprite.setPosition(getX() - 32f, getY() + 15 + meterHeight + 4.5f);
        ledSprite.setColor(Color.RED);
        ledSprite.draw(batch);


        super.draw(batch, parentAlpha);
        // Update and draw effects:
        for (int i = effects.size - 1; i >= 0; i--) {

            ParticleEffectPool.PooledEffect effect = effects.get(i);

            effect.draw(batch, delta);
            if (effect.isComplete()) {
                freeEffect(effect);
            }
        }

        if (bestMove != null) {
            byte[][] data = bestMove.partData.getData()[bestMove.rotation];
            float tileSize = GameScreen.TILE_SIZE;

            for (int y = 0; y < data.length; y++) {
                for (int x = 0; x < data[y].length; x++) {

                    if (data[y][x] > 0) {


                        int b_x = bestMove.x + x - bestMove.partData.getX(tetromino.getRotation());
                        int b_y = bestMove.y + y - bestMove.partData.getY(tetromino.getRotation());

                        byte value = data[y][x];
                        if (value == 0) continue;

                        TextureAtlas.AtlasRegion region = textureAtlas.findRegion("future");
                        batch.draw(region,
                                (b_x * tileSize) + getX(),
                                (b_y * tileSize) + getY(),
                                tileSize, tileSize);


                    }

                }
            }
        }


        if (debug) {
            batch.end();
            shapeRenderer.setColor(new Color(0, 0, 1, 0.3f));
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//            shapeRenderer.rect(getX() + (GameScreen.TILE_SIZE * (getGridData().getCol() / 2 - 1)), getY() + (GameScreen.TILE_SIZE * (getGridData().getRow() - 2)), GameScreen.TILE_SIZE * 2, GameScreen.TILE_SIZE);
//            shapeRenderer.setColor(Color.RED);
//
//            for (Rectangle rectangle : tiles) {
//                shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
//            }
            shapeRenderer.setColor(Color.FIREBRICK);


//            shapeRenderer.rect(tetBounds.x, tetBounds.y, tetBounds.width, tetBounds.height);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            if (tetromino != null) {
                shapeRenderer.rect(
                        getX() + (tetromino.getAbsX() + tetromino.getData().getX(tetromino.getRotation())) * GameScreen.TILE_SIZE,
                        getY() + (tetromino.getAbsY() + tetromino.getData().getY(tetromino.getRotation())) * GameScreen.TILE_SIZE,
                        GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
            }
            if (tetromino != null && bestMove != null) {
                shapeRenderer.rect(
                        getX() + bestMove.x * GameScreen.TILE_SIZE,
                        getY() + bestMove.y * GameScreen.TILE_SIZE,
                        GameScreen.TILE_SIZE, GameScreen.TILE_SIZE);
            }
            for (Move p : prediction) {

//                shapeRenderer.rect(
//                        getX()+p.x*GameScreen.TILE_SIZE,
//                        getY()+p.y*GameScreen.TILE_SIZE,
//                        GameScreen.TILE_SIZE,
//                        GameScreen.TILE_SIZE);
            }


            shapeRenderer.rect(
                    getX() + finalX * GameScreen.TILE_SIZE,
                    getY() + finalY * GameScreen.TILE_SIZE,
                    GameScreen.TILE_SIZE,
                    GameScreen.TILE_SIZE);


            shapeRenderer.setColor(Color.YELLOW);
            for (int gx = 0; gx < gridData.getCol(); gx++) {
                byte y = gridData.getDataHeightAt(gx);

                shapeRenderer.rect(
                        getX() + gx * GameScreen.TILE_SIZE,
                        getY() + y * GameScreen.TILE_SIZE,
                        GameScreen.TILE_SIZE,
                        GameScreen.TILE_SIZE);
            }


            for (Move p : validTiles) {
//            Move p = validTiles.first();

                shapeRenderer.rect(
                        getX() + p.x * GameScreen.TILE_SIZE,
                        getY() + p.y * GameScreen.TILE_SIZE,
                        GameScreen.TILE_SIZE,
                        GameScreen.TILE_SIZE);
            }
            shapeRenderer.end();
            batch.begin();
        }

//        for (int gy = 0; gy < gridData.getRow(); gy++) {
//            for (int gx = 0; gx < gridData.getCol(); gx++) {
//                if(getValueAt(gx, gy) > 0) continue;
//
//                int value = getAutoValue(gx, gy);
////                if(value == 47) continue;
//
//                font.draw(batch,String.valueOf(value),getX() + gx*GameScreen.TILE_SIZE,getY() + (gy+1)*GameScreen.TILE_SIZE);
//
//            }
//        }


    }

    private void freeEffect(ParticleEffectPool.PooledEffect effect) {
        effect.free();
        effects.removeValue(effect, true);

    }


    private void updateMeters() {

    }

    private void drawFutures(Batch batch, float parentAlpha) {
        float tileSize = GameScreen.TILE_SIZE / 2;
        float displayXOffset = gridData.getCol() * 2;
        for (int i = 0; i < futures.size; i++) {

            float y = i * 4;
            drawPiece(batch, parentAlpha, tileSize, futures.get(i), displayXOffset + 2.3f, y);
        }

    }


    private void drawAiTarget(Batch batch) {
        if (tetromino == null || atTarget == null) return;
        byte[][] data = tetromino.getShape();
        float tileSize = GameScreen.TILE_SIZE;
        int sy = (int) atTarget.getPosition().y;
        int sx = (int) atTarget.getPosition().x;

        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[y].length; x++) {

                if (data[y][x] > 0) {


                    int b_x = sx + x;
                    int b_y = sy + y;

                    byte value = data[y][x];
                    if (value == 0) continue;

                    Sprite sprite = textureAtlas.createSprite(String.valueOf(19));
                    sprite.setSize(tileSize, tileSize);
                    sprite.setPosition((b_x * tileSize) + getX(), (b_y * tileSize) + getY());
                    sprite.setRotation(90);
//                    batch.draw(sprite,
//                            ( b_x * tileSize ) + getX(),
//                            ( b_y * tileSize ) + getY(),
//                            tileSize,tileSize);


                }


            }
        }
        if (weightLabel != null) {
            for (int y = 0; y < gridData.getRow() - 2; y++) {
                for (int x = 0; x < gridData.getCol(); x++) {
                    weightLabel[y][x].setPosition((int) (getX() + (x * tileSize)), (int) (getY() + (y * tileSize)));
                    weightLabel[y][x].setText("[ " + String.valueOf(gridData.getDataAutoAt(x, y)) + " ]");
                    weightLabel[y][x].draw(batch, 1);
                }
            }
        }
    }

    private void drawShadow(Batch batch) {
        if (tetromino == null || landed) return;
        byte[][] data = tetromino.getShape();
        int sx = (int) tetromino.getX();
        int sy = (int) tetromino.getY();

        float tileSize = GameScreen.TILE_SIZE;
        boolean collision;
        do {
            sy -= 1;
            collision = absCollision(sx, sy, data);
            if (collision) {
                sy += 1;

            }
        } while (!collision);


        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[y].length; x++) {

                if (data[y][x] > 0) {


                    int b_x = sx + x;
                    int b_y = sy + y;

                    byte value = data[y][x];
                    if (value == 0) continue;

                    TextureAtlas.AtlasRegion region = textureAtlas.findRegion("future");
                    batch.draw(region,
                            (b_x * tileSize) + getX(),
                            (b_y * tileSize) + getY(),
                            tileSize, tileSize);


                }


            }
        }


    }


    private void drawShape(Batch batch, byte[][] data, int rotation, int px, int py) {
        float tileSize = GameScreen.TILE_SIZE;

//        boolean inBounds = isInBounds(tetromino, px, py, gridData);
//        if(!inBounds) return;


        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[y].length; x++) {

                if (data[y][x] > 0) {


                    int b_x = px + x - tetromino.data.getX(rotation);
                    int b_y = py + y - tetromino.data.getY(rotation);

                    byte value = data[y][x];
                    if (value == 0) continue;

                    TextureAtlas.AtlasRegion region = textureAtlas.findRegion("future");
                    batch.draw(region,
                            (b_x * tileSize) + getX(),
                            (b_y * tileSize) + getY(),
                            tileSize, tileSize);


                }


            }
        }


    }

    private void drawPiece(Batch batch, float parentAlpha, float tileSize, Tetromino tetromino, float px, float py) {
        if (tetromino == null) return;

        byte[][] data = tetromino.getShape();

        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[y].length; x++) {

                byte value = data[y][x];
                if (value == 0) {
                    value = 10;
                }
                // TODO: 8/1/2018 Make option
                if (one_color) value = tetromino.getColor();

                float positionX = (px * tileSize) + getX() + (x * tileSize);
                float positionY = (py * tileSize) + getY() + (y * tileSize);
                batch.draw(regions.get((int) value), positionX, positionY, tileSize, tileSize);

            }
        }
    }


    public boolean absCollision(float tx, float ty, byte[][] dat) {
        return absCollisionValue(tx, ty, dat) != 0;
    }

    public float absCollisionScore(int tx, int ty, byte[][] dat, double[][] weights) {
        float collisionScore = 0;
        for (int y = 0; y < dat.length; y++) {
            for (int x = 0; x < dat[y].length; x++) {

                if (dat[y][x] > 0) {

                    int b_x = tx + x;
                    int b_y = ty + y;
                    byte value = getValueAt(b_x, b_y);

                    double weight = weights[b_y][b_x];
                    if (value == 0) {
                        collisionScore += weight;
                    }


                }


            }
        }
        return collisionScore;
    }

    Array<Integer> tSpinLocation = new Array<Integer>();

    public float maneuverLocation() {
        float collisionScore = 0;
        tSpinLocation.clear();
        int tx = (int) getTetromino().getAbsX();
        int ty = (int) getTetromino().getAbsY();
        byte[][] dat = getTetromino().getShape();

        for (int y = 0; y < dat.length; y++) {
            for (int x = 0; x < dat[y].length; x++) {

                if (dat[y][x] > 0) {
                    int b_x = tx + x;
                    int b_y = ty + y;
                    tSpinLocation.add(b_y);

                }


            }
        }
        return collisionScore;
    }

    public int absCollisionValue(float tx, float ty, byte[][] dat) {
        int collision = 0;
        for (int y = 0; y < dat.length; y++) {
            for (int x = 0; x < dat[y].length; x++) {

                if (dat[y][x] > 0) {

                    float b_x = (tx + x);
                    float b_y = (ty + y);
                    if (
                            tx < 0 ||
                                    tx >= getGridData().getCol() ||
                                    b_y < 0 ||
                                    b_y >= getGridData().getRow() ||
                                    gridData.getDataValueAt((int) b_x, (int) b_y) != 0) {

                        collision++;
                    }
                }
            }
        }

        return collision;
    }

//    public boolean quickCollision(float tx, float ty , int rotation, PuzzleElement.PartData partData,Row[] rows) {
//        byte[][] shapeData = partData.getData()[rotation];
//
//        for (int y = 0; y < shapeData.length; y++) {
//            for (int x = 0; x < shapeData[y].length; x++) {
//
//                if (shapeData[y][x] > 0) {
//
//                    int b_x = (int) (tx + x);
//                    int b_y = (int) (ty + y);
//                    if(isOpen(b_x, b_y,rows)){
//                        return true;
//                    }
//
//                }
//            }
//        }
//
//        return false;
//    }

    public boolean collision(float tx, float ty, int rotation, PuzzleElement.PartData partData) {

        byte[][] shapeData = partData.getData()[rotation];

        for (int y = 0; y < shapeData.length; y++) {
            for (int x = 0; x < shapeData[y].length; x++) {

                if (shapeData[y][x] > 0) {

                    float b_x = (tx + x);
                    float b_y = (ty + y);
                    if (gridData.safeGetDataValueAt((int) b_x, (int) b_y) != 0) {
                        return true;
                    }

                }
            }
        }

        return false;
    }

    @Deprecated
    public boolean collision(float tx, float ty) {


        return false;
    }


    public byte getValue(float x, float y) {
        if (y < 0 || y >= gridData.getCol() || x < 0 || x >= gridData.getCol())
            return Byte.MAX_VALUE;

        return gridData.getDataValueAt((int) x, (int) y);
    }


    public boolean canRotate(int rotation) {
        final int tx = (int) tetromino.getAbsX();
        final int ty = (int) tetromino.getAbsY();
        final byte[][] dat = tetromino.data.getData()[rotation];
        final int row = gridData.getRow();
        final int col = gridData.getCol();

        for (int y = 0; y < dat.length; y++) {
            for (int x = 0; x < dat[y].length; x++) {

                if (dat[y][x] > 0) {

                    int b_x = tx + x;
                    int b_y = ty + y;

                    if (b_y < 0 || b_y >= row || b_x < 0 || b_x >= col || (gridData.getDataValueAt(b_x, b_y) != 0))
                        return false;

                }


            }
        }
        return true;
    }

    @Deprecated
    public boolean collision(int tx, int ty) {
        return true;//collision(tx, ty, tetromino.getShape());
    }

    public void setValueAt(int x, int y, byte value) {
        final int row = gridData.getRow();
        final int col = gridData.getCol();

        if (x < 0 || x >= col || y < 0 || y >= row) return;
        gridData.setDataValueAt(x, y, value);
    }

    public void rotateRight() {
        if (move) return;

        int testRotation = tetromino.getRotation();
        testRotation--;
        if (testRotation < 0)
            testRotation = 3;

        if (canRotate(testRotation)) {
            tetromino.setRotation(testRotation);
        }
//        getAllMoves(gridData, tetromino);
    }

    public void rotateLeft() {
        if (move) return;

        int testRotation = tetromino.getRotation();
        testRotation++;
        if (testRotation > 3)
            testRotation = 0;

        if (canRotate(testRotation)) {
            tetromino.setRotation(testRotation);
        }
//        getAllMoves(gridData, tetromino);
    }

    public void releasePiece(PartFactory partFactory) {
        partFactory.free(tetromino);
    }

    public void setTetromino(Tetromino tetromino) {
        this.tetromino = tetromino;
    }

    public Tetromino setNewPiece(PartFactory partFactory) {
        if (!landed) return null;
        this.tetromino = partFactory.newPiece();
        resetPosition();
        landed = false;
//        getAllMoves(gridData, tetromino);
        return tetromino;
    }

    public Tetromino setNewPiece(Tetromino tetromino) {
        this.tetromino = tetromino;
        resetPosition();
        landed = false;
        getAllMoves(gridData, tetromino);
        return tetromino;
    }

    public boolean needNewPiece() {
        return tetromino == null;
    }

    public Tetromino getTetromino() {
        return tetromino;
    }

    public boolean fall(float delta) {
        if (landed) return true;
        fallTime += delta;
        if (fallTime >= fallDelay) {

            final float tx = tetromino.getAbsX();
            final float ty = tetromino.getAbsY() - 1;
            final int rotation = tetromino.getRotation();
            final PuzzleElement.PartData partData = tetromino.getData();

            landed = collision(tx, ty, rotation, partData);
            if (!landed)
                tetromino.setAbsY(ty);

//            getAllMoves(gridData, tetromino);

            fallTime -= fallDelay;
        }

        if (landed) {
//            tetromino.moveBy(0,-1);
            fallTime = 0;
//            fallCollision(tetromino,tetromino.getAbsX(),tetromino.getAbsY());


            ParticleEffectPool.PooledEffect effect = blastPool.obtain();
            effect.setPosition(
                    tetromino.getAbsX() * GameScreen.TILE_SIZE + getX() + (GameScreen.TILE_SIZE / 2),
                    tetromino.getAbsY() * GameScreen.TILE_SIZE + getY() + (GameScreen.TILE_SIZE / 2));
            effects.add(effect);

            return true;
        }

        return false;
    }

    @Deprecated
    public void closeRow(Integer[] rows) {
    }

    public void reset() {
        final int row = gridData.getRow();
        final int col = gridData.getCol();

        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                gridData.setDataValueAt(x, y, (byte) 0);
                gridData.setDataHeightAt(x, 0);
                gridData.setDataWeightAt(x, y, (byte) 0);
            }
        }

    }

    public int getShapeRotation() {
        // FIXME: 7/27/2018 Moe dirt
        return tetromino.getRotation();
    }

    public static byte getValueAt(int x, int y, GridData gridData) {
        final int row = gridData.getRow();
        final int col = gridData.getCol();

        if (x < 0 || x > col || y < 0 || y >= row) return Byte.MAX_VALUE;
        return gridData.getDataValueAt(x, y);
    }

    public byte getValueAt(int x, int y) {
        final int row = gridData.getRow();
        final int col = gridData.getCol();

        if (x < 0 || x >= col || y < 0 || y >= row) return Byte.MAX_VALUE;
        return gridData.getDataValueAt(x, y);
    }

    /**
     * This method provide the best fit for a shape at the @param tx and @param ty based off having closed peaces under it
     *
     * @param tx           and arbitrary x value within the grid
     * @param ty           and arbitrary y value within the grid
     * @param shape        current shape of puzzle piece.
     * @param inBoundsOnly only considers values that are on grid - (0,0) -> (width,Height)
     * @return this is the sum of the values below the given position
     */
    public int coverage(int tx, int ty, byte[][] shape, boolean inBoundsOnly) {
        int collision = 0;

        for (int y = 0; y < shape.length; y++) {
            for (int x = 0; x < shape[y].length; x++) {

                if (shape[y][x] > 0) {

                    int b_x = tx + x;
                    int b_y = ty + y;

                    if (absCollision(b_x, b_y - 1, shape)) {
                        collision++;
                        collision += b_y;
                    }

                }


            }
        }


        return collision;
    }

    public GridData getGridData() {
        return gridData;
    }

    public void drawAiDebug(GridLocation gridLocation) {
        atTarget = gridLocation;
    }

    public void setWeightLabels(Label[][] weightLabels) {
        this.weightLabel = weightLabels;
    }

    public void moveRight() {
        targetX = (int) tetromino.getAbsX() + 1;
        startX = tetromino.getAbsX();

        move = true;
    }

    public void moveLeft() {
        targetX = (int) tetromino.getAbsX() - 1;
        startX = tetromino.getAbsX();

        move = true;
    }

    private final Array<BarSplash> barSplashes = new Array<BarSplash>();

    public void removeRow(Pool<Row> pool, final Integer[][][] lineGroups) {

        int first = -1, last = 0;

        /**
         * We know lines are to be removed, so animation cycle is guaranteed.
         */


        int match = 0;
        for (final Integer[][] lineGroup : lineGroups) {
            for (Integer[] lines : lineGroup) {

//              the order these line are removed matter. When items are remove the indexes change
                Arrays.sort(lines, Collections.<Integer>reverseOrder());
                for (Integer line : lines) {
                    Row rw = gridData.data()[line];
                    rw.fill((byte) 0);
                    rw.remove = true;
                    getRowGroup()[line].empty();
                    match++;
//                  This function trigger the next shoe
                    createSplashBar(line, lineGroups, shapeRenderer);

                }

            }
        }
//        for loop won't cut it here...
        boolean again;
        do {
            again = false;
            for (int i = 0; i < gridData.data().length; i++) {
                Row row = gridData.data()[i];
                if (row.remove) {

                    row.fill((byte) 0);
                    gridData.removeRow(i);
                    gridData.insertRow(gridData.getRow() - 1, row);

                    row.remove = false;
                    again = true;

                }
            }
        } while (again);

        createSplashText(match);
    }

    public byte[] updateHeights(Row[] data, byte[] heights) {
        int rows = gridData.getRow();
        int cols = gridData.getCol();

        for (int col = 0; col < cols; col++) {
            int row = getHighest(rows, col, data);
            if(row > maxHeight)
                maxHeight = row;
            if (data[row].getData()[col] == 0) heights[col] = (byte) row;

        }

        return heights;
    }


    public void updateHeights() {
        int rows = gridData.getRow();
        int cols = gridData.getCol();

        for (int col = 0; col < cols; col++)
            gridData.setDataHeightAt(col, getHighest(rows, col, gridData.data()));

        recomputeMaxHeight();
    }

    public void updateGrid() {

        int rows = gridData.getRow();
        int cols = gridData.getCol();

////        fill barrier first
        for (int row = 0; row < rows; row++) {
            rowGroup[row].setY((row * GameScreen.TILE_SIZE));
            for (int col = 0; col < cols; col++) {
                byte value = getValueAt(col, row);
                enable(col, row, value);
            }
        }
        updateHeights();
    }

    public int getBarrierOffset() {
        return barrier.getBarrierValue();
    }

    private void updateData() {

        for (int i = 0; i < getBarrier().getBarrierValue(); i++)
            gridData.insertRow(0, new Row(getGridData().getCol(), getGridData().getRow()).fill(Byte.MIN_VALUE));


        for (int i = 0; i < gridData.data().length; i++) {
            if (i >= gridData.getRow())
                gridData.pop();
        }


    }

    public void moveUp() {
        targetX = (int) tetromino.getAbsX() - 1;
        startX = tetromino.getAbsX();

        move = true;
    }

    public boolean moveUp(float delta) {
        float y = tetromino.getAbsY() + speed * delta;


        if (!absCollision(tetromino.getAbsX(), tetromino.getAbsY(), tetromino.getShape())) {
            tetromino.setAbsY(y);
            return false;
        }
        return true;
    }

    public boolean moveDown(float delta) {
        float y = tetromino.getAbsY() - 1;// speed * delta;
        if (!absCollision(tetromino.getAbsX(), y, tetromino.getShape())) {
            tetromino.setAbsY(y);
            return false;
        }
        return true;
    }

    public boolean moveDown(float delta, float speed) {
        float y = tetromino.getAbsY() - speed * delta;
        if (!absCollision(tetromino.getAbsX(), tetromino.getAbsY() - speed * delta, tetromino.getShape())) {
            tetromino.setAbsY(y);
            return false;
        }
        return true;
    }

    public void setLanded(boolean landed) {
        this.landed = landed;
    }

    public void addPenaltyLines(int hitValue) {
        for (int i = 0; i < hitValue; i++) {
            gridData.insertRow(i, new Row(gridData.getCol(), gridData.getRow()).fill((byte) 1, MathUtils.random(0, gridData.getCol() - 1)));
        }
        updateGrid();
    }

    public boolean cleanField() {
        for (int y = 0; y < getGridData().getRow(); y++) {
            for (int x = 0; x < getGridData().getCol(); x++) {
                if (getGridData().getDataValueAt(x, y) > 0) return false;
            }
        }
        return true;
    }


    public void checkTSpin() {
        checkTSpin = true;
    }

    public void setFontStyle(BitmapFont font) {
        this.font = font;
    }

    public void setFontStyle(Label.LabelStyle goldSplashStyle) {
        labelScore = new Label("Score", goldSplashStyle);
        labelUsername = new Label("Username", goldSplashStyle);
        labelTitle = new Label("Title", goldSplashStyle);
    }

    public void setFutures(PartFactory partFactory) {
        futures.clear();
        int count = partFactory.getFutureCount();

        for (int i = 0; i < count; i++) {
            Tetromino part = partFactory.get(i);
            futures.add(part);
        }
        futures.reverse();

    }

    public Tetromino heldTetromino() {
        return heldTetromino;
    }

    public void setHeldTetromino(Tetromino heldTetromino) {
        this.heldTetromino = heldTetromino;
    }

    final float meterHeight = 256.5f;

    private float getLevelValue() {
        return lineMeterOffset;
    }

    private float getComboValue() {
        return comboMeterOffset;
    }

    private float getIncomingValue() {
        return damageMeterOffset;
    }

    public void setScore(String score) {
        this.score = score;
        labelScore.setText(score);
    }

    public void setLines(int lines, int linesPerLevel) {
        float unit = meterHeight / linesPerLevel;
        lineMeterOffset = unit * lines;
    }

    public void setComboStep(int combo) {
        float unit = meterHeight / getGridData().getRow();
        comboMeterOffset = unit * combo;
    }

    public void setIncoming(int hits) {
        float unit = meterHeight / getGridData().getRow();
        damageMeterOffset = unit * hits;
    }

    public void setTargetCell(float x, float y) {

        if (finalX == x && finalY == y)
            rotateRight();

        if (tetromino.getWidth() < 2)
            x--;

        finalX = x;
        finalY = y;

        generatePossiblePlacements(tetromino);

    }

    private void updateLocations(float x, float y) {
        prediction.clear();
//        final Location newLocation = new Location((int)x,(int)y);
//        for(Location location : validTiles){
//            boolean b = location.equals(newLocation);
//            if(b){
//                Array<Location> locations = generatePossiblePlacements(tetromino, location);
//                prediction.addAll(locations.toArray());
//            }
//        }
    }

    private ObjectSet<Move> generatePossiblePlacements(Tetromino tetromino) {
        updateHeights();

//        for(Move move : prediction){
//            movesPool.free(move);
//            move.reset();
//
//        }
//        bestMove = null;
////        eg width = 3 means 0,1,2. Hench ( width-1 in name only)
//        double bestScore = 1e20;
//        for(int rotation = 0; rotation < 4; rotation++){
//
//            final PuzzleElement.PartData partData = tetromino.getData();
//            final int internalX = partData.getX(rotation);
//            final int internalY = partData.getY(rotation);
//            final int width = partData.getWidth(rotation);
//            final int height = partData.getHeight(rotation);
//            final int startX = gridData.getCol() - width;
//            final int startY = gridData.getRow() - deadZone - height;
//
//            for(int lx = gridData.getCol()- width; lx >0; lx-- ){
//
//
//                /**
//                 * Top right starting at first valid tile to the left
//                 *
//                 *  X,0,0,0
//                 *  0,0,0,0
//                 *  0,0,0,0
//                 *  0,0,0,0
//                 *
//                 *  Trying to watch for this
//                 *  0,0,0,0
//                 *  0,X,X,0
//                 *  0,X,X,0,0,0
//                 *  0,0,0,0,0,0
//                 *      0,0,0,0
//                 *      0,0,0,0
//                 *
//                 */
//
//
//
//
//                for(int ly = startY; ly > 0; ly-- ){
//
//                    int offsetX = lx - internalX;
//                    int offsetY = ly - internalY;
//
//                    if(partOffsetCollision(tetromino,offsetX,offsetY,rotation)){
//
//                        Row[] gridRows = (Row[]) SerializationUtils.clone(gridData.data());
//
//                        int result = add(gridRows, tetromino, rotation, offsetX,offsetY);
//
//                        if(result == PLACE_OUT_BOUNDS){
//                            System.out.println("Result " + result+" position "+offsetX+" "+offsetY);
//                        }
//
//                        if(result <= PLACE_ROW_FILLED){
//                            byte[] heightData = new byte[gridData.getCol()];
//                            double boardScore = rateBoard(gridRows,heightData);
//                            if (boardScore<bestScore) {
//                                bestScore = boardScore;
//
//                                if(bestMove == null)
//                                    bestMove = new Move();
//
//                                bestMove.set(offsetX,offsetY, rotation,tetromino.data,boardScore);
//
//                            }
//                        }
//
//                        break;
//                    }
//                }
//
//
//
//
//
//            }
//
//        }
//
//        if(bestMove != null){
//            System.out.println(bestMove.x+" : "+bestMove.y);
//        } else {
//            System.out.println("No best move was found "+bestMove);
//        }


        return prediction;
    }

    private void recomputeMaxHeight() {
        maxHeight = 0;
        for (int i = 0; i < gridData.getCol(); i++) {
            int height = gridData.getDataHeightAt(i);
            if (maxHeight < height)
                maxHeight = height;
        }
    }


    private double rateBoard(Row[] gridRows, byte[] heightData) {
        final int cols = gridData.getCol();

        heightData = updateHeights(gridRows, heightData);
        for (int i = 0; i < cols; i++) {
            int height = heightData[i];
            if (maxHeight < height)
                maxHeight = height;
        }

        int sumHeight = 0;
        int holes = 0;// getHoles();


        double avgHeight = ((double) sumHeight) / cols;

        // Add up the counts to make an overall score
        // The weights, 8, 40, etc., are just made up numbers that appear to work
        return (8 * maxHeight + 40 * avgHeight + 1.25 * holes);
    }

    private void remove(final GridData gridData, Tetromino tetromino, int rotation, int ax, int ay) {
        final byte[][] data = tetromino.getShape();

        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[y].length; x++) {

                if (data[y][x] > 0) {

                    int b_x = ax + x - tetromino.data.getX(rotation);
                    int b_y = ay + y - tetromino.data.getY(rotation);

                    if (b_x < 0 || b_x >= gridData.getCol() || b_y < 0 || b_y >= gridData.getRow())
                        continue;

                    gridData.data()[b_y].getData()[b_x] = 0;

                }

            }
        }
    }

    private static final byte PLACE_OK = 0;
    private static final byte PLACE_ROW_FILLED = 1;
    private static final byte PLACE_OUT_BOUNDS = 2;
    private static final byte PLACE_BAD = 3;

    ObjectSet<Integer> fullRows = new ObjectSet<Integer>();

    private int add(final Row[] gridRows, Tetromino tetromino, int rotation, int ax, int ay) {

        fullRows.clear();
        final byte[][] data = tetromino.data.getData()[rotation];

        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[y].length; x++) {

                if (data[y][x] > 0) {

                    int b_x = ax + x - tetromino.data.getX(rotation);
                    int b_y = ay + y - tetromino.data.getY(rotation);

                    if (b_x < 0 || b_x >= gridData.getCol() || b_y < 0 || b_y >= gridData.getRow()) {
                        return PLACE_OUT_BOUNDS;
                    }

                    if (gridRows[b_y].getData()[b_x] > 0) {
                        return PLACE_BAD;
                    }

                }

            }
        }

        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[y].length; x++) {

                if (data[y][x] > 0) {

                    int b_x = ax + x - tetromino.data.getX(rotation);
                    int b_y = ay + y - tetromino.data.getY(rotation);

                    gridRows[b_y].getData()[b_x] = data[y][x];
                    if (gridRows[b_y].isFull())
                        fullRows.add(b_y);

                }

            }
        }

        if (fullRows.size > 0) return PLACE_ROW_FILLED;
        return PLACE_OK;

    }

    private boolean partOffsetCollision(Tetromino tetromino, int ax, int ay, int rotation) {

        final byte[][] dat = tetromino.getShape();

        for (int y = 0; y < dat.length; y++) {
            for (int x = 0; x < dat[y].length; x++) {

                if (dat[y][x] > 0) {

                    int b_x = ax + x - tetromino.data.getX(rotation);
                    int b_y = ay + y - tetromino.data.getY(rotation);

                    byte value = getValueAt(b_x, b_y);

                    if (value > 0) {
                        return true;
                    }

                }

            }
        }

        return false;
    }

    private static final byte wallCollision = 1, floorCollision = 2, partCollision = 3, noCollision = 0;
    private static ObjectSet<Move> prediction = new ObjectSet<Move>();
    private static ObjectSet<Move> validTiles = new ObjectSet<Move>();

    private ObjectSet<Move> getAllMoves(GridData gridData, Tetromino part) {
        Tetromino tetromino = new Tetromino(part);
        tetromino.setAbsX(part.getAbsX());
        tetromino.setAbsY(part.getAbsY());

        validTiles.clear();

////        try all rotations. Width and height is based on shape of pieces so rotation happends first
////        for(int rotation = 0; rotation < 3; rotation++){
//
//
////            start from  upper right hand corner and move to lower lefte hand
        for (int rotation = 0; rotation < 4; rotation++) {

            tetromino.setRotation(rotation);
//
//            int y = lookDown(gridData.data(),tetromino,rotation);
            int furthestRight = lookRight(gridData.data(), tetromino, rotation);
            int furthestLeft = lookLeft(gridData.data(), tetromino, rotation);

            int x = (int) tetromino.getAbsX();

            int xOffset = (int) tetromino.data.getX(rotation);
            int yOffset = (int) tetromino.data.getY(rotation);

            while (x >= 0) {

                int y = (int) tetromino.getAbsY();



                do {

                    boolean open = isOpen(x, y, gridData.data(), false) == 1;
                    boolean collision = partOffsetCollision(tetromino, x, y, rotation);
                    boolean canLand = partOffsetCollision(tetromino, x, y - 1, rotation);

//                    boolean canLand = collision(x,y-1,rotation,partData);


                    if (open && !collision && canLand) {



                        /**
                         *  The Algorithm
                         **/

                        Algorithm algorithm = new Algorithm(
                                  getLinesCleared()
                                , (float) Math.pow(getMaxColumnHeight(x, y, rotation, tetromino.data), 1.5)
                                , getCumulativeHeight(x, y, rotation, tetromino.data)
                                , getRelativeHeight(x, y, rotation,tetromino. data)
                                , getHoles(x, y, rotation, tetromino.data)
                                , getRoughness(x, y, rotation, tetromino.data)
                        );
                        gridData.removeShape(x - xOffset , y - yOffset , rotation, tetromino.data);
//
                        float rating = 0;
                        rating += algorithm.getRowsCleared() * genomes.get(currentGenome).getRowsCleared();
                        rating += algorithm.getWeightedHeight() *  genomes.get(currentGenome).getWeightedHeight();
                        rating += algorithm.getCumulativeHeight() * genomes.get(currentGenome).getCumulativeHeight();
                        rating += algorithm.getRelativeHeight() * genomes.get(currentGenome).getRelativeHeight();
                        rating += algorithm.getHoles() * genomes.get(currentGenome).getHoles();
                        rating += algorithm.roughness * genomes.get(currentGenome).getRoughness();

//                        double moveScore = getScore(gridData,x - tetromino.data.getX(rotation), y - tetromino.data.getY(rotation), rotation, tetromino.data);
                        Move move = new Move();
                        move.set(x, y, rotation, tetromino.data, rating, algorithm);
                        validTiles.add(move);
                        System.out.println("score "+rating);
                    }

                    y--;

                } while (y > -4);


                x--;

            }

            while (x <= gridData.getRow()) {

                int y = (int) tetromino.getAbsY();

                do {

                    boolean open = isOpen(x, y, gridData.data(), false) == 1;
                    boolean collision = partOffsetCollision(tetromino, x, y, rotation);
                    boolean canLand = partOffsetCollision(tetromino, x, y - 1, rotation);

                    //                    boolean canLand = collision(x,y-1,rotation,partData);


                    if (open && !collision && canLand) {

                        /**
                         *  The Algorithm
                         **/

                        Algorithm algorithm = new Algorithm(
                                  getLinesCleared()
                                ,(float) Math.pow(getMaxColumnHeight(x, y, rotation, tetromino.data), 1.5)
                                , getCumulativeHeight(x, y, rotation, tetromino.data)
                                , getRelativeHeight(x, y, rotation,tetromino. data)
                                , getHoles(x, y, rotation, tetromino.data)
                                , getRoughness(x, y, rotation, tetromino.data)
 				        );
                        gridData.removeShape(x - xOffset , y - yOffset , rotation, tetromino.data);

                        float rating = 0;
                        rating += algorithm.getRowsCleared() * genomes.get(currentGenome).getRowsCleared();
                        rating += algorithm.getWeightedHeight() *  genomes.get(currentGenome).getWeightedHeight();
                        rating += algorithm.getCumulativeHeight() * genomes.get(currentGenome).getCumulativeHeight();
                        rating += algorithm.getRelativeHeight() * genomes.get(currentGenome).getRelativeHeight();
                        rating += algorithm.getHoles() * genomes.get(currentGenome).getHoles();
                        rating += algorithm.roughness * genomes.get(currentGenome).getRoughness();

//                        double moveScore = getScore(gridData,x - tetromino.data.getX(rotation), y - tetromino.data.getY(rotation), rotation, tetromino.data);
                        Move move = new Move();
                        move.set(x, y, rotation, tetromino.data, rating, algorithm);
                        validTiles.add(move);
                        System.out.println("score "+rating);
                    }

                    y--;

                } while (y > -4);


                x++;

            }
        }

        Move bestMove = null;
        double bestScore = Integer.MIN_VALUE;
        for(Move move : validTiles){
            if(move.score > bestScore){
                bestMove = move;
                bestScore = move.score;
            }
        }
        validTiles.clear();
        validTiles.add(bestMove);
        gridData.addMove(bestMove);

////


////            tetromino.setAbsX(furthestLeft);
////            int firstBottom = searchLandings(gridData.data(),tetromino,rotation);

//            Move move1 = new Move();
//            move1.set((int) tetromino.getAbsX(), y,rotation,tetromino.data,0);
//            validTiles.add(move1);
//
//            Move move2 = new Move();
//            move2.set(furthestRight, (int) tetromino.getAbsY(),rotation,tetromino.data,0);
//            validTiles.add(move2);

//            for(int rot = 0; rot < 4; rot++){
//                for(int x = 0; x < gridData.getCol(); x++){
//                    for(int y = 0; y < gridData.getRow(); y++){
//
//                        Tetromino tetromino = new Tetromino(part);
//                        tetromino.setAbsX(x);
//                        tetromino.setAbsY(y);
//                        tetromino.setRotation(rot);
//
//                        boolean open = isOpen(x, y, gridData.data(),false) == 1;
//                        boolean collision = partOffsetCollision(tetromino, x, y, rot);
//
//                        if(!open || collision){
//                            Move move1 = new Move();
//                            move1.set(x, y,rot,tetromino.data,0);
//
//                            if(validTiles.size == 0)
//                                validTiles.add(move1);
//
//                        }
//
//                    }
//                }
//            }


//            for(int x = cols; x > 0; x--){
//
//
//
//                for(int y = rows; y > 0; y--){
//
//                    int b_x = x - tetromino.data.getX(rotation);
//                    int b_y = y - tetromino.data.getY(rotation);
//
//                    System.out.println(b_x+" "+b_y+" checked ");
//                    if( isValid(gridData.data(),b_x,b_y,tetromino,rotation) ) {
//                        if(!canFall(gridData.data(),b_x,b_y-1,tetromino,rotation)){
//                            Move move = new Move();
//                        /*
//                            NOTE: internal offset must me included in the positioning.
//                         */
//
//                            move.set(b_x,b_y,rotation,tetromino.data,0);
//                            validTiles.add(move);
//                            break;
//                        }
//                    }
//
//                }
//            }
//
//
//        }
//        System.out.println("Get all moves returned "+validTiles.size+" moves");


        return prediction;
    }

    private int getLinesCleared() {
        int cleared = 0;
        for(int r = 0; r < gridData.getRow(); r++){
            if(gridData.getRow(r).isFull())
                cleared++;
        }
        return cleared;
    }

    private double getScore(GridData gridData, int x, int y, int rotation, PuzzleElement.PartData data) {

        gridData.addShape(x, y, rotation, data);
        byte[] heightData = new byte[gridData.getCol()];
        double scoreMove = rateBoard(gridData.data(),heightData);
        gridData.removeShape(x, y, rotation, data);

        return scoreMove;
    }

    private int lookDown(Row[] rows, Tetromino tetromino, int rotation) {
        int tx = (int) tetromino.getAbsX();
        int ty = (int) tetromino.getAbsY();

        do {

            boolean open = isOpen(tx, ty - 1, rows, false) == 1;
            boolean collision = partOffsetCollision(tetromino, tx, ty - 1, rotation);

            if (!open || collision) {
                return ty;
            }
            ty--;

        } while (true);
    }

    private int searchLandings(Row[] rows, Tetromino tetromino, int rotation) {
        int tx = (int) tetromino.getAbsX();
        int ty = (int) tetromino.getAbsY();

        do {

            boolean open = isOpen(tx, ty - 1, rows, false) == 1;
            boolean collision = partOffsetCollision(tetromino, tx + 1, ty, rotation);

            if (!open || collision) {
                return tx;
            }
            tx++;

        } while (true);

    }

    private int lookRight(Row[] rows, Tetromino tetromino, int rotation) {
        int tx = (int) tetromino.getAbsX();
        int ty = (int) tetromino.getAbsY();


        do {

            boolean open = isOpen(tx + 1, ty, rows, false) == 1;
            boolean collision = partOffsetCollision(tetromino, tx + 1, ty, rotation);

            if (!open || collision) {
                return tx;
            }
            tx++;

        } while (true);

    }

    private int lookLeft(Row[] rows, Tetromino tetromino, int rotation) {
        int tx = (int) tetromino.getAbsX();
        int ty = (int) tetromino.getAbsY();
        do {

            boolean open = isOpen(tx - 1, ty, rows, false) == 1;
            boolean collision = partOffsetCollision(tetromino, tx - 1, ty, rotation);

            if (!open || collision) {
                return tx;
            }
            tx--;

        } while (true);


    }

    private boolean isValid(Row[] rows, int nextX, int nextY, Tetromino tetromino, int rotation) {
        int collision = 0;
        byte[][] tshape = tetromino.data.getData()[rotation];


        for (int y = 0; y < tshape.length; y++) {
            for (int x = 0; x < tshape[y].length; x++) {

                if (tshape[y][x] > 0) {

                    int b_x = nextX + x;
                    int b_y = nextY + y;
                    if (b_y >= rows.length || b_x >= rows[b_y].getData().length) {
                        return false;
                    }


                }


            }
        }

        return true;
    }

    private byte isOpen(Row[] rows, int nextX, int nextY, Tetromino tetromino, int rotation) {
        byte[][] tshape = tetromino.data.getData()[rotation];

        for (int y = 0; y < tshape.length; y++) {
            for (int x = 0; x < tshape[y].length; x++) {

                if (tshape[y][x] > 0) {

                    int b_x = nextX + x;
                    int b_y = nextY + y;

                    short value = isOpen(b_x, b_y, rows, true);
                    switch (value) {
                        case -1:
                            return PLACE_OUT_BOUNDS;
                        case 1:
                            return PLACE_BAD;
                    }

                }

            }
        }


        return PLACE_OK;
    }

    private short isOpen(int x, int y, Row[] rows, boolean requireOpen) {
        if (y < 0 || y >= rows.length || x < 0 || x >= rows[y].getData().length) return -1;
        if (requireOpen)
            if (rows[y].getData()[x] == 0) return 0;
        return 1;
    }

    private static boolean isInBounds(Tetromino tetromino, int px, int py, GridData gridData) {
        for (int y = 0; y < tetromino.getShape().length; y++) {
            for (int x = 0; x < tetromino.getShape()[y].length; x++) {

                if (tetromino.getShape()[y][x] > 0) {

                    int b_x = px + x - tetromino.data.getX(tetromino.getRotation());
                    int b_y = py + y - tetromino.data.getY(tetromino.getRotation());

                    if (b_x < 0 || b_x >= gridData.getCol() || b_y < 0 || b_y >= gridData.getRow())
                        return false;

                }


            }
        }
        return true;
    }

    private static boolean hasCollision(int tx, int ty, Tetromino tetromino, GridData gridData, Byte... type) {
        final List<Byte> list = Arrays.asList(type);
        byte[][] dat = tetromino.getShape();
        for (int y = 0; y < dat.length; y++) {
            for (int x = 0; x < dat[y].length; x++) {

                if (dat[y][x] > 0) {

                    int b_x = tx + x - tetromino.data.getX(tetromino.getRotation());
                    int b_y = ty + y - tetromino.data.getY(tetromino.getRotation());

                    if (b_x < 0 || b_x >= gridData.getCol() && list.contains(wallCollision))
                        return true;

                    if (b_y < 0 || b_y >= gridData.getRow() && list.contains(floorCollision))
                        return true;

                    if (gridData.getDataValueAt(b_x, b_y) > 0 && list.contains(partCollision))
                        return true;

                }


            }
        }
        return false;
    }

    private static int getHighest(int rows, int x, Row[] rowData) {

        for (int y = rows - deadZone; y > 0; y--)
            if (rowData[y].getData()[x] != 0)
                return y;

        return 0;
    }


//    private static Byte getCollision(int x, int y, GridData gridData,Tetromino tetromino) {
//        if(wallCollision(x,gridData) != 0)
//            return wallCollision;
//        if(floorCollision(y,gridData) != 0)
//            return floorCollision;
//        if(partCollision(x,y,gridData,tetromino.) != 0)
//            return partCollision;
//
//        return partCollision(x,y,gridData,tetromino);
//    }


    private static Byte floorCollision(int y, GridData gridData) {
        return y < 0 /*|| y >= gridData.getRow()*/ ? floorCollision : 0;
    }

    private static Byte wallCollision(int x, GridData gridData) {
        return x < 0 || x >= gridData.getCol() ? wallCollision : 0;
    }

    private static boolean isLandable(int x, int y, GridData gridData) {
        if (y <= 0) return true;
        return gridData.getDataValueAt(x, y) > 0;
    }

    private static Byte partCollision(int tx, int ty, GridData gridData, Tetromino tetromino) {
        byte[][] data = tetromino.getShape();
        PuzzleElement.PartData dataShape = tetromino.data;
        int rotation = tetromino.getRotation();

        int cols = gridData.getCol();
        int rows = gridData.getRow();

        for (int y = rows; rows > 0; y--) {
            for (int x = 0; x < cols; x++) {

                int y2 = y - 1;
                if (y2 < 0) {
                    return floorCollision;
                }

//                byte value = gridData.getDataValueAt(x, y);
//                if (value > 0) {
//
//                }

            }
        }


//        for (int y = 0; y < dat.length; y++) {
//            for (int x = 0; x < dat[y].length; x++) {
//
//                if (dat[y][x] > 0) {
//                    int b_x = tx + x;
//                    int b_y = ty + y;
//                    tSpinLocation.add(b_y);
//
//                }
//
//
//            }
//        }


//        for (int y = dataShape.getY(rotation); y < data.length; y++) {
//            for (int x = dataShape.getX(rotation); x < data[y].length; x++) {
//
//                if (data[y][x] > 0) {
//
//                    int b_x = tx + x;
//                    int b_y = ty + y;
//
//                    if(ty < 0)
//                        return floorCollision;
//
////                    if(tx < 0 || tx >= gridData.getCol() )
////                        return wallCollision;
//
////                    byte value = gridData.safeGetDataValueAt(b_x, b_y);
////                    if (value > 0) {
////                        return partCollision;
////                    }
//
//
//                }
//
//
//            }
//        }

        return noCollision;
    }

    public int getAutoValue(int x, int y) {

//      // What tile transition we should have.
        int result = 0;
        boolean up = getValueAt(x, y + 1) > 0;
        boolean right = getValueAt(x - 1, y) > 0;
        boolean down = getValueAt(x, y - 1) > 0;
        boolean left = getValueAt(x + 1, y) > 0;

        // If tile above, if tile down, etc.
        if (up) result += NORTH;
        if (right) result += EAST;
        if (down) result += SOUTH;
        if (left) result += WEST;
//
//      // This checks for diagonals
        boolean upLeft = getValueAt(x - 1, y + 1) > 0;
        boolean upRight = getValueAt(x + 1, y + 1) > 0;
        boolean downRight = getValueAt(x + 1, y - 1) > 0;
        boolean downLeft = getValueAt(x - 1, y - 1) > 0;

        if (upLeft) result += NORTH_WEST;
        if (upRight) result += NORTH_EAST;
        if (downRight) result += SOUTH_EAST;
        if (downLeft) result += SOUTH_WEST;

        return pick_tile.get(result);
    }

    public int getAutoValue(int x, int y, byte value) {

//      // What tile transition we should have.
        int result = 0;
        boolean up = getValueAt(x, y + 1) == value;
        boolean right = getValueAt(x - 1, y) == value;
        boolean down = getValueAt(x, y - 1) == value;
        boolean left = getValueAt(x + 1, y) == value;

        // If tile above, if tile down, etc.
        if (up) result += NORTH;
        if (right) result += EAST;
        if (down) result += SOUTH;
        if (left) result += WEST;
//
//      // This checks for diagonals
        boolean upLeft = getValueAt(x - 1, y + 1) == value;
        boolean upRight = getValueAt(x + 1, y + 1) == value;
        boolean downRight = getValueAt(x + 1, y - 1) == value;
        boolean downLeft = getValueAt(x - 1, y - 1) == value;

        if (upLeft) result += NORTH_WEST;
        if (upRight) result += NORTH_EAST;
        if (downRight) result += SOUTH_EAST;
        if (downLeft) result += SOUTH_WEST;

        return pick_tile.get(result);
    }



    /*
     A simple brain function.
     Given a board, produce a number that rates
     that board position -- larger numbers for worse boards.
     This version just counts the height
     and the number of "getHoles()" in the board.
     */
//    public static double rateBoard(final GridData gridData) {
//        final int width = gridData.getCol();
//        final int maxHeight = gridData.getMaxHeight();
//
//        int sumHeight = 0;
//        int getHoles() = 0;
//
//        // Count the getHoles(), and sum up the heights
//        for (int x=0; x<width; x++) {
//            final int colHeight = board.getColumnHeight(x);
//            sumHeight += colHeight;
//
//            int y = colHeight - 2;    // addr of first possible hole
//
//            while (y>=0) {
//                if  (!board.getGrid(x,y)) {
//                    getHoles()++;
//                }
//                y--;
//            }
//        }
//
//        double avgHeight = ((double)sumHeight)/width;
//
//        // Add up the counts to make an overall score
//        // The weights, 8, 40, etc., are just made up numbers that appear to work
//        return (8*maxHeight + 40*avgHeight + 1.25*getHoles());
//    }


    private final Pool<Move> movesPool = new Pool<Move>() {
        @Override
        protected Move newObject() {
            return new Move();
        }
    };

    public static class Move implements Pool.Poolable {

        public PuzzleElement.PartData partData;
        public int y;
        public int x;
        public int rotation;
        public double score;
        private Algorithm algorithm;


        public Move() {

        }

        public Move(int x, int y, int rotation, PuzzleElement.PartData partData, double score, Algorithm algorithm) {
            this.x = x;
            this.y = y;
            this.rotation = rotation;
            this.partData = partData;
            this.score = score;
            this.algorithm = algorithm;
        }

        public Move set(int x, int y, int rotation, PuzzleElement.PartData partData, double score, Algorithm algorithm) {
            this.x = x;
            this.y = y;
            this.rotation = rotation;
            this.partData = partData;
            this.score = score;
            this.algorithm = algorithm;
            return this;
        }


        @Override
        public boolean equals(Object o) {

            if (o instanceof Move) {
                Move location = (Move) o;
                if (location.x == x && location.y == y && location.rotation == rotation && location.partData == partData && location.score == score)
                    return true;
            }

            return false;
        }

        @Override
        public void reset() {
            x = -1;
            y = -1;
            partData = null;
            score = 0;
        }
    }

    private int getHoles(int tx, int ty, int rotation, PuzzleElement.PartData data) {
        removeShape(tx,ty,rotation,data);
        int[] peaks = new int[]{20,20,20,20,20,20,20,20,20,20};
        for (int row = 0; row < gridData.getRow(); row++) {
            for (int col = 0; col < gridData.getCol(); col++) {
                if (getValueAt(col,row) > 0 && peaks[col] == 20) {
                    peaks[col] = row;
                }
            }
        }
        int holes = 0;
        for (int x = 0; x < peaks.length; x++) {
            for (int y = peaks[x]; y < gridData.getRow(); y++) {
                if (getValueAt(x,y) == 0) {
                    holes++;
                }
            }
        }
        applyShape(tx,ty,rotation,data);
        return holes;
    }

    private void applyShape(int tx, int ty, int rotation, PuzzleElement.PartData data) {
        gridData.addShape(
                tx - data.getX(rotation),
                ty - data.getY(rotation),
                rotation,
                data);
    }

    private void removeShape(int tx, int ty, int rotation, PuzzleElement.PartData data) {
        gridData.removeShape(
                tx - data.getX(rotation),
                ty - data.getY(rotation),
                rotation,
                data);
    }

    float getCumulativeHeight(int tx, int ty, int rotation, PuzzleElement.PartData data) {
        removeShape(tx,ty,rotation,data);
        int[] peaks = new int[]{20,20,20,20,20,20,20,20,20,20};
        for (int row = 0; row < gridData.getRow(); row++) {
            for (int col = 0; col < gridData.getCol(); col++) {
                if (getValueAt(col,row) > 0 && peaks[col] == 20) {
                    peaks[col] = row;
                }
            }
        }
        int totalHeight = 0;
        for (int i = 0; i < peaks.length; i++) {
            totalHeight += 20 - peaks[i];
        }
        applyShape(tx,ty,rotation,data);
        return totalHeight;
    }

    /**
     * Returns an array that replaces all the getHoles() in the grid with -1.
     * @return {Array} The modified grid array.
     */
    private int[][] getHolesArray(int tx, int ty, int rotation, PuzzleElement.PartData data) {
        int[][] array = new int[gridData.getCol()][gridData.getCol()];
        removeShape(tx,ty,rotation,data);
        int[] peaks = new int[]{20,20,20,20,20,20,20,20,20,20};
        for (int row = 0; row < gridData.getRow(); row++) {
            for (int col = 0; col < gridData.getCol(); col++) {
                if (getValueAt(col,row) > 0 && peaks[col] == 20) {
                    peaks[col] = row;
                }
            }
        }
        for (int x = 0; x < peaks.length; x++) {
            for (int y = peaks[x]; y < gridData.getRow(); y++) {
                if (getValueAt(x,y) == 0){
                    array[y][x] = -1;
                }
            }
        }
        applyShape(tx,ty,rotation,data);
        return array;
    }

    /**
     * Returns the roughness of the grid.
     * @return {Number} The roughness of the grid.
     */
    private float getRoughness(int tx, int ty, int rotation, PuzzleElement.PartData data) {
        removeShape(tx,ty,rotation,data);
        int[] peaks = new int[]{20,20,20,20,20,20,20,20,20,20};
        for (int row = 0; row < gridData.getRow(); row++) {
            for (int col = 0; col < gridData.getCol(); col++) {
                if (getValueAt(col,row) > 0 && peaks[col] == 20) {
                    peaks[col] = row;
                }
            }
        }
        float roughness = 0;
        float[] differences = new float[peaks.length];
        for (int i = 0; i < peaks.length - 1; i++) {
            roughness += Math.abs(peaks[i] - peaks[i + 1]);
            differences[i] = Math.abs(peaks[i] - peaks[i + 1]);
        }
        applyShape(tx,ty,rotation,data);
        return roughness;
    }

    /**
     * Returns the range of heights of the columns on the grid.
     * @return {Number} The relative height.
     */
    private float getRelativeHeight(int tx, int ty, int rotation, PuzzleElement.PartData data) {
        removeShape(tx,ty,rotation,data);
        int[] peaks = new int[]{20,20,20,20,20,20,20,20,20,20};
        for (int row = 0; row < gridData.getRow(); row++) {
            for (int col = 0; col < gridData.getCol(); col++) {
                if (getValueAt(col,row) > 0 && peaks[col] == 20) {
                    peaks[col] = row;
                }
            }
        }
        applyShape(tx,ty,rotation,data);
        Arrays.sort(peaks);
        return peaks[peaks.length-1] - peaks[0];
    }

    /**
     * Returns the height of the biggest column on the grid.
     * @return {Number} The absolute height.
     * @param tx
     * @param ty
     * @param rotation
     * @param data
     */
    private float getMaxColumnHeight(int tx, int ty, int rotation, PuzzleElement.PartData data) {
        removeShape(tx,ty,rotation,data);
        int[] peaks = new int[]{20,20,20,20,20,20,20,20,20,20};
        for (int row = 0; row < gridData.getRow(); row++) {
            for (int col = 0; col < gridData.getCol(); col++) {
                if (getValueAt(col,row) > 0 && peaks[col] == 20) {
                    peaks[col] = row;
                }
            }
        }
        applyShape(tx,ty,rotation,data);
        Arrays.sort(peaks);
        return 20 - peaks[0];
    }

    private void evaluateNextGenome() {
        //increment index in genome array
        currentGenome++;
        //If there is none, evolves the population.
        if (currentGenome == genomes.size) {
            evolve();
        }
        //load current gamestate
        loadState(roundState);
        //reset moves taken
        movesTaken = 0;
        //and make the next move
        makeNextMove();
    }

    private Move getHighestRatedMove(Array<Move> moves) {
        //start these values off small
        int maxRating = Integer.MIN_VALUE;
        int maxMove = -1;
        Array<Integer> ties = new Array<Integer>(true,0,Integer.class);
        //iterate through the list of moves
        for (int index = 0; index < moves.size; index++) {
            //if the current moves rating is higher than our maxrating
            if (moves.get(index).score > maxRating) {
                //update our max values to include this moves values
                maxRating = (int) moves.get(index).score;
                maxMove = index;
                //store index of this move
            } else if (moves.get(index).score == maxRating) {
                //if it ties with the max rating
                //add the index to the ties array
                ties.add(index);
            }
        }
        //eventually we'll set the highest move value to this move var
        Move move = moves.get(ties.get(0));
        //and set the number of ties
        move.algorithm.ties = ties.size;
        return move;
    }

    void evolve() {

        console.log("Generation " + generation + " evaluated.");
        //reset current genome for new generation
        currentGenome = 0;
        //increment generation
        generation++;
        //resets the game
        reset();
        //gets the current game state
        roundState = getState();
        //sorts genomes in decreasing order of fitness values
        genomes.sort(function(a, b) {
            return b.fitness - a.fitness;
        });
        //add a copy of the fittest genome to the elites list
        archive.elites.push(clone(genomes[0]));
        console.log("Elite's fitness: " + genomes[0].fitness);

        //remove the tail end of genomes, focus on the fittest
        while(genomes.length > populationSize / 2) {
            genomes.pop();
        }
        //sum of the fitness for each genome
        var totalFitness = 0;
        for (var i = 0; i < genomes.length; i++) {
            totalFitness += genomes[i].fitness;
        }

        //get a random index from genome array
        function getRandomGenome() {
            return genomes[randomWeightedNumBetween(0, genomes.length - 1)];
        }
        //create children array
        var children = [];
        //add the fittest genome to array
        children.push(clone(genomes[0]));
        //add population sized amount of children
        while (children.length < populationSize) {
            //crossover between two random genomes to make a child
            children.push(makeChild(getRandomGenome(), getRandomGenome()));
        }
        //create new genome array
        genomes = [];
        //to store all the children in
        genomes = genomes.concat(children);
        //store this in our archive
        archive.genomes = clone(genomes);
        //and set current gen
        archive.currentGeneration = clone(generation);
        console.log(JSON.stringify(archive));
        //store archive, thanks JS localstorage! (short term memory)
        localStorage.setItem("archive", JSON.stringify(archive));
    }

    /**
     * Creates a child genome from the given parent genomes, and then attempts to mutate the child genome.
     * @param  {Genome} mum The first parent genome.
     * @param  {Genome} dad The second parent genome.
     * @return {Genome}     The child genome.
     */
    Genome makeChild(Genome mum, Genome dad) {
        //init the child given two genomes (its 7 parameters + initial fitness value)
        Genome child = new Genome(
                //unique id
                RandomUtils.nextInt(),
                randomChoice(mum.getRowsCleared(), dad.getRowsCleared()),
                randomChoice(mum.getWeightedHeight(), dad.getWeightedHeight()),
                randomChoice(mum.getCumulativeHeight(), dad.getCumulativeHeight()),
                randomChoice(mum.getRelativeHeight(), dad.getRelativeHeight()),
                randomChoice(mum.getHoles(), dad.getHoles()),
                randomChoice(mum.getRoughness(), dad.getRoughness()),
                -1
        );
        
        //mutation time!

        //we mutate each parameter using our mutationstep
        if (Math.random() < mutationRate) {
            child.setRowsCleared((float) (child.getRowsCleared() + Math.random() * mutationStep * 2 - mutationStep));
        }
        if (Math.random() < mutationRate) {
            child.setWeightedHeight((float) (child.getWeightedHeight() + Math.random() * mutationStep * 2 - mutationStep));
        }
        if (Math.random() < mutationRate) {
            child.setCumulativeHeight((float) (child.getCumulativeHeight() + Math.random() * mutationStep * 2 - mutationStep));
        }
        if (Math.random() < mutationRate) {
            child.setRelativeHeight((float) (child.getRelativeHeight() + Math.random() * mutationStep * 2 - mutationStep));
        }
        if (Math.random() < mutationRate) {
            child.setHoles((float) (child.getHoles() + Math.random() * mutationStep * 2 - mutationStep));
        }
        if (Math.random() < mutationRate) {
            child.setRoughness((float) (child.getRoughness() + Math.random() * mutationStep * 2 - mutationStep));
        }
        return child;
    }

    private float randomChoice(float dad, float mom) {
        return (RandomUtils.nextBoolean()) ? mom : dad;
    }
    
}
