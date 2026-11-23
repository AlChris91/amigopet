import axios from 'axios';

// Configuração central da URL do back-end em Spring Boot
const api = axios.create({
  baseURL: 'https://amigopet-2hku.onrender.com'
});

export default api;
