package org.puig.puigapi.repository;

import org.puig.puigapi.persistence.entity.finances.Combo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComboRepository extends MongoRepository<Combo, String> {
}
