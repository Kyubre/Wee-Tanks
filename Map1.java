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
import javafx.event.*;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.application.Platform;
import javafx.stage.Screen;
import java.util.HashMap;

public class Map1 {
  private double bildschirmBreite = Screen.getPrimary().getBounds().getWidth();
  private double bildschirmHoehe = Screen.getPrimary().getBounds().getHeight();  
  private ImageView turret = new ImageView();
  private Image turretImage = new Image(getClass().getResourceAsStream("images/Turret.png"));
  private ImageView gegner;
  private Image gegnerImage = new Image(getClass().getResourceAsStream("images/Panzer.png"));
  private ImageView gegnerTurret = new ImageView();
  private Image gegnerTurretImage = new Image(getClass().getResourceAsStream("images/turret.png"));
  private boolean istNachgeladen = true;
  private boolean gegnerNachgeladen = true;
  private ArrayList<ImageView> schuesse = new ArrayList<ImageView>();
  private HashMap<ImageView, Schuss> schussDaten = new HashMap<>();
  private ArrayList<ImageView> wandListe;
  private ArrayList<Rectangle> borderListe;
  private Button bExit = new Button();
  private Button bRestart = new Button();
  private FpsLimiter fpsLimiter = new FpsLimiter(60);
  private MapGeneration generation;
  private ImageView panzer;
  private Player p1;
  private Gegner g1;
  private Pane root;
  private Stage stage1;
  private static int level;
  
