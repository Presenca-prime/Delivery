package com.example.backend.dto.response;

import com.example.backend.entity.validacao_sistema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ValidacaoSistemaResponse {
    private Integer id;
    private validacao_sistema.TipoValidacao tipoValidacao;
    private String tabelaVerificada;
    private Integer idRegistro;
    private Boolean statusValidacao;
    private LocalDateTime dataVerificacao;
    private UsuarioResponse usuarioVerificador;
    private String resultadoJson;
    private String mensagemErro;
    
    public static ValidacaoSistemaResponse fromEntity(validacao_sistema.ValidacaoSistema validacao) {
        ValidacaoSistemaResponse response = new ValidacaoSistemaResponse();
        response.setId(validacao.getId());
        response.setTipoValidacao(validacao.getTipoValidacao());
        response.setTabelaVerificada(validacao.getTabelaVerificada());
        response.setIdRegistro(validacao.getIdRegistro());
        response.setStatusValidacao(validacao.getStatusValidacao());
        response.setDataVerificacao(validacao.getDataVerificacao());
        response.setResultadoJson(validacao.getResultadoJson());
        response.setMensagemErro(validacao.getMensagemErro());
        if (validacao.getUsuarioVerificador() != null) {
            response.setUsuarioVerificador(UsuarioResponse.fromEntity(validacao.getUsuarioVerificador()));
        }
        return response;
    }
}