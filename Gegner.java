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
  Schuss s2 = new Schuss();
  
  private final String FARBE;
  private boolean isAlive;
  private boolean nachgeladen;
  private double schussX;
  private double schussY;
  private boolean started;
  private Line linie;
  private boolean update = false;
  private FpsLimiter fpsLimiter = new FpsLimiter(60);  
  
  public Gegner(String i_Farbe){
    this.FARBE = i_Farbe;
    this.nachgeladen = false;
    started = false;
  }
  
  public boolean getUpdate(){
    return update;
  }
  
  public void setAlive(boolean neu){
    isAlive = neu;
  }
  
  public boolean getAlive(){
    return isAlive;
  }
  
  public boolean gegnerSchießen(ImageView gegner, ImageView gegnerTurret, ImageView player){
    return s2.schiessenGegner(gegner, gegnerTurret, player);
  }
  
  public boolean trefferCheck(ImageView schuss, ImageView player){
    //Überprüfung, ob der Schuss mit dem Spieler kollidiert
    if (schuss.intersects(player.getBoundsInParent())) {
      return true;
    }
    return false;
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
                if (!siehtSpieler(spieler, gegner, wandListe)) {
                  //Fahren (Suchen) Methode
                  //Turret Idle Methode (Maybe direkt in Suchen Methode einbauen)
                }
                
                else {
                  //Fight Methode (Schiessen Methode MAYBE mit hinter Cover fahren oder generell moven)
                }
              }
              
              break;
            case  "indigo":
              if (isAlive) { 
                if (!siehtSpieler(spieler, gegner, wandListe)) {
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
      if (!linie.intersects(wand.getBoundsInParent())) {
        return true;
      }
    }
    return false;
  }
  
  public boolean schießen(ImageView gegner, ImageView gegnerTurret, ImageView spieler){
    //Mathe um den Winkel zu bekommen
    double deltaX = spieler.getX() - gegner.getX();
    double deltaY = spieler.getY() - gegner.getY();
    double winkel = Math.toDegrees(Math.atan2(deltaY, deltaX));
    if(gegnerTurret.getRotate()%360 <= winkel-2 || gegnerTurret.getRotate()%360 >= winkel+2) {
      if(gegnerTurret.getRotate()%360 < winkel) {
        gegnerTurret.setRotate(gegnerTurret.getRotate()+1);
      }
      else if(gegnerTurret.getRotate()%360 > winkel) {
        gegnerTurret.setRotate(gegnerTurret.getRotate()-1);
      }
    }
    
    else {
      PauseTransition nachladen = new PauseTransition(Duration.seconds(0.5));
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
  
  public boolean kollisionsCheck(ImageView schuss, ArrayList<ImageView> wandListe, ArrayList<Rectangle> borderListe){
    //Schuss bewegt sich in die ausgerechnete Richtung
    schuss.setX(schuss.getX() + schussX);
    schuss.setY(schuss.getY() + schussY);
    //Überprüfung, ob der Schuss mit den Wänden kollidiert
    for (ImageView wand : wandListe) {
      if (schuss.intersects(wand.getBoundsInParent())) {
        return true;
      }
    }
    
    for (Rectangle border : borderListe) {
      if (schuss.intersects(border.getBoundsInParent())) {
        return true;
      }
    }
    
    return false;
  }
   
  public void idlen(ImageView gegnerTurret){
    int nullOderEins = 1;
    if (gegnerTurret.getRotate()%50 == 0) {
      Random random = new Random();
      nullOderEins = random.nextInt(2);
    } 
    
    if (nullOderEins != 0) {
      gegnerTurret.setRotate(gegnerTurret.getRotate() +1);
    }
    else {
      gegnerTurret.setRotate(gegnerTurret.getRotate() -1);
    }  
  }



}