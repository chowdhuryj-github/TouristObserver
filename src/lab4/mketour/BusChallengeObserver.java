/*
 * Course: SWE2410
 * Fall 2024
 * Lab 4 - Tourist Observer
 * Name: Jawadul Chowdhury
 * Submission Date: 9/30/24
 */
package lab4.mketour;

import lab4.mketour.actors.Car;
import lab4.mketour.actors.MobileEntity;

import java.util.List;




/**
 * class for BusChallengeObserver
 */
public class BusChallengeObserver implements ChallengeObserver {

    private static final int SEVEN = 7;
    private static final int EIGHT = 8;
    private static final int NINE = 9;
    private static final int FONT_SIZE = 20;

    private final int fontSize = 20;
    private final String challengeText = "Challenge: Find all the letters in BUS";
    private final String goalText = "Goal: BUS";
    private String foundText = "Found: ***";

    /**
     * the update() method
     * @param mobileEntities list of mobile entities
     */
    @Override
    public void update(List<MobileEntity> mobileEntities) {

        // Set initial challenge texts
        CityMap.setBusChallengeText(challengeText, goalText, foundText, fontSize);

        for (MobileEntity mobileEntity : mobileEntities) {
            if (mobileEntity instanceof Car) {
                if(!mobileEntity.isTagged(CityMap.getMainCharacter().getLocation())) {

                    String plateNumber = mobileEntity.getName();
                    StringBuilder challengeString = new StringBuilder(foundText);

                    for (int i = 0; i < plateNumber.length(); i++) {
                        switch (plateNumber.charAt(i)) {
                            case 'B':
                                challengeString.setCharAt(SEVEN, 'B');
                                break;
                            case 'U':
                                challengeString.setCharAt(EIGHT, 'U');
                                break;
                            case 'S':
                                challengeString.setCharAt(NINE, 'S');
                                break;
                        }
                    }

                    // Update the foundText with the new letters
                    foundText = challengeString.toString();

                    // Update the UI with the new foundText
                    CityMap.setBusChallengeText(challengeText, goalText, foundText, fontSize);


                }

            }
        }

        if(CityMap.getBusChallengeFoundText().getText().equals("Found: BUS")) {
            CityMap.setBusChallengeText(challengeText, goalText,
                    "BUS CHALLENGE COMPLETED", FONT_SIZE);
        }

    }
}
