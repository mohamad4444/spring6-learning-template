package com.github.mohamad4444.springtemplate.model;

import jakarta.persistence.*;

import java.util.UUID;
import com.github.mohamad4444.springtemplate.model.AdvancedNoteEntity;

@Entity
@Table(name = "note")
public class AdvancedNoteEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String note;
	private String tag;
	
	private UUID creatorId;
	
	public AdvancedNoteEntity() {
	}
	
	public AdvancedNoteEntity(String note, String tag, UUID creatorId) {
		super();
		this.note = note;
		this.tag = tag;
		this.creatorId = creatorId;
	}
	
	public AdvancedNoteEntity(Long id, String note, String tag, UUID creatorId) {
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
