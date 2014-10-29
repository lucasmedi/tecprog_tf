package business.domain;

import java.util.ArrayList;
import java.util.List;

import mapping.AutorDAOMapping;
import business.bo.Autor;
import business.bo.Editora;
import exceptions.BusinessException;
import framework.IConnection;

public class AutorRepository {
	private IConnection connection;

	public AutorRepository(IConnection connection) throws BusinessException {
		if (connection == null)
			throw new BusinessException("Conex�o n�o informada.");
		this.connection = connection;
	}
	
	public List<Autor> buscarTodos() throws BusinessException {
		List<Autor> autores = new ArrayList<>();
		
		try {
			AutorDAOMapping autorDAO = new AutorDAOMapping(connection);
			autores = autorDAO.buscarTodos();
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		
		return autores;
	}
	
	public List<Autor> buscarPorNome(String nome) throws BusinessException {
		List<Autor> autores = new ArrayList<>();
		
		if (nome == null || nome.isEmpty())
			throw new BusinessException("Nome n�o informado.");
		
		try {
			AutorDAOMapping autorDAO = new AutorDAOMapping(connection);
			autores = autorDAO.buscarPorNome(nome);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		
		return autores;
	}
	
	public List<Autor> buscarPorEditora(Editora editora) throws BusinessException {
		List<Autor> autores = new ArrayList<>();
		
		if (editora == null || editora.getCodigo() <= 0)
			throw new BusinessException("Editora n�o informada.");
		
		try {
			AutorDAOMapping autorDAO = new AutorDAOMapping(connection);
			autores = autorDAO.buscarPorEditora(editora);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		
		return autores;
	}
}