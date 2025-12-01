package deepfocus.services;

import deepfocus.modelos.Pomodoro;
import deepfocus.persistence.JsonPersistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HistoricoService {

    private static final String CAMINHO_PADRAO = "data/historico.json";
    
    // Lista interna para manter o estado do histórico
    private List<Pomodoro> historico = new ArrayList<>();
    
    // Construtor
    public HistoricoService() {
        try {
            // Desserialização
            List<Pomodoro> carregados = JsonPersistence.carregar(CAMINHO_PADRAO, Pomodoro.class);
            historico = new ArrayList<>(carregados); 
            
        // Verificação de erros
        } catch (IOException e) {
            System.out.println("Não foi possível ler o histórico. Iniciando lista vazia.");
            historico = new ArrayList<>(); 
        } catch (Exception e) {
            System.out.println("Erro ao carregar histórico, iniciando lista vazia.");
            historico = new ArrayList<>();
        }
    }

    // Adiciona um novo pomodoro ao historico
    public void adicionarPomodoro(Pomodoro pomodoro) throws Exception {
        historico.add(pomodoro);
        salvar();
    }
    
    // Listagem do historico
    public List<Pomodoro> listarHistorico() {
        return new ArrayList<>(historico);
    }
    
    // Serialização
    public void salvar() throws Exception {
        JsonPersistence.salvar(CAMINHO_PADRAO, historico);
    }

    // Para teste
    public void limparTudo() throws Exception {
        historico.clear();
        salvar();
    }
}
