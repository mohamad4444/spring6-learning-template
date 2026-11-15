package com.github.mohamad4444.springtemplate.config;

import com.github.mohamad4444.springtemplate.client.BookServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class HttpClientConfig {
    @Bean
    RestClient.Builder externalRestClientBuilder() {
        var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build();
        var factory = new JdkClientHttpRequestFactory(client);
        factory.setReadTimeout(Duration.ofSeconds(5));
        return RestClient.builder().requestFactory(factory);
    }
    @Bean
    public BookServiceClient bookServiceClient(RestClient.Builder externalRestClientBuilder,
            @Value("${app.books.base-url}") String baseUrl) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(
                externalRestClientBuilder.clone().baseUrl(baseUrl).build())).build()
                .createClient(BookServiceClient.class);
    }
}
