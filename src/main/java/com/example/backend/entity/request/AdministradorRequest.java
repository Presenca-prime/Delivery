package com.example.backend.dto.request;

import com.example.backend.entity.administrador;
import lombok.Data;
import javax.validation.constraints.*;

@Data
public class AdministradorRequest {
    @NotNull(message = "ID do usuário é obrigatório")
    private Integer usuarioId;
    
    @NotBlank(message = "Cargo é obrigatório")
    @Size(max = 100, message = "Cargo deve ter no máximo 100 caracteres")
    private String cargo;
    
    private administrador.NivelAcesso nivelAcesso;
}