package com.tads.vdlab.controller;

import com.tads.vdlab.controller.dto.UsuarioDTO;
import com.tads.vdlab.model.Agendamento;
import com.tads.vdlab.model.Role;
import com.tads.vdlab.model.Usuario;
import com.tads.vdlab.repository.AgendamentoRepository;
import com.tads.vdlab.repository.RoleRepository;
import com.tads.vdlab.repository.UsuarioRepository;
import com.tads.vdlab.service.UsuarioService;
import com.tads.vdlab.util.UsuarioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/agendamento")
public class AgendamentoController {

    private AgendamentoRepository repository;
    private UsuarioRepository usuarioRepository;
    private UsuarioService usuarioService;

    @Autowired
    public AgendamentoController(AgendamentoRepository agendamentoRepository, UsuarioRepository usuarioRepository, UsuarioService usuarioService){
        this.repository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String agendamento(Model model,

                              @RequestParam("page") Optional<Integer> page,
                              @RequestParam("size") Optional<Integer> size){

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<UsuarioDTO> usuarioPage;
        boolean isBusca = model.containsAttribute("busca");
        String nomeBusca = model.containsAttribute("nomeBusca") ? (String) model.getAttribute("nomeBusca") : null;

        usuarioPage = usuarioService.findPaginated(PageRequest.of(currentPage - 1, pageSize), isBusca, nomeBusca);

        model.addAttribute("usuariosPage", usuarioPage);

        int totalPages = usuarioPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("agendamento", new Agendamento());

        return "agendamento";
    }

    @GetMapping("/buscaUsuario")
    public String buscaUsuarioAgendamento(@RequestParam("nome") String busca, Model model, RedirectAttributes redirectAttributes){

        redirectAttributes.addFlashAttribute("nomeBusca", busca);
        redirectAttributes.addFlashAttribute("busca", true);
        return "redirect:/agendamento";
    }

    @PostMapping("/agendar")
    public String agendar(@ModelAttribute Agendamento agendamento, BindingResult result, Principal principal){
        Usuario cadastrante = UsuarioUtil.getUsuarioLogado(principal, usuarioRepository);
        agendamento.setCadastrante(cadastrante);
        agendamento.setAtivo(true);

        repository.save(agendamento);

        return "redirect:/agendamento";
    }
}
