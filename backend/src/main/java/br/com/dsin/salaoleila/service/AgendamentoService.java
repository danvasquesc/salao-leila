package br.com.dsin.salaoleila.service;

import br.com.dsin.salaoleila.dto.request.AgendamentoRequest;
import br.com.dsin.salaoleila.dto.response.AgendamentoResponse;
import br.com.dsin.salaoleila.dto.response.ClienteResponse;
import br.com.dsin.salaoleila.dto.response.ServicoResponse;
import br.com.dsin.salaoleila.model.Agendamento;
import br.com.dsin.salaoleila.model.Cliente;
import br.com.dsin.salaoleila.model.Servico;
import br.com.dsin.salaoleila.model.StatusAgendamento;
import br.com.dsin.salaoleila.repository.AgendamentoRepository;
import br.com.dsin.salaoleila.repository.ClienteRepository;
import br.com.dsin.salaoleila.repository.ServicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final ServicoRepository servicoRepository;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            ClienteRepository clienteRepository,
            ServicoRepository servicoRepository) {

        this.agendamentoRepository = agendamentoRepository;
        this.clienteRepository = clienteRepository;
        this.servicoRepository = servicoRepository;
    }

    @Transactional
    public AgendamentoResponse criar(AgendamentoRequest request) {

        validarDataEHorario(request);

        validarServicosDuplicados(request.servicoIds());

        boolean horarioOcupado =
                agendamentoRepository.existsByDataAndHorarioAndStatusNot(
                        request.data(),
                        request.horario(),
                        StatusAgendamento.CANCELADO
                );

        if (horarioOcupado) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe um agendamento para esta data e horário."
            );
        }

        Cliente cliente = clienteRepository
                .findById(request.clienteId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Cliente não encontrado."
                        )
                );

        Agendamento agendamento = new Agendamento(
                request.data(),
                request.horario(),
                StatusAgendamento.AGENDADO,
                cliente
        );

        for (Long servicoId : request.servicoIds()) {

            Servico servico = servicoRepository
                    .findById(servicoId)
                    .orElseThrow(() ->
                            new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Serviço não encontrado: " + servicoId
                            )
                    );

            agendamento.adicionarServico(servico);
        }

        Agendamento agendamentoSalvo =
                agendamentoRepository.save(agendamento);

        return toResponse(agendamentoSalvo);
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listar() {

        return agendamentoRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private void validarDataEHorario(AgendamentoRequest request) {

        LocalDateTime dataHoraAgendamento =
                LocalDateTime.of(
                        request.data(),
                        request.horario()
                );

        if (dataHoraAgendamento.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Data e horário do agendamento não podem estar no passado."
            );
        }
    }

    private void validarServicosDuplicados(List<Long> servicoIds) {

        if (new HashSet<>(servicoIds).size() != servicoIds.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Um mesmo serviço não pode ser informado mais de uma vez."
            );
        }
    }

    private AgendamentoResponse toResponse(Agendamento agendamento) {

        Cliente cliente = agendamento.getCliente();

        ClienteResponse clienteResponse =
                new ClienteResponse(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getTelefone(),
                        cliente.getEmail()
                );

        List<ServicoResponse> servicosResponse =
                agendamento.getServicos()
                        .stream()
                        .map(item -> {

                            Servico servico = item.getServico();

                            return new ServicoResponse(
                                    servico.getId(),
                                    servico.getNome(),
                                    servico.getDescricao(),
                                    servico.getPreco(),
                                    servico.getDuracao()
                            );
                        })
                        .toList();

        return new AgendamentoResponse(
                agendamento.getId(),
                agendamento.getData(),
                agendamento.getHorario(),
                agendamento.getStatus(),
                clienteResponse,
                servicosResponse
        );
    }
}