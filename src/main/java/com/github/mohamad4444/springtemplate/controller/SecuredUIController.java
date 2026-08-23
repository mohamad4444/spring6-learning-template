package com.github.mohamad4444.springtemplate.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.github.mohamad4444.springtemplate.model.SecuredNoteCreate;
import com.github.mohamad4444.springtemplate.service.SecuredNoteService;

@Controller
public class SecuredUIController {

	private SecuredNoteService noteService;

	public SecuredUIController(SecuredNoteService noteService) {
		this.noteService = noteService;
	}

	@GetMapping("ui/notes")
	public String handleGet(Model model) {
		this.addNotesAndNewSecuredNoteCreate(model);
		
		return "notes";
	}
	
	@PostMapping("ui/notes")
	public String handlePost(@ModelAttribute("newNote") SecuredNoteCreate noteCreate, Model model) {
		final var createdSecuredNoteEntity = this.noteService.create(noteCreate.note(), noteCreate.tag());
		model.addAttribute("message", "Successfully created a new note with id " + createdSecuredNoteEntity.getId());
		this.addNotesAndNewSecuredNoteCreate(model);
		return "notes";
	}

	@DeleteMapping("ui/notes/{id}")
	public String handleDelete(@PathVariable Long id, Model model) {
		this.noteService.deleteById(id);
		this.addNotesAndNewSecuredNoteCreate(model);
		
		return "redirect:/ui/notes";
	}

	private void addNotesAndNewSecuredNoteCreate(Model model) {
		final var noteCreate = new SecuredNoteCreate(null, null);
		model.addAttribute("newNote", noteCreate);

		final var notes = this.noteService.getAll(Pageable.unpaged()).toList();
		model.addAttribute("notes", notes);

	}

}
