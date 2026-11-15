package com.github.mohamad4444.springtemplate.controller;
import com.github.mohamad4444.springtemplate.model.JpaNotePostRequestBody;
import com.github.mohamad4444.springtemplate.model.JpaNotePutRequestBody;
import com.github.mohamad4444.springtemplate.model.JpaNoteCreate;

import com.github.mohamad4444.springtemplate.model.JpaNoteEntity;
import com.github.mohamad4444.springtemplate.service.JpaNoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@org.springframework.context.annotation.Profile("academic")
@RestController
public class JpaNoteController {

	private JpaNoteService noteService;

	public JpaNoteController(JpaNoteService noteService) {
		this.noteService = noteService;
	}

	// CREATE
	@Operation(summary = "Create a new note with the given note & tag")
	@PostMapping("/jpa/notes")
	public ResponseEntity<JpaNoteEntity> handlePost(@RequestBody JpaNotePostRequestBody requestBody) {
		final var note = this.noteService.create(requestBody.note(), requestBody.tag());

		return new ResponseEntity<>(note, HttpStatus.OK);
	}

	// READ
	@Operation(summary = "Gets a note with the given id")
	@ApiResponse(responseCode = "404", description = "The note with the given id does not exist", content = @Content)
	@GetMapping("/jpa/notes/{id}")
	public ResponseEntity<JpaNoteEntity> handleGet(@PathVariable Long id) {
		final var note = this.noteService.findById(id);

		if (note.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(note.get(), HttpStatus.OK);
	}

	@GetMapping("/jpa/notes")
	public ResponseEntity<Page<JpaNoteEntity>> handleGetAll(Pageable pageable,
			@RequestParam(required = false) String tag) {

		if (tag == null) {
			return new ResponseEntity<>(noteService.getAll(pageable), HttpStatus.OK);
		}

		return new ResponseEntity<>(noteService.findByTag(tag, pageable), HttpStatus.OK);
	}

	// UPDATE
	@PutMapping("/jpa/notes/{id}")
	public ResponseEntity<JpaNoteEntity> handlePut(@PathVariable Long id, @RequestBody JpaNotePutRequestBody requestBody) {
		final var updateNote = new JpaNoteEntity();
		updateNote.setId(id);
		updateNote.setNote(requestBody.note());
		updateNote.setTag(requestBody.tag());

		final var updatedNote = this.noteService.update(updateNote);

		return new ResponseEntity<>(updatedNote, HttpStatus.OK);
	}

	// DELETE
	@DeleteMapping("/jpa/notes/{id}")
	public ResponseEntity<?> handleDelete(@PathVariable Long id) {
		this.noteService.deleteById(id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
