package com.amigopet.api.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amigopet.api.dto.AtualizacaoResponseDTO;
import com.amigopet.api.dto.LoginResponseDTO;
import com.amigopet.api.dto.TutorAtualizacaoDTO;
import com.amigopet.api.dto.TutorCadastroDTO;
import com.amigopet.api.dto.TutorLoginDTO;
import com.amigopet.api.dto.TutorResponseDTO;
import com.amigopet.api.exception.ApiException;
import com.amigopet.api.model.Tutor;
import com.amigopet.api.repository.TutorRepository;

/**
 * Regras de negocio da tabela {@code tutores}.
 */
@Service
public class ServicoTutor {

    private final TutorRepository tutorRepository;
    private final ServicoSenha servicoSenha;

    public ServicoTutor(TutorRepository tutorRepository, ServicoSenha servicoSenha) {
        this.tutorRepository = tutorRepository;
        this.servicoSenha = servicoSenha;
    }

    /**
     * Cadastra um novo tutor, recusando e-mails ja existentes.
     *
     * @param dados dados validados do cadastro
     * @return o tutor cadastrado, sem o campo de senha
     */
    @Transactional
    public TutorResponseDTO cadastrar(TutorCadastroDTO dados) {
        if (tutorRepository.findByEmail(dados.email()).isPresent()) {
            throw ApiException.conflict("Este e-mail ja esta cadastrado!");
        }

        Tutor tutor = new Tutor();
        tutor.setNome(dados.nome().trim());
        tutor.setEmail(dados.email().trim().toLowerCase());
        tutor.setCep(dados.cep().trim());
        tutor.setSenha(servicoSenha.criptografar(dados.senha()));

        return TutorResponseDTO.from(tutorRepository.save(tutor));
    }

    /**
     * Autentica o tutor.
     *
     * <p>Quando a senha armazenada ainda esta em texto puro, o login e
     * aceito e a senha e migrada para BCrypt na mesma operacao.</p>
     *
     * @param dados credenciais informadas
     * @return identificador e nome do tutor autenticado
     */
    @Transactional
    public LoginResponseDTO login(TutorLoginDTO dados) {
        Optional<Tutor> encontrado = tutorRepository.findByEmail(dados.email().trim().toLowerCase());

        if (encontrado.isEmpty()) {
            throw ApiException.unauthorized("E-mail ou senha invalidos.");
        }

        Tutor tutor = encontrado.get();
        if (!servicoSenha.conferir(dados.senha(), tutor.getSenha())) {
            throw ApiException.unauthorized("E-mail ou senha invalidos.");
        }

        if (servicoSenha.precisaMigrar(tutor.getSenha())) {
            tutor.setSenha(servicoSenha.criptografar(dados.senha()));
            tutorRepository.save(tutor);
        }

        return new LoginResponseDTO(tutor.getId(), tutor.getNome(), "Login efetuado com sucesso!");
    }

    /**
     * Atualiza o perfil do tutor. A senha so e alterada quando enviada.
     *
     * @param id   identificador do tutor
     * @param dados novos dados do perfil
     * @return nome salvo e aviso de confirmacao
     */
    @Transactional
    public AtualizacaoResponseDTO atualizar(Long id, TutorAtualizacaoDTO dados) {
        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Tutor nao encontrado."));

        tutor.setNome(dados.nome().trim());
        tutor.setCep(dados.cep().trim());

        if (dados.senha() != null && !dados.senha().isBlank()) {
            tutor.setSenha(servicoSenha.criptografar(dados.senha()));
        }

        Tutor salvo = tutorRepository.save(tutor);
        return new AtualizacaoResponseDTO(salvo.getNome(), "Perfil do tutor atualizado com sucesso!");
    }

    /**
     * Busca os dados publicos de um tutor.
     *
     * @param id identificador do tutor
     * @return dados do tutor
     */
    @Transactional(readOnly = true)
    public TutorResponseDTO buscarPorId(Long id) {
        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Tutor nao encontrado."));
        return TutorResponseDTO.from(tutor);
    }
}
