package com.github.mohamad4444.springtemplate.examples.data1.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="notes")
public class NoteDocument {
	@Id
	private String id;

	private String note;
	private String tag;
	
	public NoteDocument() {
	}
	
	public NoteDocument(String note, String tag) {
		super();
		this.note = note;
		this.tag = tag;
	}
	
	public NoteDocument(String id, String note, String tag) {
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
