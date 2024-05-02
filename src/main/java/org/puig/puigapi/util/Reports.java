package org.puig.puigapi.util;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.puig.puigapi.exceptions.PuigReporteException;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Objects;

public enum Reports {
    VENTAS_ARTICULOS(Objects.requireNonNull(Reports.class.getClassLoader().getResource(
            "reports/reportepuig_ventas.jrxml")));

    public final JasperReport content;

    Reports(URL url) {
        try {
            this.content = JasperCompileManager.compileReport(
                    ResourceUtils.getFile(url).getAbsolutePath());
        } catch (FileNotFoundException | JRException e) {
            throw new PuigReporteException(url);
        }
    }
}
