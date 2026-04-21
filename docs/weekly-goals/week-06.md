# Week Goals (21 Apr – 27 Apr)

## 1. Progress & Presentation

- Preparar slides para apresentação de progresso
- Mostrar:
  - arquitetura do sistema
  - fluxo end-to-end
  - aplicação a correr
- Garantir demonstração funcional (cliente + backend)

###### Deliverable:
slides + demo funcional para apresentação de progresso


## 2. GitHub Organization

- Criar issues para features principais:
  - auth
  - endpoints
  - monitoring
  - workers
  - client
  - local agent
- Organizar trabalho por milestones

###### Deliverable:
repositório com issues estruturadas e rastreáveis


## 3. Client Navigation & UI

- Criar diagrama de navegação de ecrãs
- Criar diagrama de estados (cada estado = ecrã)
- Garantir separação:
  - UI (composables)
  - lógica (view models / state)

- Implementar navegação com Compose Navigation:
  - login
  - register
  - endpoints list

###### Deliverable:
cliente com navegação estruturada + diagramas documentados


## 4. Multiplatform Testing

- Testar aplicação em:
  - web
  - Android
- Validar comportamento consistente entre plataformas

###### Deliverable:
cliente validado em múltiplas plataformas


## 5. Backend Configuration

- Uniformizar configuração:
  - portas (8080 / 8081)
- Corrigir inconsistências no application config
- Garantir uso de config externa (yaml)

###### Deliverable:
backend configurado corretamente e consistente


## 6. Local Monitoring Agent (Design)

- Refinar arquitetura do agente:
  - agente executa checks localmente
  - backend controla:
    - periodicidade
    - configuração

- Definir responsabilidades do agente:
  - acesso a APIs privadas
  - execução local
  - envio de métricas

- Documentar fluxo:
  - backend → agente → API → backend

###### Deliverable:
documentação clara do local agent + fluxo definido


## 7. API Documentation (OpenAPI)

- Criar especificação OpenAPI da API
- Documentar endpoints:
  - auth
  - endpoints
  - metrics
- Integrar documentação com Ktor

###### Deliverable:
API documentada com OpenAPI + suporte a testes


## 8. Codebase Cleanup & Shared Module

- Rever dependências (`build.gradle`)
- Remover duplicação
- Mover código partilhado para `shared`:
  - domain
  - constants
  - DTOs (se aplicável)

###### Deliverable:
estrutura modular mais limpa e consistente


## Result (to fill next week)

- done:
  - 

- partially done:
  - 

- not done:
  - 