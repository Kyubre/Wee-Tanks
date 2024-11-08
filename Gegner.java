import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;

public class Gegner {
  public Gegner(){
    
  }

  
  public void ai(ImageView gegner, ImageView spieler, Rectangle wand1, Rectangle wand2){
    System.out.println(siehtSpieler(spieler, gegner, wand1, wand2));
  }
  
  public boolean siehtSpieler(ImageView spieler, ImageView gegner, Rectangle wand1, Rectangle wand2){
    //Gegner unten links von Spieler
    if (gegner.getX() < spieler.getX() && gegner.getY() < spieler.getY()) {
      for (double i = gegner.getX(); i < spieler.getX(); i++) {
        for (double j = gegner.getY(); j < spieler.getY(); j++) {
          if (!wand1.intersects(i,j,1,1) && !wand2.intersects(i,j,1,1)) {
            return true;
          }
        }
      }
      return false;
    }
    //Gegner oben links vom Spieler
    else if (gegner.getX() < spieler.getX() && gegner.getY() > spieler.getY()) {
      for (double i = gegner.getX(); i < spieler.getX(); i++) {
        for (double j = gegner.getY(); j > spieler.getY(); j++) {
          if (!wand1.intersects(i,j,1,1) && !wand2.intersects(i,j,1,1)) {
            return true;
          }
        }
      }
      return false;
    }
    //Gegner unten rechts von Spieler
    else if (gegner.getX() > spieler.getX() && gegner.getY() > spieler.getY()) {
      for (double i = gegner.getX(); i > spieler.getX(); i++) {
        for (double j = gegner.getY(); j > spieler.getY(); j++) {
          if (!wand1.intersects(i,j,1,1) && !wand2.intersects(i,j,1,1)) {
            return true;
          }
        }
      }
      return false;
    }
    //Gegner oben rechts von Spieler
    else if (gegner.getX() > spieler.getX() && gegner.getY() < spieler.getY()) {
      for (double i = gegner.getX(); i > spieler.getX(); i++) {
        for (double j = gegner.getY(); j < spieler.getY(); j++) {
          if (!wand1.intersects(i,j,1,1) && !wand2.intersects(i,j,1,1)) {
            return true;
          }
        }
      }
      return false;
    }
    //Gegner unten mittig von Spieler
    else if (gegner.getY() > spieler.getY()) {
      for (double j = gegner.getY(); j > spieler.getY(); j++) {
        if (!wand1.intersects(spieler.getX(),j,1,1) && !wand2.intersects(spieler.getX(),j,1,1)) {
          return true;
        }
      }
      return false;
    }
    //Gegner oben mittig von Spieler
    else if (gegner.getY() < spieler.getY()) {
      for (double j = gegner.getY(); j < spieler.getY(); j++) {
        if (!wand1.intersects(spieler.getX(),j,1,1) && !wand2.intersects(spieler.getX(),j,1,1)) {
          return true;
        }
      }
      return false;
    }
    //Gegner mittig links vom Spieler
    else if (gegner.getX() < spieler.getX()) {
      for (double i = gegner.getX(); i < spieler.getX(); i++) {
        if (!wand1.intersects(i,spieler.getY(),1,1) && !wand2.intersects(i,spieler.getY(),1,1)) {
          return true;
        }
      }
      return false;
    }
    //Gegner mittig rechts vom Spieler
    else if (gegner.getX() > spieler.getX()) {
      for (double i = gegner.getX(); i > spieler.getX(); i++) {
        if (!wand1.intersects(i,spieler.getY(),1,1) && !wand2.intersects(i,spieler.getY(),1,1)) {
          return true;
        }
      }
      return false;
    }
    return false;
  }


}