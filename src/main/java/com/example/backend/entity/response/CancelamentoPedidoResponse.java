package com.example.backend.dto.response;

import com.example.backend.entity.cancelamento_pedido;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CancelamentoPedidoResponse {
    private Integer id;
    private UsuarioResponse solicitadoPor;
    private cancelamento_pedido.MotivoCancelamento motivo;
    private String descricaoMotivo;
    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataProcessamento;
    private cancelamento_pedido.StatusCancelamento status;
    private AdministradorResponse aprovadoPor;
    private BigDecimal taxaCancelamento;
    private Boolean reembolsoRealizado;
    private String observacoesAdmin;
    
    public static CancelamentoPedidoResponse fromEntity(cancelamento_pedido.CancelamentoPedido cancelamento) {
        CancelamentoPedidoResponse response = new CancelamentoPedidoResponse();
        response.setId(cancelamento.getId());
        response.setMotivo(cancelamento.getMotivo());
        response.setDescricaoMotivo(cancelamento.getDescricaoMotivo());
        response.setDataSolicitacao(cancelamento.getDataSolicitacao());
        response.setDataProcessamento(cancelamento.getDataProcessamento());
        response.setStatus(cancelamento.getStatus());
        response.setTaxaCancelamento(cancelamento.getTaxaCancelamento());
        response.setReembolsoRealizado(cancelamento.getReembolsoRealizado());
        response.setObservacoesAdmin(cancelamento.getObservacoesAdmin());
        return response;
    }
}