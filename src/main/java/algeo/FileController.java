package algeo;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import algeo.modules.Matrix;
import algeo.utils.MatrixParser;

public class FileController {

    @FXML
    private TextField fileName;

    private String mode;

    public void setMode(String mode) {
        this.mode = mode;
        System.out.println("Mode diterima: " + mode);
    }

    public void switchToMenu(ActionEvent event) throws IOException {
        AppController.switchToMenuStatic(event);
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

    public void saveMatrix() {
        String path = (System.getProperty("user.dir") + "/test/input/");
        String namaFile = fileName.getText().trim();
        if (namaFile.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input kosong", "Isi Terlebih Dahulu");
            return;
        }

        Matrix M = MatrixParser.parseMatrixFromFileTxt(path + namaFile + ".txt");
        if (M == null) {
            showAlert(Alert.AlertType.ERROR, "Data Kosong", "File tidak ada atau kosong!");
            return;
        }
        int baris = M.getBaris();
        int kolom = M.getKolom();

        System.out.println("--------------------");
        System.out.println("Matrix tersimpan:");
        M.print();
        System.out.println(baris);
        System.out.println(kolom);
        System.out.println("--------------------");

        if (mode.equals("SPL")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/SPL.fxml"));
                Parent root = loader.load();

                SPLController controller = loader.getController();
                controller.setMatrix(M);

                Scene scene = new Scene(root);
                Stage stage = (Stage) fileName.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (mode.equals("Det")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Det.fxml"));
                Parent root = loader.load();

                DetController controller = loader.getController();
                controller.setMatrix(M);

                Scene scene = new Scene(root);
                Stage stage = (Stage) fileName.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (mode.equals("Inv")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Inv.fxml"));
                Parent root = loader.load();

                InvController controller = loader.getController();
                controller.setMatrix(M);

                Scene scene = new Scene(root);
                Stage stage = (Stage) fileName.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (mode.equals("Itp")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Itp.fxml"));
                Parent root = loader.load();

                ItpController controller = loader.getController();
                controller.setMatrix(M);

                Scene scene = new Scene(root);
                Stage stage = (Stage) fileName.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveMatrixReg() {
        String path = (System.getProperty("user.dir") + "/test/input/");
        String namaFile = fileName.getText().trim();
        if (namaFile.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input kosong", "Isi Terlebih Dahulu");
            return;
        }
        Object[] parsed = MatrixParser.matrixParserRegresi(path + namaFile + ".txt");
        if (parsed == null) {
            showAlert(Alert.AlertType.ERROR, "Data Kosong", "File tidak ada atau kosong!");
            return;
        }
        Matrix data = (Matrix) parsed[0];
        System.out.println("___________________________________");
        data.print();
        System.out.println("___________________________________");
        int degree = (int) parsed[1];

        int baris = data.getBaris();
        int kolom = data.getKolom();

        System.out.println("--------------------");
        System.out.println("Matrix tersimpan:");
        data.print();
        System.out.println(baris);
        System.out.println(kolom);
        System.out.println("--------------------");

        if (mode.equals("Reg")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/DisplayReg.fxml"));
                Parent root = loader.load();

                RegController controller = loader.getController();
                controller.initDisplay(data, degree);

                Scene scene = new Scene(root);
                Stage stage = (Stage) fileName.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
