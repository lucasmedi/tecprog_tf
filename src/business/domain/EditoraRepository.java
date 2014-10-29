package business.domain;

import java.util.ArrayList;
import java.util.List;

import mapping.EditoraDAOMapping;
import business.bo.Editora;
import exceptions.BusinessException;
import framework.IConnection;

public class EditoraRepository {
	
	private IConnection connection;
	
	public EditoraRepository(IConnection connection) throws BusinessException {
		if (connection == null)
			throw new BusinessException("Conex�o n�o informada.");
		this.connection = connection;
	}
	
	public List<Editora> buscarTodos() throws BusinessException {
		List<Editora> editoras = new ArrayList<>();
		
		try {
			EditoraDAOMapping editoraDAO = new EditoraDAOMapping(connection);
			editoras = editoraDAO.buscarTodos();
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		
		return editoras;
	}
	
	public List<Editora> buscarPorNome(String nome) throws BusinessException {
		List<Editora> editoras = new ArrayList<>();
		
		if (nome == null || nome.isEmpty())
			throw new BusinessException("Nome n�o informado.");
		
		try {
			EditoraDAOMapping editoraDAO = new EditoraDAOMapping(connection);
			editoras = editoraDAO.buscarPorNome(nome);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		
		return editoras;
	}

	public Editora buscarUmPorNome(String nome) throws BusinessException {
		Editora editora = null;
		
		if (nome == null || nome.isEmpty())
			throw new BusinessException("Nome n�o informado.");
		
		try {
			EditoraDAOMapping editoraDAO = new EditoraDAOMapping(connection);
			editora = editoraDAO.buscarUmPorNome(nome);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		
		return editora;
	}

}