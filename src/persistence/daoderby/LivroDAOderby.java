package persistence.daoderby;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import persistence.dto.LivroDTO;
import exceptions.ConnectionException;
import exceptions.PersistenceException;
import framework.IConnection;

public class LivroDAOderby {

	private IConnection connection;
	
	public LivroDAOderby(IConnection connection) {
		this.connection = connection;
	}
	
	public List<LivroDTO> buscarTodos() throws PersistenceException, ConnectionException {
		List<LivroDTO> livros = new ArrayList<LivroDTO>(0);
		Statement statement = null;
		
		try {
			statement = connection.getConnection().createStatement();
			
			String query = "select * from Livros";
			ResultSet result = statement.executeQuery(query);
			while (result.next()) {
				livros.add(parseLivroDTO(result));
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
		
		return livros;
	}
	
	public List<LivroDTO> buscarPorEditora(int codigo) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
		
		List<LivroDTO> livros = new ArrayList<>(0);
		try {
			String query = "select * from Livros where CodEditora = ?";
			statement = connection.getConnection().prepareStatement(query);
			statement.setInt(1, codigo);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				livros.add(parseLivroDTO(result));
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
		
		return livros;
	}
	
	public List<LivroDTO> buscarPorAutor(int codigo) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
		
		List<LivroDTO> livros = new ArrayList<>(0);
		try {
			String query = "select liv.* " +
				"from Livros liv " +
				"inner join LivrosAutores lau on liv.Codigo = lau.CodLivro " +
				"where lau.CodAutor = ?";
			statement = connection.getConnection().prepareStatement(query);
			statement.setInt(1, codigo);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				livros.add(parseLivroDTO(result));
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
		
		return livros;
	}
	
	public List<LivroDTO> buscarPorTitulo(String titulo) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
		
		List<LivroDTO> livros = new ArrayList<>(0);
		try {
			String query = "select * from Livros where Titulo like ?";
			statement = connection.getConnection().prepareStatement(query);
			statement.setString(1, "%" + titulo + "%");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				livros.add(parseLivroDTO(result));
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
		
		return livros;
	}
	
	public LivroDTO buscarPorCodigo(int codigo) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
	
		LivroDTO livro = null;
		try {
			String query = "select * from Livros where Codigo = ?";
			statement = connection.getConnection().prepareStatement(query);
			statement.setInt(1,codigo);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				livro = parseLivroDTO(result);
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
		
		return livro;
	}
	
	public LivroDTO buscarLivroPorTitulo(String titulo) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
	
		LivroDTO livro = null;
		try {
			String query = "select * from Livros where Titulo like ?";
			statement = connection.getConnection().prepareStatement(query);
			statement.setString(1, "%" + titulo + "%");
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				livro = parseLivroDTO(result);
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
		
		return livro;
	}
	
	public int inserir(LivroDTO livro) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
		ResultSet generatedKeys = null;
		int id = 0;
		
		int result = 0;
		try {
			String query = "insert into Livros (Titulo, Ano, CodEditora) values (?, ?, ?)";
			statement = connection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, livro.getTitulo());
			statement.setInt(2, livro.getAno());
			statement.setInt(3, livro.getCodigoEditora());
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
	
	public void alterar(LivroDTO livro) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
		
		int result = 0;		
		try {
			String query = "update Livros set Titulo = ?, Ano = ?, CodEditora = ? where Codigo = ?";
			statement = connection.getConnection().prepareStatement(query);
			statement.setString(1, livro.getTitulo());
			statement.setInt(2, livro.getAno());
			statement.setInt(3, livro.getCodigoEditora());
			statement.setInt(4, livro.getCodigo());
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
	
	private LivroDTO parseLivroDTO(ResultSet o) throws SQLException {
		LivroDTO livro = new LivroDTO();
		livro.setCodigo(o.getInt("Codigo"));
		livro.setTitulo(o.getString("Titulo"));
		livro.setAno(o.getInt("Ano"));
		livro.setCodigoEditora(o.getInt("CodEditora"));
		return livro;
	}
}