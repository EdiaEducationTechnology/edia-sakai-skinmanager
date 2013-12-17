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
