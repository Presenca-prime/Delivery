package com.example.backend.dto.request;

import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class EnderecoRequest {
    @NotNull(message = "ID do usuário é obrigatório")
    private Integer usuarioId;
    
    @NotBlank(message = "Logradouro é obrigatório")
    @Size(max = 200, message = "Logradouro deve ter no máximo 200 caracteres")
    private String logradouro;
    
    @Size(max = 10, message = "Número deve ter no máximo 10 caracteres")
    private String numero;
    
    @Size(max = 100, message = "Complemento deve ter no máximo 100 caracteres")
    private String complemento;
    
    @NotBlank(message = "Bairro é obrigatório")
    @Size(max = 100, message = "Bairro deve ter no máximo 100 caracteres")
    private String bairro;
    
    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
    private String cidade;
    
    @NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres")
    private String estado;
    
    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP inválido")
    private String cep;
    
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Boolean enderecoPrincipal;
}