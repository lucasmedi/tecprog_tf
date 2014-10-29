package business.bo;

import java.util.ArrayList;
import java.util.List;

public class Autor {
	private int codigo;
	private String primeiroNome;
	private String ultimoNome;
	
	private List<Livro> livros;
	
	public Autor() {
		livros = new ArrayList<Livro>();
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public String getPrimeiroNome() {
		return primeiroNome;
	}
	
	public void setPrimeiroNome(String primeiroNome) {
		this.primeiroNome = primeiroNome;
	}
	
	public String getUltimoNome() {
		return ultimoNome;
	}
	
	public void setUltimoNome(String ultimoNome) {
		this.ultimoNome = ultimoNome;
	}

	public List<Livro> getLivros() {
		return livros;
	}

	public void setLivro(List<Livro> livros) {
		this.livros = livros;
	}
}