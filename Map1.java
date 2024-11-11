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
  private Image gegnerImage = new Image(getClass().getResourceAsStream("images/panzer.gif"));
  // Ende Attribute
  
  public void start(Stage primaryStage) { 
    Pane root = new Pane();
    Scene scene = new Scene(root, 1200, 800);
    // Anfang Komponenten
    primaryStage.setMaximized(false);
    primaryStage.resizableProperty();
    
    scene.setOnKeyPressed((KeyEvent event) -> {;
      p1.tasteGedrueckt(event);
      p1.movement(panzer, turret, wall1, wall2, border);
    });
    
    scene.setOnKeyReleased((KeyEvent event) -> {;
      p1.tasteLosgelassen(event);
    });
    
    scene.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {;
      turret.setRotate(p1.turretRotation(event, panzer));
    });
    
    scene.setOnMouseClicked((event) -> {;
      if (event.getButton().equals(MouseButton.PRIMARY)) {
        //Schuss löschen, falls noch einer existiert
        for (int i = 0;i < root.getChildren().size(); i++) {
          if (root.getChildren().get(i).equals(shot)) {
            root.getChildren().remove(shot);
          }
        }
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
        AnimationTimer schussTimer = new AnimationTimer() {
          @Override
          public void handle(long now) {
            boolean treffer = p1.trefferCheck(shot, gegner);
            if (treffer == true) {
              this.stop();
              root.getChildren().remove(shot);
              shot.setX(10000);
              shot.setY(10000);
              root.getChildren().remove(gegner);
              gegner.setX(10000);
              gegner.setY(10000);
              //Gewonnen Image einfügen, Restart Button einfügen
            } // end of if
            boolean kollision = p1.kollisionsCheck(shot, wall1, wall2, border);
            if (kollision == true) {
              this.stop();
              root.getChildren().remove(shot);
              shot.setX(10000);
              shot.setY(10000);
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
    wall1.setX(620);
    wall1.setY(150);
    wall1.setWidth(120);
    wall1.setHeight(400);
    wall1.setSmooth(false);
    root.getChildren().add(wall1);
    
    wall2.setX(520);
    wall2.setY(150);
    wall2.setWidth(110);
    wall2.setHeight(400);
    wall2.setSmooth(false);
    root.getChildren().add(wall2);
    //Ende Wände
    
    //Panzer erstellen
    panzer.setX(70);
    panzer.setY(300);
    panzer.setFitWidth(150);
    panzer.setFitHeight(125);
    panzer.setImage(panzerImage);
    root.getChildren().add(panzer);
    
    primaryStage.setResizable(false);
    
    //Turret erstellen
    turret.setX(68);
    turret.setY(310);
    turret.setFitWidth(150);
    turret.setFitHeight(100);
    turret.setImage(turretImage);
    root.getChildren().add(turret);
    
    gegner.setX(870);
    gegner.setY(310);
    gegner.setFitWidth(180);
    gegner.setFitHeight(120);
    gegner.setImage(gegnerImage);
    root.getChildren().add(gegner);
    // Ende Komponenten
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
  