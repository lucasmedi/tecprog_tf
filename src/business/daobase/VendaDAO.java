package business.daobase;

import java.util.List;

import business.bo.Venda;
import exceptions.MappingException;

public interface VendaDAO {
    List<Venda> buscarTodos() throws MappingException;
    Venda buscarPorCodigo(int codigo) throws MappingException;
    int inserir(Venda venda) throws MappingException;
}