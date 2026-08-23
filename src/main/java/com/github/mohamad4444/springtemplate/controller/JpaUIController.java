package com.github.mohamad4444.springtemplate.controller;

import com.github.mohamad4444.springtemplate.service.JpaNoteService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.github.mohamad4444.springtemplate.model.JpaNoteCreate;

@Controller
public class JpaUIController {

	private JpaNoteService noteService;

	public JpaUIController(JpaNoteService noteService) {
		this.noteService = noteService;
	}

	@GetMapping("ui/notes")
	public String handleGet(Model model) {
		this.addNotesAndNewJpaNoteCreate(model);
		
		return "notes";
	}
	
	@PostMapping("ui/notes")
	public String handlePost(@ModelAttribute("newNote") JpaNoteCreate noteCreate, Model model) {
		final var createdJpaNoteEntity = this.noteService.create(noteCreate.note(), noteCreate.tag());
		model.addAttribute("message", "Successfully created a new note with id " + createdJpaNoteEntity.getId());
		this.addNotesAndNewJpaNoteCreate(model);
		return "notes";
	}

	@DeleteMapping("ui/notes/{id}")
	public String handleDelete(@PathVariable Long id, Model model) {
		this.noteService.deleteById(id);
		this.addNotesAndNewJpaNoteCreate(model);
		
		return "redirect:/ui/notes";
	}

	private void addNotesAndNewJpaNoteCreate(Model model) {
		final var noteCreate = new JpaNoteCreate(null, null);
		model.addAttribute("newNote", noteCreate);

		final var notes = this.noteService.getAll(Pageable.unpaged()).toList();
		model.addAttribute("notes", notes);

	}

}
