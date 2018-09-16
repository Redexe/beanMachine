package engine.genericAI;

public class Result {

    private boolean lose;
    private boolean moved;
    private int rowsCleared;

    public boolean isLose() {
        return lose;
    }

    public void setLose(boolean lose) {
        this.lose = lose;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public int getRowsCleared() {
        return rowsCleared;
    }

    public void setRowsCleared(int rowsCleared) {
        this.rowsCleared = rowsCleared;
    }
}
