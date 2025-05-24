package com.github.mohamad4444.springtemplate.examples.data2Security2.controller;

import com.github.mohamad4444.springtemplate.examples.data2Security2.repository.NoteEntity;
import com.github.mohamad4444.springtemplate.examples.data2Security2.security.AuthenticatedUser;
import com.github.mohamad4444.springtemplate.examples.data2Security2.service.NoteService;
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

@RestController
public class NoteController {
	private final Logger LOGGER = LoggerFactory.getLogger(NoteController.class);

	private NoteService noteService;

	public NoteController(NoteService noteService) {
		this.noteService = noteService;
	}

	// CREATE
	@Operation(summary = "Create a new note with the given note & tag")
	@PostMapping("/notes")
	public ResponseEntity<NoteEntity> handlePost(@RequestBody NotePostRequestBody requestBody,
			@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		LOGGER.info("POST request to create a new note by user " + authenticatedUser);
		
		final var note = this.noteService.create(requestBody.note(), requestBody.tag(), authenticatedUser);

		return new ResponseEntity<>(note, HttpStatus.OK);
	}

	// READ
	@Operation(summary = "Gets a note with the given id")
	@ApiResponse(responseCode = "404", description = "The note with the given id does not exist", content = @Content)
	@GetMapping("/notes/{id}")
	public ResponseEntity<NoteEntity> handleGet(@PathVariable Long id, @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		final var note = this.noteService.findById(id);

		if (note.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(note.get(), HttpStatus.OK);
	}

	@GetMapping("/notes")
	public ResponseEntity<Page<NoteEntity>> handleGetAll(Pageable pageable,
			@RequestParam(required = false) String tag) {

		if (tag == null) {
			return new ResponseEntity<>(noteService.getAll(pageable), HttpStatus.OK);
		}

		return new ResponseEntity<>(noteService.findByTag(tag, pageable), HttpStatus.OK);
	}

	// UPDATE
	@PutMapping("/notes/{id}")
	public ResponseEntity<NoteEntity> handlePut(@PathVariable Long id, @RequestBody NotePutRequestBody requestBody, @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {

		final var updatedNote = this.noteService.update(id, requestBody.note(), requestBody.tag(), authenticatedUser);

		return new ResponseEntity<>(updatedNote, HttpStatus.OK);
	}

	// DELETE
	@DeleteMapping("/notes/{id}")
	public ResponseEntity<?> handleDelete(@PathVariable Long id, @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		this.noteService.deleteById(id, authenticatedUser);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
