package com.example.filmBooking.controllerApi;

import com.example.filmBooking.apis.Api;
import com.example.filmBooking.model.Cinema;
import com.example.filmBooking.model.Customer;
import com.example.filmBooking.model.Movie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Controller
@RequestMapping("/show/schedule")
@SessionAttributes("soldTicketsCountBill")
public class ScheduleControllerApi {
    @Autowired
    private RestTemplate restTemplate;

    public static String API_GET_START_AT = Api.baseURL + "/api/schedule/start_at";
    public static String apiGetCinema = Api.baseURL + "/api/schedule/cinema_name";
    public static String apiGetMovie = Api.baseURL + "/api/schedule/movie_name";
    public static String apiGetTime = Api.baseURL + "/api/schedule/time";

    @GetMapping
    public String displaySchedulePage(RedirectAttributes ra,@RequestParam String movieId, @RequestParam String cinemaId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("cinemaId", cinemaId);
        model.addAttribute("cinemaId", cinemaId);
        session.setAttribute("movieId", movieId);
        model.addAttribute("movieId", movieId);
        session.setAttribute("movieId", movieId);
        model.addAttribute("movieId", movieId);
        // Gắn access token jwt vào header để gửi kèm request
        Customer customer = (Customer) session.getAttribute("customer");
        model.addAttribute("customer", customer);
        HttpEntity<?> entity = new HttpEntity<>(customer);


        //lấy ra ngày chiếu phim
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(API_GET_START_AT)
                .queryParam("movieId", "{movieId}")
                .queryParam("cinemaId", "{cinemaId}")
                .encode()
                .toUriString();
        Map<String, String> listRequestParam = new HashMap<>();
        listRequestParam.put("movieId", movieId + "");
        listRequestParam.put("cinemaId", cinemaId + "");

        ResponseEntity<String[]> listStartAtEntity = restTemplate.exchange(urlTemplate,
                HttpMethod.GET,
                entity,
                String[].class,
                listRequestParam);
        List<String> listStartAt = Arrays.asList(listStartAtEntity.getBody());
//        System.out.println(listStartAt);
        if (listStartAt.isEmpty()){
            ra.addFlashAttribute("Message", "Rạp đang không có ngày chiếu nào");
            return "redirect:/show/cinema?movieId=" +movieId+ "&cinemaId=" +cinemaId;
        } else {
            model.addAttribute("listStartAt", listStartAt);
        }

//       lấy ra  giờ phim
        String urlTemplateTime = UriComponentsBuilder.fromHttpUrl(apiGetTime)
                .queryParam("movieId", "{movieId}")
                .queryParam("cinemaId", "{cinemaId}")
                .queryParam("start_at", "{start_at}")
                .encode()
                .toUriString();

        for (String ngay : listStartAt) {
            System.out.println(ngay);
            listRequestParam.put("start_at", ngay + "");
//                model.addAttribute("start_at", ngay);
            break;
//                continue;
        }
        ResponseEntity<Object[]> listStartTimesEntity = restTemplate.exchange(
                urlTemplateTime,
                HttpMethod.GET,
                entity,
                Object[].class,
                listRequestParam);
        List<Object> listTime = Arrays.asList(listStartTimesEntity.getBody());
        model.addAttribute("listTime", listTime);

//
////        lấy ra rap
        String urlTemplate1 = UriComponentsBuilder.fromHttpUrl(apiGetCinema)
                .queryParam("movieId", "{movieId}")
                .queryParam("cinemaId", "{cinemaId}")
                .encode()
                .toUriString();
        HttpEntity<Cinema[]> responsecinema = restTemplate.exchange(
                urlTemplate1,
                HttpMethod.GET,
                entity,
                Cinema[].class,
                listRequestParam
        );
        Cinema[] listcinema = responsecinema.getBody();
        model.addAttribute("listcinema", listcinema);

//
////        lấy ra movie
        String urlTemplate2 = UriComponentsBuilder.fromHttpUrl(apiGetMovie)
                .queryParam("movieId", "{movieId}")
                .queryParam("cinemaId", "{cinemaId}")
                .encode()
                .toUriString();
        HttpEntity<Movie[]> responseMovie = restTemplate.exchange(
                urlTemplate2,
                HttpMethod.GET,
                entity,
                Movie[].class,
                listRequestParam
        );
        Movie[] listmovie = responseMovie.getBody();
        System.out.println("tôi là:"+listmovie);
        model.addAttribute("listmovie", listmovie);
        return "users/Schedule";
    }


}
