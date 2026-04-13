package com.example.backend.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LogAtividadeResponse {
    private Integer id;
    private UsuarioResponse usuario;
    private String acao;
    private String descricao;
    private String ipAddress;
    private LocalDateTime dataHora;
    
    public static LogAtividadeResponse fromEntity(log_atividade.LogAtividade log) {
        LogAtividadeResponse response = new LogAtividadeResponse();
        response.setId(log.getId());
        response.setAcao(log.getAcao());
        response.setDescricao(log.getDescricao());
        response.setIpAddress(log.getIpAddress());
        response.setDataHora(log.getDataHora());
        if (log.getUsuario() != null) {
            response.setUsuario(UsuarioResponse.fromEntity(log.getUsuario()));
        }
        return response;
    }
}