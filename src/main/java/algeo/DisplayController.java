package algeo;

import java.io.IOException;

import algeo.utils.TraceResult;
import algeo.utils.TraceStep;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;

public class DisplayController {

    @FXML
    private GridPane gridMatrix;

    @FXML
    private Button nextButton;

    @FXML
    private Button prevButton;

    @FXML
    private Button resetButton;

    @FXML
    private Label kondisi;

    private TraceResult trc;
    private int step = -1;

    @FXML
    private void inisialisasi() {
        prevButton.setDisable(true);
        nextButton.setDisable(true);
        kondisi.setText("");
    }

    public void setTraceResult(TraceResult trc) {
        this.trc = trc;
        if (trc == null || trc.size() == 0) {
            clearView();
            return;
        }
        step = 0;
        displayStep(step);
        updateButton();
    }

    private void updateButton() {
        if (trc == null || trc.size() == 0) {
            nextButton.setDisable(true);
            prevButton.setDisable(true);
            return;
        }
        prevButton.setDisable(step <= 0);
        nextButton.setDisable(step >= trc.size() - 1);
    }

    private void clearView() {
        gridMatrix.getChildren().clear();
        kondisi.setText("");
        nextButton.setDisable(true);
        prevButton.setDisable(true);
    }

    @FXML
    private void next(ActionEvent e) {
        if (trc == null) {
            return;
        }
        if (step < trc.size() - 1) {
            step++;
            displayStep(step);
            updateButton();
        }
    }

    @FXML
    private void prev(ActionEvent e) {
        if (trc == null) {
            return;
        }
        if (step > 0) {
            step--;
            displayStep(step);
            updateButton();
        }
    }

    public void switchToMenu(ActionEvent event) throws IOException {
        AppController.switchToMenuStatic(event);
    }

    private void displayStep(int idx) {
        TraceStep s = trc.getSteps().get(idx);
        double temp[][] = s.getKondisi().getData();

        if (temp == null)
            return;

        gridMatrix.getChildren().clear();
        gridMatrix.getRowConstraints().clear();
        gridMatrix.getColumnConstraints().clear();
        gridMatrix.setAlignment(Pos.CENTER);
        gridMatrix.setHgap(0);
        gridMatrix.setVgap(0);

        int baris = temp.length;
        int kolom = baris > 0 ? temp[0].length : 0;

        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                Label label = new Label(String.format("%.3f", temp[i][j]));
                label.setStyle("-fx-border-color: gray; -fx-padding: 0;");
                label.setPrefWidth(50);
                label.setMaxWidth(50);
                label.setMinWidth(50);
                label.setAlignment(Pos.CENTER);
                GridPane.setHalignment(label, HPos.CENTER);
                GridPane.setValignment(label, VPos.CENTER);
                gridMatrix.add(label, j, i);
            }
        }

        Label solusi = new Label(s.getDeskripsi());
        solusi.setStyle("-fx-font-weight: bold; -fx-padding: 10;");
        gridMatrix.add(solusi, 0, baris, Math.max(1, kolom), 1);
        GridPane.setHalignment(solusi, HPos.CENTER);

        kondisi.setText(String.format("Step %d / %d", idx + 1, trc.size()));
    }
}
