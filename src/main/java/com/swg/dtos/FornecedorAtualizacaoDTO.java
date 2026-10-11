package com.swg.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class FornecedorAtualizacaoDTO {

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @NotBlank(message = "O CNPJ é obrigatório.")
    private String cnpj;

    @NotBlank(message = "O endereço é obrigatório.")
    private String endereco;

    @NotBlank(message = "O telefone é obrigatório.")
    private String telefone;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Formato de e-mail inválido.")
    private String email;

    private String senha;

    public String getNome() { return nome; }
    public String getCnpj() { return cnpj; }
    public String getEndereco() { return endereco; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
}