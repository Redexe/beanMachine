package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Timer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.ClientDiscoveryHandler;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import engine.FontUtils;
import engine.GameConfig;
import managers.DataResponse;
import managers.GameServer;
import managers.ScreenManager;
import objects.ClientMessage;
import objects.CommandRequest;
import objects.Profile;
import objects.ServerData;

public class MainMenu extends GameScreen {
    public static final String GENERAL_PREFERENCES = "general_pref";
    public static final String USERNAME ="username";
    public Array<Profile> usernames;
    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private boolean searching;
    public static final int HOST = 2;
    public static final int CLIENT = 1;
    public static final int OFFLINE = 0;

    private Stage uiStage;
    private AssetManager assetManager;
    private Skin skin;
    private boolean presentationSetup;
    private Client client;
    private InetAddress selectedAddress;
    private TextArea chatField;
    private boolean server;
    private List<String> userList;
    TextureAtlas textureAtlas;
//    private byte playerCount = -1;
    private byte playerPosition = 0;

    public MainMenu(){

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
//        int size, int xOffset, int yOffset, int border, Color color, Color shadow, Color borderColor,String url
        if(!presentationSetup){

            textureAtlas =new TextureAtlas(Gdx.files.internal("textAtlas/text.atlas"));
//          Get the number of players


//            uiStage.addActor(new Actor(){
//            });


            setup();
            presentationSetup = true;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        uiStage.act(delta);
        uiStage.draw();

        if(gameConfig != null){
            gameConfig.setPlayerOrder(playerPosition);
            ScreenManager manager = ScreenManager.getInstance();
            ArrayMap<String, Class<?>> data = new ArrayMap<String, Class<?>>();
            manager.loadScreen( data,new NetworkServerGameScreen(gameConfig));
        }
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

    private void setup() {

        usernames = new Array<Profile>(true,5,Profile.class);
        Profile profile = new Profile();
        profile.name = Gdx.app.getPreferences(GENERAL_PREFERENCES).getString(USERNAME);
        usernames.add(profile);

        final Table table = new Table(skin);
        table.setFillParent(true);
        table.center();

        /*
            Single play!
         */

        final TextButton play_text_button = new TextButton("Solo Play ",skin);

        play_text_button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

                ScreenManager manager = ScreenManager.getInstance();
                ArrayMap<String, Class<?>> data = new ArrayMap<String, Class<?>>();
                manager.loadScreen( data,new SinglePlayerGameScreen(10,22,10));


            }
        });
        table.add(play_text_button).size(250,150);
        table.row();


        /*
            Host a game
         */

        final TextButton multi_play_text_button = new TextButton("Host Play ",skin);
        multi_play_text_button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Dialog dialog = new Dialog("Players 1 - 4",skin){
                    byte players = 0;
                    @Override
                    protected void result(Object object) {
                        System.out.println(object+" players were selected");
                        if(object instanceof String){
                            String number = (String) object;
                            if(number.matches("one")){
                                players = 1;
                            } else if(number.matches("two")){
                                players = 2;
                            } else if(number.matches("three")){
                                players = 3;
                            } else if(number.matches("four")){
                                players = 4;
                            }

                            if(players > 1){
                                server = true;
                                serverDiag(players);
                            } else {
//                             Single mode...
                            }

                        }

                    }

                };
                dialog.getButtonTable().defaults().size(150);
                dialog.button("one","one");
                dialog.button("two","two");
                dialog.button("three","three");
                dialog.button("four","four");

