import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../../components/Header/Header';
import api from '../../services/api';
import styles from './EditarPerfil.module.css';

function EditarPerfil() {
  const navigate = useNavigate();
  
  const tutorId = localStorage.getItem('tutor_id');
  const clinicaId = localStorage.getItem('clinica_id');
  const isAdm = !!clinicaId;

  const [nome, setNome] = useState('');
  const [email, setEmail] = useState(''); // Estado para o e-mail editável
  const [senha, setSenha] = useState('');
  const [cep, setCep] = useState('');
  const [endereco, setEndereco] = useState('');
  const [telefone, setTelefone] = useState('');
  const [especieAtendida, setEspecieAtendida] = useState('Ambos');

  useEffect(() => {
    if (isAdm) {
      setNome(localStorage.getItem('clinica_nome') || '');
      setEmail(localStorage.getItem('clinica_email') || '');
      
      api.get(`/clinicas/${clinicaId}`).then(response => {
        setCep(response.data.cep);
        setEndereco(response.data.endereco);
        setTelefone(response.data.telefone);
        setEspecieAtendida(response.data.especieAtendida || 'Ambos');
        if (response.data.emailAdm) {
          setEmail(response.data.emailAdm);
          localStorage.setItem('clinica_email', response.data.emailAdm);
        }
      });
    } else {
      setNome(localStorage.getItem('tutor_nome') || '');
      setEmail(localStorage.getItem('tutor_email') || '');
      // Caso exista endpoint de busca do tutor por ID:
      api.get(`/tutores/${tutorId}`).then(response => {
        if (response.data.email) {
          setEmail(response.data.email);
          localStorage.setItem('tutor_email', response.data.email);
        }
      }).catch(() => {});
    }
  }, [isAdm, clinicaId, tutorId]);

  const handleSalvar = async (e) => {
    e.preventDefault();
    try {
      if (isAdm) {
        // Envia o e-mail modificado dentro do ClinicaAtualizacaoDTO
        const response = await api.put(`/clinicas/${clinicaId}`, {
          nomeFantasia: nome,
          emailAdm: email.trim().toLowerCase(), // Enviando a alteração para o Java
          telefone,
          cep,
          endereco,
          senhaAdm: senha,
          especieAtendida: especieAtendida
        });
        
        localStorage.setItem('clinica_nome', response.data.nomeFantasia || nome);
        localStorage.setItem('clinica_email', email.trim().toLowerCase());
        alert("Perfil corporativo atualizado com sucesso!");
        navigate('/adm');
      } else {
        // Envia o e-mail modificado para o TutorAtualizacaoDTO
        await api.put(`/tutores/${tutorId}`, { 
          nome, 
          email: email.trim().toLowerCase(), // Enviando a alteração para o Java
          cep, 
          senha 
        });
        
        localStorage.setItem('tutor_nome', nome);
        localStorage.setItem('tutor_email', email.trim().toLowerCase());
        alert("Perfil de tutor atualizado com sucesso!");
        navigate('/busca');
      }
    } catch (error) {
      const msg = error.response?.data?.mensagem || "Erro ao atualizar os dados. Verifique se o e-mail já está em uso.";
      alert(msg);
    }
  };

  return (
    <div className={styles.container}>
      <Header titulo="Editar Perfil" mostrarVoltar={true} />
      
      <div className={styles.content}>
        <form onSubmit={handleSalvar} className={styles.form}>
          
          <div className={styles.inputGroup}>
            <label className={styles.label}>
              {isAdm ? "Nome Fantasia da Clínica *" : "Nome Completo *"}
            </label>
            <input type="text" required value={nome} onChange={(e) => setNome(e.target.value)} className={styles.input} />
          </div>

          {/* CAMPO ATUALIZADO: Totalmente aberto e livre para edição do usuário */}
          <div className={styles.inputGroup}>
            <label className={styles.label}>E-mail de Acesso / Login *</label>
            <input 
              type="email" 
              required
              placeholder="seuemail@exemplo.com"
              value={email} 
              onChange={(e) => setEmail(e.target.value)} 
              className={styles.input} 
            />
          </div>

          <div className={styles.inputGroup}>
            <label className={styles.label}>Nova Senha (Deixe em branco para manter a atual)</label>
            <input type="password" placeholder="••••••••" value={senha} onChange={(e) => setSenha(e.target.value)} className={styles.input} />
          </div>

          <div className={styles.inputGroup}>
            <label className={styles.label}>CEP *</label>
            <input type="text" required value={cep} onChange={(e) => setCep(e.target.value)} className={styles.input} />
          </div>

          {isAdm && (
            <>
              <div className={styles.inputGroup}>
                <label className={styles.label}>Telefone *</label>
                <input type="text" required value={telefone} onChange={(e) => setTelefone(e.target.value)} className={styles.input} />
              </div>

              <div className={styles.inputGroup}>
                <label className={styles.label}>Endereço Completo *</label>
                <input type="text" required value={endereco} onChange={(e) => setEndereco(e.target.value)} className={styles.input} />
              </div>

              <div className={styles.inputGroup}>
                <label className={styles.label}>Espécies Atendidas pelo Estabelecimento *</label>
                <select value={especieAtendida} onChange={(e) => setEspecieAtendida(e.target.value)} className={styles.input}>
                  <option value="Cão">Apenas Cães</option>
                  <option value="Gato">Apenas Gatos</option>
                  <option value="Ambos">Cães e Gatos (Ambos)</option>
                </select>
              </div>
            </>
          )}

          <button type="submit" className={styles.btnSalvar}>Salvar Alterações</button>
        </form>
      </div>
    </div>
  );
}

export default EditarPerfil;
