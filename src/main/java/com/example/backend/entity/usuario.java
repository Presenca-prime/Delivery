public class usuario {
     @Entity
    @Table(name = "usuario")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Usuario {
        
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        
        @Column(name = "nome", length = 100, nullable = false)
        private String nome;
        
        @Column(name = "email", length = 100, nullable = false, unique = true)
        private String email;
        
        @Column(name = "senha", length = 255, nullable = false)
        private String senha;
        
        @Column(name = "cpf", length = 14, nullable = false, unique = true)
        private String cpf;
        
        @Column(name = "telefone", length = 20)
        private String telefone;
        
        @Column(name = "data_cadastro")
        private LocalDateTime dataCadastro;
        
        @Column(name = "ativo")
        private Boolean ativo = true;
        
        @Enumerated(EnumType.STRING)
        @Column(name = "tipo_usuario", nullable = false)
        private TipoUsuario tipoUsuario;
        
        @Column(name = "ultimo_acesso")
        private LocalDateTime ultimoAcesso;
        
        @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
        private List<Endereco> enderecos;
        
        @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
        private List<SessaoLogin> sessoes;
        
        @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
        private List<LogAtividade> logs;
        
        @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
        private Cliente cliente;
        
        @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
        private Entregador entregador;
        
        @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
        private Restaurante restaurante;
        
        @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
        private Administrador administrador;
        
        public enum TipoUsuario {
            cliente, entregador, admin, restaurante
        }
    }
}
