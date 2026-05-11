# 🎮 GameVault

Sistema de gerenciamento de biblioteca de jogos pessoal desenvolvido em Java, com foco em boas práticas de Orientação a Objetos.

---

## 📋 Sobre o projeto

O GameVault permite gerenciar uma coleção de jogos, registrando informações como gênero, ano de lançamento, horas jogadas e status de conclusão. O projeto foi desenvolvido com foco em encapsulamento, validações e separação de responsabilidades.

---

## 🚀 Funcionalidades

- Cadastrar jogos na coleção
- Listar todos os jogos
- Filtrar jogos por gênero
- Filtrar jogos por ano de lançamento
- Filtrar por quantidade de horas jogadas
- Listar jogos zerados e não zerados
- Buscar jogo por título
- Registrar horas jogadas
- Marcar jogo como zerado

---

## 🏗️ Estrutura do projeto

```
src/main/java/br/dev/guisleri/
├── cli/
│   └── Main.java           # Ponto de entrada da aplicação
├── model/
│   ├── Jogo.java           # Entidade principal
│   └── Genero.java         # Enum de gêneros disponíveis
└── service/
    └── JogoService.java    # Regras de negócio e gerenciamento da coleção
```

---

## 🧰 Tecnologias

- Java 25
- Maven

---

## ▶️ Como executar

**Pré-requisitos:** Java 25+ e Maven instalados.

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/game-vault.git

# Entre na pasta do projeto
cd game-vault

# Compile e execute
mvn compile exec:java
```

---

## 📚 Conceitos aplicados

- Orientação a Objetos (encapsulamento, abstração)
- Enum para domínio fechado de valores
- Validações no construtor com `IllegalArgumentException`
- Streams e expressões lambda
- Method references
- `Optional` para buscas que podem não retornar resultado
- Imutabilidade com `Collections.unmodifiableList`
- Separação de responsabilidades (Model / Service / CLI)

---

## 🗺️ Roadmap

- [ ] Ordenação por horas, título e ano com `Comparator`
- [ ] Estatísticas da coleção (total de horas, jogo mais jogado)
- [ ] Persistência em arquivo JSON
- [ ] Banco de dados com JPA
- [ ] API REST com Quarkus
- [ ] Interface web

---

## 👨‍💻 Autor

Desenvolvido por **Marcos Guisleri**  
[![GitHub](https://img.shields.io/badge/GitHub-seu--usuario-181717?style=flat&logo=github)](https://github.com/seu-usuario)
