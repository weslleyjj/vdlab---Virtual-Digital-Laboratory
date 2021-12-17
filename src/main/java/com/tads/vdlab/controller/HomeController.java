package com.tads.vdlab.controller;

import com.tads.vdlab.model.Agendamento;
import com.tads.vdlab.model.Usuario;
import com.tads.vdlab.repository.UsuarioRepository;
import com.tads.vdlab.service.AgendamentoService;
import com.tads.vdlab.util.ScriptUtil;
import com.tads.vdlab.util.UsuarioUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.List;

@Controller
public class HomeController {

    public static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Value("${arquivos.vdlab}")
    private String arquivosPath;

    @Value("${comandos.scripts.programmer}")
    private String scriptProgrammer;

    @Value("${comandos.scripts.getPlacas}")
    private String scriptGetPlacas;

    private List<String> placasConectadas = new ArrayList<>();

    private UsuarioRepository usuarioRepository;
    private AgendamentoService agendamentoService;

    @Autowired
    public HomeController(UsuarioRepository usuarioRepository, AgendamentoService agendamentoService) throws IOException {
        placasConectadas.add("Espaço Vazio Inicial");
        getPlacasNoSistema();
        this.agendamentoService = agendamentoService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/")
    public String paginaInicial() {
        return "home";
    }

    @GetMapping("/carregar")
    public String paginaCarregarProjeto(Model model) {

        model.addAttribute("message", model.getAttribute("message"));
        return "carregar";
    }

    @PostMapping(value = "/upload")
    public String uploadArquivoSof(@RequestParam("file") MultipartFile file, Integer placaEscolhida, Principal principal, RedirectAttributes redirectAttributes) throws IOException {
        if(!isHorarioUsuarioValido(UsuarioUtil.getUsuarioLogado(principal, usuarioRepository))){
            redirectAttributes.addFlashAttribute("message", "Você não possui agendamento neste horário.");
            return "redirect:/carregar";
        }
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Envie um arquivo válido.");
            return "redirect:/carregar";
        }
        try {
            String fileName = "carregar-"+file.getOriginalFilename();

            Path path = Paths.get(arquivosPath).toAbsolutePath().normalize();
            Files.createDirectories(path);

            Path targetLocation = path.resolve(fileName); // Caminho absoluto para acesso ao arquivo
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String[] comandoProgramar = {scriptProgrammer, placasConectadas.get(placaEscolhida), "p;" + targetLocation};

            ScriptUtil.executaComandoShellArray(comandoProgramar);
            return "redirect:/controlador-fpga/"+ placaEscolhida;

        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @GetMapping("/tutorial")
    public String paginaTutorial(){
        return "tutorial";
    }

    private void getPlacasNoSistema() throws IOException {
        List<String> resultShell = ScriptUtil.executaComandoShellLine("/home/weslley/Documentos/TADS/TCC/testes-tcl/get_fpga_order.sh");
        for (String s : resultShell) {
            if(s.contains("USB")){
                placasConectadas.add(s);
            }
        }
    }

    private boolean isHorarioUsuarioValido(Usuario usuario){
        LocalDateTime dataAtual = LocalDateTime.now();
        List<Agendamento> agendamentos = agendamentoService.buscarAgendamentoByIdUsuario(usuario.getId());

        for (Agendamento agendamento : agendamentos) {
            LocalDateTime dataExpirado = agendamento.getDataAgendada();
            boolean isIndefinido = agendamento.getTempoSessao() == 0;
            if(!isIndefinido){
                dataExpirado.plusMinutes(agendamento.getTempoSessao());
                if(agendamento.getDataAgendada().isBefore(dataAtual) && dataExpirado.isBefore(dataAtual)){
                    return true;
                }
            }else{
                if(agendamento.getDataAgendada().isBefore(dataAtual)){
                    return true;
                }
            }
        }
        return false;
    }

}

