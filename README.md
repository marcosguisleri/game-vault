<div align="center">

<img src="docs/game-vault-logo.png" alt="Game Vault Logo" width="200"/>

# 🎮 Game Vault

### Sua biblioteca de jogos. Sua história. Sua API.

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Quarkus](https://img.shields.io/badge/Quarkus-3.x-4695EB?style=for-the-badge&logo=quarkus&logoColor=white)](https://quarkus.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![Swagger](https://img.shields.io/badge/Swagger-UI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)](https://swagger.io/)

</div>

---

## 📖 Sobre o projeto

O **Game Vault** é uma API REST para gerenciar sua coleção de jogos — registrando título, gênero, ano de lançamento, horas jogadas e status de conclusão.

O projeto evoluiu em duas fases distintas, cada uma aplicando um conjunto de tecnologias e conceitos:

> 🗂️ A versão CLI com OOP pura está preservada na branch `v1-poo` — dá pra ver a evolução completa do zero até a API.

---

## 🚀 Funcionalidades

- ➕ Cadastrar, editar e remover jogos
- 📦 Cadastro em lote com relatório de sucessos e falhas
- ✅ Marcar jogo como zerado via endpoint dedicado
- ⏱️ Adicionar horas jogadas de forma incremental
- 📋 Listar todos os jogos da coleção
- 🔍 Buscar por ID ou título
- 🎯 Filtrar por gênero, status, ano e horas jogadas
- 🔃 Ordenar por título, gênero, ano ou horas jogadas
- 🛡️ Validações automáticas nos dados de entrada
- 🚨 Tratamento de erros padronizado
- 📖 Documentação interativa via Swagger UI
- ❤️ Health check via SmallRye Health

---

## 🏗️ Estrutura do projeto

```
src/main/java/br/dev/guisleri/
├── dto/
│   ├── JogoRequestDTO.java            # Dados de entrada (criação e atualização)
│   ├── JogoResponseDTO.java           # Dados de saída da API
│   ├── JogoLoteItemDTO.java           # Item do cadastro em lote
│   ├── CadastroLoteResponseDTO.java   # Relatório do processamento em lote
│   ├── ResultadoItemLoteDTO.java      # Resultado individual por item do lote
│   ├── AdicionarHorasDTO.java         # DTO para o endpoint de horas
│   └── RespostaApiDTO.java            # Envelope genérico para todas as respostas
├── exception/
│   ├── JogoJaCadastradoException.java
│   ├── JogoJaCadastradoExceptionMapper.java
│   ├── JogoJaZeradoException.java
│   ├── JogoJaZeradoExceptionMapper.java
│   ├── JogoNaoEncontradoException.java
│   ├── JogoNaoEncontradoExceptionMapper.java
│   └── ValidacaoExceptionMapper.java
├── health/
│   └── DatabaseHealthCheck.java       # Health check com contagem de jogos
├── model/
│   ├── Jogo.java                      # Entidade JPA com Panache e métodos de domínio
│   └── Genero.java                    # Enum com os gêneros disponíveis
├── resource/
│   └── JogoResource.java              # Endpoints REST (JAX-RS)
└── service/
    └── JogoService.java               # Regras de negócio com JPQL e Panache
```

---

## 🧰 Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 21 | Linguagem principal |
| Quarkus 3.x | Framework da API |
| Hibernate ORM + Panache | Persistência com Active Record |
| PostgreSQL | Banco de dados relacional |
| Flyway | Versionamento de schema |
| Bean Validation | Validações declarativas |
| JAX-RS + Jackson | REST + Serialização JSON |
| SmallRye OpenAPI | Swagger UI |
| SmallRye Health | Health check |
| Docker Compose | Infraestrutura local |
| JUnit 5 + REST Assured | Testes de integração |

---

## ▶️ Como executar

**Pré-requisitos:** Java 21+, Maven e Docker instalados.

```bash
# Clone o repositório
git clone https://github.com/marcosguisleri/game-vault.git
cd game-vault

# Configure as variáveis de ambiente
# Crie o arquivo src/main/resources/application-local.properties:
#
#   quarkus.datasource.username=postgres
#   quarkus.datasource.password=postgres
#   quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5433/gamevault
#
# Esse arquivo não sobe para o Git (.gitignore)

# Suba o banco de dados
docker compose up -d

# Inicie a aplicação em modo dev
QUARKUS_PROFILE=local ./mvnw quarkus:dev
```

| URL | Descrição |
|---|---|
| `http://localhost:8080/jogos` | API REST |
| `http://localhost:8080/q/swagger-ui` | Swagger UI |
| `http://localhost:8080/q/health` | Health check |

---

## 🧪 Testes

Os testes utilizam Quarkus DevServices — o banco PostgreSQL é iniciado automaticamente via Docker durante a execução, sem configuração manual.

```bash
./mvnw test
```

---

## 📡 Endpoints

### CRUD

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/jogos` | Cadastrar um jogo |
| `POST` | `/jogos/lote` | Cadastrar jogos em lote com relatório |
| `PUT` | `/jogos/{id}` | Atualizar um jogo |
| `DELETE` | `/jogos/{id}` | Remover um jogo |
| `DELETE` | `/jogos?confirmar=true` | Remover todos os jogos |

### Ações de domínio

| Método | Endpoint | Descrição |
|---|---|---|
| `PATCH` | `/jogos/{id}/zerar` | Marcar jogo como zerado |
| `PATCH` | `/jogos/{id}/horas` | Adicionar horas jogadas |

### Buscas

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/jogos` | Listar todos os jogos |
| `GET` | `/jogos/{id}` | Buscar por ID |
| `GET` | `/jogos/titulo/{titulo}` | Buscar por título |

### Filtros

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/jogos/zerados` | Listar jogos zerados |
| `GET` | `/jogos/nao-zerados` | Listar jogos não zerados |
| `GET` | `/jogos/genero/{genero}` | Filtrar por gênero |
| `GET` | `/jogos/ano/{ano}` | Filtrar por ano |
| `GET` | `/jogos/horas-jogadas-mais/{horas}` | Jogos com X horas ou mais |
| `GET` | `/jogos/horas-jogadas-menos/{horas}` | Jogos com menos de X horas |

### Ordenações

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/jogos/ordenar-por-titulo` | Ordenar por título |
| `GET` | `/jogos/ordenar-por-genero` | Ordenar por gênero |
| `GET` | `/jogos/ordenar-por-ano` | Ordenar por ano |
| `GET` | `/jogos/ordenar-por-horas-jogadas` | Ordenar por horas jogadas |

---

## 📨 Formato das respostas

Todas as respostas seguem o mesmo envelope:

```json
{
  "mensagem": "Jogo cadastrado com sucesso!",
  "dados": {
    "id": 1,
    "titulo": "God of War",
    "genero": "ACAO",
    "anoLancamento": 2018,
    "quantHorasJogadas": 40,
    "zerado": true
  }
}
```

Resposta do cadastro em lote (`POST /jogos/lote`):

```json
{
  "mensagem": "Processamento em lote concluído.",
  "dados": {
    "total": 3,
    "cadastrados": 2,
    "falhas": 1,
    "resultados": [
      { "titulo": "God of War", "sucesso": true, "mensagem": "Cadastrado com sucesso." },
      { "titulo": "Elden Ring", "sucesso": true, "mensagem": "Cadastrado com sucesso." },
      { "titulo": "God of War", "sucesso": false, "mensagem": "Já existe um jogo cadastrado com esse título." }
    ]
  }
}
```

Em caso de erro:

```json
{
  "mensagem": "Jogo com ID 99 não encontrado.",
  "dados": null
}
```

---

## 🎮 Gêneros disponíveis

`ACAO` `AVENTURA` `RPG` `FPS` `LUTA` `ESPORTE` `CORRIDA`
`ESTRATEGIA` `TERROR` `PLATAFORMA` `SIMULACAO` `PUZZLE`

---

## 📚 Conceitos aplicados

**Fase 1 — CLI com OOP** (`v1-poo`)
- Orientação a Objetos (encapsulamento, abstração)
- Enum para domínio fechado de valores
- Streams, lambdas e method references
- `Optional` para buscas sem resultado garantido
- `Comparator` para ordenações flexíveis
- Serialização JSON com Jackson
- Separação de responsabilidades (Model / Service / Repository / CLI)
- Padrão seed para dados iniciais

**Fase 2 — API REST com Quarkus** (`master`)
- JPA com Hibernate ORM e padrão Active Record (Panache)
- Injeção de dependência com CDI (`@ApplicationScoped`, `@Inject`)
- Controle de transações com `@Transactional`
- API REST com JAX-RS
- Padrão DTO com Java Records desacoplando a entity da API
- Validações declarativas com Bean Validation
- Consultas JPQL com Panache
- Tratamento de erros centralizado com `ExceptionMapper`
- Métodos de domínio na entity (`zerarJogo`, `adicionarHorasJogadas`)
- Processamento em lote com relatório de resultados por item
- Versionamento de schema com Flyway
- Documentação com OpenAPI / Swagger UI enriquecida com `@Operation` e `@Schema`
- Health check com SmallRye Health
- Testes de integração com JUnit 5 + REST Assured e DevServices
- Containerização do banco com Docker Compose

---

## 🗺️ Roadmap

- [x] CRUD básico em memória com OOP
- [x] Ordenação por horas, título e ano com `Comparator`
- [x] Estatísticas da coleção
- [x] Persistência em arquivo JSON com Jackson
- [x] Menu interativo CLI
- [x] Filtros e ordenações no menu (submenus)
- [x] Banco de dados com JPA + PostgreSQL
- [x] API REST com Quarkus
- [x] Validações com Bean Validation
- [x] Tratamento de erros padronizado
- [x] Versionamento do banco com Flyway
- [x] Cadastro em lote com relatório de resultados
- [x] Documentação OpenAPI enriquecida com `@Operation` e `@Schema`
- [x] Testes automatizados com JUnit 5 + REST Assured
- [ ] Interface web

---

## 👨‍💻 Autor

Desenvolvido por **Marcos Guisleri**

[![GitHub](https://img.shields.io/badge/GitHub-marcosguisleri-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/marcosguisleri)