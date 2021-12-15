package com.tads.vdlab.controller.dto;


import com.tads.vdlab.model.Agendamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgendamentoDTO {
    Long id;
    UsuarioDTO cadastrante;
    LocalDateTime dataAgendada;
    UsuarioDTO usuario;
    Integer tempoSessao;

    public static Agendamento fromDTO(AgendamentoDTO agendamento){
        return Agendamento.builder()
                .id(agendamento.getId())
                .cadastrante(UsuarioDTO.fromDTO(agendamento.getCadastrante()))
                .dataAgendada(agendamento.getDataAgendada())
                .usuario(UsuarioDTO.fromDTO(agendamento.getUsuario()))
                .tempoSessao(agendamento.getTempoSessao())
                .build();
    }

    public static AgendamentoDTO toDTO(Agendamento agendamento){
        return AgendamentoDTO.builder()
                .id(agendamento.getId())
                .cadastrante(UsuarioDTO.toDTO(agendamento.getCadastrante()))
                .dataAgendada(agendamento.getDataAgendada())
                .usuario(UsuarioDTO.toDTO(agendamento.getUsuario()))
                .tempoSessao(agendamento.getTempoSessao())
                .build();
    }
}
