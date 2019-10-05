package morales.acxel.spring.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import morales.acxel.spring.app.models.entity.Cliente;

public interface IClienteService {

	List<Cliente> findAll();
	
	Page<Cliente> findAll(Pageable pageable);

	Cliente findById(Long id);

	void save(Cliente cliente);

	void delete(Long id);

}
