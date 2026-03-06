-- Criação do banco de dados
CREATE DATABASE IF NOT EXISTS sistema_entrega;
USE sistema_entrega;

-- Tabela CLIENTE
CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    endereco_principal TEXT,
    data_cadastro DATE NOT NULL
);

-- Tabela RESTAURANTE
CREATE TABLE restaurante (
    id_restaurante INT AUTO_INCREMENT PRIMARY KEY,
    nome_fantasia VARCHAR(255) NOT NULL,
    razao_social VARCHAR(255) NOT NULL,
    cnpj VARCHAR(18) UNIQUE NOT NULL,
    endereco TEXT NOT NULL,
    telefone VARCHAR(20) NOT NULL
);

-- Tabela ENTREGADOR
CREATE TABLE entregador (
    id_entregador INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    placa_veiculo VARCHAR(10) NOT NULL,
    status ENUM('disponível', 'ocupado', 'indisponível') DEFAULT 'disponível'
);

-- Tabela PEDIDO
CREATE TABLE pedido (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_restaurante INT NOT NULL,
    id_entregador INT,
    data_hora DATETIME NOT NULL,
    status ENUM('pendente', 'confirmado', 'preparando', 'saiu_entrega', 'entregue', 'cancelado') DEFAULT 'pendente',
    valor_total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    FOREIGN KEY (id_restaurante) REFERENCES restaurante(id_restaurante),
    FOREIGN KEY (id_entregador) REFERENCES entregador(id_entregador)
);

-- Tabela ITEM
CREATE TABLE item (
    id_item INT AUTO_INCREMENT PRIMARY KEY,
    id_restaurante INT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10,2) NOT NULL,
    categoria VARCHAR(100),
    FOREIGN KEY (id_restaurante) REFERENCES restaurante(id_restaurante)
);

-- Tabela ENTREGA
CREATE TABLE entrega (
    id_entrega INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL UNIQUE, -- Relacionamento 1:1 com pedido
    id_entregador INT NOT NULL,
    data_inicio DATETIME NOT NULL,
    data_fim DATETIME,
    status ENUM('aguardando', 'em_andamento', 'concluida', 'problemas') DEFAULT 'aguardando',
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),
    FOREIGN KEY (id_entregador) REFERENCES entregador(id_entregador)
);

-- Tabela PAGAMENTO
CREATE TABLE pagamento (
    id_pagamento INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL UNIQUE, -- Relacionamento 1:1 com pedido
    metodo ENUM('dinheiro', 'cartao_credito', 'cartao_debito', 'pix', 'vale_refeicao') NOT NULL,
    status ENUM('pendente', 'processando', 'aprovado', 'recusado', 'estornado') DEFAULT 'pendente',
    valor DECIMAL(10,2) NOT NULL,
    data_pagamento DATETIME,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido)
);

-- Tabela ITEM_PEDIDO (tabela associativa para o relacionamento N:N entre PEDIDO e ITEM)
CREATE TABLE item_pedido (
    id_item_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_item INT NOT NULL,
    quantidade INT NOT NULL CHECK (quantidade > 0),
    preco_unitario DECIMAL(10,2) NOT NULL,
    observacoes TEXT,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),
    FOREIGN KEY (id_item) REFERENCES item(id_item),
    UNIQUE KEY unique_pedido_item (id_pedido, id_item) -- Evita itens duplicados no mesmo pedido
);

-- Índices para melhor performance
CREATE INDEX idx_cliente_email ON cliente(email);
CREATE INDEX idx_restaurante_cnpj ON restaurante(cnpj);
CREATE INDEX idx_entregador_cpf ON entregador(cpf);
CREATE INDEX idx_pedido_cliente ON pedido(id_cliente);
CREATE INDEX idx_pedido_restaurante ON pedido(id_restaurante);
CREATE INDEX idx_pedido_entregador ON pedido(id_entregador);
CREATE INDEX idx_pedido_data ON pedido(data_hora);
CREATE INDEX idx_item_restaurante ON item(id_restaurante);
CREATE INDEX idx_entrega_pedido ON entrega(id_pedido);
CREATE INDEX idx_entrega_entregador ON entrega(id_entregador);
CREATE INDEX idx_pagamento_pedido ON pagamento(id_pedido);

-- Cardinalidades e comentários explicativos
/*
CARDINALIDADES:

1. CLIENTE (1) ---- (N) PEDIDO
   - Um cliente pode fazer vários pedidos
   - Um pedido pertence a um único cliente

2. RESTAURANTE (1) ---- (N) PEDIDO
   - Um restaurante pode receber vários pedidos
   - Um pedido é de um único restaurante

3. ENTREGADOR (1) ---- (N) PEDIDO
   - Um entregador pode fazer várias entregas (pedidos)
   - Um pedido pode ter um entregador (ou nenhum, se ainda não atribuído)

4. RESTAURANTE (1) ---- (N) ITEM
   - Um restaurante pode ter vários itens no cardápio
   - Um item pertence a um único restaurante

5. PEDIDO (1) ---- (1) ENTREGA
   - Um pedido tem uma única entrega
   - Uma entrega pertence a um único pedido

6. PEDIDO (1) ---- (1) PAGAMENTO
   - Um pedido tem um único pagamento
   - Um pagamento pertence a um único pedido

7. PEDIDO (N) ---- (N) ITEM via ITEM_PEDIDO
   - Um pedido pode ter vários itens
   - Um item pode estar em vários pedidos
   - A tabela ITEM_PEDIDO resolve a relação muitos-para-muitos

8. ENTREGADOR (1) ---- (N) ENTREGA
   - Um entregador pode fazer várias entregas
   - Uma entrega pertence a um único entregador
*/