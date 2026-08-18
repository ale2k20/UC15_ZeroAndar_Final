-- ============================================================================
-- ZANDAR - SISTEMA DE PROSPECÇÃO IMOBILIÁRIA
-- Versão: 2.0 - Modelo Normalizado com Herança e Auditoria
-- Data: Janeiro 2026
-- ============================================================================
-- IMPORTANTE: Senhas exemplo usando bcrypt hash de "senha123"
-- Em produção, implementar hashing adequado (bcrypt, argon2, etc.)
-- ============================================================================

CREATE DATABASE IF NOT EXISTS zandarDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE zandarDB;

-- ============================================================================
-- 1. TABELAS DE DOMÍNIO E TIPOS
-- ============================================================================

CREATE TABLE TipoUsuario (
    id_tipo_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) UNIQUE NOT NULL,
    descricao VARCHAR(255),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_tipo_usuario CHECK (nome IN ('admin', 'corretor', 'cliente', 'proprietario'))
) ENGINE=InnoDB;

CREATE TABLE Permissao (
    id_permissao INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) UNIQUE NOT NULL,
    descricao VARCHAR(255),
    modulo VARCHAR(50),
    acao VARCHAR(50),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ============================================================================
-- 2. ENDEREÇOS
-- ============================================================================

CREATE TABLE Endereco (
    id_endereco INT AUTO_INCREMENT PRIMARY KEY,
    rua VARCHAR(150) NOT NULL,
    numero VARCHAR(10),
    complemento VARCHAR(100),
    bairro VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    cep VARCHAR(9) NOT NULL,
    ponto_referencia TEXT,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_cidade_bairro (cidade, bairro),
    INDEX idx_cep (cep)
) ENGINE=InnoDB;

-- ============================================================================
-- 3. PESSOA (Tabela Central - Base para todos)
-- ============================================================================

CREATE TABLE Pessoa (
    id_pessoa INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(100) UNIQUE,
    cpf VARCHAR(14) UNIQUE,
    rg VARCHAR(20),
    tipo_pessoa ENUM('F', 'J') DEFAULT 'F',
    cnpj VARCHAR(18),
    data_nascimento DATE,
    id_endereco INT,
    observacoes TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    criado_por INT,
    atualizado_por INT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_endereco) REFERENCES Endereco(id_endereco),
    INDEX idx_nome (nome),
    INDEX idx_cpf (cpf),
    INDEX idx_email (email)
) ENGINE=InnoDB;

-- ============================================================================
-- 4. PAPÉIS (Herança - Uma pessoa pode ter múltiplos papéis)
-- ============================================================================

CREATE TABLE PessoaCliente (
    id_pessoa_cliente INT AUTO_INCREMENT PRIMARY KEY,
    id_pessoa INT NOT NULL UNIQUE,
    data_cadastro DATE NOT NULL,
    origem_cadastro VARCHAR(50),
    score_interesse INT DEFAULT 0,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pessoa) REFERENCES Pessoa(id_pessoa) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE PessoaProprietario (
    id_pessoa_proprietario INT AUTO_INCREMENT PRIMARY KEY,
    id_pessoa INT NOT NULL UNIQUE,
    data_cadastro DATE NOT NULL,
    aceita_contato BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pessoa) REFERENCES Pessoa(id_pessoa) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE PessoaCorretor (
    id_pessoa_corretor INT AUTO_INCREMENT PRIMARY KEY,
    id_pessoa INT NOT NULL UNIQUE,
    creci VARCHAR(20) UNIQUE NOT NULL,
    comissao_percentual DECIMAL(5,2) DEFAULT 6.00,
    data_admissao DATE NOT NULL,
    data_desligamento DATE,
    especialidade VARCHAR(100),
    meta_mensal DECIMAL(12,2),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pessoa) REFERENCES Pessoa(id_pessoa) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================================================
-- 5. SISTEMA DE USUÁRIOS (Login e Autenticação)
-- ============================================================================

