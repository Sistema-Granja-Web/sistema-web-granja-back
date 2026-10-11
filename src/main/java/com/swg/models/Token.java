package com.swg.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    public Token() {}

    public LocalDateTime getDataCriacao() { return dataCriacao; }

    public Token(String token, Fornecedor fornecedor, boolean revogado, boolean expirado) {
        this.token = token;
        this.fornecedor = fornecedor;
        this.revogado = revogado;
        this.expirado = expirado;
    }
}
