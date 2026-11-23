import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../../components/Header/Header';
import MapaClinicas from '../../components/Mapa/MapaClinicas';
import styles from './Listagem.module.css';

function Listagem() {
  const navigate = useNavigate();
  const [clinicas, setClinicas] = useState([]);
  const [clinicasOriginal, setClinicasOriginal] = useState([]);
  const [indexAtual, setIndexAtual] = useState(0);
  const [infoSolicitacao, setInfoSolicitacao] = useState({});

  useEffect(() => {
    const dadosSalvos = localStorage.getItem('resultado_busca');
    const solicitacaoSalva = localStorage.getItem('dados_solicitacao');
    
    if (solicitacaoSalva) {
      setInfoSolicitacao(JSON.parse(solicitacaoSalva));
    }

    if (dadosSalvos) {
      const listaConvertida = JSON.parse(dadosSalvos);
      setClinicasOriginal(listaConvertida);

      const listaFormatada = listaConvertida.map(c => ({
        id: c.id,
        nome: c.nomeFantasia,
        distancia: "Aprox. " + c.cep,
        horarios: c.telefone
      }));
      setClinicas(listaFormatada);
    }
  }, []);

  const handleRecusar = () => {
    setIndexAtual(prevIndex => prevIndex + 1);
  };

  const handleSelecionar = (clinica) => {
    localStorage.setItem('clinica_selecionada', JSON.stringify(clinica));
    navigate('/resumo');
  };

  const temClinicaDisponivel = clinicas.length > 0 && indexAtual < clinicas.length;

  return (
    <div className={styles.container}>
      <Header titulo="Clínicas Elegíveis" mostrarVoltar={true} />
      <div className={styles.content}>
        
        {clinicasOriginal.length > 0 && <MapaClinicas clinicas={clinicasOriginal} />}

        {temClinicaDisponivel ? (
          <div className={styles.card}>
            <span className={styles.distanciaBadge}>{clinicas[indexAtual].distancia}</span>
            <h2 className={styles.clinicaNome}>{clinicas[indexAtual].nome}</h2>
            
            <div className={styles.infoGroup}>
              <span className={styles.label}>Procedimento Solicitado</span>
              <p className={styles.value} style={{ color: '#1a237e', fontWeight: 'bold' }}>
                {infoSolicitacao.servico} ({infoSolicitacao.especie})
              </p>
            </div>

            {/* VALOR EXIBIDO DO BANCO DE DADOS DINAMICAMENTE */}
            <div className={styles.infoGroup}>
              <span className={styles.label}>Valor do Serviço</span>
              <p className={styles.value} style={{ color: '#4CAF50', fontWeight: 'bold', fontSize: '18px' }}>
                R$ {infoSolicitacao.preco ? parseFloat(infoSolicitacao.preco).toFixed(2) : '0.00'}
              </p>
            </div>

            <div className={styles.infoGroup}>
              <span className={styles.label}>Contato / Telefone</span>
              <p className={styles.value}>{clinicas[indexAtual].horarios}</p>
            </div>

            <div className={styles.buttonGroup}>
              <button className={styles.btnRecusar} onClick={handleRecusar}>✖ Recusar</button>
              <button className={styles.btnSelecionar} onClick={() => handleSelecionar(clinicasOriginal[indexAtual])}>✔ Selecionar</button>
            </div>
          </div>
        ) : (
          <div className={styles.semClinicas}>
            <h3>Nenhuma clínica restante.</h3>
            <p>Tente mudar os filtros de busca.</p>
          </div>
        )}
      </div>
    </div>
  );
}

export default Listagem;
