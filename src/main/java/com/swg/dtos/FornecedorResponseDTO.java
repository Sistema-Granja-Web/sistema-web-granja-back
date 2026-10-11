package com.swg.dtos;

import com.swg.models.Fornecedor;

public class FornecedorResponseDTO {

    private String nome;
    private String cnpj;
    private String endereco;
    private String telefone;
    private String email;

    public FornecedorResponseDTO(Fornecedor fornecedor) {
        this.nome = fornecedor.getNome();
        this.cnpj = fornecedor.getCnpj();
        this.endereco = fornecedor.getEndereco();
        this.telefone = fornecedor.getTelefone();
        this.email = fornecedor.getEmail();
    }

    // Getters
    public String getNome() { return nome; }
    public String getCnpj() { return cnpj; }
    public String getEndereco() { return endereco; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
}
