import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Random;

public class Gegner {
    private final String FARBE;
    private boolean isAlive;
    private boolean nachgeladen;
    private boolean started;
    private Line linie;
    private boolean update = false;
    private FpsLimiter fpsLimiter = new FpsLimiter(60);
    private int zaehler = 0;
    private int richtung;
    private double speed;
    private double difficulty = Map.getLevel() / 10 + 1;

    public Gegner(String i_Farbe, ImageView gegner, ImageView gegnerTurret) {
        this.FARBE = i_Farbe;
        this.nachgeladen = true;
        started = false;
        speed = 2 * difficulty;
    }

    public boolean getUpdate() {
        return update;
    }

    public String getColor() {
        return FARBE;
    }

    public void setSpeed(int neu) {
        speed = neu;
    }

    public void setAlive(boolean neu) {
        isAlive = neu;
        update = neu;
    }

    public boolean getAlive() {
        return isAlive;
    }

    public void start(ImageView gegner, ImageView gegnerTurret, ImageView spieler, ArrayList<ImageView> wandListe, ArrayList<Rectangle> borderListe) {
        if (!started) {
            isAlive = true;
            ai(gegner, gegnerTurret, spieler, wandListe, borderListe);
            started = true;
        }
    }

    public void ai(ImageView gegner, ImageView gegnerTurret, ImageView spieler, ArrayList<ImageView> wandListe, ArrayList<Rectangle> borderListe) {
        AnimationTimer gegnerAlgo = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (fpsLimiter.canRender(now)) {
                    switch (FARBE) {
                        case "grau":
                            if (isAlive) {
                                if (siehtSpieler(spieler, gegner, wandListe)) {
                                    schießen(gegner, gegnerTurret, spieler);
                                } else {
                                    idlen(gegnerTurret);
                                }
                            }
                            break;
                        case "rot":
                            if (isAlive) {
                                if (siehtSpieler(spieler, gegner, wandListe)) {
                                    schießen(gegner, gegnerTurret, spieler);
                                    idleFahren(gegner, gegnerTurret, wandListe, borderListe);
                                } else {
                                    idleFahren(gegner, gegnerTurret, wandListe, borderListe);
                                    idlen(gegnerTurret);
                                }
                            }
                            break;
                        case "lila":
                            if (isAlive) {
                                if (siehtSpieler(spieler, gegner, wandListe)) {
                                    schießen(gegner, gegnerTurret, spieler);
                                    idleFahren(gegner, gegnerTurret, wandListe, borderListe);
                                } else {
                                    idleFahren(gegner, gegnerTurret, wandListe, borderListe);
                                    idlen(gegnerTurret);
                                }
                            }
                            break;
                        default:
                            System.out.println("Fehler 18: Ungültige Gegnerfarbe");
                    }
                }
            }
        };
        gegnerAlgo.start();
    }

    public boolean siehtSpieler(ImageView spieler, ImageView gegner, ArrayList<ImageView> wandListe) {
        linie = new Line(gegner.getX() + (gegner.getFitWidth()/2), gegner.getY() + (gegner.getFitHeight()/2), spieler.getX() + (spieler.getFitWidth()/2), spieler.getY() + (spieler.getFitHeight()/2));
        for (ImageView wand : wandListe) {
            Bounds wandBounds = wand.getBoundsInParent();
            Rectangle wandShape = new Rectangle(wandBounds.getMinX(), wandBounds.getMinY(), wandBounds.getWidth(), wandBounds.getHeight());
            if (linie.intersects(wandBounds)) {
                Shape intersection = Shape.intersect(linie, wandShape);
                if (intersection.getBoundsInLocal().getWidth() != -1) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean schießen(ImageView gegner, ImageView gegnerTurret, ImageView spieler) {
        double deltaX = spieler.getX() - gegner.getX();
        double deltaY = spieler.getY() - gegner.getY();
        double zielWinkel = Math.toDegrees(Math.atan2(deltaY, deltaX));

        double aktuelleRotation = gegnerTurret.getRotate() % 360;
        if (aktuelleRotation < 0) aktuelleRotation += 360;

        double differenz = zielWinkel - aktuelleRotation;
        if (differenz > 180) {
            differenz -= 360;
        } else if (differenz < -180) {
            differenz += 360;
        }

        if (Math.abs(differenz) > 2) {
            gegnerTurret.setRotate(gegnerTurret.getRotate() + Math.signum(differenz) * 2);
        } else {
            PauseTransition nachladen = new PauseTransition(Duration.seconds(2.5));
            if (nachgeladen) {
                update = true;
            } else {
                update = false;
                nachladen.play();
            }
            nachladen.setOnFinished(nachladEvent -> nachgeladen = true);
        }
        return false;
    }

    public void idlen(ImageView gegnerTurret) {
        update = false;
        gegnerTurret.setRotate(gegnerTurret.getRotate() + 1);
    }

    public boolean kollision(ImageView panzer, ArrayList<ImageView> wandListe, ArrayList<Rectangle> borderListe) {
        for (ImageView wand : wandListe) {
            if (panzer.intersects(wand.getBoundsInParent())) {
                return true;
            }
        }

        for (Rectangle border : borderListe) {
            if (panzer.intersects(border.getBoundsInParent())) {
                return true;
            }
        }

        return false;
    }

    public void idleFahren(ImageView gegner, ImageView gegnerTurret, ArrayList<ImageView> walls, ArrayList<Rectangle> borders) {
        if (zaehler > 180) {
            Random random = new Random();
            richtung = random.nextInt(4);
            zaehler = 0;
        }

        switch (richtung) {
            case 0:
                gegner.setX(gegner.getX() + speed);
                gegnerTurret.setX(gegnerTurret.getX() + speed);
                gegner.setRotate(0);
                break;
            case 1:
                gegner.setY(gegner.getY() + speed);
                gegnerTurret.setY(gegnerTurret.getY() + speed);
                gegner.setRotate(90);
                break;
            case 2:
                gegner.setX(gegner.getX() - speed);
                gegnerTurret.setX(gegnerTurret.getX() - speed);
                gegner.setRotate(180);
                break;
            case 3:
                gegner.setY(gegner.getY() - speed);
                gegnerTurret.setY(gegnerTurret.getY() - speed);
                gegner.setRotate(270);
                break;
            default:
                System.out.println("Fehler 42: Ungültige Richtung");
        }
        zaehler++;
        if (kollision(gegner, walls, borders)) {
            switch (richtung) {
                case 0:
                    richtung = 2;
                    zaehler = 150;
                    break;
                case 1:
                    richtung = 3;
                    zaehler = 150;
                    break;
                case 2:
                    richtung = 0;
                    zaehler = 150;
                    break;
                case 3:
                    richtung = 1;
                    zaehler = 150;
                    break;
                default:
                    System.out.println("Fehler 42: Ungültige Richtung");
            }
        }
    }

    // New method to detect incoming shots
    public boolean detectIncomingShot(ImageView shot, ImageView gegner) {
        Line shotPath = new Line(shot.getX(), shot.getY(), shot.getX() + shot.getFitWidth(), shot.getY() + shot.getFitHeight());
        Bounds gegnerBounds = gegner.getBoundsInParent();
        Rectangle gegnerShape = new Rectangle(gegnerBounds.getMinX(), gegnerBounds.getMinY(), gegnerBounds.getWidth(), gegnerBounds.getHeight());
        Shape intersection = Shape.intersect(shotPath, gegnerShape);
        return intersection.getBoundsInLocal().getWidth() != -1;
    }

    // New method to evade incoming shots
    public void evadeShot(ImageView gegner, ArrayList<ImageView> walls, ArrayList<Rectangle> borders) {
        Random random = new Random();
        int evadeDirection = random.nextInt(4);
        switch (evadeDirection) {
            case 0:
                gegner.setX(gegner.getX() + speed);
                break;
            case 1:
                gegner.setY(gegner.getY() + speed);
                break;
            case 2:
                gegner.setX(gegner.getX() - speed);
                break;
            case 3:
                gegner.setY(gegner.getY() - speed);
                break;
        }
        if (kollision(gegner, walls, borders)) {
            switch (evadeDirection) {
                case 0:
                    gegner.setX(gegner.getX() - speed);
                    break;
                case 1:
                    gegner.setY(gegner.getY() - speed);
                    break;
                case 2:
                    gegner.setX(gegner.getX() + speed);
                    break;
                case 3:
                    gegner.setY(gegner.getY() + speed);
                    break;
            }
        }
    }
}