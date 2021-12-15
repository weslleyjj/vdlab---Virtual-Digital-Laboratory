package com.tads.vdlab.controller.dto;

import com.tads.vdlab.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public static Usuario fromDTO(UsuarioDTO usuario){
        return Usuario.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .cpf(usuario.getCpf())
                .contato(usuario.getContato())
                .email(usuario.getEmail())
                .build();
    }

    public static UsuarioDTO toDTO(Usuario usuario){
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .cpf(usuario.getCpf())
                .contato(usuario.getContato())
                .email(usuario.getEmail())
                .build();
    }
}
