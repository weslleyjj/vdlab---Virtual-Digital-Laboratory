package com.tads.vdlab.repository;

import com.tads.vdlab.model.Agendamento;
import com.tads.vdlab.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query("select a from Agendamento a where a.usuario.id = :idUsuario")
    List<Agendamento> buscarAgendamentoByIdUsuario(Long idUsuario);

    List<Agendamento> findAgendamentosByUsuarioAndAtivoOrderByDataAgendadaDesc(Usuario usuario, Boolean ativo);

    @Query("select count(a) from Agendamento a where a.ativo = true " +
            "and a.id <> :idAgendamento " +
            "and (a.dataAgendada between :dataInicial and :dataExpirada)")
    Integer countAgendamentosBetweenDates(LocalDateTime dataInicial, LocalDateTime dataExpirada, Long idAgendamento);

    @Query(value = "select count(a) from agendamento a left join usuarios u on u.id = a.usuario_id " +
            "where a.ativo = true " +
            "and a.id <> ?4 " +
            "and u.id = ?3 " +
            "and ((a.data_agendada between ?1 and ?2) or " +
            "(((a.data_agendada + (a.tempo_sessao * interval '1 minute') >= ?1)) and " +
            "((a.data_agendada + (a.tempo_sessao * interval '1 minute') <= ?2))))", nativeQuery = true)
    Integer countAgendamentosUsuarioBetweenDates(LocalDateTime dataInicial, LocalDateTime dataExpirada, Long idUsuario, Long idAgendamento);

    List<Agendamento> findAgendamentosByCadastranteAndAtivoOrderByDataAgendadaDesc(Usuario cadastrante, Boolean ativo);
}
