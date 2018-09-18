package engine.genericAI;

import com.badlogic.gdx.utils.Json;

public class Algorithm {

    float completeLines = 0;
    float holes = 0;
    float aggregatedHeight = 0;
    float bumpiness = 0;
    float rating  =0;
    private int ties;

    public Algorithm(float completeLines, float holes, float aggregatedHeight, float bumpiness, float rating) {
        this.completeLines = completeLines;
        this.holes = holes;
        this.aggregatedHeight = aggregatedHeight;
        this.bumpiness = bumpiness;
        this.rating = rating;
    }

    public float getCompleteLines() {
        return completeLines;
    }

    public void setCompleteLines(float completeLines) {
        this.completeLines = completeLines;
    }

    public float getHoles() {
        return holes;
    }

    public void setHoles(float holes) {
        this.holes = holes;
    }

    public float getAggregatedHeight() {
        return aggregatedHeight;
    }

    public void setAggregatedHeight(float aggregatedHeight) {
        this.aggregatedHeight = aggregatedHeight;
    }

    public float getBumpiness() {
        return bumpiness;
    }

    public void setBumpiness(float bumpiness) {
        this.bumpiness = bumpiness;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        Json json = new Json();
        return json.toJson(this,this.getClass());
    }

    public float mul(Genome genome) {
        float rating = 0;
        rating += completeLines * genome.getCompleteLines();
        rating += holes *  genome.getHoles();
        rating += aggregatedHeight * genome.getAggregatedHeight();
        rating += bumpiness * genome.getBumpiness();
        return rating;
    }

    public void setTies(int ties) {
        this.ties = ties;
    }

    public int getTies() {
        return ties;
    }
}