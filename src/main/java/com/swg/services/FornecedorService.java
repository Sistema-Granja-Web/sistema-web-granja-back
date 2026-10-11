package com.swg.services;

import com.swg.dtos.FornecedorCadastroDTO;
import com.swg.dtos.FornecedorResponseDTO;
import com.swg.exceptions.AcessoNegadoException;
import com.swg.exceptions.NegocioException;
import com.swg.models.Fornecedor;
import com.swg.repositories.FornecedorRepository;
import com.swg.repositories.ProdutoRepository;
import com.swg.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository repository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthService authService;

    @Value("${admin.email:admin@admin.com}")
    private String adminEmail;

    public void cadastrar(FornecedorCadastroDTO dto) {
        if (repository.existsById(dto.getEmail())) {
            throw new NegocioException("Já existe um utilizador cadastrado com este e-mail.");
        }
        if (repository.findByCnpj(dto.getCnpj()).isPresent()) {
            throw new NegocioException("O CNPJ informado já se encontra registado no sistema.");
        }

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(dto.getNome());
        fornecedor.setCnpj(dto.getCnpj());
        fornecedor.setEndereco(dto.getEndereco());
        fornecedor.setTelefone(dto.getTelefone());
        fornecedor.setEmail(dto.getEmail());
        fornecedor.setSenha(passwordEncoder.encode(dto.getSenha()));

        repository.save(fornecedor);
    }

    public List<FornecedorResponseDTO> listarTodos(String emailRequisitante) {
        if (!adminEmail.equals(emailRequisitante)) {
            throw new AcessoNegadoException("Acesso restrito. Apenas o administrador possui permissão para esta funcionalidade.");
        }

        return repository.findAll().stream()
                .map(FornecedorResponseDTO::new)
                .toList();
    }

    @Transactional
    public void exclusaoLogica(String emailAlvo, String emailRequisitante) {
        if (!adminEmail.equals(emailRequisitante)) {
            throw new AcessoNegadoException("Acesso restrito. Apenas o administrador pode excluir fornecedores.");
        }

        Fornecedor fornecedor = repository.findById(emailAlvo)
                .orElseThrow(() -> new NegocioException("Fornecedor não encontrado."));

        if (!fornecedor.getAtivo()) {
            throw new NegocioException("Este fornecedor já se encontra inativo.");
        }

        fornecedor.setAtivo(false);
        repository.save(fornecedor);

        produtoRepository.inativarEstoqueByFornecedorEmail(emailAlvo);

        authService.revogarTodosTokensDoFornecedor(fornecedor);
    }


}