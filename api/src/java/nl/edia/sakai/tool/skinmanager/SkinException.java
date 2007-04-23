/*
 * $Author: roland $
 * $Revision: 927 $
 * $Date: 2007-03-16 16:26:34 +0100 (vr, 16 mrt 2007) $
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

public class SkinException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7078857106748207231L;

	public SkinException() {
	}

	public SkinException(String message) {
		super(message);
	}

	public SkinException(Throwable cause) {
		super(cause);
	}

	public SkinException(String message, Throwable cause) {
		super(message, cause);
	}

}
