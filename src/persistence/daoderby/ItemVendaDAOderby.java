package persistence.daoderby;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import persistence.dto.ItemVendaDTO;
import exceptions.ConnectionException;
import exceptions.PersistenceException;
import framework.IConnection;

public class ItemVendaDAOderby {
	
	private IConnection connection;
	
	public ItemVendaDAOderby(IConnection connection) {
		this.connection = connection;
	}
	
	public List<ItemVendaDTO> buscarPorCodigoVenda(int codigo) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
		
		List<ItemVendaDTO> itensVenda = new ArrayList<ItemVendaDTO>(0);
		try {
			String query = "select * from ItensVenda where CodVenda = ?";
			statement = connection.getConnection().prepareStatement(query);
			statement.setInt(1, codigo);
			
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				itensVenda.add(parseItemVendaDTO(result));
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
		
		return itensVenda;
	}
	
	public List<ItemVendaDTO> buscarPorCodigoLivro(int codigo) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
		
		List<ItemVendaDTO> itensVenda = new ArrayList<>(0);
		try {
			String query = "select * from ItensVenda where CodLivro = ?";
			statement = connection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, codigo);
			
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				itensVenda.add(parseItemVendaDTO(result));
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
		
		return itensVenda;
	}
	
	public int inserir(ItemVendaDTO itemVenda) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
		ResultSet generatedKeys = null;
		int id = 0;
		
		int result = 0;
		try {
			String query = "insert into ItensVenda (CodVenda, CodLivro, Quantidade) values (?, ?, ?)";
			statement = connection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, itemVenda.getCodigoVenda());
			statement.setInt(2, itemVenda.getCodigoLivro());
			statement.setInt(3, itemVenda.getQuantidade());
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
	
	private ItemVendaDTO parseItemVendaDTO(ResultSet o) throws SQLException {
		ItemVendaDTO itemVenda = new ItemVendaDTO();
		itemVenda.setCodigoVenda(o.getInt("CodVenda"));
		itemVenda.setCodigoLivro(o.getInt("CodLivro"));
		itemVenda.setQuantidade(o.getInt("Quantidade"));
		return itemVenda;
	}
}