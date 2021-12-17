package com.tads.vdlab.controller;

import com.tads.vdlab.controller.dto.UsuarioDTO;
import com.tads.vdlab.model.Agendamento;
import com.tads.vdlab.model.Role;
import com.tads.vdlab.model.Usuario;
import com.tads.vdlab.repository.AgendamentoRepository;
import com.tads.vdlab.repository.RoleRepository;
import com.tads.vdlab.repository.UsuarioRepository;
import com.tads.vdlab.util.UsuarioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/agendamento")
public class AgendamentoController {

    private AgendamentoRepository repository;
    private UsuarioRepository usuarioRepository;
    private RoleRepository roleRepository;

    @Autowired
    public AgendamentoController(AgendamentoRepository agendamentoRepository, UsuarioRepository usuarioRepository, RoleRepository roleRepository){
        this.repository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String agendamento(Model model){
        Role roleParaAgendar = roleRepository.findByName("DISCENTE");
        List<UsuarioDTO> usuarios = usuarioRepository.findByRolesIsAndAtivo(roleParaAgendar, true)
                        .stream().map(UsuarioDTO::toDTO).collect(Collectors.toList());

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("agendamento", new Agendamento());
        return "agendamento";
    }

    @PostMapping("/agendar")
    public String agendar(@ModelAttribute Agendamento agendamento, BindingResult result, Principal principal){
        Usuario cadastrante = UsuarioUtil.getUsuarioLogado(principal, usuarioRepository);
        agendamento.setCadastrante(cadastrante);
        agendamento.setAtivo(true);

        repository.save(agendamento);

        return "redirect:/";
    }
}
