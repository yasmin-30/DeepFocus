package deepfocus.modelos;

public class Notas {
    private String disciplina;
    private double nota;
    
    // Construtor vazio - jackson
    public Notas() {};
    
    // Construtor parametrizado
    public Notas(String disciplina, double nota) {
        this.disciplina = disciplina;
        this.nota = nota;
    }
    
    // Getters 
    public String getDisciplina() {return disciplina;}
    public double getNota() {return nota;}
    
    // Setters
    public void setDisciplina(String disciplina) { this.disciplina = disciplina;}
    public void setNota(double  nota) { this.nota = nota;}
    
}
