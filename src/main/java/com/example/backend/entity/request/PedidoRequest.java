package com.example.backend.dto.request;

import com.example.backend.entity.pedido;
import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PedidoRequest {
    @NotNull(message = "ID do cliente é obrigatório")
    private Integer clienteId;
    
    @NotNull(message = "ID do restaurante é obrigatório")
    private Integer restauranteId;
    
    private Integer entregadorId;
    
    @NotNull(message = "ID do endereço de entrega é obrigatório")
    private Integer enderecoEntregaId;
    
    @NotNull(message = "Valor subtotal é obrigatório")
    private BigDecimal valorSubtotal;
    
    @NotNull(message = "Taxa de entrega é obrigatória")
    private BigDecimal valorTaxaEntrega;
    
    private String observacoes;
    private Integer tempoEstimadoEntrega;
    
    @NotEmpty(message = "Pedido deve ter pelo menos um item")
    private List<ItemPedidoRequest> itens;
}

@Data
class ItemPedidoRequest {
    @NotNull(message = "ID do produto é obrigatório")
    private Integer produtoId;
    
    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser pelo menos 1")
    private Integer quantidade;
    
    private String observacoes;
}