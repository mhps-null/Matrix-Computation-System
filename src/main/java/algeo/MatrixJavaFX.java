package algeo;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import algeo.modules.Matrix;
import algeo.utils.MatrixParser;

public class MatrixJavaFX {

    @FXML
    private TextField barisField;
    @FXML
    private TextField kolomField;
    @FXML
    private GridPane matrix;

    @FXML
    private TextField jumlahTitik;
    @FXML
    private TextField jumlahVariabel;

    private TextField[][] matrixFields;

    private Matrix M;

    private String mode;

    private Stage stage;
    private Scene scene;
    private Parent root;

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

    public void setMode(String mode) {
        this.mode = mode;
        System.out.println("Mode diterima: " + mode);
    }

    public void generateMatrix(int baris, int kolom) {
        matrix.getChildren().clear();
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
                matrix.add(elemen, j, i);
                GridPane.setHalignment(elemen, HPos.CENTER);
                GridPane.setValignment(elemen, VPos.CENTER);

                matrixFields[i][j] = elemen;
            }
        }

        if (mode.equals("Reg")) {
            Label derajatRegLabel = new Label();
            derajatRegLabel.setText("Derajat Regresi");
            GridPane.setMargin(derajatRegLabel, new Insets(30, 0, 0, 0));
            TextField derajatReg = new TextField();
            derajatReg.setPrefWidth(50);
            derajatReg.setMaxWidth(50);
            derajatReg.setMinWidth(50);
            derajatReg.setAlignment(Pos.CENTER);
            derajatReg.setStyle(
                    "-fx-border-color: black; " +
                            "-fx-border-width: 1; " +
                            "-fx-background-radius: 0; " +
                            "-fx-border-radius: 0;");
            GridPane.setMargin(derajatReg, new Insets(0, 0, 15, 0));
            matrix.add(derajatRegLabel, 0, baris, kolom, 1);
            matrix.add(derajatReg, 0, baris + 1, kolom, 1);
            GridPane.setHalignment(derajatReg, HPos.CENTER);
            GridPane.setHalignment(derajatRegLabel, HPos.CENTER);

            Button button = new Button("Submit");
            matrix.add(button, 0, baris + 2, kolom, 1);
            GridPane.setHalignment(button, HPos.CENTER);

            button.setOnAction(event -> {
                if (derajatReg.getText().isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Input kosong", "Isi Terlebih Dahulu");
                    return;
                }
                saveMatrixReg(Integer.parseInt(derajatReg.getText().trim()));
            });
        } else {
            Button button = new Button("Submit");
            matrix.add(button, 0, baris, kolom, 1);
            GridPane.setHalignment(button, HPos.CENTER);

            button.setOnAction(event -> {
                saveMatrix();
            });
        }
    }

    private void saveMatrixReg(int derajat) {
        int baris = matrixFields.length;
        int kolom = matrixFields[0].length;

        M = new Matrix(baris, kolom);

        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                String text = matrixFields[i][j].getText().trim();
                if (text.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Input kosong", "Isi Terlebih Dahulu");
                    return;
                }
                double value = 0;
                try {
                    value = MatrixParser.parseFraction(text);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "ERROR", "Input angka dengan benar");
                    return;
                }
                M.setElmt(i, j, value);
            }
        }

        System.out.println("--------------------");
        System.out.println("Matrix tersimpan:");
        M.print();
        System.out.println("--------------------");

        if (mode.equals("Reg")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/DisplayReg.fxml"));
                Parent root = loader.load();

                RegController controller = loader.getController();
                controller.initDisplay(M, derajat);

                Scene scene = new Scene(root);
                Stage stage = (Stage) matrix.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveMatrix() {
        int baris = matrixFields.length;
        int kolom = matrixFields[0].length;

        M = new Matrix(baris, kolom);

        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                String text = matrixFields[i][j].getText().trim();
                if (text.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Input kosong", "Isi terlebih dahulu");
                    return;
                }
                double value = 0;
                try {
                    value = MatrixParser.parseFraction(text);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "ERROR", "Input angka dengan benar");
                    return;
                }
                M.setElmt(i, j, value);
            }
        }

        System.out.println("--------------------");
        System.out.println("Matrix tersimpan:");
        M.print();
        System.out.println("--------------------");

        if (mode.equals("SPL")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/SPL.fxml"));
                Parent root = loader.load();

                SPLController controller = loader.getController();
                controller.setMatrix(M);

                Scene scene = new Scene(root);
                Stage stage = (Stage) matrix.getScene().getWindow();
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
                Stage stage = (Stage) matrix.getScene().getWindow();
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
                Stage stage = (Stage) matrix.getScene().getWindow();
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
                Stage stage = (Stage) matrix.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void genMtrx(ActionEvent event) throws IOException {

        if (barisField.getText().isEmpty() || kolomField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input kosong", "Isi Terlebih Dahulu");
            return;
        }

        double barisD;
        double kolomD;
        try {
            barisD = MatrixParser.parseFraction(barisField.getText().trim());
            kolomD = MatrixParser.parseFraction(kolomField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "ERROR", "Input angka dengan benar");
            return;
        }

        int baris = (int) Math.round(barisD);
        int kolom = (int) Math.round(kolomD);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GenMtrx.fxml"));
        root = loader.load();
        MatrixJavaFX genMtrxController = loader.getController();
        genMtrxController.setMode(this.mode);
        genMtrxController.generateMatrix(baris, kolom);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void genMtrxItp(ActionEvent event) throws IOException {

        if (barisField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input kosong", "Isi Terlebih Dahulu");
            return;
        }

        double barisD;
        try {
            barisD = MatrixParser.parseFraction(barisField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "ERROR", "Input angka dengan benar");
            return;
        }

        int baris = (int) Math.round(barisD);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GenMtrx.fxml"));
        root = loader.load();
        MatrixJavaFX genMtrxController = loader.getController();
        genMtrxController.setMode(this.mode);
        genMtrxController.generateMatrix(baris, 2);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void genMtrxReg(ActionEvent event) throws IOException {

        if (jumlahTitik.getText().isEmpty() || jumlahVariabel.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input kosong", "Isi Terlebih Dahulu");
            return;
        }

        double barisD;
        double kolomD;
        try {
            barisD = MatrixParser.parseFraction(jumlahTitik.getText().trim());
            kolomD = MatrixParser.parseFraction(jumlahVariabel.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "ERROR", "Input angka dengan benar");
            return;
        }

        int baris = (int) Math.round(barisD);
        int kolom = (int) Math.round(kolomD);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GenMtrx.fxml"));
        root = loader.load();
        MatrixJavaFX genMtrxController = loader.getController();
        genMtrxController.setMode(this.mode);
        genMtrxController.generateMatrix(baris, kolom + 1);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // public void genMtrxItp(ActionEvent event) throws IOException {

    // int baris = Integer.parseInt(barisField.getText());

    // FXMLLoader loader = new
    // FXMLLoader(getClass().getResource("/DisplayItp.fxml"));
    // root = loader.load();
    // ItpController controller = loader.getController();
    // controller.generateMatrixItp(baris, 2);

    // stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    // scene = new Scene(root);
    // stage.setScene(scene);
    // stage.show();
    // }

    public void switchToMenu(ActionEvent event) throws IOException {
        AppController.switchToMenuStatic(event);
    }
}
