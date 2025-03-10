import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class Hauptmenu extends Application {
  // Anfang Attribute
  private ImageView imageView1 = new ImageView();
  private Image imageView1Image = new Image(getClass().getResourceAsStream("images/background.png"));
  private Button startenButton = new Button();
  private Button einstellungenButton = new Button();
  private Button klasseWechselnButton = new Button();
  private Button spielBeendenButton = new Button();
  private Stage stage;
  private StackPane stackPaneEinstellungen;
  private StackPane stackPaneKlassen;
  private double bildschirmBreite = Screen.getPrimary().getBounds().getWidth();
  private double bildschirmHoehe = Screen.getPrimary().getBounds().getHeight();
  private VBox einstellungen;
  private VBox klassewechseln;
  private Button zurueckButtonEinstellungen = new Button("Zurück");
  private Button zurueckButtonKlassenwechsel = new Button("Zurück");
  private String name = System.getProperty("user.name");
  private Label willkommen = new Label();

  // Ende Attribute
  
  public void start(Stage primaryStage) {
    stage = primaryStage;
    Pane root = new Pane();
    Scene scene = new Scene(root, bildschirmBreite, bildschirmHoehe);
    
    // Hauptmenü
    imageView1.setX(0);
    imageView1.setY(0);
    imageView1.setFitWidth(bildschirmBreite);
    imageView1.setFitHeight(bildschirmHoehe);
    imageView1.setImage(imageView1Image);
    root.getChildren().add(imageView1);
    
    willkommen.setPrefHeight(33);
    willkommen.setText("Willkommen bei Wee Tanks, " + name + ".");
    willkommen.setLayoutX(bildschirmBreite / 2 - (113 / 2) - 40);
    willkommen.setLayoutY(bildschirmHoehe / 2 - (33 / 2) - 50);
    root.getChildren().add(willkommen);

    startenButton.setLayoutX(bildschirmBreite / 2 - (113 / 2));
    startenButton.setLayoutY(bildschirmHoehe / 2 - (33 / 2));
    startenButton.setPrefHeight(33);
    startenButton.setPrefWidth(113);
    startenButton.setText("Spiel starten");
    startenButton.setOnAction((event) -> {startenButton_Action(event);});
    startenButton.setFont(Font.font("Dialog", 15));
    root.getChildren().add(startenButton);

    einstellungenButton.setLayoutX(bildschirmBreite / 2 - (113 / 2));
    einstellungenButton.setLayoutY(bildschirmHoehe / 2 - (33 / 2) + 50);
    einstellungenButton.setPrefHeight(33);
    einstellungenButton.setPrefWidth(113);
    einstellungenButton.setText("Einstellungen");
    einstellungenButton.setOnAction((event) -> {einstellungenButton_Action(event);});
    root.getChildren().add(einstellungenButton);

    klasseWechselnButton.setLayoutX(bildschirmBreite / 2 - (113 / 2));
    klasseWechselnButton.setLayoutY(bildschirmHoehe / 2 - (33 / 2) + 100);
    klasseWechselnButton.setPrefHeight(33);
    klasseWechselnButton.setPrefWidth(113);
    klasseWechselnButton.setText("Klasse Wechseln");
    klasseWechselnButton.setOnAction((event) -> {klasseWechselnButton_Action(event);});
    root.getChildren().add(klasseWechselnButton);
    
    spielBeendenButton.setLayoutX(bildschirmBreite / 2 - (113 / 2));
    spielBeendenButton.setLayoutY(bildschirmHoehe / 2 - (33 / 2) + 150);
    spielBeendenButton.setPrefHeight(33);
    spielBeendenButton.setPrefWidth(113);
    spielBeendenButton.setText("Spiel Beenden");
    spielBeendenButton.setOnAction((event) -> {spielBeendenButton_Action(event);});
    root.getChildren().add(spielBeendenButton);

    // Einstellungsmenü
    einstellungen = new VBox(100);
    einstellungen.setVisible(false);
    einstellungen.setAlignment(Pos.CENTER);
    einstellungen.setLayoutX(0);
    einstellungen.setLayoutY(0);

    zurueckButtonEinstellungen.setOnAction((event) -> zurueckButton_Action(event));
    zurueckButtonEinstellungen.setStyle("-fx-background-color: lightgray; -fx-text-fill: black;");
    Slider lautstaerke = new Slider();
    lautstaerke.setMaxWidth(300);
    lautstaerke.setStyle("-fx-background-color: lightgray;");
    einstellungen.getChildren().addAll(zurueckButtonEinstellungen, lautstaerke);

    stackPaneEinstellungen = new StackPane();
    stackPaneEinstellungen.setAlignment(Pos.CENTER);
    stackPaneEinstellungen.setPrefSize(bildschirmBreite, bildschirmHoehe);
    stackPaneEinstellungen.setVisible(false);
    root.getChildren().add(stackPaneEinstellungen);
    stackPaneEinstellungen.getChildren().add(einstellungen);

    // Klassenwechselmenü
    klassewechseln = new VBox(100);
    klassewechseln.setVisible(false);
    klassewechseln.setAlignment(Pos.CENTER);
    klassewechseln.setLayoutX(0);
    klassewechseln.setLayoutY(0);

    zurueckButtonKlassenwechsel.setOnAction((event) -> zurueckButton_Action(event));
    Button klasseDoppelSchuss = new Button("Doppelschuss");
    klasseDoppelSchuss.setStyle("-fx-background-color: lightgray;");
    klassewechseln.getChildren().addAll(zurueckButtonKlassenwechsel, klasseDoppelSchuss);

    stackPaneKlassen = new StackPane();
    stackPaneKlassen.setAlignment(Pos.CENTER);
    stackPaneKlassen.setPrefSize(bildschirmBreite, bildschirmHoehe);
    stackPaneKlassen.setVisible(false);
    root.getChildren().add(stackPaneKlassen);
    stackPaneKlassen.getChildren().add(klassewechseln);

    // Ende Komponenten
    primaryStage.setResizable(false);
    primaryStage.setWidth(bildschirmBreite);
    primaryStage.setHeight(bildschirmHoehe);
    primaryStage.setFullScreen(true);
    primaryStage.setFullScreenExitHint("");
    primaryStage.setOnCloseRequest(e -> System.exit(0));
    primaryStage.setTitle("Hauptmenu");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
  
  public Hauptmenu(){
    
  }

  // Anfang Methoden
  
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
    imageView1.setVisible(false);
    startenButton.setVisible(false);
    einstellungenButton.setVisible(false);
    klasseWechselnButton.setVisible(false);
    spielBeendenButton.setVisible(false);
  }

  public void klasseWechselnButton_Action(Event evt3) {
    stackPaneKlassen.setVisible(true);
    klassewechseln.setVisible(true);
    imageView1.setVisible(false);
    startenButton.setVisible(false);
    einstellungenButton.setVisible(false);
    klasseWechselnButton.setVisible(false);
    spielBeendenButton.setVisible(false);
  }
  
  public void spielBeendenButton_Action(Event evt4) {
    Platform.exit();
  }

  public void zurueckButton_Action(Event evt5) {
    stackPaneEinstellungen.setVisible(false);
    stackPaneKlassen.setVisible(false);
    einstellungen.setVisible(false);
    klassewechseln.setVisible(false);
    imageView1.setVisible(true);
    startenButton.setVisible(true);
    einstellungenButton.setVisible(true);
    klasseWechselnButton.setVisible(true);
    spielBeendenButton.setVisible(true);
  }
}