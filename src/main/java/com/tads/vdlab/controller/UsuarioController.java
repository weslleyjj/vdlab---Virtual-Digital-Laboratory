package com.tads.vdlab.controller;

import com.tads.vdlab.controller.dto.AgendamentoDTO;
import com.tads.vdlab.controller.dto.UsuarioDTO;
import com.tads.vdlab.controller.validator.AgendamentoValidator;
import com.tads.vdlab.model.Agendamento;
import com.tads.vdlab.model.Role;
import com.tads.vdlab.model.Usuario;
import com.tads.vdlab.repository.RoleRepository;
import com.tads.vdlab.repository.UsuarioRepository;
import com.tads.vdlab.service.UsuarioService;
import com.tads.vdlab.util.EmailUtil;
import com.tads.vdlab.util.UsuarioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private UsuarioRepository usuarioRepository;
    private UsuarioService usuarioService;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder crypt;
    private EmailUtil emailUtil;

    @Autowired
    public UsuarioController(JavaMailSender mailSender, UsuarioRepository usuarioRepository, UsuarioService usuarioService, RoleRepository roleRepository){
        crypt = new BCryptPasswordEncoder();
        emailUtil = new EmailUtil(mailSender);
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/cadastrar")
    public String cadastrarUsuario(Model model){

        if(!model.containsAttribute("usuario")){
            model.addAttribute("usuario", new Usuario());
        }

        model.addAttribute("message", model.getAttribute("message"));

        return "cadastrarUsuario";
    }

    @PostMapping("/cadastrar")
    public String cadastrarNovoUsuario(Usuario usuario, RedirectAttributes redirectAttributes){
        List<String> errors = validarUsuario(usuario, false);
        if(errors.size() > 0){
            redirectAttributes.addFlashAttribute("message", errors);
            redirectAttributes.addFlashAttribute("usuario", usuario);
            return "redirect:/usuario/cadastrar";
        }

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(usuarioRepository.getRolePadraoUsuario());
        usuario.setRoles(roleSet);
        usuario.setAtivo(true);

        usuario.setSenha(crypt.encode(usuario.getSenha()));

        new Thread(){
            @Override
            public void run() {
                emailUtil.newUserSendMail(usuario.getLogin(), usuario.getNome(),usuario.getEmail());
            }
        }.start();

        usuarioRepository.save(usuario);

        return "redirect:/";
    }

    @GetMapping("/editar")
    public String cadastrarUsuario(Model model, Principal principal){
        if(model.getAttribute("usuario") == null) {
            Usuario usuarioLogado = UsuarioUtil.getUsuarioLogado(principal, usuarioRepository);
            Usuario usr = usuarioRepository.getById(usuarioLogado.getId());
            model.addAttribute("usuario", UsuarioDTO.toDTO(usr));
        }

        model.addAttribute("message", model.getAttribute("message"));

        return "editarUsuario";
    }

    @GetMapping("/permissoes")
    public String editarPermissoes(Model model,
                                   @RequestParam("page") Optional<Integer> page,
                                   @RequestParam("size") Optional<Integer> size){

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<UsuarioDTO> usuarioPage;
        boolean isBusca = model.containsAttribute("busca");
        String nomeBusca = model.containsAttribute("nomeBusca") ? (String) model.getAttribute("nomeBusca") : null;

        usuarioPage = usuarioService.findAllPaginated(PageRequest.of(currentPage - 1, pageSize), isBusca, nomeBusca);

        model.addAttribute("usuariosPage", usuarioPage);

        int totalPages = usuarioPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        if(model.getAttribute("usuarioEscolhido") == null){
            model.addAttribute("usuarioEscolhido", new UsuarioDTO());
        }

        model.addAttribute("rolesPossiveis", roleRepository.findAll());

        return "editarPermissoesUsuario";
    }

    @PostMapping("/editarPermissaoUsuario")
    public String editarPermissoesUsuario(@ModelAttribute UsuarioDTO usuario,
                                          @RequestParam("permissoes") List<Integer> permissoes,
                                          BindingResult result, RedirectAttributes redirectAttributes) {
        Usuario usrOriginal = usuarioRepository.findById(usuario.getId()).get();

        if (permissoes.isEmpty()) {
            result.addError(new ObjectError("role", "É necessário especificar no mínimo 1 permissão"));
        }

        Set<Role> rolesEscolhidas = new HashSet<>();
        for (Integer permissao : permissoes) {
            Role teste = roleRepository.findById(permissao).get();
            if(teste != null){
                rolesEscolhidas.add(teste);
            }
        }

        boolean flag = false;
        for (Role rolesEscolhida : rolesEscolhidas) {
            if(rolesEscolhida.getName().equalsIgnoreCase("DISCENTE")){
                flag = true;
            }
        }

        if(flag && rolesEscolhidas.size() > 1){
            result.addError(new ObjectError("proibido", "Usuário DISCENTE não pode possuir outras permissões"));
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.usuarioEscolhido", result);
            usuario.setNome(usrOriginal.getNome());
            redirectAttributes.addFlashAttribute("usuarioEscolhido", usuario);
            return "redirect:/usuario/permissoes";
        }

        usrOriginal.setRoles(rolesEscolhidas);

        usuarioRepository.save(usrOriginal);

        redirectAttributes.addFlashAttribute("operacaoSucesso", true);
        return "redirect:/usuario/permissoes";
    }

    @GetMapping("/buscaUsuario")
    public String buscaUsuarioAgendamento(@RequestParam("nome") String busca, Model model, RedirectAttributes redirectAttributes){

        redirectAttributes.addFlashAttribute("nomeBusca", busca);
        redirectAttributes.addFlashAttribute("busca", true);
        return "redirect:/usuario/permissoes";
    }

    @PostMapping("/editarUsuario")
    public String editarAgendamento(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes){
        List<String> erros = validarUsuario(usuario, true);
        Usuario usrOriginal = usuarioRepository.findById(usuario.getId()).get();

        if(usrOriginal == null){
            return "redirect:/";
        }

        if(!usrOriginal.getEmail().equalsIgnoreCase(usuario.getEmail())){
            Usuario usrAux = usuarioRepository.getUsuarioByEmail(usuario.getEmail());
            if(usrAux != null) {
                erros.add("Email alterado já está cadastrado no sistema!");
            }
        }

        if(!usrOriginal.getCpf().equalsIgnoreCase(usuario.getCpf())){
            Usuario usrAux = usuarioRepository.getUsuarioByCpf(usuario.getCpf());
            if(usrAux != null) {
                erros.add("Esse cpf já está cadastrado em outro usuário!");
            }
        }

        if(usuario.getTrocarSenha() != null && usuario.getTrocarSenha()){
            if(!crypt.matches(usuario.getSenha(), usrOriginal.getSenha())){
                erros.add("Senha atual inválida");
            }
        }

        if(erros.size() > 0) {
            redirectAttributes.addFlashAttribute("message", erros);
            redirectAttributes.addFlashAttribute("usuario", UsuarioDTO.toDTO(usuario));

            return "redirect:/usuario/editar/"+usuario.getId();
        }

        usrOriginal.setContato(usuario.getContato());
        usrOriginal.setNome(usuario.getNome());
        usrOriginal.setCpf(usuario.getCpf());
        usrOriginal.setEmail(usuario.getEmail());

        if(usuario.getTrocarSenha() != null && usuario.getTrocarSenha() == true){
            usrOriginal.setSenha(crypt.encode(usuario.getNovaSenha()));
        }

        usuarioRepository.save(usrOriginal);

        redirectAttributes.addFlashAttribute("operacaoSucesso", true);
        return "redirect:/";
    }

    private List<String> validarUsuario(Usuario usuario, boolean editar) {
        List<String> erros = new ArrayList<>();
        Usuario usrAux = usuarioRepository.getUsuarioByLogin(usuario.getLogin());

        if (!editar && usrAux != null) {
            erros.add("Este login já está sendo utilizado!");
        }

        usrAux = usuarioRepository.getUsuarioByEmail(usuario.getEmail());
        if (!editar && usrAux != null) {
            erros.add("Email já cadastrado no sistema!");
        }

        usrAux = usuarioRepository.getUsuarioByCpf(usuario.getCpf());
        if (!editar && usrAux != null) {
            erros.add("Cpf já cadastrado!");
        }

        if (editar && usuario.getNome().strip().length() < 5) {
            erros.add("Nome muito curto");
        }

        return erros;
    }
}
