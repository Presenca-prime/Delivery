package com.example.backend.mapper;

import com.example.backend.entity.Usuario;
import com.example.backend.dto.request.UsuarioRequestDTO;
import com.example.backend.dto.response.UsuarioResponseDTO;
import org.mapstruct.*;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface UsuarioMapper {
    
    // Entity -> Response DTO
    UsuarioResponseDTO toResponseDTO(Usuario usuario);
    
    // Lista de Entity -> Lista de Response DTO
    List<UsuarioResponseDTO> toResponseDTOList(List<Usuario> usuarios);
    
    // Request DTO -> Entity (para criar novo)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", expression = "java(LocalDateTime.now())")
    @Mapping(target = "ativo", constant = "true")
    @Mapping(target = "tipoUsuario", expression = "java(Usuario.TipoUsuario.valueOf(request.getTipoUsuario()))")
    @Mapping(target = "enderecos", ignore = true)
    @Mapping(target = "sessoes", ignore = true)
    @Mapping(target = "logs", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "entregador", ignore = true)
    @Mapping(target = "restaurante", ignore = true)
    @Mapping(target = "administrador", ignore = true)
    @Mapping(target = "ultimoAcesso", ignore = true)
    Usuario toEntity(UsuarioRequestDTO request);
    
    // Atualizar entity existente com dados do request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", ignore = true) // senha tem tratamento especial
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "cpf", ignore = true) // CPF não deve ser alterado
    @Mapping(target = "tipoUsuario", ignore = true) // tipo não pode mudar
    @Mapping(target = "enderecos", ignore = true)
    @Mapping(target = "sessoes", ignore = true)
    @Mapping(target = "logs", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "entregador", ignore = true)
    @Mapping(target = "restaurante", ignore = true)
    @Mapping(target = "administrador", ignore = true)
    @Mapping(target = "ultimoAcesso", ignore = true)
    void updateEntityFromRequest(UsuarioRequestDTO request, @MappingTarget Usuario usuario);
}