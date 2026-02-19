package algeo;

import java.io.IOException;

import algeo.modules.Determinan;
import algeo.modules.Matrix;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import algeo.utils.SaveToFile;
import algeo.utils.TraceResult;

public class DetController {

    private Matrix M;

    @FXML
    private GridPane gridMatrix;

    @FXML
    private Button nextButton;

    @FXML
    private Button resetButton;

    @FXML
    private Label kondisi;

    @FXML
    private CheckBox isSave;

    @FXML
    private TextField fileNameSave;

    public void setMatrix(Matrix M) {
        this.M = M;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/style.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");
        alert.showAndWait();
    }

    public void metodeEkspansiKofaktor() {

        // double det = Determinan.determinanEkspansiKofaktor(M.copy());

        // System.out.println("--------------------");
        // System.out.println("Determinan hasil ekspansi kofaktor:");
        // System.out.println(det);
        // System.out.println("--------------------");

        try {
            if (isSave.isSelected()) {
                String fileNameToSave = fileNameSave.getText().trim();
                if (fileNameToSave.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Input kosong", "Isi Terlebih Dahulu");
                    return;
                }
                String Content = SaveToFile.contentBuilder("Determinan: Metode Ekspansi Kofaktor",
                        SaveToFile.matrixToString(M),
                        (Double.toString(Determinan.determinanEkspansiKofaktor(M))));
                SaveToFile.saveToTxt(Content, fileNameToSave);
            }
            switchToDisplayStep(TraceResult.determinanEkspansiKofaktorLangkah(M));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void metodeReduksiBaris() {

        // double det = Determinan.determinanOBE(M.copy());

        // System.out.println("--------------------");
        // System.out.println("Determinan hasil reduksi baris:");
        // System.out.println(det);
        // System.out.println("--------------------");

        try {
            if (isSave.isSelected()) {
                String fileNameToSave = fileNameSave.getText().trim();
                if (fileNameToSave.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Input kosong", "Isi Terlebih Dahulu");
                    return;
                }
                String Content = SaveToFile.contentBuilder("Determinan: Metode Reduksi Baris (OBE)",
                        SaveToFile.matrixToString(M),
                        (Double.toString(Determinan.determinanOBE(M))));
                SaveToFile.saveToTxt(Content, fileNameToSave);
            }
            switchToDisplayStep(TraceResult.determinanOBELangkah(M));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToDisplayStep(TraceResult trc) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DisplayStep.fxml"));
        Parent root = loader.load();

        DisplayController displayStepController = loader.getController();
        displayStepController.setTraceResult(trc);

        Stage stage = new Stage();
        Scene scene = new Scene(root, 750, 750);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMenu(ActionEvent event) throws IOException {
        AppController.switchToMenuStatic(event);
    }
}
