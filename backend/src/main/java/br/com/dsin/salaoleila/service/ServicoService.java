package br.com.dsin.salaoleila.service;

import br.com.dsin.salaoleila.dto.request.ServicoRequest;
import br.com.dsin.salaoleila.dto.response.ServicoResponse;
import br.com.dsin.salaoleila.model.Servico;
import br.com.dsin.salaoleila.repository.ServicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    // Cria servico
    public ServicoResponse criar(ServicoRequest request) {

        Servico servico = new Servico(
                request.nome(),
                request.descricao(),
                request.preco(),
                request.duracao()
        );

        servico = servicoRepository.save(servico);

        return toResponse(servico);
    }

    // Lista servicoes
    public List<ServicoResponse> listar() {

        return servicoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Busca servico por ID
    public ServicoResponse buscarPorId(Long id) {

        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Serviço não encontrado"
                        )
                );

        return toResponse(servico);
    }

    // Atualiza servico
    public ServicoResponse atualizar(Long id, ServicoRequest request) {

        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Serviço não encontrado"
                        )
                );

        servico.setNome(request.nome());
        servico.setDescricao(request.descricao());
        servico.setPreco(request.preco());
        servico.setDuracao(request.duracao());

        servico = servicoRepository.save(servico);

        return toResponse(servico);
    }

    // Exclui servico
    public void excluir(Long id) {

        if (!servicoRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Serviço não encontrado"
            );
        }

        servicoRepository.deleteById(id);
    }

    private ServicoResponse toResponse(Servico servico) {

        return new ServicoResponse(
                servico.getId(),
                servico.getNome(),
                servico.getDescricao(),
                servico.getPreco(),
                servico.getDuracao()
        );
    }
}
