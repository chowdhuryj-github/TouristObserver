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
 * class for MSOEChallengeObserver
 */
public class MSOEChallengeObserver implements ChallengeObserver {

    private static final int SEVEN = 7;
    private static final int EIGHT = 8;
    private static final int NINE = 9;
    private static final int TEN = 10;
    private static final int TWENTY = 20;

    /**
     * the update() method for Car
     * @param mobileEntities person
     */
    @Override
    public void update(List<MobileEntity> mobileEntities) {

        for (MobileEntity mobileEntity : mobileEntities) {
            if (mobileEntity instanceof Car) {
                if(mobileEntity.isTagged(CityMap.getMainCharacter().getLocation())) {
                    String plateNumber = mobileEntity.getName().substring(SEVEN);
                    StringBuilder challengeString = new StringBuilder(
                            CityMap.getMsoeChallengeFoundText().getText());

                    for(int i = 0; i < plateNumber.length(); i++) {
                        switch(plateNumber.charAt(i)) {
                            case 'M':
                                challengeString.setCharAt(SEVEN, 'M');
                                break;
                            case 'S':
                                challengeString.setCharAt(EIGHT, 'S');
                                break;
                            case 'O':
                                challengeString.setCharAt(NINE, 'O');
                                break;
                            case 'E':
                                challengeString.setCharAt(TEN, 'E');
                                break;

                        }
                    }

                    CityMap.setMSOEChallengeText(challengeString.toString(), TWENTY);


                }
            }
        }
    }
}
