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
