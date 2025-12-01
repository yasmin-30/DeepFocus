package deepfocus.modelos;

import java.util.UUID;

public class Tarefa {

    private String id;
    private String titulo;
    private int tempoEstimado;
    private boolean concluida;

    public Tarefa(String titulo, int tempoEstimado) {
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.tempoEstimado = tempoEstimado;
        this.concluida = false;
    }

    // Construtor vazio (para desserialização)
    public Tarefa() {}

    // Getters
    public String getId() {return id;}
    public String getTitulo() {return titulo;}
    public int getTempoEstimado() {return tempoEstimado;}
    public boolean isConcluida() {return concluida;} // Getter booleano

    // Setters
    public void setId(String id) {this.id = id;} 
    public void setTitulo(String titulo) {this.titulo = titulo;}
    public void setTempoEstimado(int tempoEstimado) {this.tempoEstimado = tempoEstimado;}
    public void setConcluida(boolean concluida) {this.concluida = concluida;}
}
