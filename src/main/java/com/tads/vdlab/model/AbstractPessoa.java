package com.tads.vdlab.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class AbstractPessoa extends AbstractEntidade {
    @NotNull
    String nome;
    @NotNull
    String cpf;
    @NotNull
    String contato;
    @NotNull
    String email;
}
