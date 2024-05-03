package org.puig.puigapi.service.auth;

import lombok.NonNull;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.puig.puigapi.configuration.jwt.JwtService;
import org.puig.puigapi.exceptions.BusquedaSinResultadoException;
import org.puig.puigapi.exceptions.LlaveDuplicadaException;
import org.puig.puigapi.exceptions.PasswordIncorrectoException;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.util.data.Tokenisable;
import org.puig.puigapi.util.persistence.Credentials;
import org.puig.puigapi.util.Persona;
import org.puig.puigapi.persistence.repository.PuigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;

/**
 * Servicío genérico para implementar a un servicio que requiera encriptación de claves,
 * registro y login de usuarios.
 * @param <U> el Usuario que debe extender UserDetails.
 * @param <R> el Repositorio del usuario.
 */
@Setter(onMethod_ = @Autowired)
public abstract class AuthService <U extends Persona, R extends PuigRepository<U, ObjectId>> extends
        PersistenceService<U, ObjectId, R> {

    @NonNull protected JwtService jwtService;

    protected AuthService(R repository) {
        super(repository);
    }

    public abstract Optional<U> readByCredentials(@NotNull Credentials credentials);

    /**
     * Guarda una entidad `Persona` realizando la encriptación de su password.
     * @param u La entidad `Usuario` a guardar.
     * @return La entidad `Usuario` guardada, si la contraseña no puede ser encriptada, devolverá
     * null.
     */
    @Override
    public U save(@NotNull U u) throws LlaveDuplicadaException {
        String salt = generateSalt();
        String password = u.getPassword();

        try {
            String hashedPassword = hashPasswordWithSalt(password, salt);
            u.setPassword(hashedPassword);
            u.setSalt(salt);

            return super.save(u);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * Genera una sal aleatoria.
     * La salt solo se debe generar una vez y guardar en la bd
     *
     * @return La sal generada.
     */
    @Contract(" -> new")
    protected @NotNull String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return new String(salt, StandardCharsets.UTF_8);
    }

    /**
     * Encripta una contraseña con una salt.
     *
     * @param password La contraseña a encriptar.
     * @param salt     La salt a utilizar.
     * @return La contraseña encriptada.
     * @throws NoSuchAlgorithmException cuando el algoritmo usado no puede ser implementado.
     */
    protected @NotNull String hashPasswordWithSalt(
            @NotNull String password,
            @NotNull String salt) throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] saltBytes = salt.getBytes(StandardCharsets.UTF_8);
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        byte[] inputBytes = new byte[saltBytes.length + passwordBytes.length];

        System.arraycopy(
                saltBytes,
                0,
                inputBytes,
                0,
                saltBytes.length);
        System.arraycopy(
                passwordBytes,
                0,
                inputBytes,
                saltBytes.length,
                passwordBytes.length);

        byte[] hash = digest.digest(inputBytes);

        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = String.format("%02x", b);
            hexString.append(hex);
        }

        return hexString.toString();
    }

    @Transactional
    public @Nullable Tokenisable<U> register(@NotNull U u) {
        U user = save(u);
        if (user == null) return null;

        String token = jwtService.generateToken(user);

        return new Tokenisable<>(user, token, jwtService.getExpiration(token));
    }

    @Transactional
    public Tokenisable<U> login(@NotNull Credentials credential)
            throws PasswordIncorrectoException, BusquedaSinResultadoException {

        return readByCredentials(credential)
                .map(u -> {
                    try {
                        String hashedPassword = hashPasswordWithSalt(credential.password(), u.getSalt());
                        if (!u.getPassword().equals(hashedPassword))
                            throw new PasswordIncorrectoException(credential.identifier());

                        String token = jwtService.generateToken(u);

                        return new Tokenisable<>(u, token, jwtService.getExpiration(token));
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException("Error durante el hashed del password de usuario");
                    }
                })
                .orElseThrow(() -> new BusquedaSinResultadoException("identifier", credential.identifier()));
    }
}
