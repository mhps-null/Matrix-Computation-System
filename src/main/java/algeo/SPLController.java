package algeo;

import java.io.IOException;

import algeo.modules.Determinan;
import algeo.modules.Matrix;
import algeo.modules.SPL;
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

import algeo.utils.TraceResult;
import algeo.utils.CekUkuranMtrx;
import algeo.utils.SaveToFile;

public class SPLController {

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

    public void eliminasiGauss() {
        // Matrix hasilGauss = M.copy();
        // hasilGauss = SPL.splGauss(hasilGauss);

        // System.out.println("--------------------");
        // System.out.println("Matrix hasil gauss:");
        // hasilGauss.print();
        // System.out.println("--------------------");
        // System.out.println(hasilGauss.getBaris());
        // System.out.println(hasilGauss.getKolom());

        // System.out.println(SPL.jenisHasil(hasilGauss));

        try {
            if (isSave.isSelected()) {
                String fileNameToSave = fileNameSave.getText().trim();
                if (fileNameToSave.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Input kosong", "Isi Terlebih Dahulu");
                    return;
                }
                String Content = SaveToFile.contentBuilder("SPL: Eliminasi Gauss", SaveToFile.matrixToString(M),
                        SPL.jenisHasil(SPL.splGauss(M)));
                SaveToFile.saveToTxt(Content, fileNameToSave);
            }
            switchToDisplayStep(TraceResult.splGaussLangkah(M));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void eliminasiGaussJordan() {
        // Matrix hasilGJ = M.copy();
        // hasilGJ = SPL.splGaussJordan(hasilGJ);

        // System.out.println("--------------------");
        // System.out.println("Matrix hasil gauss jordan:");
        // hasilGJ.print();
        // System.out.println("--------------------");
        // System.out.println(hasilGJ.getBaris());
        // System.out.println(hasilGJ.getKolom());

        // System.out.println(SPL.jenisHasil(hasilGJ));

        try {
            if (isSave.isSelected()) {
                String fileNameToSave = fileNameSave.getText().trim();
                if (fileNameToSave.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Input kosong", "Isi Terlebih Dahulu");
                    return;
                }
                String Content = SaveToFile.contentBuilder("SPL: Eliminasi Gauss-Jordan", SaveToFile.matrixToString(M),
                        SPL.jenisHasil(SPL.splGaussJordan(M)));
                SaveToFile.saveToTxt(Content, fileNameToSave);
            }
            switchToDisplayStep(TraceResult.splGaussJordanLangkah(M));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void kaidahCramer() {
        // Matrix hasilCr = SPL.splCramer(M.copy());

        // if (hasilCr != null) {
        // System.out.println("--------------------");
        // System.out.println("Matrix hasil cramer:");
        // hasilCr.print();
        // System.out.println("--------------------");
        // System.out.println(hasilCr.getBaris());
        // System.out.println(hasilCr.getKolom());
        // }

        try {
            if (isSave.isSelected()) {
                String fileNameToSave = fileNameSave.getText().trim();
                if (fileNameToSave.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Input kosong", "Isi Terlebih Dahulu");
                    return;
                }
                if (!CekUkuranMtrx.isPersegiMtrx(M.ambilMatrixKoef())) {
                    String Content = SaveToFile.contentBuilder("SPL: Kaidah Cramer", SaveToFile.matrixToString(M),
                            "Matriks A tidak persegi! tidak bisa cramer");
                    SaveToFile.saveToTxt(Content, fileNameToSave);
                } else if (Determinan.determinanOBE(M.ambilMatrixKoef()) == 0) {
                    String Content = SaveToFile.contentBuilder("SPL: Kaidah Cramer", SaveToFile.matrixToString(M),
                            "Determinan = 0; matriks singular, tidak dapat diinvers");
                    SaveToFile.saveToTxt(Content, fileNameToSave);
                } else {
                    String Content = SaveToFile.contentBuilder("SPL: Kaidah Cramer", SaveToFile.matrixToString(M),
                            SPL.jenisHasilCrInv(SPL.splCramer(M), "cramer"));
                    SaveToFile.saveToTxt(Content, fileNameToSave);
                }
            }
            switchToDisplayStep(TraceResult.splCramerLangkah(M));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void metodeMatriksBalikan() {
        // Matrix hasilInv = SPL.splInvers(M.copy());

        // if (hasilInv != null) {
        // System.out.println("--------------------");
        // System.out.println("Matrix hasil metode matriks balikan:");
        // hasilInv.print();
        // System.out.println("--------------------");
        // System.out.println(hasilInv.getBaris());
        // System.out.println(hasilInv.getKolom());
        // }

        try {
            if (isSave.isSelected()) {
                String fileNameToSave = fileNameSave.getText().trim();
                if (fileNameToSave.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Input kosong", "Isi Terlebih Dahulu");
                    return;
                }
                if (!CekUkuranMtrx.isPersegiMtrx(M.ambilMatrixKoef())) {
                    String Content = SaveToFile.contentBuilder("SPL: Metode Matriks Balikan",
                            SaveToFile.matrixToString(M),
                            "Matriks A tidak persegi! tidak bisa cramer");
                    SaveToFile.saveToTxt(Content, fileNameToSave);
                } else if (Determinan.determinanOBE(M.ambilMatrixKoef()) == 0) {
                    String Content = SaveToFile.contentBuilder("SPL: Metode Matriks Balikan",
                            SaveToFile.matrixToString(M),
                            "Determinan = 0; matriks singular, tidak dapat diinvers");
                    SaveToFile.saveToTxt(Content, fileNameToSave);
                } else {
                    String Content = SaveToFile.contentBuilder("SPL: Metode Matriks Balikan",
                            SaveToFile.matrixToString(M),
                            SPL.jenisHasilCrInv(SPL.splInvers(M), "inverse"));
                    SaveToFile.saveToTxt(Content, fileNameToSave);
                }
            }
            switchToDisplayStep(TraceResult.splInversLangkah(M));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // public void switchToDisplayMatrix(Matrix M, String jenis) throws IOException
    // {
    // FXMLLoader loader = new
    // FXMLLoader(getClass().getResource("/DisplayStep.fxml"));
    // root = loader.load();
    // SPLController displayMatrixController = loader.getController();

    // displayMatrixController.displayMatrix(M, jenis);

    // Stage stage = new Stage();
    // Scene scene = new Scene(root);
    // stage.setScene(scene);
    // stage.show();
    // }

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

    // public void displayMatrix(Matrix M, String jenis) {
    // if (jenis.equals("gauss") || jenis.equals("gauss-jordan")) {

    // double[][] temp = new double[M.getBaris()][M.getKolom()];
    // gridMatrix.getChildren().clear();
    // gridMatrix.setAlignment(Pos.CENTER);
    // gridMatrix.setHgap(0);
    // gridMatrix.setVgap(0);
    // gridMatrix.getColumnConstraints().clear();
    // gridMatrix.getRowConstraints().clear();

    // Matrix Mtrx = M.copy();
    // temp = Mtrx.getData();

    // for (int i = 0; i < M.getBaris(); i++) {
    // for (int j = 0; j < M.getKolom(); j++) {
    // Label label = new Label(String.format("%.3f", temp[i][j]));
    // label.setStyle("-fx-border-color: gray; -fx-padding: 0;");
    // gridMatrix.add(label, j, i);
    // label.setPrefWidth(50);
    // label.setMaxWidth(50);
    // label.setMinWidth(50);
    // label.setAlignment(Pos.CENTER);
    // GridPane.setHalignment(label, HPos.CENTER);
    // GridPane.setValignment(label, VPos.CENTER);
    // }
    // }

    // Label solusi = new Label(SPL.jenisHasil(Mtrx));
    // solusi.setStyle("-fx-font-weight: bold; -fx-padding: 10;");
    // gridMatrix.add(solusi, 0, M.getBaris(), M.getKolom(), 1);
    // GridPane.setHalignment(solusi, HPos.CENTER);
    // } else if (jenis.equals("cramer") || jenis.equals("inverse")) {

    // double[][] temp = new double[M.getBaris()][M.getKolom()];
    // gridMatrix.getChildren().clear();
    // gridMatrix.setAlignment(Pos.CENTER);
    // gridMatrix.setHgap(0);
    // gridMatrix.setVgap(0);
    // gridMatrix.getColumnConstraints().clear();
    // gridMatrix.getRowConstraints().clear();

    // Matrix Mtrx = M.copy();
    // temp = Mtrx.getData();

    // for (int i = 0; i < M.getBaris(); i++) {
    // for (int j = 0; j < M.getKolom(); j++) {
    // Label label = new Label(String.format("%.3f", temp[i][j]));
    // label.setStyle("-fx-border-color: gray; -fx-padding: 0;");
    // gridMatrix.add(label, j, i);
    // label.setPrefWidth(50);
    // label.setMaxWidth(50);
    // label.setMinWidth(50);
    // label.setAlignment(Pos.CENTER);
    // GridPane.setHalignment(label, HPos.CENTER);
    // GridPane.setValignment(label, VPos.CENTER);
    // }
    // }

    // Label solusi = new Label(SPL.jenisHasilCrInv(M, jenis));
    // solusi.setStyle("-fx-font-weight: bold; -fx-padding: 10;");
    // gridMatrix.add(solusi, 0, M.getBaris(), M.getKolom(), 1);
    // GridPane.setHalignment(solusi, HPos.CENTER);
    // }
    // }

    public void switchToMenu(ActionEvent event) throws IOException {
        AppController.switchToMenuStatic(event);
    }
}