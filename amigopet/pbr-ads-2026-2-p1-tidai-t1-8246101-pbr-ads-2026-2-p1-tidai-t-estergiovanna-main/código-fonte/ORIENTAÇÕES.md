# 🐾 AmigoPet - Trabalho Interdisciplinar

Instruções para compilação, inicialização e execução do ecossistema de gerenciamento de filas virtuais veterinárias.

## 🗄️ 1. Banco de Dados (MySQL)
Certifique-se de que o serviço do MySQL local está ativo e execute os comandos abaixo para criar a infraestrutura básica:

```sql
CREATE DATABASE IF NOT EXISTS db_amigopet;
USE db_amigopet;

-- 1. Tabela de Tutores (Clientes do Aplicativo)
CREATE TABLE IF NOT EXISTS tutores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    cep VARCHAR(10) NOT NULL,
    senha VARCHAR(255) NOT NULL
);

-- 2. Tabela de Clínicas (Administradores do Painel)
CREATE TABLE IF NOT EXISTS clinicas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_fantasia VARCHAR(100) NOT NULL,
    email_adm VARCHAR(100) NOT NULL UNIQUE,
    senha_adm VARCHAR(255) NOT NULL,
    cep VARCHAR(10) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    latitude DOUBLE,
    longitude DOUBLE,
    especie_atendida VARCHAR(50) DEFAULT 'Ambos'
);

-- 3. Tabela de Serviços oferecidos pelas Clínicas
CREATE TABLE IF NOT EXISTS servicos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    clinica_id BIGINT NOT NULL,
    nome_servico VARCHAR(100) NOT NULL,
    preco DECIMAL(10, 2) NOT NULL,
    especie_atendida VARCHAR(100),
    orientacao_pre_atendimento TEXT,
    CONSTRAINT fk_servico_clinica FOREIGN KEY (clinica_id) REFERENCES clinicas(id) ON DELETE CASCADE
);

-- 4. Tabela de Orientações Pré-Atendimento (Compatibilidade com repositório legado)
CREATE TABLE IF NOT EXISTS orientacoes_servico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    servico_id BIGINT NOT NULL,
    descricao_orientacao VARCHAR(255) NOT NULL,
    CONSTRAINT fk_orientacao_servico FOREIGN KEY (servico_id) REFERENCES servicos(id) ON DELETE CASCADE
);

-- 5. Tabela de Registro de Atendimentos (Fila Virtual em Tempo Real)
CREATE TABLE IF NOT EXISTS atendimentos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tutor_id BIGINT NOT NULL,
    clinica_id BIGINT NOT NULL,
    servico_id BIGINT NOT NULL,
    especie_animal VARCHAR(100) NOT NULL,
    porte_animal VARCHAR(30) NOT NULL,
    status_atendimento VARCHAR(30) DEFAULT 'BUSCANDO',
    posicao_fila INT DEFAULT 0,
    tempo_estimado_minutos INT DEFAULT 0,
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_atendimento_tutor FOREIGN KEY (tutor_id) REFERENCES tutores(id),
    CONSTRAINT fk_atendimento_clinica FOREIGN KEY (clinica_id) REFERENCES clinicas(id),
    CONSTRAINT fk_atendimento_servico FOREIGN KEY (servico_id) REFERENCES servicos(id)
);

-- 2. POPULAÇÃO DE DADOS INICIAIS 
-- Senha padrão de acesso administrativo de todas as clínicas: AmigoPet@2026

USE db_amigopet;

-- 1. Limpa os dados antigos com segurança
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE atendimentos;
TRUNCATE TABLE servicos;
TRUNCATE TABLE clinicas;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO clinicas (id, nome_fantasia, email_adm, senha_adm, cep, endereco, telefone, latitude, longitude, especie_atendida) VALUES
(1, 'Hospital Veterinário Vet Popular BH', 'adm.vetpopular@email.com', 'AmigoPet@2026', '31270-010', 'Av. Antônio Carlos, 6627 - Pampulha, Belo Horizonte - MG', '(31) 3499-2222', -19.869123, -43.963456, 'Cães e Gatos'),
(2, 'Clínica Veterinária Santo Agostinho', 'adm.santoagostinho@email.com', 'AmigoPet@2026', '30190-060', 'Rua Araguari, 1345 - Santo Agostinho, Belo Horizonte - MG', '(31) 3291-5555', -19.929845, -43.948123, 'Gato'),
(3, 'Centro Veterinário Prado', 'adm.vetprado@email.com', 'AmigoPet@2026', '30411-000', 'Av. Amazonas, 2560 - Prado, Belo Horizonte - MG', '(31) 3335-4444', -19.932412, -43.958789, 'Cão'),
(4, 'Clínica Vet Barreiro & Região', 'adm.barreiro@email.com', 'AmigoPet@2026', '30640-000', 'Av. Sinfrônio Brochado, 850 - Barreiro, Belo Horizonte - MG', '(31) 3384-1111', -19.975610, -44.016320, 'Cães e Gatos'),
(5, 'PetCare Bairro das Indústrias', 'adm.industrias@email.com', 'AmigoPet@2026', '30610-010', 'Av. Olinto Meireles, 1890 - Indústrias I, Belo Horizonte - MG', '(31) 3322-8899', -19.968450, -44.004120, 'Cães e Gatos'),
(6, 'Hospital Felino Savassi', 'adm.savassi@email.com', 'AmigoPet@2026', '30112-010', 'Rua Pernambuco, 1020 - Savassi, Belo Horizonte - MG', '(31) 3261-7700', -19.936810, -43.934520, 'Gato'),
(7, 'Clínica Veterinária Buritis 24h', 'adm.buritis@email.com', 'AmigoPet@2026', '30575-180', 'Av. Professor Mário Werneck, 1450 - Buritis, Belo Horizonte - MG', '(31) 3378-9090', -19.966920, -43.966410, 'Cães e Gatos'),
(8, 'Centro Médico Animal Santa Tereza', 'adm.santatereza@email.com', 'AmigoPet@2026', '31010-190', 'Rua Mármore, 412 - Santa Tereza, Belo Horizonte - MG', '(31) 3461-3030', -19.915430, -43.917850, 'Cães e Gatos'),
(9, 'Clínica Exóticos & Aves Lourdes', 'adm.lourdes@email.com', 'AmigoPet@2026', '30170-010', 'Rua Curitiba, 1780 - Lourdes, Belo Horizonte - MG', '(31) 3292-4545', -19.931200, -43.941900, 'Ave e Coelho'),
(10, 'Saúde Pet Eldorado / Cidade Industrial', 'adm.eldorado@email.com', 'AmigoPet@2026', '32310-010', 'Av. João César de Oliveira, 2400 - Eldorado, Contagem - MG', '(31) 3395-6060', -19.942150, -44.036800, 'Cão'),
(11, 'Hospital Veterinário Venda Nova', 'adm.vendanova@email.com', 'AmigoPet@2026', '31515-000', 'Av. Vilarinho, 1300 - Venda Nova, Belo Horizonte - MG', '(31) 3451-2020', -19.815230, -43.954120, 'Cães e Gatos'),
(12, 'Clínica Vet Castelo & Manacás', 'adm.castelo@email.com', 'AmigoPet@2026', '30840-300', 'Av. dos Engenheiros, 890 - Castelo, Belo Horizonte - MG', '(31) 3474-5500', -19.882410, -43.998540, 'Cães e Gatos'),
(13, 'Centro Médico Felino Serra', 'adm.serra@email.com', 'AmigoPet@2026', '30220-000', 'Rua do Ouro, 1120 - Serra, Belo Horizonte - MG', '(31) 3225-8811', -19.941250, -43.921430, 'Gato'),
(14, 'PetSaúde Sion & Anchieta', 'adm.sion@email.com', 'AmigoPet@2026', '30315-000', 'Av. Uruguai, 640 - Sion, Belo Horizonte - MG', '(31) 3281-4433', -19.951820, -43.930110, 'Cão'),
(15, 'Clínica Veterinária Gutierrez', 'adm.gutierrez@email.com', 'AmigoPet@2026', '30441-050', 'Rua André Cavalcanti, 310 - Gutierrez, Belo Horizonte - MG', '(31) 3332-9900', -19.938910, -43.962150, 'Cães e Gatos'),
(16, 'Vet Popular Padre Eustáquio', 'adm.padreeustaquio@email.com', 'AmigoPet@2026', '30720-000', 'Rua Padre Eustáquio, 2150 - Padre Eustáquio, Belo Horizonte - MG', '(31) 3462-1212', -19.918120, -43.978450, 'Cães e Gatos'),
(17, 'Clínica Animal Bairro Ouro Preto', 'adm.ouropreto@email.com', 'AmigoPet@2026', '31310-000', 'Rua Conceição do Mato Dentro, 450 - Ouro Preto, Belo Horizonte - MG', '(31) 3498-7766', -19.875420, -43.981230, 'Cão'),
(18, 'Centro Veterinário Vila da Serra', 'adm.novalima@email.com', 'AmigoPet@2026', '34006-056', 'Alameda Oscar Niemeyer, 980 - Vila da Serra, Nova Lima - MG', '(31) 3581-3344', -19.978540, -43.946120, 'Cães e Gatos'),
(19, 'Clínica Exóticos & Roedores Floresta', 'adm.floresta@email.com', 'AmigoPet@2026', '30150-270', 'Av. Assis Chateaubriand, 520 - Floresta, Belo Horizonte - MG', '(31) 3213-6767', -19.918950, -43.928410, 'Ave e Hamster'),
(20, 'Hospital Pet Betim Centro', 'adm.betim@email.com', 'AmigoPet@2026', '32600-000', 'Av. Governador Valadares, 710 - Centro, Betim - MG', '(31) 3532-4040', -19.967820, -44.198450, 'Cães e Gatos'),
(21, 'Hospital Veterinário Cidade Nova', 'adm.cidadenova@email.com', 'AmigoPet@2026', '31170-000', 'Av. Cristiano Machado, 2150 - Cidade Nova, Belo Horizonte - MG', '(31) 3484-1010', -19.893120, -43.927840, 'Cães e Gatos'),
(22, 'Clínica Vet Santa Efigênia', 'adm.santaefigenia@email.com', 'AmigoPet@2026', '30150-240', 'Av. Brasil, 480 - Santa Efigênia, Belo Horizonte - MG', '(31) 3241-5050', -19.924510, -43.921890, 'Cães e Gatos'),
(23, 'Centro Médico Animal Mangabeiras', 'adm.mangabeiras@email.com', 'AmigoPet@2026', '30210-300', 'Av. Bandeirantes, 1520 - Mangabeiras, Belo Horizonte - MG', '(31) 3221-7070', -19.952340, -43.914520, 'Cão'),
(24, 'Hospital Felino Belvedere', 'adm.belvedere@email.com', 'AmigoPet@2026', '30320-400', 'Av. Luiz Paulo Franco, 600 - Belvedere, Belo Horizonte - MG', '(31) 3286-8080', -19.974120, -43.944210, 'Gato'),
(25, 'Vet Popular Carlos Prates', 'adm.carlosprates@email.com', 'AmigoPet@2026', '30710-000', 'Av. Pedro II, 1450 - Carlos Prates, Belo Horizonte - MG', '(31) 3412-3030', -19.914230, -43.961450, 'Cães e Gatos'),
(26, 'Clínica Veterinária Caiçara', 'adm.caicara@email.com', 'AmigoPet@2026', '30770-000', 'Av. Presidente Carlos Luz, 1890 - Caiçara, Belo Horizonte - MG', '(31) 3415-9090', -19.896540, -43.968120, 'Cães e Gatos'),
(27, 'PetCare Santa Amélia & Pampulha', 'adm.santaamelia@email.com', 'AmigoPet@2026', '31560-000', 'Av. Guarapari, 720 - Santa Amélia, Belo Horizonte - MG', '(31) 3443-2211', -19.841230, -43.972450, 'Cão'),
(28, 'Centro Veterinário Alípio de Melo', 'adm.alipiodemelo@email.com', 'AmigoPet@2026', '30840-000', 'Av. Abílio Machado, 1980 - Alípio de Melo, Belo Horizonte - MG', '(31) 3471-6655', -19.894120, -44.004120, 'Cães e Gatos'),
(29, 'Clínica Vet Tirol & Barreiro de Cima', 'adm.tirol@email.com', 'AmigoPet@2026', '30662-000', 'Av. Waldyr Soeiro Emrich, 1100 - Tirol, Belo Horizonte - MG', '(31) 3385-4422', -19.988450, -44.022150, 'Cães e Gatos'),
(30, 'Saúde Animal Ibirité Centro', 'adm.ibirite@email.com', 'AmigoPet@2026', '32400-000', 'Av. São Paulo, 540 - Centro, Ibirité - MG', '(31) 3533-7788', -20.021450, -44.058910, 'Cães e Gatos');

INSERT INTO servicos (clinica_id, nome_servico, preco, especie_atendida, orientacao_pre_atendimento) VALUES
(1, 'Castração', 380.00, 'Cão', 'Jejum alimentar de 12 horas e hídrico de 8 horas. Trazer colar elizabetano.'),
(1, 'Consulta Geral', 120.00, 'Cão', 'Chegar com 15 minutos de antecedência e trazer cartão de vacinas.'),
(1, 'Consulta Geral', 120.00, 'Gato', 'Transportar o felino em caixa rígida fechada para evitar fuga na recepção.'),
(1, 'Exames Laboratoriais', 95.00, 'Cão', 'Para exame de sangue, recomenda-se jejum alimentar de 6 horas.'),
(2, 'Consulta Especializada Felina', 160.00, 'Gato', 'Transportar o gato obrigatoriamente dentro de caixa de transporte fechada e coberta com pano.'),
(2, 'Vacinação Quádrupla Felina', 110.00, 'Gato', 'O animal não pode estar febril ou tomando antibióticos nos últimos 7 dias.'),
(2, 'Castração', 340.00, 'Gato', 'Jejum de 8 horas de ração e 4 horas de água. Trazer manta térmica ou cobertor.'),
(3, 'Castração', 350.00, 'Cão', 'Necessário exame de hemograma recente (até 15 dias) e jejum de 10 horas.'),
(3, 'Ultrassom Abdominal', 180.00, 'Cão', 'Evitar que o cão urine 2 horas antes do exame para manter a bexiga cheia.'),
(3, 'Ortopedia Veterinária', 190.00, 'Cão', 'Trazer exames de raio-X anteriores, caso possua.'),
(4, 'Vacinação V10 Importada', 95.00, 'Cão', 'Trazer carteira de vacinação anterior. Filhotes devem estar vermifugados.'),
(4, 'Consulta Geral', 100.00, 'Cão', 'Cães de grande porte devem utilizar coleira e focinheira na recepção.'),
(4, 'Consulta Geral', 100.00, 'Gato', 'Manter o gato na caixa de transporte durante toda a espera.'),
(4, 'Limpeza de Tártaro', 420.00, 'Cão', 'Obrigatório avaliação cardiológica prévia e jejum total de 12 horas.'),
(5, 'Consulta Geral', 90.00, 'Cão', 'Atendimento por ordem de chegada na fila virtual. Trazer documento do tutor.'),
(5, 'Vacinação Antirrábica', 60.00, 'Cão', 'Animal deve ter mais de 4 meses de idade e estar saudável.'),
(5, 'Vacinação Antirrábica', 60.00, 'Gato', 'O animal deve estar vermifugado e sem sinais de gripe felina.'),
(6, 'Dermatologia Felina', 175.00, 'Gato', 'Não aplicar pomadas ou dar banho no gato nas 48 horas anteriores à consulta.'),
(6, 'Consulta Especializada Felina', 165.00, 'Gato', 'Clínica Cat-Friendly sem cães no ambiente. Manter o pet na caixa até entrar no consultório.'),
(7, 'Atendimento de Urgência', 220.00, 'Cão', 'Prioridade imediata. Em caso de ingestão de substância tóxica, trazer a embalagem.'),
(7, 'Atendimento de Urgência', 220.00, 'Gato', 'Atendimento emergencial imediato. Trazer histórico médico se houver.'),
(7, 'Raio-X Digital', 140.00, 'Cão', 'Tutor não deve estar gestante para acompanhar o posicionamento na sala de exame.'),
(8, 'Consulta Geral', 110.00, 'Cão', 'Chegar 10 minutos antes para triagem de peso e temperatura.'),
(8, 'Vacinação V10 Importada', 105.00, 'Cão', 'Aguardar 15 minutos na clínica após a aplicação para observação de reação alérgica.'),
(9, 'Consulta de Aves e Silvestres', 180.00, 'Ave', 'Trazer a ave na própria gaiola sem limpar o fundo para avaliação das fezes.'),
(9, 'Corte de Unhas e Bico', 70.00, 'Ave', 'Transportar a ave em caixa ventilada e escura para reduzir o estresse.'),
(9, 'Consulta de Roedores e Coelhos', 175.00, 'Coelho', 'NÃO fazer jejum antes da consulta. Trazer feno ou a ração habitual.'),
(10, 'Consulta Geral', 95.00, 'Cão', 'Uso obrigatório de guia curta nas dependências da clínica.'),
(10, 'Castração', 330.00, 'Cão', 'Jejum absoluto de 10 horas. Retorno pós-operatório gratuito em 7 dias.'),
(11, 'Consulta Geral', 95.00, 'Cão', 'Chegar com 10 minutos de antecedência com documento do tutor.'),
(11, 'Castração', 320.00, 'Gato', 'Jejum de 8 horas de alimentos sólidos e 4 horas de líquidos.'),
(12, 'Cardiologia Veterinária', 210.00, 'Cão', 'Evitar exercícios físicos intensos com o pet no dia do exame.'),
(12, 'Vacinação V10 Importada', 110.00, 'Cão', 'Trazer cartão de vacinação anterior. Animal deve estar sem febre.'),
(13, 'Ultrassom Abdominal', 175.00, 'Gato', 'Não deixar o gato usar a caixa de areia 1 hora antes do exame.'),
(13, 'Limpeza de Tártaro', 390.00, 'Gato', 'Obrigatório exame pré-anestésico de sangue e jejum de 8 horas.'),
(14, 'Fisioterapia Veterinária', 130.00, 'Cão', 'Trazer laudo ortopédico ou exames de imagem recentes.'),
(14, 'Consulta Geral', 140.00, 'Cão', 'Uso obrigatório de guia e coleira na sala de espera.'),
(15, 'Oftalmologia Veterinária', 185.00, 'Cão', 'Não pingar nenhum colírio no animal no dia da consulta.'),
(15, 'Oftalmologia Veterinária', 185.00, 'Gato', 'Não limpar secreções oculares 2 horas antes da avaliação.'),
(16, 'Consulta Geral', 90.00, 'Cão', 'Atendimento por ordem de chegada na fila virtual.'),
(16, 'Exames Laboratoriais', 85.00, 'Gato', 'Jejum alimentar de 6 horas para perfil bioquímico.'),
(17, 'Castração', 360.00, 'Cão', 'Trazer roupa cirúrgica ou colar elizabetano no tamanho adequado.'),
(17, 'Consulta Geral', 115.00, 'Cão', 'Trazer amostras de fezes caso a queixa seja gastrointestinal.'),
(18, 'Atendimento de Urgência', 250.00, 'Cão', 'Equipe de plantão 24h. Avisar na recepção em caso de sangramento ou convulsão.'),
(18, 'Raio-X Digital', 150.00, 'Gato', 'Manter o felino em caixa de transporte até o momento da radiografia.'),
(19, 'Consulta de Roedores e Coelhos', 170.00, 'Hamster', 'Trazer o animal em caixa ventilada com serragem limpa.'),
(19, 'Consulta de Aves e Silvestres', 175.00, 'Ave', 'Trazer histórico alimentar detalhado e foto do viveiro.'),
(20, 'Castração', 310.00, 'Cão', 'Jejum absoluto de 10 horas. Retorno para retirada de pontos incluso.'),
(20, 'Vacinação Quádrupla Felina', 100.00, 'Gato', 'Manter o gato na caixa de transporte durante toda a permanência na recepção.'),
(21, 'Atendimento de Urgência', 230.00, 'Gato', 'Plantão 24h. Trazer exames anteriores e receitas de medicamentos em uso.'),
(21, 'Consulta Geral', 115.00, 'Cão', 'Chegar 10 minutos antes para cadastro na recepção.'),
(22, 'Acupuntura Veterinária', 150.00, 'Cão', 'Sessão de 40 minutos. O animal não deve estar em jejum.'),
(22, 'Consulta Geral', 110.00, 'Gato', 'Gatos devem vir obrigatoriamente em caixa de transporte.'),
(23, 'Endoscopia Veterinária', 450.00, 'Cão', 'Jejum alimentar rigoroso de 12 horas e hídrico de 6 horas.'),
(23, 'Ortopedia Veterinária', 200.00, 'Cão', 'Trazer histórico de claudicação ou vídeos do animal caminhando.'),
(24, 'Consulta Especializada Felina', 180.00, 'Gato', 'Ambiente exclusivo para felinos com feromônios calmantes.'),
(24, 'Castração', 370.00, 'Gato', 'Jejum de 8 horas de ração. Trazer manta com o cheiro de casa.'),
(25, 'Vacinação Antirrábica', 60.00, 'Cão', 'Animal deve estar vermifugado e sem sinais de gripe ou diarreia.'),
(25, 'Castração', 330.00, 'Gato', 'Retorno após 10 dias para retirada de pontos sem custo adicional.'),
(26, 'Nutrição Clínica Veterinária', 145.00, 'Cão', 'Trazer foto do rótulo da ração atual e lista de petiscos oferecidos.'),
(26, 'Exames Laboratoriais', 90.00, 'Gato', 'Jejum de 6 horas para coleta de sangue e perfil renal.'),
(27, 'Vacinação V10 Importada', 105.00, 'Cão', 'Apresentar carteira de vacinação de filhote para controle das doses.'),
(27, 'Limpeza de Tártaro', 410.00, 'Cão', 'Necessário eletrocardiograma e hemograma realizados nos últimos 15 dias.'),
(28, 'Consulta Geral', 95.00, 'Cão', 'Atendimento ágil via fila virtual. Não há necessidade de impressão de comprovante.'),
(28, 'Ultrassom Abdominal', 165.00, 'Cão', 'Realizar tricotomia (raspagem do pelo da barriga) no local antes do exame.'),
(29, 'Consulta Geral', 90.00, 'Gato', 'Atendimento por ordem de chegada na fila. Trazer documento com foto do tutor.'),
(29, 'Vacinação Quádrupla Felina', 100.00, 'Gato', 'Gatos devem vir obrigatoriamente em caixas rígidas e travadas.'),
(30, 'Castração', 300.00, 'Cão', 'Jejum de 10 horas. Obrigatório o uso de roupa cirúrgica pós-operatória.'),
(30, 'Consulta Geral', 85.00, 'Cão', 'Chegar com 15 minutos de antecedência para triagem inicial.');


```

