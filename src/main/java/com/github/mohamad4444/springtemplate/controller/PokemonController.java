package com.github.mohamad4444.springtemplate.controller;

import com.github.mohamad4444.springtemplate.service.PokemonClientService;
import com.github.mohamad4444.springtemplate.model.PokeApiV2PokemonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
public class PokemonController {
	private PokemonClientService pokeClientService;

	public PokemonController(PokemonClientService pokeClientService) {
		this.pokeClientService = pokeClientService;
	}

	@RequestMapping("/pokemon/{name}")
	public PokeApiV2PokemonResponse handleGetPokemon(@PathVariable String name) {

		return pokeClientService.getPokeResponse(name);
	}

	@RequestMapping("/pokemon-with-error-handling/{name}")
	public ResponseEntity<PokeApiV2PokemonResponse> handleGetPokemonWithErrorHandling(@PathVariable String name) {
		try {
			final var pokemon = pokeClientService.getPokeResponse(name);
			return new ResponseEntity<>(pokemon, HttpStatus.OK);
			
			// while this is okay for this example, you should use the proper error handling mechanisms, for example ResponseErrorHandler 
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				throw e;
			}
		}
	}
}
