package engine;

import com.badlogic.gdx.utils.Json;

public class GameConfig {
    public byte playerOrder;
    public byte game = 0;
    public byte cols = 10;
    public byte rows = 22;
    public byte mode = 0;
    public byte players;

    public GameConfig(byte game, byte cols, byte rows, byte mode, byte playerOrder,byte players) {
        this.game = game;
        this.cols = cols;
        this.rows = rows;
        this.mode = mode;
        this.playerOrder = playerOrder;
        this.players = players;
    }

    public GameConfig(){

    }

    public byte getGame() {
        return game;
    }

    public void setGame(byte game) {
        this.game = game;
    }

    public byte getCols() {
        return cols;
    }

    public void setCols(byte cols) {
        this.cols = cols;
    }

    public byte getRows() {
        return rows;
    }

    public void setRows(byte rows) {
        this.rows = rows;
    }

    public byte getMode() {
        return mode;
    }

    public void setMode(byte mode) {
        this.mode = mode;
    }

    public byte getPlayers() {
        return players;
    }

    public void setPlayers(byte players) {
        this.players = players;
    }

    public byte getPlayerOrder() {
        return playerOrder;
    }

    public void setPlayerOrder(byte playerOrder) {
        this.playerOrder = playerOrder;
    }

    @Override
    public String toString() {
        Json json = new Json();
        return json.toJson(this,this.getClass());
    }
}
