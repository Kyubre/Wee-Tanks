
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
  
  
  public void ai(ImageView gegner, ImageView spieler){
    double richtungX = gegner.getX() - spieler.getX();
    double richtungY = gegner.getY() - spieler.getY();
    
  }

}