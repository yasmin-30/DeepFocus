package deepfocus.services;

import deepfocus.modelos.Nota;
import java.util.ArrayList;
import java.util.List;

public class NotasService {
	private List<Nota> notas = new ArrayList<>();
	
	public void criarNota(Nota nota) {
		notas.add(nota);
	}
	
	public void removerNota(Nota nota) {
		notas.remove(nota);
	}
	
	public List<Nota> listarNotas() {
		return notas;
	}
	
	//Função de editar nota?
}