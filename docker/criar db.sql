CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TYPE tipo_usuario_enum AS ENUM ('USER', 'ADMIN');

CREATE TYPE status_solicitacao_enum AS ENUM (
    'PENDENTE',
    'APROVADO',
    'RECUSADO',
    'DEVOLVIDO'
);

CREATE TYPE tipo_mensagem_enum AS ENUM ('USER', 'BOT');

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    nome_completo VARCHAR(150) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    senha_hash TEXT NOT NULL,
    telefone VARCHAR(20),
    cpf VARCHAR(14),
    cep VARCHAR(10),
    foto_perfil BYTEA,
    tipo_usuario tipo_usuario_enum DEFAULT 'USER',
    ativo BOOLEAN DEFAULT TRUE,
    criado_em TIMESTAMP DEFAULT NOW(),
    atualizado_em TIMESTAMP DEFAULT NOW()
);

CREATE TABLE password_reset_codes (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    codigo VARCHAR(10) NOT NULL,
    expiracao TIMESTAMP NOT NULL,
    utilizado BOOLEAN DEFAULT FALSE
);


CREATE TABLE livros (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    autor VARCHAR(150) NOT NULL,
    genero VARCHAR(100),
    sinopse TEXT,
    quantidade_estoque INT DEFAULT 0,
    capa BYTEA,  -- imagem da capa do livro
    criado_em TIMESTAMP DEFAULT NOW(),
    atualizado_em TIMESTAMP DEFAULT NOW()
);

CREATE TABLE solicitacoes_aluguel (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE SET NULL,
    livro_id INT REFERENCES livros(id) ON DELETE SET NULL,
    status status_solicitacao_enum DEFAULT 'PENDENTE',
    data_solicitacao TIMESTAMP DEFAULT NOW(),
    data_aprovacao TIMESTAMP,
    data_inicio DATE,
    data_fim DATE,
    codigo_retirada VARCHAR(50)
);


CREATE TABLE historico_aluguel (
    id SERIAL PRIMARY KEY,
    solicitacao_id INT UNIQUE REFERENCES solicitacoes_aluguel(id) ON DELETE CASCADE,
    user_id INT REFERENCES users(id) ON DELETE SET NULL,
    livro_id INT REFERENCES livros(id) ON DELETE SET NULL,
    data_inicio DATE,
    data_fim DATE
);

CREATE TABLE avaliacoes (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    livro_id INT REFERENCES livros(id) ON DELETE CASCADE,
    qualidade_livro INT CHECK (qualidade_livro BETWEEN 1 AND 5),
    qualidade_fisica INT CHECK (qualidade_fisica BETWEEN 1 AND 5),
    comentario TEXT,
    criado_em TIMESTAMP DEFAULT NOW()
);


CREATE TABLE notificacoes_config (
    user_id INT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    notifica_aluguel BOOLEAN DEFAULT TRUE,
    lembrete_devolucao BOOLEAN DEFAULT TRUE,
    lembrete_retirada BOOLEAN DEFAULT TRUE,
    novos_livros BOOLEAN DEFAULT TRUE,

    -- configurações apenas para admins
    novos_pedidos_admin BOOLEAN DEFAULT FALSE,
    devolucoes_atrasadas_admin BOOLEAN DEFAULT FALSE,
    edicao_livro_admin BOOLEAN DEFAULT FALSE,
    adicao_livro_admin BOOLEAN DEFAULT FALSE,
    relatos_bug_admin BOOLEAN DEFAULT FALSE
);


CREATE TABLE chat_conversas (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    iniciado_em TIMESTAMP DEFAULT NOW()
);

CREATE TABLE chat_mensagens (
    id SERIAL PRIMARY KEY,
    conversa_id INT REFERENCES chat_conversas(id) ON DELETE CASCADE,
    user_id INT REFERENCES users(id) ON DELETE SET NULL,
    tipo tipo_mensagem_enum NOT NULL,
    mensagem TEXT NOT NULL,
    criado_em TIMESTAMP DEFAULT NOW()
);
