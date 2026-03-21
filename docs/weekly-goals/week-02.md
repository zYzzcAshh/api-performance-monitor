# Week Goals (17 Mar – 24 Mar)

## 1. Backend Setup (Ktor)

- Criar projeto backend com Ktor
- Configurar servidor HTTP básico
- Definir estrutura do projeto (routes, services, models)

###### Deliverable:
Ktor server a correr com estrutura base do projeto


## 2. API Base (In-Memory)

- Implementar endpoints básicos:
  - POST /auth/register
  - POST /auth/login
- Criar estrutura para gestão de endpoints monitorizados
- Armazenar dados em memória (sem base de dados)
- Implementar autenticação JWT
- Associar endpoints a utilizadores (userId via JWT)

###### Deliverable:
API funcional com autenticação básica, gestão de endpoints e isolamento por utilizador


## 3. Monitoring Prototype

- Implementar lógica simples de monitoring
- Executar HTTP request a um endpoint
- Medir latency
- Capturar status code
- Criar estrutura de métricas
- Expor endpoint para executar checks manualmente

###### Deliverable:
primeiro protótipo de monitorização funcional


## 4. Worker (Simplified)

- Criar componente worker básico
- Executar checks periódicos (scheduler simples com coroutines)
- Integrar worker com endpoints reais (não hardcoded)
- Guardar métricas automaticamente

###### Deliverable:
worker simples a executar checks automaticamente


## 5. Technical Decisions & Documentation

- Atualizar documentação com:
  - mudança para Ktor
  - uso de in-memory storage
  - arquitetura do sistema
- Atualizar data model e metrics format

###### Deliverable:
documentação atualizada (docs/)


## 6. Simple Metrics Aggregation (optional)

- Calcular uptime básico
- Calcular average latency

###### Deliverable:
endpoint com métricas agregadas


## Result

- done:
  - Backend Setup
  - API Base
  - Monitoring Prototype
  - Worker
  - Documentation

- partially done:
  - —

- not done:
  - Metrics Aggregation (optional)