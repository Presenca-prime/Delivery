package com.example.backend.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Data
public class RestauranteResponse {
    private Integer id;
    private UsuarioResponse usuario;
    private String razaoSocial;
    private String nomeFantasia;
    private String cnpj;
    private String descricao;
    private LocalTime horarioAbertura;
    private LocalTime horarioFechamento;
    private String diasFuncionamento;
    private BigDecimal taxaEntrega;
    private Integer tempoMedioEntrega;
    private String categoria;
    private String logoUrl;
    private Boolean ativo;
    private List<ProdutoResponse> produtos;
    private List<PedidoResponse> pedidos;
    
    public static RestauranteResponse fromEntity(restaurante.Restaurante restaurante) {
        RestauranteResponse response = new RestauranteResponse();
        response.setId(restaurante.getId());
        response.setRazaoSocial(restaurante.getRazaoSocial());
        response.setNomeFantasia(restaurante.getNomeFantasia());
        response.setCnpj(restaurante.getCnpj());
        response.setDescricao(restaurante.getDescricao());
        response.setHorarioAbertura(restaurante.getHorarioAbertura());
        response.setHorarioFechamento(restaurante.getHorarioFechamento());
        response.setDiasFuncionamento(restaurante.getDiasFuncionamento());
        response.setTaxaEntrega(restaurante.getTaxaEntrega());
        response.setTempoMedioEntrega(restaurante.getTempoMedioEntrega());
        response.setCategoria(restaurante.getCategoria());
        response.setLogoUrl(restaurante.getLogoUrl());
        response.setAtivo(restaurante.getAtivo());
        if (restaurante.getUsuario() != null) {
            response.setUsuario(UsuarioResponse.fromEntity(restaurante.getUsuario()));
        }
        return response;
    }
}