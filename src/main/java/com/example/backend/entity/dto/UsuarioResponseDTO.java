package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Integer id;
    private String nome;
    private String email;
    private String telefone;
    private String tipoUsuario;
    private Boolean ativo;
    private LocalDateTime dataCadastro;
}