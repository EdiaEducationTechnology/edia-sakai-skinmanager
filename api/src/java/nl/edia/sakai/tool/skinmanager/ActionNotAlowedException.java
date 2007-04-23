/*
 * $Author: roland $
 * $Revision: 1098 $
 * $Date: 2007-04-01 01:27:40 +0200 (zo, 01 apr 2007) $
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
package nl.edia.sakai.tool.skinmanager;

public class ActionNotAlowedException extends SkinException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1465679734690344179L;

	public ActionNotAlowedException() {
		super();
	}

	public ActionNotAlowedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActionNotAlowedException(String message) {
		super(message);
	}

	public ActionNotAlowedException(Throwable cause) {
		super(cause);
	}

}
