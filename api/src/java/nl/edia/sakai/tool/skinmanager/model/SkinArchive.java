package nl.edia.sakai.tool.skinmanager.model;

import java.sql.Blob;
import java.sql.Timestamp;


public class SkinArchive implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2232986657622403826L;
	
	Long id;
	
	String name;
	
	Timestamp lastModified;
	
	int version;
	
	String comment;
	
	private Blob file;
	
	boolean active = true;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getLastModified() {
		return lastModified;
	}

	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Blob getFile() {
		return file;
	}

	public void setFile(Blob file) {
		this.file = file;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
