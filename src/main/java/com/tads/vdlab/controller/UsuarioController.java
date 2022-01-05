package com.tads.vdlab.controller;

import com.tads.vdlab.model.Role;
import com.tads.vdlab.model.Usuario;
import com.tads.vdlab.repository.UsuarioRepository;
import com.tads.vdlab.util.UsuarioUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        List<String> errors = validarUsuario(usuario);
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

    private List<String> validarUsuario(Usuario usuario){
        List<String> erros = new ArrayList<>();
        Usuario usrAux = usuarioRepository.getUsuarioByLogin(usuario.getLogin());

        if(usrAux != null) {
            erros.add("Este login já está sendo utilizado!");
        }

        usrAux = usuarioRepository.getUsuarioByEmail(usuario.getEmail());
        if(usrAux != null) {
            erros.add("Email já cadastrado no sistema!");
        }

        usrAux = usuarioRepository.getUsuarioByCpf(usuario.getCpf());
        if(usrAux != null) {
            erros.add("Cpf já cadastrado!");
        }

        if(usuario.getNome().strip().length() < 5) {
            erros.add("Nome muito curto");
        }

        return erros;
    }


}
