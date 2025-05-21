package com.projetointegrador.estoque.repository;

import com.projetointegrador.estoque.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

    Optional<Fornecedor> findByEmail(String email);

    Optional<Fornecedor> findByNomeIgnoreCase(String nome);

    List<Fornecedor> findAllByCnpjContainingIgnoreCase(String cnpj);

    List<Fornecedor> findAllByNomeContainingIgnoreCase(String nome);
}
