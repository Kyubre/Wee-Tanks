import javafx.scene.input.MouseEvent;
import javafx.scene.image.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

public class Player{
  private double xPos;
  private double yPos;
  private double centerPosX;
  private double centerPosY;
  private boolean wGedrueckt = false;
  private boolean aGedrueckt = false;
  private boolean sGedrueckt = false;
  private boolean dGedrueckt = false;
  
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
  
  public void shoot(Player panzer){
    
  }
  
  public void tasteGedrueckt(KeyEvent evt){
    switch (evt.getCode()) {
        case W: 
          wGedrueckt = true;
          break;
        case A: 
          aGedrueckt = true;
          break;
        case S: 
          sGedrueckt = true;
          break;
        case D: 
          dGedrueckt = true;
          break;
        default: 
          break;  
      }
  }
  
  public void tasteLosgelassen(KeyEvent evt){
    switch (evt.getCode()) {
        case W: 
          wGedrueckt = false;
          break;
        case A: 
          aGedrueckt = false;
          break;
        case S: 
          sGedrueckt = false;
          break;
        case D: 
          dGedrueckt = false;
          break;
        default: 
          break;  
      }
  }

  
  public void movement(ImageView panzer, ImageView turret){    
    if (wGedrueckt && aGedrueckt) {
      panzer.setY(panzer.getY()-10);
      panzer.setX(panzer.getX()-10);
      panzer.setRotate(225);         
      turret.setY(turret.getY()-10);
      turret.setX(turret.getX()-10);
    } 
    else if (wGedrueckt && dGedrueckt) {
      panzer.setY(panzer.getY()-10);
      panzer.setX(panzer.getX()+10);
      panzer.setRotate(315);
      turret.setY(turret.getY()-10);
      turret.setX(turret.getX()+10);
    } 
    else if (sGedrueckt && aGedrueckt) {
      panzer.setY(panzer.getY()+10);
      panzer.setX(panzer.getX()-10);
      panzer.setRotate(135);
      turret.setY(turret.getY()+10);
      turret.setX(turret.getX()-10);
    } 
    else if (sGedrueckt && dGedrueckt) {
      panzer.setY(panzer.getY()+10);
      panzer.setX(panzer.getX()+10);
      panzer.setRotate(45);
      turret.setY(turret.getY()+10);
      turret.setX(turret.getX()+10);
    } 
    else if (wGedrueckt) {
      panzer.setY(panzer.getY()-10);
      panzer.setRotate(270);
      turret.setY(turret.getY()-10);
    } 
    else if (aGedrueckt) {
      panzer.setX(panzer.getX()-10);
      panzer.setRotate(180);
      turret.setX(turret.getX()-10);
    } 
    else if (sGedrueckt) {
      panzer.setY(panzer.getY()+10);
      panzer.setRotate(90);
      turret.setY(turret.getY()+10);
    } 
    else if (dGedrueckt) {
      panzer.setX(panzer.getX()+10);
      panzer.setRotate(0);
      turret.setX(turret.getX()+10);
    }
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
