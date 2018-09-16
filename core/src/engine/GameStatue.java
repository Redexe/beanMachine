package engine;

public interface GameStatue {

    int falling_cycle = 0;
    int check_cycle = 1;
    int clear_cycle = 2;
    int wait = 3;
    int gameOver = 4;
    int newCycle = 5;
    int animate_removal = 6;
    int lock = 7;

    int getMode();
    void setMode(int mode);
    void changeMode(int newMode);
    void modeChanged(int oldMode, int newMode);

}
