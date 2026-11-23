import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../../components/Header/Header';
import api from '../../services/api';
import styles from './Resumo.module.css';

function Resumo() {
  const navigate = useNavigate();
  const [clinica, setClinica] = useState({});
  const [solicitacao, setSolicitacao] = useState({});

  useEffect(() => {
    const clinicaSalva = localStorage.getItem('clinica_selecionada');
    const solicitacaoSalva = localStorage.getItem('dados_solicitacao');

    if (clinicaSalva) setClinica(JSON.parse(clinicaSalva));
    if (solicitacaoSalva) setSolicitacao(JSON.parse(solicitacaoSalva));
  }, []);

  const handleConfirmarFila = async () => {
    try {
      const tutorId = localStorage.getItem('tutor_id') || 1;
      
      // Cria o registro transacional real de atendimento no MySQL
      const response = await api.post('/atendimentos', {
        tutorId: parseInt(tutorId),
        clinicaId: clinica.id,
        especieAnimal: solicitacao.especie,
        porteAnimal: solicitacao.porte,
        servicoNome: solicitacao.servico
      });

      // Armazena o ID do atendimento gerado pelo banco para a Fila monitorar
      localStorage.setItem('atendimento_id_ativo', response.data.id);
      
      alert("Entrada na Fila Virtual confirmada com sucesso!");
      navigate('/fila');
    } catch (error) {
      alert("Falha ao processar entrada na fila com o servidor.");
    }
  };

  return (
    <div className={styles.container}>
      <Header titulo="Confirmação" mostrarVoltar={true} />
      <div className={styles.content}>
        <div className={styles.resumoCard}>
          <h2 className={styles.titulo}>Resumo da Solicitação</h2>
          
          <div className={styles.metaInfo}>
            <p>Clínica Escolhida: <strong>{clinica.nomeFantasia}</strong></p>
            <p>Serviço: <strong>{solicitacao.servico}</strong></p>
            <p>Preço Final: <strong style={{ color: '#4CAF50' }}>R$ {solicitacao.preco ? parseFloat(solicitacao.preco).toFixed(2) : '0.00'}</strong></p>
          </div>

          {/* ORIENTAÇÃO EM TEMPO REAL EXTRAÍDA DIRETAMENTE DO MYSQL */}
          <div className={styles.orientacoesBox}>
            <h3>📋 Orientações de Pré-Atendimento da Clínica</h3>
            <p className={styles.textoOrientacao}>
              {solicitacao.orientacaoPreAtendimento || "Nenhuma orientação específica cadastrada para este procedimento."}
            </p>
          </div>

          <button onClick={handleConfirmarFila} className={styles.btnConfirmar}>
            🤝 Entrar na Fila Virtual Real
          </button>
        </div>
      </div>
    </div>
  );
}

export default Resumo;
