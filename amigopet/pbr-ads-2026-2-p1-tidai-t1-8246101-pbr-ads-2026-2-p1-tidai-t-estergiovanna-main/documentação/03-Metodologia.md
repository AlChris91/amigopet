# Metodologia

Esta seção descreve a organização da equipe para a execução das tarefas do projeto e as ferramentas utilizadas para a manutenção dos códigos e demais artefatos.

## Gerenciamento de Projeto

A metodologia ágil escolhida para o desenvolvimento deste projeto foi o Scrum, pois, como citam Amaral, Fleury e Isoni (2019, p. 68), seus benefícios são a

“visão clara dos resultados a entregar; ritmo e disciplina necessários à execução; definição de papéis e responsabilidades dos integrantes do projeto (Scrum Owner, Scrum Master e Team); empoderamento dos membros da equipe de projetos para atingir o desafio; conhecimento distribuído e compartilhado de forma colaborativa; ambiência favorável para crítica às ideias e não às pessoas.”

O desenvolvimento do AmigoPet será organizado em Sprints, considerados ciclos curtos de trabalho. No início de cada Sprint, o Product Owner priorizará as tarefas do Product Backlog, e a equipe definirá e distribuirá as atividades que deverão ser realizadas.

Ao final de cada Sprint, a equipe apresentará o resultado desenvolvido e realizará uma retrospectiva para identificar os pontos positivos, as dificuldades encontradas e as melhorias necessárias para os próximos ciclos do projeto.

### Divisão de Papéis

A equipe utiliza o Scrum como base para a definição do processo de desenvolvimento.

- **Scrum Master:** Eduardo Costa
- **Product Owner:** Ester Giovanna
- **Equipe de Desenvolvimento:** Pedro Benigno, Ester Giovanna, Eduardo Costa e Alan Christian.
- **Equipe de Design:** Pedro Benigno, Ester Giovanna, Eduardo Costa e Alan Christian.

Os mesmos integrantes poderão assumir mais de uma função. A responsabilidade por cada tarefa será definida individualmente no início de cada Sprint, garantindo que todos os participantes saibam o que precisam desenvolver, revisar e testar.

