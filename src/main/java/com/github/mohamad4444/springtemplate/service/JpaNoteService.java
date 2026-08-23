package com.github.mohamad4444.springtemplate.service;

import com.github.mohamad4444.springtemplate.model.JpaNoteEntity;
import com.github.mohamad4444.springtemplate.repository.JpaNoteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaNoteService {

	private JpaNoteRepository noteRepository;

	public JpaNoteService(JpaNoteRepository noteRepository) {
		this.noteRepository = noteRepository;
	}

	// CREATE
	public JpaNoteEntity create(String note, String tag) {
		final JpaNoteEntity noteDocument = new JpaNoteEntity(note, tag);

		return this.noteRepository.save(noteDocument);
	}

	// READ
	public Optional<JpaNoteEntity> findById(Long id) {
		return this.noteRepository.findById(id);
	}
	
	public Page<JpaNoteEntity> findByTag(String tag, Pageable pageable) {
		return this.noteRepository.findByTag(tag, pageable);
	}
	
	public Page<JpaNoteEntity> getAll(Pageable pageable) {
		return this.noteRepository.findAll(pageable);
	}
	
	// UPDATE
	public JpaNoteEntity update(JpaNoteEntity noteDocument) {
		return this.noteRepository.save(noteDocument);
	}

	// DELETE
	public void deleteById(Long id) {
		this.noteRepository.deleteById(id);
	}

}
