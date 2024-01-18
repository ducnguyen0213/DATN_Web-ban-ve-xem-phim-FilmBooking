package com.example.filmBooking.service;

import com.example.filmBooking.model.Language;
import com.example.filmBooking.model.Performer;

import java.util.List;

public interface LanguageService {
    List<Language> fillAll();

    Language save(Language language);

    Language update(String id, Language language);

    void delete(String id);

    Language findById(String id);

    List<Language> searchNameLanguage(String keycode);
    
    List<Language> findNameByMovieId(String id);

    Language findByNameLike(String name);
}
