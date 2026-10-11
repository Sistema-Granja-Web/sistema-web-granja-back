package com.swg.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(unique = true, nullable = false, length = 500)
    public String token;

    public boolean revogado;
    public boolean expirado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_email")
    public Fornecedor fornecedor;

    public Token() {}

    public Token(String token, Fornecedor fornecedor, boolean revogado, boolean expirado) {
        this.token = token;
        this.fornecedor = fornecedor;
        this.revogado = revogado;
        this.expirado = expirado;
    }
}
