package br.com.dsin.salaoleila.service;

import br.com.dsin.salaoleila.dto.response.OperacionalAgendamentoResponse;
import br.com.dsin.salaoleila.dto.response.OperacionalServicoResponse;
import br.com.dsin.salaoleila.dto.request.AgendamentoUpdateRequest;
import br.com.dsin.salaoleila.dto.response.AgendamentoResponse;
import br.com.dsin.salaoleila.model.Agendamento;
import br.com.dsin.salaoleila.model.AgendamentoServico;
import br.com.dsin.salaoleila.model.StatusAgendamento;
import br.com.dsin.salaoleila.model.StatusServicoAgendamento;
import br.com.dsin.salaoleila.repository.AgendamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class OperacionalService {

    private final AgendamentoRepository agendamentoRepository;
    private final AgendamentoService agendamentoService;

    public OperacionalService(
            AgendamentoRepository agendamentoRepository,
            AgendamentoService agendamentoService) {

        this.agendamentoRepository = agendamentoRepository;
        this.agendamentoService = agendamentoService;
    }

    @Transactional(readOnly = true)
    public List<OperacionalAgendamentoResponse> listarPorData(
            LocalDate data) {

        LocalDate dataConsulta =
                data != null ? data : LocalDate.now();

        return agendamentoRepository
                .findByDataOrderByHorarioAsc(dataConsulta)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public AgendamentoResponse alterarAgendamento(
            Long id,
            AgendamentoUpdateRequest request) {

        return agendamentoService.atualizarOperacional(
                id,
                request
        );
    }

    @Transactional
    public OperacionalAgendamentoResponse atualizarStatusAgendamento(
            Long agendamentoId,
            StatusAgendamento novoStatus) {

        Agendamento agendamento =
                buscarAgendamentoPorId(agendamentoId);

        agendamento.setStatus(novoStatus);

        if (novoStatus == StatusAgendamento.CANCELADO) {

            agendamento.getServicos()
                    .forEach(item ->
                            item.setStatus(
                                    StatusServicoAgendamento.CANCELADO
                            )
                    );
        }

        return toResponse(agendamento);
    }

    @Transactional
    public OperacionalAgendamentoResponse atualizarStatusServico(
            Long agendamentoId,
            Long itemId,
            StatusServicoAgendamento novoStatus) {

        Agendamento agendamento =
                buscarAgendamentoPorId(agendamentoId);

        if (agendamento.getStatus()
                == StatusAgendamento.CANCELADO) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível alterar serviços "
                            + "de um agendamento cancelado."
            );
        }

        AgendamentoServico item =
                agendamento.getServicos()
                        .stream()
                        .filter(agendamentoServico ->
                                agendamentoServico
                                        .getId()
                                        .equals(itemId)
                        )
                        .findFirst()
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Serviço não encontrado "
                                                + "neste agendamento."
                                )
                        );

        item.setStatus(novoStatus);

        return toResponse(agendamento);
    }

    private Agendamento buscarAgendamentoPorId(Long id) {

        return agendamentoRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Agendamento não encontrado."
                        )
                );
    }

    private OperacionalAgendamentoResponse toResponse(
            Agendamento agendamento) {

        List<OperacionalServicoResponse> servicos =
                agendamento.getServicos()
                        .stream()
                        .map(item ->
                                new OperacionalServicoResponse(
                                        item.getId(),
                                        item.getServico().getId(),
                                        item.getServico().getNome(),
                                        item.getServico().getDuracao(),
                                        item.getStatus()
                                )
                        )
                        .toList();

        return new OperacionalAgendamentoResponse(
                agendamento.getId(),
                agendamento.getData(),
                agendamento.getHorario(),
                agendamento.getStatus(),
                agendamento.getCliente().getId(),
                agendamento.getCliente().getNome(),
                agendamento.getCliente().getTelefone(),
                servicos
        );
    }
}