CREATE TABLE Usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    id_pessoa INT NOT NULL UNIQUE,
    id_tipo_usuario INT NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    acesso_liberado BOOLEAN DEFAULT FALSE,
    liberado_por INT,
    data_liberacao TIMESTAMP NULL,
    ultimo_acesso TIMESTAMP NULL,
    token_recuperacao VARCHAR(255),
    token_expiracao TIMESTAMP NULL,
    tentativas_login INT DEFAULT 0,
    bloqueado_ate TIMESTAMP NULL,
    criado_por INT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pessoa) REFERENCES Pessoa(id_pessoa) ON DELETE CASCADE,
    FOREIGN KEY (id_tipo_usuario) REFERENCES TipoUsuario(id_tipo_usuario),
    FOREIGN KEY (liberado_por) REFERENCES Usuario(id_usuario),
    INDEX idx_ativo_liberado (ativo, acesso_liberado)
) ENGINE=InnoDB;

CREATE TABLE UsuarioPermissao (
    id_usuario_permissao INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_permissao INT NOT NULL,
    concedido_por INT,
    data_concessao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_permissao) REFERENCES Permissao(id_permissao) ON DELETE CASCADE,
    UNIQUE KEY uk_usuario_permissao (id_usuario, id_permissao)
) ENGINE=InnoDB;

-- ============================================================================
-- 6. TELEFONES (Múltiplos por pessoa)
-- ============================================================================

CREATE TABLE TelefonePessoa (
    id_telefone INT AUTO_INCREMENT PRIMARY KEY,
    id_pessoa INT NOT NULL,
    numero VARCHAR(15) NOT NULL,
    tipo ENUM('celular', 'fixo', 'comercial', 'recado') DEFAULT 'celular',
    principal BOOLEAN DEFAULT FALSE,
    whatsapp BOOLEAN DEFAULT FALSE,
    observacoes VARCHAR(255),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pessoa) REFERENCES Pessoa(id_pessoa) ON DELETE CASCADE,
    INDEX idx_pessoa (id_pessoa)
) ENGINE=InnoDB;

-- ============================================================================
-- 7. IMÓVEIS
-- ============================================================================

CREATE TABLE Imovel (
    id_imovel INT AUTO_INCREMENT PRIMARY KEY,
    codigo_imovel VARCHAR(20) UNIQUE NOT NULL,
    descricao TEXT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    finalidade ENUM('venda', 'aluguel', 'ambos') DEFAULT 'venda',
    valor_venda DECIMAL(12,2),
    valor_aluguel DECIMAL(10,2),
    valor_condominio DECIMAL(10,2),
    valor_iptu DECIMAL(10,2),
    area_total DECIMAL(10,2),
    area_construida DECIMAL(10,2),
    quartos INT DEFAULT 0,
    suites INT DEFAULT 0,
    banheiros INT DEFAULT 0,
    vagas_garagem INT DEFAULT 0,
    andar INT,
    aceita_permuta BOOLEAN DEFAULT FALSE,
    aceita_financiamento BOOLEAN DEFAULT TRUE,
    mobiliado BOOLEAN DEFAULT FALSE,
    status ENUM('disponivel', 'reservado', 'vendido', 'alugado', 'inativo') DEFAULT 'disponivel',
    destaque BOOLEAN DEFAULT FALSE,
    id_proprietario INT NOT NULL,
    id_corretor_responsavel INT,
    id_endereco INT NOT NULL,
    criado_por INT,
    atualizado_por INT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_proprietario) REFERENCES PessoaProprietario(id_pessoa_proprietario),
    FOREIGN KEY (id_corretor_responsavel) REFERENCES PessoaCorretor(id_pessoa_corretor),
    FOREIGN KEY (id_endereco) REFERENCES Endereco(id_endereco),
    INDEX idx_tipo_finalidade (tipo, finalidade),
    INDEX idx_status (status),
    INDEX idx_valor_venda (valor_venda),
    INDEX idx_cidade (id_endereco)
) ENGINE=InnoDB;

