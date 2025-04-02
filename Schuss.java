import java.util.ArrayList;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

public class Schuss{
  private double multi = (Screen.getPrimary().getBounds().getWidth() / 1920);
  private double difficulty = Map.getLevel() /10 +1;
  private double speed = 5 * multi * difficulty;
  private double geschwindigkeitX;
  private double geschwindigkeitY;
  private boolean istSpieler;
  private int bounces;
  private final int BASESPEED = 8;
  private boolean lila;
  private int lebenszeit = 0; // in Millisekunden
   
  public Schuss(ImageView schuetzeTurret, boolean spieler, boolean lila){
    double radiant = Math.toRadians(schuetzeTurret.getRotate());
    double vektorX = Math.cos(radiant);
    double vektorY = Math.sin(radiant);
    double pythagoras = Math.sqrt(vektorX * vektorX + vektorY * vektorY);
    this.istSpieler = spieler;
    this.lila = lila;
    if(spieler){
      speed = BASESPEED * multi;
    } else {
      speed = BASESPEED * multi * difficulty;  
    } // end of if-else
    this.geschwindigkeitX = (vektorX / pythagoras) * speed;     
    this.geschwindigkeitY = (vektorY / pythagoras) * speed;
    this.bounces = 0;
  }
  
  public boolean getSpieler(){
    return istSpieler;
  }

  public int getLebenszeit() {
    return lebenszeit; 
  }

  public void erhoeheLebenszeit(int zeit) {
    lebenszeit += zeit;
  }
  
  public boolean kollisionsCheck(ImageView schussBild, ArrayList<ImageView> wandListe, ArrayList<Rectangle> borderListe){
    for (ImageView wand : wandListe) {
      if (schussBild.intersects(wand.getBoundsInParent()) && bounces < 3) {
        if(wand.getBoundsInParent().getWidth() > wand.getBoundsInParent().getHeight()) {
          geschwindigkeitX = -geschwindigkeitX; // Invertiere X-Geschwindigkeit
          bounces++;
          return true;
        } 
        if(wand.getBoundsInParent().getWidth() < wand.getBoundsInParent().getHeight()) {
          geschwindigkeitY = -geschwindigkeitY; // Invertiere Y-Geschwindigkeit
          bounces++;
          return true;
        }
        return false;
      }
    }
    
    for (Rectangle border : borderListe) {
      if (schussBild.intersects(border.getBoundsInParent()) && bounces < 3) {
        if (border.getWidth() > border.getHeight()) {
          geschwindigkeitX = -geschwindigkeitX; 
          bounces++;
          return true;
        } 
        if(border.getWidth() < border.getHeight()) {
          geschwindigkeitY = -geschwindigkeitY; 
          bounces++;
          return true;
        }
        
      }
    }
    
    return false;
  }
  public int getBounces() {
    return bounces;
  }

  public boolean trefferCheck(ImageView schussBild, ImageView ziel){ 
    if (schussBild.intersects(ziel.getBoundsInParent())) {
      return true;
    }
    return false;
  }

  public Gegner playerTrefferCheck(ImageView schussBild, ArrayList<Gegner> gegnerListe) {
    for (Gegner gegner : gegnerListe) {
      if (schussBild.intersects(gegner.getImage().getBoundsInParent())) {
        return gegner;
      }
    } 
    return null;
  }
  
  public void fliegen(ImageView schussBild, ImageView player, ArrayList<ImageView> wandListe, ArrayList<Rectangle> borderListe){
    if (lila) {
      double deltaX = player.getX() - schussBild.getX();
      double deltaY = player.getY() - schussBild.getY();
      double dist = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
      
      if (dist != 0) {
        geschwindigkeitX = (deltaX / dist) * (speed / difficulty);
        geschwindigkeitY = (deltaY / dist) * (speed / difficulty);
        double winkel = Math.toDegrees(Math.atan2(geschwindigkeitY, geschwindigkeitX));
        schussBild.setRotate(winkel);
      }
      
      double testX = schussBild.getX() + geschwindigkeitX;
      double testY = schussBild.getY() + geschwindigkeitY;
      
      if (!kollisionsCheckVirtuell(testX, testY, schussBild, wandListe, borderListe)) {
        schussBild.setX(testX);
        schussBild.setY(testY);
      } else {
        reflektiereSchuss(schussBild, wandListe, borderListe);
        bounces++;
      }
    } else {
      double deltaX = this.geschwindigkeitX;
      double deltaY = this.geschwindigkeitY;
      
      double testX = schussBild.getX() + deltaX;
      double testY = schussBild.getY() + deltaY;
      
      if (!kollisionsCheckVirtuell(testX, testY, schussBild, wandListe, borderListe)) {
        schussBild.setX(schussBild.getX() + deltaX);
        schussBild.setY(schussBild.getY() + deltaY);
      } else {
        reflektiereSchuss(schussBild, wandListe, borderListe);
        bounces++;
      }  
    } 
  }

  private void reflektiereSchuss(ImageView schussBild, ArrayList<ImageView> wandListe, ArrayList<Rectangle> borderListe) {
    double testX = schussBild.getX() + geschwindigkeitX;
    double testY = schussBild.getY() + geschwindigkeitY;
    
    boolean kollisionX = kollisionsCheckVirtuell(testX, schussBild.getY(), schussBild, wandListe, borderListe);
    boolean kollisionY = kollisionsCheckVirtuell(schussBild.getX(), testY, schussBild, wandListe, borderListe);
    if (kollisionX) {
      this.geschwindigkeitX = -this.geschwindigkeitX;
      double neuerWinkel = berechneReflexionswinkel(schussBild.getRotate(), true);  // true für X-Achse
      schussBild.setRotate(neuerWinkel);
    }
    
    if (kollisionY) {
      this.geschwindigkeitY = -this.geschwindigkeitY;
      double neuerWinkel = berechneReflexionswinkel(schussBild.getRotate(), false);  // false für Y-Achse
      schussBild.setRotate(neuerWinkel);
    }
    Sounds.bounceSound();
  }

  private double berechneReflexionswinkel(double aktuellerWinkel, boolean isXDirection) {
    double winkel = Math.toRadians(aktuellerWinkel);
    double vektorX = Math.cos(winkel);
    double vektorY = Math.sin(winkel);
    
    double normalX = isXDirection ? 1 : 0;  // X-Achse (vertikale Wand)
    double normalY = isXDirection ? 0 : 1;  // Y-Achse (horizontale Wand)
    
    double dotProduct = vektorX * normalX + vektorY * normalY;
    
    double reflektierterX = vektorX - 2 * dotProduct * normalX;
    double reflektierterY = vektorY - 2 * dotProduct * normalY;
    
    double neuerWinkel = Math.toDegrees(Math.atan2(reflektierterY, reflektierterX));
    if (neuerWinkel < 0) {
      neuerWinkel += 360;
    }
    return neuerWinkel;
  }

  private boolean kollisionsCheckVirtuell(double testX, double testY, ImageView schussBild, ArrayList<ImageView> wandListe, ArrayList<Rectangle> borderListe){
    Rectangle virtualRectangle = new Rectangle(testX, testY, schussBild.getBoundsInLocal().getWidth(), schussBild.getBoundsInLocal().getHeight());
    for (ImageView wand : wandListe) {
      if (virtualRectangle.intersects(wand.getBoundsInParent())) {
        return true;
      }
    }
    
    for (Rectangle border : borderListe) {
      if (virtualRectangle.intersects(border.getBoundsInParent())) {
        return true;
      }
    }
    return false;
  }
}
