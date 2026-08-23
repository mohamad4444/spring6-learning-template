package com.github.mohamad4444.springtemplate.service;

import com.github.mohamad4444.springtemplate.model.AdvancedNoteEntity;
import com.github.mohamad4444.springtemplate.repository.AdvancedNoteRepository;
import com.github.mohamad4444.springtemplate.security.AdvancedAuthenticatedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@PreAuthorize("isAuthenticated()")
public class AdvancedNoteService {

	private AdvancedNoteRepository noteRepository;

	public AdvancedNoteService(AdvancedNoteRepository noteRepository) {
		this.noteRepository = noteRepository;
	}

	// CREATE
	@PreAuthorize("hasAuthority('WRITER')")
	public AdvancedNoteEntity create(String note, String tag, AdvancedAuthenticatedUser currentUser) {
		final AdvancedNoteEntity noteDocument = new AdvancedNoteEntity(note, tag, currentUser.getId());

		return this.noteRepository.save(noteDocument);
	}

	// READ
	public Optional<AdvancedNoteEntity> findById(Long id) {
		return this.noteRepository.findById(id);
	}
	
	public Page<AdvancedNoteEntity> findByTag(String tag, Pageable pageable) {
		return this.noteRepository.findByTag(tag, pageable);
	}
	
	public Page<AdvancedNoteEntity> getAll(Pageable pageable) {
		return this.noteRepository.findAll(pageable);
	}
	
	// UPDATE
	public AdvancedNoteEntity update(Long id, String note, String tag, AdvancedAuthenticatedUser currentUser) {
		final var noteEntity = this.noteRepository.getReferenceById(id);
		
		if(!noteEntity.getCreatorId().equals(currentUser.getId())) {
			throw new AccessDeniedException("Only the creator can update a note.");
		}
		
		noteEntity.setNote(note);
		noteEntity.setTag(tag);
		
		return this.noteRepository.save(noteEntity);
	}

	// DELETE
	public void deleteById(Long id, AdvancedAuthenticatedUser currentUser) {
		final var noteEntity = this.noteRepository.getReferenceById(id);
		
		if(!currentUser.getRoles().contains("ADMIN") && !noteEntity.getCreatorId().equals(currentUser.getId())) {
			throw new AccessDeniedException("Only the creator or an ADMIN can delete a note.");
		}
		
		this.noteRepository.delete(noteEntity);
	}

}
