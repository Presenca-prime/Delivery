package com.example.backend.dto.response;

import com.example.backend.entity.endereco;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class EnderecoResponse {
    private Integer id;
    private UsuarioResponse usuario;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Boolean enderecoPrincipal;
    
    public static EnderecoResponse fromEntity(endereco.Endereco endereco) {
        EnderecoResponse response = new EnderecoResponse();
        response.setId(endereco.getId());
        response.setLogradouro(endereco.getLogradouro());
        response.setNumero(endereco.getNumero());
        response.setComplemento(endereco.getComplemento());
        response.setBairro(endereco.getBairro());
        response.setCidade(endereco.getCidade());
        response.setEstado(endereco.getEstado());
        response.setCep(endereco.getCep());
        response.setLatitude(endereco.getLatitude());
        response.setLongitude(endereco.getLongitude());
        response.setEnderecoPrincipal(endereco.getEnderecoPrincipal());
        if (endereco.getUsuario() != null) {
            response.setUsuario(UsuarioResponse.fromEntity(endereco.getUsuario()));
        }
        return response;
    }
}