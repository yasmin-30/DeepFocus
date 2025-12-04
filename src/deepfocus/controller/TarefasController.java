package deepfocus.controller;

import deepfocus.modelos.Tarefa;
import deepfocus.services.TarefasService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TarefasController {

    @FXML
    private FlowPane postItContainer;

    @FXML
    private Button btnAdicionar;

    private TarefasService tarefasService;

    @FXML
    public void initialize() {
        tarefasService = new TarefasService();
        carregarTarefas();
        atualizarBotaoAdicionar();
    }

    private void carregarTarefas() {
        postItContainer.getChildren().clear();

        for (Tarefa tarefa : tarefasService.listarTarefas()) {
            VBox postIt = criarPostIt(tarefa);
            postItContainer.getChildren().add(postIt);
        }
    }

    private VBox criarPostIt(Tarefa tarefa) {
        VBox postIt = new VBox(8);
        postIt.getStyleClass().add("post-it");
        postIt.setPrefSize(200, 150);
        postIt.setAlignment(Pos.TOP_LEFT);

        // Header com checkbox (conclusão) e botão excluir
        HBox header = new HBox(5);
        header.setAlignment(Pos.CENTER_LEFT);

        // CheckBox para marcar como concluído (tic verde)
        CheckBox checkConcluida = new CheckBox("✓");
        checkConcluida.setSelected(tarefa.isConcluida());
        checkConcluida.setStyle("-fx-text-fill: #00aa00; -fx-font-size: 16px; -fx-font-weight: bold;");
        checkConcluida.setOnAction(e -> {
            try {
                tarefasService.toggleConcluida(tarefa.getId());
                // Atualizar visual (diminuir opacidade se concluído)
                postIt.setStyle(checkConcluida.isSelected() ? "-fx-opacity: 0.7;" : "-fx-opacity: 1.0;");
            } catch (Exception ex) {
                mostrarErro("Erro ao atualizar status", ex.getMessage());
            }
        });

        // Spacer para empurrar o botão X para a direita
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Botão de excluir (X)
        Button btnExcluir = new Button("×");
        btnExcluir.getStyleClass().add("delete-button");
        btnExcluir.setOnAction(e -> excluirTarefa(tarefa.getId()));

        header.getChildren().addAll(checkConcluida, spacer, btnExcluir);

        // TextArea para o texto principal (título)
        TextArea textArea = new TextArea(tarefa.getTitulo());
        textArea.setWrapText(true);
        textArea.setPrefRowCount(3);
        textArea.setPromptText("Digite o lembrete...");
        textArea.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; " +
                "-fx-text-fill: #333; -fx-font-size: 13px; -fx-padding: 0;");
        VBox.setVgrow(textArea, Priority.ALWAYS);

        // Salvar título ao perder o foco
        textArea.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String novoTitulo = textArea.getText().trim();
                if (!novoTitulo.equals(tarefa.getTitulo())) {
                    try {
                        tarefasService.editarTarefa(tarefa.getId(), novoTitulo, tarefa.getPrazo());
                        tarefa.setTitulo(novoTitulo);
                    } catch (Exception e) {
                        mostrarErro("Erro ao salvar", e.getMessage());
                    }
                }
            }
        });

        // Campo de prazo (data)
        HBox prazoBox = new HBox(5);
        prazoBox.setAlignment(Pos.CENTER_LEFT);

        Label labelPrazo = new Label("📅");
        labelPrazo.setStyle("-fx-font-size: 12px;");

        TextField campoPrazo = new TextField(tarefa.getPrazo());
        campoPrazo.setPromptText("dd/mm/aaaa");
        campoPrazo.setMaxWidth(90);
        campoPrazo.setStyle("-fx-background-color: rgba(255,255,255,0.5); " +
                "-fx-border-color: #999; -fx-border-width: 0 0 1 0; " +
                "-fx-font-size: 11px; -fx-padding: 2;");

        // Salvar prazo ao perder o foco
        campoPrazo.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String novoPrazo = campoPrazo.getText().trim();
                if (!novoPrazo.equals(tarefa.getPrazo())) {
                    try {
                        tarefasService.editarTarefa(tarefa.getId(), tarefa.getTitulo(), novoPrazo);
                        tarefa.setPrazo(novoPrazo);
                    } catch (Exception e) {
                        mostrarErro("Erro ao salvar prazo", e.getMessage());
                    }
                }
            }
        });

        prazoBox.getChildren().addAll(labelPrazo, campoPrazo);

        postIt.getChildren().addAll(header, textArea, prazoBox);

        // Aplicar estilo de concluído se necessário
        if (tarefa.isConcluida()) {
            postIt.setStyle("-fx-opacity: 0.7;");
        }

        return postIt;
    }

    @FXML
    private void handleAddLembrete() {
        if (!tarefasService.podeAdicionarMais()) {
            mostrarAlerta("Limite atingido", "Você já possui o máximo de 9 lembretes!");
            return;
        }

        try {
            Tarefa novaTarefa = new Tarefa("Digite aqui...", "");
            tarefasService.criarTarefa(novaTarefa);

            VBox postIt = criarPostIt(novaTarefa);
            postItContainer.getChildren().add(postIt);

            atualizarBotaoAdicionar();

            // Focar no novo lembrete
            TextArea textArea = (TextArea) postIt.getChildren().get(1);
            textArea.requestFocus();
            textArea.selectAll();

        } catch (Exception e) {
            mostrarErro("Erro ao adicionar lembrete", e.getMessage());
        }
    }

    private void excluirTarefa(String id) {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja realmente excluir este lembrete?");

        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    tarefasService.removerTarefa(id);
                    carregarTarefas();
                    atualizarBotaoAdicionar();
                } catch (Exception e) {
                    mostrarErro("Erro ao excluir lembrete", e.getMessage());
                }
            }
        });
    }

    private void atualizarBotaoAdicionar() {
        if (btnAdicionar != null) {
            btnAdicionar.setDisable(!tarefasService.podeAdicionarMais());
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
