import axios from 'axios';

// Configuração central da URL do back-end em Spring Boot
const api = axios.create({
  baseURL: 'http://localhost:8081/api'
});

export default api;
