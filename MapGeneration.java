import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.image.*;
import javafx.stage.Screen;

public class MapGeneration {
  private Rectangle borderwall1 = new Rectangle();
  private Rectangle borderwall2 = new Rectangle();
  private Rectangle borderwall3 = new Rectangle();
  private Rectangle borderwall4 = new Rectangle();
  private ImageView hintergrund = new ImageView();
  private Image gegnerImage;
  private Image gegnerTurretImage;
  private Image gegner_grau = new Image(getClass().getResourceAsStream("src/assets/images/panzer_grau.png"));
  private Image gegner_lila = new Image(getClass().getResourceAsStream("src/assets/images/panzer_lila.png"));
  private Image gegner_rot = new Image(getClass().getResourceAsStream("src/assets/images/panzer_rot.png"));
  private Image turret_grau = new Image(getClass().getResourceAsStream("src/assets/images/turret_grau.png"));
  private Image turret_lila = new Image(getClass().getResourceAsStream("src/assets/images/turret_lila.png"));
  private Image turret_rot = new Image(getClass().getResourceAsStream("src/assets/images/turret_rot.png"));
  private Random random = new Random();
  private ArrayList<ImageView> alleWaende;
  private ArrayList<Rectangle> borderListe;
  private final int maxAttempts = 100;
  private double bildschirmBreite = Screen.getPrimary().getBounds().getWidth();
  private double bildschirmHoehe = Screen.getPrimary().getBounds().getHeight();
  private final double referenceWidth = 1920.0;
  private final double referenceHeight = 1080.0;
  private final int windowWidth = (int) (bildschirmBreite * (1280.0 / referenceWidth));
  private final int windowHeight = (int) (bildschirmHoehe * (720.0 / referenceHeight));
  private final int abstand = (int) (250 * (bildschirmBreite / referenceWidth));
  private double multi = bildschirmBreite / referenceWidth;
  private final int maxAnzahlBloecke = 12;
  private final int minAnzahlBloecke = 8;
  private Image vWand = new Image(getClass().getResourceAsStream("src/assets/images/wall_v.png"));
  private Image hWand = new Image(getClass().getResourceAsStream("src/assets/images/wall_h.png"));
  private Image panzer = new Image(getClass().getResourceAsStream("src/assets/images/panzer.png"));
  private Image powerUp_speed = new Image(getClass().getResourceAsStream("src/assets/images/speed_powerup.png"));
  private Image powerUp_reload = new Image(getClass().getResourceAsStream("src/assets/images/reload_powerup.png"));
  private ImageView speedPowerUp;
  private ImageView reloadPowerUp;
  private ImageView spieler;
  private ImageView turret = new ImageView();
  private ImageView gegnerTurret = new ImageView();
  private Image turretImage = new Image(getClass().getResourceAsStream("src/assets/images/turret.png"));
  private ImageView gegner;
  private Pane pane1;
  private String farbe;
  ArrayList<ImageView> powerUps;
  private ArrayList<Gegner> gegnerListe = new ArrayList<>();

  Pane root = new Pane();

  public MapGeneration() {
    alleWaende = new ArrayList<>();
    borderListe = new ArrayList<>();
    powerUps = new ArrayList<>();
  }

  public ArrayList<ImageView> getPowerUps() {
    return powerUps;
  }

  public ArrayList<ImageView> getWandListe() {
    return alleWaende;
  }

  public ArrayList<Rectangle> getBorderListe() {
    return borderListe;
  }

  public Pane getMap() {
    return pane1;
  }

  public ImageView getPanzer() {
    return spieler;
  }

  public ImageView getGegner() {
    return gegner;
  }

  public Image getColor() {
    return gegnerImage;
  }

  public Image getColorTurret() {
    return gegnerTurretImage;
  }

  public String getFarbe() {
    return farbe;
  }

  public ImageView getTurret() {
    return turret;
  }

  public ImageView getGegnerTurret() {
    return gegnerTurret;
  }

