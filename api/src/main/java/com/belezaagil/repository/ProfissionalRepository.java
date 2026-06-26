package com.belezaagil.repository;

import com.belezaagil.entity.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfissionalRepository extends JpaRepository<Profissional, Integer> {

    Optional<Profissional> findByCpf(String cpf);

    @Query("SELECT p FROM Profissional p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.funcao) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Profissional> search(@Param("search") String search);

    boolean existsByCpfAndIdNot(String cpf, Integer id);

    boolean existsByCpf(String cpf);
}
