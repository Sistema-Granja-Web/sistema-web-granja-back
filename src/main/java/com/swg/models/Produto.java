package com.swg.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String imagem;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private String unidade;

    @Column(nullable = false)
    private Boolean possuiEstoque;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_email", nullable = false)
    private Fornecedor fornecedor;

    public Produto() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getImagem() { return imagem; }
    public void setImagem(String imagem) { this.imagem = imagem; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }

    public Boolean getPossuiEstoque() { return possuiEstoque; }
    public void setPossuiEstoque(Boolean possuiEstoque) { this.possuiEstoque = possuiEstoque; }

    public Fornecedor getFornecedor() { return fornecedor; }
    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }
}