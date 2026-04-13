public class rastreamento_entrega {
    @Entity
@Table(name = "rastreamento_entrega")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RastreamentoEntrega {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
    
    @Column(name = "latitude", precision = 10, scale = 8, nullable = false)
    private BigDecimal latitude;
    
    @Column(name = "longitude", precision = 11, scale = 8, nullable = false)
    private BigDecimal longitude;
    
    @Column(name = "data_hora")
    private LocalDateTime dataHora;
}

}
