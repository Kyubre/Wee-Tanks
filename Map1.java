import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.ImageView;
import javafx.animation.AnimationTimer;


public class Map1 extends Application {
  // Anfang Attribut
  // Anfang Attribute
  private Rectangle border = new Rectangle();
  private Rectangle wall1 = new Rectangle();
  private Rectangle wall2 = new Rectangle();
  private ImageView panzer = new ImageView();
  private Image panzerImage = new Image(getClass().getResourceAsStream("images/panzer.gif"));
  private Player p1 = new Player(panzer);
  private Gegner g1 = new Gegner();
  private ImageView turret = new ImageView();
  private Image turretImage = new Image(getClass().getResourceAsStream("images/turret.png"));
  private ImageView shot;
  private ImageView gegner = new ImageView();
  // Ende Attribute
  
  public void start(Stage primaryStage) { 
    Pane root = new Pane();
    Scene scene = new Scene(root, 1920, 1080);
    // Anfang Komponenten
    primaryStage.setMaximized(true);
    primaryStage.resizableProperty();
    
    scene.setOnKeyPressed((KeyEvent event) -> {;
      p1.tasteGedrueckt(event);
      p1.movement(panzer, turret);
    });
    
    scene.setOnKeyReleased((KeyEvent event) -> {;
      p1.tasteLosgelassen(event);
      p1.movement(panzer, turret);
    });
    
    scene.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {;
      turret.setRotate(p1.turretRotation(event, panzer));
    });
    
    scene.setOnMouseClicked((event) -> {;
      if (event.getButton().equals(MouseButton.PRIMARY)) {
        //Schuss wird grafisch erstellt
        shot = new ImageView();
        Image shotImage = new Image(getClass().getResourceAsStream("images/turret.png"));
        shot.setX(turret.getX());
        shot.setY(turret.getY());
        shot.setFitHeight(30);
        shot.setFitWidth(80);
        shot.setRotate(turret.getRotate());
        shot.setImage(shotImage);
        root.getChildren().add(shot);
        //Methode für Schuss aufrufen
        boolean kollision = p1.schießen(event, shot);
        //Idk man timer ist halt besser als while, außerdem frag nicht was das macht hab selber keine ahnung
        AnimationTimer schussTimer = new AnimationTimer() {
          @Override
          public void handle(long now) {
            boolean treffer = p1.trefferCheck(shot, gegner);
            if (treffer == true) {
              this.stop();
              root.getChildren().remove(shot);
              root.getChildren().remove(gegner);
            } // end of if
            boolean kollision = p1.kollisionsCheck(shot, wall1, wall2, border);
            if (kollision == true) {
              this.stop();
              root.getChildren().remove(shot);
            }
          }
          
        };
        schussTimer.start();  
      }
    });
    
    
    //Border, also Rand erstellen
    //Später ersetzen durch ImageViews (Sobald Rand Images erstellt sind)
    border.setWidth(1920);
    border.setHeight(1080);
    border.setX((scene.getWidth())-border.getWidth());
    border.setY((scene.getHeight())-border.getHeight());
    root.getChildren().add(border);
    Color color = Color.TRANSPARENT;
    border.setFill(color);
    color = Color.BLACK;
    border.setStroke(color);
    border.setStrokeWidth(5.0);
    
    //Anfang Wände
    wall1.setX(480);
    wall1.setY(144);
    wall1.setWidth(120);
    wall1.setHeight(528);
    wall1.setSmooth(false);
    root.getChildren().add(wall1);
    
    wall2.setX(592);
    wall2.setY(552);
    wall2.setWidth(648);
    wall2.setHeight(120);
    wall2.setSmooth(false);
    root.getChildren().add(wall2);
    //Ende Wände
    
    //Panzer erstellen
    panzer.setX(1448);
    panzer.setY(120);
    panzer.setFitWidth(120);
    panzer.setFitHeight(100);
    panzer.setImage(panzerImage);
    root.getChildren().add(panzer);
    
    gegner.setX(40);
    gegner.setY(800);
    gegner.setFitWidth(120);
    gegner.setFitHeight(100);
    gegner.setImage(panzerImage);
    root.getChildren().add(gegner);
    
    primaryStage.setResizable(false);
    
    //Turret erstellen
    turret.setX(1448);
    turret.setY(128);
    turret.setFitWidth(120);
    turret.setFitHeight(80);
    turret.setImage(turretImage);
    root.getChildren().add(turret);
    
    primaryStage.setOnCloseRequest(e -> System.exit(0));
    primaryStage.setTitle("Wee Tanks");
    primaryStage.setScene(scene);
    primaryStage.show();
  } // end of public Map1
  
  // Anfang Methoden
  
  public static void main(String[] args) {
    launch(args);
  } // end of main
  
  // Ende Methoden
} // end of class Map1
  