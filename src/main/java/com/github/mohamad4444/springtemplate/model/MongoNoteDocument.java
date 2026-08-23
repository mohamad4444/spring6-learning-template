package com.github.mohamad4444.springtemplate.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.github.mohamad4444.springtemplate.model.MongoNoteDocument;

@Document(collection="notes")
public class MongoNoteDocument {
	@Id
	private String id;

	private String note;
	private String tag;
	
	public MongoNoteDocument() {
	}
	
	public MongoNoteDocument(String note, String tag) {
		super();
		this.note = note;
		this.tag = tag;
	}
	
	public MongoNoteDocument(String id, String note, String tag) {
		super();
		this.id = id;
		this.note = note;
		this.tag = tag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	
}
