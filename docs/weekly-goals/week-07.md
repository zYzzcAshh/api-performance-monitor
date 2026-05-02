# Week Goals (28 Apr – 05 May)

## 1. Progress Presentation (Refinement)

- Atualizar slides com base no feedback:
  - adicionar diagrama temporal (plano do projeto)
  - incluir tecnologias utilizadas (Kotlin Multiplatform - KMP)
  - melhorar explicação da arquitetura e fluxo
  - incluir navegação de ecrãs

###### Deliverable:
slides finais refinados para apresentação de progresso


## 2. Client Navigation & UI (Completion)

- Criar diagrama de navegação de ecrãs
- Criar diagrama de estados (cada estado = ecrã)

- Finalizar navegação com Compose Navigation:
  - login
  - register
  - endpoints list

- Garantir consistência entre:
  - implementação
  - diagramas

###### Deliverable:
cliente com navegação completa + diagramas documentados


## 3. Multiplatform Testing

- Testar aplicação em:
  - web
  - Android
- Validar comportamento consistente entre plataformas

###### Deliverable:
cliente validado em múltiplas plataformas


## 4. GitHub Organization

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


## 9. Multiplatform Consistency (KMP)

- Rever bibliotecas utilizadas no projeto
- Garantir compatibilidade com Kotlin Multiplatform:
  - remover dependências não compatíveis

###### Deliverable:
stack tecnológica consistente com KMP


## 10. Password Security (Refactor)

- Substituir biblioteca jBCrypt
- Avaliar alternativas compatíveis com KMP
- Integrar nova solução de hashing de passwords

###### Deliverable:
sistema de password hashing compatível com KMP


## 11. Evaluation Preparation

- Identificar possíveis arguentes para o projeto
- Avaliar alinhamento com:
  - tema (observability / distributed systems)
  - tecnologias utilizadas

###### Deliverable:
lista de possíveis arguentes para discussão com orientador


## Result

- done:
  - Client Navigation & UI (Completion) – navegação implementada com Compose Navigation + diagramas (PlantUML)
  - Password Security (Refactor) – substituição de jBCrypt por Password4j (compatível com KMP)
  - Evaluation Preparation – identificação de possíveis arguentes e alinhamento com tema

- partially done:
  - Progress Presentation (Refinement)
  - Backend Configuration
  - GitHub Organization

- not done:
  - Multiplatform Testing
  - Local Monitoring Agent (Design)
  - API Documentation (OpenAPI)
  - Codebase Cleanup & Shared Module
  - Multiplatform Consistency (KMP)