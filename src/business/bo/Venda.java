package business.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import exceptions.BusinessException;

public class Venda {
	private int codigo;
	private String nomeCliente;
	private String cpfCliente;
	private String cnpjCliente;
	private Date data;
	
	private List<ItemVenda> itensVenda;
	
	public Venda() {
		itensVenda = new ArrayList<ItemVenda>();
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public String getNomeCliente() {
		return nomeCliente;
	}
	
	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}
	
	public String getCpfCliente() {
		return cpfCliente;
	}
	
	public void setCpfCliente(String cpfCliente) {
		this.cpfCliente = cpfCliente;
	}
	
	public String getCnpjCliente() {
		return cnpjCliente;
	}
	
	public void setCnpjCliente(String cnpjCliente) {
		this.cnpjCliente = cnpjCliente;
	}
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}

	public List<ItemVenda> getItensVenda() throws BusinessException {
		return itensVenda;
	}
	
	public void setItemVenda(List<ItemVenda> itens) {
		this.itensVenda = itens;
	}
}