import React from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css'; 
import styles from './MapaClinicas.module.css';

import L from 'leaflet';
import icon from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';

// Dimensões em pixels que o Leaflet exige
let DefaultIcon = L.icon({
    iconUrl: icon,
    shadowUrl: iconShadow,
    iconSize: [25, 41],     
    iconAnchor: [12, 41]     
});
L.Marker.prototype.options.icon = DefaultIcon;


function MapaClinicas({ clinicas }) {
  // Ponto central do mapa (caso o banco venha vazio, centraliza em Belo Horizonte por padrão)
  const centroPadrao = [-19.9247, -43.9351]; 

  // Se houver clínicas carregadas, centraliza o mapa na primeira coordenada encontrada
  const centroMapa = clinicas.length > 0 && clinicas[0].latitude && clinicas[0].longitude
    ? [clinicas[0].latitude, clinicas[0].longitude]
    : centroPadrao;

  return (
    <div className={styles.mapaContainer}>
      <MapContainer center={centroMapa} zoom={13} style={{ width: '100%', height: '100%' }} scrollWheelZoom={false}>
        {/* Renderiza o mapa visual gratuito do OpenStreetMap */}
        <TileLayer
          attribution='&copy; <a href="https://openstreetmap.org">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        
        {/* Mapeia dinamicamente as clínicas vindas do MySQL e plota os marcadores no mapa */}
        {clinicas.map((clinica) => {
          if (!clinica.latitude || !clinica.longitude) return null;
          
          return (
            <Marker key={clinica.id} position={[clinica.latitude, clinica.longitude]}>
              <Popup>
                <div style={{ fontSize: '14px' }}>
                  <strong>{clinica.nomeFantasia}</strong> <br />
                  {clinica.endereco} <br />
                  <span style={{ color: 'var(--brand-primary-dark)', fontWeight: 'bold' }}>{clinica.telefone}</span>
                </div>
              </Popup>
            </Marker>
          );
        })}
      </MapContainer>
    </div>
  );
}

export default MapaClinicas;
