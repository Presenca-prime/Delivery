package com.example.backend.mapper;

import com.example.backend.entity.ItemPedido;
import com.example.backend.dto.response.ItemPedidoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemPedidoMapper {
    
    @Mapping(source = "produto.nome", target = "produtoNome")
    @Mapping(source = "produto.id", target = "produtoId")
    @Mapping(source = "produto.imagemUrl", target = "imagemUrl")
    @Mapping(target = "subtotal", expression = "java(item.getQuantidade() * item.getPrecoUnitario().doubleValue())")
    ItemPedidoResponseDTO toResponseDTO(ItemPedido item);
    
    List<ItemPedidoResponseDTO> toResponseDTOList(List<ItemPedido> itens);
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoResponseDTO {
    private Integer id;
    private String produtoNome;
    private Integer produtoId;
    private String imagemUrl;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private Double subtotal;
    private String observacoes;
}