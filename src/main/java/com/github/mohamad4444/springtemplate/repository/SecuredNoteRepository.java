package com.github.mohamad4444.springtemplate.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.github.mohamad4444.springtemplate.model.SecuredNoteEntity;

public interface SecuredNoteRepository extends JpaRepository<SecuredNoteEntity, Long>  {
	public Page<SecuredNoteEntity> findByTag(String tag, Pageable pageable);
}
