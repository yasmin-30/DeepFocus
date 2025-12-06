package deepfocus.services;

import deepfocus.modelos.Notas;
import deepfocus.persistence.JsonPersistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NotasService {

    // Caminho padrão
    private static final String CAMINHO_PADRAO = "data/notas.json";

    private List<Notas> notas = new ArrayList<>();

    // Construtor Padrão
    public NotasService() {
        try {
            // Desserialização
            List<Notas> carregadas = JsonPersistence.carregar(CAMINHO_PADRAO, Notas.class);
            notas = new ArrayList<>(carregadas);
        } catch (IOException e) {
            System.out.println("Não foi possível ler o arquivo de notas. Iniciando lista vazia.");
            notas = new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Erro ao carregar notas, iniciando lista vazia.");
            notas = new ArrayList<>();
        }

        // Garantir 8 linhas iniciais se estiver vazio ou incompleto
        garantirLinhasMinimas();
    }

    private void garantirLinhasMinimas() {
        while (notas.size() < 8) {
            notas.add(new Notas("Disciplina " + (notas.size() + 1)));
        }
    }

    // Cria notas
    public void criarNota(Notas nota) throws Exception {
        notas.add(nota);
        salvar();
    }

    // Atualiza notas (chamado após edição na tabela)
    public void atualizarNota(Notas nota) throws Exception {
        // Como o objeto já está na lista (referência), basta salvar
        salvar();
    }

    // Remove a nota desejada
    public void removerNota(Notas nota) throws Exception {
        notas.remove(nota);
        garantirLinhasMinimas(); // Mantém sempre pelo menos 8
        salvar();
    }

    // Listagem das notas
    public List<Notas> listarNotas() {
        return new ArrayList<>(notas);
    }

    // Persistência
    public void salvar() throws Exception {
        JsonPersistence.salvar(CAMINHO_PADRAO, notas);
    }

    // Para testes
    public void limparTudo() throws Exception {
        notas.clear();
        garantirLinhasMinimas();
        salvar();
    }
}
