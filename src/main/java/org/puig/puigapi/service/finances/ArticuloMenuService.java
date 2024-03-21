package org.puig.puigapi.service.finances;

import org.puig.puigapi.persistence.entity.finances.ArticuloMenu;
import org.puig.puigapi.persistence.repositories.finances.ArticuloMenuRepository;
import org.puig.puigapi.service.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticuloMenuService extends PersistenceService<ArticuloMenu, String> {
    @Autowired
    public ArticuloMenuService(ArticuloMenuRepository repository) {
        super(repository);
    }
}
