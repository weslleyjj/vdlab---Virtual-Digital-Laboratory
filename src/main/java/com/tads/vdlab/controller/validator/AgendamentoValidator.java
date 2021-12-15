package com.tads.vdlab.controller.validator;

import com.tads.vdlab.controller.dto.AgendamentoDTO;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;

public class AgendamentoValidator {
    public static BindingResult validarAgendamento(AgendamentoDTO agendamento, BindingResult result){
        if(agendamento.getUsuario() == null){
            result.addError(new ObjectError("usuario", "Usuário obrigatório!"));
        }
        if(agendamento.getDataAgendada().isBefore(LocalDateTime.now())){
            result.addError(new ObjectError("dataAgendada", "Data escolhida inválida!"));
        }

        return result;
    }
}
