package com.seekhog.backend.controller;

import com.seekhog.backend.dto.ExportRequest;
import com.seekhog.backend.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/journey/export")
public class ExportController {

    @Autowired
    private ExportService exportService;

    @PostMapping("/excel")
    public ResponseEntity<InputStreamResource> exportExcel(@RequestBody ExportRequest request) throws IOException {
        ByteArrayInputStream in = exportService.exportToExcel(request.getUserId(), request.getLogIds());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=journey.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    @PostMapping("/pdf")
    public ResponseEntity<InputStreamResource> exportPdf(@RequestBody ExportRequest request) {
        ByteArrayInputStream in = exportService.exportToPdf(request.getUserId(), request.getLogIds());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=journey.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(in));
    }
}