package com.projetointegrador.estoque.repository;

import com.projetointegrador.estoque.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findAllByNomeContainingIgnoreCase(String nome);

    Optional<Categoria> findByNomeIgnoreCase(String nome);
}

