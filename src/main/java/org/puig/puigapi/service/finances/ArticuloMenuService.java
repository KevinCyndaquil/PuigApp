package org.puig.puigapi.service.finances;

import org.puig.puigapi.persistence.entity.finances.ArticuloMenu;
import org.puig.puigapi.persistence.repositories.finances.ArticuloMenuRepository;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.service.admin.ProductoTiendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticuloMenuService extends PersistenceService<ArticuloMenu, String> {
    protected ProductoTiendaService productoTiendaService;

    @Autowired
    public void setProductoTiendaService(ProductoTiendaService productoTiendaService) {
        this.productoTiendaService = productoTiendaService;
    }

    @Autowired
    public ArticuloMenuService(ArticuloMenuRepository repository) {
        super(repository, ArticuloMenu.class);
    }

    @Override
    public List<ArticuloMenu> readAll() {
        return repository.findAllByClass(ArticuloMenu.class.getName());
    }
}