  public void initialize(Stage stage) {
    level++;
    stage1 = stage;
    generation = new MapGeneration();
    root = new Pane();
    root = generation.generateMap(root);
    Scene scene = new Scene(root, bildschirmBreite, bildschirmHoehe);
    wandListe = generation.getWandListe();
    borderListe = generation.getBorderListe();
    panzer = generation.getPanzer();
    gegner = generation.getGegner();
    p1 = new Player(panzer, turret);
    g1 = new Gegner("rot", generation.getGegner(), gegnerTurret);
    // Anfang Komponenten
    stage.setX(0);
    stage.setY(0);
    stage.setWidth(bildschirmBreite);
    stage.setHeight(bildschirmHoehe);
    stage.setMaximized(false);
    stage.resizableProperty();
    stage.setFullScreenExitHint("");
    System.out.println("Das ist Level: " + level);
    g1.start(gegner, gegnerTurret, panzer, wandListe, borderListe);
    
    AnimationTimer gameplayLoop = new AnimationTimer(){
      @Override
      public void handle(long now){
        //Gegner Schuss
        if (g1.getUpdate() && gegnerNachgeladen) {
          ImageView gegnerSchuss = gegnerSchussErstellen(gegner, gegnerTurret);
          root.getChildren().add(gegnerSchuss);
          Schuss sGegner = new Schuss(gegnerTurret, false);
          addSchuss(gegnerSchuss, sGegner);          
          boolean kollision = false;
          AnimationTimer schussTimer = new AnimationTimer() {
            @Override
            public void handle(long now2) {
              if (fpsLimiter.canRender(now2)) {
                sGegner.fliegen(gegnerSchuss, wandListe, borderListe);
                boolean treffer = sGegner.trefferCheck(gegnerSchuss, panzer);
                if (treffer == true) {
                  this.stop();
                  root.getChildren().remove(gegnerSchuss);
                  gegnerSchuss.setX(10000);
                  gegnerSchuss.setY(10000);
                  root.getChildren().remove(panzer);
                  panzer.setX(7000);
                  panzer.setY(7000);
                  root.getChildren().remove(turret);
                  turret.setX(5000);
                  turret.setY(5000);
                  g1.setAlive(false);
                  //Verloren Image einfügen, Restart Button einfügen
                  bRestart.setVisible(true);
                } // end of if
                boolean kollision = sGegner.kollisionsCheck(gegnerSchuss, wandListe, borderListe);
                if (sGegner.getBounces() == 3) {                                                                          
                  this.stop();
                  root.getChildren().remove(gegnerSchuss);
                  gegnerSchuss.setX(10000);
                  gegnerSchuss.setY(10000);
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
    
    AnimationTimer schussAnimation = new AnimationTimer() {
      @Override
      public void handle(long now) {
        if (fpsLimiter.canRender(now)) {
          for (int i = 0; i < schuesse.size(); i++) {
            ImageView schuss = schuesse.get(i);
            Schuss sObj = schussDaten.get(schuss);
            sObj.fliegen(schuss, wandListe, borderListe);
            
            if (sObj.getSpieler()) {
              boolean treffer = sObj.trefferCheck(schuss, gegner);
              if (treffer) {
                root.getChildren().remove(schuss);
                removeSchuss(schuss);
                root.getChildren().remove(gegner);
                gegner.setX(-100);
                gegner.setY(0);
                root.getChildren().remove(gegnerTurret);
                gegnerTurret.setX(-100);
                gegnerTurret.setY(0);
                g1.setAlive(false);
                i--;
                continue;
              }
                          
              boolean kollision = sObj.kollisionsCheck(schuss, wandListe, borderListe);
              if (sObj.getBounces() == 3) {
                root.getChildren().remove(schuss);
                removeSchuss(schuss);
                i--;
              }
            }
            
            else {
              
              boolean treffer = sObj.trefferCheck(schuss, panzer);
              if (treffer) {
                root.getChildren().remove(schuss);
                removeSchuss(schuss);
                root.getChildren().remove(panzer);
                root.getChildren().remove(turret);
                i--; 
                continue;
              }
              
              boolean kollision = sObj.kollisionsCheck(schuss, wandListe, borderListe);
              if (sObj.getBounces() == 3 ) {
                root.getChildren().remove(schuss);
                removeSchuss(schuss);
                i--;
              }
            }
          }
          
        }
      }
    };
    schussAnimation.start();
    
    scene.setOnKeyPressed((KeyEvent event) -> {;
      p1.tasteGedrueckt(event);
      p1.movement(panzer, turret, wandListe, borderListe);
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
        ImageView schussNeu = schussErstellen(panzer, turret);
        root.getChildren().add(schussNeu);
        Schuss sPlayer = new Schuss(turret, true);
        addSchuss(schussNeu, sPlayer);
        //Methode für Schuss aufrufen
        AnimationTimer schussTimer = new AnimationTimer() {
          @Override
          public void handle(long now) {
            if (fpsLimiter.canRender(now)) {
              sPlayer.fliegen(schussNeu, wandListe, borderListe);
              boolean treffer = sPlayer.trefferCheck(schussNeu, gegner);
              if (treffer == true) {
                this.stop();
                root.getChildren().remove(schussNeu);
                removeSchuss(schussNeu);
                schussNeu.setX(10000);
                schussNeu.setY(10000);
                root.getChildren().remove(gegner);
                gegner.setX(10000);
                gegner.setY(10000);
                root.getChildren().remove(gegnerTurret);
                gegnerTurret.setX(5000);
                gegnerTurret.setY(5000);
                g1.setAlive(false);
                //Gewonnen Image einfügen, Restart Button einfügen
                //bRestart.setVisible(true);
                this.stop();
                gameplayLoop.stop();
                
              } // end of if
              //ArrayList gemacht, muss angepasst werden!
              boolean kollision = sPlayer.kollisionsCheck(schussNeu, wandListe, borderListe);
              if (sPlayer.getBounces() == 3 ) {
                this.stop();
                root.getChildren().remove(schussNeu);
                removeSchuss(schussNeu);
                schussNeu.setX(10000);
                schussNeu.setY(10000);
                this.stop();
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
    
    //Turret erstellen 
    turret.setX(panzer.getX());
    turret.setY(panzer.getY());
    turret.setFitWidth(114);
    turret.setFitHeight(50);
    turret.setImage(turretImage);
    root.getChildren().add(turret);
    
    gegnerTurret.setX(gegner.getX());
    gegnerTurret.setY(gegner.getY());
    gegnerTurret.setFitWidth(114);
    gegnerTurret.setFitHeight(50);
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
    bRestart.setVisible(true);
    
    bExit.setLayoutX(bildschirmBreite - 80);
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
  } 
  
  public ImageView schussErstellen(ImageView panzer, ImageView turret){
    ImageView shot = new ImageView();
    Image shotImage = new Image(getClass().getResourceAsStream("images/bullet.png"));
    shot.setX(panzer.getX() + (panzer.getFitWidth()/2) - 10);
    shot.setY(panzer.getY() + (panzer.getFitHeight()/2) - 3);
    shot.setFitHeight(7);
    shot.setFitWidth(20);
    shot.setRotate(turret.getRotate());
    shot.setImage(shotImage);
    return shot;
  }
  
  public ImageView gegnerSchussErstellen(ImageView gegner, ImageView gegnerTurret){
    ImageView shot = new ImageView();
    Image shotImage = new Image(getClass().getResourceAsStream("images/bullet.png"));
    shot.setX(gegner.getX() + (gegner.getFitWidth()/2) - 10);
    shot.setY(gegner.getY() + (gegner.getFitHeight()/2) - 3);
    shot.setFitHeight(7);
    shot.setFitWidth(20);
    shot.setRotate(gegnerTurret.getRotate());
    shot.setImage(shotImage);
    return shot;
  }
  
  public void addSchuss(ImageView schuss, Schuss schussObjekt){
    schuesse.add(schuss);
    schussDaten.put(schuss, schussObjekt);
  }
  
  public void removeSchuss(ImageView schuss){
    schuesse.remove(schuss);
    schussDaten.remove(schuss);
  }
  
  public void bRestart_Action(Event evt) {
    initialize(stage1);
    stage1.setFullScreen(true);
    stage1.setFullScreenExitHint("");    
  }
  
  public void bExit_Action(Event evt) {
    Platform.exit();
  }
}