package org.puig.api.controller;

import lombok.NonNull;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Date;

public interface PdfController {

    default HttpHeaders generatePdfHeader(@NonNull String pdfName) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_PDF);
        header.setContentDisposition(ContentDisposition.inline()
                .name("pdf")
                .filename("%s-%s.pdf".formatted(pdfName, new Date()))
                .build());
        return header;
    }
}
