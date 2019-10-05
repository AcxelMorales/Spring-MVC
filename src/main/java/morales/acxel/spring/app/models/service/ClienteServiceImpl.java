package morales.acxel.spring.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import morales.acxel.spring.app.models.dao.IClienteDAO;
import morales.acxel.spring.app.models.entity.Cliente;

@Service
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	private IClienteDAO clienteDAO;
	
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) this.clienteDAO.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		return this.clienteDAO.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findById(Long id) {
		return this.clienteDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		this.clienteDAO.save(cliente);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.clienteDAO.deleteById(id);
	}
	
}
