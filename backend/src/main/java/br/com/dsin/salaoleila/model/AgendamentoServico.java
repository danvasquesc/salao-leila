package br.com.dsin.salaoleila.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "agendamento_servicos",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_agendamento_servico",
                        columnNames = {"agendamento_id", "servico_id"}
                )
        }
)
public class AgendamentoServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agendamento_id", nullable = false)
    private Agendamento agendamento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    public AgendamentoServico() {
    }

    public AgendamentoServico(
            Agendamento agendamento,
            Servico servico) {

        this.agendamento = agendamento;
        this.servico = servico;
    }

    public Long getId() {
        return id;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public Servico getServico() {
        return servico;
    }
}
