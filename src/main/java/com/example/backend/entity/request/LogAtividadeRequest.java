package com.example.backend.dto.request;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class LogAtividadeRequest {
    private Integer usuarioId;
    
    @NotBlank(message = "Ação é obrigatória")
    @Size(max = 200, message = "Ação deve ter no máximo 200 caracteres")
    private String acao;
    
    private String descricao;
    private String ipAddress;
}