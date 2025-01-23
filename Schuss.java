import java.util.ArrayList;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.shape.Rectangle;
import javafx.animation.AnimationTimer;

public class Schuss{
  private final double SPEED = 5;
  private double geschwindigkeitX;
  private double geschwindigkeitY;
  private boolean istSpieler;
   
  public Schuss(ImageView schuetzeTurret, boolean spieler){
    //Temporäre Zahlen um Vektoren in X und Y Richtung auszurechnen
    double radiant = Math.toRadians(schuetzeTurret.getRotate());
    double vektorX = Math.cos(radiant);
    double vektorY = Math.sin(radiant);
    double pythagoras = Math.sqrt(vektorX * vektorX + vektorY * vektorY);
    this.istSpieler = spieler;
    this.geschwindigkeitX = (vektorX / pythagoras) * SPEED;     
    this.geschwindigkeitY = (vektorY / pythagoras) * SPEED;
  }
  
  public boolean getSpieler(){
    return istSpieler;
  }

  
  public boolean kollisionsCheck(ImageView schussBild, ArrayList<ImageView> wandListe, ArrayList<Rectangle> borderListe){
    //Überprüfung, ob der Schuss mit den Wänden kollidiert
    for (ImageView wand : wandListe) {
      if (schussBild.intersects(wand.getBoundsInParent())) {
        return true;
      }
    }
    
    for (Rectangle border : borderListe) {
      if (schussBild.intersects(border.getBoundsInParent())) {
        return true;
      }
    }
    
    return false;
  }
  
  public boolean trefferCheck(ImageView schussBild, ImageView player){
    //Überprüfung, ob der Schuss mit dem Spieler kollidiert
    //    schussBild.setX(schussBild.getX() + geschwindigkeitX);
    //    schussBild.setY(schussBild.getY() + geschwindigkeitY);    
    if (schussBild.intersects(player.getBoundsInParent())) {
      return true;
    }
    return false;
  }
  
//  public boolean schiessenGegner(ImageView gegner, ImageView gegnerTurret, ImageView spieler){
//    //Rotation in Grad umwandeln in Radiant
//    double radiant = Math.toRadians(gegnerTurret.getRotate());
//    //Sinus und Cosinus benutzen um Radiant in X & Y Vektoren umzurechnen
//    double vektorX = Math.cos(radiant);
//    double vektorY = Math.sin(radiant);
//    //Satz des Pythagoras (A^2 + B^2 = C^2) anwenden um Vektoren X^2 & Y^2 zu Z^2 zu machen
//    //Math.sqrt berechnet automatisch die Quadratwurzel aus der Rechnung (C^2 bzw. Z^2 wird zu C bzw. Z)
//    double pythagoras = Math.sqrt(vektorX * vektorX + vektorY * vektorY);
//    //Geschwindigkeit berechnen mit anpassbaren Werten
//    this.geschwindigkeitX = (vektorX / pythagoras) * SPEED;     
//    this.geschwindigkeitY = (vektorY / pythagoras) * SPEED;     
//    //false wird in Map1 für den boolean "Kollision" eingesetzt
//    return false;
//  }
  
  public void fliegen(ImageView schussBild){
    schussBild.setX(schussBild.getX()+this.geschwindigkeitX);
    schussBild.setY(schussBild.getY()+this.geschwindigkeitY);
  }


}
