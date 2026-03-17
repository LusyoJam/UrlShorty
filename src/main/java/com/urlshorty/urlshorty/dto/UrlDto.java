package com.urlshorty.urlshorty.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UrlDto {

    @NotBlank(groups = {UrlToCode.class})
    private String url;

    @NotBlank(groups = {CodeToUrl.class})
    private String code;
    
    public interface UrlToCode{}
    public interface CodeToUrl {}
}
