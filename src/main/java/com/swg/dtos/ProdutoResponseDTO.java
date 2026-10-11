package com.swg.dtos;

import com.swg.models.Produto;
import java.math.BigDecimal;

public class ProdutoResponseDTO {

    private Long id;
    private String nome;
    private String imagem;
    private BigDecimal valor;
    private String unidade;
    private Boolean possuiEstoque;
    private FornecedorResumoDTO fornecedor;

    public ProdutoResponseDTO(Produto produto) {
        this.id = produto.getId();
        this.nome = produto.getNome();
        this.imagem = produto.getImagem();
        this.valor = produto.getValor();
        this.unidade = produto.getUnidade();
        this.possuiEstoque = produto.getPossuiEstoque();

        // Mapeia os dados do fornecedor para o sub-objeto
        this.fornecedor = new FornecedorResumoDTO(
                produto.getFornecedor().getNome(),
                produto.getFornecedor().getTelefone(),
                produto.getFornecedor().getEndereco()
        );
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getImagem() { return imagem; }
    public BigDecimal getValor() { return valor; }
    public String getUnidade() { return unidade; }
    public Boolean getPossuiEstoque() { return possuiEstoque; }
    public FornecedorResumoDTO getFornecedor() { return fornecedor; }

    public static class FornecedorResumoDTO {
        private String nome;
        private String contato;
        private String localizacao;

        public FornecedorResumoDTO(String nome, String contato, String localizacao) {
            this.nome = nome;
            this.contato = contato;
            this.localizacao = localizacao;
        }

        public String getNome() { return nome; }
        public String getContato() { return contato; }
        public String getLocalizacao() { return localizacao; }
    }
}