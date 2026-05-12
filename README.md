# 🎮 GameVault

Sistema de gerenciamento de biblioteca de jogos pessoal desenvolvido em Java, com foco em boas práticas de Orientação a Objetos.

---

## 📋 Sobre o projeto

O GameVault permite gerenciar uma coleção de jogos via terminal, registrando informações como gênero, ano de lançamento, horas jogadas e status de conclusão. Os dados são persistidos em arquivo JSON e carregados automaticamente a cada execução.

---

## 🚀 Funcionalidades

- ➕ Cadastrar novos jogos
- 📋 Listar todos os jogos da coleção
- 🔍 Buscar jogo por título
- 📊 Estatísticas da biblioteca (total de jogos, horas jogadas, jogo mais jogado)
- 💾 Persistência automática em arquivo JSON
- 🎯 Filtros por gênero, ano, status e quantidade de horas
- 🔃 Ordenação por título, ano e horas jogadas

---

## 🏗️ Estrutura do projeto

```
src/main/java/br/dev/guisleri/
├── cli/
│   └── Main.java               # Menu interativo e ponto de entrada
├── model/
│   ├── Jogo.java               # Entidade principal com validações
│   └── Genero.java             # Enum com 12 gêneros disponíveis
├── repository/
│   └── JogoRepository.java     # Leitura e escrita em JSON via Jackson
└── service/
    └── JogoService.java        # Regras de negócio e gerenciamento da coleção
```

---

## 🧰 Tecnologias

- Java 25
- Maven
- Jackson Databind 2.21.2

---

## ▶️ Como executar

**Pré-requisitos:** Java 25+ e Maven instalados.

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/game-vault.git

# Entre na pasta do projeto
cd game-vault

# Compile
mvn compile

# Execute
mvn exec:java
```

Na primeira execução, o sistema carrega uma coleção inicial de jogos e gera o arquivo `jogos.json` automaticamente na raiz do projeto.

---

## 📚 Conceitos aplicados

- Orientação a Objetos (encapsulamento, abstração)
- Enum para domínio fechado de valores
- Validações no construtor com `IllegalArgumentException`
- Streams, lambdas e method references
- `Optional` para buscas que podem não retornar resultado
- `Comparator` para ordenações flexíveis
- Imutabilidade com `Collections.unmodifiableList`
- Serialização e desserialização JSON com Jackson (`@JsonCreator`, `@JsonProperty`)
- Separação de responsabilidades (Model / Service / Repository / CLI)
- Padrão seed para dados iniciais

---

## 🗺️ Roadmap

- [x] CRUD básico em memória com OOP
- [x] Ordenação por horas, título e ano com `Comparator`
- [x] Estatísticas da coleção
- [x] Persistência em arquivo JSON com Jackson
- [x] Menu interativo CLI
- [x] Filtros e ordenações no menu (submenus)
- [ ] Banco de dados com JPA
- [ ] API REST com Quarkus
- [ ] Interface web

---

## 👨‍💻 Autor

Desenvolvido por **Marcos Guisleri**  
[![GitHub](https://img.shields.io/badge/GitHub-seu--usuario-181717?style=flat&logo=github)](https://github.com/seu-usuario)