-- Criação do banco de dados
CREATE DATABASE IF NOT EXISTS sistema_delivery;

USE sistema_delivery;

-- =====================================================
-- TABELAS BASE (USUÁRIOS E AUTENTICAÇÃO)
-- =====================================================

-- Tabela 1: Usuario (base para todos os tipos de usuário)
CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE,
    tipo_usuario ENUM(
        'cliente',
        'entregador',
        'admin',
        'restaurante'
    ) NOT NULL,
    ultimo_acesso DATETIME
);

-- Tabela 2: Endereco
CREATE TABLE endereco (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    logradouro VARCHAR(200) NOT NULL,
    numero VARCHAR(10),
    complemento VARCHAR(100),
    bairro VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado CHAR(2) NOT NULL,
    cep VARCHAR(9) NOT NULL,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    endereco_principal BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);

-- Tabela 3: Sessao_Login
CREATE TABLE sessao_login (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    token VARCHAR(255) UNIQUE NOT NULL,
    data_login DATETIME DEFAULT CURRENT_TIMESTAMP,
    data_expiracao DATETIME NOT NULL,
    ip_address VARCHAR(45),
    dispositivo VARCHAR(255),
    ativo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);

-- =====================================================
-- TABELAS ESPECÍFICAS POR TIPO DE USUÁRIO
-- =====================================================

-- Tabela 4: Cliente (dados específicos)
CREATE TABLE cliente (
    id INT PRIMARY KEY,
    data_nascimento DATE,
    preferencias_pagamento TEXT,
    -- Dados criptografados sensíveis
    dados_pagamento_cript TEXT,
    FOREIGN KEY (id) REFERENCES usuario (id)
);

-- Tabela 5: Entregador
CREATE TABLE entregador (
    id INT PRIMARY KEY,
    cnh VARCHAR(20) UNIQUE NOT NULL,
    tipo_veiculo ENUM('moto', 'carro', 'bicicleta') NOT NULL,
    placa_veiculo VARCHAR(8),
    modelo_veiculo VARCHAR(50),
    status ENUM(
        'disponivel',
        'ocupado',
        'offline'
    ) DEFAULT 'disponivel',
    avaliacao_media DECIMAL(3, 2) DEFAULT 5.0,
    total_entregas INT DEFAULT 0,
    FOREIGN KEY (id) REFERENCES usuario (id)
);

-- Tabela 6: Restaurante
CREATE TABLE restaurante (
    id INT PRIMARY KEY,
    razao_social VARCHAR(200) NOT NULL,
    nome_fantasia VARCHAR(200) NOT NULL,
    cnpj VARCHAR(18) UNIQUE NOT NULL,
    descricao TEXT,
    horario_abertura TIME,
    horario_fechamento TIME,
    dias_funcionamento VARCHAR(100), -- "segunda-sexta" ou "todos"
    taxa_entrega DECIMAL(10, 2) DEFAULT 0,
    tempo_medio_entrega INT, -- em minutos
    categoria VARCHAR(100),
    logo_url VARCHAR(500),
    ativo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id) REFERENCES usuario (id)
);

-- Tabela 7: Administrador
CREATE TABLE administrador (
    id INT PRIMARY KEY,
    cargo VARCHAR(100) NOT NULL,
    nivel_acesso ENUM(
        'master',
        'gerente',
        'suporte'
    ) DEFAULT 'suporte',
    FOREIGN KEY (id) REFERENCES usuario (id)
);

-- =====================================================
-- TABELAS DE NEGÓCIO
-- =====================================================

-- Tabela 8: Produto (Itens do cardápio)
CREATE TABLE produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    restaurante_id INT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10, 2) NOT NULL,
    categoria VARCHAR(50),
    imagem_url VARCHAR(500),
    disponivel BOOLEAN DEFAULT TRUE,
    tempo_preparo INT, -- em minutos
    FOREIGN KEY (restaurante_id) REFERENCES restaurante (id)
);

-- Tabela 9: Pedido
CREATE TABLE pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    restaurante_id INT NOT NULL,
    entregador_id INT NULL,
    endereco_entrega_id INT NOT NULL,
    data_pedido DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM(
        'aguardando_pagamento',
        'pagamento_aprovado',
        'em_preparo',
        'pronto',
        'aguardando_entregador',
        'em_entrega',
        'entregue',
        'cancelado',
        'cancelamento_solicitado' -- novo status para solicitação de cancelamento
    ) DEFAULT 'aguardando_pagamento',
    valor_subtotal DECIMAL(10, 2) NOT NULL,
    valor_taxa_entrega DECIMAL(10, 2) NOT NULL,
    valor_total DECIMAL(10, 2) NOT NULL,
    tempo_estimado_entrega INT, -- em minutos
    observacoes TEXT,
    avaliacao INT CHECK (
        avaliacao >= 1
        AND avaliacao <= 5
    ),
    FOREIGN KEY (cliente_id) REFERENCES cliente (id),
    FOREIGN KEY (restaurante_id) REFERENCES restaurante (id),
    FOREIGN KEY (entregador_id) REFERENCES entregador (id),
    FOREIGN KEY (endereco_entrega_id) REFERENCES endereco (id)
);

-- Tabela 10: Item_Pedido
CREATE TABLE item_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    produto_id INT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    observacoes TEXT,
    FOREIGN KEY (pedido_id) REFERENCES pedido (id),
    FOREIGN KEY (produto_id) REFERENCES produto (id)
);

-- Tabela 11: Pagamento
CREATE TABLE pagamento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    metodo ENUM(
        'cartao_credito',
        'cartao_debito',
        'pix',
        'dinheiro'
    ) NOT NULL,
    status ENUM(
        'pendente',
        'aprovado',
        'recusado',
        'estornado'
    ) DEFAULT 'pendente',
    valor DECIMAL(10, 2) NOT NULL,
    data_pagamento DATETIME,
    codigo_transacao VARCHAR(100),
    dados_pagamento_cript TEXT, -- dados sensíveis criptografados
    FOREIGN KEY (pedido_id) REFERENCES pedido (id)
);

