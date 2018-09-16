package managers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;

import engine.AndroidHelper;
import objects.Tetromino;
import screens.GameScreen;
import screens.LoadingScreen;

public class ScreenManager {

    private static ExtendViewport portrait ;
    private static ExtendViewport landscape;

    private static ScreenManager instance;
    private static AndroidHelper androidHelper;
    private Game mainScreen;
    private Skin skin;
    private Stage stage;

    private ScreenManager(){

        portrait = new ExtendViewport(768,1024);
        landscape = new ExtendViewport(1024,768);
        skin = new Skin(Gdx.files.internal("pixel/pixelUi.json"));
        stage = new Stage(landscape);
        Gdx.input.setInputProcessor(stage);

    }

    public static ScreenManager getInstance(){
        if(instance == null)
            instance = new ScreenManager();
        return instance;
    }

    public static ScreenManager init(Game mainScreen, AndroidHelper androidHelper) {
        ScreenManager.androidHelper = androidHelper;
        return getInstance().setMainScreen(mainScreen);
    }

    public static AndroidHelper getAndroidHelper() {
        return androidHelper;
    }

    public ScreenManager setMainScreen(Game mainScreen) {
        this.mainScreen = mainScreen;
        return instance;
    }

    public ScreenManager loadScreen(final ArrayMap<String, Class<?>> data, final GameScreen screen){
        final LoadingScreen loadingScreen = new LoadingScreen(data, null, stage, skin) {
            @Override
            public void onLoaded(AssetManager assetManager) {

                stage.clear();
                screen.setAssetManager(assetManager);
                screen.setSkin(skin);
                screen.setStage(stage);
                mainScreen.setScreen(screen);
            }
        };
        mainScreen.setScreen(loadingScreen);
        return instance;
    }

    private void clean(){
        skin.dispose();
        stage.dispose();
    }

    public Game getMainScreen() {
        return mainScreen;
    }

    public static Stage portrait(Stage uiStage ) {
        androidHelper.portrait();
        uiStage.setViewport(portrait);
        uiStage.getViewport().apply();
        return uiStage;
    }

    public static Stage landscape(Stage uiStage ) {
//        androidHelper.landscape();
//        uiStage.dispose();
//
//        uiStage = new Stage(landscape);
//        Gdx.input.setInputProcessor(uiStage);

        return uiStage;
    }




}
