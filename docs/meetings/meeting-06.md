# Relatório da Reunião 6

**Data / Hora:** 21/04/2026 - 17:00 (online | Teams)

## Tema
Avaliação de progresso, preparação da entrega intermédia e definição de próximos passos técnicos (cliente, agente local e documentação da API).

## Resumo da Reunião
Durante esta reunião foi apresentado o progresso atual do projeto, com demonstração da aplicação a correr em ambiente desktop e validação do sistema de navegação com Compose Multiplatform.

O orientador reforçou a importância de demonstrar progresso visível para a entrega intermédia (apresentação de progresso), nomeadamente através de funcionalidades implementadas, documentação e organização do trabalho (issues no GitHub).

Foram discutidos aspetos relacionados com a navegação do cliente, consistência da arquitetura, organização da configuração do sistema e melhorias na definição do local monitoring agent.

Adicionalmente, foi introduzida a necessidade de formalizar a API através de uma especificação OpenAPI, de forma a facilitar testes, documentação e evolução do sistema.

## Decisões Tomadas

### Progresso e Organização
- Garantir evidência de progresso para a entrega intermédia:
  - aplicação funcional
  - demonstração
  - documentação
- Criar issues no GitHub para funcionalidades principais (“features grandes”), garantindo rastreabilidade do trabalho.

### Cliente e Navegação
- Validar utilização de **Compose Navigation (Navigation 3)**.
- Criar:
  - diagrama de navegação
  - diagrama de estados (cada estado corresponde a um ecrã)
- Garantir separação correta:
  - composables não devem conter lógica de ações (separação UI / lógica)
- Testar aplicação em múltiplas plataformas:
  - desktop (já validado)
  - web
  - Android (a testar)

### Configuração e Sistema
- Uniformizar portas do sistema (ex: 8080 / 8081).
- Corrigir inconsistências na configuração do backend.

### Local Monitoring Agent
- Refinar o papel do agente local:

  Ideia atual:
  - agente executa checks localmente
  - comunica com o backend
  - associado a um utilizador

- Sugestões do orientador:
  - periodicidade dos testes deve ser definida no backend (não no agente)
  - agente deve atuar como intermediário (execução local apenas)
  - responsabilidade do agente:
    - acesso a APIs privadas
    - preservação de dados sensíveis

- Clarificação do objetivo:
  - permitir monitorização de APIs privadas sem exposição externa
  - agente executa dentro da rede/local do utilizador
  - backend controla configuração e lógica

### API e Documentação
- Introduzir especificação **OpenAPI** para a API do backend.
- Utilizar ferramentas do Ktor / IntelliJ para gerar documentação automaticamente.
- Objetivo:
  - facilitar testes
  - garantir consistência
  - documentar rigorosamente a API

### Código e Estrutura
- Rever dependências e organização dos módulos:
  - evitar duplicação em `build.gradle`
  - mover código partilhado para módulo `shared` quando apropriado

## Próximos Passos / Responsáveis

### Progresso e Entrega
- Preparar slides para apresentação de progresso.
- Garantir demonstração funcional do sistema.

### Cliente e UI
- Criar diagrama de navegação.
- Criar diagrama de estados.
- Testar aplicação em:
  - web
  - Android
- Ajustar arquitetura de composables (separação lógica/UI).

### Local Monitoring Agent
- Refinar arquitetura do agente:
  - definir responsabilidades claras
  - garantir controlo pelo backend
- Documentar fluxo:
  - backend ⭢ agente ⭢ API monitorizada ⭢ backend

### API & Documentação
- Criar especificação OpenAPI.
- Integrar documentação automática no projeto.

### Código e Estrutura
- Rever dependências (`build.gradle`).
- Consolidar código no módulo `shared`.

## Observações / Lembretes
- A entrega de progresso está agendada para **27 de abril de 2026**, sendo essencial demonstrar trabalho funcional e evolução consistente do projeto.
- A documentação da API (OpenAPI) será fundamental para garantir qualidade e facilitar testes.
- O local monitoring agent é uma componente diferenciadora do projeto e deve ser bem definida conceptualmente antes de avançar para implementação completa.
- A organização do trabalho (issues, documentação, commits) contribui para a avaliação contínua e demonstra maturidade no desenvolvimento do projeto.