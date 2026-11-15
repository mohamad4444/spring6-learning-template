package com.github.mohamad4444.springtemplate.controller;
import com.github.mohamad4444.springtemplate.model.AdvancedNotePostRequestBody;
import com.github.mohamad4444.springtemplate.model.AdvancedNotePutRequestBody;
import com.github.mohamad4444.springtemplate.model.AdvancedNoteCreate;

import com.github.mohamad4444.springtemplate.model.AdvancedNoteEntity;
import com.github.mohamad4444.springtemplate.security.AdvancedAuthenticatedUser;
import com.github.mohamad4444.springtemplate.service.AdvancedNoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@org.springframework.context.annotation.Profile("academic")
@RestController
public class AdvancedNoteController {
	private final Logger LOGGER = LoggerFactory.getLogger(AdvancedNoteController.class);

	private AdvancedNoteService noteService;

	public AdvancedNoteController(AdvancedNoteService noteService) {
		this.noteService = noteService;
	}

	// CREATE
	@Operation(summary = "Create a new note with the given note & tag")
	@PostMapping("/advanced/notes")
	public ResponseEntity<AdvancedNoteEntity> handlePost(@RequestBody AdvancedNotePostRequestBody requestBody,
			@AuthenticationPrincipal AdvancedAuthenticatedUser authenticatedUser) {
		LOGGER.info("POST request to create a new note by user " + authenticatedUser);

		final var note = this.noteService.create(requestBody.note(), requestBody.tag(), authenticatedUser);

		return new ResponseEntity<>(note, HttpStatus.OK);
	}

	// READ
	@Operation(summary = "Gets a note with the given id")
	@ApiResponse(responseCode = "404", description = "The note with the given id does not exist", content = @Content)
	@GetMapping("/advanced/notes/{id}")
	public ResponseEntity<AdvancedNoteEntity> handleGet(@PathVariable Long id, @AuthenticationPrincipal AdvancedAuthenticatedUser authenticatedUser) {
		final var note = this.noteService.findById(id);

		if (note.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(note.get(), HttpStatus.OK);
	}

	@GetMapping("/advanced/notes")
	public ResponseEntity<Page<AdvancedNoteEntity>> handleGetAll(Pageable pageable,
			@RequestParam(required = false) String tag) {

		if (tag == null) {
			return new ResponseEntity<>(noteService.getAll(pageable), HttpStatus.OK);
		}

		return new ResponseEntity<>(noteService.findByTag(tag, pageable), HttpStatus.OK);
	}

	// UPDATE
	@PutMapping("/advanced/notes/{id}")
	public ResponseEntity<AdvancedNoteEntity> handlePut(@PathVariable Long id, @RequestBody AdvancedNotePutRequestBody requestBody, @AuthenticationPrincipal AdvancedAuthenticatedUser authenticatedUser) {

		final var updatedNote = this.noteService.update(id, requestBody.note(), requestBody.tag(), authenticatedUser);

		return new ResponseEntity<>(updatedNote, HttpStatus.OK);
	}

	// DELETE
	@DeleteMapping("/advanced/notes/{id}")
	public ResponseEntity<?> handleDelete(@PathVariable Long id, @AuthenticationPrincipal AdvancedAuthenticatedUser authenticatedUser) {
		this.noteService.deleteById(id, authenticatedUser);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
