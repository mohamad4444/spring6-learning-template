package com.github.mohamad4444.springtemplate.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.awt.print.Book;

@FeignClient(name = "book-service", url = "http://book-service:8081")
public interface BookServiceClient {
    @GetMapping("/books/{id}")
    Book getBookById(@PathVariable Long id);
}
