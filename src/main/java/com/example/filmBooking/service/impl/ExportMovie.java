package com.example.filmBooking.service.impl;


import com.example.filmBooking.model.Movie;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ExportMovie {
    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final List<Movie> listMovie;

    public ExportMovie(List<Movie> listMovie) {
        this.listMovie = listMovie;
        workbook = new XSSFWorkbook();
    }


    private void writeHeaderLine() {
        sheet = workbook.createSheet("Movie");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Code", style);
        createCell(row, 1, "Name", style);
        createCell(row, 2, "movie_duration", style);
        createCell(row, 3, "trailer", style);
        createCell(row, 4, "premiere_date", style);
        createCell(row, 5, "end_date", style);
        createCell(row, 6, "status", style);
        createCell(row, 7, "director", style);
        createCell(row, 8, "performers", style);
        createCell(row, 9, "language", style);
        createCell(row, 10, "image", style);
        createCell(row, 11, "movie_type", style);
        createCell(row, 12, "description", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Movie Movie : listMovie) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

//            createCell(row, columnCount++, Movie.getId(), style);
            createCell(row, columnCount++, Movie.getCode(), style);
            createCell(row, columnCount++, Movie.getName(), style);
            createCell(row, columnCount++, Movie.getMovieDuration(), style);
            createCell(row, columnCount++, Movie.getTrailer(), style);
            createCell(row, columnCount++, Movie.getPremiereDate(), style);
            createCell(row, columnCount++, Movie.getEndDate(), style);
            createCell(row, columnCount++, Movie.getStatus(), style);
            createCell(row, columnCount++, Movie.getDirectors(), style);
            createCell(row, columnCount++, Movie.getPerformers(), style);
            createCell(row, columnCount++, Movie.getLanguages(), style);
            createCell(row, columnCount++, Movie.getImage(), style);
            createCell(row, columnCount++, Movie.getMovieTypes(), style);
            createCell(row, columnCount++, Movie.getDescription(), style);
//            createCell(row, columnCount++, Movie.getFoundedYear().toString(), style);
//            createCell(row, columnCount++, Movie.getAcreage(), style);

        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}
