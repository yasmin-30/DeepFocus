package deepfocus.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    @FXML
    private StackPane contentArea;

    @FXML
    private VBox sidebar;

    private List<Node> homeContent;

    @FXML
    public void initialize() {
        // Guardar conteúdo inicial da home
        homeContent = new ArrayList<>(contentArea.getChildren());
    }

    @FXML
    private void toggleMenu() {
        sidebar.setVisible(!sidebar.isVisible());
        sidebar.setManaged(!sidebar.isManaged());
    }

    @FXML
    private void showHome() {
        contentArea.getChildren().clear();
        contentArea.getChildren().addAll(homeContent);
        updateMenuSelection("Home");
    }

    @FXML
    private void showCronometro() {
        loadView("/resources/view/PomodoroView.fxml");
        updateMenuSelection("Cronômetro");
    }

    @FXML
    private void showLembretes() {
        loadView("/resources/view/TarefasView.fxml");
        updateMenuSelection("Lembretes");
    }

    @FXML
    private void showNotas() {
        loadView("/resources/view/NotasView.fxml");
        updateMenuSelection("Notas");
    }

    @FXML
    private void showHistorico() {
        loadView("/resources/view/HistoricoView.fxml");
        updateMenuSelection("Histórico");
    }

    private void loadView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateMenuSelection(String selectedMenu) {
        sidebar.getChildren().forEach(node -> {
            if (node instanceof javafx.scene.control.Button) {
                javafx.scene.control.Button btn = (javafx.scene.control.Button) node;
                btn.getStyleClass().remove("menu-button-selected");
                if (btn.getText().equals(selectedMenu)) {
                    btn.getStyleClass().add("menu-button-selected");
                }
            }
        });
    }
}
