package com.swg.config;

import com.swg.models.Fornecedor;
import com.swg.repositories.FornecedorRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeederConfig {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.senha}")
    private String adminSenha;

    @Bean
    public CommandLineRunner seedAdmin(FornecedorRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repository.findById(adminEmail).isEmpty()) {
                Fornecedor admin = new Fornecedor();
                admin.setNome("Administrador Principal");
                admin.setCnpj("99999999999999");
                admin.setEndereco("Sede da Granja");
                admin.setTelefone("99999999999");

                admin.setEmail(adminEmail);
                admin.setSenha(passwordEncoder.encode(adminSenha));

                repository.save(admin);
                System.out.println("Perfil de Admin (" + adminEmail + ") criado com sucesso na base de dados!");
            }
        };
    }
}