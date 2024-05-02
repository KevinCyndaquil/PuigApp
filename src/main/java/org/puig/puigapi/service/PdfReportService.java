package org.puig.puigapi.service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.jetbrains.annotations.NotNull;

public interface PdfReportService {

    /**
     * Exporta una lista de entidades a un informe PDF.
     * @param print el objeto JasperPrint que contiene la cadena de bytes del pdf a exportar.
     * @return Un array de bytes que contiene los datos del informe PDF.
     * @throws JRException Si se produce un error al generar el informe.
     */
    default byte[] exportPdf(@NotNull JasperPrint print)
            throws JRException {
        return JasperExportManager.exportReportToPdf(print);
    }
}
