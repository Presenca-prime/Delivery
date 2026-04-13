package com.example.backend.dto.request;

import com.example.backend.entity.pagamento;
import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class PagamentoRequest {
    @NotNull(message = "ID do pedido é obrigatório")
    private Integer pedidoId;
    
    @NotNull(message = "Método de pagamento é obrigatório")
    private pagamento.MetodoPagamento metodo;
    
    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal valor;
    
    private String codigoTransacao;
    private String dadosPagamentoCript;
}