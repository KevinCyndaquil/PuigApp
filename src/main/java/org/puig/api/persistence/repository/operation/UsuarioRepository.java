package org.puig.api.persistence.repository.operation;

import org.puig.api.persistence.entity.operation.Usuario;
import org.puig.api.persistence.repository.PuigRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends PuigRepository<Usuario> {

    @Query("{$and: [{'correo.direccion': ?0}, {_class: ?1}]}")
    Optional<Usuario> findByCorreo(String _correo, String _class);

    @Query("{$and: [{'telefono.numero': ?0}, {_class: ?1}]}")
    Optional<Usuario> findByTelefono(String _telefono, String _class);

    @Query("{$and: [{$or: [{'correo.direccion': ?0}, {'telefono.numero': ?1}]}, {_class: ?2}]}")
    Optional<Usuario> findByCorreoOrTelefono(String _correo, String _telefono, String _class);

    @Query("{$and: [{$or: [{'correo.direccion': ?0}, {'telefono.numero': ?0}]}, {_class: ?1}]}")
    Optional<Usuario> findByCorreoOrTelefono(String identifer, String _class);
}