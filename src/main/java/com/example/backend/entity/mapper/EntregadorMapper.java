package com.example.backend.mapper;

import com.example.backend.entity.Entregador;
import com.example.backend.dto.response.EntregadorResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface EntregadorMapper {
    
    @Mapping(source = "usuario.nome", target = "nome")
    @Mapping(source = "usuario.email", target = "email")
    @Mapping(source = "usuario.telefone", target = "telefone")
    @Mapping(source = "usuario.ativo", target = "usuarioAtivo")
    EntregadorResponseDTO toResponseDTO(Entregador entregador);
    
    List<EntregadorResponseDTO> toResponseDTOList(List<Entregador> entregadores);
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntregadorResponseDTO {
    private Integer id;
    private String nome;
    private String email;
    private String telefone;
    private Boolean usuarioAtivo;
    private String cnh;
    private String tipoVeiculo;
    private String placaVeiculo;
    private String modeloVeiculo;
    private String status;
    private BigDecimal avaliacaoMedia;
    private Integer totalEntregas;
}