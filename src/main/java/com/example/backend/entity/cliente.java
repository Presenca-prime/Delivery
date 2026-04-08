public class cliente {
    @Entity
@Table(name = "cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    
    @Id
    private Integer id;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Usuario usuario;
    
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;
    
    @Column(name = "preferencias_pagamento", columnDefinition = "TEXT")
    private String preferenciasPagamento;
    
    @Column(name = "dados_pagamento_cript", columnDefinition = "TEXT")
    private String dadosPagamentoCript;
    
    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos;
}
}
