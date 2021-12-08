package com.tads.vdlab.repository;

import com.tads.vdlab.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE u.login = :login")
    public Usuario getUsuarioByLogin(@Param("login") String username);

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario getUsuarioByEmail(@Param("email") String email);
}
