import java.util.ArrayList;
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
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.util.ArrayList;
import javafx.event.*;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.application.Platform;
import javafx.stage.Screen;



public class Map1 {
  // Anfang Attribute
  private Color borderColor = Color.BLACK;
  private double bildschirmBreite = Screen.getPrimary().getBounds().getWidth();
  private double bildschirmHoehe = Screen.getPrimary().getBounds().getHeight();  
  private Rectangle borderwall1 = new Rectangle();
  private Rectangle borderwall2 = new Rectangle();
  private Rectangle borderwall3 = new Rectangle();
  private Rectangle borderwall4 = new Rectangle();
  private ImageView wall1 = new ImageView();
  private ImageView wall2 = new ImageView();
  private Image wandImage = new Image(getClass().getResourceAsStream("images/wand.jpg"));
  private ImageView panzer = new ImageView();
  private Image panzerImage = new Image(getClass().getResourceAsStream("images/panzer.gif"));
  private ImageView turret = new ImageView();
  private Image turretImage = new Image(getClass().getResourceAsStream("images/Turret.png"));
  private ImageView shot;
  private ImageView gegner = new ImageView();
  private Image gegnerImage = new Image(getClass().getResourceAsStream("images/Panzer.gif"));
  private ImageView gegnerTurret = new ImageView();
  private Image gegnerTurretImage = new Image(getClass().getResourceAsStream("images/turret.png"));
  private boolean istNachgeladen = true;
  private boolean gegnerNachgeladen = true;
  private Player p1 = new Player(panzer, turret);
  private Gegner g1 = new Gegner("rot", gegnerTurret);
  //Noch private machen mit getter und setter
  public ArrayList<ImageView> schussListe = new ArrayList<ImageView>();
  public ArrayList<ImageView> wandListe = new ArrayList<ImageView>();
  public ArrayList<Rectangle> borderListe = new ArrayList<Rectangle>();
  private Button bExit = new Button();
  private Button bRestart = new Button();
  private FpsLimiter fpsLimiter = new FpsLimiter(60);
  private Stage stage1;
  // Ende Attribute
  
  public void initialize(Stage stage) { 
    stage1 = stage;
    Pane root = new Pane();
    Scene scene = new Scene(root, bildschirmBreite, bildschirmHoehe);
    // Anfang Komponenten
    stage.setX(0);
    stage.setY(0);
    stage.setWidth(bildschirmBreite);
    stage.setHeight(bildschirmHoehe);
    stage.setMaximized(false);
    stage.resizableProperty();
    stage.setFullScreenExitHint("");
    
    AnimationTimer gameplayLoop = new AnimationTimer(){
      @Override
      public void handle(long now){
        //Gegner Schuss
        if (g1.getUpdate() && gegnerNachgeladen) {
          schussErstellen(gegner, gegnerTurret);
          root.getChildren().add(shot);
          Schuss sGegner = new Schuss(gegnerTurret);
          boolean kollision = sGegner.schiessenGegner(gegner, gegnerTurret, shot);
          AnimationTimer schussTimer = new AnimationTimer() {
            @Override
            public void handle(long now2) {
              if (fpsLimiter.canRender(now2)) {
                sGegner.fliegen(shot);
                boolean treffer = g1.trefferCheck(shot, panzer);
                if (treffer == true) {
                  this.stop();
                  root.getChildren().remove(shot);
                  shot.setX(10000);
                  shot.setY(10000);
                  root.getChildren().remove(panzer);
                  panzer.setX(7000);
                  panzer.setY(7000);
                  root.getChildren().remove(turret);
                  turret.setX(5000);
                  turret.setY(5000);
                  g1.setAlive(false);
                  //Verloren Image einfügen, Restart Button einfügen
                  bRestart.setVisible(true);
                  this.stop();
                } // end of if
                boolean kollision = sGegner.kollisionsCheck(shot, wandListe, borderListe);
                if (kollision == true) {                                                                          
                  this.stop();
                  root.getChildren().remove(shot);
                  shot.setX(10000);
                  shot.setY(10000);
                }
              }
            }
          };
          schussTimer.start();
          gegnerNachgeladen = false; 
          PauseTransition nachladenGegner = new PauseTransition(Duration.seconds(2.0));
          if(gegnerNachgeladen == false) {
            nachladenGegner.play();
          }
          nachladenGegner.setOnFinished(event2 -> {;
            gegnerNachgeladen = true;
          });  
          ;  
        }     
      }
    };
    gameplayLoop.start();
    
    scene.setOnKeyPressed((KeyEvent event) -> {;
      p1.tasteGedrueckt(event);
      p1.movement(panzer, turret, wandListe, borderListe);
      g1.start(gegner, gegnerTurret, panzer, wandListe, borderListe);
    });
    
    scene.setOnKeyReleased((KeyEvent event) -> {;
      p1.tasteLosgelassen(event);
    });
    
    scene.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {;
      turret.setRotate(p1.turretRotation(event, panzer));
    });
    
