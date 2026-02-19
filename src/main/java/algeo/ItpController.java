package algeo;

import java.io.IOException;

import algeo.modules.Interpolasi;
import algeo.modules.Matrix;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ItpController {

    private Matrix baseM;

    @FXML
    private GridPane matrix;

    @FXML
    private Button submit;

    @FXML
    private Label persamaan;

    @FXML
    private TextField inputX;

    @FXML
    private Label hasilYLabel;

    @FXML
    private CheckBox isSave;

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

    public void initDisplay(Matrix input, Matrix hasil, String jenis) {
        if (jenis.equals("polinomial")) {
            this.baseM = input;
            persamaan.setText(Interpolasi.bentukPersamaan(hasil));
            displayMatrixItp(input, jenis);
        } else if (jenis.equals("bezier")) {
            this.baseM = hasil;
            persamaan.setText("");
            displayMatrixItp(hasil, jenis);
            inputX.setDisable(true);
            inputX.setVisible(false);
            submit.setDisable(true);
            submit.setVisible(false);
            hasilYLabel.setDisable(true);
            hasilYLabel.setVisible(false);
        } else if (jenis.equals("kolom tidak 2")) {
            persamaan.setText("KOLOM HARUS 2");
            displayMatrixItp(hasil, jenis);
            inputX.setDisable(true);
            inputX.setVisible(false);
            submit.setDisable(true);
            submit.setVisible(false);
        }
    }

    public void metodePolinomial() {

        // Matrix hasilInv = Invers.inversAugment(M.copy());

        // System.out.println("--------------------");
        // System.out.println("Invers hasil metode Augment:");
        // hasilInv.print();
        // System.out.println("--------------------");

        try {
            if (isSave.isSelected()) {
                String fileNameToSave = fileNameSave.getText().trim();
                if (fileNameToSave.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Input kosong", "Isi Terlebih Dahulu");
                    return;
                }
                String Content = SaveToFile.contentBuilderItp("Interpolasi: Metode Polinomial",
                        SaveToFile.matrixToString(baseM), Interpolasi.cariDomain(baseM),
                        Interpolasi.bentukPersamaan(Interpolasi.interpolasiPolinomial(baseM)));
                SaveToFile.saveToTxt(Content, fileNameToSave);
            }
            if (baseM.getKolom() != 2) {
                switchToDisplayItp(null, "kolom tidak 2");
            } else {
                switchToDisplayItp(Interpolasi.interpolasiPolinomial(baseM), "polinomial");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void metodeBezier() {

        // Matrix hasilInv = Invers.inversAdjoin(M.copy());

        // System.out.println("--------------------");
        // System.out.println("Invers hasil metode Adjoin:");
        // hasilInv.print();
        // System.out.println("--------------------");

        try {
            if (isSave.isSelected()) {
                String fileNameToSave = fileNameSave.getText().trim();
                if (fileNameToSave.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Input kosong", "Isi Terlebih Dahulu");
                    return;
                }
                String Content = SaveToFile.contentBuilderItpBezier("Interpolasi: Metode splina Bezier kubik",
                        SaveToFile.matrixToString(Interpolasi.interpolasiSplinaBezier(baseM)));
                SaveToFile.saveToTxt(Content, fileNameToSave);
            }
            if (baseM.getKolom() != 2) {
                switchToDisplayItp(null, "kolom tidak 2");
            } else {
                switchToDisplayItp(Interpolasi.interpolasiSplinaBezier(baseM), "bezier");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToDisplayItp(Matrix M, String jenis) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DisplayItp.fxml"));
        Parent root = loader.load();

        ItpController controller = loader.getController();
        controller.initDisplay(baseM, M, jenis);

        Stage stage = new Stage();
        Scene scene = new Scene(root, 750, 750);
        stage.setScene(scene);
        stage.show();
    }

    public void displayMatrixItp(Matrix M, String jenis) {
        if (jenis.equals("polinomial")) {

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
        } else if (jenis.equals("bezier")) {
            double[][] temp = new double[M.getBaris()][M.getKolom()];
            matrix.getChildren().clear();
            matrix.setAlignment(Pos.CENTER);
            matrix.setHgap(0);
            matrix.setVgap(0);
            matrix.getColumnConstraints().clear();
            matrix.getRowConstraints().clear();

            Label judul = new Label("Titik Kontrol");
            judul.setStyle("-fx-font-weight: bold; -fx-font-size: 36px");
            judul.setPrefWidth(450);
            judul.setPrefHeight(60);
            judul.setAlignment(Pos.CENTER);
            matrix.add(judul, 0, 0, 3, 1);
            GridPane.setHalignment(judul, HPos.CENTER);
            GridPane.setValignment(judul, VPos.CENTER);

            Matrix Mtrx = M.copy();
            temp = Mtrx.getData();

            for (int i = 0; i <= M.getBaris(); i++) {
                for (int j = 0; j < 3; j++) {
                    Label label;

                    if (i == 0) {
                        if (j == 0)
                            label = new Label("Segmen");
                        else if (j == 1)
                            label = new Label("Awal");
                        else
                            label = new Label("Akhir");
                    } else if (j == 0) {
                        label = new Label(String.valueOf(i - 1));
                    } else {
                        int idx = i - 1;
                        if (j == 1 && temp[idx].length >= 2) {
                            label = new Label(String.format("(%.3f, %.3f)", temp[idx][0], temp[idx][1]));
                        } else if (j == 2 && temp[idx].length >= 4) {
                            label = new Label(String.format("(%.3f, %.3f)", temp[idx][2], temp[idx][3]));
                        } else {
                            label = new Label("-");
                        }
                    }

                    label.setStyle("-fx-border-color: gray; -fx-padding: 5;");
                    label.setPrefWidth(150);
                    label.setPrefHeight(50);
                    label.setAlignment(Pos.CENTER);

                    matrix.add(label, j, i + 1);
                    GridPane.setHalignment(label, HPos.CENTER);
                    GridPane.setValignment(label, VPos.CENTER);
                }
            }
        }
    }

    public void cariY() {
        String xs = inputX.getText().trim();
        if (xs.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input kosong", "Isi Terlebih Dahulu");
            return;
        }
        double x = MatrixParser.parseFraction(xs);
        double y = Interpolasi.evaluasiPolinomial(Interpolasi.interpolasiPolinomial(baseM), x);

        hasilYLabel.setText("X = " + String.format("%.3f", x) + " Y = " + String.format("%.3f", y));
        hasilYLabel.setStyle("-fx-font-weight: bold; -fx-padding: 10;");
    }

    public void switchToMenu(ActionEvent event) throws IOException {
        AppController.switchToMenuStatic(event);
    }
}
