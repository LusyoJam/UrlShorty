package com.urlshorty.urlshorty.service;

import com.urlshorty.urlshorty.dto.UrlDto;
import com.urlshorty.urlshorty.entity.UrlMapping;
import com.urlshorty.urlshorty.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URL;
import java.util.UUID;

@Service
public class UrlShortenerService {
    private final UrlMappingRepository urlMappingRepository;

    public UrlShortenerService(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }

    public String generateShortenedUrl() {
        String code = UUID.randomUUID().toString();
        code = code.substring(0, 8);

        return code;
    }

    public UrlMapping createUrlMapping(UrlDto urlDto) {
        String code = generateShortenedUrl();

        while (urlMappingRepository.findOriginalUrlByCode(code).isPresent()) {
            code = generateShortenedUrl();
        }

        var urlMapping = new UrlMapping();

        urlMapping.setOriginalUrl(urlDto.getUrl());
        urlMapping.setCode(code);

        return urlMapping;
    }

    public void addGeneratedCode(UrlMapping urlMapping) {
        urlMappingRepository.save(urlMapping);
    }

    public String getOriginalUrl(String code) {
        var url = urlMappingRepository.findOriginalUrlByCode(code);

        return url.orElse(null);
    }

    public String getGeneratedCode(String url) {
        return  urlMappingRepository.findCodeByOriginalUrl(url).orElse(null);
    }


    public boolean isUrlValid(String url) {
        try {
            new URI(url).toURL();
            new URL(url).toURI();
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}

