package com.urlshorty.urlshorty.repository;

import com.urlshorty.urlshorty.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UrlMappingRepository extends JpaRepository <UrlMapping, Long> {

    Optional<String> findOriginalUrlByCode(String code);
    Optional<String> findCodeByOriginalUrl(String url);

}
