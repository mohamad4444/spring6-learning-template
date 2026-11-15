package com.github.mohamad4444.springtemplate.service;

import com.github.mohamad4444.springtemplate.model.PokeApiV2PokemonResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PokemonClientService {
    private final RestClient client;
    public PokemonClientService(RestClient.Builder externalRestClientBuilder,
            @Value("${pokeapi.pokemon.url}") String baseUrl) {
        client = externalRestClientBuilder.clone().baseUrl(baseUrl).build();
    }
    @Cacheable("pokemon")
    public PokeApiV2PokemonResponse getPokeResponse(String name) {
        if (name == null || !name.matches("[a-zA-Z0-9-]{1,64}")) {
            throw new IllegalArgumentException("Invalid Pokemon name");
        }
        return client.get().uri("/{name}", name).retrieve().body(PokeApiV2PokemonResponse.class);
    }
}
