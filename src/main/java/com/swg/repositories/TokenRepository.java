package com.swg.repositories;

import com.swg.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByToken(String token);

    @Query("SELECT t FROM Token t INNER JOIN t.fornecedor f WHERE f.email = :email AND (t.expirado = false OR t.revogado = false)")
    List<Token> findAllTokensValidosByFornecedor(@Param("email") String email);
}