> **Links Úteis**:
>
> - [11 Passos Essenciais para Implantar Scrum no seu Projeto](https://mindmaster.com.br/scrum-11-passos/)
> - [Scrum em 9 minutos](https://www.youtube.com/watch?v=XfvQWnRgxG0)

### Processo

A equipe utiliza o GitHub Projects para o acompanhamento do desenvolvimento do projeto. Essa ferramenta será utilizada para registrar o Product Backlog, planejar as Sprints, distribuir as tarefas e apresentar o status de desenvolvimento da solução.

No início de cada Sprint, o Product Owner será responsável por priorizar as tarefas do Product Backlog. Após a priorização, a equipe definirá as atividades que deverão ser realizadas, distribuirá as tarefas entre os integrantes — cada um responsável pela execução — e estabelecerá os critérios de aceitação necessários.

Cada tarefa deverá conter, quando necessário:

- Descrição da atividade e objetivo;
- Critérios de aceitação;
- Responsável pela execução;
- Label correspondente à categoria;
- Dependências com outras tarefas;
- Link do protótipo no Figma;
- Data prevista ou Sprint relacionada;
- Resultados das revisões e dos testes.

O quadro Kanban do projeto apresentar a seguinte estrutura:

- **Backlog:** esta coluna representa o Product Backlog e recebe as tarefas do projeto que serão trabalhadas. Todas as atividades identificadas durante o desenvolvimento também deverão ser incorporadas a essa lista.

- **To Do:** esta coluna representa o Sprint Backlog, ou seja, a lista de tarefas da Sprint atual. As tarefas do To Do serão definidas, priorizadas e distribuídas aos integrantes da equipe no início de cada Sprint.

- **In Progress:** esta coluna representa as tarefas da Sprint que estão em desenvolvimento. Quando um integrante iniciar uma tarefa da coluna To Do, deverá movê-la para esta coluna.

- **Ready to Test:** esta coluna representa as tarefas da Sprint que estão prontas para revisão e testes. Assim que a implementação de uma tarefa for concluída, ela deverá ser movida para esta coluna pelo responsável que finalizou o trabalho.

- **Testing:** esta coluna representa as tarefas que estão em revisão e/ou teste. O integrante responsável por revisar ou testar a tarefa deverá registrar os resultados obtidos na descrição dela. Caso sejam identificadas falhas ou melhorias, a tarefa retornará para a coluna In Progress para que as correções sejam realizadas.

- **Done:** esta coluna representa as tarefas concluídas que atenderam aos critérios de aceitação, passaram por revisão, foram testadas e receberam as melhorias necessárias. Somente as tarefas presentes nesta coluna serão consideradas prontas para entrega.

O status do quadro Kanban será apresentado e atualizado em todas as reuniões com o professor regente. Esse acompanhamento possibilitará a visualização das tarefas concluídas, das tarefas em desenvolvimento e dos impedimentos que precisam ser resolvidos pela equipe.

<img width="1911" height="874" alt="image" src="https://github.com/user-attachments/assets/2a071349-002e-4b1c-a548-eb3982c1680a" />


> **Links Úteis**:
>
> - [Planejamento de projetos para desenvolvedores](https://github.com/features/project-management/)
> - [Sobre Projects](https://docs.github.com/pt/github/managing-your-work-on-github/about-project-boards)
> - [Ferramentas de gerenciamento do Projects no GitHub](https://www.youtube.com/watch?v=RXEy6CFu9Hk)

### Etiquetas

As tarefas são, ainda, etiquetadas em função da natureza da atividade e seguem o seguinte esquema de cores e categorias:

- **Bug:** erros identificados no código ou no funcionamento do sistema.
- **Desenvolvimento (Development):** implementação de novas telas e funcionalidades.
- **Documentação (Documentation):** elaboração e atualização de documentos, diagramas e instruções.
- **Gerência de Projetos (Project Management):** planejamento, organização e acompanhamento das atividades.
- **Infraestrutura (Infrastructure):** configuração do repositório, dos ambientes e da publicação.
- **Testes (Tests):** revisão, validação e execução de testes das funcionalidades.

As cores utilizadas para cada categoria deverão ser mantidas durante todo o desenvolvimento, facilitando a identificação das tarefas no quadro Kanban.

<figure>
  <img src="https://user-images.githubusercontent.com/100447878/164068979-9eed46e1-9b44-461e-ab88-c2388e6767a1.png" alt="Esquema de cores e categorias do projeto" />
  <figcaption>Figura 3 - Tela do esquema de cores e categorias</figcaption>
</figure>

### Ferramentas

Os artefatos do projeto serão desenvolvidos e armazenados em diversas plataformas. O GitHub será utilizado para o armazenamento do código-fonte, o controle de versões e a organização dos documentos. O Figma será utilizado para a elaboração do protótipo e a definição das interfaces, enquanto o GitHub Projects será responsável pelo planejamento e acompanhamento das tarefas. Após a conclusão do desenvolvimento, o projeto será hospedado no GitHub Pages.

O controle de versões será realizado por meio de branches, commits e pull requests. A branch principal receberá apenas versões que já tenham sido implementadas, revisadas e testadas pela equipe.

#### Ambiente de desenvolvimento

O ambiente de trabalho utilizado pela equipe será o Visual Studio Code (VS Code), editor de código gratuito e multiplataforma. Por meio de suas extensões, a equipe terá acesso a ferramentas de depuração, formatação, lintagem e integração com Git, GitHub e Supabase, o que padroniza a escrita do código entre os integrantes e facilita a execução das aplicações localmente.

#### Linguagens e tecnologias

O front-end da aplicação AmigoPet será desenvolvido em **JavaScript**, utilizando o **React** como biblioteca de construção de interfaces. A escolha se deve à sua arquitetura baseada em componentes, que favorece a reutilização de código e a composição modular das telas do sistema. O projeto será estruturado com o Vite, ferramenta de build que provê inicialização rápida do ambiente de desenvolvimento, servidor local para execução da aplicação e geração dos arquivos de produção.

O back-end será desenvolvido em **Java**, com o framework **Spring-Boot**, responsável pela criação das rotas e das regras de negócio da aplicação, além da integração com o banco de dados.

O armazenamento dos dados será realizado no **MySQL**, plataforma que atua como back-end como serviço (Backend as a Service) e fornece banco de dados relacional (PostgreSQL), autenticação de usuários, armazenamento de arquivos e APIs. A escolha permite que a equipe dispense a administração de servidores próprios, mantendo os dados, as regras de acesso e as políticas de segurança (Row Level Security) centralizados em um único ambiente.

A relação dos ambientes utilizados, suas plataformas e suas respectivas finalidades é apresentada na tabela a seguir.

| AMBIENTE | PLATAFORMA | FINALIDADE | LINK DE ACESSO |
|---|---|---|---|
| Repositório de código-fonte | GitHub | Armazenamento do código, controle de versões, criação de branches, issues e pull requests. | http://.... |
| Editor de código | Visual Studio Code | Ambiente de trabalho da equipe para edição, depuração, formatação e integração do código. | https://code.visualstudio.com |
| Banco de dados e serviços de back-end | Supabase | Armazenamento dos dados, autenticação de usuários e disponibilização de APIs. | https://supabase.com |
| Documentos do projeto | GitHub | Organização da metodologia, dos diagramas, das instruções e dos demais documentos. | http://.... |
| Projeto de interface | Figma | Elaboração do protótipo, organização das telas e definição dos componentes visuais. | https://www.figma.com/community/file/1687105378195469922 |
| Gerenciamento do projeto | GitHub Projects | Planejamento das Sprints, acompanhamento das tarefas e manutenção do quadro Kanban. | [ https://github.com/orgs/ICEI-PUC-Minas-PBR-ADS/projects/174 |
| Hospedagem | GitHub Pages | Publicação e disponibilização do sistema para acesso dos usuários. | http://.... |

### Estratégia de Organização de Codificação

Todos os artefatos relacionados à implementação e à visualização dos conteúdos do projeto do site serão inseridos na pasta [codigo-fonte](https://github.com/ICEI-PUC-Minas-PMV-ADS/WebApplicationProject-Template-v2/tree/main/codigo-fonte).

A documentação do projeto, os diagramas e os materiais de planejamento deverão ser organizados em uma pasta separada denominada `documentos`. Dessa forma, os arquivos utilizados para o funcionamento do sistema ficarão separados dos artefatos utilizados apenas na documentação.

A organização do versionamento seguirá as seguintes regras:

- A branch `main` será utilizada para armazenar a versão estável do projeto;
- Novas funcionalidades deverão ser desenvolvidas em branches com o prefixo `feature/`;
- Correções deverão ser desenvolvidas em branches com o prefixo `fix/`;
- Atualizações de documentos deverão ser limitadas a branches com o prefixo `docs/`;
- As alterações não deverão ser enviadas diretamente para a branch `main`;
- Antes de incorporar uma alteração à branch principal, deverá ser criado um pull request;
- O pull request deverá passar por revisão e testes de outro integrante da equipe;
- As mensagens dos commits deverão descrever claramente o motivo da alteração.

A organização do repositório segue a estrutura definida pela equipe, com os arquivos de código em `codigo-fonte` e os documentos em `documentos`