-- Tabela 12: Cancelamento_Pedido
CREATE TABLE cancelamento_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL UNIQUE,
    solicitado_por INT NOT NULL, -- usuario_id
    motivo ENUM(
        'cliente_desistiu',
        'produto_indisponivel',
        'atraso_entrega',
        'problema_pagamento',
        'endereco_errado',
        'outro'
    ) NOT NULL,
    descricao_motivo TEXT,
    data_solicitacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    data_processamento DATETIME,
    status ENUM(
        'pendente',
        'aprovado',
        'negado'
    ) DEFAULT 'pendente',
    aprovado_por INT, -- admin_id
    taxa_cancelamento DECIMAL(10, 2) DEFAULT 0,
    reembolso_realizado BOOLEAN DEFAULT FALSE,
    observacoes_admin TEXT,
    FOREIGN KEY (pedido_id) REFERENCES pedido (id),
    FOREIGN KEY (solicitado_por) REFERENCES usuario (id),
    FOREIGN KEY (aprovado_por) REFERENCES administrador (id)
);

-- Tabela 13: Rastreamento_Entrega
CREATE TABLE rastreamento_entrega (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    data_hora DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pedido_id) REFERENCES pedido (id)
);

-- Tabela 14: Log_Atividade
CREATE TABLE log_atividade (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,
    acao VARCHAR(200) NOT NULL,
    descricao TEXT,
    ip_address VARCHAR(45),
    data_hora DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);

-- =====================================================
-- STORED PROCEDURES
-- =====================================================

DELIMITER /
/

-- Procedure 1: Realizar Login
CREATE PROCEDURE sp_realizar_login(
    IN p_email VARCHAR(100),
    IN p_senha VARCHAR(255),
    IN p_ip VARCHAR(45),
    IN p_dispositivo VARCHAR(255)
)
BEGIN
    DECLARE v_usuario_id INT;
    DECLARE v_token VARCHAR(255);
    
    -- Verificar credenciais
    SELECT id INTO v_usuario_id
    FROM usuario
    WHERE email = p_email AND senha = p_senha AND ativo = TRUE;
    
    IF v_usuario_id IS NOT NULL THEN
        -- Gerar token simples (em produção usar UUID ou JWT)
        SET v_token = CONCAT('token_', v_usuario_id, '_', UNIX_TIMESTAMP());
        
        -- Atualizar último acesso
        UPDATE usuario SET ultimo_acesso = NOW() WHERE id = v_usuario_id;
        
        -- Criar sessão
        INSERT INTO sessao_login (usuario_id, token, data_expiracao, ip_address, dispositivo)
        VALUES (v_usuario_id, v_token, DATE_ADD(NOW(), INTERVAL 24 HOUR), p_ip, p_dispositivo);
        
        -- Registrar log
        INSERT INTO log_atividade (usuario_id, acao, descricao, ip_address)
        VALUES (v_usuario_id, 'login', 'Login realizado com sucesso', p_ip);
        
        -- Retornar dados do usuário
        SELECT 
            u.id,
            u.nome,
            u.email,
            u.tipo_usuario,
            v_token as token,
            s.data_expiracao
        FROM usuario u
        JOIN sessao_login s ON u.id = s.usuario_id
        WHERE u.id = v_usuario_id AND s.token = v_token;
    ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Credenciais inválidas ou usuário inativo';
    END IF;
END
/
/

-- Procedure 2: Logout
CREATE PROCEDURE sp_realizar_logout(
    IN p_token VARCHAR(255)
)
BEGIN
    DECLARE v_usuario_id INT;
    
    SELECT usuario_id INTO v_usuario_id
    FROM sessao_login
    WHERE token = p_token AND ativo = TRUE;
    
    IF v_usuario_id IS NOT NULL THEN
        UPDATE sessao_login SET ativo = FALSE WHERE token = p_token;
        
        INSERT INTO log_atividade (usuario_id, acao, descricao)
        VALUES (v_usuario_id, 'logout', 'Logout realizado com sucesso');
    END IF;
END
/
/

-- Procedure 3: Solicitar Cancelamento de Pedido
CREATE PROCEDURE sp_solicitar_cancelamento_pedido(
    IN p_pedido_id INT,
    IN p_usuario_id INT,
    IN p_motivo ENUM('cliente_desistiu', 'produto_indisponivel', 'atraso_entrega', 'problema_pagamento', 'endereco_errado', 'outro'),
    IN p_descricao TEXT
)
BEGIN
    DECLARE v_status_pedido VARCHAR(50);
    DECLARE v_cliente_id INT;
    
    -- Verificar se o pedido existe e pode ser cancelado
    SELECT status, cliente_id INTO v_status_pedido, v_cliente_id
    FROM pedido
    WHERE id = p_pedido_id;
    
    -- Verificar permissão (só o cliente pode solicitar cancelamento)
    IF v_cliente_id != p_usuario_id THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Apenas o cliente pode solicitar cancelamento';
    END IF;
    
    -- Verificar se o pedido está em status que permite cancelamento
    IF v_status_pedido NOT IN ('aguardando_pagamento', 'pagamento_aprovado', 'em_preparo') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Este pedido não pode mais ser cancelado';
    END IF;
    
    -- Atualizar status do pedido
    UPDATE pedido 
    SET status = 'cancelamento_solicitado' 
    WHERE id = p_pedido_id;
    
    -- Registrar solicitação de cancelamento
    INSERT INTO cancelamento_pedido (
        pedido_id, 
        solicitado_por, 
        motivo, 
        descricao_motivo, 
        data_solicitacao,
        status
    ) VALUES (
        p_pedido_id, 
        p_usuario_id, 
        p_motivo, 
        p_descricao, 
        NOW(),
        'pendente'
    );
    
    -- Registrar log
    INSERT INTO log_atividade (usuario_id, acao, descricao)
    VALUES (p_usuario_id, 'solicitar_cancelamento', CONCAT('Solicitação de cancelamento para pedido ', p_pedido_id));
    
    -- Retornar dados da solicitação
    SELECT 'Cancelamento solicitado com sucesso' as mensagem, p_pedido_id as pedido_id;
END
/
/

