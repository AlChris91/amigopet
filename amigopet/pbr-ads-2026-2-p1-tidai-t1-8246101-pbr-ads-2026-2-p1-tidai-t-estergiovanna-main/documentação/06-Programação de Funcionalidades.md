# Programação de Funcionalidades

Implementação da aplicação descrita por meio dos requisitos codificados.

## Cadastro do tutor e do animal

[Cadastro do tutor e do animal]

#### Requisito atendido

- **RF-01:** O sistema deve permitir o cadastro do tutor com nome, e-mail, telefone, endereço e senha.
- **RF-03:** O sistema deve permitir que o tutor cadastre um ou mais pets.
- **RF-04:** O sistema deve permitir informar, para cada pet, nome, espécie e porte.

#### Artefatos da funcionalidade

- Protótipo da tela de cadastro do tutor e do animal apresentado no arquivo `documentação/04-Projeto de Interface.md`.

#### Estrutura de Dados

A funcionalidade prevê o cadastro dos dados do tutor e do animal.

**Dados do tutor:**

- Nome
- E-mail
- Telefone
- Endereço
- Senha

**Dados do animal:**

- Nome
- Espécie
- Porte

Nesta etapa, os dados estão representados no protótipo de interface e ainda não possuem armazenamento implementado no código-fonte.

---

## Busca de clínicas

[Busca de clínicas]

#### Requisito atendido

- **RF-06:** O sistema deve permitir pesquisar clínicas por raio de 5, 10, 15 ou 20 km a partir da residência do tutor.

#### Artefatos da funcionalidade

- Protótipo da tela de busca de clínicas apresentado no arquivo `documentação/04-Projeto de Interface.md`.

#### Estrutura de Dados

A funcionalidade prevê a utilização dos seguintes dados:

- Localização ou endereço do tutor
- Raio de pesquisa selecionado
- Distância entre o tutor e a clínica
- Localização das clínicas cadastradas

Os dados estão representados no protótipo e ainda não possuem integração com banco de dados ou serviço de localização.

---

## Listagem de clínicas elegíveis

[Clínicas elegíveis]

#### Requisito atendido

- **RF-06:** O sistema deve permitir pesquisar clínicas por raio de 5, 10, 15 ou 20 km a partir da residência do tutor.
- **RF-07:** O sistema deve apresentar os serviços e os tipos de animais atendidos por cada clínica cadastrada.

#### Artefatos da funcionalidade

- Protótipo da tela de clínicas elegíveis apresentado no arquivo `documentação/04-Projeto de Interface.md`.

#### Estrutura de Dados

Cada clínica deve possuir as seguintes informações:

- Nome da clínica
- Endereço
- Distância em relação ao tutor
- Serviços oferecidos
- Tipos de animais atendidos
- Horários disponíveis

Nesta etapa, essas informações estão representadas visualmente no protótipo.

---

## Seleção de uma clínica

[Clínica selecionada]
#### Requisito atendido

- **RF-07:** O sistema deve apresentar os serviços e os tipos de animais atendidos por cada clínica cadastrada.
- **RF-08:** O sistema deve permitir ao tutor solicitar atendimento para um pet, informando clínica, serviço, data e horário.

#### Artefatos da funcionalidade

- Protótipo da tela de seleção da clínica apresentado no arquivo `documentação/04-Projeto de Interface.md`.

#### Estrutura de Dados

A solicitação de atendimento deve utilizar os seguintes dados:

- Tutor
- Pet
- Clínica selecionada
- Serviço escolhido
- Data do atendimento
- Horário do atendimento
- Status da solicitação

Os dados ainda estão representados somente no protótipo de interface.

---

## Acompanhamento da fila de atendimento

[Acompanhamento da fila]

#### Requisito atendido

- **RF-13:** O sistema deve permitir ao tutor visualizar a posição de seu pet na fila de atendimento da clínica.
- **RNF-03:** A posição na fila deve ser atualizada de forma consistente com o estado dos atendimentos.

#### Artefatos da funcionalidade

- Protótipo da tela de acompanhamento da fila apresentado no arquivo `documentação/04-Projeto de Interface.md`.

#### Estrutura de Dados

A fila de atendimento deve considerar:

- Nome do tutor
- Nome do pet
- Clínica
- Serviço
- Posição atual na fila
- Status do atendimento
- Horário previsto para atendimento

Nesta etapa, a fila está representada no protótipo e ainda não possui atualização dinâmica implementada.

---

## Próximo atendimento

[Próximo atendimento]

#### Requisito atendido

- **RF-11:** O sistema deve permitir ao tutor visualizar o status de seus atendimentos.
- **RF-12:** O sistema deve permitir ao tutor visualizar atendimentos futuros e passados de seus pets.
- **RF-13:** O sistema deve permitir ao tutor visualizar a posição de seu pet na fila de atendimento da clínica.

#### Artefatos da funcionalidade

- Protótipo da tela de próximo atendimento apresentado no arquivo `documentação/04-Projeto de Interface.md`.

#### Estrutura de Dados

O atendimento deve conter:

- Nome do pet
- Nome da clínica
- Serviço agendado
- Data
- Horário
- Status do atendimento
- Posição na fila, quando aplicável

Os dados estão definidos como parte da proposta da funcionalidade, mas ainda não possuem armazenamento implementado no código-fonte.

---

## Atendimento confirmado

[Atendimento confirmado]

#### Requisito atendido

- **RF-08:** O sistema deve permitir ao tutor solicitar atendimento para um pet, informando clínica, serviço, data e horário.
- **RF-10:** O sistema deve permitir à clínica aceitar ou recusar uma solicitação de atendimento.
- **RF-11:** O sistema deve permitir ao tutor visualizar o status de seus atendimentos.

#### Artefatos da funcionalidade

- Protótipo da tela de atendimento confirmado apresentado no arquivo `documentação/04-Projeto de Interface.md`.

#### Estrutura de Dados

A confirmação do atendimento deve registrar:

- Tutor
- Pet
- Clínica
- Serviço
- Data
- Horário
- Status da solicitação
- Identificação do atendimento

O status esperado para essa etapa é **confirmado**. A funcionalidade ainda está representada por meio do protótipo de interface e não possui implementação no código-fonte.


#### Instruções de acesso

[Adicione as orientações de acesso à funcionalidade]

#### Responsável

Cada integrante do grupo ficará responsável por desenvolver uma parte da aplicação

> **Links Úteis**:
> - [Trabalhando com HTML5 Local Storage e JSON](https://www.devmedia.com.br/trabalhando-com-html5-local-storage-e-json/29045)
> - [JSON Tutorial](https://www.w3resource.com/JSON)
> - [JSON - Introduction (W3Schools)](https://www.w3schools.com/js/js_json_intro.asp)
> - [JSON Tutorial (TutorialsPoint)](https://www.tutorialspoint.com/json/index.htm)

