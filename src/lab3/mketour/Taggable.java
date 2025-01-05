/*
 * Course: SWE2410
 * Fall 2024
 * Lab 3 - Tourist Observer
 * Name: Jawadul Chowdhury
 * Submission Date: 9/23/24
 */
package lab3.mketour;

import javafx.geometry.Point2D;
import lab3.mketour.actors.MobileEntity;

/**
 * Simple interface for all taggable objects.
 * The CityMap uses this class to simplify collision detection.  See the CityMap's taggedBy method.
 */
public interface Taggable {

    /**
     * method for isTagged()
     * @param location The location to check
     * @return true if this object was tagged by touching that location
     */
    boolean isTagged(Point2D location);

    /**
     * Respond to being tagged. This method is called when the object is tagged.
     * @param entity The entity performing the tagging.
     */
    void taggedBy(MobileEntity entity);


}
