package engine;

public abstract class NpcGameStatus implements GameStatue {

    int mode;
    private int oldMode;

    @Override
    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public void changeMode(int newMode) {
        this.oldMode = mode;
        this.mode = newMode;
        this.modeChanged(oldMode,newMode);
    }

    public abstract void modeChanged(int oldMode, int newMode);


}
