package algeo;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;

public class AppController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private String mode;

    public void setMode(String mode) {
        this.mode = mode;
        System.out.println("Mode diterima: " + mode);
    }

    public void switchToInputMtrx(ActionEvent event) throws IOException {
        // Button btn = (Button) event.getSource();
        // String mode = (String) btn.getUserData();

        if ("SPL".equals(mode) || "Det".equals(mode) || "Inv".equals(mode)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InputMtrx.fxml"));
            Parent root = loader.load();

            MatrixJavaFX controller = loader.getController();
            controller.setMode(mode);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else if ("Itp".equals(mode)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InputMtrxItp.fxml"));
            Parent root = loader.load();

            MatrixJavaFX controller = loader.getController();
            controller.setMode(mode);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else if ("Reg".equals(mode)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InputReg.fxml"));
            Parent root = loader.load();

            MatrixJavaFX controller = loader.getController();
            controller.setMode(mode);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void switchToOpsiInput(ActionEvent event) throws IOException {
        Button btn = (Button) event.getSource();
        String mode = (String) btn.getUserData();

        if ("SPL".equals(mode) || "Det".equals(mode) || "Inv".equals(mode) || "Itp".equals(mode)
                || "Reg".equals(mode)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/OpsiInput.fxml"));
            Parent root = loader.load();

            AppController controller = loader.getController();
            controller.setMode(mode);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void switchToInputFile(ActionEvent event) throws IOException {
        // Button btn = (Button) event.getSource();
        // String mode = (String) btn.getUserData();

        if ("SPL".equals(mode) || "Det".equals(mode) || "Inv".equals(mode) ||
                "Itp".equals(mode)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InputFileName.fxml"));
            Parent root = loader.load();

            FileController controller = loader.getController();
            controller.setMode(mode);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else if ("Reg".equals(mode)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InputFileNameReg.fxml"));
            Parent root = loader.load();

            FileController controller = loader.getController();
            controller.setMode(mode);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void switchToSPL(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/SPL.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMenu(ActionEvent event) throws IOException {
        AppController.switchToMenuStatic(event);
    }

    public static void switchToMenuStatic(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(AppController.class.getResource("/App.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}