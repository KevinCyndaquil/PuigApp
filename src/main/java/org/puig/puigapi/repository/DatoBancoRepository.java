package org.puig.puigapi.repository;

import org.puig.puigapi.persistence.entity.DatoBanco;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatoBancoRepository extends MongoRepository<DatoBanco, String> {
}
