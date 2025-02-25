import java.util.ArrayList;
import java.util.Random;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

/* Hexcodes für Gegnerklassen
 * Rot(stationär): #8B0000
 * Grün(fahren): #006400
 * Indigo(spezial): #4B0082
*/

public class Gegner {  
  private final String FARBE;
  private boolean isAlive;
  private boolean nachgeladen;
  private double schussX;
  private double schussY;
  private boolean started;
  private Line linie;
  private boolean update = false;
  private FpsLimiter fpsLimiter = new FpsLimiter(60);
  private int zaehler = 0;
  private int richtung;
  
  public Gegner(String i_Farbe, ImageView gegner, ImageView gegnerTurret){
    this.FARBE = i_Farbe;
    this.nachgeladen = false;
    started = false;
  }
  
  public boolean getUpdate(){
    return update;
  }
  
  
  
  public void setAlive(boolean neu){
    isAlive = neu;
    update = neu;
  }
  
  public boolean getAlive(){
    return isAlive;
  }
  
  public void start(ImageView gegner, ImageView gegnerTurret, ImageView spieler, ArrayList<ImageView> wandListe, ArrayList<Rectangle> borderListe){
    if (started == false) {
      isAlive = true;
      ai(gegner, gegnerTurret, spieler, wandListe, borderListe);
      started = true;
    }
  }
  
  public void ai(ImageView gegner, ImageView gegnerTurret, ImageView spieler, ArrayList<ImageView> wandListe, ArrayList<Rectangle> borderListe){
    //Unterschiedliche loops für unterschiedliche Gegnertypen
    AnimationTimer gegnerAlgo = new AnimationTimer(){
      @Override
      public void handle(long now){
        if (fpsLimiter.canRender(now)) {
          switch (FARBE) {
            case  "rot":
              if(isAlive){
                if (siehtSpieler(spieler, gegner, wandListe)) {
                  siehtSpieler(spieler, gegner, wandListe);
                  schießen(gegner, gegnerTurret, spieler);
                }
                
                else {
                  idlen(gegnerTurret);
                }
              }
              
              
              break;
            case  "grün": 
              if(isAlive){
                if (siehtSpieler(spieler, gegner, wandListe)) {
                  siehtSpieler(spieler, gegner, wandListe);
                  schießen(gegner, gegnerTurret, spieler);
                }
                
                else {
                  idleFahren(gegner, gegnerTurret, wandListe, borderListe);
                  idlen(gegnerTurret);
                }
              }
              
              break;
            case  "indigo":
              if (isAlive) { 
                if (siehtSpieler(spieler, gegner, wandListe)) {
                  //Irgendwas besonderes idk
                }
                
                else {
                  //Fight Methode
                }
              }
              break;
            default: 
              System.out.println("Fehler 187: Ungültige Gegnerfarbe");
          }
        }
      } //
    };  //
    gegnerAlgo.start();
  }
  
  public boolean siehtSpieler(ImageView spieler, ImageView gegner, ArrayList<ImageView> wandListe){
    linie = new Line(gegner.getX(), gegner.getY(), spieler.getX(), spieler.getY());
    for (ImageView wand : wandListe) {
      if (linie.intersects(wand.getBoundsInParent())) {
        return false;
      }
    }
    return true;
  }
  
  public boolean schießen(ImageView gegner, ImageView gegnerTurret, ImageView spieler){
    //Mathe um den Winkel zu bekommen
    double deltaX = spieler.getX() - gegner.getX();
    double deltaY = spieler.getY() - gegner.getY();
    double zielWinkel = Math.toDegrees(Math.atan2(deltaY, deltaX));
    
    // Aktuelle Rotation des Turrets
    double aktuelleRotation = gegnerTurret.getRotate() % 360;
    if (aktuelleRotation < 0) aktuelleRotation += 360; // Sicherstellen, dass der Wert positiv ist
    
    // Berechnung der kürzesten Drehung
    double differenz = zielWinkel - aktuelleRotation;
    if (differenz > 180) {
      differenz -= 360;
    } else if (differenz < -180) {
      differenz += 360;
    }
    
    // Drehe in die kürzere Richtung
    if (Math.abs(differenz) > 2) {
      gegnerTurret.setRotate(gegnerTurret.getRotate() + Math.signum(differenz)*2);
    }
    
    
    else {
      PauseTransition nachladen = new PauseTransition(Duration.seconds(2.5));
      if(nachgeladen) {
        update = true;
      }
      
      else {
        update = false;
        nachladen.play();
      }
      nachladen.setOnFinished(nachladEvent -> {;
        nachgeladen = true;
      });
    }
    return false;
  }
     
  public void idlen(ImageView gegnerTurret){
    update = false;
    gegnerTurret.setRotate(gegnerTurret.getRotate() +1); 
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
    
    if (zaehler > 40 * 2 || kollision(gegner, walls, borders)) {
      Random random = new Random();
      richtung = random.nextInt(4);
      zaehler = 0;
    }
    
    switch (richtung) {
      case 0: 
        gegner.setX(gegner.getX() + 1);
        gegnerTurret.setX(gegnerTurret.getX() + 1);
        //Rotation 0
        gegner.setRotate(0);
        break;
      case 1: 
        gegner.setY(gegner.getY() + 1);
        gegnerTurret.setY(gegnerTurret.getY() + 1);
        //Rotation 90
        gegner.setRotate(90);
        break;
      case 2: 
        gegner.setX(gegner.getX() - 1);
        gegnerTurret.setX(gegnerTurret.getX() - 1);
        //Rotation 180
        gegner.setRotate(180);
        break;
      case 3: 
        gegner.setY(gegner.getY() - 1);
        gegnerTurret.setY(gegnerTurret.getY() - 1);
        //Rotation 270
        gegner.setRotate(270);
        break;
      default: 
        System.out.println("Fehler 42: Ungültige Richtung");  
    }
    zaehler++;
  }
} 