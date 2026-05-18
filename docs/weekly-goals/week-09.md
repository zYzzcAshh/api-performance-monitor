# Week Goals (12 May – 19 May)

## 1. REST API Refactor

- Rever estrutura das rotas
- Remover ações explícitas:
  - `/create`
  - `/metrics`
- Tornar API mais RESTful e consistente

- Rever prefixos desnecessários:
  - `/api/auth/...`

###### Deliverable:
API mais consistente e alinhada com boas práticas REST


## 2. Validation & Error Handling

- Eliminar uso de `!!`
- Criar helpers reutilizáveis para:
  - parsing de parâmetros
  - validação
  - conversão segura

- Garantir respostas:
  - BadRequest
  - InvalidRequest
  - erros consistentes

###### Deliverable:
sistema de validação e tratamento de erros mais robusto


## 3. Security & Configuration

- Remover segredos hardcoded:
  - JWT secret
  - SMTP credentials

- Introduzir:
  - environment variables
  - configuração segura

- Rever configuração global da aplicação

###### Deliverable:
configuração mais segura e preparada para ambientes reais


## 4. Database Integration

- Introduzir persistência real
- Configurar:
  - PostgreSQL
  - Exposed

- Evitar JDBC direto
- Migrar armazenamento in-memory progressivamente

###### Deliverable:
primeira integração funcional com base de dados


## 5. TimescaleDB Evaluation

- Avaliar viabilidade de utilização de TimescaleDB
- Verificar:
  - compatibilidade JDBC
  - compatibilidade Exposed
  - adequação ao sistema de métricas

###### Deliverable:
decisão técnica documentada sobre utilização de TimescaleDB


## 6. Compose Refactor & UI Improvements

- Melhorar organização do `composeApp`
- Reduzir construções repetitivas
- Melhorar reutilização de componentes

- Rever estrutura atual de screens
- Avaliar necessidade de novos ecrãs

###### Deliverable:
cliente mais modular e consistente


## 7. Local Monitoring Agent

- Refinar arquitetura dos agentes
- Explorar soluções mais inovadoras
- Garantir:
  - segurança
  - privacidade
  - controlo centralizado

###### Deliverable:
arquitetura do agente mais consolidada


## 8. Project Structure Cleanup

- Rever organização global dos módulos
- Alterar nome da pasta `backend`
- Melhorar naming e estrutura do projeto

###### Deliverable:
estrutura do projeto mais coerente e organizada


## 9. Demo Video

- Preparar vídeo demonstrativo da aplicação
- Demonstrar:
  - autenticação
  - endpoints
  - monitoring
  - navegação

###### Deliverable:
vídeo demo funcional preparado para apresentação


## 10. Testing

- Executar novamente testes do sistema
- Validar:
  - rotas
  - autenticação
  - monitoring
  - navegação

###### Deliverable:
sistema validado após refactors


## Result (to fill next week)

- done:
  - Compose refactor and UI improvements
  - Project structure cleanup
  - Demo video preparation
  - System testing and validation
  - Validation and error handling improvements

- partially done:
  - REST API refactor
    - revisão parcial das rotas
    - melhoria da consistência REST
    - reorganização da navegação e estrutura da API

- not done:
  - Security & configuration
  - Database integration
  - TimescaleDB evaluation
  - Local monitoring agent improvements