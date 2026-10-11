package com.swg.services;

import com.swg.dtos.LoginDTO;
import com.swg.exceptions.NegocioException;
import com.swg.models.Fornecedor;
import com.swg.models.Token;
import com.swg.repositories.FornecedorRepository;
import com.swg.repositories.TokenRepository;
import com.swg.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private FornecedorRepository repository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public Map<String, String> autenticar(LoginDTO dto) {
        Fornecedor fornecedor = repository.findById(dto.getEmail())
                .orElseThrow(() -> new NegocioException("E-mail ou senha inválidos."));

        if (!passwordEncoder.matches(dto.getSenha(), fornecedor.getSenha())) {
            throw new NegocioException("E-mail ou senha inválidos.");
        }

        // 1. Invalida qualquer sessão ativa (token) existente na base de dados
        revogarTodosTokensDoFornecedor(fornecedor);

        // 2. Gera o novo token limpo
        String jwtToken = jwtService.gerarToken(fornecedor);

        // 3. Guarda o novo token como ativo
        Token token = new Token(jwtToken, fornecedor, false, false);
        tokenRepository.save(token);

        return Map.of(
                "mensagem", "Login realizado com sucesso!",
                "email", fornecedor.getEmail(),
                "token", jwtToken
        );
    }

    public void logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String jwt = authHeader.substring(7);

        var tokenArmazenado = tokenRepository.findByToken(jwt).orElse(null);

        if (tokenArmazenado != null) {
            tokenArmazenado.revogado = true;
            tokenArmazenado.expirado = true;
            tokenRepository.save(tokenArmazenado);
        }
    }


    private void revogarTodosTokensDoFornecedor(Fornecedor fornecedor) {
        var tokensValidos = tokenRepository.findAllTokensValidosByFornecedor(fornecedor.getEmail());
        if (tokensValidos.isEmpty()) {
            return;
        }

        tokensValidos.forEach(token -> {
            token.revogado = true;
            token.expirado = true;
        });

        tokenRepository.saveAll(tokensValidos);
    }
}
