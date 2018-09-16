package managers;

import java.text.Format;

public class DataResponse {
    public byte id = Byte.MIN_VALUE;
    public String gameName ="no data";
    public String playerName="no data";
    public String serverName="no data";


    @Override
    public String toString() {
        return String.format("%s on %s  with %s ",gameName,serverName,playerName);
    }
}
