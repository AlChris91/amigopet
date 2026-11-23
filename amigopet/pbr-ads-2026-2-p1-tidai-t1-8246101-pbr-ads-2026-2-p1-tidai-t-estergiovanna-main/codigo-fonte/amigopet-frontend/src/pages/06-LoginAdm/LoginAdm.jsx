import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import PawMark from '../../components/Brand/PawMark';
import api from '../../services/api';
import styles from './LoginAdm.module.css';

function LoginAdm() {
  const navigate = useNavigate();
  const [isCadastro, setIsCadastro] = useState(false);
  
  const [nomeFantasia, setNomeFantasia] = useState('');
  const [emailAdm, setEmailAdm] = useState('');
  const [senhaAdm, setSenhaAdm] = useState('');
  const [cep, setCep] = useState('');
  const [endereco, setEndereco] = useState('');
  const [telefone, setTelefone] = useState('');

    const handleSubmit = async (e) => {
    e.preventDefault();
    
    // LINHA DE DIAGNÓSTICO: Abre o inspecionar elemento do navegador (F12) e veja se esse texto aparece!
    console.log("Disparando LOGIN DA CLÍNICA para /clinicas/login com:", emailAdm);

    try {
      if (isCadastro) {
        let lat = -19.9247;
        let lon = -43.9351;
        try {
          const geoResponse = await axios.get('https://openstreetmap.org', {
            params: { postalcode: cep, country: 'Brazil', format: 'json' }
          });
          if (geoResponse.data && geoResponse.data.length > 0) {
            lat = parseFloat(geoResponse.data[0].lat);
            lon = parseFloat(geoResponse.data[0].lon);
          }
        } catch (geoError) {
          console.error("Falha ao buscar coordenadas pelo CEP.");
        }

        await api.post('/clinicas/cadastro', {
          nomeFantasia,
          emailAdm,
          senhaAdm,
          cep,
          endereco,
          telefone,
          latitude: lat,
          longitude: lon
        });
        alert("Clínica registrada com sucesso!");
        setIsCadastro(false);
      } else {
        // FORÇANDO O ENDPOINT DA CLÍNICA EXPLICITAMENTE
        const response = await api.post('/clinicas/login', { 
          emailAdm: emailAdm.trim(), 
          senhaAdm: senhaAdm 
        });
        
        localStorage.setItem('clinica_id', response.data.id);
        localStorage.setItem('clinica_nome', response.data.nome);
        
        alert("Acesso autorizado! Bem-vindo ao painel operacional.");
        navigate('/adm');
      }
    } catch (error) {
      const msg = error.response?.data?.mensagem || "E-mail ou senha do administrador inválidos.";
      alert(msg);
    }
  };


  return (
    <div className={styles.container}>
      
      <div className={styles.content}>
        <div className={styles.logoContainer}>
          <div className={styles.brand}>
            <PawMark className={styles.logo} />
            <h2 className={styles.titulo}>Amigo<span>Pet</span></h2>
          </div>
          <span className={styles.badgeAdm}>Painel do ADM</span>
          <p className={styles.subtitulo}>
            {isCadastro ? "Cadastre sua clínica parceira" : "Entre com suas credenciais de administrador"}
          </p>
        </div>

        <form onSubmit={handleSubmit} className={styles.form}>
          {isCadastro && (
            <div className={styles.inputGroup}>
              <label className={styles.label}>Nome Fantasia da Clínica *</label>
              <input type="text" required placeholder="Ex: Hospital Vet Popular" value={nomeFantasia} onChange={(e) => setNomeFantasia(e.target.value)} className={styles.input} />
            </div>
          )}

          <div className={styles.inputGroup}>
            <label className={styles.label}>E-mail do Administrador *</label>
            <input type="email" required placeholder="adm@clinica.com" value={emailAdm} onChange={(e) => setEmailAdm(e.target.value)} className={styles.input} />
          </div>

          <div className={styles.inputGroup}>
            <label className={styles.label}>Senha Corporativa *</label>
            <input type="password" required placeholder="••••••••" value={senhaAdm} onChange={(e) => setSenhaAdm(e.target.value)} className={styles.input} />
          </div>

          {isCadastro && (
            <>
              <div className={styles.inputGroup}>
                <label className={styles.label}>Telefone de Contato *</label>
                <input type="text" required placeholder="(31) 3444-2222" value={telefone} onChange={(e) => setTelefone(e.target.value)} className={styles.input} />
              </div>
              <div className={styles.inputGroup}>
                <label className={styles.label}>CEP Comercial *</label>
                <input type="text" required placeholder="31270-010" value={cep} onChange={(e) => setCep(e.target.value)} className={styles.input} />
              </div>
              <div className={styles.inputGroup}>
                <label className={styles.label}>Endereço Completo *</label>
                <input type="text" required placeholder="Av. Antônio Carlos, 6627 - Pampulha" value={endereco} onChange={(e) => setEndereco(e.target.value)} className={styles.input} />
              </div>
            </>
          )}

          <button type="submit" className={styles.btnAcao}>
            {isCadastro ? "Registrar Clínica" : "Acessar Painel"}
          </button>
        </form>

        <div className={styles.alternarContainer}>
          {isCadastro ? (
            <span>Já possui parceria? <button className={styles.btnLink} onClick={() => setIsCadastro(false)}>Fazer Login ADM</button></span>
          ) : (
            <span>Quer tornar-se parceiro? <button className={styles.btnLink} onClick={() => setIsCadastro(true)}>Cadastrar Clínica</button></span>
          )}
          
          <button className={styles.btnLink} style={{ color: '#666', fontSize: '13px', marginTop: '10px' }} onClick={() => navigate('/')}>
            ← Voltar para o Portal do Tutor
          </button>
        </div>
      </div>
    </div>
  );
}

export default LoginAdm;
