import javafx.scene.input.MouseEvent;
import javafx.scene.image.*;
import javafx.scene.image.ImageView;

public class Player{
  private double xPos;
  private double yPos;
  private double centerPosX;
  private double centerPosY;
  
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
  
  
  
  public double getX(){
    return xPos;
  }

  public double getY(){
    return yPos;
  }
  
  public double getCenterX(){
    return centerPosX;
  }
  
  public double getCenterY(){
    return centerPosY;
  }

}
