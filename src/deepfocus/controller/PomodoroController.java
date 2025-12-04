package deepfocus.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import deepfocus.modelos.Pomodoro;
import deepfocus.services.HistoricoService;
import deepfocus.services.PomodoroService;
import deepfocus.utils.TimerUtil;

public class PomodoroController {

    @FXML
    private Label timerLabel;

    @FXML
    private TextField tempoFocoField;

    @FXML
    private TextField tempoDescansoField;

    @FXML
    private Button startButton;

    @FXML
    private Button resetButton;

    private PomodoroService pomodoroService;
    private HistoricoService historicoService;
    private Pomodoro pomodoroAtual;

    @FXML
    public void initialize() {
        historicoService = new HistoricoService();
    }

    @FXML
    private void handleStart() {
        if (pomodoroService == null) {
            // Primeira vez - criar novo Pomodoro e iniciar
            int tempoFocoSegundos = parseTime(tempoFocoField.getText());
            int tempoDescansoSegundos = parseTime(tempoDescansoField.getText());

            if (tempoFocoSegundos <= 0) {
                System.out.println("Erro: Tempo de foco inválido!");
                return;
            }

            // Criar modelo Pomodoro
            pomodoroAtual = new Pomodoro();
            pomodoroAtual.setDuracaoFoco(tempoFocoSegundos);
            pomodoroAtual.setDuracaoDescanso(tempoDescansoSegundos);

            // Criar TimerUtil e PomodoroService
            TimerUtil timer = new TimerUtil();
            pomodoroService = new PomodoroService(pomodoroAtual, timer, historicoService);

            // Configurar listeners
            pomodoroService.setTickListener(remainingSeconds -> {
                timerLabel.setText(formatTime(remainingSeconds));
            });

            pomodoroService.setCicloChangeListener((novoStatus, proximaDuracao) -> {
                System.out.println("Mudou para: " + novoStatus);
                timerLabel.setText(formatTime(proximaDuracao));
            });

            // Iniciar
            pomodoroService.start();
            startButton.setText("Pausar");

        } else {
            // Já existe - pausar ou retomar
            if (pomodoroService.isRunning()) {
                pomodoroService.pause();
                startButton.setText("Retomar");
            } else if (pomodoroService.isPaused()) {
                pomodoroService.resume();
                startButton.setText("Pausar");
            }
        }
    }

    @FXML
    private void handleReset() {
        if (pomodoroService != null) {
            pomodoroService.stop(); // Isso salva no histórico
            pomodoroService = null;
            pomodoroAtual = null;
        }

        startButton.setText("Começar");
        timerLabel.setText("00:00:00");
        System.out.println("Timer resetado e salvo no histórico!");
    }

    // Converter HH:MM:SS ou MM:SS para segundos totais
    private int parseTime(String timeStr) {
        try {
            if (timeStr == null || timeStr.trim().isEmpty()) {
                return 0;
            }

            String[] parts = timeStr.trim().split(":");
            int hours = 0, minutes = 0, seconds = 0;

            if (parts.length == 3) {
                // HH:MM:SS
                hours = Integer.parseInt(parts[0]);
                minutes = Integer.parseInt(parts[1]);
                seconds = Integer.parseInt(parts[2]);
            } else if (parts.length == 2) {
                // MM:SS
                minutes = Integer.parseInt(parts[0]);
                seconds = Integer.parseInt(parts[1]);
            } else if (parts.length == 1) {
                // SS
                seconds = Integer.parseInt(parts[0]);
            }

            return hours * 3600 + minutes * 60 + seconds;
        } catch (NumberFormatException e) {
            System.out.println("Erro ao parsear tempo: " + timeStr);
            return 0;
        }
    }

    // Converter segundos totais para HH:MM:SS
    private String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