-- Procedure 4: Processar Cancelamento (Admin)
CREATE PROCEDURE sp_processar_cancelamento(
    IN p_cancelamento_id INT,
    IN p_admin_id INT,
    IN p_status ENUM('aprovado', 'negado'),
    IN p_observacoes TEXT
)
BEGIN
    DECLARE v_pedido_id INT;
    DECLARE v_pagamento_status VARCHAR(50);
    DECLARE v_valor_pedido DECIMAL(10,2);
    
    -- Obter dados do cancelamento
    SELECT pedido_id INTO v_pedido_id
    FROM cancelamento_pedido
    WHERE id = p_cancelamento_id AND status = 'pendente';
    
    IF v_pedido_id IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cancelamento não encontrado ou já processado';
    END IF;
    
    -- Atualizar cancelamento
    UPDATE cancelamento_pedido 
    SET status = p_status,
        data_processamento = NOW(),
        aprovado_por = p_admin_id,
        observacoes_admin = p_observacoes
    WHERE id = p_cancelamento_id;
    
    -- Se aprovado, atualizar status do pedido e processar reembolso
    IF p_status = 'aprovado' THEN
        -- Verificar se já foi pago
        SELECT status, valor INTO v_pagamento_status, v_valor_pedido
        FROM pagamento
        WHERE pedido_id = v_pedido_id;
        
        IF v_pagamento_status = 'aprovado' THEN
            -- Marcar para reembolso
            UPDATE cancelamento_pedido 
            SET reembolso_realizado = TRUE,
                taxa_cancelamento = v_valor_pedido * 0.05 -- 5% de taxa
            WHERE id = p_cancelamento_id;
            
            UPDATE pagamento 
            SET status = 'estornado' 
            WHERE pedido_id = v_pedido_id;
        END IF;
        
        -- Atualizar status do pedido
        UPDATE pedido 
        SET status = 'cancelado' 
        WHERE id = v_pedido_id;
    END IF;
    
    -- Registrar log
    INSERT INTO log_atividade (usuario_id, acao, descricao)
    VALUES (p_admin_id, 'processar_cancelamento', CONCAT('Cancelamento ', p_cancelamento_id, ' ', p_status));
    
    -- Retornar resultado
    SELECT CONCAT('Cancelamento ', p_status, ' com sucesso') as mensagem;
END
/
/

-- Procedure 5: Criar Pedido
CREATE PROCEDURE sp_criar_pedido(
    IN p_cliente_id INT,
    IN p_restaurante_id INT,
    IN p_endereco_id INT,
    IN p_observacoes TEXT,
    IN p_metodo_pagamento ENUM('cartao_credito', 'cartao_debito', 'pix', 'dinheiro'),
    IN p_dados_pagamento TEXT
)
BEGIN
    DECLARE v_pedido_id INT;
    DECLARE v_taxa_entrega DECIMAL(10,2);
    DECLARE v_subtotal DECIMAL(10,2) DEFAULT 0;
    
    -- Obter taxa de entrega
    SELECT taxa_entrega INTO v_taxa_entrega
    FROM restaurante
    WHERE id = p_restaurante_id;
    
    -- Criar pedido
    INSERT INTO pedido (
        cliente_id, 
        restaurante_id, 
        endereco_entrega_id,
        valor_subtotal,
        valor_taxa_entrega,
        valor_total,
        observacoes
    ) VALUES (
        p_cliente_id,
        p_restaurante_id,
        p_endereco_id,
        0,
        v_taxa_entrega,
        0,
        p_observacoes
    );
    
    SET v_pedido_id = LAST_INSERT_ID();
    
    -- Criar pagamento
    INSERT INTO pagamento (
        pedido_id,
        metodo,
        valor,
        dados_pagamento_cript
    ) VALUES (
        v_pedido_id,
        p_metodo_pagamento,
        0,
        p_dados_pagamento
    );
    
    SELECT v_pedido_id as pedido_id;
END
/
/

-- Procedure 6: Adicionar Item ao Pedido
CREATE PROCEDURE sp_adicionar_item_pedido(
    IN p_pedido_id INT,
    IN p_produto_id INT,
    IN p_quantidade INT,
    IN p_observacoes TEXT
)
BEGIN
    DECLARE v_preco DECIMAL(10,2);
    DECLARE v_subtotal DECIMAL(10,2);
    DECLARE v_taxa_entrega DECIMAL(10,2);
    
    -- Obter preço do produto
    SELECT preco INTO v_preco
    FROM produto
    WHERE id = p_produto_id;
    
    -- Inserir item
    INSERT INTO item_pedido (
        pedido_id,
        produto_id,
        quantidade,
        preco_unitario,
        observacoes
    ) VALUES (
        p_pedido_id,
        p_produto_id,
        p_quantidade,
        v_preco,
        p_observacoes
    );
    
    -- Atualizar valores do pedido
    UPDATE pedido 
    SET valor_subtotal = valor_subtotal + (v_preco * p_quantidade),
        valor_total = valor_subtotal + valor_taxa_entrega
    WHERE id = p_pedido_id;
    
    -- Atualizar valor do pagamento
    UPDATE pagamento 
    SET valor = (
        SELECT valor_total FROM pedido WHERE id = p_pedido_id
    )
    WHERE pedido_id = p_pedido_id;
END
/
/

-- Procedure 7: Confirmar Pagamento
CREATE PROCEDURE sp_confirmar_pagamento(
    IN p_pedido_id INT,
    IN p_codigo_transacao VARCHAR(100)
)
BEGIN
    UPDATE pagamento 
    SET status = 'aprovado',
        data_pagamento = NOW(),
        codigo_transacao = p_codigo_transacao
    WHERE pedido_id = p_pedido_id;
    
    UPDATE pedido 
    SET status = 'pagamento_aprovado'
    WHERE id = p_pedido_id;
    
    INSERT INTO log_atividade (usuario_id, acao, descricao)
    VALUES ((SELECT cliente_id FROM pedido WHERE id = p_pedido_id), 
            'pagamento_aprovado', 
            CONCAT('Pagamento aprovado para pedido ', p_pedido_id));
END
/
/

DELIMITER;

-- =====================================================
-- POPULAÇÃO DO BANCO DE DADOS
-- =====================================================

-- Inserir usuários base
INSERT INTO
    usuario (
        nome,
        email,
        senha,
        cpf,
        telefone,
        tipo_usuario
    )
VALUES
    -- Administradores
    (
        'Renato Freitas',
        'renato@delivery.com',
        'admin123',
        '123.456.789-00',
        '(11) 99999-0001',
        'admin'
    ),
    (
        'João Gustavo',
        'joao@delivery.com',
        'admin123',
        '123.456.789-01',
        '(11) 99999-0002',
        'admin'
    ),
    (
        'Jhonathan Monteiro',
        'jhonathan@delivery.com',
        'admin123',
        '123.456.789-02',
        '(11) 99999-0003',
        'admin'
    ),
    (
        'Sammy Lincon',
        'sammy@delivery.com',
        'admin123',
        '123.456.789-03',
        '(11) 99999-0004',
        'admin'
    ),
    (
        'Kevin Caio',
        'kevin@delivery.com',
        'admin123',
        '123.456.789-04',
        '(11) 99999-0005',
        'admin'
    ),

