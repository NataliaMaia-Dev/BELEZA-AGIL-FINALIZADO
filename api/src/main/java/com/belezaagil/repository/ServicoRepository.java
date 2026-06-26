package com.belezaagil.repository;

import com.belezaagil.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Integer> {

    @Query("SELECT s FROM Servico s WHERE LOWER(s.nome) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Servico> search(@Param("search") String search);
}
