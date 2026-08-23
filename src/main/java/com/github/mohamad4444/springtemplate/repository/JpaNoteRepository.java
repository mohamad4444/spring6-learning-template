package com.github.mohamad4444.springtemplate.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.github.mohamad4444.springtemplate.model.JpaNoteEntity;

public interface JpaNoteRepository extends JpaRepository<JpaNoteEntity, Long>  {
	public Page<JpaNoteEntity> findByTag(String tag, Pageable pageable);
}
