package com.example.backend.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SessaoLoginResponse {
    private Integer id;
    private UsuarioResponse usuario;
    private String token;
    private LocalDateTime dataLogin;
    private LocalDateTime dataExpiracao;
    private String ipAddress;
    private String dispositivo;
    private Boolean ativo;
    
    public static SessaoLoginResponse fromEntity(sessao_login.SessaoLogin sessao) {
        SessaoLoginResponse response = new SessaoLoginResponse();
        response.setId(sessao.getId());
        response.setToken(sessao.getToken());
        response.setDataLogin(sessao.getDataLogin());
        response.setDataExpiracao(sessao.getDataExpiracao());
        response.setIpAddress(sessao.getIpAddress());
        response.setDispositivo(sessao.getDispositivo());
        response.setAtivo(sessao.getAtivo());
        if (sessao.getUsuario() != null) {
            response.setUsuario(UsuarioResponse.fromEntity(sessao.getUsuario()));
        }
        return response;
    }
}