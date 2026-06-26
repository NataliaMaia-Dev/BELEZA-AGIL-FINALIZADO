package com.belezaagil.repository;

import com.belezaagil.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {

    @Query("SELECT DISTINCT a FROM Agendamento a " +
           "LEFT JOIN FETCH a.horarios " +
           "LEFT JOIN FETCH a.cliente " +
           "LEFT JOIN FETCH a.servico " +
           "LEFT JOIN FETCH a.profissional " +
           "LEFT JOIN FETCH a.comanda " +
           "WHERE CAST(a.dataAgendamento AS date) = :data " +
           "ORDER BY a.dataAgendamento")
    List<Agendamento> findByData(@Param("data") LocalDate data);

    @Query("SELECT DISTINCT a FROM Agendamento a " +
           "LEFT JOIN FETCH a.horarios " +
           "LEFT JOIN FETCH a.cliente " +
           "LEFT JOIN FETCH a.servico " +
           "LEFT JOIN FETCH a.profissional " +
           "LEFT JOIN FETCH a.comanda " +
           "ORDER BY a.dataAgendamento")
    List<Agendamento> findAllWithDetails();

    @Query("SELECT DISTINCT a FROM Agendamento a " +
           "LEFT JOIN FETCH a.horarios " +
           "WHERE CAST(a.dataAgendamento AS date) = :data AND a.profissional.id = :profissionalId")
    List<Agendamento> findByDataAndProfissional(@Param("data") LocalDate data,
                                                 @Param("profissionalId") Integer profissionalId);

    @Modifying
    @Query("UPDATE Agendamento a SET a.finalizado = true WHERE a.comanda.id = :comandaId")
    void finalizarComanda(@Param("comandaId") Integer comandaId);

    @Modifying
    @Query("DELETE FROM Agendamento a WHERE a.comanda.id = :comandaId")
    void deleteByComandaId(@Param("comandaId") Integer comandaId);
}
