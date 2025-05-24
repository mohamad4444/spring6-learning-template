package com.github.mohamad4444.springtemplate.examples.data1.controller;

import com.github.mohamad4444.springtemplate.examples.data1.repository.NoteDocument;
import com.github.mohamad4444.springtemplate.examples.data1.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class NoteController {
	
	private NoteService noteService;
	
	public NoteController(NoteService noteService) {
		this.noteService = noteService;
	}
	
	// CREATE
	@PostMapping("/notes")
	public ResponseEntity<NoteDocument> handlePost(@RequestBody NotePostRequestBody requestBody) {
		final var note = this.noteService.create(requestBody.note(), requestBody.tag());
		
		return new ResponseEntity<>(note, HttpStatus.OK);
	}
	
	// READ
	@GetMapping("/notes/{id}")
	public ResponseEntity<NoteDocument> handleGet(@PathVariable String id) {
		final var note = this.noteService.findById(id);
		
		if(note.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(note.get(), HttpStatus.OK);
	}
	
	@GetMapping("/notes") 
	public ResponseEntity<Iterable<NoteDocument>> handleGetAll() {
		return new ResponseEntity<>(noteService.getAll(), HttpStatus.OK);
	}
	
	// UPDATE
	@PutMapping("/notes/{id}")
	public ResponseEntity<NoteDocument> handlePut(
			@PathVariable String id,
			@RequestBody NotePutRequestBody requestBody
			) {
		final var updateNote = new NoteDocument(id, requestBody.note(), requestBody.tag());
		final var updatedNote = this.noteService.update(updateNote);
		
		return new ResponseEntity<>(updatedNote, HttpStatus.OK);
	}
	
	// DELETE
	@DeleteMapping("/notes/{id}")
	public ResponseEntity<?> handleDelete(@PathVariable String id) {
		this.noteService.deleteById(id);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
