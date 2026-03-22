# Week Goals (17 Mar - 24 Mar)

## 1. Backend Setup (Ktor)

- Criar projeto backend com Ktor
- Configurar servidor HTTP básico
- Definir estrutura do projeto (routes, services, models)

###### Deliverable:
Ktor server a correr com estrutura modular organizada:
- `routes/` ⭢ definição de endpoints HTTP
- `services/` ⭢ lógica de negócio
- `repo/` ⭢ abstração de dados (memory)
- `configure/` ⭢ configuração (auth, serialization, errors)
- `app/` ⭢ bootstrap da aplicação

---

## 2. API Base (In-Memory)

- Implementar endpoints básicos:
  - POST /api/auth/register
  - POST /api/auth/login
- Criar estrutura para gestão de endpoints monitorizados
- Armazenar dados em memória (sem base de dados)
- Implementar autenticação JWT
- Associar endpoints a utilizadores (userId via JWT)

###### Deliverable:
API funcional com:
- Autenticação JWT (login devolve token)
- Endpoints protegidos com `authenticate("auth-jwt")`
- Gestão de endpoints por utilizador:
  - POST /api/endpoints/create
  - GET /api/endpoints
  - DELETE /api/endpoints/{id}
- Isolamento por utilizador garantido via `userId` no token

---

## 3. Monitoring Prototype

- Implementar lógica simples de monitoring
- Executar HTTP request a um endpoint
- Medir latency
- Capturar status code
- Criar estrutura de métricas
- Expor endpoint para executar checks manualmente

###### Deliverable:
Protótipo funcional de monitorização:
- Serviço `MonitoringService`
- Modelo `RequestMetric`
- Endpoint:
  - POST /api/metrics/check
- Métricas incluem:
  - latency
  - statusCode
  - timestamp

---

## 4. Worker (Simplified)

- Criar componente worker básico
- Executar checks periódicos (scheduler simples com coroutines)
- Integrar worker com endpoints reais (não hardcoded)
- Guardar métricas automaticamente

###### Deliverable:
Worker funcional:
- Classe `MonitoringWorker`
- Execução periódica com coroutines (`delay`)
- Integração com `EndpointService`
- Persistência automática de métricas via `MetricsService`
- Logs com resultados dos checks

---

## 5. Technical Decisions & Documentation

- Atualizar documentação com:
  - mudança para Ktor
  - uso de in-memory storage
  - arquitetura do sistema
- Atualizar data model e metrics format

###### Deliverable:
Documentação atualizada com:
- Decisão de stack:
  - Ktor (backend)
  - kotlinx.serialization
  - JWT auth
- Arquitetura em camadas:
  - routes ⭢ services ⭢ repository
- Data model:
  - User
  - MonitoredEndpoint
  - RequestMetric
- Estratégia atual:
  - armazenamento in-memory (fase inicial)

---

## 6. Simple Metrics Aggregation (optional)

- Calcular uptime básico
- Calcular average latency

###### Deliverable:
Endpoint implementado:
- GET /api/metrics/{endpointId}/summary

Inclui:
- uptime (% de requests 2xx)
- average latency
- total requests

---

## 7. Validation & Testing (extra)

- Validação de inputs:
  - URL válida
  - interval >= 50s
  - password segura (uppercase + digit)
  - evitar endpoints duplicados
- Tratamento de erros com StatusPages

- Testes implementados:
  - Services:
    - AuthServiceTests
    - EndpointServiceTests
    - MetricsServiceTests
  - Routes:
    - AuthRoutesTests
    - EndpointRoutesTests
    - MetricsRoutesTests
  - Test utilities:
    - getToken()
    - createEndpoint()

###### Deliverable:
Cobertura de testes unitários e de integração com Ktor TestHost

---

## Result

- done:
  - Backend Setup
  - API Base
  - Monitoring Prototype
  - Worker
  - Documentation
  - Metrics Aggregation (optional)
  - Validation & Error Handling
  - Testing (services + routes)

- partially done:
  - —

- not done:
  - —