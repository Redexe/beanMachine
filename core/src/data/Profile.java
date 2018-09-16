package data;

import com.badlogic.gdx.utils.Json;

public class Profile {
    String username = "no set";

    @Override
    public String toString() {
        Json json = new Json();
        return json.toJson(json);
    }
}
