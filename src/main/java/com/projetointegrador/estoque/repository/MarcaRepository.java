package com.projetointegrador.estoque.repository;

import com.projetointegrador.estoque.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {

    Optional<Marca> findByNomeIgnoreCase(String nome);

    List<Marca> findAllByNomeContainingIgnoreCase(String nome);
}
