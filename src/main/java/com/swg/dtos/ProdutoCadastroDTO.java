package com.swg.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class ProdutoCadastroDTO {

    @NotBlank(message = "O nome do produto é obrigatório.")
    private String nome;

    @NotBlank(message = "A imagem do produto (Base64) é obrigatória.")
    private String imagem;

    @NotNull(message = "O valor é obrigatório.")
    @Positive(message = "O valor deve ser maior que zero.")
    private BigDecimal valor;

    @NotBlank(message = "A unidade de medida é obrigatória.")
    @Pattern(regexp = "^(Dúzia|Cartela|Unidade)$", message = "A unidade deve ser exclusivamente: Dúzia, Cartela ou Unidade.")
    private String unidade;

    @NotNull(message = "A informação de estoque é obrigatória.")
    private Boolean possuiEstoque;

    // Getters
    public String getNome() { return nome; }
    public String getImagem() { return imagem; }
    public BigDecimal getValor() { return valor; }
    public String getUnidade() { return unidade; }
    public Boolean getPossuiEstoque() { return possuiEstoque; }
}