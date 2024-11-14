import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;

public class Gegner {
  public Gegner(){
    
  }

  
  public void ai(ImageView gegner, ImageView spieler, Rectangle wand1, Rectangle wand2){
    System.out.println(siehtSpieler(spieler, gegner, wand1, wand2));
  }
  
  public boolean siehtSpieler(ImageView spieler, ImageView gegner, Rectangle wand1, Rectangle wand2){
    Line lineOfSight = new Line(gegner.getX(), gegner.getY(), spieler.getX(), spieler.getY());
    return false;
  }


}