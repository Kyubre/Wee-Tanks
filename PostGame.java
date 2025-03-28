import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PostGame {
  private boolean win;
  private int level;
  private double bildschirmBreite;
  private double bildschirmHoehe;
  private Button nextRound = new Button();
  private Button hauptmenu = new Button();
  private Label text1;
  private Stage stageTemp;

  public PostGame(int lvl, boolean win) {
    this.level = lvl;
    this.win = win;
    bildschirmBreite = Screen.getPrimary().getBounds().getWidth();
    bildschirmHoehe = Screen.getPrimary().getBounds().getHeight();
  }

  public void initialize(Stage stage) {
    nextRound.getStyleClass().add("button");
    Platform.runLater(() -> {
      stageTemp = stage;
      Pane root = new Pane();
      Scene scene = new Scene(root, bildschirmBreite, bildschirmHoehe);
      scene.getStylesheets().add("src/Styles/postgame.css");
      stage.setScene(scene);
      stage.setResizable(false);
      stage.setHeight(bildschirmHoehe);
      stage.setWidth(bildschirmBreite);
      stage.setFullScreen(true);
      stage.setFullScreenExitHint("");
      stage.setFullScreenExitKeyCombination(null);
      stage.show();

      VBox vbox = new VBox(10);
      vbox.setAlignment(Pos.CENTER);
      vbox.setPrefWidth(bildschirmBreite);
      vbox.setPrefHeight(bildschirmHoehe);

      hauptmenu.setOnAction((event) -> hauptmenu_Action(event));
      hauptmenu.setText("Zurück zum Hauptmenu");
      hauptmenu.setVisible(true);
      hauptmenu.getStyleClass().add("button");

      if (win) {
        nextRound.setOnAction((event) -> nextRound_Action(event));
        nextRound.setText("Nächste Runde");
        nextRound.getStyleClass().add("button");
        nextRound.setVisible(true);

        text1 = new Label("Level " + level + " geschafft!");
      } else {
        text1 = new Label("An Level " + level + " gescheitert!");
      }
      text1.getStyleClass().add("label");
      text1.setVisible(true);

      vbox.getChildren().addAll(text1, nextRound, hauptmenu);
      root.getChildren().add(vbox);
    });
  }

  public void hauptmenu_Action(Event event) {
    new Map().resetLevel();
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
