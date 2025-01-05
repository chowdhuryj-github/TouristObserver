/*
 * Course: SWE2410
 * Fall 2024
 * Lab 4 - Tourist Observer
 * Name: Jawadul Chowdhury
 * Submission Date: 9/30/24
 */
package lab4.mketour.actors;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

import lab4.mketour.BusChallengeObserver;
import lab4.mketour.CityMap;
import lab4.mketour.MSOEChallengeObserver;
import lab4.mketour.Taggable;


/**
 * Represents anything that can move around town.
 * Basic functionality to display the item on the cityMap and move in straight lines.
 */
public abstract class MobileEntity implements Taggable {

    /**
     * Height in pixels to show an entity on the cityMap
     */
    public static final int HEIGHT = 30;

    /**
     * Maximum starting speed for randomly created entities along any one axis
     */
    public static final int MAX_INITIAL_SPEED_PIXELS_PER_STEP = 2;

    /**
     * Euclidean distance within which an entity can be tagged
     */
    public static final int TAG_DISTANCE_PIXELS = 10;

    /**
     * Distance before edge of image that an entity will turn in pixels.
     */
    public static final int TURNING_MARGIN = 10;

    /**
     * Like TURNING_MARGIN, but used for the east edge.
     * This keeps things off the lake (and off of the Challenges!)
     */
    public static final int EAST_TURNING_MARGIN = 200;

    /**
     * Time between calls to step() (ms)
     */
    public static final double MILLISECONDS_PER_STEP = 1000. / 30;

    /**
     * Extra downward margin when displaying the name of an entity on the map
     */
    public static final int TEXT_DISPLAY_FUDGE_FACTOR = 5;

    /**
     * Total number of entities on the map
     */
    protected static int instanceCount = 0;

    /**
     * Velocity. In pixels/step
     */
    protected Point2D stepSize;

    private final MSOEChallengeObserver msoeChallengeObserver = new MSOEChallengeObserver();
    private final BusChallengeObserver busChallengeObserver = new BusChallengeObserver();



    /**
     * The cityMap to which this entity belongs.
     */
    private final CityMap cityMap;

    /**
     * Position of the center of the entity. In pixels.
     */
    private Point2D location;


    private final ImageView iconView;


    /**
     * A unique name for this entity
     */
    private String name = "Entity " + ++instanceCount;
    private final Text nameText;

