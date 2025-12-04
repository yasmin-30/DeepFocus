package deepfocus.modelos;

import java.util.UUID;

public class Tarefa {

    private String id;
    private String titulo;
    private String prazo; // Mudado de int tempoEstimado para String prazo
    private boolean concluida;

    public Tarefa(String titulo, String prazo) {
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.prazo = prazo != null ? prazo : "";
        this.concluida = false;
    }

    // Construtor vazio (para desserialização)
    public Tarefa() {
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getPrazo() {
        return prazo;
    }

    public boolean isConcluida() {
        return concluida;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setPrazo(String prazo) {
        this.prazo = prazo;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }
}
