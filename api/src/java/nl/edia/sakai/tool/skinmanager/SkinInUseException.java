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
 * Skin is in use by a site. Cannot be deteled.
 * @author Roland
 *
 */
public class SkinInUseException extends SkinException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1670139642975466473L;

	/**
	 * 
	 */
	public SkinInUseException() {
	}

	/**
	 * @param message
	 */
	public SkinInUseException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public SkinInUseException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SkinInUseException(String message, Throwable cause) {
		super(message, cause);
	}

}
