package com.tads.vdlab.repository;

import com.tads.vdlab.model.Agendamento;
import com.tads.vdlab.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query("select a from Agendamento a where a.usuario.id = :idUsuario")
    List<Agendamento> buscarAgendamentoByIdUsuario(Long idUsuario);

    List<Agendamento> findAgendamentosByUsuarioAndAtivo(Usuario usuario, Boolean ativo);

    List<Agendamento> findAgendamentosByCadastranteAndAtivoOrderByDataAgendadaDesc(Usuario cadastrante, Boolean ativo);
}
