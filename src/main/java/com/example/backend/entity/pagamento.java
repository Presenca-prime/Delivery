public class pagamento {
    @Entity
@Table(name = "pagamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo", nullable = false)
    private MetodoPagamento metodo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusPagamento status = StatusPagamento.pendente;
    
    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;
    
    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;
    
    @Column(name = "codigo_transacao", length = 100)
    private String codigoTransacao;
    
    @Column(name = "dados_pagamento_cript", columnDefinition = "TEXT")
    private String dadosPagamentoCript;
    
    public enum MetodoPagamento {
        cartao_credito, cartao_debito, pix, dinheiro
    }
    
    public enum StatusPagamento {
        pendente, aprovado, recusado, estornado
    }
}

}
