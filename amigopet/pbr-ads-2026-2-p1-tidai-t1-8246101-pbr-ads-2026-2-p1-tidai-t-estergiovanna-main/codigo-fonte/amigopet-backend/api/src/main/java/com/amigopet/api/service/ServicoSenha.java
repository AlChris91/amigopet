package com.amigopet.api.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Criptografia e verificacao das senhas de tutores e administradores.
 *
 * <p>As senhas sao gravadas com BCrypt, que gera um hash com sal aleatorio a
 * cada execucao. O script de banco de dados fornecido no projeto, porem,
 * popula a tabela {@code clinicas} com senhas em texto puro
 * ({@code AmigoPet@2026}). Para que esses registros continuem funcionando, a
 * verificacao aceita o formato legado e, quando o login é bem-sucedido, a
 * senha e automaticamente migrada para o hash BCrypt.</p>
 */
@Service
public class ServicoSenha {

    /** Prefixo dos hashes gerados pelo BCrypt. */
    private static final String PREFIXO_BCRYPT = "$2";

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Gera o hash BCrypt de uma senha em texto puro.
     *
     * @param senhaPura senha digitada pelo usuario
     * @return hash seguro para gravacao no banco
     */
    public String criptografar(String senhaPura) {
        return encoder.encode(senhaPura);
    }

    /**
     * Confere a senha digitada contra a senha armazenada.
     *
     * <p>Aceita tanto hashes BCrypt quanto senhas legadas em texto puro.</p>
     *
     * @param senhaPura     senha digitada pelo usuario
     * @param senhaArmazenada valor gravado no banco
     * @return {@code true} quando as senhas conferem
     */
    public boolean conferir(String senhaPura, String senhaArmazenada) {
        if (senhaPura == null || senhaArmazenada == null) {
            return false;
        }
        if (senhaArmazenada.startsWith(PREFIXO_BCRYPT)) {
            return encoder.matches(senhaPura, senhaArmazenada);
        }
        return senhaPura.equals(senhaArmazenada);
    }

    /**
     * Informa se a senha armazenada ainda esta no formato legado em texto puro.
     *
     * @param senhaArmazenada valor gravado no banco
     * @return {@code true} quando a senha deve ser migrada para BCrypt
     */
    public boolean precisaMigrar(String senhaArmazenada) {
        return senhaArmazenada != null && !senhaArmazenada.startsWith(PREFIXO_BCRYPT);
    }
}
