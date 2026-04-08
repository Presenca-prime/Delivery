package com.example.backend.dto.request;

import lombok.Data;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class ClienteRequest {
    @NotNull(message = "ID do usuário é obrigatório")
    private Integer usuarioId;
    
    private LocalDate dataNascimento;
    
    private String preferenciasPagamento;
    
    private String dadosPagamentoCript;
}