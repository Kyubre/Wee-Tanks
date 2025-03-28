import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.event.*;
import javafx.scene.control.Button;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.scene.control.Label;

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
      if (stage == null) {
        System.out.println("Fehler: Stage ist null!");
        return;
      }
      stageTemp = stage;
      Pane root = new Pane();
      Scene scene = new Scene(root, bildschirmBreite, bildschirmHoehe);
      scene.getStylesheets().add("src/Styles/hauptmenu.css");
      stage.setScene(scene);
      stage.setResizable(false);
      stage.setHeight(bildschirmHoehe);
      stage.setWidth(bildschirmBreite);
      stage.setFullScreen(true);
      stage.setFullScreenExitHint("");
      stage.setFullScreenExitKeyCombination(null);
      stage.show();

      hauptmenu.setOnAction((event) -> hauptmenu_Action(event));
      hauptmenu.setPrefHeight(32);
      hauptmenu.setPrefWidth(150);
      hauptmenu.setLayoutY((bildschirmHoehe / 2) - (hauptmenu.getPrefHeight() / 2));
      hauptmenu.setLayoutX(zentrieren(hauptmenu.getPrefWidth()));
      hauptmenu.setText("Zurück zum Hauptmenu");
      root.getChildren().add(hauptmenu);
      hauptmenu.setVisible(true);
      hauptmenu.getStyleClass().add("button");

      if (win) {
        nextRound.setOnAction((event) -> nextRound_Action(event));
        nextRound.setPrefWidth(112);
        nextRound.setPrefHeight(32);
        nextRound.setLayoutX(zentrieren(nextRound.getPrefWidth()));
        nextRound.setLayoutY(hauptmenu.getLayoutY() - 66);
        nextRound.setText("Nächste Runde");
        root.getChildren().add(nextRound);
        nextRound.setVisible(true);

        text1 = new Label("Level " + level + " geschafft!");
      } else {
        text1 = new Label("An Level " + level + " gescheitert!");
      }

      text1.setPrefWidth(150);
      text1.setLayoutX(zentrieren(text1.getPrefWidth()));
      text1.setLayoutY(hauptmenu.getLayoutY() - 150);
      root.getChildren().add(text1);
      text1.setVisible(true);
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

  private double zentrieren(double objektBreite) {
    return (bildschirmBreite - objektBreite) / 2;
  }

}
