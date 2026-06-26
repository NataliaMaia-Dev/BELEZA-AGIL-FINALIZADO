package com.belezaagil.repository;

import com.belezaagil.entity.Comanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ComandaRepository extends JpaRepository<Comanda, Integer> {

    @Query("SELECT COALESCE(MAX(c.id), 0) + 1 FROM Comanda c")
    int getProximoNumero();
}
