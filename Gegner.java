import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;

public class Gegner {
  public Gegner(){
    
  }

  
  public void ai(ImageView gegner, ImageView spieler, Rectangle wand1, Rectangle wand2){
    System.out.println(siehtSpieler(spieler, gegner, wand1, null));
  }
  
  public boolean siehtSpieler(ImageView spieler, ImageView gegner, Rectangle wand1, Rectangle wand2){
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
    
    return false;
  }


}