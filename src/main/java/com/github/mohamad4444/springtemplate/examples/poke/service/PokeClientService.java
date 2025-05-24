package com.github.mohamad4444.springtemplate.examples.poke.service;

import com.github.mohamad4444.springtemplate.examples.poke.service.response.PokeApiV2PokemonResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class PokeClientService {

	@Value("${pokeapi.pokemon.url}")
	public String pokemonApiUrl;

	public PokeApiV2PokemonResponse getPokeResponse(String name) {
		if (name.isBlank()) {
			throw new IllegalArgumentException("pokemon name must not be null");
		}

		final var template = new RestTemplate();
		final var uri = pokemonApiUrl + "/" + name;

		return template.getForObject(uri, PokeApiV2PokemonResponse.class);
	}
}
