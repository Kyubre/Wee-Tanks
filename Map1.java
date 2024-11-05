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


public class Map1 extends Application {
  // Anfang Attribut
  // Anfang Attribute
  private Rectangle border = new Rectangle();
  private Rectangle wall1 = new Rectangle();
  private Rectangle wall2 = new Rectangle();
  private ImageView panzer = new ImageView();
  private Image panzerImage = new Image(getClass().getResourceAsStream("images/96.gif"));
  private Player p1 = new Player(panzer);
  private boolean wGedrueckt = false;
  private boolean aGedrueckt = false;
  private boolean sGedrueckt = false;
  private boolean dGedrueckt = false;
  private ImageView turret = new ImageView();
  private Image turretImage = new Image(getClass().getResourceAsStream("images/96.png"));
  private ImageView shot;
  // Ende Attribute
  
  public void start(Stage primaryStage) { 
    Pane root = new Pane();
    Scene scene = new Scene(root, 1904, 1042);
    // Anfang Komponenten
    primaryStage.setMaximized(true);
    primaryStage.resizableProperty();
    
    scene.setOnKeyPressed((KeyEvent event) -> {
      switch (event.getCode()) {
        case W: 
          wGedrueckt = true;
          break;
        case A: 
          aGedrueckt = true;
          break;
        case S: 
          sGedrueckt = true;
          break;
        case D: 
          dGedrueckt = true;
          break;
        default: 
          break;  
      } // end of switch
      updateMovement();
    });
    
    scene.setOnKeyReleased((KeyEvent event) -> {
      switch (event.getCode()) {
        case W: 
          wGedrueckt = false;
          break;
        case A: 
          aGedrueckt = false;
          break;
        case S: 
          sGedrueckt = false;
          break;
        case D: 
          dGedrueckt = false;
          break;
        default: 
          break;  
      }
      updateMovement();
    });
    
    scene.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {;
      turret.setRotate(p1.turretRotation(event, panzer));
    });
    
    scene.setOnMouseClicked((event) -> {;
      if (event.getButton().equals(MouseButton.PRIMARY)) {
        shot = new ImageView();
        Image shotImage = new Image(getClass().getResourceAsStream("images/96.png"));
        shot.setX(turret.getX()+turret.getFitWidth());
        shot.setY(turret.getY()+turret.getFitHeight());
        shot.setFitHeight(30);
        shot.setFitWidth(80);
        shot.setRotate(turret.getRotate());
        shot.setImage(shotImage);
        root.getChildren().add(shot);
      } // end of if
    });
    
    
    border.setX(scene.getWidth()-border.getWidth());
    border.setY(scene.getHeight()-border.getHeight());
    border.setWidth(1920);
    border.setHeight(1080);
    root.getChildren().add(border);
    Color color = Color.TRANSPARENT;
    border.setFill(color);
    color = Color.BLACK;
    border.setStroke(color);
    border.setStrokeWidth(5.0);
    
    wall1.setX(480);
    wall1.setY(144);
    wall1.setWidth(120);
    wall1.setHeight(528);
    wall1.setSmooth(false);
    root.getChildren().add(wall1);
    
    wall2.setX(592);
    wall2.setY(560);
    wall2.setWidth(648);
    wall2.setHeight(120);
    wall2.setSmooth(false);
    root.getChildren().add(wall2);
    panzer.setX(1448);
    panzer.setY(120);
    panzer.setFitWidth(120);
    panzer.setFitHeight(100);
    panzer.setImage(panzerImage);
    root.getChildren().add(panzer);
    primaryStage.setResizable(false);
    
    turret.setX(1448);
    turret.setY(128);
    turret.setFitWidth(120);
    turret.setFitHeight(80);
    turret.setImage(turretImage);
    root.getChildren().add(turret);
    // Ende Komponenten
    primaryStage.setOnCloseRequest(e -> System.exit(0));
    primaryStage.setTitle("Wee Tanks");
    primaryStage.setScene(scene);
    primaryStage.show();
  } // end of public Map1
  
  // Anfang Methoden
  public void updateMovement() {
    
    if (wGedrueckt && aGedrueckt) {
      panzer.setY(panzer.getY()-10);
      panzer.setX(panzer.getX()-10);
      panzer.setRotate(225);         
      turret.setY(turret.getY()-10);
      turret.setX(turret.getX()-10);
    } 
    else if (wGedrueckt && dGedrueckt) {
      panzer.setY(panzer.getY()-10);
      panzer.setX(panzer.getX()+10);
      panzer.setRotate(315);
      turret.setY(turret.getY()-10);
      turret.setX(turret.getX()+10);
    } 
    else if (sGedrueckt && aGedrueckt) {
      panzer.setY(panzer.getY()+10);
      panzer.setX(panzer.getX()-10);
      panzer.setRotate(135);
      turret.setY(turret.getY()+10);
      turret.setX(turret.getX()-10);
    } 
    else if (sGedrueckt && dGedrueckt) {
      panzer.setY(panzer.getY()+10);
      panzer.setX(panzer.getX()+10);
      panzer.setRotate(45);
      turret.setY(turret.getY()+10);
      turret.setX(turret.getX()+10);
    } 
    else if (wGedrueckt) {
      panzer.setY(panzer.getY()-10);
      panzer.setRotate(270);
      turret.setY(turret.getY()-10);
    } 
    else if (aGedrueckt) {
      panzer.setX(panzer.getX()-10);
      panzer.setRotate(180);
      turret.setX(turret.getX()-10);
    } 
    else if (sGedrueckt) {
      panzer.setY(panzer.getY()+10);
      panzer.setRotate(90);
      turret.setY(turret.getY()+10);
    } 
    else if (dGedrueckt) {
      panzer.setX(panzer.getX()+10);
      panzer.setRotate(0);
      turret.setX(turret.getX()+10);
    }
  }

  
  public static void main(String[] args) {
    launch(args);
  } // end of main
  
  // Ende Methoden
} // end of class Map1
