import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.KeyEvent;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.stage.Screen;
import java.util.HashMap;
import javafx.scene.input.KeyCombination;
public class Map {
  private double bildschirmBreite = Screen.getPrimary().getBounds().getWidth();
  private double bildschirmHoehe = Screen.getPrimary().getBounds().getHeight();
  private double multi = (bildschirmBreite / 1920.0);
  private static double speedPowerUpValue = 1.0;
  private double reloadPowerUpValue = 1.5;
  private ImageView turret;
  private boolean istNachgeladen = true;
  private boolean gegnerNachgeladen = true;
  private ArrayList<ImageView> schuesse = new ArrayList<ImageView>();
  private HashMap<ImageView, Schuss> schussDaten = new HashMap<>();
  private ArrayList<ImageView> wandListe;
  private ArrayList<Rectangle> borderListe;
  private FpsLimiter fpsLimiter = new FpsLimiter(60);
  private ArrayList<Gegner> gegnerListe = new ArrayList<Gegner>();
  private MapGeneration generation;
  private ImageView panzer;
  private Player p1;
  private Pane root;
  private Stage stage1;
  private static int level = 0;
  private boolean restart = false;
  private static boolean godmode = false;

  public void initialize(Stage stage) {
    level++;
    stage1 = stage;
    generation = new MapGeneration();
    root = new Pane();
    root = generation.generateMap(root);
    Scene scene = new Scene(root, bildschirmBreite, bildschirmHoehe);
    wandListe = generation.getWandListe();
    borderListe = generation.getBorderListe();
    gegnerListe = generation.getGegnerListe();
    panzer = generation.getPanzer();
    turret = generation.getTurret();
    p1 = new Player(panzer, turret);
    // fängt an die hintergrundmusik abzuspielen
    //if (Sounds.getMusicStarted() == false) {
    //  Sounds.bgmAbspielen();
    //} else {
    //  Sounds.resumeBgmMusic();
    //}

    stage.setX(0);
    stage.setY(0);
    stage.setWidth(bildschirmBreite);
    stage.setHeight(bildschirmHoehe);
    stage.setMaximized(false);
    stage.resizableProperty();
    stage.setFullScreenExitHint("");
    stage.setFullScreen(true);
    stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Disable Escape key for exiting fullscreen
    for(Gegner g : gegnerListe){
      g.start(panzer, wandListe, borderListe);
    }
    

    AnimationTimer gameplayLoop = new AnimationTimer() {
      @Override
      public void handle(long now) {
        // Spiel vorbei
        boolean einerLebt = false;
        for(Gegner g : gegnerListe){
          if (g.getAlive()) {
            einerLebt = true;
          }
        }
        if (!einerLebt && !restart) {
          bRestart_Action(true);
          restart = true;
        }
        

        // Gegner Schuss
        for (Gegner g : gegnerListe) {
          if (g.getUpdate() && gegnerNachgeladen) {
            ImageView gegnerSchuss = gegnerSchussErstellen(g);
            root.getChildren().add(gegnerSchuss);
            Schuss sGegner;
            if (g.getColor().equals("lila")) {
              sGegner = new Schuss(g.getGegnerTurret(), false, true);
            } else {
              sGegner = new Schuss(g.getGegnerTurret(), false, false);
            }
  
            addSchuss(gegnerSchuss, sGegner);
            g.setNachgeladen(false);
            g.setUpdate(false); // Reset update after shooting
          }
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
            sObj.fliegen(schuss, panzer, wandListe, borderListe);

            if (sObj.getLebenszeit() >= 15000) {
              root.getChildren().remove(schuss);
              removeSchuss(schuss);
              i--;
              continue;
            }
            sObj.erhoeheLebenszeit(1000 / fpsLimiter.getFps());

            if (sObj.getSpieler()) {
              Gegner getroffen = sObj.playerTrefferCheck(schuss, gegnerListe);
              if (getroffen != null) {
                root.getChildren().remove(schuss);
                removeSchuss(schuss);
                root.getChildren().remove(getroffen.getImage());
                getroffen.getImage().setX(-100);
                getroffen.getImage().setY(0);
                root.getChildren().remove(getroffen.getGegnerTurret());
                getroffen.getGegnerTurret().setX(-100);
                getroffen.getGegnerTurret().setY(0);
                getroffen.setAlive(false);
                i--;
                continue;
              }

              sObj.kollisionsCheck(schuss, wandListe, borderListe);
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
                if (!godmode && !restart) {
                  root.getChildren().remove(panzer);
                  root.getChildren().remove(turret);
                  p1.setAlive(false);
                  bRestart_Action(false);
                  restart = true;
                }
                i--;
                continue;
              }

              sObj.kollisionsCheck(schuss, wandListe, borderListe);
              if (sObj.getBounces() == 3) {
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

    scene.setOnKeyPressed((KeyEvent event) -> {
      p1.tasteGedrueckt(event);
      p1.movement(panzer, turret, wandListe, borderListe);
      powerUpCollision();
    });

    scene.setOnKeyReleased((KeyEvent event) -> {
      p1.tasteLosgelassen(event);
    });

    scene.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
      turret.setRotate(p1.turretRotation(event, panzer));
    });

    scene.setOnMouseClicked((event) -> {
      if (event.getButton().equals(MouseButton.PRIMARY) && istNachgeladen == true) {
        // Schuss wird erstellt
        ImageView schussNeu = schussErstellen(panzer, turret);
        root.getChildren().add(schussNeu);
        Schuss sPlayer = new Schuss(turret, true, false);
        addSchuss(schussNeu, sPlayer);
        // Nachladen
        istNachgeladen = false;
        PauseTransition nachladen = new PauseTransition(Duration.seconds(reloadPowerUpValue));
        if (istNachgeladen == false) {
          nachladen.play();
        }
        nachladen.setOnFinished(event2 -> {
          istNachgeladen = true;
        });

      }

      else if (event.getButton().equals(MouseButton.SECONDARY)) {
        for(Gegner g : gegnerListe){
          g.setAlive(false);
        }
        
      }
    });

    scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
      if (event.getCode().toString().equals("ESCAPE")) {
        event.consume(); // Prevent exiting fullscreen
      }
    });


    // Ende Komponenten
    stage.setOnCloseRequest(e -> {
      aufraeumen();
      System.exit(0);
    });
    stage.setTitle("Wee Tanks");
    stage.setScene(scene);
    stage.show();
  }

