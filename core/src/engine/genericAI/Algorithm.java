package engine.genericAI;

public class Algorithm {

    public float rowsCleared;
    public float weightedHeight;
    public float cumulativeHeight;
    public float relativeHeight;
    public float holes;
    public float roughness;
    public int ties;

    public Algorithm(float rowsCleared, float weightedHeight, float cumulativeHeight, float relativeHeight, float holes, float roughness) {
        this.rowsCleared = rowsCleared;
        this.weightedHeight = weightedHeight;
        this.cumulativeHeight = cumulativeHeight;
        this.relativeHeight = relativeHeight;
        this.holes = holes;
        this.roughness = roughness;
    }

    public float getRowsCleared() {
        return rowsCleared;
    }

    public void setRowsCleared(float rowsCleared) {
        this.rowsCleared = rowsCleared;
    }

    public float getWeightedHeight() {
        return weightedHeight;
    }

    public void setWeightedHeight(float weightedHeight) {
        this.weightedHeight = weightedHeight;
    }

    public float getCumulativeHeight() {
        return cumulativeHeight;
    }

    public void setCumulativeHeight(float cumulativeHeight) {
        this.cumulativeHeight = cumulativeHeight;
    }

    public float getRelativeHeight() {
        return relativeHeight;
    }

    public void setRelativeHeight(float relativeHeight) {
        this.relativeHeight = relativeHeight;
    }

    public float getHoles() {
        return holes;
    }

    public void setHoles(float holes) {
        this.holes = holes;
    }

    public float getRoughness() {
        return roughness;
    }

    public void setRoughness(float roughness) {
        this.roughness = roughness;
    }
}