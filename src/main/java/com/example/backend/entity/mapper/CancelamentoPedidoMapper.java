package com.example.backend.mapper;

import com.example.backend.entity.CancelamentoPedido;
import com.example.backend.dto.response.CancelamentoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CancelamentoPedidoMapper {
    
    @Mapping(source = "pedido.id", target = "pedidoId")
    @Mapping(source = "solicitadoPor.nome", target = "solicitadoPorNome")
    @Mapping(source = "aprovadoPor.usuario.nome", target = "aprovadoPorNome")
    CancelamentoResponseDTO toResponseDTO(CancelamentoPedido cancelamento);
    
    List<CancelamentoResponseDTO> toResponseDTOList(List<CancelamentoPedido> cancelamentos);
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelamentoResponseDTO {
    private Integer id;
    private Integer pedidoId;
    private String motivo;
    private String descricaoMotivo;
    private String solicitadoPorNome;
    private String aprovadoPorNome;
    private String status;
    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataProcessamento;
    private BigDecimal taxaCancelamento;
    private Boolean reembolsoRealizado;
    private String observacoesAdmin;
}