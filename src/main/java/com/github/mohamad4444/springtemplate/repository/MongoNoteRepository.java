package com.github.mohamad4444.springtemplate.repository;

import org.springframework.data.repository.CrudRepository;
import com.github.mohamad4444.springtemplate.model.MongoNoteDocument;

public interface MongoNoteRepository extends CrudRepository<MongoNoteDocument, String> {

}
