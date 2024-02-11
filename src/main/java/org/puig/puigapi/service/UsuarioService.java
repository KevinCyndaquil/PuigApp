package org.puig.puigapi.service;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.puig.puigapi.persistence.entity.operation.Usuario;
import org.puig.puigapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;

@Service
public class UsuarioService extends PersistenceService<Usuario, String>{

    @Autowired
    public UsuarioService(UsuarioRepository repository){
        super(repository);
    }

    /**
     * Encripta una contraseña con una salt.
     * @param password La contraseña a encriptar.
     * @param salt La sal a utilizar.
     * @return La contraseña encriptada.
     * @throws NoSuchAlgorithmException Si el algoritmo SHA-256 no está disponible.
     */
    public static @Nullable String hashPasswordWithSalt(
            @NotNull String password, @NotNull String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] saltBytes = salt.getBytes(StandardCharsets.UTF_8);
            byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
            byte[] inputBytes = new byte[saltBytes.length + passwordBytes.length];
            System.arraycopy(saltBytes, 0, inputBytes, 0, saltBytes.length);
            System.arraycopy(passwordBytes, 0, inputBytes, saltBytes.length, passwordBytes.length);
            byte[] hash = digest.digest(inputBytes);
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Genera una sal aleatoria.
     * La salt solo se debe generar una vez y guardar en la bd
     * @return La sal generada.
     */
    @Contract(" -> new")
    private static @NotNull String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return new String(salt, StandardCharsets.UTF_8);
    }

    /**
     * Guarda una entidad `Usuario`.
     * @param user La entidad `Usuario` a guardar.
     * @return La entidad `Usuario` guardada.
     */
    @Override
    public Usuario save(@NotNull Usuario user) {
        String salt = generateSalt();
        String password = user.getPassword();
        String hashedPassword = hashPasswordWithSalt(password, salt);
        user.setPassword(hashedPassword);
        user.setSalt(salt);
        return super.save(user);
    }

    /**
     * Comprueba si una entidad `Usuario` existe.
     * @param user La entidad `Usuario` a comprobar.
     * @return `true` si la entidad `Usuario` existe, `false` en caso contrario.
     */
    @Override
    public boolean ifExists(@NotNull Usuario user){
        String passwordTemp = user.getPassword();
        user.setPassword(null);

        Optional<Usuario> usuarioDB = super.findBy(user);
        if (usuarioDB.isPresent()) {
            String salt = usuarioDB.get().getSalt();
            String hashedPassword = hashPasswordWithSalt(passwordTemp, salt);
            user.setPassword(hashedPassword);
            return super.ifExists(user);
        }
        return false;
    }
}

