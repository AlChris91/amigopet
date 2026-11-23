# Especificação do Projeto

## Perfis de Usuários

[Enumere e faça o detalhamento dos perfis de usuários. Utilize o modelo de tabela abaixo para sintetizá-los.]

## Perfis de Usuários

Os usuários do sistema são divididos em dois perfis principais: **Tutor** e **Clínica Veterinária**. Cada perfil possui diferentes responsabilidades, funcionalidades e necessidades dentro da plataforma.

<table>
<tbody>
<tr align=center>
<th colspan="2">Perfil — Tutor</th>
</tr>
<tr>
<td width="150px"><b>Descrição</b></td>
<td width="600px">Pessoa responsável pelo animal de estimação que utiliza o sistema para realizar seu cadastro, cadastrar e gerenciar seus pets, pesquisar clínicas veterinárias disponíveis e solicitar atendimentos.</td>
</tr>
<tr>
<td><b>Necessidades</b></td>
<td>Cadastrar-se e realizar login; cadastrar, editar e consultar os dados dos pets; pesquisar clínicas veterinárias disponíveis por localização ou raio de distância; consultar os serviços oferecidos; selecionar o serviço desejado e o horário disponível; realizar solicitações de atendimento; acompanhar o status das solicitações; consultar atendimentos futuros e anteriores; acompanhar a posição do pet na fila de atendimento.</td>
</tr>
</tbody>
</table>

<table>
<tbody>
<tr align=center>
<th colspan="2">Perfil — Clínica Veterinária</th>
</tr>
<tr>
<td width="150px"><b>Descrição</b></td>
<td width="600px">Estabelecimento veterinário cadastrado na plataforma, responsável por disponibilizar seus serviços, receber e gerenciar solicitações de atendimento e organizar sua agenda e fila de atendimento.</td>
</tr>
<tr>
<td><b>Necessidades</b></td>
<td>Cadastrar e atualizar os dados da clínica; informar localização, serviços oferecidos e tipos de animais atendidos; definir horários disponíveis; receber solicitações de atendimento; aceitar ou recusar solicitações; gerenciar a agenda evitando sobreposição de horários; consultar os atendimentos agendados; organizar e acompanhar a fila de atendimento; atualizar o status dos atendimentos.</td>
</tr>
</tbody>
</table>



## Histórias de Usuários

[Apresente aqui as histórias de usuários que são relevantes para o projeto da solução.]

