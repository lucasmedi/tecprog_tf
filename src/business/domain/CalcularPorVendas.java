package business.domain;

import java.util.ArrayList;
import java.util.List;

import business.bo.Autor;
import business.bo.AutorPagamento;
import business.bo.Livro;
import exceptions.BusinessException;
import framework.IConnection;

public class CalcularPorVendas implements ICalcularPagamento {

	private IConnection connection;
	private List<Autor> autores;
	private double valorFixo;
	private int bonusVendas;
	private int bonusExclusividade;
	
	public CalcularPorVendas(IConnection connection, List<Autor> autores) {
		this.connection = connection;
		this.autores = autores;
	}
	
	@Override
	public List<AutorPagamento> calcularValor() throws BusinessException {
		VendaRepository vendaRepo = new VendaRepository(connection); 
		LivroRepository livroRepo  = new LivroRepository(connection);
		
		List<AutorPagamento> result = new ArrayList<>(0);
		List<Livro> livros = new ArrayList<>(0);
		
		int quantidadeTotal = 0;
		for (Autor autor : autores) {
			livros = livroRepo.buscarLivrosPorAutor(autor);
			
			boolean exclusividade = true;
			boolean primeiro = true;
			int codigo = 0;
			
			double valorAutor = 0.0;
			for (Livro livro : livros) {
				int quantidade = vendaRepo.buscarQuantidadeVendidaPorLivro(livro);
				quantidadeTotal += quantidade;
				valorAutor += quantidade * valorFixo;
				
				if (primeiro) {
					primeiro = false;
					codigo = livro.getEditora().getCodigo();
				}
				
				if (livro.getEditora().getCodigo() != codigo)
					exclusividade = false;
			}
			
			if (exclusividade)
				valorAutor += valorAutor * (bonusExclusividade / 100.0);
			
			result.add(new AutorPagamento(autor, valorAutor));
		}
		
		double bonus = ((quantidadeTotal * valorFixo) * (bonusVendas / 100.0)) / result.size();
		for (AutorPagamento autor : result) {
			autor.setValor(autor.getValor() + bonus);
		}
		
		return result;
	}

	@Override
	public void setValorLivro(double valor) {
		this.valorFixo = valor;
	}

	@Override
	public void setBonusVenda(int bonus) {
		this.bonusVendas = bonus;
	}

	@Override
	public void setBonusExclusividade(int bonus) {
		this.bonusExclusividade = bonus;
	}
}