import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.event.*;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;


public class Hauptmenu extends Application {
  // Anfang Attribute
  private ImageView imageView1 = new ImageView();
  private Image imageView1Image = new Image(getClass().getResourceAsStream("images/background.png"));
  private Button startenButton = new Button();
  private Button einstellungenButton = new Button();
  private Stage stage;
  private StackPane stackPane;
  private double bildschirmBreite = Screen.getPrimary().getBounds().getWidth();
  private double bildschirmHoehe = Screen.getPrimary().getBounds().getHeight();
  private VBox einstellungen;
  // Ende Attribute
  
  public void start(Stage primaryStage) {
    stage = primaryStage;
    Pane root = new Pane();
    Scene scene = new Scene(root, bildschirmBreite, bildschirmHoehe);
    //Hauptmenü
    imageView1.setX(0);
    imageView1.setY(0);
    imageView1.setFitWidth(bildschirmBreite);
    imageView1.setFitHeight(bildschirmHoehe);
    imageView1.setImage(imageView1Image);
    root.getChildren().add(imageView1);
    startenButton.setLayoutX(bildschirmBreite/2 - (113 / 2));
    startenButton.setLayoutY(bildschirmHoehe/2 - (33 / 2));
    startenButton.setPrefHeight(33);
    startenButton.setPrefWidth(113);
    startenButton.setText("Spiel starten");
    startenButton.setOnAction(
    (event) -> {startenButton_Action(event);} 
    );
    startenButton.setFont(Font.font("Dialog", 15));
    root.getChildren().add(startenButton);
    
    einstellungenButton.setLayoutX(bildschirmBreite/2 - (113 / 2));
    einstellungenButton.setLayoutY(bildschirmBreite/2 - 243);
    einstellungenButton.setPrefHeight(33);
    einstellungenButton.setPrefWidth(113);
    einstellungenButton.setText("Einstellungen");
    einstellungenButton.setOnAction(
    (event) -> {einstellungenButton_Action(event);}
    );
    root.getChildren().add(einstellungenButton);
    
    
    //Einstellungsmenü
    einstellungen = new VBox(100);
    einstellungen.setVisible(false);
    einstellungen.setAlignment(Pos.CENTER);
    einstellungen.setLayoutX(0);
    einstellungen.setLayoutY(0);
    
    Button zurueckButton = new Button("Zurück");
    zurueckButton.setOnAction((event) -> zurueckButton_Action(event));
    zurueckButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black;");
    Slider lautstaerke = new Slider();
    lautstaerke.setMaxWidth(300);
    lautstaerke.setStyle("-fx-background-color: lightgray;");
    einstellungen.getChildren().addAll(zurueckButton, lautstaerke);
    
    stackPane = new StackPane();
    stackPane.setAlignment(Pos.CENTER);
    stackPane.setPrefSize(bildschirmBreite, bildschirmHoehe);
    stackPane.setVisible(false); // Unsichtbar, wenn nicht aktiv
    root.getChildren().add(stackPane);
    stackPane.getChildren().add(einstellungen);
    
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
  } // end of public Hauptmenu
  
  // Anfang Methoden
  
  public static void main(String[] args) {
    launch(args);
  } 
  
  public void startenButton_Action(Event evt) {
    Map1 map1 = new Map1();
    map1.initialize(stage);
    stage.setFullScreen(true);
  }
  
  public void einstellungenButton_Action(Event evt2) {
    stackPane.setVisible(true);
    einstellungen.setVisible(true);
    imageView1.setVisible(false);
    startenButton.setVisible(false);
    einstellungenButton.setVisible(false);
  }
      
  public void zurueckButton_Action(Event evt3) {
    stackPane.setVisible(false);
    einstellungen.setVisible(true);
    imageView1.setVisible(true);
    startenButton.setVisible(true);
    einstellungenButton.setVisible(true);
  }  
} 
