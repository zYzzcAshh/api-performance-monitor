# Week Goals (24 Mar – 31 Mar)

## 1. Domain Refactor (Strong Types)

- Introduzir tipos de domínio fortes:
  - Username
  - Password
  - UserId
  - EndpointUrl
- Validar dados no momento de criação (init / require)
- Remover validações espalhadas pelos services

###### Deliverable:
Domain model mais robusto:
- validação centralizada nos tipos
- eliminação de dados inválidos no sistema


## 2. DTO vs Domain Separation

- Garantir separação clara entre DTOs e domain
- DTOs usam apenas tipos primitivos:
  - String, Int, Long
- Conversão explícita DTO → Domain
- Evitar uso de Instant e tipos complexos nos DTOs

###### Deliverable:
Camada de API desacoplada do domain interno


## 3. Package Organization

- Reorganizar estrutura do projeto:
  - shared/domain
  - shared/exceptions
  - server/routes
  - server/services
- Mover exceptions para módulo shared

###### Deliverable:
Projeto com estrutura modular clara e consistente


## 4. Exception Handling Simplification

- Reduzir uso de exceptions custom desnecessárias
- Usar:
  - IllegalArgumentException
  - IllegalStateException
- Manter apenas exceptions de domínio relevantes
- Centralizar mapping exception > HTTP status

###### Deliverable:
Sistema de erros mais simples e consistente


## 5. Constants & API Contract

- Criar dicionário de rotas (Routes.kt)
- Evitar strings hardcoded nos endpoints
- Partilhar constantes entre server e futuro cliente

###### Deliverable:
API mais consistente e menos propensa a erros


## 6. Monitoring System Improvements

- Consolidar sistema de múltiplos workers por intervalo
- Garantir filtragem eficiente de endpoints por intervalo
- Melhorar robustez dos workers (fail-safe execution)

###### Deliverable:
Sistema de monitoring mais escalável e eficiente


## 7. Minimal Client (Frontend Prototype)

- Criar cliente simples (Compose Multiplatform ou CLI)
- Implementar funcionalidades mínimas:
  - register
  - login
  - listar endpoints
- Validar fluxo end-to-end

###### Deliverable:
Cliente funcional para testar o sistema real


## 8. UI & UX Considerations (early)

- Definir princípios base de layout:
  - adaptável a web / mobile
- Preparar estrutura para evolução futura

###### Deliverable:
Base para frontend consistente e multiplatform


## 9. Testing Improvements

- Adaptar testes às mudanças no domain
- Garantir cobertura após refactor
- Manter testes de integração (routes)

###### Deliverable:
Testes atualizados e sistema estável após refactor


## Result (filled)

## Result

- done:
  - Domain Refactor
  - DTO vs Domain Separation
  - Package Organization
  - Exception Handling
  - Constants & API Contract
  - Monitoring System Improvements
  - Testing Improvements
  - Cliente funcional completo (end-to-end UI)
  - Minimal Client
  - UI & UX Considerations

- partially done:
  - —

- not done:
  - —