package com.example.backend.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemPedidoResponse {
    private Integer id;
    private ProdutoResponse produto;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private String observacoes;
    private BigDecimal subtotal;
    
    public static ItemPedidoResponse fromEntity(item_pedido.ItemPedido item) {
        ItemPedidoResponse response = new ItemPedidoResponse();
        response.setId(item.getId());
        response.setQuantidade(item.getQuantidade());
        response.setPrecoUnitario(item.getPrecoUnitario());
        response.setObservacoes(item.getObservacoes());
        if (item.getPrecoUnitario() != null && item.getQuantidade() != null) {
            response.setSubtotal(item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())));
        }
        if (item.getProduto() != null) {
            response.setProduto(ProdutoResponse.fromEntity(item.getProduto()));
        }
        return response;
    }
}