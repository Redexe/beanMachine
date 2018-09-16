package engine.genericAI;


public class Genome {
    float id = 0;
    float rowsCleared = 0;
    float weightedHeight = 0;
    float cumulativeHeight = 0;
    float relativeHeight = 0;
    float holes = 0;
    float roughness = 0;
    float fitness = 0;

    public Genome(float id, float rowsCleared, float weightedHeight, float cumulativeHeight, float relativeHeight, float holes, float roughness, float fitness) {
        this.id = id;
        this.rowsCleared = rowsCleared;
        this.weightedHeight = weightedHeight;
        this.cumulativeHeight = cumulativeHeight;
        this.relativeHeight = relativeHeight;
        this.holes = holes;
        this.roughness = roughness;
        this.fitness = fitness;
    }

    public Genome() {
        this((float)Math.random(),
                (float)Math.random() - 0.5f,
                (float)Math.random() - 0.5f,
                (float)Math.random() - 0.5f,
                (float)Math.random() - 0.5f,
                (float)Math.random() * 0.5f,
                (float)Math.random() - 0.5f,-1);
    }


    public float getFitness() {
        return fitness;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }

    public float getId() {
        return id;
    }

    public void setId(float id) {
        this.id = id;
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
