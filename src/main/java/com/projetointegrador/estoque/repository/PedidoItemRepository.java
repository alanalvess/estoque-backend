package com.projetointegrador.estoque.repository;

import com.projetointegrador.estoque.model.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {

}
