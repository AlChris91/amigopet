import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './Header.module.css';
import PawMark from '../Brand/PawMark';

function Header({ titulo, mostrarVoltar = true }) {
  const navigate = useNavigate();
  const [menuAberto, setMenuAberto] = useState(false);
  const [nomeUsuario, setNomeUsuario] = useState('Usuário');
  const [isClinica, setIsClinica] = useState(false);

  useEffect(() => {
    const nomeClinica = localStorage.getItem('clinica_nome');
    const nomeTutor = localStorage.getItem('tutor_nome');

    if (nomeClinica) {
      setNomeUsuario(nomeClinica);
      setIsClinica(true);
    } else if (nomeTutor) {
      setNomeUsuario(nomeTutor);
      setIsClinica(false);
    }
  }, [menuAberto]);

  const handleLogout = () => {
    if (window.confirm("Deseja realmente sair do aplicativo?")) {
      localStorage.clear(); 
      navigate('/'); 
    }
  };

  const alternarMenu = () => {
    setMenuAberto(!menuAberto);
  };

  return (
    <>
      <header className={styles.header}>
        {mostrarVoltar ? (
          <button className={styles.backButton} onClick={() => navigate(-1)}>
            ⟨
          </button>
        ) : (
          <div className={styles.spacer}></div>
        )}
        
        <span className={styles.title}>
          <PawMark className={styles.brandMark} />
          {titulo === 'AmigoPet' ? (
            <span className={styles.brandName}>
              Amigo<span className={styles.brandPet}>Pet</span>
            </span>
          ) : titulo}
        </span>
        
        <div className={styles.menuIcon} onClick={alternarMenu}>
          <div className={styles.menuBar}></div>
          <div className={styles.menuBar}></div>
          <div className={styles.menuBar}></div>
        </div>
      </header>

      {menuAberto && <div className={styles.overlay} onClick={alternarMenu}></div>}

      <div className={`${styles.sidebar} ${menuAberto ? styles.sidebarOpen : ''}`}>
        
        <div className={styles.perfilArea}>
          <PawMark className={styles.sidebarMark} />
          <p className={styles.username}>Olá, {nomeUsuario}!</p>
          <span style={{ fontSize: '12px', opacity: 0.7 }}>
            {isClinica ? "Perfil Corporativo" : "Perfil Tutor"}
          </span>
        </div>

        <nav className={styles.menuLista}>
          {localStorage.getItem('tutor_id') && (
            <>
              <button className={styles.menuItem} onClick={() => { navigate('/busca'); setMenuAberto(false); }}>
                🐾 Nova Busca de Clínicas
              </button>
              <button className={styles.menuItem} onClick={() => { navigate('/fila'); setMenuAberto(false); }}>
                📋 Acompanhar Fila Atual
              </button>
            </>
          )}

          {localStorage.getItem('clinica_id') && (
            <>
              <button className={styles.menuItem} onClick={() => { navigate('/adm'); setMenuAberto(false); }}>
                🏢 Acessar Painel Clínica (ADM)
              </button>
              <button className={styles.menuItem} onClick={() => { navigate('/configurar-servicos'); setMenuAberto(false); }}>
                🛠️ Configurar Nossos Serviços
              </button>
            </>
          )}

          <button className={styles.menuItem} onClick={() => { navigate('/editar-perfil'); setMenuAberto(false); }}>
            ⚙️ Editar Meu Perfil
          </button>
        </nav>

        <button className={styles.btnLogout} onClick={handleLogout}>
          🚪 Sair do App
        </button>
      </div>
    </>
  );
}

export default Header;
