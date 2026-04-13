package com.example.backend.dto.request;

import com.example.backend.entity.validacao_sistema;
import lombok.Data;
import javax.validation.constraints.*;

@Data
public class ValidacaoSistemaRequest {
    @NotNull(message = "Tipo de validação é obrigatório")
    private validacao_sistema.TipoValidacao tipoValidacao;
    
    @NotBlank(message = "Tabela verificada é obrigatória")
    @Size(max = 50, message = "Tabela verificada deve ter no máximo 50 caracteres")
    private String tabelaVerificada;
    
    @NotNull(message = "ID do registro é obrigatório")
    private Integer idRegistro;
    
    private Integer usuarioVerificadorId;
    private String resultadoJson;
    private String mensagemErro;
}