## 🚀 2. Servidor Back-end (Spring Boot)
1. Certifique-se de que o **JDK 17** está instalado e configurado na variável de ambiente `JAVA_HOME`.
2. Abra o arquivo `api/src/main/resources/application.properties`.
3. Valide as credenciais de acesso `spring.datasource.username` e `spring.datasource.password` conforme seu ambiente local.
4. Navegue até a pasta da API e inicie o servidor:
```bash
cd api
mvn clean spring-boot:run
```
O servidor estará ativo assim que exibir a mensagem indicando a porta `8081`.

> A API escuta na porta **8081** (`server.port=8081`), a mesma endereço configurado no front-end em `amigopet-frontend/src/services/api.js`.

### ⚡ Modo demonstração (sem MySQL na máquina)
Para apresentar o projeto em um computador que **não tem MySQL instalado**, existe o perfil `demo`: ele sobe a API com o H2 em memória já populado com os dados de exemplo (as cinco clínicas de Belo Horizonte, seus serviços, as orientações pré-atendimento, um tutor e dois atendimentos na fila).

```bash
cd api
mvn spring-boot:run -Dspring-boot.run.profiles=demo
```

**Credenciais prontas para a apresentação:**

| Perfil | E-mail | Senha |
|---|---|---|
| Tutor | `tutor@amigopet.com` | `AmigoPet@2026` |
| Clínica (ADM) | `adm.vetpopular@email.com` | `AmigoPet@2026` |

