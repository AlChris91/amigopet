import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../../components/Header/Header';
import api from '../../services/api';
import styles from './GerenciarServicos.module.css';

function GerenciarServicos() {
  const navigate = useNavigate();
  const clinicaIdReal = localStorage.getItem('clinica_id');

  const [meusServicos, setMeusServicos] = useState([]);
  const [nomeServico, setNomeServico] = useState('');
  const [preco, setPreco] = useState('');
  const [especie, setEspecie] = useState('');
  const [orientacao, setOrientacao] = useState('');

  useEffect(() => {
    if (!clinicaIdReal) {
      alert("Acesso negado.");
      navigate('/login-adm');
    } else {
      carregarServicos();
    }
  }, [clinicaIdReal]);

  const carregarServicos = async () => {
    try {
      const response = await api.get(`/clinicas/${clinicaIdReal}/servicos`);
      setMeusServicos(response.data);
    } catch (error) {
      console.error("Erro ao carregar serviços da clínica.");
    }
  };

  const handleSalvarServico = async (e) => {
    e.preventDefault();
    try {
      // CORREÇÃO CRÍTICA: Passando o preço formatado como string numérica para o BigDecimal aceitar
      await api.post(`/clinicas/${clinicaIdReal}/servicos`, {
        clinicaId: parseInt(clinicaIdReal),
        nomeServico: nomeServico.trim(),
        preco: parseFloat(preco).toFixed(2), 
        especieAtendida: especie.trim(),
        orientacaoPreAtendimento: orientacao.trim()
      });

      alert("Serviço publicado com sucesso no catálogo!");
      setNomeServico('');
      setPreco('');
      setEspecie('');
      setOrientacao('');
      carregarServicos();
    } catch (error) {
      alert("Erro ao cadastrar serviço no servidor. Verifique o console do back-end.");
    }
  };

  if (!clinicaIdReal) return null;

  return (
    <div className={styles.container}>
      <Header titulo="Configurar Serviços" mostrarVoltar={true} />
      
      <div className={styles.content}>
        <div className={styles.cardForm}>
          <h3 className={styles.subtitulo}>Oferecer Novo Serviço</h3>
          <form onSubmit={handleSalvarServico} className={styles.form}>
            
            <div className={styles.inputGroup}>
              <label className={styles.label}>Nome do Serviço / Procedimento *</label>
              <input 
                type="text" 
                required 
                placeholder="Ex: Consulta Geral, Castração, Ultrassom" 
                value={nomeServico} 
                onChange={(e) => setNomeServico(e.target.value)} 
                className={styles.input} 
              />
            </div>

            <div className={styles.inputGroup}>
              <label className={styles.label}>Animais / Espécies Atendidas neste Serviço *</label>
              <input 
                type="text" 
                required 
                placeholder="Ex: Cães, Gatos, Ambos" 
                value={especie} 
                onChange={(e) => setEspecie(e.target.value)} 
                className={styles.input} 
              />
            </div>

            <div className={styles.inputGroup}>
              <label className={styles.label}>Preço Cobrado (R$) *</label>
              <input type="number" step="0.01" required placeholder="Ex: 150.00" value={preco} onChange={(e) => setPreco(e.target.value)} className={styles.input} />
            </div>

            <div className={styles.inputGroup}>
              <label className={styles.label}>Orientações de Pré-Atendimento para o Tutor *</label>
              <textarea required placeholder="Ex: Traga o animal em jejum hídrico de 8 horas." value={orientacao} onChange={(e) => setOrientacao(e.target.value)} className={styles.textarea} />
            </div>

            <button type="submit" className={styles.btnAdicionar}>➕ Publicar Serviço</button>
          </form>
        </div>

        <div className={styles.cardLista}>
          <h3 className={styles.subtitulo}>Seus Serviços no Catálogo</h3>
          {meusServicos.length === 0 ? (
            <p style={{ fontSize: '14px', color: '#666' }}>Nenhum serviço cadastrado ainda.</p>
          ) : (
            meusServicos.map((s) => (
              <div key={s.id} className={styles.servicoItem}>
                <div className={styles.servicoInfo}>
                  <h4>{s.nomeServico}</h4>
                  <p>Foco: <strong>{s.especieAtendida || 'Geral'}</strong></p>
                  {s.orientacaoPreAtendimento && (
                    <p style={{ marginTop: '4px', fontStyle: 'italic', color: '#555' }}>
                      "{s.orientacaoPreAtendimento}"
                    </p>
                  )}
                </div>
                <div className={styles.servicoPreco}>
                  R$ {s.preco ? parseFloat(s.preco).toFixed(2) : '0.00'}
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}

export default GerenciarServicos;
