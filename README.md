# 🧪 Automação de Testes - Demo Project API

Automação dos testes da API Demo Project, utilizando **Java 17** com **RestAssured**, **JUnit**, e geração de evidências em PDF.  
A execução é feita via **GitHub Actions** e **GitLab CI**, com arquitetura modular de serviços e utilitários para facilitar a manutenção e reuso dos testes.

---

## 🚀 Objetivo

Automatizar testes da API com foco em:

✅ Testes automatizados para métodos HTTP (**GET, POST, PUT, DELETE**)  
✅ Validações de payloads com **RestAssured**  
✅ Arquitetura orientada a serviços e utilitários  
✅ Geração de evidências automatizadas em **PDF**  
✅ Execução automatizada com **GitHub Actions** e **GitLab CI**  
✅ Suporte a múltiplos ambientes via variáveis/configurações

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia          | Finalidade                                       |
|---------------------|-------------------------------------------------|
| Java 17             | Linguagem base                                  |
| Maven               | Gerenciador de dependências                     |
| JUnit 4             | Framework de execução dos testes                |
| REST-assured 5.2.0  | Testes de APIs REST                             |
| Hamcrest 2.2        | Validações mais expressivas                     |
| iText 9             | Geração de evidências em PDF                    |
| Java Faker 1.0.2    | Geração de dados dinâmicos para testes          |
| org.json            | Manipulação de objetos JSON                     |

---

## ▶️ Como Executar Localmente

### Pré-requisitos

- Java 17 instalado  
- Maven 3.8+ instalado

### Comando de execução

```bash
mvn clean test
```

> Relatórios são gerados automaticamente na pasta `target/surefire-reports`.

---

## 📂 Estrutura do Projeto

## 🧱 Arquitetura do Projeto

| Caminho                                    | Descrição                                                 |
|--------------------------------------------|-----------------------------------------------------------|
| `src/main/java/demo.project.api.service/`  | Serviços de negócio da API (ex: Auth, Service, payloads)  |
| `src/main/java/demo.project.api.utils/`    | Utilitários reutilizáveis (PDF, JSON, etc.)               |
| `src/test/java/demo.project.api.tests/`    | Casos de teste automatizados                              |
| `.github/workflows/api-tests.yml`          | Pipeline de execução via GitHub Actions                   |
| `.gitlab-ci.yml`                           | Pipeline de execução via GitLab CI                        |
| `pom.xml`                                  | Gerenciador de dependências Maven                         |
| `README.md`                                | Documentação do projeto                                   |

---

## 🔁 Integração Contínua

CI/CD configurado com:

- **GitHub Actions:** `.github/workflows/api-tests.yml`  
- **GitLab CI/CD:** `.gitlab-ci.yml`

Etapas executadas:

- Build e testes com Maven  
- Geração de evidências  

---

## 🍴 Como Fazer Fork do Projeto

Para contribuir com este projeto, você pode criar um **fork** no GitHub ou no GitLab, conforme a plataforma que preferir:

### No GitHub

1. Acesse o repositório principal no GitHub.  
2. Clique no botão **Fork** no canto superior direito da página.  
3. O projeto será copiado para sua conta pessoal, onde você poderá fazer alterações livremente.  
4. Após fazer suas alterações, crie um Pull Request para o repositório original.

### No GitLab

1. Acesse o repositório principal no GitLab.  
2. Clique no botão **Fork** no canto superior direito da página.  
3. Escolha o namespace (sua conta ou grupo) para onde o fork será criado.  
4. Faça suas alterações no fork criado e abra um Merge Request para o repositório original.

---
