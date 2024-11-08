import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;

public class Gegner{
  private int xPos;
  private int yPos;
  
  public int getX(){
    return xPos;
  }
  
  public int getY(){
    return yPos;
  }
  
  public void setX(int xNeu){
    xPos = xNeu;
  }
  
  public void setY(int yNeu){
    yPos = yNeu;
  }
  
  
  public void ai(ImageView gegner, ImageView spieler, Rectangle wand1, Rectangle wand2){
    boolean siehtSpieler = false;
    if (gegner.getX() < spieler.getX() && gegner.getY() < spieler.getY()) {
      for (double i = gegner.getX(); i < spieler.getX(); i++) {
        for (double j = gegner.getY(); j < spieler.getY(); j++) {
          if (!wand1.intersects(i,j,1,1)) {
            siehtSpieler = true;
            break;
          }
        }
      }
      siehtSpieler = false;
    }
  }

}