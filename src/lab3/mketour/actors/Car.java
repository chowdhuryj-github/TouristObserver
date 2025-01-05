/*
 * Course: SWE2410
 * Fall 2024
 * Lab 3 - Tourist Observer
 * Name: Jawadul Chowdhury
 * Submission Date: 9/23/24
 */
package lab3.mketour.actors;

import javafx.scene.image.Image;
import lab3.mketour.CityMap;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * A randomly-driving car.
 *
 * The car drives in straight lines and randomly turns occasionally, always right.
 *
 */
public class Car extends MobileEntity {

    /**
     * Average number of pixels that a car drives straight before turning right. See comments on
     * step().
     */
    public static final int MEAN_DISTANCE_BEFORE_TURN = 100;

    /** Number of letters in the alphabet */
    public static final int NUM_LETTERS = 26;

    private static final Image CAR_IMAGE =
            new Image(CityMap.class.getResource("img/car.png").toString());


    /** Number of letters in a car's license plate */
    private static final int PLATE_LENGTH = 5;


    /**
     Total possible number of plates
     *  -10 margin to reduce risk of floating-point error leading to an infinite loop in
     *  findUniquePlate*/
    private static final int POSSIBLE_NUMBER_OF_PLATES =
            (int) Math.pow(NUM_LETTERS, PLATE_LENGTH) - 10;

    /** List of plates used throughout the game. (Ensures no duplicate plates are generated.) */
    private static final Set<String> PLATES = new HashSet<>();

    /** Random number generator used to generate new plates */
    private static final Random RANDOM = new Random();

    /** This car's license plate number.*/
    private final String plate;

    /**
     * Create a new car with a unique license plate number
     * @param cityMap The map to which this car will be added.
     */
    public Car(CityMap cityMap) {
        super(cityMap, CAR_IMAGE);
        String putativePlate = findUniquePlate();
        plate = putativePlate;
        setName("Car "+ MobileEntity.instanceCount + ": " + plate);
    }

    /**
     * If all plates are generated, starts generating random plates with duplicates...
     * @return a new unique plate, unless all plates are used.
     */
    private String findUniquePlate() {
        String putativePlate;
        do {
            putativePlate = generatePlate();
        } while(PLATES.contains(putativePlate) &&
                // If nearly plates are generated, start producing duplicates
                PLATES.size() < POSSIBLE_NUMBER_OF_PLATES);
        PLATES.add(putativePlate);
        return putativePlate;
    }

    /**
     * Generates random, possibly not-unique plate.
     * @return The plate generated.
     */
    private String generatePlate() {
        StringBuilder plate = new StringBuilder();
        for(int i = 0; i < PLATE_LENGTH; i++) {
            plate.append((char)('A'+ RANDOM.nextInt(NUM_LETTERS)));
        }
        return plate.toString();
    }

    /**
     * Steps as all MobileEntities do.
     * Randomly turn right with equal probability during each time-step.
     * I'm using the theory of exponential distributions [1]
     * in naming the variable MEAN_DISTANCE_BEFORE_TURN.
     * Because the probability density function has a density of $\lambda$ at time zero, and a
     * of $1/\lambda$, we can say that since the probability of the event occurring is
     * mean of 1/MEAN_NUMBER_OF_STEPS, then (on average) we will go MEAN_NUMBER_OF_STEPS
     * before an event occurs. This approximation is valid only when MEAN_NUMBER_OF_STEPS
     * is small.
     * Challenge: Make another "Turn Observer" that measures the mean distance between turns
     * to see if my theory works out in practice!
     * [1] https://en.wikipedia.org/wiki/Exponential_distribution
     *
     * @author Dr. Yoder
     */
    @Override
    protected void step() {
        super.step();
        double randNum = Math.random();
        if(randNum <
                (Math.hypot(stepSize.getX(), stepSize.getY())/MEAN_DISTANCE_BEFORE_TURN)) {
            turnRight();
        } // else no turn needed!
    }
}
