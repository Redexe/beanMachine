package engine;

public interface GameEvent {
    boolean isDone();
    void act(float delta);
}
