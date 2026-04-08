package com.example.backend.dto.request;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class SessaoLoginRequest {
    @NotNull(message = "ID do usuário é obrigatório")
    private Integer usuarioId;
    
    @NotBlank(message = "Token é obrigatório")
    private String token;
    
    private String ipAddress;
    private String dispositivo;
}