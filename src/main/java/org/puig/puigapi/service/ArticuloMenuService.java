package org.puig.puigapi.service;

import org.puig.puigapi.persistence.entity.finances.Articulo;
import org.puig.puigapi.persistence.entity.finances.ArticuloMenu;
import org.puig.puigapi.repository.ArticuloMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticuloMenuService extends PersistenceService<ArticuloMenu, String>{
    @Autowired
    public ArticuloMenuService(ArticuloMenuRepository repository){super(repository);}
}
