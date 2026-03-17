package com.urlshorty.urlshorty.controller;

import com.urlshorty.urlshorty.service.UrlShortenerService;
import com.urlshorty.urlshorty.dto.UrlDto;
import com.urlshorty.urlshorty.exception.NoSuchUrlCodeException;
import com.urlshorty.urlshorty.exception.UrlNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class UrlShortenerController {
    private final UrlShortenerService urlShortenerService;

    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/shorten")
    public ResponseEntity <String> shorten(@RequestBody @Validated(UrlDto.UrlToCode.class) UrlDto urlDto) {
        if (!urlShortenerService.isUrlValid(urlDto.getUrl())) throw new UrlNotValidException();

        String code = urlShortenerService.getGeneratedCode(urlDto.getUrl());
        if (code != null) return ResponseEntity.ok(code);


        var urlMapping = urlShortenerService.createUrlMapping(urlDto);
        urlShortenerService.addGeneratedCode(urlMapping);
        return ResponseEntity.status(201).body(urlMapping.getCode());
    }

    @GetMapping("/{code}")
    public ResponseEntity <String> getCode(@PathVariable String code) {
        String originalUrl = urlShortenerService.getOriginalUrl(code);

        if (originalUrl == null) throw new NoSuchUrlCodeException();
        else return ResponseEntity.status(302).location(URI.create(originalUrl)).body("Redirecting...");
    }
}