CREATE TABLE ImovelCaracteristica (
    id_caracteristica INT AUTO_INCREMENT PRIMARY KEY,
    id_imovel INT NOT NULL,
    caracteristica VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_imovel) REFERENCES Imovel(id_imovel) ON DELETE CASCADE,
    INDEX idx_imovel (id_imovel)
) ENGINE=InnoDB;

-- ============================================================================
-- 8. PREFERÊNCIAS E SEGUIMENTO DE CLIENTES
-- ============================================================================

CREATE TABLE Preferencia (
    id_preferencia INT AUTO_INCREMENT PRIMARY KEY,
    id_pessoa_cliente INT NOT NULL,
    tipo_imovel VARCHAR(50),
    finalidade ENUM('compra', 'aluguel', 'ambos'),
    valor_minimo DECIMAL(12,2),
    valor_maximo DECIMAL(12,2),
    bairros_interesse TEXT,
    quartos_minimo INT,
    vagas_minimo INT,
    aceita_financiamento BOOLEAN,
    observacoes TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pessoa_cliente) REFERENCES PessoaCliente(id_pessoa_cliente) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Seguimento (
    id_seguimento INT AUTO_INCREMENT PRIMARY KEY,
    id_pessoa_cliente INT NOT NULL,
    tipo_interesse VARCHAR(100),
    frequencia_contato ENUM('semanal', 'quinzenal', 'mensal', 'trimestral', 'semestral') DEFAULT 'mensal',
    prioridade ENUM('baixa', 'media', 'alta', 'urgente') DEFAULT 'media',
    ultima_interacao DATE,
    proxima_interacao DATE,
    observacoes TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pessoa_cliente) REFERENCES PessoaCliente(id_pessoa_cliente) ON DELETE CASCADE,
    INDEX idx_proxima_interacao (proxima_interacao)
) ENGINE=InnoDB;

-- ============================================================================
-- 9. INTERAÇÕES E AGENDA
-- ============================================================================

CREATE TABLE Interacao (
    id_interacao INT AUTO_INCREMENT PRIMARY KEY,
    id_pessoa_cliente INT NOT NULL,
    id_pessoa_corretor INT NOT NULL,
    id_imovel INT,
    tipo_interacao ENUM('telefone', 'email', 'whatsapp', 'presencial', 'video', 'outro') NOT NULL,
    data_interacao DATETIME NOT NULL,
    duracao_minutos INT,
    assunto VARCHAR(255),
    detalhes TEXT,
    resultado ENUM('positivo', 'neutro', 'negativo', 'sem_resposta'),
    proxima_acao TEXT,
    data_proxima_acao DATE,
    criado_por INT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pessoa_cliente) REFERENCES PessoaCliente(id_pessoa_cliente) ON DELETE CASCADE,
    FOREIGN KEY (id_pessoa_corretor) REFERENCES PessoaCorretor(id_pessoa_corretor),
    FOREIGN KEY (id_imovel) REFERENCES Imovel(id_imovel),
    INDEX idx_data (data_interacao)
) ENGINE=InnoDB;

CREATE TABLE Agenda (
    id_agendamento INT AUTO_INCREMENT PRIMARY KEY,
    id_pessoa_cliente INT NOT NULL,
    id_pessoa_corretor INT NOT NULL,
    id_imovel INT,
    tipo_compromisso ENUM('visita', 'reuniao', 'avaliacao', 'assinatura', 'outro') NOT NULL,
    data_hora DATETIME NOT NULL,
    duracao_minutos INT DEFAULT 60,
    local VARCHAR(255),
    status ENUM('agendado', 'confirmado', 'realizado', 'cancelado', 'remarcado') DEFAULT 'agendado',
    motivo_cancelamento TEXT,
    observacoes TEXT,
    lembrete_enviado BOOLEAN DEFAULT FALSE,
    criado_por INT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pessoa_cliente) REFERENCES PessoaCliente(id_pessoa_cliente) ON DELETE CASCADE,
    FOREIGN KEY (id_pessoa_corretor) REFERENCES PessoaCorretor(id_pessoa_corretor),
    FOREIGN KEY (id_imovel) REFERENCES Imovel(id_imovel),
    INDEX idx_corretor_data (id_pessoa_corretor, data_hora),
    INDEX idx_status (status)
) ENGINE=InnoDB;

