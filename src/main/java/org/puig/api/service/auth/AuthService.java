package org.puig.api.service.auth;

import lombok.NonNull;
import org.puig.api.configuration.jwt.JwtService;
import org.puig.api.service.SaveService;
import org.puig.api.util.errors.BusquedaSinResultadoException;
import org.puig.api.util.errors.LlaveDuplicadaException;
import org.puig.api.util.errors.NombreUnicoRepetidoException;
import org.puig.api.util.errors.PasswordIncorrectoException;
import org.puig.api.util.data.Tokenisable;
import org.puig.api.util.persistence.Credentials;
import org.puig.api.util.PuigUser;
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
 */
public interface AuthService <U extends PuigUser> extends
        SaveService<U> {

    JwtService jwtService();
    Optional<U> readByCredentials(@NonNull Credentials credentials);

    /**
     * Guarda una entidad `Persona` realizando la encriptación de su password.
     * @param u La entidad `Usuario` a guardar.
     * @return La entidad `Usuario` guardada, si la contraseña no puede ser encriptada, devolverá
     * null.
     */
    @Override
    default U save(@NonNull U u) throws LlaveDuplicadaException, NombreUnicoRepetidoException {
        String salt = generateSalt();
        String password = u.getPassword();

        try {
            String hashedPassword = hashPasswordWithSalt(password, salt);
            u.setPassword(hashedPassword);
            u.setSalt(salt);

            return SaveService.super.save(u);
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
    private @NonNull String generateSalt() {
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
    private @NonNull String hashPasswordWithSalt(
            @NonNull String password,
            @NonNull String salt) throws NoSuchAlgorithmException {

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
    default Tokenisable<U> register(@NonNull U u) {
        U user = save(u);
        if (user == null) return null;

        String token = jwtService().generateToken(user);

        return new Tokenisable<>(user, token, jwtService().getExpiration(token));
    }

    @Transactional
    default Tokenisable<U> login(@NonNull Credentials credential)
            throws PasswordIncorrectoException, BusquedaSinResultadoException {

        return readByCredentials(credential)
                .map(u -> {
                    try {
                        String hashedPassword = hashPasswordWithSalt(credential.password(), u.getSalt());
                        if (!u.getPassword().equals(hashedPassword))
                            throw new PasswordIncorrectoException(credential.identifier());

                        String token = jwtService().generateToken(u);

                        return new Tokenisable<>(u, token, jwtService().getExpiration(token));
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException("Error durante el hashed del password de usuario");
                    }
                })
                .orElseThrow(() -> new BusquedaSinResultadoException("identifier", credential.identifier()));
    }
}
