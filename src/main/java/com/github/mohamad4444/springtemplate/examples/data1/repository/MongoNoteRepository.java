package com.github.mohamad4444.springtemplate.examples.data1.repository;

import org.springframework.data.repository.CrudRepository;

public interface MongoNoteRepository extends CrudRepository<NoteDocument, String> {

}
