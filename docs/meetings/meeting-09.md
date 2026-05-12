# Relatório da Reunião 9

**Data / Hora:** 12/05/2026 - 16:30

## Tema
Revisão técnica da API, consistência arquitetural e preparação da demonstração final.

## Resumo da Reunião
Durante esta reunião foi apresentado o progresso realizado desde a semana anterior, incluindo alterações ainda não refletidas no repositório GitHub.

O orientador analisou várias inconsistências presentes na API e na organização atual do projeto, principalmente relacionadas com naming, estrutura RESTful, configuração de segurança e qualidade geral do código.

Foram também discutidos aspetos relacionados com persistência de dados, tecnologias de base de dados e evolução futura do sistema de agentes locais.

Por fim, foi reforçada a necessidade de preparar uma demonstração em vídeo da aplicação para garantir estabilidade durante futuras apresentações.

## Decisões Tomadas

### API Design & REST Consistency
- Rever organização das rotas da API.
- Evitar prefixos desnecessários como:
  - `/api/auth/...`
- Tornar rotas mais RESTful:
  - evitar ações explícitas como:
    - `/create`
    - `/metrics`
- Melhorar consistência global da API.

### Agent Routes
- Foi discutida a existência de `AgentRoutes.kt`.
- Apesar de atualmente existir pouca funcionalidade associada aos agentes, foi considerada útil a possibilidade futura de:
  - `createAgent`
  - gestão explícita de agentes registados.

### Segurança & Configuração
- Remover segredos hardcoded:
  - JWT secret (`AuthConfig`)
  - credenciais SMTP

- Passar configuração sensível para:
  - variáveis de ambiente
  - ficheiros de configuração seguros

### Error Handling & Validation
- Eliminar completamente uso de `!!`.
- Substituir falhas por validações explícitas com:
  - `InvalidRequest`
  - `BadRequest`

Exemplo:
```kotlin
val endpointId = call.parameters["endpoint"]!!.toUInt()
```

Deve passar a:
- helper reutilizável:
  - valida parâmetro
  - converte tipo
  - devolve erro apropriado

### Compose & UI
- Rever construções repetitivas no `composeApp`.
- Melhorar organização e reutilização de componentes.

- O orientador observou que o sistema ainda possui poucos screens e baixa complexidade de navegação.

### Persistência & Base de Dados
- Introduzir persistência real com:
  - PostgreSQL
  - Exposed

- Evitar utilização direta de JDBC.

### TimescaleDB
- Foi discutida a possibilidade de utilização de **TimescaleDB**.
- A ideia foi considerada interessante para métricas temporais.

No entanto:
- é necessário validar:
  - compatibilidade com JDBC
  - compatibilidade com Exposed
  - conformidade com os requisitos do projeto

### Local Monitoring Agent
- Continuar a explorar possibilidades mais inovadoras para os agentes locais.
- Pensar em soluções fora da abordagem tradicional, mantendo:
  - segurança
  - privacidade
  - controlo centralizado

### Organização do Projeto
- Alterar nome da pasta `backend`, dado que atualmente o módulo representa mais do que apenas backend.

### Demonstração
- Preparar vídeo demo da aplicação até sexta-feira.

## Próximos Passos / Responsáveis

### API Refactor
- Refatorar rotas REST:
  - remover `/create`
  - simplificar paths
  - melhorar consistência

### Segurança
- Remover segredos hardcoded.
- Introduzir variáveis de ambiente/config segura.

### Validation & Helpers
- Criar helpers reutilizáveis para:
  - parâmetros
  - validações
  - parsing seguro

### Compose Refactor
- Melhorar reutilização no `composeApp`.
- Rever estrutura de screens.

### Base de Dados
- Integrar PostgreSQL + Exposed.
- Avaliar viabilidade de TimescaleDB.

### Local Agent
- Refinar arquitetura do agente.
- Explorar ideias diferenciadoras.

### Demonstração
- Preparar vídeo demo funcional até sexta-feira.

## Observações / Lembretes
- A consistência RESTful da API é importante para qualidade arquitetural e manutenção futura.
- O uso de `!!` deve ser evitado completamente para prevenir falhas inesperadas.
- A gestão segura de credenciais e segredos é essencial mesmo em protótipos académicos.
- A introdução de persistência real representa uma nova fase do projeto, aproximando-o de um sistema mais completo.
- O local monitoring agent continua a ser um dos principais elementos diferenciadores do projeto e deve ser bem pensado antes da implementação definitiva.