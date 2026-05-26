# Week Goals (19 May – 26 May)

## 1. Database Integration

- Configurar PostgreSQL no projeto
- Integrar Exposed ORM
- Definir primeiras tabelas:
  - users
  - endpoints
  - metrics
  - agents

- Iniciar migração gradual do armazenamento memory para persistência real
- Configurar inicialização automática da base de dados
- Estruturar DatabaseConfig e DatabaseInitializer
- Criar ambiente de testes com H2 em memória

###### Deliverable:
primeira versão funcional da persistência em base de dados


## 2. Repository Layer Refactor

- Implementar repositories PostgreSQL/Exposed
- Manter compatibilidade com repositories memory
- Garantir separação clara entre interfaces e implementações

- Adaptar testes para:
  - repositories memory
  - repositories postgres/exposed

- Criar mappers e extensões para conversão de entidades
- Melhorar organização interna da camada de persistência
- Reduzir acoplamento entre repositories e serialização

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
- Documentar estrutura geral do backend e frontend
- Explicar abordagem adotada para monitoring distribuído

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

- Corrigir imports e warnings
- Atualizar APIs deprecated
- Melhorar consistência Kotlin idiomática

###### Deliverable:
estrutura de dependências mais limpa e organizada


## 5. Frontend Navigation & Screens

- Separar screens de:
  - configuração/definição
  - resultados de monitorização

- Melhorar navegação geral
- Rever reutilização de componentes Compose
- Melhorar organização visual das screens

###### Deliverable:
fluxo de navegação mais claro e organizado


## 6. Backend Testing

- Criar testes para:
  - repositories
  - services
  - routes/endpoints

- Validar persistência PostgreSQL/Exposed
- Criar ambiente isolado de testes com H2
- Melhorar cobertura geral do backend
- Corrigir problemas de isolamento entre testes

###### Deliverable:
maior cobertura e estabilidade dos testes backend


## 7. Client Testing

- Avaliar testes adicionais para o cliente
- Explorar:
  - testes de navegação
  - testes de UI
  - testes de estado

###### Deliverable:
maior cobertura de testes no frontend


## 8. Cross-Platform Validation

- Testar aplicação em diferentes plataformas:
  - Desktop
  - Android
  - WASM/Web

- Identificar inconsistências de comportamento ou UI

###### Deliverable:
validação multiplataforma inicial


## 9. Monitoring & Architecture Improvements

- Continuar refinamento da arquitetura de monitoring
- Rever workers e organização interna
- Melhorar estrutura geral do sistema de monitorização

- Melhorar lógica de alertas
- Implementar cooldown de notificações
- Melhorar logs e rastreabilidade
- Corrigir nullable handling e remover unsafe operators (`!!`)
- Melhorar organização do código dos workers

###### Deliverable:
arquitetura de monitoring mais consistente


## 10. GitHub & Project Management

- Adicionar Paulo Pereira ao repositório GitHub
- Preparar email de onboarding/contextualização
- Melhorar organização geral do repositório

- Rever estrutura de pastas
- Melhorar organização de commits e branches

###### Deliverable:
melhor gestão e colaboração do projeto


## 11. General Refactor & Cleanup

- Continuar limpeza de código
- Melhorar naming e consistência
- Rever estrutura geral dos módulos

- Aplicar melhorias Kotlin idiomáticas
- Remover warnings e código unsafe
- Melhorar organização de mappers e extensões
- Refatorar helpers e funções auxiliares
- Melhorar legibilidade e manutenção do código

###### Deliverable:
código mais consistente, idiomático e organizado


# Result (to fill until 26th May)

- done:
  - Database integration
    - configuração inicial de PostgreSQL
    - integração de Exposed ORM
    - criação das tabelas principais
    - criação da tabela de agents
    - migração inicial de armazenamento memory para persistência real
    - criação de DatabaseConfig e DatabaseInitializer
    - configuração de ambiente de testes com H2

  - Repository layer refactor
    - implementação dos repositories PostgreSQL/Exposed
    - manutenção de compatibilidade com repositories memory
    - separação consistente entre interfaces e implementações
    - alinhamento da camada de persistência com Exposed
    - criação de mappers e extensões dedicadas
    - reorganização da lógica de serialização

  - Repository testing
    - testes concluídos para repositories memory
    - testes concluídos para repositories PostgreSQL/Exposed
    - validação da persistência e queries principais
    - correção de isolamento de testes

  - Service & route testing
    - testes concluídos para services
    - testes concluídos para routes/endpoints
    - melhoria significativa da cobertura de testes do backend
    - validação de autenticação e autorização
    - testes de validação de requests e responses

  - Monitoring & architecture improvements
    - melhoria da arquitetura dos workers
    - implementação de cooldown de alertas
    - melhoria da lógica de monitorização
    - melhoria dos logs internos
    - reorganização parcial do sistema de monitoring

  - Kotlin cleanup & refactor
    - remoção de unsafe operators (`!!`)
    - melhoria do nullable handling
    - adoção de APIs modernas de Duration
    - melhoria da consistência Kotlin idiomática
    - reorganização de helpers e extensões
    - melhoria de naming e legibilidade

  - Technical decisions documentation
    - documentação técnica iniciada
    - justificação das principais tecnologias utilizadas
    - alinhamento arquitetural e documentação de decisões

  - GitHub & project management
    - adição do arguente ao repositório
    - preparação de onboarding/contextualização
    - melhoria da organização do repositório

- partially done:
  - Dependency cleanup
    - algumas dependências reorganizadas
    - redundâncias ainda por rever
    - melhoria parcial da estrutura Gradle

  - Frontend navigation & screens
    - separação parcial de screens
    - melhorias iniciais de navegação e organização Compose

  - General refactor & cleanup
    - melhoria progressiva de naming e estrutura
    - limpeza parcial de código e módulos
    - melhoria gradual da organização do projeto

- not done:
  - Client testing
  - Cross-platform validation