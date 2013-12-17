/*
 * #%L
 * Edia Skin Manager Model API
 * %%
 * Copyright (C) 2007 - 2013 Edia
 * %%
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *             http://opensource.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package nl.edia.sakai.tool.skinmanager.model;

import java.sql.Blob;
import java.sql.Timestamp;

/**
 * This class represents a skin archive persisted in the database.
 * The binary blob contains the zipped skin archive.
 * @author Roland
 *
 */
public class SkinArchive implements java.io.Serializable {
	/**
	 * The serial version unique identifier 
	 */
	private static final long serialVersionUID = -2232986657622403826L;
	
	/**
	 * Primary key of the object
	 */
	Long id;
	/**
	 * The logical name of the skin archive
	 */
	String name;
	/**
	 * The timestamp that represents the last modification time.
	 */
	Timestamp lastModified;
	
	/**
	 * The version of the skin, is incremented by each modification
	 */
	int version;
	
	/**
	 * The comment, usually bound to the {@link #version}.
	 */
	String comment;
	
	/**
	 * The binary representation of the skin archive.
	 */
	private Blob file;
	
	/**
	 * True if the skin exists, false if the skin is deleted.
	 */
	boolean active = true;

	/**
	 * Gets active flag
	 * @return true if the skin exists, false if the skin is deleted.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets active flag
	 * @param active true if the skin exists, false if the skin is deleted.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Gets the primary key of the object
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the primary key of the object
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the timestamp that represents the last modification time.
	 * @return
	 */
	public Timestamp getLastModified() {
		return lastModified;
	}

	/**
	 * Sets the timestamp that represents the last modification time.
	 * @param lastModified
	 */
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * Get the logical name of the skin archive
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the logical name of the skin archive
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the version of the skin, is incremented by each modification
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Sets the version of the skin, is incremented by each modification
	 * @param version, the version
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * <p>
	 * Gets the binary representation of the skin archive.
	 * </p>
	 * <p>
	 * Please note that the data from the blob is only readable and writable
	 * when the database transaction is open. 
	 * </p>
	 * @return the binary blob
	 */
	public Blob getFile() {
		return file;
	}

	/**
	 * <p>
	 * Sets the binary representation of the skin archive.
	 * </p>
	 * <p>
	 * Please note that the data from the blob is only readable and writable
	 * when the database transaction is open. 
	 * </p>
	 * @param file
	 */
	public void setFile(Blob file) {
		this.file = file;
	}

	/**
	 * Gets the comment, usually bound to the {@link #version}.
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Sets the comment, usually bound to the {@link #version}.
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
