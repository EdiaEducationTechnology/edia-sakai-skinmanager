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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>Represents a skin directory in a skin package, please note that 
 * the set children are all {@link SkinFile}s and do not contain directories.
 * </p>
 * <p>The abstract paths of the files will contain their directory names.</p>
 * 
 * For example:
 * <pre>
 * tools.css
 * images/logo.gif
 * </pre>
 * are all legal filenames.
 * 
 */
public class SkinDirectory implements Serializable {

	private static final long serialVersionUID = 8892247180002016647L;
	
	/** 
	 * The relative name of the skin directory.
	 */
	String name;
	
	/**
	 * The timestamp that represents the last modification time.
	 */
	Date lastModified;
	
	/**
	 * The children of the directory.
	 */
	List<SkinFile> files;
	
	/**
	 * Gets children of the directory
	 * @return
	 */
	public List<SkinFile> getFiles() {
		return files;
	}
	
	/**
	 * Sets children of the directory
	 * @param files
	 */
	public void setFiles(List<SkinFile> files) {
		this.files = files;
	}
	
	/**
	 * Gets timestamp that represents the last modification time.
	 * @return
	 */
	public Date getLastModified() {
		return protectDate(lastModified);
	}
	
	/**
	 * Sets timestamp that represents the last modification time.
	 * @param lastModified
	 */
	public void setLastModified(Date lastModified) {
		this.lastModified = protectDate(lastModified);
	}
	
	/**
	 * Gets relative name of the skin directory.
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets relative name of the skin directory.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the size of all the children of this directory.
	 * @return
	 */
	public Long getSize() {
		long mySize = 0;
		for (SkinFile skinFile : files) {
			mySize += skinFile.getSize();
		}
		return mySize;
	}
	
	private Date protectDate(Date date) {
		if (date != null) {
			return new Date(date.getTime());
		}
		return null;
	}

}