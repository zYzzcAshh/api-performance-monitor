# Relatório da Reunião 4

**Data / Hora:** 24/03/2026 - 14:30

## Tema
Melhorias na arquitetura do código, modelação do domínio e boas práticas de desenvolvimento.

## Resumo da Reunião
Durante esta reunião foram analisadas as decisões de implementação realizadas na semana anterior, com foco na qualidade da arquitetura do código e na modelação do domínio.

O orientador destacou a importância de separar claramente as responsabilidades entre camadas, definir tipos de domínio mais rigorosos e evitar soluções simplistas que possam comprometer a qualidade do sistema a médio prazo.

Foram também discutidas boas práticas relacionadas com tratamento de erros, configuração da aplicação e reutilização de código entre cliente e servidor.

Adicionalmente, foi reforçada a necessidade de validar o sistema de forma incremental, através da implementação de um cliente simples antes de aumentar a complexidade do sistema.

## Decisões Tomadas

### Organização e Domínio
- Introduzir uma melhor organização de packages, incluindo uma camada de **domain**.
- Definir tipos de domínio com validação no momento da criação (ex: `init {}` em Kotlin), evitando validações posteriores.
- Criar tipos específicos para conceitos importantes (ex: `Password`, `Username`) em vez de usar strings diretamente.

### DTOs e Modelos
- Utilizar tipos agnósticos nos DTOs (ex: string, int).
- Evitar tipos específicos como `Instant` nos DTOs.
- Fazer a conversão para tipos de domínio apenas no backend.

### Tratamento de Erros
- Centralizar exceções numa camada partilhada (`shared`).
- Evitar uso excessivo de exceções genéricas.
- Utilizar exceções padrão (ex: `IllegalArgumentException`) quando apropriado.
- Criar uma função para mapear exceções para status codes HTTP.
- Simplificar mensagens de erro e evitar mensagens alternativas desnecessárias.

### Configuração da Aplicação
- Corrigir a configuração da aplicação.
- Utilizar ficheiros de configuração (ex: `.yaml`) para:
  - port
  - plugins
  - settings gerais
- Evitar configuração hardcoded em Kotlin.

### Reutilização e Consistência
- Criar constantes partilhadas (ex: paths de endpoints).
- Evitar repetição de strings entre cliente e servidor.
- Definir um “dicionário de endpoints” para garantir consistência.

### Desenvolvimento Incremental
- Criar um cliente simples para testar o sistema atual.
- Validar funcionalidades mínimas antes de escalar o sistema.
- Começar já a integrar frontend (mesmo que básico).

### UI / UX Considerations
- Pensar no layout desde início.
- Garantir compatibilidade entre:
  - web
  - desktop
  - mobile (Android / iOS)
- Possibilidade de priorizar algumas plataformas, mantendo compatibilidade geral.

## Próximos Passos / Responsáveis

### Refactor do Backend
- Introduzir camada `domain` com tipos fortes.
- Criar value objects:
  - Password
  - Username
- Aplicar validações no construtor dos modelos.

### Error Handling
- Implementar mapeamento de exceções para HTTP status codes.
- Centralizar tratamento de erros.

### Configuração
- Migrar configuração para ficheiro `.yaml`.
- Remover configurações hardcoded.

### API Consistency
- Criar constantes para endpoints.
- Garantir consistência entre cliente e servidor.

### Cliente de Teste
- Implementar cliente simples (Compose Multiplatform).
- Testar:
  - registo de utilizador
  - login
  - listagem de endpoints

## Observações / Lembretes
- A qualidade do domínio e da arquitetura do código será determinante para a evolução do projeto.
- A abordagem incremental deve ser mantida: validar primeiro funcionalidades mínimas antes de adicionar complexidade.
- Estas decisões alinham o projeto com boas práticas de software e facilitam futuras extensões (base de dados, agentes locais, etc.).