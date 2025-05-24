package com.github.mohamad4444.springtemplate.examples.data2Security1.service;

import com.github.mohamad4444.springtemplate.examples.data2.repository.NoteEntity;
import com.github.mohamad4444.springtemplate.examples.data2.repository.NoteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NoteService {

	private NoteRepository noteRepository;

	public NoteService(NoteRepository noteRepository) {
		this.noteRepository = noteRepository;
	}

	// CREATE
	public NoteEntity create(String note, String tag) {
		final NoteEntity noteDocument = new NoteEntity(note, tag);

		return this.noteRepository.save(noteDocument);
	}

	// READ
	public Optional<NoteEntity> findById(Long id) {
		return this.noteRepository.findById(id);
	}
	
	public Page<NoteEntity> findByTag(String tag, Pageable pageable) {
		return this.noteRepository.findByTag(tag, pageable);
	}
	
	public Page<NoteEntity> getAll(Pageable pageable) {
		return this.noteRepository.findAll(pageable);
	}
	
	// UPDATE
	public NoteEntity update(NoteEntity noteDocument) {
		return this.noteRepository.save(noteDocument);
	}

	// DELETE
	public void deleteById(Long id) {
		this.noteRepository.deleteById(id);
	}

}
