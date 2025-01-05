/*
 * Course: SWE2410
 * Fall 2024
 * Lab 4 - Tourist Observer
 * Name: Jawadul Chowdhury
 * Submission Date: 9/30/24
 */
package lab4.mketour;

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
import lab4.mketour.actors.Bus;
import lab4.mketour.actors.Car;
import lab4.mketour.actors.MobileEntity;
import lab4.mketour.actors.Person;


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

    /**
     * number of tourists
     */
    public static final int NUM_TOURISTS = 15;

    static final Text BUS_CHALLENGE_FOUND_TEXT = new Text();
    private static final Text MSOE_CHALLENGE_FONUD_TEXT = new Text();



    // the bus challenge
    private static final Text BUS_CHALLENGE_TEXT = new Text();
    private static final Text BUS_CHALLENGE_GOAL = new Text();


    private static final List<MobileEntity> MOBILE_ENTITIES = new ArrayList<>();

    private static final int WIDTH = 250;
    private static final int SPACING = 10;
    private static final Image BACKGROUND_IMAGE = new Image(CityMap.class.getResource("img/map.png")
            .toString());
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

    // the art challenge
    private final Text artChallengeText = new Text();
    private final ArtChallengeObserver artChallengeObserver = new ArtChallengeObserver();

    // the msoe challenge
    private final Text msoeChallengeText = new Text();
    private final Text msoeChallengeGoalText = new Text();






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
    public static List<MobileEntity> getMobileEntities() {
        return new ArrayList<>(MOBILE_ENTITIES);
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
        artChallengeObserver.update(getMobileEntities());
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

        // setting up the museumChallengeText here!
        VBox museumChallenge = new VBox();
        museumChallenge.getChildren().addAll(artChallengeText, FOUND_TEXT, IMAGE_VIEW);
        museumChallenge.setPrefWidth(WIDTH);
        museumChallenge.setSpacing(SPACING);

        // setting up the msoeChallengeText here!
        VBox msoeChallenge = new VBox();
        msoeChallengeText.setText("Challenge: Find all the letters in MSOE");
        msoeChallengeText.setFont(new Font(FONT_SIZE));
        msoeChallengeGoalText.setText("Goal: MSOE");
        msoeChallengeGoalText.setFont(new Font(FONT_SIZE));
        MSOE_CHALLENGE_FONUD_TEXT.setText("Found: ****");
        MSOE_CHALLENGE_FONUD_TEXT.setFont(new Font(FONT_SIZE));
        msoeChallenge.getChildren().addAll(msoeChallengeText, msoeChallengeGoalText,
                MSOE_CHALLENGE_FONUD_TEXT);
        msoeChallenge.setPrefWidth(WIDTH);

        // setting up the busChallengeText here!
        VBox busChallenge = new VBox();
        busChallenge.getChildren().addAll(BUS_CHALLENGE_TEXT, BUS_CHALLENGE_GOAL,
                BUS_CHALLENGE_FOUND_TEXT);


        // combining all the challenges together!
        VBox challengeCombined = new VBox();
        challengeCombined.getChildren().addAll(msoeChallenge, museumChallenge, busChallenge);
        challengeCombined.setSpacing(SPACING);


        backgroundView.relocate(0, 0);
        mapPane.setMaxWidth(BACKGROUND_IMAGE.getWidth());
        mapPane.setMaxHeight(BACKGROUND_IMAGE.getHeight());

        overlay.getChildren().addAll(backgroundView);
        mapPane.getChildren().addAll(overlay);
        artChallengeText.setText("Challenge: Find art");
        artChallengeText.setFont(new Font(FONT_SIZE));

        root.getChildren().addAll(mapPane, challengeCombined, challengePane);


        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        addEntities();
    }


    /**
     * method for setting the bus challenge text
     * @param challengeText challenge text
     * @param goalText goal text
     * @param foundText found text
     * @param fontSize size of the font
     */
    public static void setBusChallengeText(String challengeText, String goalText,
                                           String foundText, int fontSize) {
        BUS_CHALLENGE_TEXT.setText(challengeText);
        BUS_CHALLENGE_TEXT.setFont(new Font(fontSize));
        BUS_CHALLENGE_GOAL.setText(goalText);
        BUS_CHALLENGE_GOAL.setFont(new Font(fontSize));
        BUS_CHALLENGE_FOUND_TEXT .setText(foundText);
        BUS_CHALLENGE_FOUND_TEXT .setFont(new Font(fontSize));

    }

    public static Text getBusChallengeFoundText() {
        return BUS_CHALLENGE_FOUND_TEXT;
    }

    /**
     * method for setting the MSOE challenge text
     * @param text text
     * @param fontSize fontSize
     */
    public static void setMSOEChallengeText(String text, int fontSize) {
        MSOE_CHALLENGE_FONUD_TEXT.setText(text);
        MSOE_CHALLENGE_FONUD_TEXT.setFont(new Font(fontSize));
    }

    public static Text getMsoeChallengeFoundText() {
        return MSOE_CHALLENGE_FONUD_TEXT;
    }


    /**
     * method for setting the challenge text
     * @param text text
     * @param fontSize fontSize
     */
    public static void setArtChallengeText(String text, int fontSize) {
        FOUND_TEXT.setText(text);
        FOUND_TEXT.setFont(new Font(fontSize));
    }

    /**
     * method for setting the challenge image
     * @param artImage artImage
     * @param height height
     * @param width width
     */
    public static void setArtChallengeImage(Image artImage, int height, int width) {
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
            MOBILE_ENTITIES.add(car);
        }

        for(int i = 0; i < NUM_BUSSES; i++) {
            Bus bus = new Bus(this);
            // This method can be moved into the Mobile Entity constructor
            // for some implementations.
            bus.addToCityMap();
            MOBILE_ENTITIES.add(bus);
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
