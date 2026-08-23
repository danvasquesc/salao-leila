package br.com.dsin.salaoleila.repository;

import br.com.dsin.salaoleila.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Verifica se ja existe agendamento no mesmo dia e horario
    boolean existsByDataAndHorario(LocalDate data, LocalTime horario);
}
