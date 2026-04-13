public class restaurante {
    @Entity
@Table(name = "restaurante")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurante {
    
    @Id
    private Integer id;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Usuario usuario;
    
    @Column(name = "razao_social", length = 200, nullable = false)
    private String razaoSocial;
    
    @Column(name = "nome_fantasia", length = 200, nullable = false)
    private String nomeFantasia;
    
    @Column(name = "cnpj", length = 18, nullable = false, unique = true)
    private String cnpj;
    
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
    
    @Column(name = "horario_abertura")
    private LocalTime horarioAbertura;
    
    @Column(name = "horario_fechamento")
    private LocalTime horarioFechamento;
    
    @Column(name = "dias_funcionamento", length = 100)
    private String diasFuncionamento;
    
    @Column(name = "taxa_entrega", precision = 10, scale = 2)
    private BigDecimal taxaEntrega = BigDecimal.ZERO;
    
    @Column(name = "tempo_medio_entrega")
    private Integer tempoMedioEntrega;
    
    @Column(name = "categoria", length = 100)
    private String categoria;
    
    @Column(name = "logo_url", length = 500)
    private String logoUrl;
    
    @Column(name = "ativo")
    private Boolean ativo = true;
    
    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL)
    private List<Produto> produtos;
    
    @OneToMany(mappedBy = "restaurante")
    private List<Pedido> pedidos;
}
}
