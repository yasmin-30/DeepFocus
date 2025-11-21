package deepfocus.modelos;

public class Tarefas {
	private String titulo;
    private int tempoEstimado;
    private boolean concluida;
    
    // Construtor vazio - jackson
    public Tarefas() {};
    
    // Construtor
    public Tarefas(String titulo, int tempoEstimado) {
        this.titulo = titulo;
        this.tempoEstimado = tempoEstimado;
        this.concluida = false;
    }
    
    // Getters 
    public String getTitulo() {return titulo;}
    public int getTempoEstimado() {return tempoEstimado;}
    public boolean getConcluida() {return concluida;}
    
    //Setters
    public void setTitulo(String titulo) { this.titulo = titulo;}
    public void setTempoEstimado(int tempoEstimado) { this.tempoEstimado = tempoEstimado;}
    public void setConcluida(boolean concluida) { this.concluida = concluida;}
}
