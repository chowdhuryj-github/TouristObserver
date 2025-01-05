/*
 * Course: SWE2410
 * Fall 2024
 * Lab 4 - Tourist Observer
 * Name: Jawadul Chowdhury
 * Submission Date: 9/30/24
 */
package lab4.mketour;


import javafx.scene.image.Image;
import lab4.mketour.actors.MobileEntity;
import java.util.List;


/**
 * class for ArtChallengeObserver
 */
public class ArtChallengeObserver implements ChallengeObserver {

    private static final Image ART_IMAGE = new Image("lab4/mketour/img/wood-gatherer.png");
    private static final int HEIGHT = 100;
    private static final int WIDTH = 100;
    private static final int FONT_SIZE = 20;

    /**
     * This method checks if any mobile entity has tagged a museum.
     * @param mobileEntities list of all mobile entities on the map.
     */
    @Override
    public void update(List<MobileEntity> mobileEntities) {
        // Retrieve museums directly from the CityMap
        List<Museum> museums = CityMap.getMuseums();
        for (Museum museum : museums) {
            if (museum.isTagged(CityMap.getMainCharacter().getLocation())) {
                CityMap.setArtChallengeText("Artistic Works Found: ", FONT_SIZE);
                CityMap.setArtChallengeImage(ART_IMAGE, HEIGHT, WIDTH);
            }
        }
    }

}