-- ============================================================================
-- 10. NEGOCIAÇÕES E AVALIAÇÕES
-- ============================================================================

CREATE TABLE Negociacao (
    id_negociacao INT AUTO_INCREMENT PRIMARY KEY,
    codigo_negociacao VARCHAR(20) UNIQUE NOT NULL,
    tipo_negociacao ENUM('compra', 'venda', 'aluguel') NOT NULL,
    id_pessoa_cliente INT NOT NULL,
    id_imovel INT NOT NULL,
    id_pessoa_corretor INT NOT NULL,
    valor_proposto DECIMAL(12,2) NOT NULL,
    valor_final DECIMAL(12,2),
    comissao_valor DECIMAL(10,2),
    data_proposta DATE NOT NULL,
    data_conclusao DATE,
    data_vencimento DATE,
    status ENUM('em_negociacao', 'proposta_enviada', 'proposta_aceita', 'proposta_recusada', 
                'contrato_assinado', 'concluido', 'cancelado') DEFAULT 'em_negociacao',
    forma_pagamento VARCHAR(100),
    observacoes TEXT,
    criado_por INT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pessoa_cliente) REFERENCES PessoaCliente(id_pessoa_cliente),
    FOREIGN KEY (id_imovel) REFERENCES Imovel(id_imovel),
    FOREIGN KEY (id_pessoa_corretor) REFERENCES PessoaCorretor(id_pessoa_corretor),
    INDEX idx_status (status)
) ENGINE=InnoDB;

CREATE TABLE Avaliacao (
    id_avaliacao INT AUTO_INCREMENT PRIMARY KEY,
    id_imovel INT NOT NULL,
    id_pessoa_cliente INT,
    valor_avaliado DECIMAL(12,2) NOT NULL,
    valor_sugerido_venda DECIMAL(12,2),
    valor_sugerido_aluguel DECIMAL(10,2),
    roi DECIMAL(10,2),
    nota INT,
    pontos_positivos TEXT,
    pontos_negativos TEXT,
    comentario TEXT,
    avaliador_nome VARCHAR(150),
    data_avaliacao DATE NOT NULL,
    criado_por INT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_imovel) REFERENCES Imovel(id_imovel) ON DELETE CASCADE,
    FOREIGN KEY (id_pessoa_cliente) REFERENCES PessoaCliente(id_pessoa_cliente),
    INDEX idx_imovel (id_imovel)
) ENGINE=InnoDB;

-- ============================================================================
-- 11. DOCUMENTOS E ANEXOS
-- ============================================================================

CREATE TABLE Anexo (
    id_anexo INT AUTO_INCREMENT PRIMARY KEY,
    id_imovel INT,
    id_pessoa_cliente INT,
    id_negociacao INT,
    tipo_documento ENUM('contrato', 'escritura', 'iptu', 'condominio', 'foto', 
                        'planta', 'certidao', 'laudo', 'outro') NOT NULL,
    nome_arquivo VARCHAR(255) NOT NULL,
    caminho_arquivo VARCHAR(500) NOT NULL,
    tamanho_kb INT,
    extensao VARCHAR(10),
    descricao TEXT,
    publico BOOLEAN DEFAULT FALSE,
    data_upload TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    enviado_por INT,
    FOREIGN KEY (id_imovel) REFERENCES Imovel(id_imovel) ON DELETE CASCADE,
    FOREIGN KEY (id_pessoa_cliente) REFERENCES PessoaCliente(id_pessoa_cliente) ON DELETE CASCADE,
    FOREIGN KEY (id_negociacao) REFERENCES Negociacao(id_negociacao) ON DELETE CASCADE,
    INDEX idx_imovel (id_imovel),
    INDEX idx_tipo (tipo_documento)
) ENGINE=InnoDB;

