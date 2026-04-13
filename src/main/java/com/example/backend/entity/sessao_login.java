public class sessao_login {
    @Entity
@Table(name = "sessao_login")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessaoLogin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @Column(name = "token", length = 255, nullable = false, unique = true)
    private String token;
    
    @Column(name = "data_login")
    private LocalDateTime dataLogin;
    
    @Column(name = "data_expiracao", nullable = false)
    private LocalDateTime dataExpiracao;
    
    @Column(name = "ip_address", length = 45)
    private String ipAddress;
    
    @Column(name = "dispositivo", length = 255)
    private String dispositivo;
    
    @Column(name = "ativo")
    private Boolean ativo = true;
}
}
