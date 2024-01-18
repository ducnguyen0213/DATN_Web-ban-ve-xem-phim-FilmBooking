package com.example.filmBooking.service.impl;

import com.example.filmBooking.model.Language;
import com.example.filmBooking.repository.LanguageRepository;
import com.example.filmBooking.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.filmBooking.repository.MovieRepository;
import com.example.filmBooking.model.Movie;


import java.util.List;
import java.util.Random;

@Service
public class LanguageServiceImpl implements LanguageService {

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<Language> fillAll() {
        return languageRepository.findAll();
    }

    @Override
    public Language save(Language language) {
        Random generator = new Random();
        int value = generator.nextInt((100000 - 1) + 1) + 1;
        language.setCode("NN" + value);
        return languageRepository.save(language);
    }

    @Override
    public Language update(String id, Language language) {
        Language languageUpdate = findById(id);
        languageUpdate.setName(language.getName());
        return languageRepository.save(languageUpdate);
    }

    @Override
    public void delete(String id) {
        languageRepository.delete(findById(id));
    }

    @Override
    public Language findById(String id) {
        return languageRepository.findById(id).get();
    }

    @Override
    public List<Language> searchNameLanguage(String keycode) {
        return languageRepository.findByNameContains(keycode);
    }
    
    @Override
    public List<Language> findNameByMovieId(String id) {
        Movie movie = movieRepository.findById(id).get();
        List<Language> listLanguage = movie.getLanguages();
        return listLanguage;
    }

    @Override
    public Language findByNameLike(String name) {
        return languageRepository.findByNameLike(name);
    }
}
