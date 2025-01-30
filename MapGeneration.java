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
    
    public class MapGeneration extends Application {
      private Random random = new Random();
      private ArrayList<Rectangle> alleRectangles = new ArrayList<>();
      private final int minDistance = 200;
      private final  int maxAttempts = 100;
      private final int windowWidth = 1280;
      private final int windowHeight = 720;
      private final int abstand = 250;
      private final int maxAnzahlBloecke = 5;
      private final int minAnzahlBloecke = 2; 
    
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
          alleRectangles.clear(); 
          generateMap(mapPane);
        });
        
        // Vbox und scene werden erstellt für beide buttons
        VBox buttonBox = new VBox(5);
        buttonBox.getChildren().addAll(exitButton, regenerateButton);
        root.getChildren().addAll(buttonBox, mapPane);
        Scene scene = new Scene(root, windowWidth, windowHeight);
        generateMap(mapPane);
        
        stage.setFullScreen(true); 
        stage.setTitle("Wee-Tanks");
        stage.setScene(scene);
        stage.show();
      }
    
    
    
      private void MapGeneration(Pane mapPane) {
        int attempts = 0;
        while (attempts < maxAttempts) {
          boolean overlap = true;
          Rectangle rect = new Rectangle();
          
          // generiert zufällig horizontale oder vertikale rectangles
          int randomInt = random.nextInt(2);
          if (randomInt == 1) {  // Bei 1 wird ein Horizontales rectangle erstellt
            rect.setWidth(200);
            rect.setHeight(80);
          } else {                // sonst wird ein vertikales erstellt
            rect.setWidth(80);
            rect.setHeight(200);
          }
          
                                     // windowWidth = 1280 - die width des rectangles
          int availableWidth = (int)(windowWidth - rect.getWidth());
                                       // windowHeight = 720 - die höhe des rectangles
          int availableHeight = (int) (windowHeight - rect.getHeight());
  
          // setzt die X / Y Koordinaten der rectangles inerhalb der spielfläche
          rect.setX(random.nextInt(availableWidth));
          rect.setY(random.nextInt(availableHeight));
  
          // erstellt ein 2. unsichbares rectangle mit der größe des 1. rectangles + einen festen abstand (250px)
          // damit wird sicher gegangen, dass rectangles nicht ineinander generiert werden könnten
          Rectangle rect2 = new Rectangle();
          rect2.setWidth(rect.getWidth()+abstand*2);
          rect2.setHeight(rect.getHeight()+abstand*2);      
          rect2.setX(rect.getX()-abstand);
          rect2.setY(rect.getY()-abstand);
          
          // wenn rect2 mit rect1 overlapped, wird das rectangle nicht generiert
          overlap = false;
          for (Rectangle existingRect : alleRectangles) {
              if (rect2.intersects(existingRect.getBoundsInParent())) {
                overlap = true;
                break;
              }
          }
          
          // falls kein overlap besteht, wird das rectangle erstellt
          if (!overlap) {
            alleRectangles.add(rect);
            mapPane.getChildren().add(rect);

            return;
          }
          
          // maximale versuche rectangles zu generieren: 100, wenn die nummer erreicht wird, werden keine mehr generiert
          attempts++;
        }
      }  
    
      // hier wird festgelegt wie viele rectangles minmal und maximal auf der map sein dürfen
      private void generateMap(Pane mapPane) {
        int numberOfRectangles = random.nextInt(maxAnzahlBloecke) + minAnzahlBloecke;
        for (int i = 0; i < numberOfRectangles; i++) {
          MapGeneration(mapPane);
        }
      }
    
      public static void main(String[] args) {                      
        launch(args);
      }
    }
