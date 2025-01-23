import javafx.application.Application;
import javafx.scene.Scene;
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


public class Hauptmenu extends Application {
  // Anfang Attribute
  private ImageView imageView1 = new ImageView();
  private Image imageView1Image = new Image(getClass().getResourceAsStream("images/background.png"));
  private Button startenButton = new Button();
  private Stage stage;
  private double bildschirmBreite = Screen.getPrimary().getBounds().getWidth();
  private double bildschirmHoehe = Screen.getPrimary().getBounds().getHeight();
  // Ende Attribute
  
  public void start(Stage primaryStage) {
    stage = primaryStage;
    Pane root = new Pane();
    Scene scene = new Scene(root, bildschirmBreite, bildschirmHoehe);
    // Anfang Komponenten
    
    imageView1.setX(0);
    imageView1.setY(0);
    imageView1.setFitWidth(bildschirmBreite);
    imageView1.setFitHeight(bildschirmHoehe);
    imageView1.setImage(imageView1Image);
    root.getChildren().add(imageView1);
    startenButton.setLayoutX(696);
    startenButton.setLayoutY(376);
    startenButton.setPrefHeight(33);
    startenButton.setPrefWidth(113);
    startenButton.setText("Spiel starten");
    startenButton.setOnAction(
      (event) -> {startenButton_Action(event);} 
    );
    startenButton.setFont(Font.font("Dialog", 11));
    root.getChildren().add(startenButton);
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

  
} 
