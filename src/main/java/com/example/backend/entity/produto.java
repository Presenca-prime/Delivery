public class produto {
    @Entity
@Table(name = "produto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;
    
    @Column(name = "nome", length = 100, nullable = false)
    private String nome;
    
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
    
    @Column(name = "preco", precision = 10, scale = 2, nullable = false)
    private BigDecimal preco;
    
    @Column(name = "categoria", length = 50)
    private String categoria;
    
    @Column(name = "imagem_url", length = 500)
    private String imagemUrl;
    
    @Column(name = "disponivel")
    private Boolean disponivel = true;
    
    @Column(name = "tempo_preparo")
    private Integer tempoPreparo;
    
    @OneToMany(mappedBy = "produto")
    private List<ItemPedido> itensPedido;
}  
} 
