package com.example.filmBooking.service.impl;

import com.example.filmBooking.model.*;
import com.example.filmBooking.repository.MovieRepository;
import com.example.filmBooking.service.MovieService;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository repository;

//    private ImportMovie importMovie = new ImportMovie();

    @Override
    public List<Movie> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Movie> showPhimDangChieu() {
        return repository.showPhimDangChieu();
    }

    @Override
    public List<Movie> showPhimSapChieu() {
        return repository.showPhimSapChieu();
    }

    @Override
    public List<Movie> showPhishowPhimSapChieuAndDangChieumSapChieu() {
        return repository.showPhimSapChieuAndDangChieu();
    }

    @Override
    public Movie save(Movie movie) {
        Random generator = new Random();
        int value = generator.nextInt((100000 - 1) + 1) + 1;
        movie.setCode("mv" + value);
        System.out.println(movie.getEndDate());
        java.util.Date date = new java.util.Date();
        if (date.after(movie.getEndDate())) {
            movie.setStatus("Đã chiếu");
        } else if (date.before(movie.getPremiereDate())) {
            movie.setStatus("Sắp chiếu");
        } else {
            movie.setStatus("Đang chiếu");
        }
        return repository.save(movie);
    }


    //tự động check ngày để đổi trạng thái phim
//    @Scheduled(fixedRate = 86400000)
//    public void scheduleFixedRate() {
//        // check danh sách movie
//        List<Movie> movies = repository.findAll();
//        for (Movie dto : movies) {
//            java.util.Date date = new java.util.Date();
//            if (date.after(dto.getEndDate())) {
//                dto.setStatus("Đã chiếu");
//                repository.save(dto);
//            } else if (date.before(dto.getPremiereDate())) {
//                dto.setStatus("Sắp chiếu");
//                repository.save(dto);
//            } else {
//                dto.setStatus("Đang chiếu");
//                repository.save(dto);
//            }
//        }
//    }

    @Override
    public Movie update(String id, Movie movie) {
        Movie movieNew = findById(id);
        movieNew.setName(movie.getName());
        movieNew.setMovieDuration(movie.getMovieDuration());
        movieNew.setTrailer(movie.getTrailer());
        movieNew.setPremiereDate(movie.getPremiereDate());
        movieNew.setEndDate(movie.getEndDate());
        movieNew.setEndDate(movie.getEndDate());
        movieNew.setDirectors(movie.getDirectors());
        movieNew.setPerformers(movie.getPerformers());
        movieNew.setLanguages(movie.getLanguages());
        movieNew.setImage(movie.getImage());
        movieNew.setMovieTypes(movie.getMovieTypes());
        movieNew.setDescription(movie.getDescription());
        movieNew.setRated(movie.getRated());
        java.util.Date date = new java.util.Date();
        if (date.after(movie.getEndDate())) {
            movie.setStatus("Đã chiếu");
        } else if (date.before(movie.getPremiereDate())) {
            movie.setStatus("Sắp chiếu");
        } else {
            movie.setStatus("Đang chiếu");
        }
        return repository.save(movieNew);
    }

    @Override
    public Movie findById(String id) {
        return repository.findById(id).get();
    }

    @Override
    public void exportExcel() {

    }

    @Override
    public Movie readExcel() {
        try {
            FileInputStream excelFile = new FileInputStream(new File("C:\\Users\\dieul\\Desktop\\tai_lieu\\projectGraduate\\Demo-ApachePOI-Excel.xlsx"));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            DataFormatter fmt = new DataFormatter();
            Iterator<Row> iterator = datatypeSheet.iterator();
            Row firstRow = iterator.next();
//            Cell firstCell = firstRow.getCell(1);
//            System.out.println(firstCell.getStringCellValue());
            List<Movie> listMovie = new ArrayList<Movie>();
            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                Movie movie = new Movie();
                movie.setName(currentRow.getCell(1).getStringCellValue());
                movie.setMovieDuration(Integer.parseInt(fmt.formatCellValue(currentRow.getCell(2))));
                movie.setPremiereDate(new Date(String.valueOf(currentRow.getCell(3))));
                movie.setEndDate(new Date(String.valueOf(currentRow.getCell(4))));
                //movie.setDirector(currentRow.getCell(5).getStringCellValue());
                //movie.setPerformers(currentRow.getCell(6).getStringCellValue());
                //movie.setLanguages(currentRow.getCell(7).getStringCellValue());
                movie.setImage(currentRow.getCell(8).getStringCellValue());
                //movie.setMovieTypes(currentRow.getCell(9).getStringCellValue());
                movie.setDescription(currentRow.getCell(10).getStringCellValue());
                movie.setTrailer(currentRow.getCell(11).getStringCellValue());
                listMovie.add(movie);
            }
            for (Movie movie : listMovie) {
//                movie.setId();
                save(movie);
            }
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void delete(String id) {
        repository.delete(findById(id));
    }
     @Override
    public List<Movie> getMovie(String cinemaId, String movieId) {
        return repository.getMovie(cinemaId,movieId);
    }
    
    @Override
    public Page<Movie> getAll(Integer curentPage) {
        return repository.findAll(pageMovie(curentPage));
    }

    @Override
    public Pageable pageMovie(Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 8);
        return pageable;
    }

    @Override
    public Movie findByName(String name) {
        return repository.findByNameLike(name);
    }

    @Override
    public Page<Movie> filterMovies(Integer pageNumber, String directors, String languages, String movieTypes,  String performers, String status, String keyword) {
        return repository.filterMovies(pageMovie(pageNumber), directors, languages, movieTypes,  performers, status, keyword);
    }


}