-- Clientes
(
    'Ana Souza',
    'ana.souza@email.com',
    'cliente123',
    '234.567.890-11',
    '(11) 98888-1111',
    'cliente'
),
(
    'Pedro Lima',
    'pedro.lima@email.com',
    'cliente123',
    '234.567.890-12',
    '(11) 98888-2222',
    'cliente'
),
(
    'Mariana Costa',
    'mariana.costa@email.com',
    'cliente123',
    '234.567.890-13',
    '(11) 98888-3333',
    'cliente'
),
(
    'Carlos Santos',
    'carlos.santos@email.com',
    'cliente123',
    '234.567.890-14',
    '(11) 97777-4444',
    'cliente'
),
(
    'Fernanda Oliveira',
    'fernanda@email.com',
    'cliente123',
    '234.567.890-15',
    '(11) 97777-5555',
    'cliente'
),

-- Entregadores
(
    'José Pereira',
    'jose.pereira@email.com',
    'entregador123',
    '345.678.901-22',
    '(11) 96666-1111',
    'entregador'
),
(
    'Antonio Ferreira',
    'antonio@email.com',
    'entregador123',
    '345.678.901-23',
    '(11) 96666-2222',
    'entregador'
),
(
    'Carlos Eduardo',
    'carlos.edu@email.com',
    'entregador123',
    '345.678.901-24',
    '(11) 96666-3333',
    'entregador'
),
(
    'Rafael Lima',
    'rafael.lima@email.com',
    'entregador123',
    '345.678.901-25',
    '(11) 95555-4444',
    'entregador'
),
(
    'Diego Santos',
    'diego@email.com',
    'entregador123',
    '345.678.901-26',
    '(11) 95555-5555',
    'entregador'
),

-- Restaurantes
(
    'McDonald\'s',
    'contato@mcdonalds.com',
    'restaurante123',
    '456.789.012-33',
    '(11) 3333-1111',
    'restaurante'
),
(
    'Burger King',
    'contato@burgerking.com',
    'restaurante123',
    '456.789.012-34',
    '(11) 3333-2222',
    'restaurante'
),
(
    'Pizza Hut',
    'contato@hut.com',
    'restaurante123',
    '456.789.012-35',
    '(11) 3333-3333',
    'restaurante'
),
(
    'Habib\'s',
    'contato@habibs.com',
    'restaurante123',
    '456.789.012-36',
    '(11) 3333-4444',
    'restaurante'
),
(
    'Gendai',
    'contato@gendai.com',
    'restaurante123',
    '456.789.012-37',
    '(11) 3333-5555',
    'restaurante'
);

-- Inserir endereços
INSERT INTO
    endereco (
        usuario_id,
        logradouro,
        numero,
        bairro,
        cidade,
        estado,
        cep,
        endereco_principal
    )
VALUES
    -- Clientes (6-10)
    (
        6,
        'Rua das Flores',
        '123',
        'Jardim Paulista',
        'São Paulo',
        'SP',
        '01234-567',
        TRUE
    ),
    (
        6,
        'Av. Paulista',
        '1000',
        'Bela Vista',
        'São Paulo',
        'SP',
        '01310-100',
        FALSE
    ),
    (
        7,
        'Rua Augusta',
        '500',
        'Consolação',
        'São Paulo',
        'SP',
        '01305-000',
        TRUE
    ),
    (
        8,
        'Alameda Santos',
        '1500',
        'Jardins',
        'São Paulo',
        'SP',
        '01418-200',
        TRUE
    ),
    (
        9,
        'Rua Oscar Freire',
        '800',
        'Jardins',
        'São Paulo',
        'SP',
        '01426-001',
        TRUE
    ),
    (
        10,
        'Av. Brigadeiro',
        '2000',
        'Jardim Paulista',
        'São Paulo',
        'SP',
        '01451-000',
        TRUE
    ),

-- Entregadores (11-15)
(
    11,
    'Rua dos Entregadores',
    '100',
    'Centro',
    'São Paulo',
    'SP',
    '01001-000',
    TRUE
),
(
    12,
    'Av. dos Estados',
    '200',
    'Centro',
    'São Paulo',
    'SP',
    '01002-000',
    TRUE
),
(
    13,
    'Rua das Entregas',
    '300',
    'Centro',
    'São Paulo',
    'SP',
    '01003-000',
    TRUE
),
(
    14,
    'Av. das Nações',
    '400',
    'Centro',
    'São Paulo',
    'SP',
    '01004-000',
    TRUE
),
(
    15,
    'Rua Rápida',
    '500',
    'Centro',
    'São Paulo',
    'SP',
    '01005-000',
    TRUE
),

-- Restaurantes (16-20)
(
    16,
    'Av. Paulista',
    '1500',
    'Bela Vista',
    'São Paulo',
    'SP',
    '01310-200',
    TRUE
),
(
    17,
    'Rua da Consolação',
    '2000',
    'Consolação',
    'São Paulo',
    'SP',
    '01302-000',
    TRUE
),
(
    18,
    'Av. Brasil',
    '1000',
    'Jardins',
    'São Paulo',
    'SP',
    '01430-000',
    TRUE
),
(
    19,
    'Rua 25 de Março',
    '500',
    'Centro',
    'São Paulo',
    'SP',
    '01021-000',
    TRUE
),
(
    20,
    'Av. Faria Lima',
    '3000',
    'Itaim Bibi',
    'São Paulo',
    'SP',
    '04538-000',
    TRUE
);

-- Inserir dados específicos
INSERT INTO
    administrador (id, cargo, nivel_acesso)
VALUES (
        1,
        'Gestor de Projeto',
        'master'
    ),
    (2, 'Gerente de TI', 'master'),
    (
        3,
        'Analista de Suporte',
        'gerente'
    ),
    (
        4,
        'Suporte Técnico',
        'suporte'
    ),
    (5, 'Suporte N2', 'suporte');

INSERT INTO
    cliente (
        id,
        data_nascimento,
        preferencias_pagamento
    )
VALUES (
        6,
        '1990-05-15',
        '{"metodo_preferido": "cartao_credito", "parcelamento": true}'
    ),
    (
        7,
        '1988-08-22',
        '{"metodo_preferido": "pix", "parcelamento": false}'
    ),
    (
        8,
        '1995-03-10',
        '{"metodo_preferido": "dinheiro", "parcelamento": false}'
    ),
    (
        9,
        '1985-12-01',
        '{"metodo_preferido": "cartao_debito", "parcelamento": true}'
    ),
    (
        10,
        '1992-07-18',
        '{"metodo_preferido": "cartao_credito", "parcelamento": true}'
    );

