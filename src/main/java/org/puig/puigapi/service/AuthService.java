package org.puig.puigapi.service;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.puig.puigapi.configuration.jwt.JwtService;
import org.puig.puigapi.persistence.entity.utils.Credentials;
import org.puig.puigapi.persistence.entity.utils.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;

/**
 * Servicío genérico para implementar a un servicio que requiera encriptación de claves,
 * registro y loggeo de usuarios.
 * @param <U> el Usuario que debe extender UserDetails.
 * @param <ID> su id.
 */
public abstract class AuthService <U extends Persona, ID>
        extends PersistenceService<U, ID>{
    protected JwtService jwtService;

    public AuthService(MongoRepository<U, ID> repository) {
        super(repository);
    }

    @Autowired public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Guarda una entidad `Persona` realizando la encriptación de su password.
     * @param u La entidad `Usuario` a guardar.
     * @return La entidad `Usuario` guardada, si la contraseña no puede ser encriptada, devolverá
     * null.
     */
    @Override
    public @Nullable U save(@NotNull U u) {
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

    public String register(@NotNull U u) {
        U saved = save(u);
        if (saved == null) return null;
        return jwtService.generateToken(saved);
    }

    public Optional<String> login(@NotNull Credentials<ID> credential) {
        Optional<U> usuario = repository.findById(credential.identifier());

        return usuario.map(u -> {
            try {
                String hashedPassword = hashPasswordWithSalt(credential.password(), u.getSalt());
                if (u.getPassword().equals(hashedPassword)) return jwtService.generateToken(u);
                return null;
            } catch (NoSuchAlgorithmException e) {
                return null;
            }
        });
    }
}
