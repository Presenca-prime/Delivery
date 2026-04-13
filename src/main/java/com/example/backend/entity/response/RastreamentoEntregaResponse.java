package com.example.backend.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RastreamentoEntregaResponse {
    private Integer id;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDateTime dataHora;
    
    public static RastreamentoEntregaResponse fromEntity(rastreamento_entrega.RastreamentoEntrega rastreamento) {
        RastreamentoEntregaResponse response = new RastreamentoEntregaResponse();
        response.setId(rastreamento.getId());
        response.setLatitude(rastreamento.getLatitude());
        response.setLongitude(rastreamento.getLongitude());
        response.setDataHora(rastreamento.getDataHora());
        return response;
    }
}