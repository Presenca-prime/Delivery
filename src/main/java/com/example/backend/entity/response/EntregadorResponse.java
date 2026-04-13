package com.example.backend.dto.response;

import com.example.backend.entity.entregador;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class EntregadorResponse {
    private Integer id;
    private UsuarioResponse usuario;
    private String cnh;
    private entregador.TipoVeiculo tipoVeiculo;
    private String placaVeiculo;
    private String modeloVeiculo;
    private entregador.StatusEntregador status;
    private BigDecimal avaliacaoMedia;
    private Integer totalEntregas;
    private List<PedidoResponse> pedidos;
    
    public static EntregadorResponse fromEntity(entregador.Entregador entregador) {
        EntregadorResponse response = new EntregadorResponse();
        response.setId(entregador.getId());
        response.setCnh(entregador.getCnh());
        response.setTipoVeiculo(entregador.getTipoVeiculo());
        response.setPlacaVeiculo(entregador.getPlacaVeiculo());
        response.setModeloVeiculo(entregador.getModeloVeiculo());
        response.setStatus(entregador.getStatus());
        response.setAvaliacaoMedia(entregador.getAvaliacaoMedia());
        response.setTotalEntregas(entregador.getTotalEntregas());
        if (entregador.getUsuario() != null) {
            response.setUsuario(UsuarioResponse.fromEntity(entregador.getUsuario()));
        }
        return response;
    }
}