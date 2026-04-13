package com.example.backend.dto.request;

import com.example.backend.entity.cancelamento_pedido;
import lombok.Data;
import javax.validation.constraints.*;

@Data
public class CancelamentoPedidoRequest {
    @NotNull(message = "ID do pedido é obrigatório")
    private Integer pedidoId;
    
    @NotNull(message = "Motivo é obrigatório")
    private cancelamento_pedido.MotivoCancelamento motivo;
    
    private String descricaoMotivo;
    
    private String observacoesAdmin;
    private BigDecimal taxaCancelamento;
}