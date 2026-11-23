import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../../components/Header/Header';
import api from '../../services/api';
import styles from './PainelAdm.module.css';

function PainelAdm() {
  const navigate = useNavigate();
  const [filaPets, setFilaPets] = useState([]);
  const [abaAtual, setAbaAtual] = useState('FILA'); // 'FILA' ou 'HISTORICO'
  const clinicaIdReal = localStorage.getItem('clinica_id');

  useEffect(() => {
    if (!clinicaIdReal) {
      alert("Acesso negado! Esta área é restrita para administradores de clínicas.");
      navigate('/login-adm');
    }
  }, [clinicaIdReal, navigate]);

  const carregarFilaReal = async () => {
    try {
      const response = await api.get(`/atendimentos/clinica/${clinicaIdReal}`);
      setFilaPets(response.data);
    } catch (error) {
      console.log("Erro ao buscar a fila do banco.");
    }
  };

  useEffect(() => {
    if (clinicaIdReal) {
      carregarFilaReal();
      const intervalo = setInterval(carregarFilaReal, 5000);
      return () => clearInterval(intervalo);
    }
  }, [clinicaIdReal]);

  if (!clinicaIdReal) return null;

  // Função unificada para mudar o status do atendimento no MySQL
  const handleAlterarStatus = async (id, novoStatus, mensagemAlerta) => {
    try {
      await api.put(`/atendimentos/${id}/status`, { status: novoStatus });
      alert(mensagemAlerta);
      carregarFilaReal();
    } catch (error) {
      alert("Falha ao atualizar o status do atendimento no servidor.");
    }
  };

  return (
    <div className={styles.container}>
      <Header titulo="Painel da Clínica (ADM)" mostrarVoltar={false} />
      
      <div className={styles.content}>
        <div className={styles.dashboardHeader}>
          <h3>Painel Operacional Real</h3>
          <p>Pacientes na fila: <strong>{filaPets.length} aguardando</strong></p>
        </div>

        {/* Botões rápidos de gestão da clínica */}
        <div className={styles.abasContainer}>
          <button 
            className={`${styles.abaBtn} ${abaAtual === 'FILA' ? styles.abaAtiva : ''}`}
            onClick={() => setAbaAtual('FILA')}
          >
            📋 Fila de Espera ({filaPets.length})
          </button>
          <button 
            className={styles.abaBtn}
            onClick={() => navigate('/configurar-servicos')}
          >
            🛠️ Gerenciar Serviços
          </button>
        </div>

        <div className={styles.filaLista}>
          {filaPets.length === 0 ? (
            <p style={{ textAlign: 'center', color: '#666', marginTop: '20px' }}>
              Nenhum paciente aguardando atendimento no momento.
            </p>
          ) : (
            filaPets.map((pet, index) => (
              <div key={pet.id} className={styles.petCard}>
                <div className={styles.petInfo}>
                  <span className={styles.badgePosicao}>{index + 1}° Lugar</span>
                  <h4 className={styles.petNome}>Paciente #{pet.id} — {pet.especieAnimal}</h4>
                  <p className={styles.detalhesAtendimento}>Porte: <strong>{pet.porteAnimal}</strong></p>
                  <span className={styles.statusBadge}>Status: {pet.statusAtendimento || 'AGUARDANDO'}</span>
                </div>

                {/* Grupo de Ações Operacionais Reais */}
                <div className={styles.acoesGrupo}>
                  <button 
                    className={styles.btnChamar} 
                    onClick={() => handleAlterarStatus(pet.id, "CHAMADO", "Paciente chamado para o consultório!")}
                  >
                    🔔 Chamar
                  </button>

                  <button 
                    className={styles.btnConcluir} 
                    onClick={() => handleAlterarStatus(pet.id, "CONCLUIDO", "Atendimento finalizado e removido da fila!")}
                  >
                    ✅ Concluir
                  </button>

                  <button 
                    className={styles.btnRecusar} 
                    onClick={() => {
                      if (window.confirm("Deseja realmente cancelar/recusar este atendimento?")) {
                        handleAlterarStatus(pet.id, "RECUSADO", "Atendimento recusado/cancelado.");
                      }
                    }}
                  >
                    ✖ Recusar
                  </button>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}

export default PainelAdm;
