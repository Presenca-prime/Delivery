package com.example.backend.mapper;

import com.example.backend.entity.Restaurante;
import com.example.backend.dto.response.RestauranteResponseDTO;
import com.example.backend.dto.request.RestauranteRequestDTO;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", uses = {ProdutoMapper.class})
public interface RestauranteMapper {
    
    @Mapping(source = "usuario.email", target = "email")
    @Mapping(source = "usuario.telefone", target = "telefone")
    @Mapping(source = "usuario.ativo", target = "usuarioAtivo")
    @Mapping(target = "produtos", source = "produtos")
    RestauranteResponseDTO toResponseDTO(Restaurante restaurante);
    
    List<RestauranteResponseDTO> toResponseDTOList(List<Restaurante> restaurantes);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true) // Usuario deve ser criado separadamente
    @Mapping(target = "produtos", ignore = true)
    @Mapping(target = "pedidos", ignore = true)
    Restaurante toEntity(RestauranteRequestDTO request);
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestauranteResponseDTO {
    private Integer id;
    private String razaoSocial;
    private String nomeFantasia;
    private String cnpj;
    private String email;
    private String telefone;
    private Boolean usuarioAtivo;
    private String descricao;
    private String horarioAbertura;
    private String horarioFechamento;
    private BigDecimal taxaEntrega;
    private Integer tempoMedioEntrega;
    private String categoria;
    private String logoUrl;
    private Boolean ativo;
    private List<ProdutoResponseDTO> produtos;
}