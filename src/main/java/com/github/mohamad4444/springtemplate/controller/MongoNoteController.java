package com.github.mohamad4444.springtemplate.controller;
import com.github.mohamad4444.springtemplate.model.MongoNotePostRequestBody;
import com.github.mohamad4444.springtemplate.model.MongoNotePutRequestBody;
import com.github.mohamad4444.springtemplate.model.MongoNoteCreate;

import com.github.mohamad4444.springtemplate.model.MongoNoteDocument;
import com.github.mohamad4444.springtemplate.service.MongoNoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MongoNoteController {
	
	private MongoNoteService noteService;
	
	public MongoNoteController(MongoNoteService noteService) {
		this.noteService = noteService;
	}
	
	// CREATE
	@PostMapping("/mongo/notes")
	public ResponseEntity<MongoNoteDocument> handlePost(@RequestBody MongoNotePostRequestBody requestBody) {
		final var note = this.noteService.create(requestBody.note(), requestBody.tag());
		
		return new ResponseEntity<>(note, HttpStatus.OK);
	}
	
	// READ
	@GetMapping("/mongo/notes/{id}")
	public ResponseEntity<MongoNoteDocument> handleGet(@PathVariable String id) {
		final var note = this.noteService.findById(id);
		
		if(note.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(note.get(), HttpStatus.OK);
	}
	
	@GetMapping("/mongo/notes") 
	public ResponseEntity<Iterable<MongoNoteDocument>> handleGetAll() {
		return new ResponseEntity<>(noteService.getAll(), HttpStatus.OK);
	}
	
	// UPDATE
	@PutMapping("/mongo/notes/{id}")
	public ResponseEntity<MongoNoteDocument> handlePut(
			@PathVariable String id,
			@RequestBody MongoNotePutRequestBody requestBody
			) {
		final var updateNote = new MongoNoteDocument(id, requestBody.note(), requestBody.tag());
		final var updatedNote = this.noteService.update(updateNote);
		
		return new ResponseEntity<>(updatedNote, HttpStatus.OK);
	}
	
	// DELETE
	@DeleteMapping("/mongo/notes/{id}")
	public ResponseEntity<?> handleDelete(@PathVariable String id) {
		this.noteService.deleteById(id);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
