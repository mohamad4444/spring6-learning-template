package com.github.mohamad4444.springtemplate.model;

import jakarta.persistence.*;

@Entity
@Table(name = "note")
public class SecuredNoteEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String note;
	private String tag;
	
	public SecuredNoteEntity() {
	}
	
	public SecuredNoteEntity(String note, String tag) {
		super();
		this.note = note;
		this.tag = tag;
	}
	
	public SecuredNoteEntity(Long id, String note, String tag) {
		super();
		this.id = id;
		this.note = note;
		this.tag = tag;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
