package persistence.daoderby;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import persistence.dto.VendaDTO;
import exceptions.ConnectionException;
import exceptions.PersistenceException;
import framework.IConnection;

public class VendaDAOderby {

	private IConnection connection;
	
	public VendaDAOderby(IConnection connection) {
		this.connection = connection;
	}
	
	public List<VendaDTO> buscarTodos() throws PersistenceException, ConnectionException {
		List<VendaDTO> vendas = new ArrayList<VendaDTO>(0);
		Statement statement = null;
		
		try {
			statement = connection.getConnection().createStatement();
			
			String query = "select * from Vendas";
			
			ResultSet result = statement.executeQuery(query);
			while (result.next()) {
				vendas.add(parseVendaDTO(result));
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
		
		return vendas;
	}
	
	public VendaDTO buscarPorCodigo(int codigo) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
		
		VendaDTO venda = null;
		try {
			String query = "select * from Vendas where Codigo = ?";
			statement = connection.getConnection().prepareStatement(query);
			statement.setInt(1, codigo);
			
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				venda = parseVendaDTO(result);
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
		
		return venda;
	}
	
	public int inserir(VendaDTO venda) throws PersistenceException, ConnectionException {
		PreparedStatement statement = null;
		ResultSet generatedKeys = null;
		int id = 0;
		
		int result = 0;
		try {
			String query = "insert into Vendas (NomeCliente, CpfCliente, CnpjCliente, Data) values (?, ?, ?, CURRENT_DATE)";
			statement = connection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, venda.getNomeCliente());
			statement.setString(2, venda.getCpfCliente());
			statement.setString(3, venda.getCnpjCliente());
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
	
	private VendaDTO parseVendaDTO(ResultSet o) throws SQLException {
		VendaDTO venda = new VendaDTO();
		venda.setCodigo(o.getInt("Codigo"));
		venda.setNomeCliente(o.getString("NomeCliente"));
		venda.setCpfCliente(o.getString("CpfCliente"));
		venda.setCnpjCliente(o.getString("CnpjCliente"));
		venda.setData(o.getDate("Data"));
		return venda;
	}
}