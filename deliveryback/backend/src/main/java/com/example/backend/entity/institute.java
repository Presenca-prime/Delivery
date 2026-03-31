
public class institute {
    package com.delivery.entity;
    
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.AllArgsConstructor;
    import javax.persistence.*;
    import java.time.LocalDateTime;
    import java.util.List;
    
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

    package com.delivery.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;

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

    package com.delivery.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

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

     package com.delivery.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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

    package com.delivery.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

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

     package com.delivery.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

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

     package com.delivery.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "administrador")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Administrador {
    
    @Id
    private Integer id;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Usuario usuario;
    
    @Column(name = "cargo", length = 100, nullable = false)
    private String cargo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_acesso")
    private NivelAcesso nivelAcesso = NivelAcesso.suporte;
    
    public enum NivelAcesso {
        master, gerente, suporte
    }
}

    package com.delivery.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

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

    package com.delivery.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;
    
    @ManyToOne
    @JoinColumn(name = "entregador_id")
    private Entregador entregador;
    
    @ManyToOne
    @JoinColumn(name = "endereco_entrega_id", nullable = false)
    private Endereco enderecoEntrega;
    
    @Column(name = "data_pedido")
    private LocalDateTime dataPedido;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusPedido status = StatusPedido.aguardando_pagamento;
    
    @Column(name = "valor_subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorSubtotal;
    
    @Column(name = "valor_taxa_entrega", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorTaxaEntrega;
    
    @Column(name = "valor_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorTotal;
    
    @Column(name = "tempo_estimado_entrega")
    private Integer tempoEstimadoEntrega;
    
    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;
    
    @Column(name = "avaliacao")
    private Integer avaliacao;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens;
    
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    private Pagamento pagamento;
    
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    private CancelamentoPedido cancelamento;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<RastreamentoEntrega> rastreamentos;
    
    public enum StatusPedido {
        aguardando_pagamento, pagamento_aprovado, em_preparo, pronto,
        aguardando_entregador, em_entrega, entregue, cancelado, cancelamento_solicitado
    }
}

     package com.delivery.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "item_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
    
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    
    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;
    
    @Column(name = "preco_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precoUnitario;
    
    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;
}

     package com.delivery.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo", nullable = false)
    private MetodoPagamento metodo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusPagamento status = StatusPagamento.pendente;
    
    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;
    
    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;
    
    @Column(name = "codigo_transacao", length = 100)
    private String codigoTransacao;
    
    @Column(name = "dados_pagamento_cript", columnDefinition = "TEXT")
    private String dadosPagamentoCript;
    
    public enum MetodoPagamento {
        cartao_credito, cartao_debito, pix, dinheiro
    }
    
    public enum StatusPagamento {
        pendente, aprovado, recusado, estornado
    }
}

     package com.delivery.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cancelamento_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelamentoPedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;
    
    @ManyToOne
    @JoinColumn(name = "solicitado_por", nullable = false)
    private Usuario solicitadoPor;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "motivo", nullable = false)
    private MotivoCancelamento motivo;
    
    @Column(name = "descricao_motivo", columnDefinition = "TEXT")
    private String descricaoMotivo;
    
    @Column(name = "data_solicitacao")
    private LocalDateTime dataSolicitacao;
    
    @Column(name = "data_processamento")
    private LocalDateTime dataProcessamento;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusCancelamento status = StatusCancelamento.pendente;
    
    @ManyToOne
    @JoinColumn(name = "aprovado_por")
    private Administrador aprovadoPor;
    
    @Column(name = "taxa_cancelamento", precision = 10, scale = 2)
    private BigDecimal taxaCancelamento = BigDecimal.ZERO;
    
    @Column(name = "reembolso_realizado")
    private Boolean reembolsoRealizado = false;
    
    @Column(name = "observacoes_admin", columnDefinition = "TEXT")
    private String observacoesAdmin;
    
    public enum MotivoCancelamento {
        cliente_desistiu, produto_indisponivel, atraso_entrega, 
        problema_pagamento, endereco_errado, outro
    }
    
    public enum StatusCancelamento {
        pendente, aprovado, negado
    }
}

     package com.delivery.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
 
     package com.delivery.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

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

    package com.delivery.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

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



import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

