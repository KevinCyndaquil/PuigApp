package org.puig.puigapi.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Date;

public interface PdfController {

    default HttpHeaders generatePdfHeader(@NotNull String pdfName) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_PDF);
        header.setContentDisposition(ContentDisposition.inline()
                .name("pdf")
                .filename("%s-%s.pdf".formatted(pdfName, new Date()))
                .build());
        header.setContentDispositionFormData("attachment", pdfName);
        return header;
    }
}
