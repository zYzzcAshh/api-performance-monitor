# Relatório da Reunião 3

**Data / Hora:** 17/03/2026

## Tema
Validação da arquitetura e definição da estratégia de implementação inicial.

## Resumo da Reunião
Durante esta reunião com o orientador foi apresentado o trabalho realizado na semana anterior, nomeadamente a definição da arquitetura do sistema, modelo de dados, design da API e estrutura dos monitoring workers.

Com base nesta proposta inicial, foram discutidas alterações tecnológicas e estratégias de desenvolvimento com o objetivo de reduzir a complexidade inicial do projeto e acelerar a implementação de um protótipo funcional.

O orientador sugeriu uma abordagem mais incremental, começando por uma versão simplificada do sistema, sem dependência de frontend ou base de dados, permitindo validar rapidamente as funcionalidades principais.

## Decisões Tomadas
- Substituir o uso de **Spring Boot** por **Ktor** no backend.
- Substituir o frontend em React por **Compose Multiplatform** (a considerar mais tarde).
- Utilizar a biblioteca **Exposed** para acesso à base de dados (a explorar posteriormente).
- Começar por implementar apenas as **funcionalidades mínimas do sistema (MVP)**.
- Não desenvolver frontend nesta fase inicial.
- Não utilizar base de dados para já, optando por armazenamento **in-memory**.
- Utilizar DTOs com tipos simples (ex: strings, inteiros) de forma a manter a API independente da tecnologia do cliente.

## Próximos Passos / Responsáveis

### Backend Setup (Ktor)
- @Francisco & @Martim ⭢ Criar projeto backend com Ktor.
- @Francisco & @Martim ⭢ Configurar servidor HTTP básico.
- @Francisco & @Martim ⭢ Definir estrutura do projeto (routes, services, models).

### API Base (In-Memory)
- @Francisco & @Martim ⭢ Implementar endpoints básicos de autenticação:
  - POST /auth/register
  - POST /auth/login
- @Francisco & @Martim ⭢ Criar estrutura para gestão de endpoints monitorizados.
- @Francisco & @Martim ⭢ Armazenar dados em memória.

### Monitoring Prototype
- @Francisco & @Martim ⭢ Implementar lógica de monitoring:
  - execução de HTTP requests
  - medição de latência
  - captura de status code
- @Francisco & @Martim ⭢ Definir estrutura de métricas.

### Worker (Simplified)
- @Francisco & @Martim ⭢ Criar worker básico.
- @Francisco & @Martim ⭢ Implementar execução periódica (scheduler simples).
- @Francisco & @Martim ⭢ Enviar ou registar métricas.

### Technical Decisions & Documentation
- @Francisco & @Martim ⭢ Atualizar documentação do projeto com:
  - mudança para Ktor
  - abordagem in-memory (por agora)

## Observações / Lembretes
- A abordagem definida privilegia uma implementação incremental, começando por um protótipo funcional antes de introduzir complexidade adicional (base de dados, frontend).
- Esta estratégia permite validar rapidamente as decisões de arquitetura e reduzir riscos técnicos iniciais.
- A próxima reunião deverá avaliar o progresso da implementação do backend e do sistema de monitorização.