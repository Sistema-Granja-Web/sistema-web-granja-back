package com.swg.repositories;

import com.swg.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByFornecedorEmail(String email);

    @Modifying
    @Query("UPDATE Produto p SET p.possuiEstoque = false WHERE p.fornecedor.email = :emailFornecedor")
    void inativarEstoqueByFornecedorEmail(@Param("emailFornecedor") String emailFornecedor);
}