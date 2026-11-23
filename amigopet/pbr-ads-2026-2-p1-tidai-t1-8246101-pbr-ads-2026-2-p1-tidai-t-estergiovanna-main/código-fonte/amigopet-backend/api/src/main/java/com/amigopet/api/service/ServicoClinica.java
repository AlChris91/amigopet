package com.amigopet.api.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amigopet.api.dto.AtualizacaoResponseDTO;
import com.amigopet.api.dto.ClinicaAtualizacaoDTO;
import com.amigopet.api.dto.ClinicaBuscaResponseDTO;
import com.amigopet.api.dto.ClinicaCadastroDTO;
import com.amigopet.api.dto.ClinicaLoginDTO;
import com.amigopet.api.dto.ClinicaResponseDTO;
import com.amigopet.api.dto.LoginResponseDTO;
import com.amigopet.api.exception.ApiException;
import com.amigopet.api.model.Clinica;
import com.amigopet.api.model.Servico;
import com.amigopet.api.repository.ClinicaRepository;
import com.amigopet.api.repository.ServicoRepository;
import com.amigopet.api.util.CalculadoraDistancia;
import com.amigopet.api.util.NormalizadorTexto;

/**
 * Regras de negocio da tabela {@code clinicas}, incluindo a busca de
 * prestadores por distancia, servico e regiao.
 */
@Service
public class ServicoClinica {

    /** Raio padrao da busca quando o front-end nao informa o parametro. */
    private static final double RAIO_PADRAO_KM = 10.0;

    private final ClinicaRepository clinicaRepository;
    private final ServicoRepository servicoRepository;
    private final ServicoSenha servicoSenha;

    public ServicoClinica(ClinicaRepository clinicaRepository,
                          ServicoRepository servicoRepository,
                          ServicoSenha servicoSenha) {
        this.clinicaRepository = clinicaRepository;
        this.servicoRepository = servicoRepository;
        this.servicoSenha = servicoSenha;
    }

    /**
     * Cadastra uma clinica parceira, recusando e-mails administrativos ja existentes.
     */
    @Transactional
    public ClinicaResponseDTO cadastrar(ClinicaCadastroDTO dados) {
        if (clinicaRepository.findByEmailAdm(dados.emailAdm().trim().toLowerCase()).isPresent()) {
            throw ApiException.conflict("Este e-mail administrativo ja esta cadastrado!");
        }

        Clinica clinica = new Clinica();
        clinica.setNomeFantasia(dados.nomeFantasia().trim());
        clinica.setEmailAdm(dados.emailAdm().trim().toLowerCase());
        clinica.setSenhaAdm(servicoSenha.criptografar(dados.senhaAdm()));
        clinica.setCep(dados.cep().trim());
        clinica.setEndereco(dados.endereco().trim());
        clinica.setTelefone(dados.telefone().trim());
        clinica.setLatitude(dados.latitude());
        clinica.setLongitude(dados.longitude());
        clinica.setEspecieAtendida(dados.especieAtendida());

        return ClinicaResponseDTO.from(clinicaRepository.save(clinica));
    }

    /**
     * Autentica o administrador da clinica.
     */
    @Transactional
    public LoginResponseDTO login(ClinicaLoginDTO dados) {
        Optional<Clinica> encontrada = clinicaRepository.findByEmailAdm(dados.emailAdm().trim().toLowerCase());

        if (encontrada.isEmpty()) {
            throw ApiException.unauthorized("E-mail ou senha do administrador invalidos.");
        }

        Clinica clinica = encontrada.get();
        if (!servicoSenha.conferir(dados.senhaAdm(), clinica.getSenhaAdm())) {
            throw ApiException.unauthorized("E-mail ou senha do administrador invalidos.");
        }

        if (servicoSenha.precisaMigrar(clinica.getSenhaAdm())) {
            clinica.setSenhaAdm(servicoSenha.criptografar(dados.senhaAdm()));
            clinicaRepository.save(clinica);
        }

        return new LoginResponseDTO(clinica.getId(), clinica.getNomeFantasia(),
                "Login administrativo autorizado!");
    }

