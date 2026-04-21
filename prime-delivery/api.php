<?php
require_once 'conexao.php';

$action = $_GET['action'] ?? '';

switch($action) {
    
    // LOGIN
    case 'login':
        $email = $_POST['email'];
        $senha = md5($_POST['senha']);
        $tipo = $_POST['tipo'];
        
        if ($tipo == 'admin' && $email == 'admin@gmail.com' && $_POST['senha'] == '123456') {
            echo json_encode(['success' => true, 'user' => ['id' => 99, 'nome' => 'Admin', 'role' => 'admin']]);
        } elseif ($tipo == 'cliente') {
            $sql = "SELECT * FROM clientes WHERE email = '$email' AND senha = '$senha'";
            $result = $conn->query($sql);
            if ($row = $result->fetch_assoc()) {
                echo json_encode(['success' => true, 'user' => ['id' => $row['id'], 'nome' => $row['nome'], 'role' => 'cliente']]);
            } else {
                echo json_encode(['success' => false, 'message' => 'Credenciais inválidas']);
            }
        } elseif ($tipo == 'entregador') {
            $sql = "SELECT * FROM entregadores WHERE email = '$email' AND senha = '$senha'";
            $result = $conn->query($sql);
            if ($row = $result->fetch_assoc()) {
                echo json_encode(['success' => true, 'user' => ['id' => $row['id'], 'nome' => $row['nome'], 'role' => 'entregador', 'veiculo' => $row['veiculo']]]);
            } else {
                echo json_encode(['success' => false, 'message' => 'Credenciais inválidas']);
            }
        }
        break;
    
    // CADASTRO
    case 'cadastrar':
        $nome = $_POST['nome'];
        $email = $_POST['email'];
        $senha = md5($_POST['senha']);
        $tipo = $_POST['tipo'];
        
        if ($tipo == 'cliente') {
            $sql = "INSERT INTO clientes (nome, email, senha) VALUES ('$nome', '$email', '$senha')";
            if ($conn->query($sql)) {
                echo json_encode(['success' => true, 'id' => $conn->insert_id]);
            } else {
                echo json_encode(['success' => false, 'message' => 'Email já cadastrado']);
            }
        } else {
            $veiculo = $_POST['veiculo'];
            $sql = "INSERT INTO entregadores (nome, email, senha, veiculo) VALUES ('$nome', '$email', '$senha', '$veiculo')";
            if ($conn->query($sql)) {
                echo json_encode(['success' => true, 'id' => $conn->insert_id]);
            } else {
                echo json_encode(['success' => false, 'message' => 'Email já cadastrado']);
            }
        }
        break;
    
    // LISTAR PEDIDOS
    case 'listar_pedidos':
        $cliente_id = $_GET['cliente_id'] ?? 0;
        $entregador_id = $_GET['entregador_id'] ?? 0;
        
        if ($cliente_id > 0) {
            $sql = "SELECT * FROM pedidos WHERE cliente_id = $cliente_id ORDER BY id DESC";
        } elseif ($entregador_id > 0) {
            $sql = "SELECT * FROM pedidos WHERE entregador_id = $entregador_id ORDER BY id DESC";
        } else {
            $sql = "SELECT p.*, c.nome as cliente_nome, e.nome as entregador_nome 
                    FROM pedidos p 
                    LEFT JOIN clientes c ON p.cliente_id = c.id 
                    LEFT JOIN entregadores e ON p.entregador_id = e.id 
                    ORDER BY p.id DESC";
        }
        
        $result = $conn->query($sql);
        $pedidos = [];
        while ($row = $result->fetch_assoc()) {
            $pedidos[] = $row;
        }
        echo json_encode($pedidos);
        break;
    
    // CRIAR PEDIDO
    case 'criar_pedido':
        $cliente_id = $_POST['cliente_id'];
        $restaurante = $_POST['restaurante'];
        $produto = $_POST['produto'];
        $quantidade = $_POST['quantidade'];
        $valor = $_POST['valor'];
        $endereco = $_POST['endereco'];
        $metodo = $_POST['metodo'];
        
        $sql = "INSERT INTO pedidos (cliente_id, restaurante, produto, quantidade, valor, status, status_text, endereco, metodo_pagamento) 
                VALUES ($cliente_id, '$restaurante', '$produto', $quantidade, $valor, 'pagamento_aprovado', '✅ Pagamento aprovado', '$endereco', '$metodo')";
        
        if ($conn->query($sql)) {
            echo json_encode(['success' => true, 'id' => $conn->insert_id]);
        } else {
            echo json_encode(['success' => false, 'message' => $conn->error]);
        }
        break;
    
    // ATUALIZAR STATUS DO PEDIDO
    case 'atualizar_status':
        $pedido_id = $_POST['pedido_id'];
        $status = $_POST['status'];
        $status_text = $_POST['status_text'];
        
        $sql = "UPDATE pedidos SET status = '$status', status_text = '$status_text' WHERE id = $pedido_id";
        $conn->query($sql);
        echo json_encode(['success' => true]);
        break;
    
    // ACEITAR ENTREGA
    case 'aceitar_entrega':
        $pedido_id = $_POST['pedido_id'];
        $entregador_id = $_POST['entregador_id'];
        
        $sql = "UPDATE pedidos SET entregador_id = $entregador_id, status = 'em_entrega', status_text = '🚚 Em rota de entrega' WHERE id = $pedido_id";
        $conn->query($sql);
        
        $sql2 = "UPDATE entregadores SET status = 'ocupado' WHERE id = $entregador_id";
        $conn->query($sql2);
        
        echo json_encode(['success' => true]);
        break;
    
    // CONFIRMAR ENTREGA
    case 'confirmar_entrega':
        $pedido_id = $_POST['pedido_id'];
        $entregador_id = $_POST['entregador_id'];
        
        $sql = "UPDATE pedidos SET status = 'entregue', status_text = '🏁 Entregue' WHERE id = $pedido_id";
        $conn->query($sql);
        
        $sql2 = "UPDATE entregadores SET status = 'disponivel', entregas = entregas + 1 WHERE id = $entregador_id";
        $conn->query($sql2);
        
        echo json_encode(['success' => true]);
        break;
    
    // ENVIAR MENSAGEM
    case 'enviar_mensagem':
        $remetente_id = $_POST['remetente_id'];
        $remetente_tipo = $_POST['remetente_tipo'];
        $destinatario_id = $_POST['destinatario_id'];
        $destinatario_tipo = $_POST['destinatario_tipo'];
        $mensagem = addslashes($_POST['mensagem']);
        
        $sql = "INSERT INTO mensagens (remetente_id, remetente_tipo, destinatario_id, destinatario_tipo, mensagem) 
                VALUES ($remetente_id, '$remetente_tipo', $destinatario_id, '$destinatario_tipo', '$mensagem')";
        $conn->query($sql);
        echo json_encode(['success' => true]);
        break;
    
    // LISTAR MENSAGENS
    case 'listar_mensagens':
        $usuario_id = $_GET['usuario_id'];
        $usuario_tipo = $_GET['usuario_tipo'];
        $contato_id = $_GET['contato_id'];
        $contato_tipo = $_GET['contato_tipo'];
        
        $sql = "SELECT * FROM mensagens WHERE 
                (remetente_id = $usuario_id AND remetente_tipo = '$usuario_tipo' AND destinatario_id = $contato_id AND destinatario_tipo = '$contato_tipo')
                OR 
                (remetente_id = $contato_id AND remetente_tipo = '$contato_tipo' AND destinatario_id = $usuario_id AND destinatario_tipo = '$usuario_tipo')
                ORDER BY data_envio ASC";
        
        $result = $conn->query($sql);
        $mensagens = [];
        while ($row = $result->fetch_assoc()) {
            $mensagens[] = $row;
        }
        echo json_encode($mensagens);
        break;
    
    // LISTAR ENTREGADORES
    case 'listar_entregadores':
        $sql = "SELECT * FROM entregadores";
        $result = $conn->query($sql);
        $entregadores = [];
        while ($row = $result->fetch_assoc()) {
            $entregadores[] = $row;
        }
        echo json_encode($entregadores);
        break;
    
    // LISTAR CLIENTES
    case 'listar_clientes':
        $sql = "SELECT * FROM clientes";
        $result = $conn->query($sql);
        $clientes = [];
        while ($row = $result->fetch_assoc()) {
            $clientes[] = $row;
        }
        echo json_encode($clientes);
        break;
    
    // AVALIAR PEDIDO
    case 'avaliar_pedido':
        $pedido_id = $_POST['pedido_id'];
        $nota = $_POST['nota'];
        $comentario = addslashes($_POST['comentario']);
        
        $sql = "UPDATE pedidos SET avaliacao = $nota, comentario = '$comentario' WHERE id = $pedido_id";
        $conn->query($sql);
        
        // Atualizar média do entregador
        $sql2 = "SELECT AVG(avaliacao) as media FROM pedidos WHERE entregador_id = (SELECT entregador_id FROM pedidos WHERE id = $pedido_id) AND avaliacao IS NOT NULL";
        $result = $conn->query($sql2);
        $row = $result->fetch_assoc();
        if ($row['media']) {
            $conn->query("UPDATE entregadores SET avaliacao = {$row['media']} WHERE id = (SELECT entregador_id FROM pedidos WHERE id = $pedido_id)");
        }
        
        echo json_encode(['success' => true]);
        break;
}
?>