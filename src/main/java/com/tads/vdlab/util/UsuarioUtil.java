package com.tads.vdlab.util;

import com.tads.vdlab.model.Usuario;
import com.tads.vdlab.repository.UsuarioRepository;

import java.security.Principal;

public class UsuarioUtil {

    public static Usuario getUsuarioLogado(Principal principal, UsuarioRepository usuarioRepository){
        Usuario u = usuarioRepository.getByLoginEqualsAndAtivo(principal.getName(), true);
        return u;
    }
}
