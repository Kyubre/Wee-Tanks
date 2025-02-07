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
import javafx.scene.image.ImageView;
import javafx.scene.image.*;
    
public class MapGeneration extends Application {
  private Random random = new Random();
  private ArrayList<ImageView> alleWaende = new ArrayList<>();
  private final int minDistance = 200;
  private final int maxAttempts = 100;
  private final int windowWidth = 1280;
  private final int windowHeight = 720;
  private final int abstand = 250;
  private final int maxAnzahlBloecke = 10;
  private final int minAnzahlBloecke = 6; 
  private Image vWand = new Image(getClass().getResourceAsStream("images/wall_v.png"));
  private Image hWand = new Image(getClass().getResourceAsStream("images/wall_h.png"));
    
  @Override
  public void start(Stage stage) {
    initialize(stage);
  }
    
  public void initialize(Stage stage) {        
    Pane mapPane = new Pane();
    VBox root = new VBox();
    
    // button um das spiel zu schließen
    Button exitButton = new Button("Exit");
    exitButton.setOnAction(e -> {
      stage.close();
      System.exit(0);
    });
    
    // regenerate Button
    Button regenerateButton = new Button("Regenerate Map");
    regenerateButton.setOnAction(e -> {
      mapPane.getChildren().clear();
      alleWaende.clear(); 
      generateMap(mapPane);
    });
    
    // Vbox und scene werden erstellt für beide buttons
    VBox buttonBox = new VBox(5);
    buttonBox.getChildren().addAll(exitButton, regenerateButton);
    root.getChildren().addAll(buttonBox, mapPane);
    Scene scene = new Scene(root, windowWidth, windowHeight);
    generateMap(mapPane);
    
    stage.setFullScreen(true); 
    stage.setTitle("Map Generation Test");
    stage.setScene(scene);
    stage.show();
  }
    
    
    
  private void placeWalls(Pane mapPane) {
    int attempts = 0;
    while (attempts < maxAttempts) {
      boolean overlap = true;
      ImageView wall = new ImageView();
      
      // generiert zufällig horizontale oder vertikale rectangles
      int randomInt = random.nextInt(2);
      if (randomInt == 1) {  // Bei 1 wird ein Horizontales rectangle erstellt
        wall.setFitWidth(200);
        wall.setFitHeight(80);
        wall.setImage(hWand);
      } else {                // sonst wird ein vertikales erstellt
        wall.setFitWidth(80);
        wall.setFitHeight(200);
        wall.setImage(vWand);
      }
      
      // windowWidth = 1280 - die width des rectangles
      int availableWidth = (int)(windowWidth - wall.getFitWidth());
      // windowHeight = 720 - die höhe des rectangles
      int availableHeight = (int) (windowHeight - wall.getFitHeight());
      
      // setzt die X / Y Koordinaten der rectangles inerhalb der spielfläche
      wall.setX(random.nextInt(availableWidth));
      wall.setY(random.nextInt(availableHeight));
      
      // erstellt ein 2. unsichbares rectangle mit der größe des 1. rectangles + einen festen abstand (250px)
      // damit wird sicher gegangen, dass rectangles nicht ineinander generiert werden könnten
      Rectangle rect2 = new Rectangle();
      rect2.setWidth(wall.getFitWidth()+abstand*2);
      rect2.setHeight(wall.getFitHeight()+abstand*2);      
      rect2.setX(wall.getX()-abstand);
      rect2.setY(wall.getY()-abstand);
      
      // wenn rect2 mit rect1 overlapped, wird das rectangle nicht generiert
      overlap = false;
      for (ImageView existingWall : alleWaende) {
        if (rect2.intersects(existingWall.getBoundsInParent())) {
          overlap = true;
          break;
        }
      }
      
      // falls kein overlap besteht, wird das rectangle erstellt
      if (!overlap) {
        alleWaende.add(wall);
        mapPane.getChildren().add(wall);
        
        return;
      }
      
      // maximale versuche rectangles zu generieren: 100, wenn die nummer erreicht wird, werden keine mehr generiert
      attempts++;
    }
  }  
    
      // hier wird festgelegt wie viele rectangles minmal und maximal auf der map sein dürfen
  private void generateMap(Pane mapPane) {
    int numberOfWalls = random.nextInt(maxAnzahlBloecke) + minAnzahlBloecke;
    for (int i = 0; i < numberOfWalls; i++) {
      placeWalls(mapPane);
    }
  }
    
  public static void main(String[] args) {                      
    launch(args);
  }
}
