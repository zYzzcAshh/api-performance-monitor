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

###### Deliverable:
API funcional com autenticação básica e gestão de endpoints (in-memory)


## 3. Monitoring Prototype

- Implementar lógica simples de monitoring:
  - Executar HTTP request a um endpoint
  - Medir latency
  - Capturar status code
- Criar estrutura de métricas

###### Deliverable:
primeiro protótipo de monitorização funcional


## 4. Worker (Simplified)

- Criar componente worker básico
- Executar checks periódicos (ex: scheduler simples)
- Enviar métricas para o backend (ou logar localmente)

###### Deliverable:
worker simples a executar checks automaticamente


## 5. Technical Decisions & Documentation

- Atualizar documentação com:
  - mudança para Ktor
  - uso de in-memory storage (para já)

###### Deliverable:
documentação atualizada (docs/)


## 6. Simple Metrics Aggregation (optional)

- Calcular uptime básico
- Calcular average latency

Deliverable:
endpoint com métricas agregadas


## Result (to fill next week)
- done
- partially done
- not done