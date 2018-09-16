package engine;

public interface Player {
    void takeHit(int hit);
    void makeHit(int hit);
    void win();
    void lose();
}