INSERT INTO
    entregador (
        id,
        cnh,
        tipo_veiculo,
        placa_veiculo,
        modelo_veiculo,
        status
    )
VALUES (
        11,
        '12345678901',
        'moto',
        'ABC-1234',
        'Honda CG 160',
        'disponivel'
    ),
    (
        12,
        '12345678902',
        'moto',
        'DEF-5678',
        'Yamaha Factor',
        'disponivel'
    ),
    (
        13,
        '12345678903',
        'carro',
        'GHI-9012',
        'Fiat Uno',
        'disponivel'
    ),
    (
        14,
        '12345678904',
        'bicicleta',
        NULL,
        'Caloi 10',
        'disponivel'
    ),
    (
        15,
        '12345678905',
        'moto',
        'JKL-3456',
        'Suzuki Intruder',
        'ocupado'
    );

INSERT INTO
    restaurante (
        id,
        razao_social,
        nome_fantasia,
        cnpj,
        descricao,
        horario_abertura,
        horario_fechamento,
        taxa_entrega,
        tempo_medio_entrega,
        categoria
    )
VALUES (
        16,
        'Arcos Dourados Ltda',
        'McDonald\'s',
        '12.345.678/0001-90',
        'Lanches rápidos',
        '10:00:00',
        '23:00:00',
        8.50,
        30,
        'Fast Food'
    ),
    (
        17,
        'BK Brasil Operação',
        'Burger King',
        '23.456.789/0001-01',
        'Hambúrgueres',
        '10:00:00',
        '23:00:00',
        7.50,
        35,
        'Fast Food'
    ),
    (
        18,
        'Pizza Hut Brasil',
        'Pizza Hut',
        '34.567.890/0001-12',
        'Pizzas',
        '18:00:00',
        '00:00:00',
        10.00,
        45,
        'Pizzaria'
    ),
    (
        19,
        'Habib\'s Alimentos',
        'Habib\'s',
        '45.678.901/0001-23',
        'Árabe e Lanches',
        '10:00:00',
        '22:00:00',
        6.00,
        25,
        'Fast Food'
    ),
    (
        20,
        'Gendai Japan Food',
        'Gendai',
        '56.789.012/0001-34',
        'Comida japonesa',
        '11:00:00',
        '22:00:00',
        9.00,
        40,
        'Japonesa'
    );

-- Inserir produtos
INSERT INTO
    produto (
        restaurante_id,
        nome,
        descricao,
        preco,
        categoria,
        tempo_preparo
    )
VALUES
    -- McDonald's
    (
        16,
        'Big Mac',
        'Sanduíche com dois hambúrgueres',
        32.90,
        'Sanduíches',
        10
    ),
    (
        16,
        'McFritas Média',
        'Batatas fritas',
        15.90,
        'Acompanhamentos',
        5
    ),
    (
        16,
        'McFlurry',
        'Sorvete com chocolate',
        14.90,
        'Sobremesas',
        3
    ),

-- Burger King
(
    17,
    'Whopper',
    'Sanduíche com hambúrguer',
    34.90,
    'Sanduíches',
    10
),
(
    17,
    'Batata Frita',
    'Batata média',
    14.90,
    'Acompanhamentos',
    5
),
(
    17,
    'Sundae',
    'Sorvete com calda',
    12.90,
    'Sobremesas',
    3
),

-- Pizza Hut
(
    18,
    'Pizza Calabresa',
    'Média com calabresa',
    45.90,
    'Pizzas',
    30
),
(
    18,
    'Pizza Portuguesa',
    'Média com presunto e ovos',
    49.90,
    'Pizzas',
    30
),
(
    18,
    'Borda Recheada',
    'Catupiry',
    8.90,
    'Extras',
    5
),

-- Habib's
(
    19,
    'Beirute',
    'Sanduíche especial',
    25.90,
    'Lanches',
    15
),
(
    19,
    'Esfirra Carne',
    'Esfirra aberta',
    6.90,
    'Esfirras',
    5
),
(
    19,
    'Kibe',
    'Porção 6 unidades',
    18.90,
    'Pasteis',
    10
),

-- Gendai
(
    20,
    'Combo Temakeria',
    'Temaki + Hot Roll',
    45.90,
    'Combos',
    20
),
(
    20,
    'Hot Roll 8 pçs',
    'Filadélfia',
    32.90,
    'Sushis',
    15
),
(
    20,
    'Sunomono',
    'Salada de pepino',
    12.90,
    'Entradas',
    5
);

-- Inserir pedidos
INSERT INTO
    pedido (
        cliente_id,
        restaurante_id,
        entregador_id,
        endereco_entrega_id,
        status,
        valor_subtotal,
        valor_taxa_entrega,
        valor_total,
        data_pedido,
        observacoes
    )
VALUES (
        6,
        16,
        11,
        1,
        'entregue',
        48.80,
        8.50,
        57.30,
        DATE_SUB(NOW(), INTERVAL 2 DAY),
        'Sem cebola'
    ),
    (
        6,
        17,
        12,
        1,
        'entregue',
        49.80,
        7.50,
        57.30,
        DATE_SUB(NOW(), INTERVAL 1 DAY),
        'Molho extra'
    ),
    (
        7,
        18,
        13,
        3,
        'em_entrega',
        99.80,
        10.00,
        109.80,
        NOW(),
        'Borda recheada'
    ),
    (
        8,
        19,
        14,
        4,
        'em_preparo',
        31.80,
        6.00,
        37.80,
        NOW(),
        'Sem cebola'
    ),
    (
        9,
        20,
        NULL,
        5,
        'aguardando_pagamento',
        58.80,
        9.00,
        67.80,
        NOW(),
        'Shoyu à parte'
    ),
    (
        10,
        16,
        15,
        6,
        'cancelamento_solicitado',
        32.90,
        8.50,
        41.40,
        NOW(),
        'Cancelar por atraso'
    );

-- Inserir itens dos pedidos
INSERT INTO
    item_pedido (
        pedido_id,
        produto_id,
        quantidade,
        preco_unitario,
        observacoes
    )
VALUES
    -- Pedido 1
    (1, 1, 1, 32.90, ''),
    (1, 2, 1, 15.90, 'Sem sal'),
    -- Pedido 2
    (2, 4, 1, 34.90, ''),
    (2, 5, 1, 14.90, ''),
    -- Pedido 3
    (3, 7, 1, 45.90, ''),
    (3, 9, 1, 8.90, ''),
    (3, 8, 1, 45.00, ''),
    -- Pedido 4
    (4, 10, 1, 25.90, ''),
    (4, 12, 1, 5.90, ''),
    -- Pedido 5
    (5, 13, 1, 45.90, ''),
    (5, 15, 1, 12.90, ''),
    -- Pedido 6
    (6, 1, 1, 32.90, '');

