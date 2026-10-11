package com.swg.controller;

import com.swg.dtos.FornecedorCadastroDTO;
import com.swg.dtos.FornecedorResponseDTO;
import com.swg.services.FornecedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fornecedor")
@CrossOrigin(origins = "*")
public class FornecedorController {

    @Autowired
    private FornecedorService service;

    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> cadastrar(@Valid @RequestBody FornecedorCadastroDTO dto) {
        service.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<FornecedorResponseDTO>> listarTodos() {
        String emailRequisitante = SecurityContextHolder.getContext().getAuthentication().getName();

        List<FornecedorResponseDTO> fornecedores = service.listarTodos(emailRequisitante);

        return ResponseEntity.ok(fornecedores);
    }
}
