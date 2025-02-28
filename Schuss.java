import java.util.ArrayList;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class Schuss{
  private final double SPEED = 5;
  private double geschwindigkeitX;
  private double geschwindigkeitY;
  private boolean istSpieler;
  private int bounces;
   
  public Schuss(ImageView schuetzeTurret, boolean spieler){
    double radiant = Math.toRadians(schuetzeTurret.getRotate());
    double vektorX = Math.cos(radiant);
    double vektorY = Math.sin(radiant);
    double pythagoras = Math.sqrt(vektorX * vektorX + vektorY * vektorY);
    this.istSpieler = spieler;
    this.geschwindigkeitX = (vektorX / pythagoras) * SPEED;     
    this.geschwindigkeitY = (vektorY / pythagoras) * SPEED;
    this.bounces = 0;
  }
  
  public boolean getSpieler(){
    return istSpieler;
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

  public boolean trefferCheck(ImageView schussBild, ImageView player){ 
    if (schussBild.intersects(player.getBoundsInParent())) {
      return true;
    }
    return false;
  }
  
  public void fliegen(ImageView schussBild, ArrayList<ImageView> wandListe, ArrayList<Rectangle> borderListe){
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
        return true;  // Kollision mit Wand
      }
    }// Überprüfe, ob der Schuss in der virtuellen Position mit einem Border kollidiert
    
    for (Rectangle border : borderListe) {
      if (virtualRectangle.intersects(border.getBoundsInParent())) {
        return true;  // Kollision mit Border
      }
    }
    return false; // Keine Kollision erkannt
  }
}
