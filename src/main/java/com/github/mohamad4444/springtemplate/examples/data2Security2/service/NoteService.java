package com.github.mohamad4444.springtemplate.examples.data2Security2.service;

import com.github.mohamad4444.springtemplate.examples.data2Security2.repository.NoteEntity;
import com.github.mohamad4444.springtemplate.examples.data2Security2.repository.NoteRepository;
import com.github.mohamad4444.springtemplate.examples.data2Security2.security.AuthenticatedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@PreAuthorize("isAuthenticated()")
public class NoteService {

	private NoteRepository noteRepository;

	public NoteService(NoteRepository noteRepository) {
		this.noteRepository = noteRepository;
	}

	// CREATE
	@PreAuthorize("hasAuthority('WRITER')")
	public NoteEntity create(String note, String tag, AuthenticatedUser currentUser) {
		final NoteEntity noteDocument = new NoteEntity(note, tag, currentUser.getId());

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
	public NoteEntity update(Long id, String note, String tag, AuthenticatedUser currentUser) {
		final var noteEntity = this.noteRepository.getReferenceById(id);
		
		if(!noteEntity.getCreatorId().equals(currentUser.getId())) {
			throw new AccessDeniedException("Only the creator can update a note.");
		}
		
		noteEntity.setNote(note);
		noteEntity.setTag(tag);
		
		return this.noteRepository.save(noteEntity);
	}

	// DELETE
	public void deleteById(Long id, AuthenticatedUser currentUser) {
		final var noteEntity = this.noteRepository.getReferenceById(id);
		
		if(!currentUser.getRoles().contains("ADMIN") && !noteEntity.getCreatorId().equals(currentUser.getId())) {
			throw new AccessDeniedException("Only the creator or an ADMIN can delete a note.");
		}
		
		this.noteRepository.delete(noteEntity);
	}

}
