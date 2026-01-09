package com.seekhog.backend.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.seekhog.backend.model.LearningEntry;
import com.seekhog.backend.repository.LearningEntryRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExportService {

    @Autowired
    private LearningEntryRepository repository;

    public ByteArrayInputStream exportToExcel(String userId, List<Long> logIds) throws IOException {
        List<LearningEntry> entries = getValidatedEntries(userId, logIds);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Learning Journey");

            // Header
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Date", "Topic", "Sub-Topic", "Content", "Difficulty", "Platform"};
            for (int i = 0; i < columns.length; i++) {
                headerRow.createCell(i).setCellValue(columns[i]);
            }

            // Data
            int rowIdx = 1;
            for (LearningEntry entry : entries) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(entry.getLearningDate().toString());
                row.createCell(1).setCellValue(entry.getTopic());
                row.createCell(2).setCellValue(entry.getSubTopic());
                row.createCell(3).setCellValue(entry.getContent());
                row.createCell(4).setCellValue(entry.getDifficulty() != null ? entry.getDifficulty().toString() : "");
                row.createCell(5).setCellValue(entry.getPlatform());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream exportToPdf(String userId, List<Long> logIds) {
        List<LearningEntry> entries = getValidatedEntries(userId, logIds);
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font font = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
            Paragraph para = new Paragraph("My Learning Journey", font);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            
            // Headers
            String[] headers = {"Date", "Topic", "Sub-Topic", "Content", "Diff", "Platform"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }

            // Data
            for (LearningEntry entry : entries) {
                table.addCell(entry.getLearningDate().toString());
                table.addCell(entry.getTopic());
                table.addCell(entry.getSubTopic());
                table.addCell(entry.getContent());
                table.addCell(entry.getDifficulty() != null ? entry.getDifficulty().toString() : "");
                table.addCell(entry.getPlatform());
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private List<LearningEntry> getValidatedEntries(String userId, List<Long> logIds) {
        // 1. Fetch all requested IDs
        List<LearningEntry> entries = repository.findAllById(logIds);

        // 2. Filter out any that don't belong to this user (Security Check)
        return entries.stream()
                .filter(entry -> entry.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}