-- ============================================================================
-- 12. AUDITORIA (RF011)
-- ============================================================================

CREATE TABLE LogAuditoria (
    id_log BIGINT AUTO_INCREMENT PRIMARY KEY,
    tabela VARCHAR(50) NOT NULL,
    operacao ENUM('INSERT', 'UPDATE', 'DELETE', 'SELECT') NOT NULL,
    id_registro INT,
    id_usuario INT,
    dados_anteriores JSON,
    dados_novos JSON,
    ip_origem VARCHAR(45),
    user_agent TEXT,
    data_operacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario),
    INDEX idx_tabela_operacao (tabela, operacao),
    INDEX idx_data (data_operacao),
    INDEX idx_usuario (id_usuario)
) ENGINE=InnoDB;

-- ============================================================================
-- 13. DADOS INICIAIS (SEED)
-- ============================================================================

-- Tipos de Usuário
INSERT INTO TipoUsuario (nome, descricao) VALUES
('admin', 'Administrador do sistema com acesso total'),
('corretor', 'Corretor de imóveis que gerencia clientes e imóveis'),
('cliente', 'Cliente que busca imóveis'),
('proprietario', 'Proprietário de imóveis');

-- Permissões
INSERT INTO Permissao (nome, descricao, modulo, acao) VALUES
('imoveis.criar', 'Criar novos imóveis', 'imoveis', 'criar'),
('imoveis.editar', 'Editar imóveis', 'imoveis', 'editar'),
('imoveis.visualizar', 'Visualizar imóveis', 'imoveis', 'visualizar'),
('imoveis.excluir', 'Excluir imóveis', 'imoveis', 'excluir'),
('clientes.criar', 'Criar clientes', 'clientes', 'criar'),
('clientes.editar', 'Editar clientes', 'clientes', 'editar'),
('clientes.visualizar', 'Visualizar clientes', 'clientes', 'visualizar'),
('usuarios.gerenciar', 'Gerenciar usuários', 'usuarios', 'gerenciar'),
('usuarios.liberar_acesso', 'Liberar acesso de clientes/proprietários', 'usuarios', 'liberar_acesso'),
('relatorios.visualizar', 'Visualizar relatórios', 'relatorios', 'visualizar'),
('configuracoes.editar', 'Editar configurações', 'configuracoes', 'editar');

-- Endereços
INSERT INTO Endereco (rua, numero, bairro, cidade, estado, cep) VALUES
('Av. Brasil', '1500', 'Centro', 'Passo Fundo', 'RS', '99010-000'),
('Rua Moron', '850', 'Centro', 'Passo Fundo', 'RS', '99010-100'),
('Av. Presidente Vargas', '2300', 'Vera Cruz', 'Passo Fundo', 'RS', '99025-000'),
('Rua Uruguai', '1200', 'Centro', 'Passo Fundo', 'RS', '99010-200'),
('Av. São Vicente', '500', 'São Cristóvão', 'Passo Fundo', 'RS', '99052-000'),
('Rua Paissandu', '789', 'Centro', 'Passo Fundo', 'RS', '99010-300');

