package com.github.mohamad4444.springtemplate.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface BookServiceClient {
    @GetExchange("/books/{id}")
    BookResponse getBookById(@PathVariable Long id);

    record BookResponse(Long id, String title, String author) { }
}
