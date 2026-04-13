package com.example.backend.dto.request;

import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class RestauranteRequest {
    @NotNull(message = "ID do usuário é obrigatório")
    private Integer usuarioId;
    
    @NotBlank(message = "Razão social é obrigatória")
    @Size(max = 200, message = "Razão social deve ter no máximo 200 caracteres")
    private String razaoSocial;
    
    @NotBlank(message = "Nome fantasia é obrigatório")
    @Size(max = 200, message = "Nome fantasia deve ter no máximo 200 caracteres")
    private String nomeFantasia;
    
    @NotBlank(message = "CNPJ é obrigatório")
    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}|\\d{14}", message = "CNPJ inválido")
    private String cnpj;
    
    private String descricao;
    private LocalTime horarioAbertura;
    private LocalTime horarioFechamento;
    
    @Size(max = 100, message = "Dias de funcionamento deve ter no máximo 100 caracteres")
    private String diasFuncionamento;
    
    private BigDecimal taxaEntrega;
    private Integer tempoMedioEntrega;
    
    @Size(max = 100, message = "Categoria deve ter no máximo 100 caracteres")
    private String categoria;
    
    @Size(max = 500, message = "Logo URL deve ter no máximo 500 caracteres")
    private String logoUrl;
    
    private Boolean ativo;
}