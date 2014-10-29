package persistence.daoderby;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import persistence.dto.LivroAutorDTO;
import exceptions.ConnectionException;
import exceptions.PersistenceException;
import framework.IConnection;

public class LivroAutorDAOderby {
	
	private IConnection connection;
	
	public LivroAutorDAOderby(IConnection connection) {
		this.connection = connection;
	}
	
	public List<LivroAutorDTO> buscarPorCodigoAutor(int codigo) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
		
		List<LivroAutorDTO> livroAutor = new ArrayList<LivroAutorDTO>(0);
		try {
			String query = "select * from LivrosAutores where CodAutor = ?";
			statement = connection.getConnection().prepareStatement(query);
			statement.setInt(1, codigo);
			
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				livroAutor.add(parseLivroAutorDTO(result));
			}
		} catch (Exception e) {
			throw new PersistenceException("Erro ao executar busca: " + e.getMessage(), e);
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				throw new ConnectionException("Erro ao encerrar conex�o com a base de dados.", e);
			}
		}
		
		return livroAutor;
	}
	
	public List<LivroAutorDTO> buscarPorCodigoLivro(int codigo) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
		
		List<LivroAutorDTO> livroAutor = new ArrayList<>(0);
		try {
			String query = "select * from LivrosAutores where CodLivro = ?";
			statement = connection.getConnection().prepareStatement(query);
			statement.setInt(1, codigo);
			
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				livroAutor.add(parseLivroAutorDTO(result));
			}
		} catch (Exception e) {
			throw new PersistenceException("Erro ao executar busca: " + e.getMessage(), e);
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				throw new ConnectionException("Erro ao encerrar conex�o com a base de dados.", e);
			}
		}
		
		return livroAutor;
	}
	
	public void inserir(LivroAutorDTO livroAutor) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
		
		int result = 0;
		try {
			String query = "insert into LivrosAutores (CodAutor, CodLivro) values (?, ?)";
			statement = connection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, livroAutor.getCodigoAutor());
			statement.setInt(1, livroAutor.getCodigoLivro());
			result = statement.executeUpdate();
		} catch (Exception e) {			
			throw new PersistenceException("Erro ao executar inser��o: " + e.getMessage(), e);
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				throw new ConnectionException("Erro ao encerrar conex�o com a base de dados.", e);
			}
		}
		
		if (result == 0)
			throw new PersistenceException("Erro ao executar inser��o.");
	}
	
	public void deletar(LivroAutorDTO livroAutor) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
		
		int result = 0;
		try {
			String query = "delete LivrosAutores where CodAutor = ? and CodLivro = ?";
			statement = connection.getConnection().prepareStatement(query);
			statement.setInt(1, livroAutor.getCodigoAutor());
			statement.setInt(2, livroAutor.getCodigoLivro());
			result = statement.executeUpdate();
		} catch (Exception e) {
			throw new PersistenceException("Erro ao executar a atualiza��o: " + e.getMessage() , e);
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				throw new ConnectionException("Erro ao encerrar conex�o com a base de dados.", e);
			}
		}
		
		if (result == 0)
			throw new PersistenceException("Erro ao executar a atualiza��o.");
	}
	
	private LivroAutorDTO parseLivroAutorDTO(ResultSet o) throws SQLException {
		LivroAutorDTO livroAutor = new LivroAutorDTO();
		livroAutor.setCodigoAutor(o.getInt("CodAutor"));
		livroAutor.setCodigoLivro(o.getInt("CodLivro"));
		return livroAutor;
	}
}
