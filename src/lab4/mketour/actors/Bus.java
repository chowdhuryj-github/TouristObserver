/*
 * Course: SWE2410
 * Fall 2024
 * Lab 4 - Tourist Observer
 * Name: Jawadul Chowdhury
 * Submission Date: 9/30/24
 */
package lab4.mketour.actors;

import javafx.scene.image.Image;
import lab4.mketour.CityMap;

/**
 * Simplification (!) of the Milwaukee County Transit System.
 * A Mobile Entity that drives in a fixed rectangular loop.
 * Although the buses start off of the loop, they quickly find it.
 */
public class Bus extends MobileEntity {

    /** x coordinates of the bus's route */
    public static final int LEFT_EDGE = 100;

    /**
     * right edge value
     */
    public static final int RIGHT_EDGE = 600;

    /** y coordinates of the bus's route */
    public static final int TOP_EDGE = 50;

    /**
     * bottom edge value
     */
    public static final int BOTTOM_EDGE = 400;

    private static final Image BUS_IMAGE =
            new Image(CityMap.class.getResource("img/bus.png").toString());


    /**
     * Bus Constructor
     * @param cityMap cityMap
     */
    public Bus(CityMap cityMap) {
        super(cityMap, BUS_IMAGE);
        setName("Bus "+ MobileEntity.instanceCount);
    }

    /**
     * Take one simulation step forward, towards or continuing to follow the loop.
     */
    @Override
    protected void step() {
        super.step();
        checkBounds(LEFT_EDGE, RIGHT_EDGE, TOP_EDGE, BOTTOM_EDGE);
    }
}
