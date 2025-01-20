import java.util.ArrayList;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.shape.Rectangle;
import javafx.animation.AnimationTimer;

public class Schuss{
  private double geschwindigkeitX;
  private double geschwindigkeitY;
  
  public Schuss(){
    
  }
  
  public boolean kollisionsCheck(ImageView schuss, ArrayList<ImageView> wandListe, ArrayList<Rectangle> borderListe){
    //Schuss bewegt sich in die ausgerechnete Richtung
    schuss.setX(schuss.getX() + geschwindigkeitX);
    schuss.setY(schuss.getY() + geschwindigkeitY);
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
  
  public boolean schiessen(MouseEvent evt, ImageView schuss){
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
  
  public boolean schiessenGegner(ImageView gegner, ImageView gegnerTurret, ImageView spieler){
    //Koordinaten vom Mauszeiger
    double eventX = spieler.getX();
    double eventY = spieler.getY();
    //Rotation in Grad umwandeln in Radiant
    double radiant = Math.toRadians(gegnerTurret.getRotate());
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

}
