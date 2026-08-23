package com.github.mohamad4444.springtemplate.model;

import java.util.List;

public record PokeApiV2PokemonResponse(
		Integer id,
		String name,
		Integer height,
		Integer weight,
		List<PokeApiV2MoveResponse> moves,
		List<PokeApiV2TypeResponse> types
		) {
}
