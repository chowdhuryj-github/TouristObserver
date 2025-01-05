/*
 * Course: SWE2410
 * Fall 2024
 * Lab 4 - Tourist Observer
 * Name: Jawadul Chowdhury
 * Submission Date: 9/30/24
 */
package lab4.mketour.actors;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lab4.mketour.CityMap;

/**
 * The main character in the game.
 *
 * This person walks straight toward the Goal circle (see inner class).
 */
public class Person extends MobileEntity {
    /**
     * max speed of person
     */
    public static final int PERSON_MAX_SPEED = 2;
    private static final double OPACITY_MULTIPLIER = 0.2;
    private static final Image SMILEY_IMAGE = new Image(CityMap.class.getResource("img/smiley.png")
            .toString());

    private final Goal goal;

    /**
     * constructor of Person
     * @param cityMap cityMap
     * @param goal goal
     */
    public Person(CityMap cityMap, Goal goal) {
        super(cityMap, SMILEY_IMAGE);
        setName("You");
        stepSize = new Point2D(0, 0);
        this.goal = goal;
    }

    /**
     * Step toward the goal circle.
     */
    @Override
    protected void step() {
        Point2D direction = goal.getTarget().subtract(getLocation());
        if(Math.hypot(direction.getX(), direction.getY()) < PERSON_MAX_SPEED) {
            // last step to goal
            stepSize = direction;
        } else {
            // approaching goal
            stepSize = direction.multiply(PERSON_MAX_SPEED /
                    Math.hypot(direction.getX(), direction.getY()));
        }
        super.step();
    }


    /**
     * The person's Goal - the circular shape on the map that the person approaches.
     */
    public static class Goal {

        /**
         * radius of circle
         */
        public static final int RADIUS = 100;
        private final Circle circularGoal;

        /**
         * Adds a new draggable circle to the map to serve as the goal toward which the person
         * moves
         * @param cityMap the map to which this is added
         */
        public Goal(CityMap cityMap) {
            circularGoal = new Circle(RADIUS);
            circularGoal.setStroke(Color.GREEN);
            circularGoal.setFill(Color.GREEN.deriveColor(1, 1, 1, OPACITY_MULTIPLIER));
            circularGoal.relocate(0, 0);

            MouseGestures mg = new MouseGestures();
            mg.makeDraggable(circularGoal);

            cityMap.addNodeToMap(circularGoal);
        }

        /**
         * the getTarget() method
         * @return the center of the circular target
         */
        public Point2D getTarget() {
            return new Point2D(circularGoal.getCenterX(), circularGoal.getCenterY())
                    .add(RADIUS, RADIUS);
        }
    }

    /**
     * This inner class manages drag gestures on the person's Goal. From [1].
     * [1] https://stackoverflow.com/questions/27999430/javafx-clickable-line-on-canvas
     */
    private static class MouseGestures {

        private double orgSceneX;
        private double orgSceneY;
        private double orgTranslateX;
        private double orgTranslateY;

        private final EventHandler<MouseEvent> circleOnMouseDraggedEventHandler =
                (MouseEvent event) -> {
                    double offsetX = event.getSceneX() - orgSceneX;
                    double offsetY = event.getSceneY() - orgSceneY;

                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;

                    if (event.getSource() instanceof Circle) {
                        Circle p = (Circle) (event.getSource());

                        p.setCenterX(newTranslateX);
                        p.setCenterY(newTranslateY);
                    } else {
                        throw new IllegalArgumentException("Unknown type of object to drag!");
                    }
                };

        private final EventHandler<MouseEvent> circleOnMousePressedEventHandler =
                (MouseEvent event) -> {
                    orgSceneX = event.getSceneX();
                    orgSceneY = event.getSceneY();

                    if (event.getSource() instanceof Circle) {
                        Circle p = (Circle) (event.getSource());

                        orgTranslateX = p.getCenterX();
                        orgTranslateY = p.getCenterY();
                    } else {
                        throw new IllegalArgumentException("Unknown type of object to drag!");
                    }
                };

        /** Make the Circle object draggable */
        private void makeDraggable(Circle circle) {
            circle.setOnMousePressed(circleOnMousePressedEventHandler);
            circle.setOnMouseDragged(circleOnMouseDraggedEventHandler);
        }


    }

}
