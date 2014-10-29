package persistence.daoderby;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import persistence.dto.AutorDTO;
import persistence.dto.EditoraDTO;
import exceptions.ConnectionException;
import exceptions.PersistenceException;
import framework.IConnection;

public class AutorDAOderby {

	private IConnection connection;
	
	public AutorDAOderby(IConnection connection) {
		this.connection = connection;
	}
	
	public List<AutorDTO> buscarTodos() throws PersistenceException, ConnectionException {
		List<AutorDTO> autores = new ArrayList<AutorDTO>(0);
		Statement statement = null;
		
		try {
			statement = connection.getConnection().createStatement();
			
			String query = "select * from Autores";
			
			ResultSet result = statement.executeQuery(query);
			while (result.next()) {
				autores.add(parseAutorDTO(result));
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
		
		return autores;
	}
	
	public List<AutorDTO> buscarPorNome(String nome) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
		
		List<AutorDTO> autores = new ArrayList<>(0);
		try {
			String query = "select * from Autores where PrimeiroNome like ? or UltimoNome like ?";
			statement = connection.getConnection().prepareStatement(query);
			statement.setString(1, nome);
			statement.setString(2, nome);
			
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				autores.add(parseAutorDTO(result));
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

		return autores;
	}
	
	public List<AutorDTO> buscarPorEditora(EditoraDTO editora) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
		
		List<AutorDTO> autores = new ArrayList<>(0);
		try {
			String query = "select distinct aut.* "
					+ "from Autores aut "
					+ "inner join LivrosAutores lau on aut.Codigo = lau.CodAutor "
					+ "inner join Livros liv on lau.CodLivro = liv.Codigo "
					+ "where liv.CodEditora = ?";
			statement = connection.getConnection().prepareStatement(query);
			statement.setInt(1, editora.getCodigo());
			
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				autores.add(parseAutorDTO(result));
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

		return autores;
	}
	
	public AutorDTO buscarPorCodigo(int codigo) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
		
		AutorDTO autor = null;
		try {
			String query = "select * from Autores where Codigo = ?";
			statement = connection.getConnection().prepareStatement(query);
			statement.setInt(1, codigo);
			
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				autor = parseAutorDTO(result);
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
		
		return autor;
	}
	
	public AutorDTO buscarUmPorNome(String nome) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
		
		AutorDTO autor = null;
		try {
			String query = "select * from Autores where PrimeiroNome || ' ' || UltimoNome like ?";
			statement = connection.getConnection().prepareStatement(query);
			statement.setString(1, "%" + nome + "%");
			
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				autor = parseAutorDTO(result);
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
		
		return autor;
	}
	
	public int inserir(AutorDTO autor) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
		ResultSet generatedKeys = null;
		int id = 0;
		
		int result = 0;
		try {
			String query = "insert into Autores (PrimeiroNome, UltimoNome) values (?, ?)";
			statement = connection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, autor.getPrimeiroNome());
			statement.setString(2, autor.getUltimoNome());
			result = statement.executeUpdate();
			
			generatedKeys = statement.getGeneratedKeys();
	        if (generatedKeys.next())
	            id = generatedKeys.getInt(1);
		} catch (Exception e) {
			throw new PersistenceException("Erro ao executar inser��o: " + e.getMessage(), e);
		} finally {
			try {
				generatedKeys.close();
				statement.close();
			} catch (SQLException e) {
				throw new ConnectionException("Erro ao encerrar conex�o com a base de dados.", e);
			}
		}
		
		if (result == 0)
			throw new PersistenceException("Erro ao executar inser��o.");
		
		return id;
	}
	
	public void alterar(AutorDTO autor) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
		
		int result = 0;		
		try {
			String query = "update Autores set PrimeiroNome = ?, UltimoNome = ? where Codigo = ?";
			statement = connection.getConnection().prepareStatement(query);
			statement.setString(1, autor.getPrimeiroNome());
			statement.setString(2, autor.getUltimoNome());
			statement.setInt(3, autor.getCodigo());
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
	
	private AutorDTO parseAutorDTO(ResultSet o) throws SQLException {
		AutorDTO autor = new AutorDTO();
		autor.setCodigo(o.getInt("Codigo"));
		autor.setPrimeiroNome(o.getString("PrimeiroNome"));
		autor.setUltimoNome(o.getString("UltimoNome"));
		return autor;
	}
}