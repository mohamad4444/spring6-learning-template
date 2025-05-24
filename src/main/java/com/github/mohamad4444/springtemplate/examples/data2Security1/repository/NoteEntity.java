package com.github.mohamad4444.springtemplate.examples.data2Security1.repository;

import jakarta.persistence.*;

@Entity
@Table(name = "note")
public class NoteEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String note;
	private String tag;
	
	public NoteEntity() {
	}
	
	public NoteEntity(String note, String tag) {
		super();
		this.note = note;
		this.tag = tag;
	}
	
	public NoteEntity(Long id, String note, String tag) {
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
