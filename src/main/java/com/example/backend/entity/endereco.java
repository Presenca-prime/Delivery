public class endereco {
    @Entity
@Table(name = "endereco")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @Column(name = "logradouro", length = 200, nullable = false)
    private String logradouro;
    
    @Column(name = "numero", length = 10)
    private String numero;
    
    @Column(name = "complemento", length = 100)
    private String complemento;
    
    @Column(name = "bairro", length = 100, nullable = false)
    private String bairro;
    
    @Column(name = "cidade", length = 100, nullable = false)
    private String cidade;
    
    @Column(name = "estado", length = 2, nullable = false)
    private String estado;
    
    @Column(name = "cep", length = 9, nullable = false)
    private String cep;
    
    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;
    
    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;
    
    @Column(name = "endereco_principal")
    private Boolean enderecoPrincipal = false;
    
    @OneToMany(mappedBy = "enderecoEntrega")
    private List<Pedido> pedidos;
}
}
