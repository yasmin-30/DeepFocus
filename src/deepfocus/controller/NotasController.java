package deepfocus.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import deepfocus.modelos.Notas;
import deepfocus.services.NotasService;

public class NotasController {

    @FXML
    private TableView<Notas> notasTable;

    @FXML
    private TableColumn<Notas, String> disciplinaColumn;

    @FXML
    private TableColumn<Notas, Double> nota1Column;

    @FXML
    private TableColumn<Notas, Double> nota2Column;

    @FXML
    private TableColumn<Notas, Double> nota3Column;

    private NotasService notasService;

    @FXML
    public void initialize() {
        notasService = new NotasService();

        // Configurar tabela editável
        notasTable.setEditable(true);

        // Configurar coluna Disciplina (String)
        disciplinaColumn.setCellValueFactory(new PropertyValueFactory<>("disciplina"));
        disciplinaColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        disciplinaColumn.setOnEditCommit(event -> {
            Notas nota = event.getRowValue();
            nota.setDisciplina(event.getNewValue());
            salvarAlteracoes(nota);
        });

        // Configurar colunas de Notas (Double)
        configurarColunaNota(nota1Column, "nota1");
        configurarColunaNota(nota2Column, "nota2");
        configurarColunaNota(nota3Column, "nota3");

        // Carregar dados
        carregarNotas();
    }

    private void configurarColunaNota(TableColumn<Notas, Double> coluna, String propriedade) {
        coluna.setCellValueFactory(new PropertyValueFactory<>(propriedade));
        coluna.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        coluna.setOnEditCommit(event -> {
            Notas nota = event.getRowValue();
            Double novoValor = event.getNewValue();

            if (propriedade.equals("nota1"))
                nota.setNota1(novoValor);
            else if (propriedade.equals("nota2"))
                nota.setNota2(novoValor);
            else if (propriedade.equals("nota3"))
                nota.setNota3(novoValor);

            salvarAlteracoes(nota);
        });
    }

    private void carregarNotas() {
        notasTable.getItems().setAll(notasService.listarNotas());
    }

    private void salvarAlteracoes(Notas nota) {
        try {
            notasService.atualizarNota(nota);
        } catch (Exception e) {
            System.err.println("Erro ao salvar nota: " + e.getMessage());
        }
    }
}
