package com.github.mohamad4444.springtemplate.examples.data2.controller;

import com.github.mohamad4444.springtemplate.examples.data2.service.NoteService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UIController {

	private NoteService noteService;

	public UIController(NoteService noteService) {
		this.noteService = noteService;
	}

	@GetMapping("ui/notes")
	public String handleGet(Model model) {
		this.addNotesAndNewNoteCreate(model);
		
		return "notes";
	}
	
	@PostMapping("ui/notes")
	public String handlePost(@ModelAttribute("newNote") NoteCreate noteCreate, Model model) {
		final var createdNoteEntity = this.noteService.create(noteCreate.note(), noteCreate.tag());
		model.addAttribute("message", "Successfully created a new note with id " + createdNoteEntity.getId());
		this.addNotesAndNewNoteCreate(model);
		return "notes";
	}

	@DeleteMapping("ui/notes/{id}")
	public String handleDelete(@PathVariable Long id, Model model) {
		this.noteService.deleteById(id);
		this.addNotesAndNewNoteCreate(model);
		
		return "redirect:/ui/notes";
	}

	private void addNotesAndNewNoteCreate(Model model) {
		final var noteCreate = new NoteCreate(null, null);
		model.addAttribute("newNote", noteCreate);

		final var notes = this.noteService.getAll(Pageable.unpaged()).toList();
		model.addAttribute("notes", notes);

	}

}
