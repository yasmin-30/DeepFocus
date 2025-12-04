package deepfocus.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import deepfocus.modelos.Pomodoro;
import deepfocus.services.HistoricoService;

import java.time.format.DateTimeFormatter;

public class HistoricoController {

    @FXML
    private TableView<PomodoroTableRow> historicoTable;

    @FXML
    private TableColumn<PomodoroTableRow, String> dataColumn;

    @FXML
    private TableColumn<PomodoroTableRow, String> tempoFocoColumn;

    @FXML
    private TableColumn<PomodoroTableRow, String> tempoDescansoColumn;

    private HistoricoService historicoService;

    @FXML
    public void initialize() {
        historicoService = new HistoricoService();

        // Configurar colunas
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
        tempoFocoColumn.setCellValueFactory(new PropertyValueFactory<>("tempoFoco"));
        tempoDescansoColumn.setCellValueFactory(new PropertyValueFactory<>("tempoDescanso"));

        carregarHistorico();
    }

    private void carregarHistorico() {
        ObservableList<PomodoroTableRow> rows = FXCollections.observableArrayList();

        for (Pomodoro pomodoro : historicoService.listarHistorico()) {
            rows.add(new PomodoroTableRow(pomodoro));
        }

        historicoTable.setItems(rows);
    }

    // Classe interna para representar linha da tabela
    public static class PomodoroTableRow {
        private final String data;
        private final String tempoFoco;
        private final String tempoDescanso;

        public PomodoroTableRow(Pomodoro pomodoro) {
            // Formatar data
            if (pomodoro.getInicio() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                this.data = pomodoro.getInicio().format(formatter);
            } else {
                this.data = "-";
            }

            // Converter duração de segundos para HH:MM:SS
            this.tempoFoco = formatarTempo(pomodoro.getDuracaoFoco());
            this.tempoDescanso = formatarTempo(pomodoro.getDuracaoDescanso());
        }

        private String formatarTempo(int totalSegundos) {
            int horas = totalSegundos / 3600;
            int minutos = (totalSegundos % 3600) / 60;
            int segundos = totalSegundos % 60;
            return String.format("%02d:%02d:%02d", horas, minutos, segundos);
        }

        // Getters para PropertyValueFactory
        public String getData() {
            return data;
        }

        public String getTempoFoco() {
            return tempoFoco;
        }

        public String getTempoDescanso() {
            return tempoDescanso;
        }
    }
}