    scene.setOnMouseClicked((event) -> {;
      if (event.getButton().equals(MouseButton.PRIMARY) && istNachgeladen == true) {
        //Schuss wird grafisch erstellt
        schussErstellen(panzer, turret);
        root.getChildren().add(shot);
        Schuss sPlayer = new Schuss(turret);
        //Methode für Schuss aufrufen
        boolean kollision = sPlayer.schiessen(event, shot);
        AnimationTimer schussTimer = new AnimationTimer() {
          @Override
          public void handle(long now) {
            if (fpsLimiter.canRender(now)) {
              sPlayer.fliegen(shot);
              boolean treffer = p1.trefferCheck(shot, gegner);
              if (treffer == true) {
                this.stop();
                root.getChildren().remove(shot);
                shot.setX(10000);
                shot.setY(10000);
                root.getChildren().remove(gegner);
                gegner.setX(10000);
                gegner.setY(10000);
                root.getChildren().remove(gegnerTurret);
                gegnerTurret.setX(5000);
                gegnerTurret.setY(5000);
                g1.setAlive(false);
                //Gewonnen Image einfügen, Restart Button einfügen
                bRestart.setVisible(true);
              } // end of if
              //ArrayList gemacht, muss angepasst werden!
              boolean kollision = sPlayer.kollisionsCheck(shot, wandListe, borderListe);
              if (kollision == true) {
                this.stop();
                root.getChildren().remove(shot);
                shot.setX(10000);
                shot.setY(10000);
              }
            }
          }
          
        };
        schussTimer.start();
        istNachgeladen = false; 
        PauseTransition nachladen = new PauseTransition(Duration.seconds(1.5));
        if(istNachgeladen == false) {
          nachladen.play();
        }
        nachladen.setOnFinished(event2 -> {
          istNachgeladen = true;
        });  
        ;
      }
    });
    
    
    //Border erstellen
    borderwall1.setWidth(4000);
    borderwall1.setHeight(1);
    borderwall1.setX(1);
    borderwall1.setY(1);
    borderwall1.setSmooth(false);
    root.getChildren().add(borderwall1);
    borderwall1.setStroke(borderColor);
    borderListe.add(borderwall1);
    
    borderwall2.setWidth(20);
    borderwall2.setHeight(3000);
    borderwall2.setX(bildschirmBreite-1);
    borderwall2.setY(1);
    borderwall2.setSmooth(false);
    root.getChildren().add(borderwall2);
    borderwall2.setStroke(borderColor);
    borderListe.add(borderwall2);
    
    borderwall3.setWidth(4000);
    borderwall3.setHeight(20);
    borderwall3.setX(0);
    borderwall3.setY(bildschirmHoehe-1);
    borderwall3.setSmooth(false);
    root.getChildren().add(borderwall3);
    borderwall3.setStroke(borderColor);
    borderListe.add(borderwall3);
    
    borderwall4.setWidth(1);
    borderwall4.setHeight(3000);
    borderwall4.setX(1);
    borderwall4.setY(1);
    borderwall4.setSmooth(false);
    root.getChildren().add(borderwall4);
    borderwall4.setStroke(borderColor);
    borderListe.add(borderwall4);
    
    //Anfang Wände
    wall1.setX(620);
    wall1.setY(150);
    wall1.setFitWidth(120);
    wall1.setFitHeight(400);
    wall1.setSmooth(false);
    wall1.setImage(wandImage);
    root.getChildren().add(wall1);
    wandListe.add(wall1);
    
    wall2.setX(520);
    wall2.setY(150);
    wall2.setFitWidth(110);
    wall2.setFitHeight(400);
    wall2.setSmooth(false);
    wall2.setImage(wandImage);
    root.getChildren().add(wall2);
    wandListe.add(wall2);
    //Ende Wände
    
    //Panzer erstellen
    panzer.setX(70);
    panzer.setY(300);
    panzer.setFitWidth(150);
    panzer.setFitHeight(125);
    panzer.setImage(panzerImage);
    root.getChildren().add(panzer);
    
    stage.setResizable(false);
    
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
    
    gegnerTurret.setX(868);
    gegnerTurret.setY(320);
    gegnerTurret.setFitWidth(150);
    gegnerTurret.setFitHeight(100);
    gegnerTurret.setRotate(180);
    gegnerTurret.setImage(gegnerTurretImage);
    root.getChildren().add(gegnerTurret);
    
    bRestart.setLayoutX(600);
    bRestart.setLayoutY(600);
    bRestart.setPrefHeight(48);
    bRestart.setPrefWidth(160);
    bRestart.setText("Restart");
    bRestart.setOnAction(
    (event) -> {bRestart_Action(event);} 
    );
    bRestart.setFont(Font.font("Dialog", 32));
    root.getChildren().add(bRestart);
    bRestart.setVisible(false);
    
    bExit.setLayoutX(1120);
    bExit.setLayoutY(0);
    bExit.setPrefHeight(24);
    bExit.setPrefWidth(80);
    bExit.setText("Exit");
    bExit.setOnAction(
    (event) -> {bExit_Action(event);} 
    );
    bExit.setFont(Font.font("Dialog", 16));
    root.getChildren().add(bExit);
    // Ende Komponenten
    stage.setOnCloseRequest(e -> System.exit(0));
    stage.setTitle("Wee Tanks");
    stage.setScene(scene);
    stage.show();
  } // end of public Map1
  
  public void schussErstellen(ImageView panzer, ImageView turret){
    shot = new ImageView();
    Image shotImage = new Image(getClass().getResourceAsStream("images/bullet.png"));
    shot.setX(panzer.getX() + (panzer.getFitWidth()/2) - 40);
    shot.setY(panzer.getY() + (panzer.getFitHeight()/2) - 15);
    shot.setFitHeight(30);
    shot.setFitWidth(80);
    shot.setRotate(turret.getRotate());
    shot.setImage(shotImage);
    //root.getChildren().add(shot);
    addSchuss(shot);
  }
  
  public void addSchuss(ImageView schuss){
    schussListe.add(schuss);
  }
  
  public void removeSchuss(ImageView schuss){
    schussListe.remove(schuss);
  }
  
  public void bRestart_Action(Event evt) {
    initialize(stage1);
    
  }
  
  public void bExit_Action(Event evt) {
    Platform.exit();
  }

  // Ende Methoden
} // end of class Map1
  