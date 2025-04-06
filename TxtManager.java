import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TxtManager {
    private static final String HIGHSCORE_FILE = "src/game.txt";
    private static final String PREFIX = "highscore: ";

    // Highscore aus Datei lesen
    public static int readHighscore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(HIGHSCORE_FILE))) {
            String line = reader.readLine();
            if (line != null && line.startsWith(PREFIX)) {
                String numberPart = line.substring(PREFIX.length()).trim();
                return Integer.parseInt(numberPart);
            }
        } catch (IOException | NumberFormatException e) {
            // Datei fehlt oder Zahl ungültig – dann einfach 0 zurückgeben
        }
        return 0;
    }

    // Highscore in Datei schreiben
    public static void writeHighscore(int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGHSCORE_FILE))) {
            writer.write(PREFIX + score);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Neuer Score wird geprüft und ggf. gespeichert
    public static void updateHighscore(int newScore) {
        int currentHighscore = readHighscore();
        if (newScore > currentHighscore) {
            writeHighscore(newScore);
        }
    }
}