-- Inserir pagamentos
INSERT INTO
    pagamento (
        pedido_id,
        metodo,
        status,
        valor,
        data_pagamento,
        codigo_transacao
    )
VALUES (
        1,
        'cartao_credito',
        'aprovado',
        57.30,
        DATE_SUB(NOW(), INTERVAL 2 DAY),
        'trans_001'
    ),
    (
        2,
        'pix',
        'aprovado',
        57.30,
        DATE_SUB(NOW(), INTERVAL 1 DAY),
        'trans_002'
    ),
    (
        3,
        'cartao_credito',
        'aprovado',
        109.80,
        NOW(),
        'trans_003'
    ),
    (
        4,
        'dinheiro',
        'pendente',
        37.80,
        NULL,
        NULL
    ),
    (
        5,
        'pix',
        'pendente',
        67.80,
        NULL,
        NULL
    ),
    (
        6,
        'cartao_debito',
        'aprovado',
        41.40,
        NOW(),
        'trans_006'
    );

-- Inserir cancelamento de pedido
INSERT INTO
    cancelamento_pedido (
        pedido_id,
        solicitado_por,
        motivo,
        descricao_motivo,
        data_solicitacao,
        status
    )
VALUES (
        6,
        10,
        'atraso_entrega',
        'Pedido atrasou mais de 30 minutos',
        NOW(),
        'pendente'
    );

-- Inserir rastreamento de entregas
INSERT INTO
    rastreamento_entrega (
        pedido_id,
        latitude,
        longitude
    )
VALUES (1, -23.550520, -46.633308),
    (2, -23.551234, -46.634567),
    (3, -23.552345, -46.635678),
    (6, -23.553456, -46.636789);

-- Inserir logs de atividade
INSERT INTO
    log_atividade (
        usuario_id,
        acao,
        descricao,
        ip_address
    )
VALUES (
        6,
        'pedido_realizado',
        'Pedido #1 realizado',
        '192.168.1.100'
    ),
    (
        6,
        'pedido_recebido',
        'Pedido #1 entregue',
        '192.168.1.100'
    ),
    (
        6,
        'pedido_realizado',
        'Pedido #2 realizado',
        '192.168.1.100'
    ),
    (
        7,
        'pedido_realizado',
        'Pedido #3 realizado',
        '192.168.1.101'
    ),
    (
        8,
        'pedido_realizado',
        'Pedido #4 realizado',
        '192.168.1.102'
    ),
    (
        9,
        'pedido_realizado',
        'Pedido #5 realizado',
        '192.168.1.103'
    ),
    (
        10,
        'cancelamento_solicitado',
        'Solicitou cancelamento do pedido #6',
        '192.168.1.104'
    );

-- =====================================================
-- CONSULTAS PARA VERIFICAR CARDINALIDADES
-- =====================================================

-- 1. Usuário → Endereço (1:N)
SELECT u.nome, u.tipo_usuario, COUNT(e.id) as qtd_enderecos
FROM usuario u
    LEFT JOIN endereco e ON u.id = e.usuario_id
GROUP BY
    u.id;

-- 2. Restaurante → Produto (1:N)
SELECT r.nome_fantasia as restaurante, COUNT(p.id) as qtd_produtos
FROM restaurante r
    LEFT JOIN produto p ON r.id = p.restaurante_id
GROUP BY
    r.id;

-- 3. Cliente → Pedido (1:N)
SELECT u.nome as cliente, COUNT(ped.id) as qtd_pedidos
FROM
    usuario u
    JOIN cliente c ON u.id = c.id
    LEFT JOIN pedido ped ON c.id = ped.cliente_id
GROUP BY
    u.id;

-- 4. Pedido → Item_Pedido (1:N)
SELECT ped.id as pedido, COUNT(ip.id) as qtd_itens
FROM pedido ped
    LEFT JOIN item_pedido ip ON ped.id = ip.pedido_id
GROUP BY
    ped.id;

-- 5. Pedido → Pagamento (1:1)
SELECT ped.id as pedido, pag.status, pag.valor
FROM pedido ped
    JOIN pagamento pag ON ped.id = pag.pedido_id;

-- 6. Pedido → Cancelamento_Pedido (1:1)
SELECT ped.id as pedido, cp.status as status_cancelamento
FROM
    pedido ped
    LEFT JOIN cancelamento_pedido cp ON ped.id = cp.pedido_id;

-- 7. Entregador → Pedido (1:N)
SELECT
    u.nome as entregador,
    COUNT(ped.id) as entregas_realizadas
FROM
    usuario u
    JOIN entregador e ON u.id = e.id
    LEFT JOIN pedido ped ON e.id = ped.entregador_id
GROUP BY
    u.id;

-- 8. Usuário → Sessao_Login (1:N)
SELECT u.nome, COUNT(sl.id) as qtd_sessoes
FROM usuario u
    LEFT JOIN sessao_login sl ON u.id = sl.usuario_id
GROUP BY
    u.id;

-- =====================================================
-- EXEMPLOS DE USO DAS PROCEDURES
-- =====================================================

/*
-- Exemplo 1: Realizar login
CALL sp_realizar_login('ana.souza@email.com', 'cliente123', '192.168.1.100', 'iPhone 12');

-- Exemplo 2: Solicitar cancelamento
CALL sp_solicitar_cancelamento_pedido(6, 10, 'atraso_entrega', 'Entrega atrasou muito');

-- Exemplo 3: Processar cancelamento (admin)
CALL sp_processar_cancelamento(1, 1, 'aprovado', 'Reembolso processado');

-- Exemplo 4: Criar novo pedido
CALL sp_criar_pedido(6, 16, 1, 'Sem cebola', 'cartao_credito', '{"numero":"**** **** **** 1234"}');

-- Exemplo 5: Adicionar item ao pedido
CALL sp_adicionar_item_pedido(7, 1, 1, '');
*/

