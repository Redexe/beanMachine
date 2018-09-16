package engine;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

import engine.genericAI.Genome;
import objects.GridActor;

public class AI extends Action implements Steerable<Vector2> {



    public float [][] weights;
    boolean remove;
    private GridActor gridActor;
    private StateMachine<AI, AIState> stateMachine;
    private int targetRotation;
    private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());


    Vector2 position;  // like scene2d centerX and centerY, but we need a vector to implement Steerable
    Vector2 linearVelocity;
    float angularVelocity;
    float boundingRadius;
    boolean tagged;

    float maxLinearSpeed = 100;
    float maxLinearAcceleration = 200;
    float maxAngularSpeed = 5;
    float maxAngularAcceleration = 10;

    boolean independentFacing;


    SteeringBehavior<Vector2> steeringBehavior;
    private GridLocation targetLocation;
    private int currentGenome;
    private NpcGameStatus npcGamesStatus;
    private LearningAI brain;

    public AI(){
        stateMachine = new DefaultStateMachine<AI, AIState>(this, AIState.EVALUATE_GRID_WEIGHTS);
        brain = new LearningAI();

    }

    public LearningAI getBrain() {
        return brain;
    }

    public float[][] getWeights() {
        return weights;
    }

    public void setWeights(float[][] weights) {
        this.weights = weights;
    }

    public StateMachine<AI, AIState> getStateMachine() {
        return stateMachine;
    }

    @Override
    public boolean act(float delta) {

        stateMachine.update();

        return false;
    }

    public void setGridActor(GridActor gridActor) {
        this.gridActor = gridActor;
    }

    public GridActor getGridActor() {
        return gridActor;
    }

    public void setTargetRotation(int targetRotation) {
        this.targetRotation = targetRotation;
    }

    @Override
    public Vector2 getPosition () {
        return position;
    }

    @Override
    public float getOrientation () {
        return 0;
    }

    @Override
    public void setOrientation (float orientation) {

    }

    @Override
    public Vector2 getLinearVelocity () {
        return linearVelocity;
    }

    @Override
    public float getAngularVelocity () {
        return angularVelocity;
    }

    public void setAngularVelocity (float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    @Override
    public float getBoundingRadius () {
        return boundingRadius;
    }

    @Override
    public boolean isTagged () {
        return tagged;
    }

    @Override
    public void setTagged (boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public Location<Vector2> newLocation () {
        return new GridLocation();
    }

    @Override
    public float vectorToAngle (Vector2 vector) {
        return (float)Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector (Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float)Math.cos(angle);
        return outVector;
    }

    @Override
    public float getMaxLinearSpeed () {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed (float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration () {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration (float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed () {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed (float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration () {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration (float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public float getZeroLinearSpeedThreshold () {
        return 0.001f;
    }

    @Override
    public void setZeroLinearSpeedThreshold (float value) {
        throw new UnsupportedOperationException();
    }

    public boolean isIndependentFacing () {
        return independentFacing;
    }

    public void setIndependentFacing (boolean independentFacing) {
        this.independentFacing = independentFacing;
    }

    public SteeringBehavior<Vector2> getSteeringBehavior () {
        return steeringBehavior;
    }

    public void setSteeringBehavior (SteeringBehavior<Vector2> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
    }

    public void setTargetLocation(Vector2 targetLocation, int targetRotation) {

        GridLocation gridLocation = new GridLocation();
        gridLocation.setPosition(targetLocation);
        gridLocation.setOrientation(targetRotation);


        this.targetLocation = gridLocation;
    }

    public void setTargetLocation(Vector3 targetLocation) {
        GridLocation gridLocation = new GridLocation();
        gridLocation.setPosition(targetLocation);
        this.targetLocation = gridLocation;
    }

    public GridLocation getTargetLocation() {
        return targetLocation;
    }




//  Learning AI
    int generation = 0;
    Array<Genome> genomes = new Array<Genome>(true,50,Genome.class);
    public int getCurrentGenome() {
        return currentGenome;
    }

    public boolean evaluateNextGenome() {
        currentGenome++;
        return currentGenome == genomes.size ;
    }

    public void evolve(){
        currentGenome = 0;
        generation++;
        resetGenetics();
        genomes.sort(sortByFitness);
    }

    private void resetGenetics() {

    }


    private Comparator<? super Genome> sortByFitness = new Comparator<Genome>() {
        @Override
        public int compare(Genome genomeA, Genome genomeB) {
            return (int) (genomeB.getFitness() - genomeA.getFitness());
        }
    };

    public void setNpcGamesStatus(NpcGameStatus npcGamesStatus) {
        this.npcGamesStatus = npcGamesStatus;
    }

    public NpcGameStatus getNpcGamesStatus() {
        return npcGamesStatus;
    }
}