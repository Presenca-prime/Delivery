package com.example.backend.dto.request;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UsuarioRequestDTO {
    @NotBlank
    private String nome;
    
    @Email
    @NotBlank
    private String email;
    
    @NotBlank
    private String senha;
    
    @NotBlank
    private String cpf;
    
    private String telefone;
    private String tipoUsuario;
}