package network;

public class GameConnection {
    public byte game;
    public byte cols;
    public byte rows;
    public byte mode;
    public byte playerCount;
    public byte playerOrder;

    public GameConnection(){

    }

    public GameConnection set(byte game, byte cols, byte rows, byte mode, byte playerOrder,byte playerCount){
        this.game = game;
        this.cols = cols;
        this.rows = rows;
        this.mode = mode;
        this.playerOrder = playerOrder;
        this.playerCount = playerCount;
        return this;
    }

}
