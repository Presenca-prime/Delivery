package com.example.backend.dto.response;

import com.example.backend.entity.pagamento;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PagamentoResponse {
    private Integer id;
    private pagamento.MetodoPagamento metodo;
    private pagamento.StatusPagamento status;
    private BigDecimal valor;
    private LocalDateTime dataPagamento;
    private String codigoTransacao;
    
    public static PagamentoResponse fromEntity(pagamento.Pagamento pagamento) {
        PagamentoResponse response = new PagamentoResponse();
        response.setId(pagamento.getId());
        response.setMetodo(pagamento.getMetodo());
        response.setStatus(pagamento.getStatus());
        response.setValor(pagamento.getValor());
        response.setDataPagamento(pagamento.getDataPagamento());
        response.setCodigoTransacao(pagamento.getCodigoTransacao());
        return response;
    }
}