    /**
     * Places and displays this entity at a random location on the map. Causes this entity to start
     * moving with a random velocity along a grid direction.
     *
     * @param cityMap The map on which this entity will be placed.
     * @param image   The image representing this entity on the map.
     */
    public MobileEntity(CityMap cityMap, Image image) {
        this.cityMap = cityMap;

        // We use an EAST_TURNING_MARGIN to allow for the lake boundary.
        location = chooseRandomInBoundsPoint();
        if (Math.random() > 0) {
            stepSize = new Point2D(MAX_INITIAL_SPEED_PIXELS_PER_STEP * (Math.random() * 2 - 1), 0);
        } else {
            stepSize = new Point2D(0, MAX_INITIAL_SPEED_PIXELS_PER_STEP * (Math.random() * 2 - 1));
        }

        iconView = new ImageView(image);
        iconView.setPreserveRatio(true);
        iconView.setFitHeight(HEIGHT);
        nameText = new Text(name);

        setPosition();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(MILLISECONDS_PER_STEP), e -> {
            step();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * method for adding to cityMap
     */
    public void addToCityMap() {
        cityMap.addNodeToMap(iconView);
        cityMap.addNodeToMap(nameText);
        cityMap.addTaggableToMap(this);
    }

    protected Point2D chooseRandomInBoundsPoint() {
        return new Point2D(
                TURNING_MARGIN +
                        ((cityMap.getWidth() - TURNING_MARGIN - EAST_TURNING_MARGIN)
                                * Math.random()),
                TURNING_MARGIN +
                        ((cityMap.getHeight() - 2 * TURNING_MARGIN) * Math.random()));
    }

    /**
     * Update both the name of this entity and the text label displaying it
     *
     * @param name The new name for this entity
     */
    public void setName(String name) {
        this.name = name;
        nameText.setText(name);
    }

    public String getName() {
        return name;
    }

    public Point2D getLocation() {
        return location;
    }

    /**
     * Move the entity to a given position
     */
    private void setPosition() {
        double iconCornerX = location.getX() -
                iconView.boundsInParentProperty().get().getWidth() / 2;
        double iconCornerY = location.getY() -
                iconView.boundsInParentProperty().get().getHeight() / 2;
        iconView.relocate(iconCornerX, iconCornerY);
        double textCornerX =
                location.getX() - nameText.boundsInParentProperty().get().getWidth() / 2;
        nameText.relocate(textCornerX, location.getY() + (double) HEIGHT / 2
                + TEXT_DISPLAY_FUDGE_FACTOR);
    }

    /**
     * Increment by step size and perform basic functions to not walk off-screen.
     * Also: Tag anything on which we land after the step!
     */
    protected void step() {
        location = location.add(stepSize);
        setPosition();
        checkBounds(TURNING_MARGIN,
                cityMap.getWidth() - EAST_TURNING_MARGIN,
                TURNING_MARGIN,
                cityMap.getHeight() - TURNING_MARGIN);
        cityMap.taggedBy(this);
    }

    /**
     * Turn right if outside the bounds given.
     *
     * @param leftEdge   distance from left edge of map to bounding box in pixels
     * @param rightEdge  distance from left edge of map to right edge of bounding box in pixels
     * @param topEdge    distance from top of map to bounding box in pixels
     * @param bottomEdge distance from top of map to bottom of bounding box in pixels.
     */
    protected void checkBounds(double leftEdge, double rightEdge, double topEdge,
                               double bottomEdge) {
        // basic strategy: If currently going in a bad direction, turn!
        // Should correct to not wander further, even if other wrong turns are made.
        if (location.getX() > rightEdge && stepSize.getX() > 0) {
            turnRight();
        } else if (location.getY() > bottomEdge && stepSize.getY() > 0) {
            turnRight();
        } else if (location.getX() < leftEdge && stepSize.getX() < 0) {
            turnRight();
        } else if (location.getY() < topEdge && stepSize.getY() < 0) {
            turnRight();
        } // else no turn needed!
    }

    /**
     * Perform a 90-degree clockwise turn on the step size.
     */
    protected void turnRight() {
        stepSize = new Point2D(-stepSize.getY(), stepSize.getX());
    }

    /**
     * Determine if tagged by a location based upon Euclidean distance
     *
     * @param location The location of the tag
     * @return true if location is within TAG_DISTANCE_PIXELS of this mobile entity's location
     */
    @Override
    public boolean isTagged(Point2D location) {
        return location.distance(this.location) < TAG_DISTANCE_PIXELS;
    }

    /**
     * This method is called when this mobile entity is tagged by another entity.
     *
     * @param entity The entity that tagged this mobile entity.
     */
    @Override
    public void taggedBy(MobileEntity entity) {

        Point2D mainCharacterLocation = CityMap.getMainCharacter().getLocation();

        if(entity instanceof Car && this instanceof Car && this.isTagged(mainCharacterLocation)) {
            msoeChallengeObserver.update(CityMap.getMobileEntities());
        }

        if(entity instanceof Bus && this instanceof Bus && this.isTagged(mainCharacterLocation)) {
            busChallengeObserver.update(CityMap.getMobileEntities());
        }

        if (CityMap.DEBUG_LEVEL > 0) {
            System.out.println(this + " tagged by  " + entity);
        }

    }




    /**
     * toString() method
     * @return the name of this mobile entity
     */
    @Override
    public String toString() {
        return name;
    }
}
