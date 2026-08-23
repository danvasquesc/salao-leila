package br.com.dsin.salaoleila.service;

import br.com.dsin.salaoleila.model.Agendamento;
import br.com.dsin.salaoleila.model.Cliente;
import br.com.dsin.salaoleila.model.Servico;
import br.com.dsin.salaoleila.repository.AgendamentoRepository;
import br.com.dsin.salaoleila.repository.ClienteRepository;
import br.com.dsin.salaoleila.repository.ServicoRepository;
import org.springframework.stereotype.Service;

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

    // Metodo que cria agendamentos com validacoes
    public Agendamento criar(Agendamento agendamento) {

        if (agendamento.getCliente() == null ||
                agendamento.getCliente().getId() == null) {

            throw new IllegalArgumentException("Cliente é obrigatório.");
        }

        if (agendamento.getServico() == null ||
                agendamento.getServico().getId() == null) {

            throw new IllegalArgumentException("Serviço é obrigatório.");
        }

        if (agendamento.getData() == null) {
            throw new IllegalArgumentException("Data é obrigatória.");
        }

        if (agendamento.getHorario() == null) {
            throw new IllegalArgumentException("Horário é obrigatório.");
        }

        if (agendamento.getStatus() == null) {
            throw new IllegalArgumentException("Status é obrigatório.");
        }

        if (agendamentoRepository.existsByDataAndHorario(
                agendamento.getData(),
                agendamento.getHorario())) {

            throw new IllegalArgumentException(
                    "Já existe um agendamento para esta data e horário."
            );
        }

        Cliente cliente = clienteRepository.findById(
                agendamento.getCliente().getId()
        ).orElseThrow(() ->
                new IllegalArgumentException("Cliente não encontrado.")
        );

        Servico servico = servicoRepository.findById(
                agendamento.getServico().getId()
        ).orElseThrow(() ->
                new IllegalArgumentException("Serviço não encontrado.")
        );

        agendamento.setCliente(cliente);
        agendamento.setServico(servico);

        return agendamentoRepository.save(agendamento);
    }

    public List<Agendamento> listar() {
        return agendamentoRepository.findAll();
    }
}
