package br.com.dsin.salaoleila.service;

import br.com.dsin.salaoleila.dto.request.AgendamentoRequest;
import br.com.dsin.salaoleila.dto.request.AgendamentoUpdateRequest;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

        validarDataEHorario(
                request.data(),
                request.horario()
        );

        validarServicosDuplicados(request.servicoIds());

        Cliente cliente = clienteRepository
                .findById(request.clienteId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Cliente não encontrado."
                        )
                );

        List<Servico> servicos =
                buscarServicos(request.servicoIds());

        validarDisponibilidade(
                request.data(),
                request.horario(),
                servicos,
                null
        );

        Agendamento agendamento = new Agendamento(
                request.data(),
                request.horario(),
                StatusAgendamento.AGENDADO,
                cliente
        );

        servicos.forEach(agendamento::adicionarServico);

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

    @Transactional(readOnly = true)
    public AgendamentoResponse buscarPorId(Long id) {

        Agendamento agendamento =
                buscarAgendamentoPorId(id);

        return toResponse(agendamento);
    }

    @Transactional
    public AgendamentoResponse atualizar(
            Long id,
            AgendamentoUpdateRequest request) {

        Agendamento agendamento =
                buscarAgendamentoPorId(id);

        validarPrazoAlteracao(agendamento);

        validarDataEHorario(
                request.data(),
                request.horario()
        );

        validarServicosDuplicados(request.servicoIds());

        List<Servico> servicos =
                buscarServicos(request.servicoIds());

        validarDisponibilidade(
                request.data(),
                request.horario(),
                servicos,
                agendamento.getId()
        );

        agendamento.setData(request.data());
        agendamento.setHorario(request.horario());

        agendamento.removerServicos();

        agendamentoRepository.flush();

        servicos.forEach(agendamento::adicionarServico);

        return toResponse(agendamento);
    }

    @Transactional
    public AgendamentoResponse atualizarOperacional(
            Long id,
            AgendamentoUpdateRequest request) {

        Agendamento agendamento =
                buscarAgendamentoPorId(id);

        validarDataEHorario(
                request.data(),
                request.horario()
        );

        validarServicosDuplicados(
                request.servicoIds()
        );

        List<Servico> servicos =
                buscarServicos(
                        request.servicoIds()
                );

        validarDisponibilidade(
                request.data(),
                request.horario(),
                servicos,
                agendamento.getId()
        );

        agendamento.setData(
                request.data()
        );

        agendamento.setHorario(
                request.horario()
        );

        agendamento.removerServicos();

        agendamentoRepository.flush();

        servicos.forEach(
                agendamento::adicionarServico
        );

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

    private List<Servico> buscarServicos(
            List<Long> servicoIds) {

        return servicoIds
                .stream()
                .map(servicoId ->
                        servicoRepository
                                .findById(servicoId)
                                .orElseThrow(() ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Serviço não encontrado: "
                                                        + servicoId
                                        )
                                )
                )
                .toList();
    }

    private void validarPrazoAlteracao(
            Agendamento agendamento) {

        LocalDate dataLimite =
                LocalDate.now().plusDays(2);

        if (agendamento.getData().isBefore(dataLimite)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Agendamentos com menos de 2 dias de antecedência "
                            + "só podem ser alterados por telefone."
            );
        }
    }

    private void validarDataEHorario(
            LocalDate data,
            LocalTime horario) {

        LocalDateTime dataHoraAgendamento =
                LocalDateTime.of(data, horario);

        if (dataHoraAgendamento
                .isBefore(LocalDateTime.now())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Data e horário do agendamento "
                            + "não podem estar no passado."
            );
        }
    }

    private void validarServicosDuplicados(
            List<Long> servicoIds) {

        if (new HashSet<>(servicoIds).size()
                != servicoIds.size()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Um mesmo serviço não pode ser "
                            + "informado mais de uma vez."
            );
        }
    }

    private void validarDisponibilidade(
            LocalDate data,
            LocalTime horario,
            List<Servico> novosServicos,
            Long agendamentoIdIgnorado) {

        int duracaoNovoAgendamento =
                calcularDuracaoServicos(novosServicos);

        LocalDateTime inicioNovo =
                LocalDateTime.of(data, horario);

        LocalDateTime fimNovo =
                inicioNovo.plusMinutes(
                        duracaoNovoAgendamento
                );

        List<Agendamento> agendamentosDoDia =
                agendamentoRepository
                        .findByDataAndStatusNot(
                                data,
                                StatusAgendamento.CANCELADO
                        );

        boolean possuiConflito =
                agendamentosDoDia
                        .stream()
                        .filter(agendamento ->
                                agendamentoIdIgnorado == null
                                        || !agendamento
                                        .getId()
                                        .equals(
                                                agendamentoIdIgnorado
                                        )
                        )
                        .anyMatch(agendamentoExistente ->
                                possuiConflitoDeHorario(
                                        inicioNovo,
                                        fimNovo,
                                        agendamentoExistente
                                )
                        );

        if (possuiConflito) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "O horário informado conflita "
                            + "com outro agendamento."
            );
        }
    }

    private boolean possuiConflitoDeHorario(
            LocalDateTime inicioNovo,
            LocalDateTime fimNovo,
            Agendamento agendamentoExistente) {

        LocalDateTime inicioExistente =
                LocalDateTime.of(
                        agendamentoExistente.getData(),
                        agendamentoExistente.getHorario()
                );

        int duracaoExistente =
                agendamentoExistente
                        .getServicos()
                        .stream()
                        .mapToInt(item ->
                                item
                                        .getServico()
                                        .getDuracao()
                        )
                        .sum();

        LocalDateTime fimExistente =
                inicioExistente.plusMinutes(
                        duracaoExistente
                );

        return inicioNovo.isBefore(fimExistente)
                && fimNovo.isAfter(inicioExistente);
    }

    private int calcularDuracaoServicos(
            List<Servico> servicos) {

        return servicos
                .stream()
                .mapToInt(Servico::getDuracao)
                .sum();
    }

    private AgendamentoResponse toResponse(
            Agendamento agendamento) {

        Cliente cliente = agendamento.getCliente();

        ClienteResponse clienteResponse =
                new ClienteResponse(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getTelefone(),
                        cliente.getEmail()
                );

        List<ServicoResponse> servicosResponse =
                agendamento
                        .getServicos()
                        .stream()
                        .map(item -> {

                            Servico servico =
                                    item.getServico();

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

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> buscarHistorico(
            Long clienteId,
            LocalDate dataInicio,
            LocalDate dataFim) {

        validarPeriodo(dataInicio, dataFim);

        if (!clienteRepository.existsById(clienteId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Cliente não encontrado."
            );
        }

        return agendamentoRepository
                .findByCliente_IdAndDataBetweenOrderByDataDescHorarioDesc(
                        clienteId,
                        dataInicio,
                        dataFim
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private void validarPeriodo(
            LocalDate dataInicio,
            LocalDate dataFim) {

        if (dataInicio.isAfter(dataFim)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A data inicial não pode ser posterior à data final."
            );
        }
    }
}
