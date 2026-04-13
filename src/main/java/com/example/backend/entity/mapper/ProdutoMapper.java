package com.example.backend.mapper;

import com.example.backend.entity.Produto;
import com.example.backend.dto.response.ProdutoResponseDTO;
import com.example.backend.dto.request.ProdutoRequestDTO;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    
    ProdutoResponseDTO toResponseDTO(Produto produto);
    List<ProdutoResponseDTO> toResponseDTOList(List<Produto> produtos);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurante", ignore = true)
    @Mapping(target = "itensPedido", ignore = true)
    Produto toEntity(ProdutoRequestDTO request);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurante", ignore = true)
    void updateEntity(ProdutoRequestDTO request, @MappingTarget Produto produto);
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoResponseDTO {
    private Integer id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String categoria;
    private String imagemUrl;
    private Boolean disponivel;
    private Integer tempoPreparo;
}