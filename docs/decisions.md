# Technical Decisions & Architecture

## 1. Introduction

Breve contextualização do projeto e dos objetivos principais da arquitetura:
- modularidade
- multiplataforma
- simplicidade
- escalabilidade
- maintainability
- testabilidade

---

# 2. Backend Framework - Ktor

## Why Ktor?

- framework nativo para Kotlin
- lightweight
- asynchronous/non-blocking
- integração natural com coroutines
- maior controlo arquitetural
- menor overhead comparativamente a Spring Boot

## Alternatives considered

- Spring Boot

## Why not Spring Boot?

- mais pesado
- maior complexidade
- menos alinhado com Kotlin idiomático
- excesso de abstração para o contexto do projeto

---

# 3. Kotlin Multiplatform (KMP)

## Why KMP?

- partilha de código entre plataformas
- reutilização de domain models e DTOs
- consistência entre cliente e servidor
- redução de duplicação

## Advantages for the project

- shared validation
- shared serialization
- shared business models

---

# 4. Compose Multiplatform & WASM

## Why Compose Multiplatform?

- UI declarativa moderna
- alinhamento com Kotlin ecosystem
- reutilização entre desktop/web

## Why WASM?

- execução no browser sem JavaScript tradicional
- exploração tecnológica moderna
- alinhamento com visão multiplataforma

---

# 5. PostgreSQL

## Why PostgreSQL?

- robustez
- estabilidade
- open-source
- excelente suporte relacional
- compatibilidade forte com ferramentas Kotlin

## Why relational database?

- relações claras entre:
  - users
  - endpoints
  - metrics
  - alert rules

---

# 6. Exposed ORM

## Why Exposed?

- DSL nativa em Kotlin
- type-safe queries
- compile-time validation
- integração natural com coroutines

## Why not JDBC directly?

- maior verbosidade
- maior risco de erros runtime
- menor maintainability

---

# 7. JWT Authentication

## Why JWT?

- stateless authentication
- simplicidade para APIs REST
- facilidade de integração frontend/backend

---

# 8. Password4j

## Why Password4j?

- hashing seguro de passwords
- suporte moderno para algoritmos robustos
- simplicidade de integração

---

# 9. Jakarta Mail

## Why Jakarta Mail?

- standard consolidado para SMTP em JVM
- integração simples para notificações por email

---

# 10. Swagger / OpenAPI

## Why Swagger?

- documentação automática da API
- facilidade de teste e exploração
- melhoria da developer experience

---

# 11. Architecture Decisions

## Layered Architecture

Separação em:
- routes
- services
- repositories
- domain
- dto

## Why this architecture?

- separação de responsabilidades
- testabilidade
- maintainability
- flexibilidade de implementação

---

# 12. Repository Abstraction

## Why repository interfaces?

- suporte a múltiplas implementações:
  - memory
  - PostgreSQL

- facilidade de testes
- desacoplamento da persistência

---

# 13. Testing Strategy

- route tests
- service tests
- repository tests
- isolamento de responsabilidades

---

# 14. Future Improvements

- TimescaleDB evaluation
- distributed monitoring agents
- notification improvements
- dashboard evolution