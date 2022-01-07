package com.tads.vdlab.controller;

import com.tads.vdlab.controller.dto.UsuarioDTO;
import com.tads.vdlab.model.Agendamento;
import com.tads.vdlab.model.Role;
import com.tads.vdlab.model.Usuario;
import com.tads.vdlab.repository.UsuarioRepository;
import com.tads.vdlab.service.AgendamentoService;
import com.tads.vdlab.util.ScriptUtil;
import com.tads.vdlab.util.UsuarioUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.*;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class HomeController {

    private UsuarioRepository usuarioRepository;
    private AgendamentoService agendamentoService;

    @Autowired
    public HomeController(UsuarioRepository usuarioRepository, AgendamentoService agendamentoService) throws IOException {
        this.agendamentoService = agendamentoService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/")
    public String paginaInicial(Model model,
                                Principal principal,
                                @RequestParam("page") Optional<Integer> page,
                                @RequestParam("size") Optional<Integer> size){

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Agendamento> agendamentoPage;

        if(isUsuarioAdminOrDocente(principal)) {
            agendamentoPage = agendamentoService.findPaginatedCadastrados(PageRequest.of(currentPage - 1, pageSize), UsuarioUtil.getUsuarioLogado(principal, usuarioRepository));
        } else {
            agendamentoPage = agendamentoService.findPaginated(PageRequest.of(currentPage - 1, pageSize), UsuarioUtil.getUsuarioLogado(principal, usuarioRepository));
        }

        model.addAttribute("agendamentosPage", agendamentoPage);

        int totalPages = agendamentoPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "home";
    }

    @GetMapping("/carregar")
    public String paginaCarregarProjeto(Model model) {

        model.addAttribute("message", model.getAttribute("message"));
        return "carregar";
    }

    @GetMapping("/tutorial")
    public String paginaTutorial(){
        return "tutorial";
    }

    private boolean isUsuarioAdminOrDocente(Principal principal){
        Usuario usr = UsuarioUtil.getUsuarioLogado(principal, usuarioRepository);

        for (Role role : usr.getRoles()) {
            if(role.getName().equalsIgnoreCase("ADMIN") || role.getName().equalsIgnoreCase("DOCENTE")) {
                return true;
            }
        }

        return false;
    }

}

