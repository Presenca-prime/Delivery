package com.example.backend.dto.response;

import com.example.backend.entity.cliente;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class ClienteResponse {
    private Integer id;
    private UsuarioResponse usuario;
    private LocalDate dataNascimento;
    private String preferenciasPagamento;
    private List<PedidoResponse> pedidos;
    
    public static ClienteResponse fromEntity(cliente.Cliente cliente) {
        ClienteResponse response = new ClienteResponse();
        response.setId(cliente.getId());
        response.setDataNascimento(cliente.getDataNascimento());
        response.setPreferenciasPagamento(cliente.getPreferenciasPagamento());
        if (cliente.getUsuario() != null) {
            response.setUsuario(UsuarioResponse.fromEntity(cliente.getUsuario()));
        }
        return response;
    }
}