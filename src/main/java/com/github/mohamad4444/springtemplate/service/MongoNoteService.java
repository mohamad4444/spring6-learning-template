package com.github.mohamad4444.springtemplate.service;

import com.github.mohamad4444.springtemplate.repository.MongoNoteRepository;
import com.github.mohamad4444.springtemplate.model.MongoNoteDocument;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MongoNoteService {

	private MongoNoteRepository noteRepository;

	public MongoNoteService(MongoNoteRepository noteRepository) {
		this.noteRepository = noteRepository;
	}

	// CREATE
	public MongoNoteDocument create(String note, String tag) {
		final MongoNoteDocument noteDocument = new MongoNoteDocument(note, tag);

		return this.noteRepository.save(noteDocument);
	}

	// READ
	public Optional<MongoNoteDocument> findById(String id) {
		return this.noteRepository.findById(id);
	}

	public Iterable<MongoNoteDocument> getAll() {
		return this.noteRepository.findAll();
	}

	// UPDATE
	public MongoNoteDocument update(MongoNoteDocument noteDocument) {
		return this.noteRepository.save(noteDocument);
	}

	// DELETE
	public void deleteById(String id) {
		this.noteRepository.deleteById(id);
	}

}