-- =====================================================
-- COMENTÁRIOS SOBRE AS CARDINALIDADES
-- =====================================================
/* 
CARDINALIDADES DO BANCO DE DADOS:

1. Usuário → Endereço: 1 : N
- Um usuário pode ter vários endereços
- Cada endereço pertence a um único usuário

2. Usuário → Cliente/Entregador/Restaurante/Admin: 1 : 1
- Um usuário tem um único perfil específico
- Cada perfil pertence a um único usuário

3. Restaurante → Produto: 1 : N
- Um restaurante pode ter vários produtos
- Cada produto pertence a um único restaurante

4. Cliente → Pedido: 1 : N
- Um cliente pode fazer vários pedidos
- Cada pedido pertence a um único cliente

5. Restaurante → Pedido: 1 : N
- Um restaurante pode receber vários pedidos
- Cada pedido é de um único restaurante

6. Entregador → Pedido: 1 : N
- Um entregador pode realizar várias entregas
- Cada pedido pode ter um único entregador (0 ou 1)

7. Pedido → Item_Pedido: 1 : N
- Um pedido pode ter vários itens
- Cada item pertence a um único pedido

8. Pedido → Pagamento: 1 : 1
- Um pedido tem um único pagamento
- Cada pagamento é de um único pedido

9. Pedido → Cancelamento_Pedido: 1 : 1
- Um pedido pode ter no máximo um cancelamento
- Cada cancelamento é de um único pedido

10. Pedido → Rastreamento_Entrega: 1 : N
- Um pedido pode ter vários pontos de rastreamento
- Cada rastreamento pertence a um único pedido

11. Usuário → Sessao_Login: 1 : N
- Um usuário pode ter várias sessões ativas
- Cada sessão pertence a um único usuário

12. Usuário → Log_Atividade: 1 : N
- Um usuário pode ter várias atividades registradas
- Cada atividade pertence a um único usuário
*/

-- =====================================================
-- TABELA DE VALIDAÇÃO E VERIFICAÇÃO DE DADOS
-- =====================================================

CREATE TABLE validacao_sistema (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_validacao ENUM(
        'usuario_existe',
        'email_valido',
        'cpf_valido',
        'cnpj_valido',
        'restaurante_ativo',
        'entregador_disponivel',
        'produto_disponivel',
        'endereco_valido',
        'pedido_existe',
        'status_permitido',
        'cancelamento_pendente',
        'pagamento_pendente',
        'sessao_ativa',
        'dados_obrigatorios'
    ) NOT NULL,
    tabela_verificada VARCHAR(50) NOT NULL,
    id_registro INT NOT NULL,
    status_validacao BOOLEAN DEFAULT FALSE,
    data_verificacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    usuario_verificador_id INT,
    resultado_json JSON,
    mensagem_erro TEXT,
    INDEX idx_tabela_id (
        tabela_verificada,
        id_registro
    ),
    INDEX idx_status (status_validacao),
    FOREIGN KEY (usuario_verificador_id) REFERENCES usuario (id)
);

-- =====================================================
-- PROCEDURE PARA VALIDAR EXISTÊNCIA DE REGISTROS
-- =====================================================

DELIMITER / /

CREATE PROCEDURE sp_validar_existencia_registro(
    IN p_tabela VARCHAR(50),
    IN p_id_registro INT,
    IN p_tipo_validacao ENUM('usuario_existe', 'email_valido', 'cpf_valido', 'cnpj_valido', 
                             'restaurante_ativo', 'entregador_disponivel', 'produto_disponivel',
                             'endereco_valido', 'pedido_existe', 'status_permitido')
)
BEGIN
    DECLARE v_existe INT DEFAULT 0;
    DECLARE v_status_ativo BOOLEAN DEFAULT FALSE;
    DECLARE v_resultado JSON;
    DECLARE v_mensagem TEXT;
    
    -- Validação baseada na tabela e tipo
    CASE 
        -- VALIDAÇÃO DE USUÁRIOS
        WHEN p_tabela = 'usuario' AND p_tipo_validacao = 'usuario_existe' THEN
            SELECT COUNT(*) INTO v_existe 
            FROM usuario 
            WHERE id = p_id_registro;
            
            IF v_existe > 0 THEN
                SET v_resultado = JSON_OBJECT(
                    'id', p_id_registro,
                    'existe', TRUE,
                    'tabela', 'usuario'
                );
                SET v_mensagem = 'Usuário encontrado';
            ELSE
                SET v_resultado = JSON_OBJECT(
                    'id', p_id_registro,
                    'existe', FALSE,
                    'tabela', 'usuario'
                );
                SET v_mensagem = 'Usuário não encontrado';
            END IF;
            
        -- VALIDAÇÃO DE EMAIL
        WHEN p_tabela = 'usuario' AND p_tipo_validacao = 'email_valido' THEN
            SELECT COUNT(*) INTO v_existe 
            FROM usuario 
            WHERE email = (SELECT email FROM usuario WHERE id = p_id_registro)
            AND id != p_id_registro;
            
            IF v_existe = 0 THEN
                SET v_resultado = JSON_OBJECT(
                    'email', (SELECT email FROM usuario WHERE id = p_id_registro),
                    'valido', TRUE
                );
                SET v_mensagem = 'Email disponível';
            ELSE
                SET v_resultado = JSON_OBJECT(
                    'email', (SELECT email FROM usuario WHERE id = p_id_registro),
                    'valido', FALSE
                );
                SET v_mensagem = 'Email já cadastrado para outro usuário';
            END IF;
            
        -- VALIDAÇÃO DE CPF
        WHEN p_tabela = 'usuario' AND p_tipo_validacao = 'cpf_valido' THEN
            SELECT COUNT(*) INTO v_existe 
            FROM usuario 
            WHERE cpf = (SELECT cpf FROM usuario WHERE id = p_id_registro)
            AND id != p_id_registro;
            
            IF v_existe = 0 THEN
                SET v_resultado = JSON_OBJECT(
                    'cpf', (SELECT cpf FROM usuario WHERE id = p_id_registro),
                    'valido', TRUE
                );
                SET v_mensagem = 'CPF disponível';
            ELSE
                SET v_resultado = JSON_OBJECT(
                    'cpf', (SELECT cpf FROM usuario WHERE id = p_id_registro),
                    'valido', FALSE
                );
                SET v_mensagem = 'CPF já cadastrado para outro usuário';
            END IF;
            
        -- VALIDAÇÃO DE RESTAURANTE ATIVO
        WHEN p_tabela = 'restaurante' AND p_tipo_validacao = 'restaurante_ativo' THEN
            SELECT ativo INTO v_status_ativo 
            FROM restaurante 
            WHERE id = p_id_registro;
            
            IF v_status_ativo = 1 THEN
                SET v_resultado = JSON_OBJECT(
                    'restaurante_id', p_id_registro,
                    'nome', (SELECT nome_fantasia FROM restaurante WHERE id = p_id_registro),
                    'ativo', TRUE
                );
                SET v_mensagem = 'Restaurante ativo';
            ELSE
                SET v_resultado = JSON_OBJECT(
                    'restaurante_id', p_id_registro,
                    'ativo', FALSE
                );
                SET v_mensagem = 'Restaurante inativo ou não encontrado';
            END IF;
            
        -- VALIDAÇÃO DE ENTREGADOR DISPONÍVEL
        WHEN p_tabela = 'entregador' AND p_tipo_validacao = 'entregador_disponivel' THEN
            SELECT COUNT(*) INTO v_existe 
            FROM entregador 
            WHERE id = p_id_registro AND status = 'disponivel';
            
            IF v_existe > 0 THEN
                SET v_resultado = JSON_OBJECT(
                    'entregador_id', p_id_registro,
                    'disponivel', TRUE
                );
                SET v_mensagem = 'Entregador disponível';
            ELSE
                SET v_resultado = JSON_OBJECT(
                    'entregador_id', p_id_registro,
                    'disponivel', FALSE
                );
                SET v_mensagem = 'Entregador não disponível';
            END IF;
            
        -- VALIDAÇÃO DE PRODUTO DISPONÍVEL
        WHEN p_tabela = 'produto' AND p_tipo_validacao = 'produto_disponivel' THEN
            SELECT disponivel INTO v_status_ativo 
            FROM produto 
            WHERE id = p_id_registro;
            
            IF v_status_ativo = 1 THEN
                SET v_resultado = JSON_OBJECT(
                    'produto_id', p_id_registro,
                    'nome', (SELECT nome FROM produto WHERE id = p_id_registro),
                    'disponivel', TRUE
                );
                SET v_mensagem = 'Produto disponível';
            ELSE
                SET v_resultado = JSON_OBJECT(
                    'produto_id', p_id_registro,
                    'disponivel', FALSE
                );
                SET v_mensagem = 'Produto indisponível';
            END IF;
            
        -- VALIDAÇÃO DE ENDEREÇO VÁLIDO
        WHEN p_tabela = 'endereco' AND p_tipo_validacao = 'endereco_valido' THEN
            SELECT COUNT(*) INTO v_existe 
            FROM endereco 
            WHERE id = p_id_registro 
            AND logradouro IS NOT NULL 
            AND cep IS NOT NULL;
            
            IF v_existe > 0 THEN
                SET v_resultado = JSON_OBJECT(
                    'endereco_id', p_id_registro,
                    'completo', TRUE
                );
                SET v_mensagem = 'Endereço válido';
            ELSE
                SET v_resultado = JSON_OBJECT(
                    'endereco_id', p_id_registro,
                    'completo', FALSE
                );
                SET v_mensagem = 'Endereço incompleto ou inválido';
            END IF;
            
        ELSE
            SET v_mensagem = 'Tipo de validação não implementado';
    END CASE;
    
    -- Inserir resultado da validação
    INSERT INTO validacao_sistema (
        tipo_validacao,
        tabela_verificada,
        id_registro,
        status_validacao,
        resultado_json,
        mensagem_erro
    ) VALUES (
        p_tipo_validacao,
        p_tabela,
        p_id_registro,
        (v_existe > 0 OR v_status_ativo = 1),
        v_resultado,
        v_mensagem
    );
    
    -- Retornar resultado
    SELECT 
        v_mensagem as resultado,
        v_resultado as detalhes,
        (v_existe > 0 OR v_status_ativo = 1) as valido;
    
