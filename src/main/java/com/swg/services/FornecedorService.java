package com.swg.services;

import com.swg.dtos.FornecedorAtualizacaoDTO;
import com.swg.dtos.FornecedorCadastroDTO;
import com.swg.dtos.FornecedorResponseDTO;
import com.swg.exceptions.AcessoNegadoException;
import com.swg.exceptions.NegocioException;
import com.swg.models.Fornecedor;
import com.swg.models.Produto;
import com.swg.models.Token;
import com.swg.repositories.FornecedorRepository;
import com.swg.repositories.ProdutoRepository;
import com.swg.repositories.TokenRepository;
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
    private TokenRepository tokenRepository;

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

    @Transactional
    public void atualizarCadastroCompleto(String emailLogado, FornecedorAtualizacaoDTO dto) {
        Fornecedor fornecedorAtual = repository.findById(emailLogado)
                .orElseThrow(() -> new NegocioException("Fornecedor não encontrado."));

        if (!fornecedorAtual.getAtivo()) {
            throw new NegocioException("A sua conta encontra-se inativa.");
        }

        if (!fornecedorAtual.getCnpj().equals(dto.getCnpj()) && repository.findByCnpj(dto.getCnpj()).isPresent()) {
            throw new NegocioException("O CNPJ informado já se encontra registado no sistema.");
        }

        boolean mudouSenha = dto.getSenha() != null && !dto.getSenha().trim().isEmpty();
        if (mudouSenha && dto.getSenha().length() < 4) {
            throw new NegocioException("A nova senha deve ter no mínimo 4 caracteres.");
        }

        String senhaFinal = mudouSenha ? passwordEncoder.encode(dto.getSenha()) : fornecedorAtual.getSenha();
        boolean mudouEmail = !fornecedorAtual.getEmail().equals(dto.getEmail());

        if (mudouEmail) {
            if (repository.existsById(dto.getEmail())) {
                throw new NegocioException("O novo e-mail informado já está em uso.");
            }

            Fornecedor novoFornecedor = new Fornecedor();
            novoFornecedor.setEmail(dto.getEmail());
            novoFornecedor.setNome(dto.getNome());
            novoFornecedor.setCnpj(dto.getCnpj());
            novoFornecedor.setEndereco(dto.getEndereco());
            novoFornecedor.setTelefone(dto.getTelefone());
            novoFornecedor.setSenha(senhaFinal);
            novoFornecedor.setAtivo(fornecedorAtual.getAtivo());

            novoFornecedor = repository.save(novoFornecedor);

            List<Produto> produtos = produtoRepository.findByFornecedorEmail(emailLogado);
            Fornecedor finalNovoFornecedor = novoFornecedor;
            produtos.forEach(p -> p.setFornecedor(finalNovoFornecedor));
            authService.revogarTodosTokensDoFornecedor(fornecedorAtual);
            produtoRepository.saveAll(produtos);

            repository.delete(fornecedorAtual);
        } else {
            fornecedorAtual.setNome(dto.getNome());
            fornecedorAtual.setCnpj(dto.getCnpj());
            fornecedorAtual.setEndereco(dto.getEndereco());
            fornecedorAtual.setTelefone(dto.getTelefone());
            fornecedorAtual.setSenha(senhaFinal);

            repository.save(fornecedorAtual);

            if (mudouSenha) {
                authService.revogarTodosTokensDoFornecedor(fornecedorAtual);
            }
        }
    }
}