import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.image.ImageView;
import javafx.scene.image.*;
import javafx.stage.Screen;


public class MapGeneration {
  private Rectangle borderwall1 = new Rectangle();
  private Rectangle borderwall2 = new Rectangle();
  private Rectangle borderwall3 = new Rectangle();
  private Rectangle borderwall4 = new Rectangle();
  private ImageView hintergrund = new ImageView();
  private Image gegnerImage;
  private Image gegnerTurret;
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
  private ImageView gegner;
  private Pane pane1;
  private String farbe;
  ArrayList<ImageView> powerUps;
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
    return gegnerTurret;
  }

  public String getFarbe() {
    return farbe;
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
      }

      attempts++;
    }
  }

  private void gegnerFarbe() {
    int level = Map.getLevel();
    if (level <= 5) {
      if (level != 5) {
        gegnerImage = gegner_grau;
        gegnerTurret = turret_grau;
        farbe = "grau";
      } else {
        gegnerImage = gegner_rot;
        gegnerTurret = turret_rot;
        farbe = "rot";
      }
    } else if (level <= 20) {
      if ((level % 5) == 0) {
        gegnerImage = gegner_lila;
        gegnerTurret = turret_lila;
        farbe = "lila";
      } else {
        gegnerImage = gegner_rot;
        gegnerTurret = turret_rot;
        farbe = "rot";
      }
    } else {
      gegnerImage = gegner_lila;
      gegnerTurret = turret_lila;
      farbe = "lila";
    }
  }

  private void placeGegner(Pane root) {
    int attempts = 0;
    boolean placed = false;
    ImageView tank = new ImageView();

    while (attempts < maxAttempts && !placed) {
      boolean overlap = true;

      tank.setFitWidth(100 * multi);
      tank.setFitHeight(75 * multi);

      int availableHeight = (int) (windowHeight - tank.getFitHeight());

      // setX generiert im rechten Viertel ein random Wert von 0 bis 1 * Größe von
      // einem Viertel für einen random Wert im rechten Viertel
      tank.setX((bildschirmBreite / 4) * 3 + Math.random() * (bildschirmBreite / 4) - tank.getFitWidth());
      tank.setY(random.nextInt(availableHeight));
      tank.setRotate(180);

      overlap = false;
      for (ImageView existingWall : alleWaende) {
        if (tank.intersects(existingWall.getBoundsInParent())) {
          overlap = true;
          break;
        }
      }

      Gegner gTest = new Gegner("rot", tank, null);
      if (gTest.siehtSpieler(spieler, tank, alleWaende)) {
        overlap = true;
      }

      if (!overlap) {
        root.getChildren().add(tank);
        tank.setImage(panzer);
        gegner = tank;
        placed = true;
      }

      attempts++;
    }

    // Backup Spawn falls das Spawning nicht geht
    if (!placed) {
      tank.setX(bildschirmBreite - 150);
      tank.setY(bildschirmHoehe / 2);
      root.getChildren().add(tank);
      gegner = tank;
      gegner.setImage(panzer);
    }
  }

  public Pane generateMap(Pane root) {
    placeHintergrund(root);
    int numberOfWalls = random.nextInt(maxAnzahlBloecke) + minAnzahlBloecke;
    for (int i = 0; i < numberOfWalls; i++) {
      placeWalls(root);
    }
    placePlayer(root);
    placeGegner(root);
    placeBorder(root);
    placePowerUps(root);
    gegnerFarbe();
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