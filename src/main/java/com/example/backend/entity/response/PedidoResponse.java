package com.example.backend.dto.response;

import com.example.backend.entity.pedido;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoResponse {
    private Integer id;
    private ClienteResponse cliente;
    private RestauranteResponse restaurante;
    private EntregadorResponse entregador;
    private EnderecoResponse enderecoEntrega;
    private LocalDateTime dataPedido;
    private pedido.StatusPedido status;
    private BigDecimal valorSubtotal;
    private BigDecimal valorTaxaEntrega;
    private BigDecimal valorTotal;
    private Integer tempoEstimadoEntrega;
    private String observacoes;
    private Integer avaliacao;
    private List<ItemPedidoResponse> itens;
    private PagamentoResponse pagamento;
    private CancelamentoPedidoResponse cancelamento;
    
    public static PedidoResponse fromEntity(pedido.Pedido pedido) {
        PedidoResponse response = new PedidoResponse();
        response.setId(pedido.getId());
        response.setDataPedido(pedido.getDataPedido());
        response.setStatus(pedido.getStatus());
        response.setValorSubtotal(pedido.getValorSubtotal());
        response.setValorTaxaEntrega(pedido.getValorTaxaEntrega());
        response.setValorTotal(pedido.getValorTotal());
        response.setTempoEstimadoEntrega(pedido.getTempoEstimadoEntrega());
        response.setObservacoes(pedido.getObservacoes());
        response.setAvaliacao(pedido.getAvaliacao());
        return response;
    }
}