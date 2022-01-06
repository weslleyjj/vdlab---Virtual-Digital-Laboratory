package com.tads.vdlab.service;

import com.tads.vdlab.controller.dto.UsuarioDTO;
import com.tads.vdlab.model.Agendamento;
import com.tads.vdlab.model.Usuario;
import com.tads.vdlab.repository.AgendamentoRepository;
import com.tads.vdlab.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public Page<Agendamento> findPaginated(Pageable pageable, Usuario usuario) {

        List<Agendamento> agendamentos = agendamentoRepository.findAgendamentosByUsuarioAndAtivoOrderByDataAgendadaDesc(usuario, true);

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Agendamento> list;

        if (agendamentos.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, agendamentos.size());
            list = agendamentos.subList(startItem, toIndex);
        }

        Page<Agendamento> agendamentoPage
                = new PageImpl<Agendamento>(list, PageRequest.of(currentPage, pageSize), agendamentos.size());

        return agendamentoPage;
    }

    public Page<Agendamento> findPaginatedCadastrados(Pageable pageable, Usuario usuario) {

        List<Agendamento> agendamentos = agendamentoRepository.findAgendamentosByCadastranteAndAtivoOrderByDataAgendadaDesc(usuario, true);

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Agendamento> list;

        if (agendamentos.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, agendamentos.size());
            list = agendamentos.subList(startItem, toIndex);
        }

        Page<Agendamento> agendamentoPage
                = new PageImpl<Agendamento>(list, PageRequest.of(currentPage, pageSize), agendamentos.size());

        return agendamentoPage;
    }

}