  public ArrayList<Gegner> getGegnerListe() {
    return gegnerListe;
  }

  public void initialize(Stage stage) {
    VBox root = new VBox();

    // button um das spiel zu schließen
    Button exitButton = new Button("Exit");
    exitButton.setOnAction(e -> {
      stage.close();
      System.exit(0);
    });

    // regenerate Button
    Button regenerateButton = new Button("Regenerate Map");
    regenerateButton.setOnAction(e -> {
      root.getChildren().clear();
      alleWaende.clear();
      generateMap(root);
    });

    Scene scene = new Scene(root, windowWidth, windowHeight);
    generateMap(root);

    stage.setFullScreen(true);
    stage.setTitle("Map Generation Test");
    stage.setScene(scene);
    stage.show();
    pane1 = root;
  }

  public void placeHintergrund(Pane root) {
    hintergrund.setX(0);
    hintergrund.setY(0);
    hintergrund.setFitHeight(bildschirmHoehe);
    hintergrund.setFitWidth(bildschirmBreite);
    hintergrund.setImage(new Image(getClass().getResourceAsStream("src/assets/images/hintergrund.png")));
    root.getChildren().add(hintergrund);
  }

  private void placeBorder(Pane root) {
    borderwall1.setWidth(bildschirmBreite);
    borderwall1.setHeight(1);
    borderwall1.setX(1);
    borderwall1.setY(1);
    borderwall1.setSmooth(false);
    root.getChildren().add(borderwall1);
    borderListe.add(borderwall1);

    borderwall2.setWidth(20);
    borderwall2.setHeight(bildschirmHoehe);
    borderwall2.setX(bildschirmBreite - 1);
    borderwall2.setY(1);
    borderwall2.setSmooth(false);
    root.getChildren().add(borderwall2);
    borderListe.add(borderwall2);

    borderwall3.setWidth(bildschirmBreite);
    borderwall3.setHeight(20);
    borderwall3.setX(0);
    borderwall3.setY(bildschirmHoehe - 1);
    borderwall3.setSmooth(false);
    root.getChildren().add(borderwall3);
    borderListe.add(borderwall3);

    borderwall4.setWidth(1);
    borderwall4.setHeight(bildschirmHoehe);
    borderwall4.setX(1);
    borderwall4.setY(1);
    borderwall4.setSmooth(false);
    root.getChildren().add(borderwall4);
    borderListe.add(borderwall4);

  }

  private void placeWalls(Pane root) {
    int attempts = 0;
    while (attempts < maxAttempts) {
      boolean overlap = true;
      ImageView wall = new ImageView();

      // generiert zufällig horizontale oder vertikale rectangles
      int randomInt = random.nextInt(2);
      if (randomInt == 1) { // Bei 1 wird ein Horizontales rectangle erstellt
        wall.setFitWidth(200 * multi);
        wall.setFitHeight(80 * multi);
        wall.setImage(hWand);
      } else { // sonst wird ein vertikales erstellt
        wall.setFitWidth(80 * multi);
        wall.setFitHeight(200 * multi);
        wall.setImage(vWand);
      }

      int availableWidth = (int) bildschirmBreite - (int) wall.getFitWidth();
      int availableHeight = (int) bildschirmHoehe - (int) wall.getFitHeight();

      // setzt die X / Y Koordinaten der rectangles inerhalb der spielfläche
      wall.setX(random.nextInt(availableWidth));
      wall.setY(random.nextInt(availableHeight));

      // erstellt ein 2. unsichbares rectangle mit der größe des 1. rectangles + einen
      // festen abstand (250px)
      // damit wird sicher gegangen, dass rectangles nicht ineinander generiert werden
      // könnten
      Rectangle rect2 = new Rectangle();
      rect2.setWidth(wall.getFitWidth() + abstand * 2);
      rect2.setHeight(wall.getFitHeight() + abstand * 2);
      rect2.setX(wall.getX() - abstand);
      rect2.setY(wall.getY() - abstand);

      // wenn rect2 mit rect1 overlapped, wird die wand nicht generiert
      overlap = false;
      for (ImageView existingWall : alleWaende) {
        if (rect2.intersects(existingWall.getBoundsInParent())) {
          overlap = true;
          break;
        }
      }

      // falls kein overlap besteht, wird das rectangle erstellt
      if (!overlap) {
        alleWaende.add(wall);
        root.getChildren().add(wall);
        return;
      }

      // maximale versuche rectangles zu generieren, wenn die nummer erreicht wird,
      // werden keine mehr generiert
      attempts++;
    }
  }

