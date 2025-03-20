import java.util.ArrayList;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.animation.AnimationTimer;
import javafx.stage.Screen;

public class Player{
  private double multi = (Screen.getPrimary().getBounds().getWidth() / 1920);
  private double xPos;
  private double yPos;
  private double centerPosX;
  private double centerPosY;
  private boolean wGedrueckt = false;
  private boolean aGedrueckt = false;
  private boolean sGedrueckt = false;
  private boolean dGedrueckt = false;
  private boolean bewegungsTimerErstellt = false;
  private final double speed = 5 * multi;
  private double panzerAltX;
  private double panzerAltY;
  private double turretAltX;
  private double turretAltY;
  private double altRotation;
  private boolean isAlive;

  private FpsLimiter fpsLimiter = new FpsLimiter(60);
  
  public Player(ImageView panzer, ImageView panzerTurret){
    isAlive = true;
  } 
  
  public void setAlive(boolean neu){
    isAlive = neu;
  }
  
  public boolean getAlive(){
    return isAlive;
  }
       
  
  public double turretRotation(MouseEvent evnt, ImageView panzer){
    double eventX = evnt.getX();
    double eventY = evnt.getY();
    xPos = panzer.getX();
    yPos = panzer.getY();
    double playerWidth = panzer.getFitWidth();
    double playerHeight = panzer.getFitHeight();
    
    centerPosX = xPos + (playerWidth / 2);
    centerPosY = yPos + (playerHeight / 2);
    
    double angle = Math.toDegrees(Math.atan2(eventY - centerPosY, eventX - centerPosX));
    return angle;
  }
  
  
  public void tasteGedrueckt(KeyEvent evt){
    switch (evt.getCode()) {
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
      case ESCAPE:
        Map m = new Map();
        //Ist noch broken
        m.quit();
        break;
      default: 
        break;  
    }
  }
  
  public void tasteLosgelassen(KeyEvent evt){
    switch (evt.getCode()) {
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
  }
  
  //AnimationTimer erstellen für movement jeden Frame
  public void bewegungsTimerErstellen(ImageView panzer, ImageView turret, ArrayList<ImageView> wandListe, ArrayList<Rectangle> borderListe){
    if (!bewegungsTimerErstellt) {
      AnimationTimer bewegungsTimer = new AnimationTimer(){
        @Override
        public void handle(long now){
          if (fpsLimiter.canRender(now)) {
            movement(panzer, turret, wandListe, borderListe);
          }
        }
      };
      bewegungsTimer.start();
      bewegungsTimerErstellt = true;
    }
  }

  
  public boolean panzerKollision(ImageView panzer, ArrayList<ImageView> wandListe, ArrayList<Rectangle> borderListe) {
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
      
  public void movement(ImageView panzer, ImageView turret, ArrayList<ImageView> wandListe, ArrayList<Rectangle> borderListe){    
    bewegungsTimerErstellen(panzer, turret, wandListe, borderListe);
    
    panzerAltX = panzer.getX();
    panzerAltY = panzer.getY();
    turretAltX = turret.getX();
    turretAltY = turret.getY();
    altRotation = panzer.getRotate();
    
    if (wGedrueckt && aGedrueckt) {
      panzer.setY(panzer.getY()-speed/2);
      panzer.setX(panzer.getX()-speed/2);
      panzer.setRotate(225);         
      turret.setY(turret.getY()-speed/2);
      turret.setX(turret.getX()-speed/2);
    } 
    else if (wGedrueckt && dGedrueckt) {
      panzer.setY(panzer.getY()-speed/2);
      panzer.setX(panzer.getX()+speed/2);
      panzer.setRotate(315);
      turret.setY(turret.getY()-speed/2);
      turret.setX(turret.getX()+speed/2);
    } 
    else if (sGedrueckt && aGedrueckt) {
      panzer.setY(panzer.getY()+speed/2);
      panzer.setX(panzer.getX()-speed/2);
      panzer.setRotate(135);
      turret.setY(turret.getY()+speed/2);
      turret.setX(turret.getX()-speed/2);
    } 
    else if (sGedrueckt && dGedrueckt) {
      panzer.setY(panzer.getY()+speed/2);
      panzer.setX(panzer.getX()+speed/2);
      panzer.setRotate(45);
      turret.setY(turret.getY()+speed/2);
      turret.setX(turret.getX()+speed/2);
    } 
    else if (wGedrueckt) {
      panzer.setY(panzer.getY()-speed);
      panzer.setRotate(270);
      turret.setY(turret.getY()-speed);
    } 
    else if (aGedrueckt) {
      panzer.setX(panzer.getX()-speed);
      panzer.setRotate(180);
      turret.setX(turret.getX()-speed);
    } 
    else if (sGedrueckt) {
      panzer.setY(panzer.getY()+speed);
      panzer.setRotate(90);
      turret.setY(turret.getY()+speed);
    } 
    else if (dGedrueckt) {
      panzer.setX(panzer.getX()+speed);
      panzer.setRotate(0);
      turret.setX(turret.getX()+speed);
    }
    
    if (panzerKollision(panzer, wandListe, borderListe)) {
      panzer.setX(panzerAltX);
      panzer.setY(panzerAltY);
      turret.setX(turretAltX);
      turret.setY(turretAltY);
      panzer.setRotate(altRotation);
    }
  }
}
