/*
 * Course: SWE2410
 * Fall 2024
 * Lab 3 - Tourist Observer
 * Name: Jawadul Chowdhury
 * Submission Date: 9/23/24
 */
package lab3.mketour;

/**
 * method for interface
 */
public interface Observable {

    /**
     * Method for adding a observer
     * @param observer observer
     */
    void registerObserver(ChallengeObserver observer);

    /**
     * Method for removing an observer
     * @param observer observer
     */
    void unRegisterObserver(ChallengeObserver observer);

    /**
     * method for notifying a observer
     */
    void notifyObservers();
}
