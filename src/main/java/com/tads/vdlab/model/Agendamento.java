package com.tads.vdlab.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name="agendamento")
public class Agendamento extends AbstractEntidade{

    @ManyToOne
    private Usuario usuario;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataAgendada;

    @OneToOne
    private Usuario cadastrante;

    private Integer tempoSessao; // Tempo da sessão em minutos

    public boolean isAgendamentoExpirado(){
        LocalDateTime aux = dataAgendada;
        aux.plusMinutes(tempoSessao);
        return aux.isBefore(LocalDateTime.now());
    }
}
