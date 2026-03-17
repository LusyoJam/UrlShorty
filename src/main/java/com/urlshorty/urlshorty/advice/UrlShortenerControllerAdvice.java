package com.urlshorty.urlshorty.advice;

import com.urlshorty.urlshorty.exception.NoSuchUrlCodeException;
import com.urlshorty.urlshorty.exception.UrlNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UrlShortenerControllerAdvice {

    @ExceptionHandler(NoSuchUrlCodeException.class)
    public ResponseEntity <String> noSuchCodeHandler(){
        return ResponseEntity.status(404).body("An original url is not associated with the given code. Please try again");
    }

    @ExceptionHandler(UrlNotValidException.class)
        public ResponseEntity <String> urlNotValidHandler() {
            return ResponseEntity.badRequest().body("The url provided is not valid. Please try again.");
    }
}
