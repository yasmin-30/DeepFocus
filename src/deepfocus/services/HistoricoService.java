package deepfocus.services;

import deepfocus.modelos.Pomodoro;
import deepfocus.persistence.JsonPersistence;

import java.util.List;

public class HistoricoService {

    private final String caminho = "data/historico.json";
    
    //Serialização
    public void salvar(List<Pomodoro> lista) throws Exception {
        JsonPersistence.salvar(caminho, lista);
    }

    //Desserialização
    public List<Pomodoro> carregar() throws Exception {
        return JsonPersistence.carregar(caminho, Pomodoro.class);
    }
}