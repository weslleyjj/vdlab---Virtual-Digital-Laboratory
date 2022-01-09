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

    @Query(value = "select count(a) from agendamento a left join usuarios u on u.id = a.usuario_id " +
            "where a.ativo = true " +
            "and u.id = ?1 " +
            "and (?2 between a.data_agendada and (a.data_agendada + (a.tempo_sessao * interval '1 minute'))) ", nativeQuery = true)
    Integer buscarAgendamentoByIdUsuario(Long idUsuario, LocalDateTime dataAtual);

    List<Agendamento> findAgendamentosByUsuarioAndAtivoOrderByDataAgendadaDesc(Usuario usuario, Boolean ativo);

    @Query(value = "select count(a) from agendamento a " +
            "where a.ativo = true " +
            "and a.id <> ?3 " +
            "and (?1 between a.data_agendada and (a.data_agendada + (a.tempo_sessao * interval '1 minute')) or " +
            "(?2 between a.data_agendada and (a.data_agendada + (a.tempo_sessao * interval '1 minute'))))", nativeQuery = true)
    Integer countAgendamentosBetweenDates(LocalDateTime dataInicial, LocalDateTime dataExpirada, Long idAgendamento);

    @Query(value = "select count(a) from agendamento a left join usuarios u on u.id = a.usuario_id " +
            "where a.ativo = true " +
            "and a.id <> ?4 " +
            "and u.id = ?3 " +
            "and (?1 between a.data_agendada and (a.data_agendada + (a.tempo_sessao * interval '1 minute')) or " +
            "(?2 between a.data_agendada and (a.data_agendada + (a.tempo_sessao * interval '1 minute'))))", nativeQuery = true)
    Integer countAgendamentosUsuarioBetweenDates(LocalDateTime dataInicial, LocalDateTime dataExpirada, Long idUsuario, Long idAgendamento);

    List<Agendamento> findAgendamentosByCadastranteAndAtivoOrderByDataAgendadaDesc(Usuario cadastrante, Boolean ativo);

    @Query(value = "select count(a) from agendamento a left join usuarios u on u.id = a.usuario_id " +
            "where a.ativo = true " +
            "and u.id = ?1 " +
            "and ?2 between a.data_agendada and (a.data_agendada + (a.tempo_sessao * interval '1 minute'))", nativeQuery = true)
    Integer buscarAgendamentoEntrePeriodoUsuario(Long idUsuario, LocalDateTime data);
}
