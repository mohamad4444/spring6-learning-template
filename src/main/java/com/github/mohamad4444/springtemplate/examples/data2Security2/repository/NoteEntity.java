package com.github.mohamad4444.springtemplate.examples.data2Security2.repository;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "note")
public class NoteEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String note;
	private String tag;
	
	private UUID creatorId;
	
	public NoteEntity() {
	}
	
	public NoteEntity(String note, String tag, UUID creatorId) {
		super();
		this.note = note;
		this.tag = tag;
		this.creatorId = creatorId;
	}
	
	public NoteEntity(Long id, String note, String tag, UUID creatorId) {
		super();
		this.id = id;
		this.note = note;
		this.tag = tag;
		this.creatorId = creatorId;
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

	public UUID getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(UUID creatorId) {
		this.creatorId = creatorId;
	}
}
