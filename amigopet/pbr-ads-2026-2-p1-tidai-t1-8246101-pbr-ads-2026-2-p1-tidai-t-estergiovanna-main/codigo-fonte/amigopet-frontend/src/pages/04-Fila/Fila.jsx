import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../../components/Header/Header';
import api from '../../services/api';
import styles from './Fila.module.css';

function Fila() {
  const navigate = useNavigate();
  const [posicao, setPosicao] = useState(1);
  const [tempo, setTempo] = useState(15);
  const [chamado, setChamado] = useState(false);

  useEffect(() => {
    const dadosFila = localStorage.getItem('atendimento_atual');
    if (!dadosFila) return;
    const atual = JSON.parse(dadosFila);

    const aplicar = (a) => {
      setPosicao(a.posicaoFila);
      setTempo(a.tempoEstimadoMinutos);
      setChamado(a.statusAtendimento === 'CHAMADO');
    };
    aplicar(atual);

    // Consulta o servidor a cada 5s para acompanhar a fila andando
    const atualizar = async () => {
      try {
        const response = await api.get(`/atendimentos/${atual.id}`);
        aplicar(response.data);
        localStorage.setItem('atendimento_atual', JSON.stringify(response.data));
      } catch (error) {
        console.log('Erro ao atualizar a fila.');
      }
    };
    atualizar();
    const intervalo = setInterval(atualizar, 5000);
    return () => clearInterval(intervalo);
  }, []);

  return (
    <div className={styles.container}>
      <Header titulo="Fila de Atendimento" mostrarVoltar={false} />
      <div className={styles.content}>
        <div className={styles.statusCard}>
          <p className={styles.statusTexto}>✓ Entrou na Fila Virtual</p>
        </div>
        <div className={styles.filaCard}>
          <h1 className={styles.posicaoNumero}>{chamado ? 'Sua vez!' : `${posicao}°`}</h1>
          <span className={styles.posicaoLabel}>Lugar</span>
        </div>
        <div className={styles.infoPainel}>
          <p className={styles.tempoEstimado}>Tempo estimado: <strong>{tempo} min</strong></p>
        </div>
        <button className={styles.btnCancelar} onClick={() => navigate('/busca')}>Voltar para Início</button>
      </div>
    </div>
  );
}

export default Fila;
