package objects;

public class ClientMessage {
    public String message;
    public ClientMessage() {
    }

    public ClientMessage set(String message) {
        this.message = message;
        return this;
    }
}
