import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.event.*;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class PostGame{
  private boolean win;
  private int level;
  private double bildschirmBreite; 
  private double bildschirmHoehe;
  private Button nextRound = new Button();
  private Button hauptmenu = new Button();
  private Label text1 = new Label("Level " + level + " geschafft!");
  private Stage stageTemp; 
  
  public PostGame(int lvl, boolean win){
    this.level = lvl;
    this.win = win;
    bildschirmBreite = Screen.getPrimary().getBounds().getWidth();
    bildschirmHoehe = Screen.getPrimary().getBounds().getHeight();
  }
  
  public void initialize(Stage stage){
    stageTemp = stage;
    Pane root = new Pane();
    Scene scene = new Scene(root, bildschirmBreite, bildschirmHoehe);
    stage.setFullScreen(true);
    stage.setFullScreenExitHint("");
    
    hauptmenu.setOnAction((event) -> hauptmenu_Action(event));
    hauptmenu.setPrefHeight(33);
    hauptmenu.setPrefWidth(113);
    hauptmenu.setLayoutY((bildschirmHoehe / 2) - (hauptmenu.getPrefHeight() / 2));
    hauptmenu.setLayoutX((bildschirmBreite / 2) - (hauptmenu.getPrefWidth() / 2));
    hauptmenu.setText("Zurück zum Hauptmenu");
    root.getChildren().add(hauptmenu);
    hauptmenu.setVisible(true);
    
    if(win){
      nextRound.setOnAction((event) -> nextRound_Action(event));
      nextRound.setPrefWidth(113);
      nextRound.setPrefHeight(33);
      nextRound.setLayoutX(hauptmenu.getLayoutX());
      nextRound.setLayoutY(hauptmenu.getLayoutY() - 66);
      nextRound.setText("Nächste Runde");
      root.getChildren().add(nextRound);
      nextRound.setVisible(true);
    }
    
    
    
    stage.setScene(scene);
    stage.show();
  }

  public void hauptmenu_Action(Event event){
    Hauptmenu h = new Hauptmenu();
    h.start(stageTemp);
    stageTemp.setFullScreen(true);
    stageTemp.setFullScreenExitHint("");
  }

  public void nextRound_Action(Event event) {
    Map map = new Map();
    map.initialize(stageTemp);
    stageTemp.setFullScreen(true);
    stageTemp.setFullScreenExitHint("");
  }

}