-- Pessoas
INSERT INTO Pessoa (nome, email, cpf, tipo_pessoa, data_nascimento, id_endereco) VALUES
('Carlos Admin', 'admin@zandar.com', '111.111.111-11', 'F', '1985-03-15', 1),
('Ricardo Corretor', 'ricardo@zandar.com', '222.222.222-22', 'F', '1990-07-20', 2),
('Patricia Corretora', 'patricia@zandar.com', '333.333.333-33', 'F', '1992-09-08', 3),
('Ana Cliente', 'ana.cliente@email.com', '444.444.444-44', 'F', '1988-11-10', 4),
('João Proprietário', 'joao.prop@email.com', '555.555.555-55', 'F', '1975-05-25', 5),
('Maria Proprietária', 'maria.prop@email.com', '666.666.666-66', 'F', '1980-08-14', 6);

-- Telefones
INSERT INTO TelefonePessoa (id_pessoa, numero, tipo, principal, whatsapp) VALUES
(1, '(54) 99999-0001', 'celular', TRUE, TRUE),
(2, '(54) 99999-0002', 'celular', TRUE, TRUE),
(3, '(54) 99999-0003', 'celular', TRUE, TRUE),
(4, '(54) 99999-0004', 'celular', TRUE, TRUE),
(5, '(54) 99999-0005', 'celular', TRUE, TRUE),
(6, '(54) 99999-0006', 'celular', TRUE, TRUE);

-- Corretores
INSERT INTO PessoaCorretor (id_pessoa, creci, comissao_percentual, data_admissao, especialidade) VALUES
(2, 'CRECI-RS-12345', 6.00, '2020-01-15', 'Residencial'),
(3, 'CRECI-RS-67890', 6.50, '2021-03-20', 'Comercial');

-- Clientes
INSERT INTO PessoaCliente (id_pessoa, data_cadastro, origem_cadastro, score_interesse) VALUES
(4, '2025-01-10', 'site', 85);

-- Proprietários
INSERT INTO PessoaProprietario (id_pessoa, data_cadastro, aceita_contato) VALUES
(5, '2024-12-01', TRUE),
(6, '2024-11-15', TRUE);

-- Usuários (senha_hash é exemplo de bcrypt para "senha123")
INSERT INTO Usuario (id_pessoa, id_tipo_usuario, senha_hash, ativo, acesso_liberado, liberado_por) VALUES
(1, 1, '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', TRUE, TRUE, NULL),
(2, 2, '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', TRUE, TRUE, 1),
(3, 2, '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', TRUE, TRUE, 1);

-- Permissões para Admin
INSERT INTO UsuarioPermissao (id_usuario, id_permissao, concedido_por) 
SELECT 1, id_permissao, NULL FROM Permissao;

-- Permissões para Corretores
INSERT INTO UsuarioPermissao (id_usuario, id_permissao, concedido_por)
SELECT u.id_usuario, p.id_permissao, 1
FROM Usuario u, Permissao p
WHERE u.id_tipo_usuario = 2 
  AND p.nome IN ('imoveis.criar', 'imoveis.editar', 'imoveis.visualizar', 
                 'clientes.criar', 'clientes.editar', 'clientes.visualizar',
                 'usuarios.liberar_acesso', 'relatorios.visualizar');

-- Imóveis
INSERT INTO Imovel (codigo_imovel, descricao, tipo, finalidade, valor_venda, quartos, banheiros, vagas_garagem, 
                    area_total, status, id_proprietario, id_corretor_responsavel, id_endereco, criado_por) VALUES
('IMOV-001', 'Apartamento 3 quartos no Centro', 'Apartamento', 'venda', 350000.00, 3, 2, 1, 85.50, 'disponivel', 1, 1, 1, 2),
('IMOV-002', 'Casa ampla no Vera Cruz', 'Casa', 'venda', 650000.00, 4, 3, 2, 220.00, 'disponivel', 2, 1, 3, 2),
('IMOV-003', 'Sala comercial Centro', 'Comercial', 'aluguel', NULL, 0, 1, 0, 45.00, 'disponivel', 1, 2, 2, 3);

UPDATE Imovel SET valor_aluguel = 2500.00 WHERE codigo_imovel = 'IMOV-003';

