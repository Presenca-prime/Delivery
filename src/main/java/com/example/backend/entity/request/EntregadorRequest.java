package com.example.backend.dto.request;

import com.example.backend.entity.entregador;
import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class EntregadorRequest {
    @NotNull(message = "ID do usuário é obrigatório")
    private Integer usuarioId;
    
    @NotBlank(message = "CNH é obrigatória")
    @Size(max = 20, message = "CNH deve ter no máximo 20 caracteres")
    private String cnh;
    
    @NotNull(message = "Tipo de veículo é obrigatório")
    private entregador.TipoVeiculo tipoVeiculo;
    
    @Size(max = 8, message = "Placa deve ter no máximo 8 caracteres")
    private String placaVeiculo;
    
    @Size(max = 50, message = "Modelo deve ter no máximo 50 caracteres")
    private String modeloVeiculo;
    
    private entregador.StatusEntregador status;
    private BigDecimal avaliacaoMedia;
}