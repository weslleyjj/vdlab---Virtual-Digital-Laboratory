package com.tads.vdlab.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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

    private LocalDateTime dataAgendada;

    @OneToOne
    private Usuario cadastrante;
}