                dialog.show(uiStage);
            }
        });

        table.add(multi_play_text_button).size(250,150);
        table.row();

        final TextButton connect_play_text_button = new TextButton("Connect Play ",skin);
        connect_play_text_button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
               searchGui();

            }
        });

        table.add(connect_play_text_button).size(250,150);
        table.row();

        uiStage.addActor(table);

    }

    private void serverDiag(final byte players) {
        GameServer gameServer = GameServer.getGameServer();

        try {

            gameServer.start(GameServer.TCP, GameServer.UDP);
            selectedAddress = InetAddress.getLocalHost();
            gameServer.getServer().addListener(serverListener);

            server = true;
            createWait(players);
//            handleUsername();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Table playerTables =new Table();
        playerTables.setFillParent(true);
        playerTables.center();
    }

    private void createWait(final byte players) {

        final Dialog dialog = new Dialog("Waiting on players...",skin){
            @Override
            protected void result(Object object) {

            }

        };

        dialog.getContentTable().defaults().size(150);

        final Image[] playerConnectionStatusImg = new Image[players];

        if(players > 0){
            playerConnectionStatusImg[0] = new Image(textureAtlas.findRegion("P1D"));
            dialog.getContentTable().add(playerConnectionStatusImg[0]);
        }
        if(players > 1){
            playerConnectionStatusImg[1] = new Image(textureAtlas.findRegion("P2D"));
            dialog.getContentTable().add(playerConnectionStatusImg[1]);
        }
        if(players > 2){
            playerConnectionStatusImg[2] = new Image(textureAtlas.findRegion("P3D"));
            dialog.getContentTable().add(playerConnectionStatusImg[2]);
        }
        if(players > 3){
            playerConnectionStatusImg[3] = new Image(textureAtlas.findRegion("P4D"));
            dialog.getContentTable().add(playerConnectionStatusImg[3]);
        }

        final TextureRegionDrawable region1 = new TextureRegionDrawable(textureAtlas.findRegion("P1"));
        final TextureRegionDrawable region2 = new TextureRegionDrawable(textureAtlas.findRegion("P2"));
        final TextureRegionDrawable region3 = new TextureRegionDrawable(textureAtlas.findRegion("P3"));
        final TextureRegionDrawable region4 = new TextureRegionDrawable(textureAtlas.findRegion("P4"));

        uiStage.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                Server gameServer = GameServer.getGameServer().getServer();
                int connections = gameServer.getConnections().length;
                if(GameServer.getGameServer().isStarted()){
                   playerConnectionStatusImg[0].setDrawable(region1);
                }
                if(connections > 0){
                    playerConnectionStatusImg[1].setDrawable(region2);
                }
                if(connections > 1){
                    playerConnectionStatusImg[2].setDrawable(region3);
                }
                if(connections > 2){
                    playerConnectionStatusImg[3].setDrawable(region4);
                }

                boolean done = connections >= players-1;
                if(done){
                    dialog.hide();
                    startServerGame(players);

                }
                return done;
            }
        });

        dialog.button("Cancel");
        dialog.show(uiStage);
    }

    private void startServerGame(byte players) {

        byte game = 1;
        byte cols = 10;
        byte rows = 22;
        byte mode = 1;

        GameConfig gameConfig = new GameConfig(game,cols,rows,mode,(byte)1,players);
        GameServer.getGameServer().getServer().sendToAllTCP(gameConfig);

//      start game screen transition
        ScreenManager manager = ScreenManager.getInstance();
        ArrayMap<String, Class<?>> data = new ArrayMap<String, Class<?>>();
        manager.loadScreen( data,new NetworkServerGameScreen(gameConfig));

    }

    private void createChat() {

        if(server) GameServer.getGameServer().getServer().sendToAllTCP(new CommandRequest().set("create","username",""));
        else handleUsername();

        final Table table = new Table();
        table.setFillParent(true);
        table.center();

        final Table headTable = new Table();
        headTable.setWidth(500);
//      List
        userList = new List<String>(skin);
        final ScrollPane listScrollPane = new ScrollPane(userList);
        headTable.add(listScrollPane).size(200);
//      Panel
        final Table gameTable = new Table(skin);
        gameTable.setFillParent(true);
        gameTable.defaults().size(250);

        final ScrollPane gameImageSelector = new ScrollPane(gameTable);

        gameTable.add(new TextButton("Game",skin));
        gameTable.add(new TextButton("Game",skin));
        gameTable.add(new TextButton("Game",skin));
        gameTable.add(new TextButton("Game",skin));
        gameTable.add(new TextButton("Game",skin));
        gameTable.add(new TextButton("Game",skin));
        headTable.add(gameImageSelector).size(300).fill();
        table.add(headTable).colspan(3);
        table.row();


//      Text area
        chatField = new TextArea("SERVER INFO",skin);
        chatField.setDisabled(true);
        final ScrollPane scrollPane =new ScrollPane(chatField);
        table.add(scrollPane).width(500).height(200).colspan(3);
        table.row();

//      message area
        final TextField textField = new TextField(" ",skin);
        final Label chatLbl = new Label("Chat",skin);
        table.add(chatLbl);
        table.add(textField).width(400);
        final TextButton send = new TextButton("Send",skin);
        table.add(send);
        final TextButton startGame = new TextButton("Start",skin);
        table.row();

        if(server)
        table.add(startGame).colspan(3);

        final String username = Gdx.app.getPreferences(GENERAL_PREFERENCES).getString(USERNAME);

        send.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if(!server) {

//                  Sends out message so server can it can be posted to chat
                    if(textField.getText().isEmpty()) return;
                    client.sendTCP(new ClientMessage().set(textField.getText().trim()));
                    textField.setText("");

                    Gdx.app.getPreferences(GENERAL_PREFERENCES).flush();

                }else{
//                  Server chat only sends to others. but posts it chat directly to Area text

                    String message = textField.getText().trim();
                    message = username +":"+message;

                    stringBuilder.append(message).append("\n");
                    chatField.setText(stringBuilder.toString());
                    GameServer.getGameServer().getServer().sendToAllTCP(new ClientMessage().set(message));
                    textField.setText("");

                    Gdx.app.getPreferences(GENERAL_PREFERENCES).flush();
                }

            }
        });

        uiStage.addActor(table);
    }

    private void searchGui() {
        if(client == null) client = GameServer.getGameServer().createClient();

        final List<ServerData> list = new List<ServerData>(skin);
        final ScrollPane scrollPane = new ScrollPane(list);
        final HashSet<ServerData> results = new HashSet<ServerData>();

        Table container = new Table(skin);
        container.setFillParent(true);
        container.center();

//        scheduler.scheduleAtFixedRate(searchRunnable, 0, 10, TimeUnit.SECONDS);

        Dialog dialog = new Dialog("Network Games",skin){
            @Override
            protected void result(Object object) {
                if(object instanceof String){
                    String value = (String) object;
                    if(value.matches("refresh")){
//                        clear list for fresh connects
                        results.clear();
                        super.cancel();
                        if(!searching){
                            executorService.submit(searchRunnable);
                            searching = true;
                        }


                    }else if(value.matches("okay")){
                        final ServerData selected = list.getSelected();
                        if(selected == null){
//                            todo: Warn user
                            return;
                        }

                        selectedAddress = selected.getAddress();
//                      on connect, try and set the username
                        hide();
                        if(!client.isConnected())
                            executorService.submit(attemptConnect);

                    }
                }

            }


        };
        container.add(dialog);
        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }
        });

        Table contentTable = new Table();
        contentTable.add(scrollPane).height(150).width(250);
        dialog.getContentTable().add(contentTable);

        uiStage.addActor(container);
        dialog.button("Refresh","refresh");
        dialog.button("Okay","okay");


        final ClientDiscoveryHandler clientDiscoveryHandler = new ClientDiscoveryHandler() {
            private Input input = null;

            @Override
            public DatagramPacket onRequestNewDatagramPacket() {
                byte[] buffer = new byte[1024];
                input = new Input(buffer);
                return new DatagramPacket(buffer, buffer.length);
            }

            @Override
            public void onDiscoveredHost(DatagramPacket datagramPacket, Kryo kryo) {
                if (input != null) {
                    DataResponse packet;
                    packet = (DataResponse) kryo.readClassAndObject(input);
                    ServerData serverData = new ServerData(packet,datagramPacket.getAddress());

                    results.add( serverData );

                    try{

                        final ServerData[] array = results.toArray(new ServerData[results.size()]);
                        list.setItems(array);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                searching = false;
            }

            @Override
            public void onFinally() {
                if (input != null) {
                    input.close();
                }
            }


        };

        client.setDiscoveryHandler(clientDiscoveryHandler);
        client.addListener(clientListener);


    }

    private Callable<java.util.List<InetAddress>> searchRunnable = new Callable<java.util.List<InetAddress>>(){
        @Override
        public java.util.List<InetAddress> call() {
            java.util.List<InetAddress> hosts = client.discoverHosts(GameServer.UDP, GameServer.TIMEOUT);
            return hosts;
        }
    };

    private Runnable attemptConnect = new Runnable() {
        @Override
        public void run() {
            try {

                client.start();
                client.connect(GameServer.TIMEOUT, selectedAddress,GameServer.TCP,GameServer.UDP);
                client.update(GameServer.TIMEOUT);

//              So we get Gdx thread...
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        clientWait();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    private void clientWait() {

        final boolean[] cancel = new boolean[1];
        final TextureRegionDrawable noConnect = new TextureRegionDrawable(textureAtlas.findRegion("PC"));
        final Image image = new Image(noConnect);

        final Dialog dialog = new Dialog("Connecting...",skin){
            @Override
            protected void result(Object object) {
                if(object instanceof String){
                    String command = (String) object;
                    if(command.matches("cancel"))
                        cancel[0] = true;
                }
            }
        };

        dialog.getContentTable().defaults().size(150);
        dialog.getContentTable().add(image);
        dialog.button("cancel","cancel");
        dialog.show(uiStage);

        final TextureRegionDrawable region1 = new TextureRegionDrawable(textureAtlas.findRegion("P1"));
        final TextureRegionDrawable region2 = new TextureRegionDrawable(textureAtlas.findRegion("P2"));
        final TextureRegionDrawable region3 = new TextureRegionDrawable(textureAtlas.findRegion("P3"));
        final TextureRegionDrawable region4 = new TextureRegionDrawable(textureAtlas.findRegion("P4"));

        /*Client reaction to connection...*/
        uiStage.addAction(new Action() {
            boolean connected = false;
            float delay = 1,time;
            @Override
            public boolean act(float delta) {
//              Delay is just to let system breath
                time+=delta;
                if(time > delay) {

                    if(playerPosition <= 0){
                        image.setDrawable(noConnect);
                        connected = false;
                    }else if(playerPosition == 1){
                        image.setDrawable(region1);
                        connected = true;
                    }else if(playerPosition == 2){
                        image.setDrawable(region2);
                        connected = true;
                    }else if(playerPosition == 3){
                        image.setDrawable(region3);
                        connected = true;
                    }else if(playerPosition == 4){
                        image.setDrawable(region4);
                        connected = true;
                    }

                    if(client.isConnected()){
                        dialog.getTitleLabel().setText("Player "+playerPosition);

                    }else{
                        dialog.getTitleLabel().setText("Connecting...");
                        image.setDrawable(noConnect);
                    }

                    if(connected){
                        executorService.shutdownNow();
                        hide();
                    }

                    time -= delay;
                }

                return connected || cancel[0];
            }
        });
    }

    final StringBuilder stringBuilder = new StringBuilder();

    private void handleUsername() {

        String storedUsername = Gdx.app.getPreferences(GENERAL_PREFERENCES).getString(USERNAME);
        if(storedUsername == null ){
            createUserNameGui();
        } else if(storedUsername.isEmpty()){
            createUserNameGui();
        } else {
            if(!server){
                client.sendTCP(new CommandRequest().set("set","username",storedUsername));
            }
        }
    }

    private void createUserNameGui() {
        Table table = new Table();
        table.setFillParent(true);
        table.top().center();

        final TextField textField = new TextField("",skin);
        Dialog window = new Dialog("Set Name",skin){
            @Override
            protected void result(Object object) {
                if(object instanceof String){
                    String value = (String) object;
                    if(value.matches("set")){
//                          fixme check if okay text
                        if(textField.getText().isEmpty())
                            return;

                        String usr = textField.getText().trim();
                        Gdx.app.getPreferences(GENERAL_PREFERENCES).putString(USERNAME,usr);
                        Gdx.app.getPreferences(GENERAL_PREFERENCES).flush();

                        if(!server) client.sendTCP(new CommandRequest().set("set","username",usr));
                        textField.setText("");

                        createChat();
                    }else if(value.matches("cancel")){

                        if(!server) client.sendTCP(new CommandRequest().set("set","username","Random "+hashCode()));
                        textField.setText("");
                        createChat();
                    }
                }

            }
        };
        window.getContentTable().add(new Label("What do we call you?",skin));
        window.getContentTable().row();
        window.getContentTable().add(textField);

        window.button("Cancel","cancel");
        window.button("Set","set");

        window.pack();

        table.add(window).size(400,300);
        table.toFront();
        uiStage.addActor(table);
    }

    private GameConfig gameConfig;
    final Listener clientListener =  new Listener(){

        public void connected (Connection connection) {
        }

        public void disconnected (Connection connection) {

        }

        public void received (Connection connection, final Object object) {

            if(object instanceof CommandRequest){
                CommandRequest commandRequest = (CommandRequest) object;
                if(commandRequest.command.contains("set")){
                    if(commandRequest.command.contains("player")){
                        playerPosition = Byte.valueOf(commandRequest.command.split(":")[2]);
                    }
                }
            }

            else if(object instanceof GameConfig){
                gameConfig = (GameConfig) object;
            }
        }

        public void idle (Connection connection) {

        }

    };

    private Listener serverListener = new Listener() {
        @Override
        public void connected(Connection connection) {
            connection.sendTCP(new CommandRequest().set("set","player",String.valueOf(GameServer.getGameServer().getServer().getConnections().length + 1)));
        }

        public void received (Connection connection, Object object) {

        }

        @Override
        public void disconnected(Connection connection) {


        }
    };


}
