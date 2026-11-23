import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import PawMark from '../../components/Brand/PawMark';
import styles from './Login.module.css';

function Login() {
  const navigate = useNavigate();
  const [isCadastro, setIsCadastro] = useState(false);
  
  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [cep, setCep] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      if (isCadastro) {
        await api.post('/tutores/cadastro', { nome, email, cep, senha });
        alert("Cadastro realizado com sucesso! Faça seu login.");
        setIsCadastro(false);
      } else {
        const response = await api.post('/tutores/login', { email, senha });
        
        localStorage.setItem('tutor_id', response.data.id);
        localStorage.setItem('tutor_nome', response.data.nome);
        
        alert(response.data.mensagem);
        navigate('/busca');
      }
    } catch (error) {
      const mensagemErro = error.response?.data?.mensagem || "Falha na comunicação com o servidor.";
      alert(mensagemErro);
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
          <p className={styles.subtitulo}>
            {isCadastro ? "Crie sua conta de tutor" : "Acesse sua conta para cuidar do seu pet"}
          </p>
        </div>

        <form onSubmit={handleSubmit} className={styles.form}>
          {isCadastro && (
            <div className={styles.inputGroup}>
              <label className={styles.label}>Nome Completo *</label>
              <input type="text" required placeholder="Ex: Maria Silva" value={nome} onChange={(e) => setNome(e.target.value)} className={styles.input} />
            </div>
          )}

          <div className={styles.inputGroup}>
            <label className={styles.label}>E-mail *</label>
            <input type="email" required placeholder="seuemail@exemplo.com" value={email} onChange={(e) => setEmail(e.target.value)} className={styles.input} />
          </div>

          {isCadastro && (
            <div className={styles.inputGroup}>
              <label className={styles.label}>CEP Residencial *</label>
              <input type="text" required placeholder="Ex: 30123-450" value={cep} onChange={(e) => setCep(e.target.value)} className={styles.input} />
            </div>
          )}

          <div className={styles.inputGroup}>
            <label className={styles.label}>Senha *</label>
            <input type="password" required placeholder="••••••••" value={senha} onChange={(e) => setSenha(e.target.value)} className={styles.input} />
          </div>

          <button type="submit" className={styles.btnAcao}>
            {isCadastro ? "Finalizar Cadastro" : "Entrar no App"}
          </button>
        </form>

        <div className={styles.alternarContainer}>
          {isCadastro ? (
            <span>Já tem uma conta? <button className={styles.btnLink} onClick={() => setIsCadastro(false)}>Faça Login</button></span>
          ) : (
            <span>Não tem conta? <button className={styles.btnLink} onClick={() => setIsCadastro(true)}>Cadastre-se</button></span>
          )}
          
          <div style={{ marginTop: '20px', borderTop: '1px solid #eee', paddingTop: '15px' }}>
            <span style={{ fontSize: '13px', color: '#666' }}>É uma clínica parceira?</span>
            <button type="button" className={styles.btnLink} onClick={() => navigate('/login-adm')} style={{ marginLeft: '5px' }}>
              Acesse o Portal do ADM →
            </button>
          </div>
        </div>

      </div>
    </div>
  );
}

export default Login;