  private void aufraeumen() {
    for (Gegner g : gegnerListe) {
      g.stopAI();
    }

    schuesse.clear();
    schussDaten.clear();
    gegnerListe.clear();
    wandListe.clear();
    borderListe.clear();

    // Remove all children from the root pane
    root.getChildren().clear();
  }

  public void resetLevel() {
    level = 0;
  }

  public static int getLevel() {
    return level;
  }

  public static void quit(Stage stage2) {
    if (stage2 != null) {
      level = 0;
      Hauptmenu h = new Hauptmenu();
      h.start(stage2);
      stage2.setFullScreen(true);
      stage2.setFullScreenExitHint("");
    } else {
      System.out.println("stage1 ist null");
    }
  }

  public ImageView schussErstellen(ImageView panzer, ImageView turret) {
    ImageView shot = new ImageView();
    Image shotImage = new Image(getClass().getResourceAsStream("src/assets/images/bullet.png"));
    shot.setX(panzer.getX() + (panzer.getFitWidth() / 2) - 10);
    shot.setY(panzer.getY() + (panzer.getFitHeight() / 2) - 3);
    shot.setFitHeight(7 * multi);
    shot.setFitWidth(20 * multi);
    shot.setRotate(turret.getRotate());
    shot.setImage(shotImage);
    Sounds.schussSound();
    return shot;
  }

  public ImageView gegnerSchussErstellen(Gegner gegner) {
    ImageView shot = new ImageView();
    Image shotImage = new Image(getClass().getResourceAsStream("src/assets/images/bullet_" + gegner.getColor() + ".png"));
    shot.setX(gegner.getImage().getX() + (gegner.getImage().getFitWidth() / 2) - 11);
    shot.setY(gegner.getImage().getY() + (gegner.getImage().getFitHeight() / 2) + 4);
    shot.setFitHeight(7 * multi);
    shot.setFitWidth(20 * multi);
    shot.setRotate(gegner.getGegnerTurret().getRotate());
    shot.setImage(shotImage);
    Sounds.schussSound();
    return shot;
  }

  public void addSchuss(ImageView schuss, Schuss schussObjekt) {
    schuesse.add(schuss);
    schussDaten.put(schuss, schussObjekt);
  }

  public void removeSchuss(ImageView schuss) {
    schuesse.remove(schuss);
    schussDaten.remove(schuss);
  }

  public void bRestart_Action(boolean win) {
    aufraeumen();
    Settings.highscore = level;
    Sounds.pauseBgmMusic();
    PostGame postGame = new PostGame(level, win);
    postGame.initialize(stage1);
  }

  public static void setGodmode(boolean neu) {
    godmode = neu;
  }

  public static boolean getGodmode() {
    return godmode;
  }

  public static double getSpeedPowerUpValue() {
    return speedPowerUpValue;
  }

  public double getReloadPowerUpValue() {
    return reloadPowerUpValue;
  }

  public Map() {

  }

  public void powerUpCollision() {

    ArrayList<ImageView> powerUps = generation.getPowerUps();
    for (int i = 0; i < powerUps.size(); i++) {
      ImageView powerUp = powerUps.get(i);
      if (panzer.getBoundsInParent().intersects(powerUp.getBoundsInParent())) {
        String powerUpType = powerUp.getId();
        System.out.println(powerUpType + " power-up collected!");

        switch (powerUpType) {
          case "speed":
          speedPowerUpValue = 1.5;
          System.out.println("Speed boost activated!");

          // Start a 10-second timer to reset the speed
          new Timeline(new KeyFrame(Duration.seconds(10), e -> {
            speedPowerUpValue = 1.0; // Reset
            System.out.println("Speed boost ended!");
          })).play();
            break;
        
          case "reload":
          reloadPowerUpValue = 0.5;
          System.out.println("Reload boost activated!");

          // Start a 10-second timer to reset the reload time
          new Timeline(new KeyFrame(Duration.seconds(10), e -> {
            reloadPowerUpValue = 1.5; // Reset
            System.out.println("Reload boost ended!");
          })).play();
            break;  
          default:
            break;
        }
        // Remove the power-up from the map and list
        root.getChildren().remove(powerUp);
        powerUps.remove(i);
        i--;
      }
    }
  }
}