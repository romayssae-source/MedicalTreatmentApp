package util;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.PrintWriter;

public class CsvExporter {

    public static void exportText(Window owner, String defaultFileName, String content) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter en CSV");
        fileChooser.setInitialFileName(defaultFileName);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichier CSV", "*.csv")
        );

        File file = fileChooser.showSaveDialog(owner);

        if (file == null) {
            return;
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.print(content);
            AlertUtil.showInfo("Export réussi", "Le fichier CSV a été exporté avec succès.");
        } catch (Exception e) {
            AlertUtil.showError("Erreur export", e.getMessage());
        }
    }
}