    /**
     * Atualiza o perfil da clinica. A senha so e alterada quando enviada.
     */
    @Transactional
    public AtualizacaoResponseDTO atualizar(Long id, ClinicaAtualizacaoDTO dados) {
        Clinica clinica = clinicaRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Clinica nao encontrada."));

        clinica.setNomeFantasia(dados.nomeFantasia().trim());
        clinica.setCep(dados.cep().trim());
        clinica.setEndereco(dados.endereco().trim());
        clinica.setTelefone(dados.telefone().trim());
        clinica.setEspecieAtendida(dados.especieAtendida());
        
        if (dados.senhaAdm() != null && !dados.senhaAdm().isBlank()) {
            clinica.setSenhaAdm(servicoSenha.criptografar(dados.senhaAdm()));
        }

        Clinica salva = clinicaRepository.save(clinica);
        return new AtualizacaoResponseDTO(salva.getNomeFantasia(), "Perfil da clinica atualizado com sucesso!");
    }

    /**
     * Busca as clinicas que atendem aos filtros informados pelo tutor.
     **/
    @Transactional(readOnly = true)
    public List<ClinicaBuscaResponseDTO> buscar(String cep, String servico, String especie, Double raio,
                                                 Double latitude, Double longitude) {
        double raioKm = (raio == null || raio <= 0) ? RAIO_PADRAO_KM : raio;

        List<Clinica> clinicas = clinicaRepository.findAll().stream()
                .filter(clinica -> clinica.getLatitude() != null && clinica.getLongitude() != null)
                .collect(Collectors.toList());

        if (clinicas.isEmpty()) {
            return List.of();
        }

        double[] referencia = pontoDeReferencia(cep, latitude, longitude, clinicas);
        if (referencia == null) {
            return List.of();
        }

        Map<Long, List<Servico>> servicosPorClinica = servicosPorClinica(clinicas);
        
        String categoriaBusca = servico != null ? NormalizadorTexto.semAcento(servico.trim().toLowerCase()) : "";
        String especieBusca = especie != null ? NormalizadorTexto.semAcento(especie.trim().toLowerCase()) : "";

        List<ClinicaBuscaResponseDTO> elegiveis = new ArrayList<>();

        for (Clinica clinica : clinicas) {
            List<Servico> servicosDaClinica = servicosPorClinica.get(clinica.getId());
            
            if (servicosDaClinica == null || servicosDaClinica.isEmpty()) {
                continue;
            }

            boolean ofereceProcedimentoValido = servicosDaClinica.stream().anyMatch(s -> {
                String nomeDoServicoNoBanco = NormalizadorTexto.semAcento(s.getNomeServico().trim().toLowerCase());
                String especieNoBanco = s.getEspecieAtendida() != null ? NormalizadorTexto.semAcento(s.getEspecieAtendida().trim().toLowerCase()) : "";
                
                boolean bateServico = categoriaBusca.isBlank() || nomeDoServicoNoBanco.contains(categoriaBusca);
                boolean bateEspecie = especieBusca.isBlank() || especieNoBanco.contains(especieBusca) || especieNoBanco.contains("ambos") || especieNoBanco.contains("todos");
                
                return bateServico && bateEspecie;
            });

            if (!ofereceProcedimentoValido) {
                continue;
            }

            double distancia = CalculadoraDistancia.distanciaKm(
                    referencia[0], referencia[1], clinica.getLatitude(), clinica.getLongitude());

            if (distancia <= raioKm) {
                elegiveis.add(ClinicaBuscaResponseDTO.from(clinica, distancia));
            }
        }

        elegiveis.sort(Comparator.comparingDouble(ClinicaBuscaResponseDTO::distancia));
        return elegiveis;
    }

    private double[] pontoDeReferencia(String cep, Double lat, Double lon, List<Clinica> clinicas) {
        if (lat != null && lon != null) {
            return new double[]{lat, lon};
        }
        return new double[]{-19.9247, -43.9351}; // Coordenadas centrais padrão de BH
    }

    private Map<Long, List<Servico>> servicosPorClinica(List<Clinica> clinicas) {
        List<Long> ids = clinicas.stream().map(Clinica::getId).collect(Collectors.toList());
        return servicoRepository.findByClinicaIdIn(ids).stream()
                .collect(Collectors.groupingBy(Servico::getClinicaId));
    }
}
