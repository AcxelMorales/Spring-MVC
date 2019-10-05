package morales.acxel.spring.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import morales.acxel.spring.app.models.entity.Cliente;

public interface IClienteDAO extends PagingAndSortingRepository<Cliente, Long> {
	
}
