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


public class Hauptmenu extends Application {
  // Anfang Attribute
  private Button bSpielstarten = new Button("Spiel Starten");
  private Button bKarte = new Button("Karte von Lokkas öffnen");
  private Button bSpielBeenden = new Button("Spiel beenden");
  private ImageView background = new ImageView();
  private Image backgroundImage = new Image(getClass().getResourceAsStream("images/background.png"));
  private Button bSpielverlassen = new Button();
  // Ende Attribute
  
  public void start(Stage primaryStage) {
   
    StackPane hauptmenuLayout = new StackPane(bSpielstarten);
    Scene hauptmenuScene = new Scene(hauptmenuLayout, 1920, 1080);
    // Anfang Komponenten
    bSpielstarten.setLayoutX(840);
    bSpielstarten.setLayoutY(504);
    bSpielstarten.setPrefHeight(56);
    bSpielstarten.setPrefWidth(219);
    bSpielstarten.setContentDisplay(ContentDisplay.CENTER);
    bSpielstarten.setText("Spiel starten");
    bSpielstarten.setFont(Font.font("Arial Rounded MT Bold", 34));
    bSpielstarten.setOnAction(
    (event) -> {;bSpielstarten_Action(event);}
    );
    
    bSpielverlassen.setLayoutX(840);
    bSpielverlassen.setLayoutY(872);
    bSpielverlassen.setPrefHeight(73);
    bSpielverlassen.setPrefWidth(254);
    bSpielverlassen.setText("Spiel verlassen");
    bSpielverlassen.setOnAction(
    (event) -> {;bSpielverlassen_Action(event);}
    );
    bSpielverlassen.setFont(Font.font("Arial Rounded MT Bold", 34));
    bSpielverlassen.setContentDisplay(ContentDisplay.CENTER);
    // Ende Komponenten
    
    primaryStage.setTitle("Hauptmenü");
    primaryStage.setScene(hauptmenuScene);
    primaryStage.show();
    
    background.setX(0);
    background.setY(0);
    background.setFitWidth(1920);
    background.setFitHeight(1080);
    background.setImage(backgroundImage);
  }
  // Anfang Methoden
  public static void main(String[] args) {
    launch(args);
  }
  
  public void bSpielstarten_Action(Event evt) {
    
    
  }
  
  public void bSpielverlassen_Action(Event evt) {
    
    
  }
  
  // Ende Methoden
}