END//

-- =====================================================
-- VIEW PARA CONSULTAR VALIDAÇÕES RECENTES
-- =====================================================

CREATE VIEW view_validacoes_recentes AS
SELECT
    v.id,
    v.tipo_validacao,
    v.tabela_verificada,
    v.id_registro,
    CASE
        WHEN v.status_validacao = 1 THEN '✅ Válido'
        ELSE '❌ Inválido'
    END as status,
    v.data_verificacao,
    v.resultado_json,
    v.mensagem_erro,
    u.nome as verificado_por
FROM
    validacao_sistema v
    LEFT JOIN usuario u ON v.usuario_verificador_id = u.id
ORDER BY v.data_verificacao DESC;

-- =====================================================
-- FUNÇÃO PARA VERIFICAR SE REGISTRO EXISTE
-- =====================================================

CREATE FUNCTION fn_registro_existe(
    p_tabela VARCHAR(50),
    p_id INT
) RETURNS BOOLEAN
DETERMINISTIC
BEGIN
    DECLARE v_existe INT;
    
    CASE p_tabela
        WHEN 'usuario' THEN
            SELECT COUNT(*) INTO v_existe FROM usuario WHERE id = p_id;
        WHEN 'restaurante' THEN
            SELECT COUNT(*) INTO v_existe FROM restaurante WHERE id = p_id;
        WHEN 'entregador' THEN
            SELECT COUNT(*) INTO v_existe FROM entregador WHERE id = p_id;
        WHEN 'cliente' THEN
            SELECT COUNT(*) INTO v_existe FROM cliente WHERE id = p_id;
        WHEN 'pedido' THEN
            SELECT COUNT(*) INTO v_existe FROM pedido WHERE id = p_id;
        WHEN 'produto' THEN
            SELECT COUNT(*) INTO v_existe FROM produto WHERE id = p_id;
        ELSE
            SET v_existe = 0;
    END CASE;
    
    RETURN v_existe > 0;
END//

DELIMITER;

-- =====================================================
-- EXEMPLOS DE USO DA VALIDAÇÃO
-- =====================================================

/*
-- Exemplo 1: Verificar se usuário existe
CALL sp_validar_existencia_registro('usuario', 6, 'usuario_existe');

-- Exemplo 2: Verificar se email é válido (não duplicado)
CALL sp_validar_existencia_registro('usuario', 7, 'email_valido');

-- Exemplo 3: Verificar se restaurante está ativo
CALL sp_validar_existencia_registro('restaurante', 16, 'restaurante_ativo');

-- Exemplo 4: Verificar disponibilidade de entregador
CALL sp_validar_existencia_registro('entregador', 11, 'entregador_disponivel');

-- Exemplo 5: Verificar disponibilidade de produto
CALL sp_validar_existencia_registro('produto', 1, 'produto_disponivel');

-- Exemplo 6: Verificar validade de endereço
CALL sp_validar_existencia_registro('endereco', 1, 'endereco_valido');
*/

-- =====================================================
-- CONSULTA PARA VERIFICAR VALIDAÇÕES RECENTES
-- =====================================================

SELECT * FROM view_validacoes_recentes LIMIT 10;