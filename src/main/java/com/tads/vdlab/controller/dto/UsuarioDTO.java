package com.tads.vdlab.controller.dto;

import com.tads.vdlab.model.Role;
import com.tads.vdlab.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDTO {

    Long id;
    String nome;
    String cpf;
    String contato;
    String email;
    List<Role> roles;

    public static Usuario fromDTO(UsuarioDTO usuario){
        return Usuario.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .cpf(usuario.getCpf())
                .contato(usuario.getContato())
                .email(usuario.getEmail())
                .roles(usuario.getRoles().stream().collect(Collectors.toSet()))
                .build();
    }

    public static UsuarioDTO toDTO(Usuario usuario){
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .cpf(usuario.getCpf())
                .contato(usuario.getContato())
                .email(usuario.getEmail())
                .roles(usuario.getRoles().stream().collect(Collectors.toList()))
                .build();
    }
}
