package br.com.dsin.salaoleila.repository;

import br.com.dsin.salaoleila.model.Agendamento;
import br.com.dsin.salaoleila.model.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AgendamentoRepository
        extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByDataAndStatusNot(
            LocalDate data,
            StatusAgendamento status
    );
}
