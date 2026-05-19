# Week Goals (19 May – 26 May)

## 1. Database Integration

- Configurar PostgreSQL no projeto
- Integrar Exposed ORM
- Definir primeiras tabelas:
  - users
  - endpoints
  - metrics

- Iniciar migração gradual do armazenamento memory para persistência real

###### Deliverable:
primeira versão funcional da persistência em base de dados


## 2. Repository Layer Refactor

- Implementar repositories PostgreSQL
- Manter compatibilidade com repositories memory
- Garantir separação clara entre interfaces e implementações

- Adaptar testes para:
  - repositories memory
  - repositories postgres

###### Deliverable:
camada de persistência modular e preparada para múltiplas implementações


## 3. Technical Decisions Documentation

- Criar documento técnico com justificação das tecnologias utilizadas:
  - Ktor
  - Kotlin Multiplatform
  - Exposed
  - PostgreSQL
  - JWT
  - Password4j
  - Jakarta Mail
  - Swagger/OpenAPI
  - WASM

- Explicar principais decisões arquiteturais

###### Deliverable:
documentação técnica das decisões do projeto


## 4. Dependency Cleanup

- Rever dependências Gradle
- Remover redundâncias
- Corrigir dependências mal posicionadas entre módulos
- Organizar melhor:
  - server
  - shared
  - composeApp

###### Deliverable:
estrutura de dependências mais limpa e organizada


## 5. Frontend Navigation & Screens

- Separar screens de:
  - configuração/definição
  - resultados de monitorização

- Melhorar navegação geral
- Rever reutilização de componentes Compose

###### Deliverable:
fluxo de navegação mais claro e organizado


## 6. Client Testing

- Avaliar testes adicionais para o cliente
- Explorar:
  - testes de navegação
  - testes de UI
  - testes de estado

###### Deliverable:
maior cobertura de testes no frontend


## 7. Cross-Platform Validation

- Testar aplicação em diferentes plataformas:
  - Desktop
  - Android
  - WASM/Web

- Identificar inconsistências de comportamento ou UI

###### Deliverable:
validação multiplataforma inicial


## 8. Monitoring & Architecture Improvements

- Continuar refinamento da arquitetura de monitoring
- Rever workers e organização interna
- Melhorar estrutura geral do sistema de monitorização

###### Deliverable:
arquitetura de monitoring mais consistente


## 9. GitHub & Project Management

- Adicionar Paulo Pereira ao repositório GitHub
- Preparar email de onboarding/contextualização
- Melhorar organização geral do repositório

###### Deliverable:
melhor gestão e colaboração do projeto


## 10. General Refactor & Cleanup

- Continuar limpeza de código
- Melhorar naming e consistência
- Rever estrutura geral dos módulos

###### Deliverable:
código mais consistente e organizado


## Result (to fill next week)

- done:
- -

- partially done:
- -

- not done:
- -