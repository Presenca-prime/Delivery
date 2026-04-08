public class validacao_sistema {
    @Entity
@Table(name = "validacao_sistema")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidacaoSistema {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_validacao", nullable = false)
    private TipoValidacao tipoValidacao;
    
    @Column(name = "tabela_verificada", length = 50, nullable = false)
    private String tabelaVerificada;
    
    @Column(name = "id_registro", nullable = false)
    private Integer idRegistro;
    
    @Column(name = "status_validacao")
    private Boolean statusValidacao = false;
    
    @Column(name = "data_verificacao")
    private LocalDateTime dataVerificacao;
    
    @ManyToOne
    @JoinColumn(name = "usuario_verificador_id")
    private Usuario usuarioVerificador;
    
    @Column(name = "resultado_json", columnDefinition = "JSON")
    private String resultadoJson;
    
    @Column(name = "mensagem_erro", columnDefinition = "TEXT")
    private String mensagemErro;
    
    public enum TipoValidacao {
        usuario_existe, email_valido, cpf_valido, cnpj_valido,
        restaurante_ativo, entregador_disponivel, produto_disponivel,
        endereco_valido, pedido_existe, status_permitido,
        cancelamento_pendente, pagamento_pendente, sessao_ativa, dados_obrigatorios
    }
}

}
