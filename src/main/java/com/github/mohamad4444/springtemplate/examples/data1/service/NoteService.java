package com.github.mohamad4444.springtemplate.examples.data1.service;

import com.github.mohamad4444.springtemplate.examples.data1.repository.MongoNoteRepository;
import com.github.mohamad4444.springtemplate.examples.data1.repository.NoteDocument;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NoteService {

	private MongoNoteRepository noteRepository;

	public NoteService(MongoNoteRepository noteRepository) {
		this.noteRepository = noteRepository;
	}

	// CREATE
	public NoteDocument create(String note, String tag) {
		final NoteDocument noteDocument = new NoteDocument(note, tag);

		return this.noteRepository.save(noteDocument);
	}

	// READ
	public Optional<NoteDocument> findById(String id) {
		return this.noteRepository.findById(id);
	}

	public Iterable<NoteDocument> getAll() {
		return this.noteRepository.findAll();
	}
	
	// UPDATE
	public NoteDocument update(NoteDocument noteDocument) {
		return this.noteRepository.save(noteDocument);
	}

	// DELETE
	public void deleteById(String id) {
		this.noteRepository.deleteById(id);
	}

}
