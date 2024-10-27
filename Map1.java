import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;


public class Map1 extends Application {
  // Anfang Attribute
  private Rectangle border = new Rectangle();
  private Rectangle wall1 = new Rectangle();
  private Rectangle wall2 = new Rectangle();
    // Ende Attribute
  
  public void start(Stage primaryStage) { 
    Pane root = new Pane();
    Scene scene = new Scene(root, 1920, 1080);
    primaryStage.setMaximized(true);
    primaryStage.resizableProperty();
    // Anfang Komponenten
    

    border.setX(0);
    border.setY(0);
    border.setWidth(scene.getWidth());
    border.setHeight(scene.getHeight());
    root.getChildren().add(border);
    Color color = Color.TRANSPARENT;
    border.setFill(color);
    color = Color.BLACK;
    border.setStroke(color);
    border.setStrokeWidth(5.0);
    
    wall1.setX(480);
    wall1.setY(144);
    wall1.setWidth(120);
    wall1.setHeight(528);
    wall1.setSmooth(false);
    root.getChildren().add(wall1);
    
    wall2.setX(592);
    wall2.setY(552);
    wall2.setWidth(648);
    wall2.setHeight(120);
    wall2.setSmooth(false);
    root.getChildren().add(wall2);
   // Ende Komponenten
    
    primaryStage.setOnCloseRequest(e -> System.exit(0));
    primaryStage.setTitle("Map1");
    primaryStage.setScene(scene);
    primaryStage.show();
  } // end of public Map1
  
  // Anfang Methoden
  
  public static void main(String[] args) {
    launch(args);
  } // end of main
  
  // Ende Methoden
} // end of class Map1
