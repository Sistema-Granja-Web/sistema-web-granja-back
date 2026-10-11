package com.swg.controller;

import com.swg.dto.ProdutoCadastroDTO;
import com.swg.dtos.ProdutoAtualizacaoDTO;
import com.swg.dtos.ProdutoResponseDTO;
import com.swg.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodos() {
        List<ProdutoResponseDTO> produtos = service.listarTodos();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/meus-produtos")
    public ResponseEntity<List<ProdutoResponseDTO>> listarMeusProdutos() {
        String emailFornecedorLogado = SecurityContextHolder.getContext().getAuthentication().getName();

        List<ProdutoResponseDTO> meusProdutos = service.listarMeusProdutos(emailFornecedorLogado);
        return ResponseEntity.ok(meusProdutos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoAtualizacaoDTO dto) {
        String emailFornecedorLogado = SecurityContextHolder.getContext().getAuthentication().getName();

        service.atualizar(id, dto, emailFornecedorLogado);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        String emailFornecedorLogado = SecurityContextHolder.getContext().getAuthentication().getName();

        service.deletar(id, emailFornecedorLogado);

        return ResponseEntity.noContent().build();
    }
}