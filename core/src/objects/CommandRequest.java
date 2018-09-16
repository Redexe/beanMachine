package objects;

public class CommandRequest {
    public String command;

    public CommandRequest(){

    }

    public CommandRequest set(String action, String field, String value){
        this.command = action+":"+field+":"+value;
        return this;
    }
}
