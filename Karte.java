import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Random;

public class Karte {
  private Random random = new Random();

  public void initialize(Stage stage) {
    Pane mapPane = new Pane(); 
    VBox root = new VBox();
    root.getChildren().add(mapPane);
    
    Button regenerateButton = new Button("Regenerate Map");
    regenerateButton.setOnAction(e -> {
      mapPane.getChildren().clear(); 
      generateMap(mapPane);
    });
    
    root.getChildren().add(0, regenerateButton);
    
    Scene scene = new Scene(root, 1280, 720);
    
    generateMap(mapPane);
    
    stage.setOnCloseRequest(e -> System.exit(0));
    stage.setTitle("Karte");
    stage.setScene(scene);
    stage.show();
  }

  private void generateMap(Pane mapPane) {
    ArrayList<Rectangle> alleRectangles = new ArrayList<>();
    
    for (int i = 0; i < random.nextInt(10) + 1; i++) {
      Rectangle rect = new Rectangle();
      rect.setX(random.nextInt(700));
      rect.setY(random.nextInt(1250));
      rect.setHeight(random.nextInt(250) + 80);
      rect.setWidth(random.nextInt(250) + 80);
      
      boolean overlap = false;
      for (Rectangle existingRect : alleRectangles) {
        //            if (rectanglesOverlap(rect, existingRect)) {
        //                overlap = true;
        //                break;
        //            }
      }
      
      if (!overlap) {
        alleRectangles.add(rect); 
        mapPane.getChildren().add(rect);
      }
    }
  }
}
