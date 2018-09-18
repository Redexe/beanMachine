package engine.genericAI;


import com.badlogic.gdx.utils.Json;

public class Genome {

    float completeLines = 0;
    float holes = 0;
    float aggregatedHeight = 0;
    float bumpiness = 0;
    float fitness  =0;

    public Genome() {
        this(
                (float)Math.random(),
                (float)Math.random() - 0.5f,
                (float)Math.random() - 0.5f,
                (float)Math.random() - 0.5f,-1);
    }

    public Genome(float completeLines, float holes, float aggregatedHeight, float bumpiness, float fitness) {
        this.completeLines = completeLines;
        this.holes = holes;
        this.aggregatedHeight = aggregatedHeight;
        this.bumpiness = bumpiness;
        this.fitness = fitness;
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

    public float getFitness() {
        return fitness;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }

    public Genome normalize(){
        double norm = Math.sqrt( aggregatedHeight * aggregatedHeight + completeLines * completeLines + holes * holes +  bumpiness * bumpiness);
        aggregatedHeight /= norm;
        completeLines /= norm;
        holes /= norm;
        bumpiness /= norm;
        return this;
    }

    public static Genome newGenome(){
        return new Genome().normalize();
    }

    public void mutHeight(double quantity) {
        aggregatedHeight+=quantity;
    }

    public void mutLines(double quantity) {
        completeLines+=quantity;
    }

    public void mutHoles(double quantity) {
        holes+=quantity;
    }

    public void mutBump(double quantity) {
        bumpiness+=quantity;
    }

    @Override
    public String toString() {
        Json json= new Json();
        return json.toJson(this,this.getClass());
    }
}
