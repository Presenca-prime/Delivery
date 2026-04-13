public class pedido {
    @Entity
@Table(name = "pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;
    
    @ManyToOne
    @JoinColumn(name = "entregador_id")
    private Entregador entregador;
    
    @ManyToOne
    @JoinColumn(name = "endereco_entrega_id", nullable = false)
    private Endereco enderecoEntrega;
    
    @Column(name = "data_pedido")
    private LocalDateTime dataPedido;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusPedido status = StatusPedido.aguardando_pagamento;
    
    @Column(name = "valor_subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorSubtotal;
    
    @Column(name = "valor_taxa_entrega", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorTaxaEntrega;
    
    @Column(name = "valor_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorTotal;
    
    @Column(name = "tempo_estimado_entrega")
    private Integer tempoEstimadoEntrega;
    
    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;
    
    @Column(name = "avaliacao")
    private Integer avaliacao;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens;
    
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    private Pagamento pagamento;
    
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    private CancelamentoPedido cancelamento;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<RastreamentoEntrega> rastreamentos;
    
    public enum StatusPedido {
        aguardando_pagamento, pagamento_aprovado, em_preparo, pronto,
        aguardando_entregador, em_entrega, entregue, cancelado, cancelamento_solicitado
    }
}

}
