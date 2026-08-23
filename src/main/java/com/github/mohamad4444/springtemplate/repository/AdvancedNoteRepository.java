package com.github.mohamad4444.springtemplate.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.github.mohamad4444.springtemplate.model.AdvancedNoteEntity;

public interface AdvancedNoteRepository extends JpaRepository<AdvancedNoteEntity, Long>  {
	public Page<AdvancedNoteEntity> findByTag(String tag, Pageable pageable);
}
