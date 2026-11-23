-- Dados de exemplo do perfil "demo", equivalentes ao script de MySQL do
-- arquivo ORIENTACOES.md. As chaves estrangeiras sao resolvidas por
-- subconsulta para que os identificadores continuem em 1, 2, 3... como no
-- script de MySQL (o front-end usa o servico de identificador 1).

-- 1. Clinicas parceiras de Belo Horizonte, com as coordenadas exatas.
INSERT INTO clinicas (nome_fantasia, email_adm, senha_adm, cep, endereco, telefone, latitude, longitude) VALUES
('Hospital Veterinário Vet Popular BH', 'adm.vetpopular@email.com', 'AmigoPet@2026', '31270-010', 'Av. Antônio Carlos, 6627 - Pampulha, Belo Horizonte - MG', '(31) 3499-2222', -19.869123, -43.963456),
('Clínica Veterinária Santo Agostinho', 'adm.santoagostinho@email.com', 'AmigoPet@2026', '30190-060', 'Rua Araguari, 1345 - Santo Agostinho, Belo Horizonte - MG', '(31) 3291-5555', -19.929845, -43.948123),
('Centro Veterinário Prado', 'adm.vetprado@email.com', 'AmigoPet@2026', '30411-000', 'Av. Amazonas, 2560 - Prado, Belo Horizonte - MG', '(31) 3335-4444', -19.932412, -43.958789),
('Hospital Veterinário UFMG', 'adm.vetufmg@email.com', 'AmigoPet@2026', '31270-901', 'Av. Pres. Antônio Carlos, 6627 - São José, Belo Horizonte - MG', '(31) 3409-2000', -19.863145, -43.971254),
('Ouro Preto Clínica Veterinária', 'adm.ouropreto@email.com', 'AmigoPet@2026', '31310-260', 'Rua Conceição do Mato Dentro, 415 - Ouro Preto, Belo Horizonte - MG', '(31) 3498-1010', -19.860478, -43.990145);

-- 2. Servicos de cada clinica.
INSERT INTO servicos (clinica_id, nome_servico, preco)
SELECT id, 'Castração', 380.00 FROM clinicas WHERE email_adm = 'adm.vetpopular@email.com';
INSERT INTO servicos (clinica_id, nome_servico, preco)
SELECT id, 'Consulta Geral', 120.00 FROM clinicas WHERE email_adm = 'adm.vetpopular@email.com';
INSERT INTO servicos (clinica_id, nome_servico, preco)
SELECT id, 'Vacinação Antirrábica', 75.00 FROM clinicas WHERE email_adm = 'adm.vetpopular@email.com';
INSERT INTO servicos (clinica_id, nome_servico, preco)
SELECT id, 'Consulta Geral', 140.00 FROM clinicas WHERE email_adm = 'adm.santoagostinho@email.com';
INSERT INTO servicos (clinica_id, nome_servico, preco)
SELECT id, 'Exames de Sangue', 110.00 FROM clinicas WHERE email_adm = 'adm.santoagostinho@email.com';
INSERT INTO servicos (clinica_id, nome_servico, preco)
SELECT id, 'Castração', 350.00 FROM clinicas WHERE email_adm = 'adm.vetprado@email.com';
INSERT INTO servicos (clinica_id, nome_servico, preco)
SELECT id, 'Consulta Geral', 100.00 FROM clinicas WHERE email_adm = 'adm.vetprado@email.com';
INSERT INTO servicos (clinica_id, nome_servico, preco)
SELECT id, 'Consulta Especializada', 150.00 FROM clinicas WHERE email_adm = 'adm.vetufmg@email.com';
INSERT INTO servicos (clinica_id, nome_servico, preco)
SELECT id, 'Cirurgia Geral', 850.00 FROM clinicas WHERE email_adm = 'adm.vetufmg@email.com';
INSERT INTO servicos (clinica_id, nome_servico, preco)
SELECT id, 'Vacinação V10', 95.00 FROM clinicas WHERE email_adm = 'adm.ouropreto@email.com';
INSERT INTO servicos (clinica_id, nome_servico, preco)
SELECT id, 'Consulta Geral', 130.00 FROM clinicas WHERE email_adm = 'adm.ouropreto@email.com';

-- 3. Orientacoes pre-atendimento exibidas na confirmacao do agendamento.
INSERT INTO orientacoes_servico (servico_id, descricao_orientacao)
SELECT id, 'Jejum hídrico e alimentar absoluto de 8 horas antes do procedimento.' FROM servicos WHERE nome_servico = 'Castração' AND clinica_id = 1;
INSERT INTO orientacoes_servico (servico_id, descricao_orientacao)
SELECT id, 'Trazer uma manta ou cobertor para o pós-operatório do animal.' FROM servicos WHERE nome_servico = 'Castração' AND clinica_id = 1;
INSERT INTO orientacoes_servico (servico_id, descricao_orientacao)
SELECT id, 'O animal deve estar em jejum alimentar minimo de 4 horas para a coleta.' FROM servicos WHERE nome_servico = 'Exames de Sangue';
INSERT INTO orientacoes_servico (servico_id, descricao_orientacao)
SELECT id, 'Exige-se a apresentação de exames de sangue recentes do pet.' FROM servicos WHERE nome_servico = 'Castração' AND clinica_id = 3;
INSERT INTO orientacoes_servico (servico_id, descricao_orientacao)
SELECT id, 'Internação prévia de 2 horas obrigatoria com documento do tutor.' FROM servicos WHERE nome_servico = 'Cirurgia Geral';

-- 4. Tutor de exemplo, para abrir o aplicativo ja autenticado na
--    apresentação. A senha "AmigoPet@2026" e migrada para BCrypt no
--    primeiro login.
INSERT INTO tutores (nome, email, cep, senha)
VALUES ('Pedro Benigno', 'tutor@amigopet.com', '31270-010', 'AmigoPet@2026');

-- 5. Dois atendimentos ja na fila da primeira clinica, para o painel do
--    administrador ter o que mostrar.
INSERT INTO atendimentos (tutor_id, clinica_id, servico_id, especie_animal, porte_animal, status_atendimento, posicao_fila, tempo_estimado_minutos, data_registro)
SELECT (SELECT id FROM tutores WHERE email = 'tutor@amigopet.com'), 1, 1, 'Cão', 'Médio', 'BUSCANDO', 1, 15, CURRENT_TIMESTAMP;
INSERT INTO atendimentos (tutor_id, clinica_id, servico_id, especie_animal, porte_animal, status_atendimento, posicao_fila, tempo_estimado_minutos, data_registro)
SELECT (SELECT id FROM tutores WHERE email = 'tutor@amigopet.com'), 1, 2, 'Gato', 'Pequeno', 'BUSCANDO', 2, 30, CURRENT_TIMESTAMP;
