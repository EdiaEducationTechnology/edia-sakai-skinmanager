/*
 * $Author: roland $
 * $Revision: 1142 $
 * $Date: 2007-04-05 08:51:35 +0200 (Thu, 05 Apr 2007) $
 * 
 * Edia Project edia-sakai-skin-manager
 * Copyright (C) 2007 Roland, Edia Educatie Technologie
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
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