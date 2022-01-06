package com.tads.vdlab.controller.validator;

import com.tads.vdlab.controller.dto.AgendamentoDTO;
import com.tads.vdlab.model.Agendamento;
import com.tads.vdlab.repository.AgendamentoRepository;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AgendamentoValidator {

    public static BindingResult validarAgendamento(AgendamentoDTO agendamento, BindingResult result,
                                                   Integer quantidadePlacas, AgendamentoRepository agendamentoRepository){
        if(agendamento.getUsuario() == null){
            result.addError(new ObjectError("usuario", "Usuário obrigatório!"));
        }
        if(agendamento.getDataAgendada().isBefore(LocalDateTime.now())){
            result.addError(new ObjectError("dataAgendada", "Data escolhida inválida!"));
        }

        if(result.hasErrors()){
            return result;
        } else {
            List<String> errosBanco = validarAgendamentoBanco(AgendamentoDTO.fromDTO(agendamento), quantidadePlacas, agendamentoRepository);
            for (String s : errosBanco) {
                Integer indexErro = 0;
                result.addError(new ObjectError(indexErro.toString(), s));
            }
        }

        return result;
    }

    public static List<String> validarAgendamentoBanco(Agendamento agendamento, Integer quantidadePlacas, AgendamentoRepository agendamentoRepository){
        List<String> erros = new ArrayList<>();

        if(agendamentoRepository.countAgendamentosBetweenDates(agendamento.getDataAgendada(),
                agendamento.getDateAfterTempoSessao(), Objects.isNull(agendamento.getId())? 0 : agendamento.getId()) > quantidadePlacas) {
            erros.add("Não há mais placas disponíveis para esse horário");
        }

        if(agendamentoRepository.countAgendamentosUsuarioBetweenDates(agendamento.getDataAgendada(),
                agendamento.getDateAfterTempoSessao(), agendamento.getUsuario().getId(), Objects.isNull(agendamento.getId())? 0 : agendamento.getId()) > 0){
            erros.add("Este usuário já possui um agendamento nesse horário");
        }

        Integer tempo = agendamento.getTempoSessao();

        if(tempo != 10 && tempo != 15 && tempo != 20 && tempo != 25 && tempo != 30) {
            erros.add("Tempo de sessão escolhido não permitido");
        }

        return erros;
    }

}
