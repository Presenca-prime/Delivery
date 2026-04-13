package com.example.backend.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProdutoResponse {
    private Integer id;
    private RestauranteResponse restaurante;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String categoria;
    private String imagemUrl;
    private Boolean disponivel;
    private Integer tempoPreparo;
    private List<ItemPedidoResponse> itensPedido;
    
    public static ProdutoResponse fromEntity(produto.Produto produto) {
        ProdutoResponse response = new ProdutoResponse();
        response.setId(produto.getId());
        response.setNome(produto.getNome());
        response.setDescricao(produto.getDescricao());
        response.setPreco(produto.getPreco());
        response.setCategoria(produto.getCategoria());
        response.setImagemUrl(produto.getImagemUrl());
        response.setDisponivel(produto.getDisponivel());
        response.setTempoPreparo(produto.getTempoPreparo());
        return response;
    }
}