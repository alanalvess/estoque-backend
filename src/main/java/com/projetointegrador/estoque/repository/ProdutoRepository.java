package com.projetointegrador.estoque.repository;

import com.projetointegrador.estoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findByNomeIgnoreCase(String nome);

    List<Produto> findAllByNomeContainingIgnoreCase(String nome); // NOVO

    List<Produto> findAllByCodigoContainingIgnoreCase(String codigo);
}
