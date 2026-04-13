public class cancelamento_pedido {
    @Entity
@Table(name = "cancelamento_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelamentoPedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;
    
    @ManyToOne
    @JoinColumn(name = "solicitado_por", nullable = false)
    private Usuario solicitadoPor;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "motivo", nullable = false)
    private MotivoCancelamento motivo;
    
    @Column(name = "descricao_motivo", columnDefinition = "TEXT")
    private String descricaoMotivo;
    
    @Column(name = "data_solicitacao")
    private LocalDateTime dataSolicitacao;
    
    @Column(name = "data_processamento")
    private LocalDateTime dataProcessamento;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusCancelamento status = StatusCancelamento.pendente;
    
    @ManyToOne
    @JoinColumn(name = "aprovado_por")
    private Administrador aprovadoPor;
    
    @Column(name = "taxa_cancelamento", precision = 10, scale = 2)
    private BigDecimal taxaCancelamento = BigDecimal.ZERO;
    
    @Column(name = "reembolso_realizado")
    private Boolean reembolsoRealizado = false;
    
    @Column(name = "observacoes_admin", columnDefinition = "TEXT")
    private String observacoesAdmin;
    
    public enum MotivoCancelamento {
        cliente_desistiu, produto_indisponivel, atraso_entrega, 
        problema_pagamento, endereco_errado, outro
    }
    
    public enum StatusCancelamento {
        pendente, aprovado, negado
    }
}
}
