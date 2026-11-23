import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../../components/Header/Header';
import api from '../../services/api';
import styles from './Busca.module.css';

function Busca() {
  const navigate = useNavigate();
  const tutorIdReal = localStorage.getItem('tutor_id');
  
  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [cep, setCep] = useState('');
  const [especie, setEspecie] = useState('');
  const [porte, setPorte] = useState('Médio');
  const [servico, setServico] = useState('');
  const [raio, setRaio] = useState('10');
  
  const [servicosDoBanco, setServicosDoBanco] = useState([]);
  const [listaServicosVisiveis, setListaServicosVisiveis] = useState([]);
  const [listaEspeciesVisiveis, setListaEspeciesVisiveis] = useState([]);

  useEffect(() => {
    if (!tutorIdReal) {
      alert("Sessão expirada ou acesso inválido. Por favor, faça login.");
      navigate('/');
    } else {
      carregarDadosDoBanco();
    }
  }, [tutorIdReal, navigate]);

  const carregarDadosDoBanco = async () => {
    try {
      const resServicos = await api.get('/clinicas/servicos');
      if (resServicos.data && resServicos.data.length > 0) {
        setServicosDoBanco(resServicos.data);

        // 1. Lista todos os tipos de procedimentos únicos
        const nomesUnicos = [...new Set(resServicos.data.map(s => s.nomeServico).filter(Boolean))];
        setListaServicosVisiveis(nomesUnicos);

        if (nomesUnicos.length > 0) {
          const primeiroServico = nomesUnicos[0];
          setServico(primeiroServico);
          
          // 2. Filtra na hora os animais que atendem APENAS àquele primeiro serviço
          const especiesFiltradas = [...new Set(
            resServicos.data
              .filter(s => s.nomeServico === primeiroServico)
              .map(s => s.especieAtendida)
              .filter(Boolean)
          )];
          setListaEspeciesVisiveis(especiesFiltradas);
          if (especiesFiltradas.length > 0) setEspecie(especiesFiltradas[0]);
        }
      }
    } catch (error) {
      console.error("Erro na comunicação com a API.");
      setListaEspeciesVisiveis(["Cão", "Gato"]);
      setListaServicosVisiveis(["Consulta Geral"]);
    }
  };

  // REGRA DE NEGÓCIO: Sempre que mudar o serviço, atualiza dinamicamente as espécies vinculadas a ele
  const handleMudarServico = (nomeSelecionado) => {
    setServico(nomeSelecionado);
    
    const especiesFiltradas = [...new Set(
      servicosDoBanco
        .filter(s => s.nomeServico === nomeSelecionado)
        .map(s => s.especieAtendida)
        .filter(Boolean)
    )];
    
    setListaEspeciesVisiveis(especiesFiltradas);
    if (especiesFiltradas.length > 0) {
      setEspecie(especiesFiltradas[0]);
    } else {
      setEspecie('');
    }
  };

    const handleBuscar = async (e) => {
    e.preventDefault();
    try {
      // ENVIANDO O FILTRO COMPLETO: cep, servico e especie para o Spring Boot
      const response = await api.get('/clinicas/busca', {
        params: { cep, servico, especie, raio } // Certifique-se de incluir 'especie' aqui!
      });

      const infoServicoObj = servicosDoBanco.find(
        s => s.nomeServico === servico && s.especieAtendida === especie
      ) || servicosDoBanco.find(s => s.nomeServico === servico);

      if (response.data.length > 0) {
        localStorage.setItem('resultado_busca', JSON.stringify(response.data));
        localStorage.setItem('dados_solicitacao', JSON.stringify({ 
          especie, 
          porte, 
          servico,
          preco: infoServicoObj ? infoServicoObj.preco : "0.00",
          orientacaoPreAtendimento: infoServicoObj ? infoServicoObj.orientacaoPreAtendimento : "Sem orientações específicas."
        }));
        navigate('/clinicas');
      } else {
        alert("Nenhuma clínica encontrada para os filtros selecionados.");
      }
    } catch (error) {
      alert("Erro ao conectar com o servidor do AmigoPet.");
    }
  };


  return (
    <div className={styles.container}>
      <Header titulo="AmigoPet" mostrarVoltar={false} />
      <div className={styles.content}>
        <p className={styles.subtitle}>Encontre a clínica ideal para o seu animal</p>
        <form onSubmit={handleBuscar} className={styles.form}>
          
          <fieldset className={styles.fieldset}>
            <legend className={styles.legend}>Dados do Tutor</legend>
            <label className={styles.label}>Nome do Tutor *</label>
            <input type="text" required placeholder="Ex: Maria Silva" value={nome} onChange={(e) => setNome(e.target.value)} className={styles.input} />
            <label className={styles.label}>Email *</label>
            <input type="email" required placeholder="exemplo@email.com" value={email} onChange={(e) => setEmail(e.target.value)} className={styles.input} />
            <label className={styles.label}>CEP *</label>
            <input type="text" required placeholder="Ex: 31270-010" value={cep} onChange={(e) => setCep(e.target.value)} className={styles.input} />
          </fieldset>

          <fieldset className={styles.fieldset}>
            <legend className={styles.legend}>Tipo de Atendimento</legend>
            <select value={servico} onChange={(e) => handleMudarServico(e.target.value)} className={styles.select}>
              {listaServicosVisiveis.map((nomeUnico, idx) => (
                <option key={idx} value={nomeUnico}>{nomeUnico}</option>
              ))}
            </select>
          </fieldset>

          <fieldset className={styles.fieldset}>
            <legend className={styles.legend}>Dados do Animal</legend>
            <div className={styles.row}>
              <div className={styles.col}>
                <label className={styles.label}>Espécie Disponível *</label>
                <select value={especie} onChange={(e) => setEspecie(e.target.value)} className={styles.select}>
                  {listaEspeciesVisiveis.map((esp, idx) => (
                    <option key={idx} value={esp}>{esp}</option>
                  ))}
                </select>
              </div>
              <div className={styles.col}>
                <label className={styles.label}>Porte *</label>
                <select value={porte} onChange={(e) => setPorte(e.target.value)} className={styles.select}>
                  <option value="Pequeno">Pequeno</option>
                  <option value="Médio">Médio</option>
                  <option value="Grande">Grande</option>
                </select>
              </div>
            </div>
          </fieldset>

          <fieldset className={styles.fieldset}>
            <legend className={styles.legend}>Raio de distância</legend>
            <select value={raio} onChange={(e) => setRaio(e.target.value)} className={styles.select}>
              <option value="5">Até 5 km</option>
              <option value="10">Até 10 km</option>
              <option value="15">Até 15 km</option>
              <option value="20">Até 20 km</option>
            </select>
          </fieldset>

          <button type="submit" className={styles.submitButton}>🔍 Encontrar clínica</button>
        </form>
      </div>
    </div>
  );
}

export default Busca;
