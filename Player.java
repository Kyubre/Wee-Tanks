import javafx.scene.input.MouseEvent;
import javafx.scene.image.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.shape.Rectangle;
import javafx.animation.AnimationTimer;

public class Player{
  private double xPos;
  private double yPos;
  private double centerPosX;
  private double centerPosY;
  private boolean wGedrueckt = false;
  private boolean aGedrueckt = false;
  private boolean sGedrueckt = false;
  private boolean dGedrueckt = false;
  private boolean bewegungsTimerErstellt = false;
  private double geschwindigkeitX = 0;
  private double geschwindigkeitY = 0;
  private final double speed = 5;
  double panzerAltX;
  double panzerAltY;
  double turretAltX;
  double turretAltY;
  double altRotation;
  
  
  
  public Player(ImageView panzer){
    this.xPos = panzer.getX();
    this.yPos = panzer.getY();
    this.centerPosX = xPos + (panzer.getFitWidth() / 2);
    this.centerPosY = yPos + (panzer.getFitHeight() / 2);
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
  
  //Schießen (wird ausgeführt wenn Linksklick)
  public boolean schießen(MouseEvent evt, ImageView schuss){
    //Koordinaten vom Mauszeiger
    double eventX = evt.getX();
    double eventY = evt.getY();
    //Rotation in Grad umwandeln in Radiant
    double radiant = Math.toRadians(schuss.getRotate());
    //Sinus und Cosinus benutzen um Radiant in X & Y Vektoren umzurechnen
    double vektorX = Math.cos(radiant);
    double vektorY = Math.sin(radiant);
    //Satz des Pythagoras (A^2 + B^2 = C^2) anwenden um Vektoren X^2 & Y^2 zu Z^2 zu machen
    //Math.sqrt berechnet automatisch die Quadratwurzel aus der Rechnung (C^2 bzw. Z^2 wird zu C bzw. Z)
    double pythagoras = Math.sqrt(vektorX * vektorX + vektorY * vektorY);
    //Geschwindigkeit berechnen mit anpassbaren Werten
    geschwindigkeitX = (vektorX / pythagoras) * 5;     
    geschwindigkeitY = (vektorY / pythagoras) * 5;     
    //false wird in Map1 für den boolean "Kollision" eingesetzt
    return false;
  }
  
  //Kollisionsüberprüfung mit Boundaries
  public boolean kollisionsCheck(ImageView schuss, Rectangle wand1, Rectangle wand2, Rectangle border){
    //Schuss bewegt sich in die ausgerechnete Richtung
    schuss.setX(schuss.getX() + geschwindigkeitX);
    schuss.setY(schuss.getY() + geschwindigkeitY);
    //Überprüfung, ob der Schuss mit den Wänden kollidiert
    if (schuss.intersects(wand1.getBoundsInParent()) || schuss.intersects(wand2.getBoundsInParent()) || !schuss.intersects(border.getBoundsInParent())) {
      return true;
    }
    return false;
  }
  
  public boolean trefferCheck(ImageView schuss, ImageView gegner){
    //Überprüfung, ob der Schuss mit dem Gegner kollidiert
    if (schuss.intersects(gegner.getBoundsInParent())) {
      return true;
    }
    return false;
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
  
  public void bewegungsTimerErstellen(ImageView panzer, ImageView turret, Rectangle wand1, Rectangle wand2, Rectangle border){
    if (bewegungsTimerErstellt) {
      return;
    }
    
    AnimationTimer bewegungsTimer = new AnimationTimer(){
      @Override
      public void handle(long now){
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
        
        if (panzerKollision(panzer, wand1, wand2, border)) {
          panzer.setX(panzerAltX);
          panzer.setY(panzerAltY);
          turret.setX(turretAltX);
          turret.setY(turretAltY);
          panzer.setRotate(altRotation);
        }
      }
    };
    bewegungsTimer.start();
    bewegungsTimerErstellt = true;
  }

  
  public boolean panzerKollision(ImageView panzer, Rectangle wand1, Rectangle wand2, Rectangle border) {
    if (panzer.intersects(wand1.getBoundsInParent()) || panzer.intersects(wand2.getBoundsInParent()) || !panzer.intersects(border.getBoundsInParent())) {
      return true;
    }
    return false;
  }
  
  public void movement(ImageView panzer, ImageView turret, Rectangle wand1, Rectangle wand2, Rectangle border){    
    bewegungsTimerErstellen(panzer, turret, wand1, wand2, border);
  }
}
