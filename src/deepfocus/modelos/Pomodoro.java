package deepfocus.modelos;

import java.time.LocalDateTime;

public class Pomodoro {

    private LocalDateTime inicio;
    private LocalDateTime termino;
    
    private int duracaoFoco;
    private int duracaoDescanso;

    // Construtor vazio - jackson
    public Pomodoro() {}

    // Construtor parametrizado
    public Pomodoro(LocalDateTime inicio, int foco, int descanso) {
        this.inicio = inicio;
        this.duracaoFoco = foco;
        this.duracaoDescanso = descanso;
    }

    // Getters
    public LocalDateTime getInicio() { return inicio; }
    public LocalDateTime getTermino() { return termino; } 
    public int getDuracaoFoco() { return duracaoFoco; }
    public int getDuracaoDescanso() { return duracaoDescanso; }

    // Setters
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio;}
    public void setTermino(LocalDateTime termino) { this.termino = termino;} 
    public void setDuracaoFoco(int duracaoFoco) { this.duracaoFoco = duracaoFoco;}
    public void setDuracaoDescanso(int duracaoDescanso) { this.duracaoDescanso = duracaoDescanso;}
}
