package com.swg.repositories;

import com.swg.models.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FornecedorRepository extends JpaRepository<Fornecedor, String> {
    Optional<Fornecedor> findByCnpj(String cnpj);
}
