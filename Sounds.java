import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.File;
import java.util.ArrayList;
import javafx.animation.PauseTransition;

public class Sounds {
    private static boolean musicStarted = false;
    private static boolean looping = false;
    private static ArrayList<MediaPlayer> soundListe = new ArrayList<>();
    private static MediaPlayer startPlayer = new MediaPlayer(new Media(new File("src/assets/sounds/start.mp3").toURI().toString()));
    private static MediaPlayer loopPlayer = new MediaPlayer(new Media(new File("src/assets/sounds/loop.mp3").toURI().toString()));
    private static PauseTransition startTransition = new PauseTransition(Duration.seconds(19.0));

    public static void bounceSound() {
        MediaPlayer bouncePlayer = new MediaPlayer(new Media(new File("src/assets/sounds/bounce.mp3").toURI().toString()));
        soundListe.add(bouncePlayer);
        bouncePlayer.setVolume(Settings.lautstaerke);
        bouncePlayer.play();
    }

    public static void bgmAbspielen() {
        startPlayer.setVolume(Settings.musikLautstaerke);
        startPlayer.setCycleCount(1);
        startPlayer.play();
        if (!looping) {
            startTransition.play();
            startTransition.setOnFinished(e -> {
                startPlayer.stop();
                loopPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                loopPlayer.setVolume(Settings.musikLautstaerke);
                loopPlayer.play();
                looping = true;
            });
        }
        if (!musicStarted) {
            soundListe.add(loopPlayer);
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
            new KeyFrame(Duration.seconds(0), e -> loopPlayer.setVolume(Settings.musikLautstaerke)),
            new KeyFrame(Duration.seconds(0.25), e -> loopPlayer.setVolume(Settings.musikLautstaerke * 0.75)),
            new KeyFrame(Duration.seconds(0.5), e -> loopPlayer.setVolume(Settings.musikLautstaerke * 0.5)),
            new KeyFrame(Duration.seconds(0.75), e -> loopPlayer.setVolume(Settings.musikLautstaerke * 0.25)),
            new KeyFrame(Duration.seconds(1), e -> loopPlayer.setVolume(Settings.musikLautstaerke * 0.1)),
            new KeyFrame(Duration.seconds(1.25), e -> {
                loopPlayer.setVolume(0);
                loopPlayer.pause();
                loopPlayer.setVolume(Settings.musikLautstaerke);
            })
        );
        fadeOut.play();
    }

    public static void resumeBgmMusic() {
        Timeline fadeIn = new Timeline(
            new KeyFrame(Duration.seconds(0), e -> loopPlayer.setVolume(0)),
            new KeyFrame(Duration.seconds(0.1), e -> loopPlayer.setVolume(Settings.musikLautstaerke * 0.1)),
            new KeyFrame(Duration.seconds(0.2), e -> loopPlayer.setVolume(Settings.musikLautstaerke * 0.25)),
            new KeyFrame(Duration.seconds(0.4), e -> loopPlayer.setVolume(Settings.musikLautstaerke * 0.5)),
            new KeyFrame(Duration.seconds(0.6), e -> loopPlayer.setVolume(Settings.musikLautstaerke * 0.75)),
            new KeyFrame(Duration.seconds(0.8), e -> loopPlayer.setVolume(Settings.musikLautstaerke))
        );
        fadeIn.play();
        loopPlayer.play();
    }
}
