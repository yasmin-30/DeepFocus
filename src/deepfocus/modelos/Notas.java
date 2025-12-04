package deepfocus.modelos;

public class Notas {
    private String disciplina;
    private double nota1;
    private double nota2;
    private double nota3;

    // Construtor vazio - jackson
    public Notas() {
    }

    // Construtor parametrizado
    public Notas(String disciplina, double nota1, double nota2, double nota3) {
        this.disciplina = disciplina;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.nota3 = nota3;
    }

    // Construtor apenas com disciplina (inicializa notas com 0)
    public Notas(String disciplina) {
        this.disciplina = disciplina;
        this.nota1 = 0.0;
        this.nota2 = 0.0;
        this.nota3 = 0.0;
    }

    // Getters
    public String getDisciplina() {
        return disciplina;
    }

    public double getNota1() {
        return nota1;
    }

    public double getNota2() {
        return nota2;
    }

    public double getNota3() {
        return nota3;
    }

    // Setters
    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public void setNota1(double nota1) {
        this.nota1 = nota1;
    }

    public void setNota2(double nota2) {
        this.nota2 = nota2;
    }

    public void setNota3(double nota3) {
        this.nota3 = nota3;
    }
}