> **Link Útil**:
> - [Como escrever boas histórias de usuário](https://medium.com/vertice/como-escrever-boas-users-stories-hist%C3%B3rias-de-usu%C3%A1rios-b29c75043fac)

[Utilize o modelo de tabela abaixo para apresentar as histórias de usuários.]

| **EU COMO... QUEM** | **QUERO/PRECISO... O QUE** | **PARA... PORQUE** |
|---|---|---|
| Tutor | Realizar meu cadastro com nome, e-mail, telefone e endereço. | Poder acessar o sistema e solicitar atendimentos para meus pets. |
| Tutor | Cadastrar um pet informando nome, espécie e porte. | Poder solicitar o atendimento adequado para cada animal. |
| Tutor | Pesquisar clínicas em um raio de 5, 10, 15 ou 20 km da minha casa. | Encontrar clínicas de acordo com a distância que desejo percorrer. |
| Tutor | Visualizar os serviços oferecidos pelas clínicas. | Escolher uma clínica que realize o serviço de que meu pet precisa. |
| Tutor | Solicitar um atendimento para um pet, escolhendo serviço, clínica, data e horário disponíveis. | Agendar o atendimento do meu animal. |
| Tutor | Acompanhar o status da minha solicitação. | Saber se a clínica aceitou ou recusou o atendimento. |
| Tutor | Visualizar a posição do meu pet na fila de atendimento. | Acompanhar quando o atendimento do meu pet estiver se aproximando. |
| Tutor | Consultar atendimentos futuros e passados. | Ter acesso ao histórico e aos próximos atendimentos dos meus pets. |
| Tutor | Cadastrar e editar meus pets. | Manter os dados dos meus animais atualizados. |
| Clínica Veterinária | Cadastrar minha clínica com nome, CEP, endereço, telefone e e-mail. | Disponibilizar meus dados para os tutores. |
| Clínica Veterinária | Informar os tipos de animais e serviços que atendo. | Receber solicitações compatíveis com a estrutura da clínica. |
| Clínica Veterinária | Aceitar ou recusar uma solicitação de atendimento. | Controlar quais atendimentos serão realizados pela clínica. |
| Clínica Veterinária | Gerenciar minha agenda sem permitir sobreposição de horários. | Evitar conflitos entre os atendimentos agendados. |

## Requisitos do Projeto

[Com base nas Histórias de Usuários, enumere os requisitos da solução. Lembre-se que cada requisito deve corresponder a uma, e somente uma, característica alvo da solução. Além disso, certifique-se de que todos os aspectos capturados nas Histórias de Usuário foram cobertos.]

### Requisitos Funcionais

[Utilize o modelo de tabela abaixo para apresentar os requisitos funcionais]

| **ID** | **Descrição** | **Prioridade** |
|---|---|---|
| RF-01 | O sistema deve permitir o cadastro do tutor com nome, e-mail, telefone, endereço e senha. | Alta |
| RF-02 | O sistema deve permitir que o tutor realize login para acessar seu perfil. | Alta |
| RF-03 | O sistema deve permitir que o tutor cadastre um ou mais pets. | Alta |
| RF-04 | O sistema deve permitir informar, para cada pet, nome, espécie e porte. | Alta |
| RF-05 | O sistema deve permitir ao tutor editar os dados de seus pets. | Média |
| RF-06 | O sistema deve permitir pesquisar clínicas por raio de 5, 10, 15 ou 20 km a partir da residência do tutor. | Alta |
| RF-07 | O sistema deve apresentar os serviços e os tipos de animais atendidos por cada clínica cadastrada. | Alta |
| RF-08 | O sistema deve permitir ao tutor solicitar atendimento para um pet, informando clínica, serviço, data e horário. | Alta |
| RF-09 | O sistema deve permitir à clínica visualizar as solicitações de atendimento recebidas. | Alta |
| RF-10 | O sistema deve permitir à clínica aceitar ou recusar uma solicitação de atendimento. | Alta |
| RF-11 | O sistema deve permitir ao tutor visualizar o status de seus atendimentos. | Alta |
| RF-12 | O sistema deve permitir ao tutor visualizar atendimentos futuros e passados de seus pets. | Média |
| RF-13 | O sistema deve permitir ao tutor visualizar a posição de seu pet na fila de atendimento da clínica. | Alta |
| RF-14 | O sistema deve permitir o cadastro da clínica com nome, CEP, endereço, telefone e e-mail. | Alta |
| RF-15 | O sistema deve permitir à clínica informar os tipos de animais atendidos e os serviços oferecidos. | Alta |
| RF-16 | O sistema deve disponibilizar uma agenda para controle dos horários de atendimento da clínica. | Alta |
| RF-17 | O sistema deve impedir a criação de atendimentos com sobreposição de horários na agenda da clínica. | Alta |


**Prioridade: Alta / Média / Baixa. 

### Requisitos não Funcionais

[Utilize o modelo de tabela abaixo para apresentar os requisitos não-funcionais]

| **ID** | **Descrição** | **Prioridade** |
|---|---|---|
| RNF-01 | A interface deve apresentar informações de forma clara para os dois perfis de usuário: tutor e clínica. | Média |
| RNF-02 | O sistema deve manter separadas as funcionalidades destinadas ao tutor e à clínica conforme o perfil de acesso. | Alta |
| RNF-03 | A posição na fila deve ser atualizada de forma consistente com o estado dos atendimentos. | Alta |
| RNF-04 | O controle de agenda deve garantir a consistência dos horários, impedindo sobreposição de atendimentos. | Alta |

**Prioridade: Alta / Média / Baixa. 

