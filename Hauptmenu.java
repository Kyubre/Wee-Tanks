import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class Hauptmenu extends Application {
  // Anfang Attribute
  private ImageView hintergrund = new ImageView();
  private Image hintergrundImage = new Image(getClass().getResourceAsStream("src/assets/images/background.png"));
  private Button startenButton = new Button();
  private Button einstellungenButton = new Button();
  private Button tutorialButton = new Button();
  private Button spielBeendenButton = new Button();
  private Stage stage;
  private StackPane stackPaneEinstellungen;
  private double bildschirmBreite = Screen.getPrimary().getBounds().getWidth();
  private double bildschirmHoehe = Screen.getPrimary().getBounds().getHeight();
  private VBox einstellungen;
  private ImageView tutorial;
  private Button zurueckButton = new Button("Zurück");
  private Label highscoreLabel = new Label();
  Pane root = new Pane();
  Scene scene = new Scene(root, bildschirmBreite, bildschirmHoehe);
  private static boolean tutorialGeschaut = false;
  private static int highscore = 0;

  // Ende Attribute

  public void start(Stage primaryStage) {
    stage = primaryStage;

    // Hauptmenü
    hintergrund.setX(0);
    hintergrund.setY(0);
    hintergrund.setFitWidth(bildschirmBreite);
    hintergrund.setFitHeight(bildschirmHoehe);
    hintergrund.setImage(hintergrundImage);
    root.getChildren().add(hintergrund);

    scene.getStylesheets().add("src/Styles/hauptmenu.css");
    VBox buttons = new VBox(10);
    buttons.setAlignment(Pos.CENTER);
    buttons.setPrefWidth(bildschirmBreite);
    buttons.setPrefHeight(bildschirmHoehe);
    buttons.setLayoutX(0);
    buttons.setLayoutY(50);

    berechneHighscore();

    highscoreLabel.getStyleClass().add("label");
    highscoreLabel.setPrefHeight(33);
    highscoreLabel.setText("Highscore: " + highscore);
    highscoreLabel.setLayoutX(bildschirmBreite / 2 - (113 / 2) - 40);
    highscoreLabel.setLayoutY(bildschirmHoehe / 2 - (33 / 2) - 50);
    root.getChildren().add(highscoreLabel);

    startenButton.getStyleClass().add("button");
    startenButton.setLayoutX(bildschirmBreite / 2 - (113 / 2));
    startenButton.setLayoutY(bildschirmHoehe / 2 - (33 / 2));
    startenButton.setText("Spiel starten");
    startenButton.setOnAction((event) -> {
      startenButton_Action(event);
    });
    root.getChildren().add(startenButton);

    einstellungenButton.getStyleClass().add("button");
    einstellungenButton.setLayoutX(bildschirmBreite / 2 - (113 / 2));
    einstellungenButton.setLayoutY(bildschirmHoehe / 2 - (33 / 2) + 50);
    einstellungenButton.setText("Einstellungen");
    einstellungenButton.setOnAction((event) -> {
      einstellungenButton_Action(event);
    });
    root.getChildren().add(einstellungenButton);

    tutorialButton.getStyleClass().add("button");
    tutorialButton.setLayoutX(bildschirmBreite / 2 - (113 / 2));
    tutorialButton.setLayoutY(bildschirmHoehe / 2 - (33 / 2) + 100);
    tutorialButton.setText("Tutorial");
    if (!tutorialGeschaut) {
      tutorialButton.setStyle("-fx-border-width: 4; -fx-border-color: #DC143C");
    }
    tutorialButton.setOnAction((event) -> {
      tutorialButton_Action(event);
    });
    root.getChildren().add(tutorialButton);

    spielBeendenButton.getStyleClass().add("button");
    spielBeendenButton.setLayoutX(bildschirmBreite / 2 - (113 / 2));
    spielBeendenButton.setLayoutY(bildschirmHoehe / 2 - (33 / 2) + 150);
    spielBeendenButton.setText("Spiel Beenden");
    spielBeendenButton.setOnAction((event) -> {
      spielBeendenButton_Action(event);
    });
    root.getChildren().add(spielBeendenButton);

    buttons.getChildren().addAll(highscoreLabel, startenButton, einstellungenButton, tutorialButton,
        spielBeendenButton);
    root.getChildren().add(buttons);

    zurueckButton.setAlignment(Pos.CENTER);
    zurueckButton.setOnAction((event) -> zurueckButton_Action(event));
    zurueckButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black;");

    // Einstellungsmenü
    einstellungen = new VBox(100);
    einstellungen.setVisible(false);
    einstellungen.setAlignment(Pos.CENTER);
    einstellungen.setLayoutX(0);
    einstellungen.setLayoutY(0);

    Label lautstaerkeLabel = new Label("Lautstärke: " + (int) (Settings.lautstaerke * 100) + "%");
    lautstaerkeLabel.getStyleClass().add("label");

    Slider lautstaerke = new Slider();
    lautstaerke.setMaxWidth(300);
    lautstaerke.setStyle("-fx-background-color: lightgray;");
    lautstaerke.setMin(0); // Minimale Lautstärke
    lautstaerke.setMax(1); // Maximale Lautstärke
    lautstaerke.setValue(Settings.lautstaerke);
    lautstaerke.getStyleClass().add("slider");
    lautstaerke.valueProperty().addListener((observable, oldValue, newValue) -> {
      Settings.lautstaerke = newValue.doubleValue(); // Lautstärke in Settings aktualisieren
      lautstaerkeLabel.setText("Lautstärke: " + (int) (Settings.lautstaerke * 100) + "%"); // Label aktualisieren
    });
    VBox lautstaerkeBox = new VBox(10);
    lautstaerkeBox.setAlignment(Pos.CENTER);
    lautstaerkeBox.getChildren().addAll(lautstaerkeLabel, lautstaerke);    

    Button resetHighscore = new Button("Highscore zurücksetzen");
    resetHighscore.getStyleClass().add("longbutton");
    resetHighscore.setOnAction(e -> {
      highscore = 0;
      highscoreLabel.setText("Highscore: " + highscore);
    });


    einstellungen.getChildren().addAll(zurueckButton, lautstaerkeBox, resetHighscore);

    stackPaneEinstellungen = new StackPane();
    stackPaneEinstellungen.setAlignment(Pos.CENTER);
    stackPaneEinstellungen.setPrefSize(bildschirmBreite, bildschirmHoehe);
    stackPaneEinstellungen.setVisible(false);
    root.getChildren().add(stackPaneEinstellungen);
    stackPaneEinstellungen.getChildren().add(einstellungen);

    // Tutorial
    tutorial = new ImageView();
    tutorial.setImage(new Image(getClass().getResourceAsStream("src/assets/images/tutorial.png")));
    tutorial.setX(0);
    tutorial.setY(0);
    tutorial.setFitHeight(bildschirmHoehe);
    tutorial.setFitWidth(bildschirmBreite);
    tutorial.setVisible(false);
    root.getChildren().add(tutorial);

    // Ende Komponenten
    primaryStage.setResizable(false);
    primaryStage.setWidth(bildschirmBreite);
    primaryStage.setHeight(bildschirmHoehe);
    primaryStage.setFullScreen(true);
    primaryStage.setFullScreenExitHint("");
    primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    primaryStage.setOnCloseRequest(e -> System.exit(0));
    primaryStage.setTitle("Wee Tanks");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public Hauptmenu() {

  }

  public static void main(String[] args) {
    launch(args);
  }

  public void startenButton_Action(Event evt) {
    Map map = new Map();
    map.initialize(stage);
    stage.setFullScreen(true);
    stage.setFullScreenExitHint("");
  }

  public void einstellungenButton_Action(Event evt2) {
    stackPaneEinstellungen.setVisible(true);
    einstellungen.setVisible(true);
    hintergrund.setVisible(false);
    startenButton.setVisible(false);
    einstellungenButton.setVisible(false);
    tutorialButton.setVisible(false);
    spielBeendenButton.setVisible(false);
    highscoreLabel.setVisible(false);
  }

  public void tutorialButton_Action(Event evt3) {
    tutorial.setVisible(true);
    hintergrund.setVisible(false);
    startenButton.setVisible(false);
    einstellungenButton.setVisible(false);
    tutorialButton.setVisible(false);
    spielBeendenButton.setVisible(false);
    highscoreLabel.setVisible(false);
    // Der FullScreenExitHint wird genutzt, um kurz eine Nachricht auf dem
    // Bildschirm gut sichtbar anzuzeigen
    stage.setFullScreenExitHint("Drücke eine beliebige Taste um zum Hauptmenu zurück zu kehren.");
    stage.setFullScreen(false);
    Platform.runLater(() -> stage.setFullScreen(true));
    // Key Listener, damit man zum Hauptmenu zurück kommt
    scene.setOnKeyPressed(event -> zurueckButton_Action(event));
    tutorialGeschaut = true;
  }

  public void spielBeendenButton_Action(Event evt4) {
    Platform.exit();
  }

  public void zurueckButton_Action(Event evt5) {
    stackPaneEinstellungen.setVisible(false);
    einstellungen.setVisible(false);
    tutorial.setVisible(false);
    hintergrund.setVisible(true);
    startenButton.setVisible(true);
    einstellungenButton.setVisible(true);
    tutorialButton.setVisible(true);
    spielBeendenButton.setVisible(true);
    highscoreLabel.setVisible(true);
    stage.setFullScreenExitHint("");
    stage.setFullScreen(true);
    if (tutorialGeschaut) {
      tutorialButton.setStyle("");
    }
  }

  public void berechneHighscore() {
    if (Settings.highscore > highscore) {
      highscore = Settings.highscore;
      highscoreLabel.setText("Highscore: " + highscore);
    } else {
      highscoreLabel.setText("Highscore: " + highscore);
    }
  }
}