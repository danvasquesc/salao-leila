package br.com.dsin.salaoleila.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agendamentos")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime horario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusAgendamento status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(
            mappedBy = "agendamento",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AgendamentoServico> servicos = new ArrayList<>();

    public Agendamento() {
    }

    public Agendamento(
            LocalDate data,
            LocalTime horario,
            StatusAgendamento status,
            Cliente cliente) {

        this.data = data;
        this.horario = horario;
        this.status = status;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<AgendamentoServico> getServicos() {
        return servicos;
    }

    public void adicionarServico(Servico servico) {

        AgendamentoServico agendamentoServico =
                new AgendamentoServico(this, servico);

        this.servicos.add(agendamentoServico);
    }

    public void removerServicos() {
        this.servicos.clear();
    }
}