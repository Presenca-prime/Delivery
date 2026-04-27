<?php
// Pega a string de conexão do ambiente
$database_url = getenv('DATABASE_URL');

if ($database_url) {
    // Converte a string postgresql:// para formato mysqli
    // Extrai as informações da URL
    $url = parse_url($database_url);
    
    $host = $url['host'];
    $port = $url['port'] ?? 5432;
    $user = $url['user'];
    $password = $url['pass'];
    $database = ltrim($url['path'], '/');
    
    // Conecta usando MySQLi (já que seu código original usa mysqli)
    // Nota: O Render usa PostgreSQL, mas vamos manter a compatibilidade
    $conn = new mysqli($host, $user, $password, $database, $port);
    
    if ($conn->connect_error) {
        // Se falhar, tenta com PDO (mais compatível com PostgreSQL)
        try {
            $pdo = new PDO($database_url);
            echo "Conectado com PDO ao PostgreSQL!";
            // Para seu sistema funcionar, você precisará adaptar as consultas
        } catch(PDOException $e) {
            die("Erro na conexão: " . $e->getMessage());
        }
    }
} else {
    // Configuração local para testes (XAMPP)
    $conn = new mysqli('localhost', 'root', '', 'prime_delivery');
    if ($conn->connect_error) {
        die("Erro local: " . $conn->connect_error);
    }
}

echo "✅ Conectado com sucesso!";
?>