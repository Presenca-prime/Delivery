package com.example.backend.dto.request;

import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class RastreamentoEntregaRequest {
    @NotNull(message = "ID do pedido é obrigatório")
    private Integer pedidoId;
    
    @NotNull(message = "Latitude é obrigatória")
    private BigDecimal latitude;
    
    @NotNull(message = "Longitude é obrigatória")
    private BigDecimal longitude;
}