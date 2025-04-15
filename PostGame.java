import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
  private Image hintergrundImage = new Image(getClass().getResourceAsStream("src/assets/images/postgame_bg.png"));
  private ImageView hintergrund = new ImageView(hintergrundImage);

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

      TxtManager.updateHighscore(level);

      hintergrund.setFitWidth(bildschirmBreite);
      hintergrund.setFitHeight(bildschirmHoehe);
      hintergrund.setVisible(true);
      root.getChildren().add(hintergrund);

      VBox vbox = new VBox(10);
      vbox.setAlignment(Pos.CENTER);
      vbox.setPrefWidth(bildschirmBreite);
      vbox.setPrefHeight(bildschirmHoehe);

      hauptmenu.setOnAction((event) -> hauptmenu_Action(event));
      hauptmenu.setText("Mission abbrechen");
      hauptmenu.setVisible(true);
      hauptmenu.getStyleClass().add("button");

      if (win) {
        nextRound.setOnAction((event) -> nextRound_Action(event));
        nextRound.setText("Nächster Tag");
        nextRound.getStyleClass().add("button");
        nextRound.setVisible(true);

        text1 = new Label("Tag " + level + " geschafft!");
      } else {
        nextRound.setOnAction((event) -> neuerTry(event));
        nextRound.setText("Neue Mission starten");
        nextRound.getStyleClass().add("button");
        nextRound.setVisible(true);
        text1 = new Label("An Tag " + level + " gescheitert!");
      }

      if(level == 14){
        nextRound.getStyleClass().add("widebutton");
        hauptmenu.getStyleClass().add("widebutton");
        text1.setText("Du hast 14 Tage überlebt! \nHerzlichen Glückwunsch!");
        nextRound.setText("Mal schauen, wie lange du es schaffst!");
        hauptmenu.setText("Mission abbrechen und zurück zur Familie");
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

  public void neuerTry(Event event) {
    Map map = new Map();
    map.resetLevel();
    map.initialize(stageTemp);
    stageTemp.setFullScreen(true);
    stageTemp.setFullScreenExitHint("");
  }

}
