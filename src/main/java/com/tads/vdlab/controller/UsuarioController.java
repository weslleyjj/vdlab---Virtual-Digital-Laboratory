package com.tads.vdlab.controller;

import com.tads.vdlab.controller.dto.UsuarioDTO;
import com.tads.vdlab.model.Agendamento;
import com.tads.vdlab.model.Role;
import com.tads.vdlab.model.Usuario;
import com.tads.vdlab.repository.UsuarioRepository;
import com.tads.vdlab.util.UsuarioUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private UsuarioRepository usuarioRepository;
    private BCryptPasswordEncoder crypt;

    public UsuarioController(UsuarioRepository usuarioRepository){
        crypt = new BCryptPasswordEncoder();
        this.usuarioRepository = usuarioRepository;
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

    private List<String> validarUsuario(Usuario usuario, boolean editar){
        List<String> erros = new ArrayList<>();
        Usuario usrAux = usuarioRepository.getUsuarioByLogin(usuario.getLogin());

        if(!editar && usrAux != null) {
            erros.add("Este login já está sendo utilizado!");
        }

        usrAux = usuarioRepository.getUsuarioByEmail(usuario.getEmail());
        if(!editar && usrAux != null) {
            erros.add("Email já cadastrado no sistema!");
        }

        usrAux = usuarioRepository.getUsuarioByCpf(usuario.getCpf());
        if(!editar && usrAux != null) {
            erros.add("Cpf já cadastrado!");
        }

        if(editar && usuario.getNome().strip().length() < 5) {
            erros.add("Nome muito curto");
        }

        return erros;
    }


}
