import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';


import Login from './pages/00-Login/Login'; 
import LoginAdm from './pages/06-LoginAdm/LoginAdm';
import Busca from './pages/01-Busca/Busca';
import Listagem from './pages/02-Listagem/Listagem';
import Resumo from './pages/03-Resumo/Resumo';
import Fila from './pages/04-Fila/Fila';
import PainelAdm from './pages/05-PainelAdm/PainelAdm';
import EditarPerfil from './pages/07-EditarPerfil/EditarPerfil';
import GerenciarServicos from './pages/08-GerenciarServicos/GerenciarServicos';



function App() {
  return (
    <Router>
      <div className="appShell">
        <Routes>
          {/* A primeira página ao carregar o app agora é o Login/Cadastro */}
          <Route path="/" element={<Login />} />
          
          {/* Fluxo do Tutor após o login */}
          <Route path="/busca" element={<Busca />} />
          <Route path="/clinicas" element={<Listagem />} />
          <Route path="/resumo" element={<Resumo />} />
          <Route path="/fila" element={<Fila />} />
          <Route path="/editar-perfil" element={<EditarPerfil />} />
          
          {/* Fluxo da Clínica Veterinária (ADM) */}
          <Route path="/login-adm" element={<LoginAdm />} />
          <Route path="/adm" element={<PainelAdm />} />
          <Route path="/configurar-servicos" element={<GerenciarServicos />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
