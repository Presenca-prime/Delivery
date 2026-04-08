package com.example.backend.dto.request;

import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class ProdutoRequest {
    @NotNull(message = "ID do restaurante é obrigatório")
    private Integer restauranteId;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;
    
    private String descricao;
    
    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    private BigDecimal preco;
    
    @Size(max = 50, message = "Categoria deve ter no máximo 50 caracteres")
    private String categoria;
    
    @Size(max = 500, message = "Imagem URL deve ter no máximo 500 caracteres")
    private String imagemUrl;
    
    private Boolean disponivel;
    private Integer tempoPreparo;
}