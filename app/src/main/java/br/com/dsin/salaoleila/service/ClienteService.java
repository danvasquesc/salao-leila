package br.com.dsin.salaoleila.service;

import br.com.dsin.salaoleila.dto.request.ClienteRequest;
import br.com.dsin.salaoleila.dto.response.ClienteResponse;
import br.com.dsin.salaoleila.model.Cliente;
import br.com.dsin.salaoleila.repository.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // Cria cliente
    public ClienteResponse criar(ClienteRequest request) {

        Cliente cliente = new Cliente(
                request.nome(),
                request.telefone(),
                request.email()
        );

        cliente = clienteRepository.save(cliente);

        return toResponse(cliente);
    }

    // Lista clientes
    public List<ClienteResponse> listar() {

        return clienteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Busca cliente por ID
    public ClienteResponse buscarPorId(Long id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Cliente não encontrado"
                        )
                );

        return toResponse(cliente);
    }

    // Atualiza cliente
    public ClienteResponse atualizar(Long id, ClienteRequest request) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Cliente não encontrado"
                        )
                );

        cliente.setNome(request.nome());
        cliente.setTelefone(request.telefone());
        cliente.setEmail(request.email());

        cliente = clienteRepository.save(cliente);

        return toResponse(cliente);
    }

    // Exclui cliente
    public void excluir(Long id) {

        if (!clienteRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Cliente não encontrado"
            );
        }

        clienteRepository.deleteById(id);
    }

    private ClienteResponse toResponse(Cliente cliente) {

        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getTelefone(),
                cliente.getEmail()
        );
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorTelefone(String telefone) {

        Cliente cliente = clienteRepository
                .findByTelefone(telefone)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Cliente não encontrado."
                        )
                );

        return toResponse(cliente);
    }
}