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
│   └── RespostaApiDTO.java         # DTO genérico para respostas da API
├── exception/
│   ├── JogoJaCadastradoException.java
│   ├── JogoJaCadastradoExceptionMapper.java
│   ├── JogoNaoEncontradoException.java
│   └── JogoNaoEncontradoExceptionMapper.java
├── model/
│   ├── Jogo.java                   # Entidade JPA com Panache e Bean Validation
│   └── Genero.java                 # Enum com os gêneros disponíveis
├── resource/
│   └── JogoResource.java           # Endpoints REST (JAX-RS)
└── service/
    └── JogoService.java            # Regras de negócio com JPQL e Panache
```

---

## 🧰 Tecnologias

- Java 25
- Quarkus 3.35
- Hibernate ORM with Panache
- PostgreSQL
- Hibernate Validator (Bean Validation)
- Quarkus REST + Jackson
- SmallRye OpenAPI (Swagger UI)
- SmallRye Health
- Docker

---

## ▶️ Como executar

**Pré-requisitos:** Java 25+, Maven e Docker instalados.

```bash
# Clone o repositório
git clone https://github.com/marcosguisleri/game-vault.git
cd game-vault

# Mude para a branch Quarkus
git checkout feature/quarkus

# Configure as variáveis de ambiente
# Crie o arquivo src/main/resources/application-local.properties com as credenciais:
#
#   quarkus.datasource.username=postgres
#   quarkus.datasource.password=postgres
#   quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5433/gamevault
#
# Esse arquivo não sobe para o Git (.gitignore)

# Suba o banco de dados
docker compose up -d

# Inicie a aplicação em modo dev
./mvnw quarkus:dev
```

| URL | Descrição |
|---|---|
| `http://localhost:8080/jogos` | API REST |
| `http://localhost:8080/q/swagger-ui` | Swagger UI |
| `http://localhost:8080/q/health` | Health check |

---

## 📡 Endpoints

### CRUD

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/jogos` | Cadastrar um jogo |
| `PUT` | `/jogos/{id}` | Atualizar um jogo |
| `DELETE` | `/jogos/{id}` | Remover um jogo |

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
- Validações declarativas com Bean Validation (`@NotBlank`, `@NotNull`, `@Min`, `@PositiveOrZero`)
- Consultas JPQL com Panache e `Optional` via `firstResultOptional` / `findByIdOptional`
- Tratamento de erros centralizado com `ExceptionMapper` e exceptions customizadas
- DTO genérico com Java Records para respostas padronizadas
- Documentação automática com OpenAPI / Swagger UI
- Health check com SmallRye Health
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
- [ ] Versionamento do banco com Flyway
- [ ] Testes automatizados
- [ ] Interface web

---

## 👨‍💻 Autor

Desenvolvido por **Marcos Guisleri**  
[![GitHub](https://img.shields.io/badge/GitHub-marcosguislei-181717?style=flat&logo=github)](https://github.com/marcosguisleri)