package com.tads.vdlab.repository;

import com.tads.vdlab.model.Role;
import com.tads.vdlab.model.Usuario;
import org.postgresql.util.PSQLException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE u.login = :login")
    Usuario getUsuarioByLogin(@Param("login") String username);

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    Usuario getUsuarioByEmail(@Param("email") String email);

    @Query("SELECT u FROM Usuario u WHERE u.cpf = :cpf")
    Usuario getUsuarioByCpf(@Param("cpf") String cpf);

    @Query("select u from Usuario u where u.codigoPlaca is not null")
    List<Usuario> findUsuariosComCodigoPlaca();

    @Query("SELECT r FROM Role r WHERE r.name = 'DISCENTE'")
    Role getRolePadraoUsuario();

    List<Usuario> findByAtivo(Boolean ativo);

    List<Usuario> findByRolesIsAndAtivoOOrderByNome(Role role, Boolean ativo);

    Usuario getByLoginEqualsAndAtivo(String login, Boolean ativo);

    @Query(value = "update usuarios set codigo_placa = null", nativeQuery = true)
    void resetPlacasUsuarios() throws PSQLException;
}
