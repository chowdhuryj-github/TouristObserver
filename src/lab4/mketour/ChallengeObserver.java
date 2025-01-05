/*
 * Course: SWE2410
 * Fall 2024
 * Lab 4 - Tourist Observer
 * Name: Jawadul Chowdhury
 * Submission Date: 9/30/24
 */
package lab4.mketour;

import lab4.mketour.actors.MobileEntity;
import java.util.List;

/**
 * interface class for ChallengeObserver
 */
public interface ChallengeObserver {

    /**
     * method for updating
     * @param mobileEntity person
     */
    void update(List<MobileEntity> mobileEntity);

}
