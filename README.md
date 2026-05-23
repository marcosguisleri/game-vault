# 🎮 GameVault

Sistema de gerenciamento de biblioteca de jogos pessoal, evoluído de uma aplicação CLI orientada a objetos para uma API REST completa com banco de dados relacional.

---

## 📖 Sobre o projeto

O GameVault permite gerenciar uma coleção de jogos registrando informações como título, gênero, ano de lançamento, horas jogadas e status de conclusão.

O projeto passou por duas fases distintas de desenvolvimento, cada uma com seu próprio conjunto de tecnologias e conceitos aplicados.

> A versão CLI original está preservada na branch `master`.

---

## 🚀 Funcionalidades

- ➕ Cadastrar, editar e remover jogos
- ✅ Marcar jogo como zerado via endpoint dedicado
- ⏱️ Adicionar horas jogadas de forma incremental
- 📋 Listar todos os jogos da coleção
- 🔍 Buscar jogo por ID ou título
- 🎯 Filtrar por gênero, status de conclusão, ano e horas jogadas
- 🔃 Ordenar por título, gênero, ano ou horas jogadas
- ✅ Validações automáticas nos dados de entrada
- 🚨 Tratamento de erros padronizado com respostas consistentes
- 📖 Documentação interativa via Swagger UI
- ❤️ Health check via SmallRye Health

---

## 🏗️ Estrutura do projeto

```
src/main/java/br/dev/guisleri/
├── dto/
│   ├── JogoRequestDTO.java          # Dados de entrada da API (criação e atualização)
│   ├── JogoResponseDTO.java         # Dados de saída da API
│   ├── AdicionarHorasDTO.java       # DTO para o endpoint de horas
│   └── RespostaApiDTO.java          # Envelope genérico para todas as respostas
├── exception/
│   ├── JogoJaCadastradoException.java
│   ├── JogoJaCadastradoExceptionMapper.java
│   ├── JogoJaZeradoException.java
│   ├── JogoJaZeradoExceptionMapper.java
│   ├── JogoNaoEncontradoException.java
│   ├── JogoNaoEncontradoExceptionMapper.java
│   └── ValidacaoExceptionMapper.java
├── health/
│   └── DatabaseHealthCheck.java     # Health check com contagem de jogos
├── model/
│   ├── Jogo.java                    # Entidade JPA com Panache e métodos de domínio
│   └── Genero.java                  # Enum com os gêneros disponíveis
├── resource/
│   └── JogoResource.java            # Endpoints REST (JAX-RS)
└── service/
    └── JogoService.java             # Regras de negócio com JPQL e Panache
```

---

## 🧰 Tecnologias

- Java 21
- Quarkus 3.35
- Hibernate ORM with Panache
- PostgreSQL
- Flyway
- Hibernate Validator (Bean Validation)
- Quarkus REST + Jackson
- SmallRye OpenAPI (Swagger UI)
- SmallRye Health
- Docker
- JUnit 5 + REST Assured

---

## ▶️ Como executar

**Pré-requisitos:** Java 21+, Maven e Docker instalados.

```bash
# Clone o repositório
git clone https://github.com/marcosguisleri/game-vault.git
cd game-vault

# Mude para a branch Quarkus
git checkout feature/quarkus

# Configure as variáveis de ambiente
# Crie o arquivo src/main/resources/application-local.properties:
#
#   quarkus.datasource.username=postgres
#   quarkus.datasource.password=postgres
#   quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5433/gamevault
#
# Esse arquivo não sobe para o Git (.gitignore)
# Use application-local.properties.example como referência

# Suba o banco de dados
docker compose up -d

# Inicie a aplicação em modo dev com o profile local
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
| `PUT` | `/jogos/{id}` | Atualizar um jogo |
| `DELETE` | `/jogos/{id}` | Remover um jogo |

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
| `GET` | `/jogos/horas-jogadas-menos/{horas}` | Jogos com X horas ou menos |

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

**Fase 1 — CLI com OOP** (`master`)
- Orientação a Objetos (encapsulamento, abstração)
- Enum para domínio fechado de valores
- Streams, lambdas e method references
- `Optional` para buscas sem resultado garantido
- `Comparator` para ordenações flexíveis
- Serialização JSON com Jackson
- Separação de responsabilidades (Model / Service / Repository / CLI)
- Padrão seed para dados iniciais

**Fase 2 — API REST com Quarkus** (`feature/quarkus`)
- JPA com Hibernate ORM e padrão Active Record (Panache)
- Injeção de dependência com CDI (`@ApplicationScoped`, `@Inject`)
- Controle de transações com `@Transactional`
- API REST com JAX-RS
- Padrão DTO com Java Records (`JogoRequestDTO`, `JogoResponseDTO`) desacoplando a entity da API
- Validações declarativas com Bean Validation (`@NotBlank`, `@NotNull`, `@Min`, `@PositiveOrZero`)
- Consultas JPQL com Panache e `Optional` via `firstResultOptional` / `findByIdOptional`
- Tratamento de erros centralizado com `ExceptionMapper` e exceptions customizadas
- Métodos de domínio na entity (`zerarJogo`, `adicionarHorasJogadas`)
- DTO genérico com Java Records para respostas padronizadas
- Versionamento de schema com Flyway
- Documentação automática com OpenAPI / Swagger UI
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
- [x] Documentação com Swagger UI
- [x] Tratamento de erros padronizado
- [x] Versionamento do banco com Flyway
- [x] Testes automatizados com JUnit 5 + REST Assured
- [ ] Documentação OpenAPI enriquecida com `@Operation` e `@Schema`
- [ ] Interface web

---

## 👨‍💻 Autor

Desenvolvido por **Marcos Guisleri**  
[![GitHub](https://img.shields.io/badge/GitHub-marcosguislei-181717?style=flat&logo=github)](https://github.com/marcosguisleri)