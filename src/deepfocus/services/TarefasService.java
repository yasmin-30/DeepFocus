package deepfocus.services;

import deepfocus.modelos.Tarefa;
import deepfocus.persistence.JsonPersistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TarefasService {

    // Caminho padrão 
    private static final String CAMINHO_PADRAO = "data/tarefas.json"; 
    
    private List<Tarefa> tarefas = new ArrayList<>();
    
    // Construtor Padrão
    public TarefasService() {
        try {
        	// Desserialização
            List<Tarefa> carregadas = JsonPersistence.carregar(CAMINHO_PADRAO, Tarefa.class);
            tarefas = new ArrayList<>(carregadas); 
            
        // Tratamento de erros
        } catch (IOException e) {
            System.out.println("⚠ Não foi possível ler o arquivo. Iniciando lista vazia.");
            tarefas = new ArrayList<>(); 
        } catch (Exception e) {
            System.out.println("⚠ Erro ao carregar tarefas, iniciando lista vazia.");
            tarefas = new ArrayList<>();
        }
    }

    // Crud
    public void criarTarefa(Tarefa tarefa) throws Exception {
        tarefas.add(tarefa);
        salvar();
    }

    public List<Tarefa> listarTarefas() {
        return new ArrayList<>(tarefas); 
    }

    public void removerTarefa(String id) throws Exception {
        tarefas.removeIf(t -> t.getId().equals(id));
        salvar();
    }

    public void editarTarefa(String id, String novoTitulo, int novoTempoEstimado) throws Exception {
        Tarefa tarefa = buscarPorId(id);
        tarefa.setTitulo(novoTitulo);
        tarefa.setTempoEstimado(novoTempoEstimado);
        salvar();
    }

    public void concluirTarefa(String id) throws Exception {
        Tarefa tarefa = buscarPorId(id);
        tarefa.setConcluida(true);
        salvar();
    }

    // Procura a tarefa desejada
    private Tarefa buscarPorId(String id) {
        return tarefas.stream()
            .filter(t -> t.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada: " + id));
    }
    
    // Serialização
    public void salvar() throws Exception {
        JsonPersistence.salvar(CAMINHO_PADRAO, tarefas);
    }

    // Para testes
    public void limparTudo() throws Exception {
        tarefas.clear();
        salvar();
    }
}
