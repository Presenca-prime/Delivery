public class log_atividade {
    @Entity
@Table(name = "log_atividade")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogAtividade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    @Column(name = "acao", length = 200, nullable = false)
    private String acao;
    
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
    
    @Column(name = "ip_address", length = 45)
    private String ipAddress;
    
    @Column(name = "data_hora")
    private LocalDateTime dataHora;
}

}
