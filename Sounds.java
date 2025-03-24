import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.File;
import java.util.ArrayList;

public class Sounds {
    private static boolean musicStarted = false;
    private static ArrayList<MediaPlayer> soundListe = new ArrayList<>();
    private static MediaPlayer bgmPlayer = new MediaPlayer(new Media(new File("src/assets/sounds/bgm.mp3").toURI().toString()));

    public static void bounceSound() {
        MediaPlayer bouncePlayer = new MediaPlayer(new Media(new File("src/assets/sounds/bounce.mp3").toURI().toString()));
        soundListe.add(bouncePlayer);
        bouncePlayer.setVolume(Settings.lautstaerke);
        bouncePlayer.play();
    }

    public static void bgmAbspielen() {
        if (!musicStarted) {
            soundListe.add(bgmPlayer);
            bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            bgmPlayer.setVolume(Settings.lautstaerke);
            bgmPlayer.play();
            musicStarted = true;
        }
    }

    public static void schussSound() {
        MediaPlayer schussPlayer = new MediaPlayer(new Media(new File("src/assets/sounds/schuss.mp3").toURI().toString()));
        soundListe.add(schussPlayer);
        schussPlayer.setVolume(Settings.lautstaerke);
        schussPlayer.play();
    }

    public static boolean getMusicStarted() {
        return musicStarted;
    }

    public static void pauseBgmMusic() {
        Timeline fadeOut = new Timeline(
            new KeyFrame(Duration.seconds(0), e -> bgmPlayer.setVolume(Settings.lautstaerke)),
            new KeyFrame(Duration.seconds(0.25), e -> bgmPlayer.setVolume(Settings.lautstaerke * 0.75)),
            new KeyFrame(Duration.seconds(0.5), e -> bgmPlayer.setVolume(Settings.lautstaerke * 0.5)),
            new KeyFrame(Duration.seconds(0.75), e -> bgmPlayer.setVolume(Settings.lautstaerke * 0.25)),
            new KeyFrame(Duration.seconds(1), e -> bgmPlayer.setVolume(Settings.lautstaerke * 0.1)),
            new KeyFrame(Duration.seconds(1.25), e -> {
                bgmPlayer.setVolume(0);
                bgmPlayer.pause();
                bgmPlayer.setVolume(Settings.lautstaerke);
            })
        );
        fadeOut.play();
    }

    public static void resumeBgmMusic() {
        Timeline fadeIn = new Timeline(
            new KeyFrame(Duration.seconds(0), e -> bgmPlayer.setVolume(0)),
            new KeyFrame(Duration.seconds(0.1), e -> bgmPlayer.setVolume(Settings.lautstaerke * 0.1)),
            new KeyFrame(Duration.seconds(0.2), e -> bgmPlayer.setVolume(Settings.lautstaerke * 0.25)),
            new KeyFrame(Duration.seconds(0.4), e -> bgmPlayer.setVolume(Settings.lautstaerke * 0.5)),
            new KeyFrame(Duration.seconds(0.6), e -> bgmPlayer.setVolume(Settings.lautstaerke * 0.75)),
            new KeyFrame(Duration.seconds(0.8), e -> bgmPlayer.setVolume(Settings.lautstaerke))
        );
        fadeIn.play();
        bgmPlayer.play();
    }
}
