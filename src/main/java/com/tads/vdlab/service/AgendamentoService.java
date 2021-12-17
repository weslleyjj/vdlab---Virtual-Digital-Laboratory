package com.tads.vdlab.service;

import com.tads.vdlab.model.Agendamento;
import com.tads.vdlab.repository.AgendamentoRepository;
import com.tads.vdlab.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendamentoService {

    private AgendamentoRepository agendamentoRepository;
    private UsuarioRepository usuarioRepository;

    @Autowired
    public AgendamentoService(AgendamentoRepository agendamentoRepository, UsuarioRepository usuarioRepository){
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Agendamento> buscarAgendamentoByIdUsuario(Long id){
        return agendamentoRepository.buscarAgendamentoByIdUsuario(id);
    }


}
