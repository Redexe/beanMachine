package managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Serialization;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.ServerDiscoveryHandler;


import java.nio.channels.DatagramChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import engine.GameConfig;
import network.GameConnection;
import network.PartUpdate;
import network.UserListUpdate;
import objects.ClientMessage;
import objects.CommandRequest;

import static com.esotericsoftware.minlog.Log.info;
import static screens.MainMenu.GENERAL_PREFERENCES;
import static screens.MainMenu.USERNAME;

public class GameServer {
    private final ArrayMap<String,Object> players = new ArrayMap<String,Object>();
    public static int TIMEOUT = 5000;
    public static int TCP = 56555,UDP = 64777;
    public static String gameName = "Unknown";
    public static String serverName = "Unknown";
    public static String username = "Unknown";

    private final static Array<Listener> listeners = new Array<Listener>();
    private final static GameServer gameServer = new GameServer();
    private Server server;
    private int connect;
    private byte packetId = Byte.MIN_VALUE;
    private Client client;
    private boolean isServer;
    private boolean started;


    private GameServer(){


    }

    private Server createServer(){
        Server server = new Server();
        final Kryo kryo = server.getKryo();
        registerKryoClasses(kryo);


        /**
         * This listener reacts to people looking for servers. Should be a few time deal.
         */
        final StringBuilder stringBuilder = new StringBuilder();
        server.setDiscoveryHandler(new ServerDiscoveryHandler() {

            @Override
            public boolean onDiscoverHost(DatagramChannel datagramChannel, InetSocketAddress fromAddress, Serialization serialization) throws IOException {
                stringBuilder.setLength(0);

//               Send info so query can choose if right server

                DataResponse packet = new DataResponse();
                packet.id = packetId;
                packet.serverName = serverName;
                packet.gameName = gameName;


                for(ObjectMap.Entry<String, Object> entry : players.entries()){
                    stringBuilder.append(entry.key).append(" , ");
                }
                packet.playerName = stringBuilder.toString();

                ByteBuffer buffer = ByteBuffer.allocate(256);
                serialization.write(null, buffer, packet);
                buffer.flip();

                datagramChannel.send(buffer, fromAddress);
                packetId++;

                return true;
            }
        });
        return server;
    }

    private Kryo registerKryoClasses(Kryo kryo) {
        kryo.setRegistrationRequired(false);
//        kryo.register(DataRequest.class);
//        kryo.register(DataResponse.class);
//        kryo.register(CommandRequest.class);
//        kryo.register(ClientMessage.class);
//        kryo.register(GameConnection.class);
//        kryo.register(UserListUpdate.class);
//        kryo.register(GameConfig.class);
//        kryo.register(PartUpdate.class);

        return kryo;
    }

    private void processCommand(final CommandRequest commandRequest) {
        System.out.println("command requested "+commandRequest);
    }

    public void addListener(Listener listener){
        listeners.add(listener);
    }

    public static GameServer getGameServer() {
        return gameServer;
    }

    public Server getServer() {
        return server;
    }

    public void start(int tcp, int udp) throws IOException {
        if(server == null) server = createServer();
        server.start();
        server.bind(tcp, udp);
        isServer = true;
        started = true;
    }

    public Client createClient(){
        if(client != null) return client;
        client = new Client();
        Kryo kryo = client.getKryo();
        registerKryoClasses(kryo);
        return client;

    }


    public void nukeClient() throws IOException {
        if(client == null) return;
        client.dispose();
        client = null;
    }


    public boolean isServer() {
        return isServer;
    }

    public String[] getUser() {
        if(server != null){
            Connection[] connections = server.getConnections();
            String[] users = new String[connections.length+1];
            users[0] = Gdx.app.getPreferences(GENERAL_PREFERENCES).getString(USERNAME);
            if(users.length > 1)
            for(int i = 1; i < users.length; i++)
                users[i] = connections[i].name;

            return users;
        }
        return null;
    }

    public boolean isStarted() {
        return started;
    }
}
