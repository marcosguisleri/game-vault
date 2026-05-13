# 🎮 GameVault

Sistema de gerenciamento de biblioteca de jogos pessoal, evoluído de uma aplicação CLI com persistência em JSON para uma API REST com banco de dados relacional.

---

## 📋 Sobre o projeto

O GameVault permite gerenciar uma coleção de jogos registrando informações como gênero, ano de lançamento, horas jogadas e status de conclusão. Nesta fase, o projeto foi migrado de uma aplicação CLI orientada a objetos para uma API REST construída com Quarkus e PostgreSQL.

> A versão CLI original está preservada na branch `master`.

---

## 🚀 Funcionalidades

- ➕ Cadastrar novos jogos
- 📋 Listar todos os jogos da coleção
- 🔍 Buscar jogo por título
- 🎯 Filtros por gênero, status de conclusão e ano
- 🔃 Ordenação por título, ano e horas jogadas
- 📖 Documentação interativa via Swagger UI

---

## 🏗️ Estrutura do projeto

```
src/main/java/br/dev/guisleri/
├── model/
│   ├── Jogo.java               # Entidade JPA com Panache
│   └── Genero.java             # Enum com 12 gêneros disponíveis
├── resource/
│   └── JogoResource.java       # Endpoints REST
└── service/
    └── JogoService.java        # Regras de negócio
```

---

## 🧰 Tecnologias

- Java 25
- Quarkus 3
- Hibernate ORM with Panache
- PostgreSQL
- RESTEasy Reactive + Jackson
- Hibernate Validator
- SmallRye OpenAPI (Swagger UI)
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

# Suba o banco de dados
docker run --name gamevault-db \
  -e POSTGRES_USER=gamevault \
  -e POSTGRES_PASSWORD=gamevault \
  -e POSTGRES_DB=gamevault \
  -p 5432:5432 \
  -d postgres:15

# Inicie a aplicação em modo dev
./mvnw quarkus:dev
```

A aplicação estará disponível em `http://localhost:8080`.

O Swagger UI estará disponível em `http://localhost:8080/q/swagger-ui`.

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

**Fase 2 — API REST com Quarkus** (`feature/quarkus`)
- JPA com Hibernate ORM e padrão Active Record (Panache)
- Injeção de dependência com CDI (`@ApplicationScoped`, `@Inject`)
- Controle de transações com `@Transactional`
- API REST com JAX-RS
- Validações declarativas com Bean Validation
- Documentação automática com OpenAPI/Swagger

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
- [ ] Interface web

---

## 👨‍💻 Autor

Desenvolvido por **Marcos Guisleri**  
[![GitHub](https://img.shields.io/badge/GitHub-marcosguisleri-181717?style=flat&logo=github)](https://github.com/marcosguisleri)