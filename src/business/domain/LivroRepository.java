package business.domain;

import java.util.ArrayList;
import java.util.List;

import mapping.AutorDAOMapping;
import mapping.EditoraDAOMapping;
import mapping.LivroDAOMapping;
import business.bo.Autor;
import business.bo.Editora;
import business.bo.Livro;
import exceptions.BusinessException;
import framework.IConnection;

public class LivroRepository {
	private IConnection connection;
	
	public LivroRepository(IConnection connection) throws BusinessException {
		if (connection == null)
			throw new BusinessException("Conex�o n�o informada.");
		this.connection = connection;
	}
	
	public List<Livro> buscarTodos() throws BusinessException {
		List<Livro> livros = new ArrayList<>();
		
		try {
			LivroDAOMapping livroDAO = new LivroDAOMapping(connection);
			livros = livroDAO.buscarTodos();
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		
		return livros;
	}
	
	public List<Livro> buscarLivrosPorAutor(String nome) throws BusinessException {
		List<Livro> livros = new ArrayList<>();
		
		AutorDAOMapping autorDAO = new AutorDAOMapping(connection);
		try {
			Autor autor = autorDAO.buscarUmPorNome(nome);
			if (autor == null)
				throw new BusinessException("Autor n�o encontrado");
			
			LivroDAOMapping livroDAO = new LivroDAOMapping(connection);
			livros = livroDAO.buscarPorAutor(autor.getCodigo());
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		
		return livros;
	}
	
	public List<Livro> buscarLivrosPorAutor(Autor autor) throws BusinessException {
		List<Livro> livros = new ArrayList<>();
		
		if (autor == null || autor.getCodigo() <= 0)
			throw new BusinessException("Autor n�o informado.");
		
		try {
			LivroDAOMapping livroDAO = new LivroDAOMapping(connection);
			livros = livroDAO.buscarPorAutor(autor.getCodigo());
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		
		return livros;
	}
	
	public List<Livro> buscarLivrosPorEditora(String nome) throws BusinessException {
		List<Livro> livros = new ArrayList<>();
		
		EditoraDAOMapping editoraDAO = new EditoraDAOMapping(connection);
		try {
			Editora editora = editoraDAO.buscarUmPorNome(nome);
			if (editora == null)
				throw new BusinessException("Editora n�o encontradas");
			
			LivroDAOMapping livroDAO = new LivroDAOMapping(connection);
			livros = livroDAO.buscarPorEditora(editora.getCodigo());
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		
		return livros;
	}
	
	public List<Livro> buscarLivrosPorTitulo(String titulo) throws BusinessException {
		List<Livro> livros = new ArrayList<>();
		
		try {
			LivroDAOMapping livroDAO = new LivroDAOMapping(connection);
			livros = livroDAO.buscarPorTitulo(titulo);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		
		return livros;
	}

	public Livro buscarLivroPorTitulo(String titulo) throws BusinessException {
		Livro livro = null;
		
		try {
			LivroDAOMapping livroDAO = new LivroDAOMapping(connection);
			livro = livroDAO.buscarLivroPorTitulo(titulo);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		
		return livro;
	}
}