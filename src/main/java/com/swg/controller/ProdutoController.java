package com.swg.controller;

import com.swg.dto.ProdutoCadastroDTO;
import com.swg.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produto")
@CrossOrigin(origins = "*")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @PostMapping("/cadastro")
    public ResponseEntity<Void> cadastrar(@Valid @RequestBody ProdutoCadastroDTO dto) {
        String emailFornecedorLogado = SecurityContextHolder.getContext().getAuthentication().getName();

        service.cadastrar(dto, emailFornecedorLogado);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}