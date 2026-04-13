package com.example.backend.mapper;

import com.example.backend.entity.Pedido;
import com.example.backend.entity.ItemPedido;
import com.example.backend.dto.response.PedidoResponseDTO;
import com.example.backend.dto.response.ItemPedidoResponseDTO;
import org.mapstruct.*;
import java.math.BigDecimal;

@Mapper(componentModel = "spring", uses = {ItemPedidoMapper.class})
public interface PedidoMapper {
    
    @Mapping(source = "cliente.usuario.nome", target = "clienteNome")
    @Mapping(source = "cliente.id", target = "clienteId")
    @Mapping(source = "restaurante.nomeFantasia", target = "restauranteNome")
    @Mapping(source = "restaurante.id", target = "restauranteId")
    @Mapping(source = "entregador.usuario.nome", target = "entregadorNome")
    @Mapping(source = "enderecoEntrega.logradouro", target = "enderecoEntrega")
    @Mapping(source = "itens", target = "itens")
    @Mapping(target = "statusPedido", source = "status")
    @Mapping(target = "valorTotal", source = "valorTotal")
    @Mapping(target = "tempoEstimado", source = "tempoEstimadoEntrega")
    @Mapping(target = "dataCriacao", source = "dataPedido")
    PedidoResponseDTO toResponseDTO(Pedido pedido);
    
    List<PedidoResponseDTO> toResponseDTOList(List<Pedido> pedidos);
}

// DTO correspondente
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {
    private Integer id;
    private String clienteNome;
    private Integer clienteId;
    private String restauranteNome;
    private Integer restauranteId;
    private String entregadorNome;
    private String enderecoEntrega;
    private List<ItemPedidoResponseDTO> itens;
    private String statusPedido;
    private BigDecimal valorTotal;
    private Integer tempoEstimado;
    private LocalDateTime dataCriacao;
    private String observacoes;
    private Integer avaliacao;
}