package com.github.mohamad4444.springtemplate.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import com.github.mohamad4444.springtemplate.model.SecuredNoteEntity;
import com.github.mohamad4444.springtemplate.repository.SecuredNoteRepository;

@Service
public class SecuredNoteService {

	private SecuredNoteRepository noteRepository;

	public SecuredNoteService(SecuredNoteRepository noteRepository) {
		this.noteRepository = noteRepository;
	}

	// CREATE
	public SecuredNoteEntity create(String note, String tag) {
		final SecuredNoteEntity noteDocument = new SecuredNoteEntity(note, tag);

		return this.noteRepository.save(noteDocument);
	}

	// READ
	public Optional<SecuredNoteEntity> findById(Long id) {
		return this.noteRepository.findById(id);
	}
	
	public Page<SecuredNoteEntity> findByTag(String tag, Pageable pageable) {
		return this.noteRepository.findByTag(tag, pageable);
	}
	
	public Page<SecuredNoteEntity> getAll(Pageable pageable) {
		return this.noteRepository.findAll(pageable);
	}
	
	// UPDATE
	public SecuredNoteEntity update(SecuredNoteEntity noteDocument) {
		return this.noteRepository.save(noteDocument);
	}

	// DELETE
	public void deleteById(Long id) {
		this.noteRepository.deleteById(id);
	}

}
