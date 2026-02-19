package algeo;

import java.io.IOException;

import algeo.modules.Matrix;
import algeo.modules.RegresiPolinomGanda;
import algeo.utils.MatrixParser;
import algeo.utils.SaveToFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegController {

    private Matrix baseM;

    private int derajatReg;

    private TextField[][] matrixFields;

    @FXML
    private GridPane matrix;

    @FXML
    private Button submit;

    @FXML
    private Label persamaan;

    @FXML
    private GridPane inputX;

    @FXML
    private Label hasilYLabel;

    @FXML
    private Button isSave;

    @FXML
    private TextField fileNameSave;

    public void setMatrix(Matrix M) {
        this.baseM = M;
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

    public void initDisplay(Matrix input, int derajat) {
        this.baseM = input;
        this.derajatReg = derajat;
        persamaan.setText(RegresiPolinomGanda.bentukPersamaan(
                RegresiPolinomGanda.regresiPolinom(baseM.ambilMatrixKoef(), baseM.ambilMatrixKons(), derajatReg),
                baseM.ambilMatrixKoef().getKolom(), derajatReg));
        displayMatrixReg(input);
    }

    public void metodeReg() {

        // Matrix hasilInv = Invers.inversAugment(M.copy());

        // System.out.println("--------------------");
        // System.out.println("Invers hasil metode Augment:");
        // hasilInv.print();
        // System.out.println("--------------------");

        try {
            switchToDisplayReg(
                    RegresiPolinomGanda.regresiPolinom(baseM.ambilMatrixKoef(), baseM.ambilMatrixKons(), derajatReg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFileReg() {
        String fileNameToSave = fileNameSave.getText().trim();
        if (fileNameToSave.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input kosong", "Isi Terlebih Dahulu");
            return;
        }
        String Content = SaveToFile.contentBuilder("Regresi Polinomial Berganda",
                SaveToFile.matrixToString(baseM),
                RegresiPolinomGanda.bentukPersamaan(RegresiPolinomGanda.regresiPolinom(baseM.ambilMatrixKoef(),
                        baseM.ambilMatrixKons(), derajatReg), baseM.ambilMatrixKoef().getKolom(), derajatReg));
        SaveToFile.saveToTxt(Content, fileNameToSave);
    }

    public void switchToDisplayReg(Matrix M) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DisplayReg.fxml"));
        Parent root = loader.load();

        RegController controller = loader.getController();
        controller.initDisplay(baseM, derajatReg);

        Stage stage = new Stage();
        Scene scene = new Scene(root, 750, 750);
        stage.setScene(scene);
        stage.show();
    }

    public void displayMatrixReg(Matrix M) {
        double[][] temp = new double[baseM.getBaris()][baseM.getKolom()];
        matrix.getChildren().clear();
        matrix.setAlignment(Pos.CENTER);
        matrix.setHgap(0);
        matrix.setVgap(0);
        matrix.getColumnConstraints().clear();
        matrix.getRowConstraints().clear();

        Matrix Mtrx = baseM.copy();
        temp = Mtrx.getData();

        for (int i = 0; i < M.getBaris(); i++) {
            for (int j = 0; j < M.getKolom(); j++) {
                Label label = new Label(String.format("%.3f", temp[i][j]));
                label.setStyle("-fx-border-color: gray; -fx-padding: 0;");
                matrix.add(label, j, i);
                label.setPrefWidth(50);
                label.setMaxWidth(50);
                label.setMinWidth(50);
                label.setAlignment(Pos.CENTER);
                GridPane.setHalignment(label, HPos.CENTER);
                GridPane.setValignment(label, VPos.CENTER);
            }
        }
        generateMatrixInputReg(1, baseM.getKolom() - 1);
    }

    public void cariY() {
        double[] x = new double[matrixFields[0].length];
        for (int i = 0; i < matrixFields[0].length; i++) {
            if (matrixFields[0][i].getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input kosong", "Isi Terlebih Dahulu");
                return;
            }
            x[i] = MatrixParser.parseFraction(matrixFields[0][i].getText().trim());
        }
        System.out.println("--------------------------------------");
        RegresiPolinomGanda.regresiPolinom(baseM.ambilMatrixKoef(), baseM.ambilMatrixKons(), derajatReg).print();
        System.out.println("--------------------------------------");
        double y = RegresiPolinomGanda.hitungY(
                RegresiPolinomGanda.regresiPolinom(baseM.ambilMatrixKoef(), baseM.ambilMatrixKons(), derajatReg), x,
                derajatReg);

        hasilYLabel.setText(" Y = " + String.format("%.3f", y));
        hasilYLabel.setStyle("-fx-font-weight: bold; -fx-padding: 10;");
    }

    public void generateMatrixInputReg(int baris, int kolom) {
        inputX.getChildren().clear();
        inputX.setAlignment(Pos.CENTER);
        matrixFields = new TextField[baris][kolom];

        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                TextField elemen = new TextField("");
                elemen.setPrefWidth(50);
                elemen.setMaxWidth(50);
                elemen.setMinWidth(50);
                elemen.setAlignment(Pos.CENTER);
                elemen.setStyle(
                        "-fx-border-color: black; " +
                                "-fx-border-width: 1; " +
                                "-fx-background-radius: 0; " +
                                "-fx-border-radius: 0;");
                inputX.add(elemen, j, i);
                GridPane.setHalignment(elemen, HPos.CENTER);
                GridPane.setValignment(elemen, VPos.CENTER);

                matrixFields[i][j] = elemen;
            }
        }
    }

    public void switchToMenu(ActionEvent event) throws IOException {
        AppController.switchToMenuStatic(event);
    }
}
