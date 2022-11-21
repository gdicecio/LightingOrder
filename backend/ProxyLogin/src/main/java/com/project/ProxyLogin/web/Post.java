package com.project.ProxyLogin.web;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class Post {

    private final RestTemplate restTemplate;

    public Post () {
        restTemplate = new RestTemplate();
    }

    public String createPost (String uri, String message) {
        // create headers
        HttpHeaders headers = new HttpHeaders();

        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // build the request
        HttpEntity<String> entity = new HttpEntity<>(message, headers);

        // send POST request
        return this.restTemplate.postForObject(uri, entity, String.class);
    }
}
