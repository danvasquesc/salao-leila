package br.com.dsin.salaoleila.repository;

import br.com.dsin.salaoleila.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
