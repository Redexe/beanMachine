package screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.esotericsoftware.kryonet.Listener;

public abstract class GameScreen extends Listener implements Screen {

    public static int TILE_SIZE = 64;

    public abstract void setAssetManager(AssetManager assetManager);
    public abstract void setSkin(Skin skin);
    public abstract void setStage(Stage stage);

}
