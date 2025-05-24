package com.github.mohamad4444.springtemplate.examples.data2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<NoteEntity, Long>  {
	public Page<NoteEntity> findByTag(String tag, Pageable pageable);
}
