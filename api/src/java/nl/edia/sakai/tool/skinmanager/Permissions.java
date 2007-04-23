/*
 * $Author: roland $
 * $Revision: 1143 $
 * $Date: 2007-04-05 23:59:06 +0200 (do, 05 apr 2007) $
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
 * Convenience interface with all the permissions used by the tool.
 * @author Roland
 *
 */
public interface Permissions {
	/**
	 * Permission to view skins.
	 */
	public static final String PERMISSION_EDIA_SAKAI_SKININSTALL_VIEW = "skinmanager.view";
	
	/**
	 * Permsission to create skins
	 */
	public static final String PERMISSION_EDIA_SAKAI_SKININSTALL_CREATE = "skinmanager.create";
	
	/**
	 * Permission to update / edit skins
	 */
	public static final String PERMISSION_EDIA_SAKAI_SKININSTALL_EDIT = "skinmanager.edit";
	
	/**
	 * Permission to remove skins
	 */
	public static final String PERMISSION_EDIA_SAKAI_SKININSTALL_DELETE = "skinmanager.delete";

}
