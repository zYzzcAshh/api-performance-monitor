# Relatório da Reunião 8

**Data / Hora:** 06/05/2026 - 11:30

## Tema
Refinamento da apresentação de progresso, sistema de notificações e consistência técnica do projeto.

## Resumo da Reunião
Durante esta reunião foram discutidos ajustes relacionados com a apresentação de progresso, melhorias no sistema de notificações e algumas decisões técnicas importantes para a evolução do projeto.

Foi também analisada a lista de possíveis arguentes previamente preparada pelo grupo, tendo o orientador indicado que irá contactar alguns dos nomes sugeridos.

O orientador reforçou ainda a importância de manter consistência tecnológica no projeto, especialmente na escolha de bibliotecas compatíveis e bem mantidas, e chamou a atenção para a necessidade de manter a especificação OpenAPI sempre atualizada.

## Decisões Tomadas

### Avaliação e Arguentes
- Discutir a lista de possíveis arguentes apresentada pelo grupo.
- O orientador irá contactar alguns dos docentes sugeridos.

### Navegação e UI
- Continuar o trabalho relacionado com navegação de ecrãs.
- Garantir documentação visual da navegação entre estados/ecrãs.

### Sistema de Notificações
- Discutido o novo sistema de notificações da plataforma.
- Evitar excesso de especificidade na criação de eventos e regras de alerta.

Exemplo discutido:
- Em vez de:
  - “notificar apenas para status code > 500”
- Preferir:
  - “notificar quando existir erro do servidor”

O orientador sugeriu pensar em cenários mais práticos e úteis para utilizadores reais da plataforma, evitando regras demasiado específicas sem justificação clara.

### Demonstração do Projeto
- Preparar um vídeo demonstrativo da aplicação.
- Evitar demonstrações totalmente live durante apresentações para reduzir risco de falhas técnicas.

### Bibliotecas e Dependências
- Rever bibliotecas utilizadas no projeto.
- Evitar dependências pouco mantidas ou menos fiáveis.
- Garantir maior consistência tecnológica da stack.

### OpenAPI
- Como o backend utiliza Ktor, aproveitar para manter o `openapi.yaml` atualizado com pouco esforço adicional.
- Garantir que a documentação da API acompanha sempre a implementação.

## Próximos Passos / Responsáveis

### Apresentação e Demonstração
- Preparar vídeo demonstrativo da aplicação.
- Melhorar documentação visual da navegação de ecrãs.

### Sistema de Notificações
- Refinar modelo de eventos e alertas.
- Definir condições de alerta mais genéricas e práticas.

### OpenAPI & Documentação
- Atualizar especificação `openapi.yaml`.
- Garantir sincronização entre API implementada e documentação.

### Dependências e Bibliotecas
- Rever bibliotecas do projeto.
- Substituir dependências problemáticas ou pouco compatíveis.

## Observações / Lembretes
- A demonstração da aplicação deve privilegiar estabilidade e previsibilidade, justificando a utilização de vídeo.
- O sistema de notificações deve focar-se em cenários úteis para utilizadores reais e não em regras demasiado específicas ou difíceis de justificar.
- A documentação OpenAPI será importante para testes, manutenção e qualidade geral do projeto.
- A consistência e fiabilidade das bibliotecas utilizadas influencia diretamente a robustez e sustentabilidade do sistema.