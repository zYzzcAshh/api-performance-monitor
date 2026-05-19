# Relatório da Reunião 10

**Data / Hora:** 19/05/2026 - 17:45  
**Formato:** Online

## Tema
Revisão do progresso semanal, melhoria da estrutura da aplicação, testes e preparação da integração da base de dados.

## Resumo da Reunião

Durante esta reunião foram apresentados os progressos desenvolvidos ao longo da semana, com foco principal na reorganização da API, melhoria da consistência arquitetural e reforço da cobertura de testes.

Foi discutida a importância de continuar a expandir os testes automatizados, incluindo possíveis testes adicionais para o cliente, apesar da maior complexidade associada à componente frontend. Foi considerado que este investimento poderá aumentar significativamente a robustez global do sistema.

Relativamente à interface gráfica, foi sugerida a separação entre os ecrãs de definição/configuração de monitorização e os ecrãs de visualização de resultados de monitorização, permitindo uma navegação mais clara e uma melhor organização funcional da aplicação.

No diagrama PlantUML foi também identificado um detalhe desnecessário relacionado com ações de “voltar”, tendo sido recomendado remover essa indicação por ser implícita no fluxo de navegação.

Foi ainda sugerida a criação de um documento técnico dedicado à justificação das decisões arquiteturais e tecnológicas adotadas no projeto, incluindo a fundamentação da escolha das principais bibliotecas e frameworks utilizadas. Entre os pontos discutidos encontram-se:
- utilização do Exposed em vez de JDBC direto;
- escolha do PostgreSQL;
- utilização do Ktor em vez de alternativas como Spring;
- utilização de JWT para autenticação;
- adoção da biblioteca Password4j;
- utilização de Jakarta Mail para envio de emails;
- utilização de Swagger/OpenAPI;
- explicação da utilização de WASM;
- introdução ao conceito de Kotlin Multiplatform.

Foi recomendado utilizar os ficheiros Gradle como referência para rever dependências utilizadas e identificar redundâncias ou bibliotecas mal posicionadas entre módulos.

Por fim, foram discutidos os próximos passos relacionados com:
- implementação da persistência com base de dados;
- experimentação da aplicação nas diferentes plataformas suportadas;
- reorganização das dependências do projeto;
- adição do docente Paulo Pereira ao repositório GitHub;
- envio de email de boas-vindas e contextualização do projeto.

## Tarefas / Próximos Passos

- Adicionar mais testes automatizados ao cliente;
- Separar screens de configuração e visualização de monitorização;
- Atualizar diagramas PlantUML;
- Criar documento técnico de decisões arquiteturais;
- Rever e limpar dependências Gradle;
- Iniciar integração com PostgreSQL e Exposed;
- Testar aplicação em diferentes plataformas;
- Adicionar Paulo Pereira ao repositório GitHub;
- Preparar email de onboarding/contextualização do projeto.