  private void placePlayer(Pane root) {
    int attempts = 0;
    boolean placed = false;

    while (attempts < maxAttempts && !placed) {
      boolean overlap = true;
      ImageView tank = new ImageView();

      tank.setFitWidth(100 * multi);
      tank.setFitHeight(75 * multi);

      int availableWidth = (int) (windowWidth - tank.getFitWidth());
      int availableHeight = (int) (windowHeight - tank.getFitHeight());

      // Setzt zufällige X / Y Koordinaten
      tank.setX(random.nextInt(availableWidth));
      tank.setY(random.nextInt(availableHeight));

      overlap = false;
      for (ImageView existingWall : alleWaende) {
        if (tank.intersects(existingWall.getBoundsInParent())) {
          overlap = true;
          break;
        }
      }

      Rectangle links = new Rectangle();
      links.setX(0);
      links.setY(0);
      links.setHeight(bildschirmHoehe);
      links.setWidth(bildschirmBreite / 4);

      if (!tank.intersects(links.getBoundsInParent())) {
        overlap = true;
      }

      if (!overlap) {
        root.getChildren().add(tank);
        tank.setImage(panzer);
        spieler = tank;
        placed = true;
        // Turret erstellen
        turret.setX(tank.getX() - 8);
        turret.setY(tank.getY() + 12);
        turret.setFitWidth(114 * multi);
        turret.setFitHeight(50 * multi);
        turret.setImage(turretImage);
        root.getChildren().add(turret);
      }

      attempts++;
    }
  }

  private void gegnerFarbe() {
    int level = Map.getLevel();
    if (level <= 5) {
      if (level != 5) {
        gegnerImage = gegner_grau;
        gegnerTurretImage = turret_grau;
        farbe = "grau";
      } else {
        gegnerImage = gegner_rot;
        gegnerTurretImage = turret_rot;
        farbe = "rot";
      }
    } else if (level <= 20) {
      if ((level % 5) == 0) {
        gegnerImage = gegner_lila;
        gegnerTurretImage = turret_lila;
        farbe = "lila";
      } else {
        gegnerImage = gegner_rot;
        gegnerTurretImage = turret_rot;
        farbe = "rot";
      }
    } else {
      gegnerImage = gegner_lila;
      gegnerTurretImage = turret_lila;
      farbe = "lila";
    }
  }

