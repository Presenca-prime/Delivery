public class entregador {
    @Entity
@Table(name = "entregador")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entregador {
    
    @Id
    private Integer id;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Usuario usuario;
    
    @Column(name = "cnh", length = 20, nullable = false, unique = true)
    private String cnh;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_veiculo", nullable = false)
    private TipoVeiculo tipoVeiculo;
    
    @Column(name = "placa_veiculo", length = 8)
    private String placaVeiculo;
    
    @Column(name = "modelo_veiculo", length = 50)
    private String modeloVeiculo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEntregador status = StatusEntregador.disponivel;
    
    @Column(name = "avaliacao_media", precision = 3, scale = 2)
    private BigDecimal avaliacaoMedia = BigDecimal.valueOf(5.0);
    
    @Column(name = "total_entregas")
    private Integer totalEntregas = 0;
    
    @OneToMany(mappedBy = "entregador")
    private List<Pedido> pedidos;
    
    public enum TipoVeiculo {
        moto, carro, bicicleta
    }
    
    public enum StatusEntregador {
        disponivel, ocupado, offline
    }
}

}
