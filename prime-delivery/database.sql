-- Criação do banco de dados
CREATE DATABASE IF NOT EXISTS prime_delivery;
USE prime_delivery;

-- Tabela de clientes
CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de entregadores
CREATE TABLE entregadores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    veiculo VARCHAR(50),
    status ENUM('disponivel', 'ocupado', 'offline') DEFAULT 'disponivel',
    avaliacao DECIMAL(3,2) DEFAULT 5.00,
    entregas INT DEFAULT 0,
    lat DECIMAL(10,8),
    lng DECIMAL(11,8),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de pedidos
CREATE TABLE pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    entregador_id INT NULL,
    restaurante VARCHAR(100) NOT NULL,
    produto VARCHAR(100) NOT NULL,
    quantidade INT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    status_text VARCHAR(100),
    endereco VARCHAR(255) NOT NULL,
    metodo_pagamento VARCHAR(50),
    avaliacao INT,
    comentario TEXT,
    data_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    FOREIGN KEY (entregador_id) REFERENCES entregadores(id)
);

-- Tabela de mensagens (chat)
CREATE TABLE mensagens (
    id INT AUTO_INCREMENT PRIMARY KEY,
    remetente_id INT NOT NULL,
    remetente_tipo ENUM('cliente', 'entregador', 'admin') NOT NULL,
    destinatario_id INT NOT NULL,
    destinatario_tipo ENUM('cliente', 'entregador', 'admin') NOT NULL,
    mensagem TEXT NOT NULL,
    lida BOOLEAN DEFAULT FALSE,
    data_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Inserir admin padrão
INSERT INTO clientes (nome, email, senha) VALUES ('Administrador', 'admin@gmail.com', MD5('123456'));

-- Inserir entregadores exemplo
INSERT INTO entregadores (nome, email, senha, veiculo) VALUES 
('José Pereira', 'jose@email.com', MD5('123456'), 'Moto Honda'),
('Antonio Ferreira', 'antonio@email.com', MD5('123456'), 'Moto Yamaha');

-- Inserir clientes exemplo
INSERT INTO clientes (nome, email, senha) VALUES 
('Ana Souza', 'ana@email.com', MD5('123456')),
('Carlos Lima', 'carlos@email.com', MD5('123456'));