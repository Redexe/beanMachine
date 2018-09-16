package screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;

public abstract class LoadingScreen implements Screen{

    private final AssetManager assetManager;
    private final ScreenConfig config;
    private final Stage stage;
    private final Skin skin;
    private boolean paused,hid,loaded;
    private ProgressBar progressBar;
    private boolean constructed;
    private String didYouKnow;
    private Label hintLabel;

    public LoadingScreen(ArrayMap<String,Class<?>> data, ScreenConfig config, Stage stage, Skin skin){
        this.config = config;
        this.assetManager = new AssetManager();
        this.stage = stage;
        this.skin = skin;
        this.didYouKnow = "Did you know #{}";
        for(ObjectMap.Entry<String, Class<?>> entry : data.entries())
            assetManager.load(entry.key,entry.value);
    }

    public abstract void onLoaded(AssetManager assetManager);

    public void show (){
        hid = false;
        ui();
    }

    private void ui() {
        if(!constructed){

            final Table mainContainer = new Table();
            mainContainer.setFillParent(true);
            progressBar = new ProgressBar(0f,1f,0.1f,false,skin);
            mainContainer.add(progressBar);
            stage.addActor(mainContainer);

            final Table hintTable = new Table();
            hintTable.setFillParent(true);
            hintTable.bottom().center();
            hintLabel = new Label(didYouKnow,skin);
            hintTable.add(hintLabel);
            stage.addActor(hintTable);


            constructed = true;
        }
    }

    /** Called when the screen should render itself.
     * @param delta The time in seconds since the last render. */
    public void render (float delta){
        if(!hid){
            float progress = assetManager.getProgress();
            progressBar.setValue(progress);
        }

        boolean complete = assetManager.update();
        if(complete)
            onLoaded(assetManager);
    }

    /** @see ApplicationListener#resize(int, int) */
    public void resize (int width, int height){
        stage.getViewport().update(width,height);
    }

    /** @see ApplicationListener#pause() */
    public void pause (){
        paused = true;
    }

    /** @see ApplicationListener#resume() */
    public void resume (){

    }

    /** Called when this screen is no longer the current screen for a {@link Game}. */
    public void hide (){
        hid = true;
    }

    /** Called when this screen should release all resources. */
    public void dispose (){

    }


}