> Os dados ficam apenas na memória: a cada reinício a demonstração volta ao estado inicial. Use o perfil `demo` na apresentação e o perfil padrão (MySQL) no dia a dia.

### 📡 Endpoints disponíveis

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/tutores/cadastro` | Cadastra um tutor |
| POST | `/api/tutores/login` | Autentica o tutor |
| PUT | `/api/tutores/{id}` | Atualiza o perfil do tutor |
| GET | `/api/tutores/{id}` | Consulta os dados do tutor |
| POST | `/api/clinicas/cadastro` | Cadastra uma clínica parceira |
| POST | `/api/clinicas/login` | Autentica o administrador da clínica |
| PUT | `/api/clinicas/{id}` | Atualiza o perfil da clínica |
| GET | `/api/clinicas/busca?cep=&servico=&raio=` | Busca as clínicas elegíveis por serviço e distância |
| GET | `/api/clinicas/{id}/servicos` | Lista os serviços de uma clínica |
| GET | `/api/servicos` | Lista todos os serviços cadastrados |
| POST | `/api/servicos` | Cadastra um serviço para uma clínica |
| DELETE | `/api/servicos/{id}` | Remove um serviço e suas orientações |
| GET | `/api/servicos/{id}/orientacoes` | Lista as orientações pré-atendimento de um serviço |
| POST | `/api/servicos/orientacoes` | Cadastra uma orientação pré-atendimento |
| GET | `/api/servicos/clinica/{id}/orientacoes` | Lista as orientações de todos os serviços de uma clínica |
| POST | `/api/atendimentos` | Registra a entrada do tutor na fila virtual |
| GET | `/api/atendimentos/clinica/{clinicaId}` | Lista a fila de atendimento de uma clínica |
| GET | `/api/atendimentos/{id}` | Consulta a situação de um atendimento |
| PUT | `/api/atendimentos/{id}/status` | Altera o status de um atendimento |

**Parâmetros da busca de clínicas:** `cep` (CEP do tutor), `servico` (categoria desejada, como `Castração`, `Vacinação`, `Consulta` ou `Exames`), `raio` (distância máxima em quilômetros) e, opcionalmente, `latitude` e `longitude` exatas do tutor. As clínicas são devolvidas ordenadas da mais próxima para a mais distante, com o campo `distancia` em quilômetros.

**Status da fila:** `BUSCANDO`, `CHAMADO`, `ATENDIDO`, `CANCELADO` e `FINALIZADO`.

### ✅ Testes automatizados
A suíte de testes roda com o banco H2 em memória, sem precisar de MySQL instalado:
```bash
cd api
mvn test
```

## 💻 3. Interface Front-end (React + Vite)
1. Abra um novo terminal na pasta do front-end.
2. Instale as dependências estruturais e inicie o ecossistema:
```bash
cd amigopet-frontend
npm install
npm run dev
```
Clique no link local gerado `http://localhost:5173/` para interagir com a aplicação.
