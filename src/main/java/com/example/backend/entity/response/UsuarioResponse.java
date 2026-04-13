package com.example.backend.dto.response;

import com.example.backend.entity.Usuario;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UsuarioResponse {
    private Integer id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private LocalDateTime dataCadastro;
    private Boolean ativo;
    private Usuario.TipoUsuario tipoUsuario;
    private LocalDateTime ultimoAcesso;
    private List<EnderecoResponse> enderecos;
    
    public static UsuarioResponse fromEntity(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setNome(usuario.getNome());
        response.setEmail(usuario.getEmail());
        response.setCpf(usuario.getCpf());
        response.setTelefone(usuario.getTelefone());
        response.setDataCadastro(usuario.getDataCadastro());
        response.setAtivo(usuario.getAtivo());
        response.setTipoUsuario(usuario.getTipoUsuario());
        response.setUltimoAcesso(usuario.getUltimoAcesso());
        return response;
    }
}