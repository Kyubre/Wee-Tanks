import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Random;
import javafx.animation.AnimationTimer;

public class Karte extends Application {
  private Random random = new Random();
  private ArrayList<Rectangle> alleRectangles = new ArrayList<>();
  private int minDistance = 200;
  private int maxAttempts = 100; 
  private int windowWidth; 
  private int windowHeight; 

  @Override
  public void start(Stage stage) {
    initialize(stage);
  }

  public void initialize(Stage stage) {
    windowWidth = 1920;
    windowHeight = 1080;
    
    Pane mapPane = new Pane();
    VBox root = new VBox();
    
    Button exitButton = new Button("Exit");
    exitButton.setOnAction(e -> {
      stage.close(); // Schließt das Fenster
      System.exit(0); // Beendet die Anwendung
    });
    
    Button regenerateButton = new Button("Regenerate Map");
    regenerateButton.setOnAction(e -> {
      mapPane.getChildren().clear();
      alleRectangles.clear(); 
      generateMap(mapPane);
    });
    
    VBox buttonBox = new VBox(5);
    buttonBox.getChildren().addAll(exitButton, regenerateButton);
    root.getChildren().addAll(buttonBox, mapPane);
    Scene scene = new Scene(root, windowWidth, windowHeight);
    generateMap(mapPane);
    
    stage.setFullScreen(true); 
    stage.setTitle("Karte");
    stage.setScene(scene);
    stage.show();
  }



  private void idk(Pane mapPane) {
    int attempts = 0;
    while (attempts < maxAttempts) {
      boolean overlap = true;
      Rectangle rect = new Rectangle();
      
      // Random orientation
      int randomInt = random.nextInt(2); // 0 or 1
      if (randomInt == 1) {
        // Horizontal rectangle
        rect.setWidth(200);
        rect.setHeight(80);
      } else {
        // Vertical rectangle
        rect.setWidth(80);
        rect.setHeight(200);
      }
      
      int availableWidth = windowWidth - 500;
      int availableHeight = windowHeight - 100;
      int abstand = 250;
      
      // Set random position within the bounds
      rect.setX(random.nextInt((int) (availableWidth - rect.getWidth())));
      rect.setY(random.nextInt((int) (availableHeight - rect.getHeight())));
      
      Rectangle rect2 = new Rectangle();
      rect2.setWidth(rect.getWidth()+abstand*2);
      rect2.setHeight(rect.getHeight()+abstand*2);      
      rect2.setX(rect.getX()-abstand);
      rect2.setY(rect.getY()-abstand);
      
      overlap = false;
      
      // Check for overlap and minimum distance
      for (Rectangle existingRect : alleRectangles) {
        if (rect2.intersects(existingRect.getBoundsInParent())) {
          overlap = true;
          break;
        }
      }
      
      // If no overlap, add the rectangle
      if (!overlap) {
        alleRectangles.add(rect);
        mapPane.getChildren().add(rect);
        return;
      }
      
      attempts++;
    }
  }  

  private void generateMap(Pane mapPane) {
    int numberOfRectangles = random.nextInt(4) + 2;
    for (int i = 0; i < numberOfRectangles; i++) {
      idk(mapPane);
    }
  }

  public static void main(String[] args) {                      
    launch(args);
  }
}
