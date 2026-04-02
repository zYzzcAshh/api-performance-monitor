# Relatório da Reunião 5

**Data / Hora:** 02/04/2026 - 10:00 (online | teams)

## Tema
Refinamento do domínio, validação de arquitetura e planeamento da camada de cliente.

## Resumo da Reunião
Durante esta reunião foram analisadas melhorias ao nível da modelação do domínio, organização das camadas da aplicação e consistência entre cliente e servidor.

O orientador reforçou a importância de garantir que todas as validações são feitas nas camadas corretas (principalmente nos boundaries do sistema, como routes), assegurando que os serviços e repositórios trabalham apenas com objetos de domínio válidos.

Foram também discutidas boas práticas relacionadas com reutilização de código, definição de tipos de domínio, organização de constantes e configuração do sistema.

Adicionalmente, foi validado o mini cliente desenvolvido e discutida a próxima fase de desenvolvimento, focada na navegação e estrutura de UI com Compose Multiplatform.

## Decisões Tomadas

### Modelação do Domínio
- Criar um diagrama de domínio em **PlantUML**.
- Atualizar o modelo de domínio:
  - substituir `userId: UInt` por `user: User` no `MonitoredEndpoint`.
- Garantir que os tipos de domínio são usados desde as camadas superiores (cliente e serviços).
- Criar tipos específicos (value objects) para evitar uso direto de tipos primitivos.

### Validação e Camadas
- Validar dados nas **routes** (boundary do sistema).
- Garantir que os serviços recebem sempre dados já validados.
- Garantir que repositórios trabalham apenas com objetos de domínio.
- Evitar validações fora das camadas apropriadas.

### DTOs vs Domínio
- DTOs continuam a usar tipos agnósticos (string, int, etc.).
- Conversão DTO → Domain feita apenas no backend.
- Cliente deve passar a usar também tipos de domínio.

### Organização e Código
- Remover lógica indevida dos serviços (ex: criação de utilizador default).
- Mover inicializações (ex: admin user) para o repositório (in-memory).
- Criar ficheiros específicos para conceitos importantes (ex: `Password.kt`).
- Melhorar consistência e qualidade do código (“tipos mais fortes e expressivos”).

### Erros e Status Codes
- Simplificar tratamento de erros.
- Criar função central para mapear exceções → HTTP status codes.
- Evitar mensagens alternativas desnecessárias.
- Utilizar exceções padrão quando apropriado.

### Configuração
- Melhorar configuração da aplicação.
- Utilizar ficheiro `.yaml` para:
  - port
  - plugins
  - configurações gerais

### Consistência e Reutilização
- Criar constantes para endpoints (evitar strings hardcoded).
- Definir um “dicionário de endpoints”.
- Evitar duplicação entre cliente e servidor.

### Cliente e Navegação
- Validar mini cliente existente (estado atual considerado correto).
- Não implementar navegação manualmente.
- Utilizar sistema de navegação oficial (Compose Navigation).
- Começar a estruturar ecrãs e navegação.

## Próximos Passos / Responsáveis

### Domínio
- Criar diagrama de domínio (PlantUML).
- Atualizar modelos:
  - substituir `userId` por `User`
- Criar value objects:
  - Username
  - Password

### Backend Refactor
- Garantir validação nas routes.
- Ajustar services para usar apenas domínio.
- Mover inicializações para repositório.

### API e Consistência
- Criar constantes de endpoints.
- Uniformizar tipos de retorno (ex: `Result<T>`).

### Cliente
- Adaptar cliente para usar tipos de domínio.
- Garantir consistência entre cliente e backend.

### UI / Navegação (até 14/04/2026)
- Criar diagrama de navegação de ecrãs.
- Criar diagrama de estados (cada estado = ecrã).
- Implementar navegação com Compose Navigation.
- Implementar fluxo básico:
  - login
  - listagem de endpoints monitorizados

## Observações / Lembretes
- A validação deve ocorrer sempre nas camadas externas (routes), garantindo que o domínio permanece consistente.
- A utilização de tipos fortes (value objects) melhora a qualidade e robustez do sistema.
- O desenvolvimento deve continuar de forma incremental, validando cada camada antes de avançar.
- A próxima fase do projeto foca-se na consolidação do cliente e na integração com o backend.