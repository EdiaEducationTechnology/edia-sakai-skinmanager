/*
 * $Author: roland $
 * $Revision: 1143 $
 * $Date: 2007-04-05 23:59:06 +0200 (Thu, 05 Apr 2007) $
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

/**
 * Exception that occurs when illegally attempting to 
 * overwrite an existing skin.
 * @author Roland
 *
 */
public class CannotOverwriteException extends SkinException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5550767363907418031L;

	public CannotOverwriteException() {
		super();
	}

	public CannotOverwriteException(String message, Throwable cause) {
		super(message, cause);
	}

	public CannotOverwriteException(String message) {
		super(message);
	}

	public CannotOverwriteException(Throwable cause) {
		super(cause);
	}

}