  private void placeGegner(Pane root) {
    int attempts = 0;
    boolean placed = false;
    gegner = new ImageView(); // Stelle sicher, dass gegner initialisiert wird
    gegnerTurret = new ImageView(); // Initialisiere das Turret des Gegners

    while (attempts < maxAttempts && !placed) {
        boolean overlap = false;

        gegner.setFitWidth(100 * multi);
        gegner.setFitHeight(75 * multi);

        int availableHeight = (int) (windowHeight - gegner.getFitHeight());
        gegner.setX((bildschirmBreite / 4) * 3 + Math.random() * (bildschirmBreite / 4) - gegner.getFitWidth());
        gegner.setY(random.nextInt(availableHeight));
        gegner.setRotate(180);

        for (ImageView existingWall : alleWaende) {
            if (gegner.intersects(existingWall.getBoundsInParent())) {
                overlap = true;
                break;
            }
        }
        // geht sicher, dass gegner nicht ineinander spawnen können
        for (Gegner existingGegner : gegnerListe) {
            if (gegner.intersects(existingGegner.getImage().getBoundsInParent())) {
                overlap = true;
                break;
            }
        }

        if (!overlap) {
            root.getChildren().add(gegner);
            gegnerFarbe();
            gegner.setImage(gegnerImage);
            Gegner g = new Gegner(farbe, gegner, gegnerTurret);
            gegnerListe.add(g);

            // Setze das Turret des Gegners
            gegnerTurret.setFitWidth(114 * multi);
            gegnerTurret.setFitHeight(50 * multi);
            gegnerTurret.setX(gegner.getX() + (gegner.getFitWidth() / 2) - (gegnerTurret.getFitWidth() / 2));
            gegnerTurret.setY(gegner.getY() + (gegner.getFitHeight() / 2) - (gegnerTurret.getFitHeight() / 2));
            gegnerTurret.setImage(gegnerTurretImage);
            root.getChildren().add(gegnerTurret);

            placed = true;
        }
        attempts++;
    }

    // Falls kein gültiger Platz gefunden wurde, setze einen Backup-Spawn
    if (!placed) {
        gegner.setX(bildschirmBreite - 150);
        gegner.setY(bildschirmHoehe / 2);
        root.getChildren().add(gegner);
        gegner.setImage(panzer);
        Gegner g = new Gegner(farbe, gegner, gegnerTurret);
        gegnerListe.add(g);

        // Setze das Turret des Gegners
        gegnerTurret.setFitWidth(114 * multi);
        gegnerTurret.setFitHeight(50 * multi);
        gegnerTurret.setX(gegner.getX() + (gegner.getFitWidth() / 2) - (gegnerTurret.getFitWidth() / 2));
        gegnerTurret.setY(gegner.getY() + (gegner.getFitHeight() / 2) - (gegnerTurret.getFitHeight() / 2));
        gegnerTurret.setImage(gegnerTurretImage);
        root.getChildren().add(gegnerTurret);
    }
  }

  public Pane generateMap(Pane root) {
    placeHintergrund(root);
    int numberOfWalls = random.nextInt(maxAnzahlBloecke) + minAnzahlBloecke;
    for (int i = 0; i < numberOfWalls; i++) {
      placeWalls(root);
    }
    placePlayer(root);
    for (int i = 0; i < 3; i++){
      placeGegner(root);
    }
    placeBorder(root);
    placePowerUps(root);
    return root;
  }

  private void placePowerUps(Pane root) {
    speedPowerUp = new ImageView(powerUp_speed);
    speedPowerUp.setFitWidth(50);
    speedPowerUp.setFitHeight(50);
    speedPowerUp.setX(random.nextInt((int) (bildschirmBreite - speedPowerUp.getFitWidth())));
    speedPowerUp.setY(random.nextInt((int) (bildschirmHoehe - speedPowerUp.getFitHeight())));
    speedPowerUp.setId("speed");
    for (ImageView existingWall : alleWaende) {
      if (speedPowerUp.intersects(existingWall.getBoundsInParent())) {
        speedPowerUp.setX(random.nextInt((int) (bildschirmBreite - speedPowerUp.getFitWidth())));
        speedPowerUp.setY(random.nextInt((int) (bildschirmHoehe - speedPowerUp.getFitHeight())));
      }
    }
    root.getChildren().add(speedPowerUp);
    powerUps.add(speedPowerUp);

    reloadPowerUp = new ImageView(powerUp_reload);
    reloadPowerUp.setX(random.nextInt((int) (bildschirmBreite - 50)));
    reloadPowerUp.setY(random.nextInt((int) (bildschirmHoehe - 50)));
    reloadPowerUp.setId("reload");
    for (ImageView existingWall : alleWaende) {
      if (reloadPowerUp.intersects(existingWall.getBoundsInParent())) {
        reloadPowerUp.setX(random.nextInt((int) (bildschirmBreite - 50)));
        reloadPowerUp.setY(random.nextInt((int) (bildschirmHoehe - 50)));
      }
    }
    root.getChildren().add(reloadPowerUp);
    powerUps.add(reloadPowerUp);
  }

}