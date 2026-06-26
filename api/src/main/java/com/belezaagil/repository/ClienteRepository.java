package com.belezaagil.repository;

import com.belezaagil.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Optional<Cliente> findByCpf(String cpf);

    @Query("SELECT c FROM Cliente c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :search, '%')) OR c.cpf LIKE CONCAT('%', :search, '%')")
    List<Cliente> search(@Param("search") String search);

    boolean existsByCpfAndIdNot(String cpf, Integer id);

    boolean existsByCpf(String cpf);
}
