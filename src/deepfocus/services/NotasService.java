package deepfocus.services;

import deepfocus.modelos.Nota;
import deepfocus.persistence.JsonPersistence; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; 

public class NotasService {
    
    // Caminho padrão
    private static final String CAMINHO_PADRAO = "data/notas.json";
    
    private List<Nota> notas = new ArrayList<>();
    
    // Construtor Padrão
    public NotasService() {
        try {
        	// Desserialização
            List<Nota> carregadas = JsonPersistence.carregar(CAMINHO_PADRAO, Nota.class);
            notas = new ArrayList<>(carregadas); 
            
        // Verificação de erros
        } catch (IOException e) {
            System.out.println("Não foi possível ler o arquivo de notas. Iniciando lista vazia.");
            notas = new ArrayList<>(); 
        } catch (Exception e) {
            System.out.println("Erro ao carregar notas, iniciando lista vazia.");
            notas = new ArrayList<>();
        }
    }

    // Cria notas
    public void criarNota(Nota nota) throws Exception {
        notas.add(nota);
        salvar();
    }
    
    // Edita notas
    public void editarNota(String disciplina, double novaNota) throws Exception {
        Nota notaParaEditar = buscarPorDisciplina(disciplina)
            .orElseThrow(() -> new IllegalArgumentException("Disciplina não encontrada: " + disciplina));
        
        // Setta a alteração
        notaParaEditar.setNota(novaNota);
        salvar();
    }

    // Remove a nota desejada
    public void removerNota(Nota nota) throws Exception {
        notas.remove(nota);
        salvar();
    }
    
    // Listagem das notas
    public List<Nota> listarNotas() {
        return new ArrayList<>(notas);
    }
    
    // Procura a nota desejada
    private Optional<Nota> buscarPorDisciplina(String disciplina) {
        return notas.stream()
            .filter(n -> n.getDisciplina().equalsIgnoreCase(disciplina))
            .findFirst();
    }

    // Persistência
    public void salvar() throws Exception {
        JsonPersistence.salvar(CAMINHO_PADRAO, notas);
    }

    // Para testes
    public void limparTudo() throws Exception {
        notas.clear();
        salvar();
    }
}
