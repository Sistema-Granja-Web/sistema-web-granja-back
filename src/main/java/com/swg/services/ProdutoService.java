package com.swg.services;

import com.swg.dto.ProdutoCadastroDTO;
import com.swg.dtos.ProdutoResponseDTO;
import com.swg.exceptions.NegocioException;
import com.swg.models.Fornecedor;
import com.swg.models.Produto;
import com.swg.repositories.FornecedorRepository;
import com.swg.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    public void cadastrar(ProdutoCadastroDTO dto, String emailFornecedorLogado) {
        Fornecedor fornecedor = fornecedorRepository.findById(emailFornecedorLogado)
                .orElseThrow(() -> new NegocioException("Fornecedor não encontrado na base de dados."));

        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setImagem(dto.getImagem());
        produto.setValor(dto.getValor());
        produto.setUnidade(dto.getUnidade());
        produto.setPossuiEstoque(dto.getPossuiEstoque());

        produto.setFornecedor(fornecedor);

        produto = produtoRepository.save(produto);
    }

    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(ProdutoResponseDTO::new)
                .toList(); // .toList() é nativo do Java 16+
    }

    public List<ProdutoResponseDTO> listarMeusProdutos(String emailFornecedorLogado) {
        return produtoRepository.findByFornecedorEmail(emailFornecedorLogado).stream()
                .map(ProdutoResponseDTO::new)
                .toList();
    }
}
