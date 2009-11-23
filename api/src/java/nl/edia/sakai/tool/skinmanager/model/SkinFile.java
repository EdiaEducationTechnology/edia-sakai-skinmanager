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

/**
 * This class represents a abstract file in a skin package, and is used as a 
 * value object for visualization. The abtract file name may contain a subdirectory.
 */

public class SkinFile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1416603761213678448L;
	/**
	 * The relative file name, may contain a directory:
	 * <pre>
	 * tool.css
	 * images/logo.gif
	 * </pre>
	 * are all legal file names.
	 */
	String name;
	/**
	 * The timestamp that represents the last modification time.
	 */
	Date lastModified;
	
	/**
	 * The file size in bytes
	 */
	Long size;
	
	/**
	 * Gets the timestamp that represents the last modification time.
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
	 * Gets relative file name, may contain a directory:
	 * <pre>
	 * tool.css
	 * images/logo.gif
	 * </pre>
	 * are all legal file names.
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets relative file name, may contain a directory:
	 * <pre>
	 * tool.css
	 * images/logo.gif
	 * </pre>
	 * are all legal file names.
	 * 
	 * @param name, the relative file name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the file size in bytes
	 * @return
	 */
	public Long getSize() {
		return size;
	}
	
	/**
	 * Sets the file size in bytes
	 * @param size
	 */
	public void setSize(Long size) {
		this.size = size;
	}

	private Date protectDate(Date date) {
		if (date != null) {
			return new Date(date.getTime());
		}
		return null;
	}
}
