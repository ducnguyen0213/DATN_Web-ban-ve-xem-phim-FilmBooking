package com.example.filmBooking.controller;

import com.example.filmBooking.common.ResponseBean;
import com.example.filmBooking.model.*;
import com.example.filmBooking.service.*;
import com.example.filmBooking.util.UploadImage;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/movie")
@SessionAttributes("soldTicketsCount")

public class MovieAdminController {
    @Autowired
    private MovieService service;

    @Autowired
    private RatedService ratedService;

    @Autowired
    private DirectorService directorService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private MovieTypeService movieTypeService;

    @Autowired
    private PerformerService performerService;
    @Autowired
    private UploadImage uploadImage;


    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

//    @GetMapping("/find-all")
//    public String viewMovie(Model model) {
//        return findAll(model, 1, null, null, null, null, null, null);
//    }

    @GetMapping("/find-all/page/{pageNumber}")
    @Operation(summary = "[Hiển thị tất cả]")
    public String findAll(Model model, @PathVariable("pageNumber") Integer pageNumber,
                          @RequestParam(value = "keyword", required = false) String keyword,
                          @RequestParam(value = "status", required = false) String status,
                          @RequestParam(value = "director", required = false) String directors,
                          @RequestParam(value = "movieType", required = false) String movieTypes,
                          @RequestParam(value = "language", required = false) String languages,
                          @RequestParam(value = "performer", required = false) String performers) {
        Page<Movie> page;
        page = service.filterMovies(pageNumber, directors, languages, movieTypes, performers, status, keyword);
        List<Rated> ratedId = ratedService.fillAll();
        List<Director> directorId = directorService.fillAll();
        List<Language> languageId = languageService.fillAll();
        List<MovieType> movieTypeId = movieTypeService.fillAll();
        List<Performer> performerId = performerService.fillAll();

        model.addAttribute("ratedId", ratedId);
        model.addAttribute("languages", languageId);
        model.addAttribute("movieTypes", movieTypeId);
        model.addAttribute("directors", directorId);
        model.addAttribute("performers", performerId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listMovie", page.getContent());

        return "admin/movie";
    }

    @GetMapping("/view-add")
    public String viewAdd(Model model) {
        List<Rated> ratedId = ratedService.fillAll();
        List<Director> directorId = directorService.fillAll();
        List<Language> languageId = languageService.fillAll();
        List<MovieType> movieTypeId = movieTypeService.fillAll();
        List<Performer> performerId = performerService.fillAll();

        model.addAttribute("ratedId", ratedId);
        model.addAttribute("languages", languageId);
        model.addAttribute("movieTypes", movieTypeId);
        model.addAttribute("directors", directorId);
        model.addAttribute("performers", performerId);
        model.addAttribute("movie", new Movie());
        return "admin/form-add-movie";
    }

    @PostMapping("/save")
    @Operation(summary = "[Thêm mới]")
    public String save(Model model,
                       @RequestParam(name = "name") String name,
                       @RequestParam(name = "movieType") List<MovieType> movieTypes,
                       @RequestParam(name = "language") List<Language> languages,
                       @RequestParam(name = "trailer") String trailer,
                       @RequestParam(name = "performer") List<Performer> performers,
                       @RequestParam(name = "description") String description,
                       @RequestParam(name = "endDate") Date endDate,
                       @RequestParam(name = "premiereDate") Date premiereDate,
                       @RequestParam(name = "directors") List<Director> directors,
                       @RequestParam(name = "image") MultipartFile multipartFile,
                       @RequestParam(name = "movieDuration") Integer movieDuration,
                       @RequestParam(name = "ratedId") Rated rated
    ) {
        uploadImage.handerUpLoadFile(multipartFile);
        try {
            Movie movie = Movie.builder()
                    .movieDuration(movieDuration)
                    .name(name)
                    .description(description)
                    .trailer(trailer)
                    .endDate(endDate)
                    .premiereDate(premiereDate)
                    .image(multipartFile.getOriginalFilename())
                    .rated(rated)
                    .directors(directors)
                    .movieTypes(movieTypes)
                    .languages(languages)
                    .performers(performers)
                    .build();

            if (service.save(movie) instanceof Movie) {
                model.addAttribute("thanhCong", "Thêm thành công!");
            } else {
                model.addAttribute("thatBai", "Thêm thất bại");
            }
            model.addAttribute("movie", new Movie());
            return "redirect:/movie/find-all/page/1?status=&keyword=";
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/movie";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteMovie(@PathVariable(name = "id") String id, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute("successMessage", "Xóa thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Xóa thất bại");
        }
        return "redirect:/movie/find-all/page/1?status=&keyword=";
    }

    @GetMapping("/update/{pageNumber}/{id}")
    public String detailMovie(@PathVariable(name = "id") String id, Model model, @PathVariable("pageNumber") Integer currentPage) {
        List<Director> movieDirecttor = directorService.findDireactorByMovieId(id);
        System.out.println(movieDirecttor);
        List<MovieType> movieTypes = movieTypeService.findMovieTyprbyMovieId(id);
        List<Language> languageSelector = languageService.findNameByMovieId(id);
        List<Performer> perfprmerSelect = performerService.findPerformerByMovieId(id);
        List<Rated> ratedId = ratedService.fillAll();
        List<Director> directorId = directorService.fillAll();
        List<Language> languageId = languageService.fillAll();
        List<MovieType> movieTypeId = movieTypeService.fillAll();
        List<Performer> performerId = performerService.fillAll();
        
        model.addAttribute("ratedId", ratedId);
        model.addAttribute("ratedId", ratedId);
        model.addAttribute("languages", languageId);
        model.addAttribute("movieTypes", movieTypeId);
        model.addAttribute("directors", directorId);
        model.addAttribute("directorsSelect", movieDirecttor);
        model.addAttribute("performerSelect", perfprmerSelect);
        model.addAttribute("performers", performerId);
        model.addAttribute("languageSelect", languageSelector);
//        model.addAttribute("movie", new Movie());
        model.addAttribute("movie", service.findById(id));
        return "admin/form-add-movie";
    }

    //    @GetMapping("/search-movie/{pageNumber}")
//    public String searchMovie(Model model, @RequestParam(name = "keyword") String keyword, @PathVariable("pageNumber") Integer currentPage) {
//        Page<Movie> page = service.searchMovie(keyword, currentPage);
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("currentPage", currentPage);
//        model.addAttribute("totalPages", page.getTotalPages());
//        model.addAttribute("totalItems", page.getTotalElements());
//        model.addAttribute("listMovie", page.getContent());
//        model.addAttribute("movie", new Movie());
//        return "admin/movie";
//    }
    @GetMapping("/checkDuplicateName")
    public ResponseEntity<Map<String, Boolean>> checkDuplicateName(@RequestParam("name") String name) {
        boolean isDuplicate = service.findByName(name) != null;
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/director/save")
    @Operation(summary = "[Thêm mới đạo diễn]")
    public String saveDirector(Model model,
                               @RequestParam(name = "name") String name) {
        try {
            Director director = Director.builder()
                    .name(name)
                    .build();

            directorService.save(director);

            model.addAttribute("thanhCong", "Thêm đạo diễn thành công!");
            return "redirect:/movie/view-add";
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/movie";
        }
    }

    @PostMapping("/home/director/save")
    @Operation(summary = "[Thêm mới đạo diễn]")
    public String addDirector(Model model,
                               @RequestParam(name = "name") String name) {
        try {
            Director director = Director.builder()
                    .name(name)
                    .build();

            directorService.save(director);

            model.addAttribute("thanhCong", "Thêm đạo diễn thành công!");
            return "redirect:/movie/director";
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/movie";
        }
    }
    
    @PostMapping("/language/save")
    @Operation(summary = "[Thêm mới ngôn ngữ]")
    public String saveLanguage(Model model,
                               @RequestParam(name = "name") String name) {
        try {
            Language language = Language.builder()
                    .name(name)
                    .build();

            languageService.save(language);

            model.addAttribute("thanhCong", "Thêm ngôn ngữ thành công!");
            return "redirect:/movie/view-add";
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/movie";
        }
    }
    
    @PostMapping("/home/language/save")
    @Operation(summary = "[Thêm mới ngôn ngữ]")
    public String addLanguage(Model model,
                               @RequestParam(name = "name") String name) {
        try {
            Language language = Language.builder()
                    .name(name)
                    .build();

            languageService.save(language);

            model.addAttribute("thanhCong", "Thêm ngôn ngữ thành công!");
            return "redirect:/movie/language";
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/movie";
        }
    }

    @PostMapping("/performer/save")
    @Operation(summary = "[Thêm mới diễn viên]")
    public String savePerformer(Model model,
                                @RequestParam(name = "name") String name) {
        try {
            Performer performer = Performer.builder()
                    .name(name)
                    .build();

            performerService.save(performer);

            model.addAttribute("thanhCong", "Thêm diễn viên thành công!");
            return "redirect:/movie/view-add";
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/movie";
        }
    }
    
    @PostMapping("/home/performer/save")
    @Operation(summary = "[Thêm mới diễn viên]")
    public String addPerformer(Model model,
                                @RequestParam(name = "name") String name) {
        try {
            Performer performer = Performer.builder()
                    .name(name)
                    .build();

            performerService.save(performer);

            model.addAttribute("thanhCong", "Thêm diễn viên thành công!");
            return "redirect:/movie/performer";
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/movie";
        }
    }

    @PostMapping("/movie-type/save")
    @Operation(summary = "[Thêm mới thể loại phim]")
    public String saveMovieType(Model model,
                                @RequestParam(name = "name") String name) {
        try {
            MovieType movieType = MovieType.builder()
                    .name(name)
                    .build();

            movieTypeService.save(movieType);

            model.addAttribute("thanhCong", "Thêm thể loại phim thành công!");
            return "redirect:/movie/view-add";
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/movie";
        }
    }
    
    @PostMapping("/home/movie-type/save")
    @Operation(summary = "[Thêm mới thể loại phim]")
    public String addMovieType(Model model,
                               @RequestParam(name = "name") String name) {
        try {
            MovieType movieType = MovieType.builder()
                    .name(name)
                    .build();

            movieTypeService.save(movieType);

            model.addAttribute("thanhCong", "Thêm thể loại phim thành công!");
            return "redirect:/movie/movie-type";
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/movie";
        }
    }

    @GetMapping("/movie-type")
    public String viewMovieType(Model model) {
        List<MovieType> listMovieType = movieTypeService.fillAll();
        model.addAttribute("listMovieType", listMovieType);
        return "admin/movie-type";
    }

    @GetMapping("/language")
    public String languageMovie(Model model) {
        List<Language> listLanguage = languageService.fillAll();
        model.addAttribute("listLanguage", listLanguage);
        return "admin/language";
    }

    @GetMapping("/director")
    public String viewDirector(Model model) {
        List<Director> listDirector = directorService.fillAll();
        model.addAttribute("listDirector", listDirector);
        return "admin/director";
    }

    @GetMapping("/performer")
    public String viewPerformer(Model model) {
        List<Performer> listPerformer = performerService.fillAll();
        model.addAttribute("listPerformer", listPerformer);
        return "admin/performer";
    }

    //delete
    @GetMapping("/movie-type/delete/{id}")
    public String deleteMovieType(@PathVariable("id") String id, RedirectAttributes ra) {
        try {
            movieTypeService.delete(id);
            ra.addFlashAttribute("successMessage", "Xóa thành công");
        } catch (Exception exception) {
            ra.addFlashAttribute("errorMessage", "Xóa thất bại");
        }
        return "redirect:/movie/movie-type";
    }

    @GetMapping("/language/delete/{id}")
    public String deleteLanguage(@PathVariable("id") String id, RedirectAttributes ra) {
        try {
            languageService.delete(id);
            ra.addFlashAttribute("successMessage", "Xóa thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Xóa thất bại");
        }
        return "redirect:/movie/language";
    }

    @GetMapping("/director/delete/{id}")
    public String deleteDirector(@PathVariable("id") String id, RedirectAttributes ra) {
        try {
            directorService.delete(id);
            ra.addFlashAttribute("successMessage", "Xóa thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Xóa thất bại");
        }
        return "redirect:/movie/director";
    }

    @GetMapping("/performer/delete/{id}")
    public String deletePerformer(@PathVariable("id") String id, RedirectAttributes ra) {
        try {
            performerService.delete(id);
            ra.addFlashAttribute("successMessage", "Xóa thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Xóa thất bại");
        }
        return "redirect:/movie/performer";
    }

    // update
    @PostMapping("/movie-type/update/{id}")
    public String updateMovieType(Model model, @PathVariable("id") String id, MovieType updateType, RedirectAttributes ra) {
        movieTypeService.update(id, updateType);
        ra.addAttribute("successMessage", "Sửa thành công");
        return "redirect:/movie/movie-type";
    }

    @PostMapping("/language/update/{id}")
    public String updateLanguage(Model model, @PathVariable("id") String id, Language update, RedirectAttributes ra) {
        languageService.update(id, update);
        ra.addAttribute("successMessage", "Sửa thành công");
        return "redirect:/movie/language";
    }

    @PostMapping("/update/director/{id}")
    public String updateDirector(Model model, @PathVariable("id") String id, Director director, RedirectAttributes ra) {
        directorService.update(id, director);
        ra.addAttribute("successMessage", "Sửa thành công");
        return "redirect:/movie/director";
    }

    @PostMapping("/performer/update/{id}")
    public String updatePerformer(Model model, @PathVariable("id") String id, Performer update, RedirectAttributes ra) {
        performerService.update(id, update);
        ra.addAttribute("successMessage", "Sửa thành công");
        return "redirect:/movie/performer";
    }

    @GetMapping("/search/director")
    public String searchDirector(Model model, @RequestParam(value = "keyword", required = false) String name){
        List<Director> searchDirector = directorService.searchNameDirector(name);
        model.addAttribute("listDirector", searchDirector);
        return "admin/director";
    }
    @GetMapping("/search/performer")
    public String searchPerformer(Model model, @RequestParam(value = "keyword", required = false) String name){
        List<Performer> searchPerformer = performerService.searchNamePerformer(name);
        model.addAttribute("listPerformer", searchPerformer);
        return "admin/performer";
    }
    @GetMapping("/search/language")
    public String searchLanguage(Model model, @RequestParam(value = "keyword", required = false) String name){
        List<Language> searchLanguage = languageService.searchNameLanguage(name);
        model.addAttribute("listLanguage", searchLanguage);
        return "admin/language";
    }
    @GetMapping("/search/movie-type")
    public String searchMovieType(Model model, @RequestParam(value = "keyword", required = false) String name){
        List<MovieType> searchMovieType = movieTypeService.searchNameMovieType(name);
        model.addAttribute("listMovieType", searchMovieType);
        return "admin/movie-type";
    }

    @GetMapping("/checkLanguage")
    public ResponseEntity<Map<String, Boolean>> checkLanguage(@RequestParam("name") String name) {
        boolean isDuplicate = languageService.findByNameLike(name) != null;
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/checkDirector")
    public ResponseEntity<Map<String, Boolean>> checkDirector(@RequestParam("name") String name) {
        boolean isDuplicate = directorService.findByNameLike(name) != null;
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/checkPerformer")
    public ResponseEntity<Map<String, Boolean>> checkPerformer(@RequestParam("name") String name) {
        boolean isDuplicate = performerService.findByNameLike(name) != null;
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/checkMovieType")
    public ResponseEntity<Map<String, Boolean>> checkMovieType(@RequestParam("name") String name) {
        boolean isDuplicate = movieTypeService.findByNameLike(name) != null;
        Map<String, Boolean> response = new HashMap<>();
        response.put("checkMovieType", isDuplicate);
        return ResponseEntity.ok(response);
    }
}