-- Características dos Imóveis
INSERT INTO ImovelCaracteristica (id_imovel, caracteristica) VALUES
(1, 'Elevador'),
(1, 'Sacada'),
(1, 'Churrasqueira'),
(2, 'Piscina'),
(2, 'Churrasqueira'),
(2, 'Jardim'),
(3, 'Ar condicionado'),
(3, 'Recepção');

-- Preferências
INSERT INTO Preferencia (id_pessoa_cliente, tipo_imovel, finalidade, valor_minimo, valor_maximo, bairros_interesse, quartos_minimo) VALUES
(1, 'Apartamento', 'compra', 300000.00, 450000.00, 'Centro, Vera Cruz', 3);

-- Seguimento
INSERT INTO Seguimento (id_pessoa_cliente, tipo_interesse, frequencia_contato, prioridade, ultima_interacao, proxima_interacao) VALUES
(1, 'Apartamento 3 quartos', 'mensal', 'alta', '2025-01-10', '2025-02-10');

-- Interações
INSERT INTO Interacao (id_pessoa_cliente, id_pessoa_corretor, id_imovel, tipo_interacao, data_interacao, resultado, detalhes) VALUES
(1, 1, 1, 'whatsapp', '2025-01-10 14:30:00', 'positivo', 'Cliente demonstrou interesse no apartamento IMOV-001');

-- Agendamentos
INSERT INTO Agenda (id_pessoa_cliente, id_pessoa_corretor, id_imovel, tipo_compromisso, data_hora, status, observacoes) VALUES
(1, 1, 1, 'visita', '2025-01-15 10:00:00', 'agendado', 'Primeira visita ao apartamento'),
(1, 1, 2, 'visita', '2025-01-17 14:00:00', 'agendado', 'Visita à casa no Vera Cruz');

-- Negociações
INSERT INTO Negociacao (codigo_negociacao, tipo_negociacao, id_pessoa_cliente, id_imovel, id_pessoa_corretor, 
                       valor_proposto, data_proposta, status, criado_por) VALUES
('NEG-2025-001', 'compra', 1, 1, 1, 340000.00, '2025-01-11', 'em_negociacao', 2);

-- Avaliações
INSERT INTO Avaliacao (id_imovel, valor_avaliado, valor_sugerido_venda, nota, pontos_positivos, 
                      pontos_negativos, data_avaliacao, criado_por) VALUES
(1, 350000.00, 355000.00, 5, 'Localização excelente, bem conservado', 'Sem garagem coberta', '2024-12-15', 2),
(2, 650000.00, 680000.00, 5, 'Ampla, com piscina, ótimo acabamento', 'Precisa pintura externa', '2024-12-20', 2);

-- Anexos (exemplos)
INSERT INTO Anexo (id_imovel, tipo_documento, nome_arquivo, caminho_arquivo, tamanho_kb, extensao, publico, enviado_por) VALUES
(1, 'foto', 'fachada_imov001.jpg', '/uploads/imoveis/2025/01/fachada_imov001.jpg', 1250, 'jpg', TRUE, 2),
(1, 'foto', 'sala_imov001.jpg', '/uploads/imoveis/2025/01/sala_imov001.jpg', 980, 'jpg', TRUE, 2),
(1, 'planta', 'planta_imov001.pdf', '/uploads/imoveis/2025/01/planta_imov001.pdf', 450, 'pdf', TRUE, 2),
(2, 'foto', 'fachada_imov002.jpg', '/uploads/imoveis/2025/01/fachada_imov002.jpg', 1500, 'jpg', TRUE, 2);

-- ============================================================================
-- 14. ÍNDICES ADICIONAIS PARA PERFORMANCE
-- ============================================================================

CREATE INDEX idx_negociacao_datas ON Negociacao(data_proposta, data_conclusao);
CREATE INDEX idx_agenda_corretor_status ON Agenda(id_pessoa_corretor, status, data_hora);
CREATE INDEX idx_imovel_busca ON Imovel(tipo, finalidade, status, valor_venda);

