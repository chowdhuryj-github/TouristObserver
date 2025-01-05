/*
 * Course: SWE2410
 * Fall 2024
 * Lab 3 - Tourist Observer
 * Name: Jawadul Chowdhury
 * Submission Date: 9/23/24
 */
package lab3.mketour;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import lab3.mketour.actors.MobileEntity;
import lab3.mketour.actors.Bus;
import lab3.mketour.actors.Car;
import lab3.mketour.actors.Person;


/**
 * This application represents a city map which a tourist can explore solving a variety of
 * challenges.
 * The CityMap holds all the items appearing on the map, populating itself at the beginning of
 * the game.
 * The basic game framework is based on a SO answer about how to make Canvas elements clickable [1],
 * but in the end, we don't use a Canvas.
 * [1] https://stackoverflow.com/questions/27999430/javafx-clickable-line-on-canvas
 */
public class CityMap extends Application {

    /**
     * number of tourists
     */
    public static final int NUM_TOURISTS = 15;

    /**
     * set non-zero to control debugging; return to 0 when demo -
     * critical because this slows simulations
     */
    public static final int DEBUG_LEVEL = 0;

    /** Width of the "challenges" space on the right side of the map in pixels */
    public static final int MIN_CHALLENGES_WIDTH = 250;

    /**
     * number of buses
     */
    public static final int NUM_BUSSES = 2;
    private static final int WIDTH = 250;
    private static final int SPACING = 10;
    private static final Image BACKGROUND_IMAGE = new Image(CityMap.class.
            getResource("img/map.png").toString());
    private static final Text FOUND_TEXT = new Text();
    private static final ImageView IMAGE_VIEW = new ImageView();
    private static final int FONT_SIZE = 20;
    private static final List<Museum> MUSEUMS = new ArrayList<>();


    /** For ease of access, there is a single character accessible as a sort of Singleton. */
    private static MobileEntity mainCharacter = null;

    private final Pane overlay = new Pane();

    private final ImageView backgroundView = new ImageView(BACKGROUND_IMAGE);
    private Pane challengePane;
    private final Collection<Taggable> taggables = new ArrayList<>();

    private final List<MobileEntity> mobileEntities = new ArrayList<>();

    private final Text challengeText = new Text();
    private final ArtChallengeObserver artChallengeObserver = new ArtChallengeObserver();







    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Each taggable must also call addTaggableToMap() to add itself to the map.
     * @param node node the GUI element to add.
     */
    public void addNodeToMap(Node node) {
        overlay.getChildren().addAll(node);
    }

    /**
     * Add a taggable to the map. Muse also call addNodeToMap for each visual element to be
     * placed on the map.
     * @param taggable the taggable item to be added to the map.
     */
    public void addTaggableToMap(Taggable taggable) {
        taggables.add(taggable);
    }

    /**
     * Adds a visual pane for a Challenge to the map.
     * @param node the visual pane to add.
     */
    public void addChallengeNode(Node node) {
        challengePane.getChildren().addAll(node);
    }

    /**
     * getting the mobile entities
     * @return a copy of list of the mobile entities found on the map. The entities themselves
     * are NOT copies.
     */
    public List<MobileEntity> getMobileEntities() {
        return new ArrayList<>(mobileEntities);
    }

    /**
     * The areas themselves are NOT copies.
     * @return a copy of the list of the museums found on the map.
     */
    public static List<Museum> getMuseums() {
        return new ArrayList<>(MUSEUMS);
    }

    /**
     * Called by an entity each time it moves.
     * Causes the entity to tag any entities that it now touches.
     *
     * @param entity The entity doing the tagging.
     */
    public void taggedBy(MobileEntity entity) {
        artChallengeObserver.update(getMuseums(), getMainCharacter());
        for(Taggable taggable: taggables) {
            if(taggable.isTagged(entity.getLocation())) {
                taggable.taggedBy(entity);
            }
        }
    }



    public double getWidth(){
        return backgroundView.getImage().getWidth();
    }

    public double getHeight() {
        return backgroundView.getImage().getHeight();
    }

    /**
     * Set up the basic map layout.
     * See addEntities() for the fun part (MobileEntities, etc.)
     *
     * @param primaryStage The main window.
     */
    @Override
    public void start(Stage primaryStage) {
        Pane root = new HBox();
        challengePane = new VBox();
        challengePane.setMinWidth(MIN_CHALLENGES_WIDTH);
        Pane mapPane = new Pane();

        VBox museumChallenge = new VBox();
        museumChallenge.getChildren().addAll(challengeText, FOUND_TEXT, IMAGE_VIEW);
        museumChallenge.setPrefWidth(WIDTH);
        museumChallenge.setSpacing(SPACING);


        backgroundView.relocate(0, 0);
        mapPane.setMaxWidth(BACKGROUND_IMAGE.getWidth());
        mapPane.setMaxHeight(BACKGROUND_IMAGE.getHeight());

        overlay.getChildren().addAll(backgroundView);
        mapPane.getChildren().addAll(overlay);
        challengeText.setText("Challenge: Find art");
        challengeText.setFont(new Font(FONT_SIZE));
        root.getChildren().addAll(mapPane, museumChallenge, challengePane);


        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        addEntities();
    }

    /**
     * method for setting the challenge text
     * @param text text
     * @param fontSize fontSize
     */
    public static void setChallengeText(String text, int fontSize) {
        FOUND_TEXT.setText(text);
        FOUND_TEXT.setFont(new Font(fontSize));
    }

    /**
     * method for setting the challenge image
     * @param artImage artImage
     * @param height height
     * @param width width
     */
    public static void setChallengeImage(Image artImage, int height, int width) {
        IMAGE_VIEW.setImage(artImage);
        IMAGE_VIEW.setFitWidth(height);
        IMAGE_VIEW.setFitHeight(width);
    }

    /**
     * Get the main character Singleton
     * @return the main character (the Person)
     * @throws RuntimeException runTimeException
     */
    public static MobileEntity getMainCharacter() {
        if(mainCharacter == null) {
            throw new RuntimeException("getMainCharacter called before game was initialized!");
        }
        return mainCharacter;
    }

    /**
     * Set up all the Mobile and non-mobile Entities on the map.
     * Also sets up Challenges.
     */
    private void addEntities() {
        // Each entity places itself on the map, so we place the goal first
        // , so it will show at the bottom.
        Person.Goal goal = new Person.Goal(this);

        for(int i = 0; i < NUM_TOURISTS; i++) {
            Car car = new Car(this);
            // This method can be moved into the Mobile Entity constructor
            // for some implementations.
            car.addToCityMap();
            mobileEntities.add(car);
        }

        for(int i = 0; i < NUM_BUSSES; i++) {
            Bus bus = new Bus(this);
            // This method can be moved into the Mobile Entity constructor
            // for some implementations.
            bus.addToCityMap();
            mobileEntities.add(bus);
        }

        synchronized (CityMap.class) {
            if(mainCharacter == null) {
                mainCharacter = new Person(this, goal);
                mainCharacter.addToCityMap();
            } else {
                System.out.println("Warning: initializing map more than once!");
            }
        }

        MUSEUMS.add(new Museum(this));


    }


}
