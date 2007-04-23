/*
 * $Author: roland $
 * $Revision: 945 $
 * $Date: 2007-03-18 22:52:48 +0100 (zo, 18 mrt 2007) $
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import nl.edia.sakai.tool.skinmanager.model.SkinDirectory;
/**
 * The SkinFileSystemService interface defines the skin manager file system service.
 * Although backed by the file system, it reflects as much as possible
 * any other persistent API.
 * @author Roland
 *
 */
public interface SkinFileSystemService {
	/**
	 * Gets the installed skins.
	 * @return
	 * @throws SkinException, in case of any confuration problem, usually when the skin dir cannot
	 * be found or is unreadable.
	 * Should be treated as expected with respect to the business logic.
	 * @throws IOException, in case of any IO error. Should be handled as unexpected and unrecovable. 
	 */
	public List<SkinDirectory> fetchInstalledSkins() throws SkinException, IOException;
	
	/**
	 * Find a skin by id / name
	 * @param name
	 * @return
	 * @throws IOException 
	 * @throws SkinException 
	 */
	public SkinDirectory findSkin(String id) throws SkinException, IOException;
	
	/**
	 * Creates a new skin, with the given name and containing the contents of the file.
	 * @param name
	 * @param file
	 * @throws SkinException, in case of an existing skin with the same name, or an illegal attempt 
	 * to overwrite an existing skin.
	 * Should be treated as expected with respect to the business logic.
	 * @throes IOException, in case of any IO error. Should be handled as unexpected and unrecovable. 
	 */
	public void createSkin(String name, InputStream data) throws SkinException, IOException;
	/**
	 * Updates and overwrites an existing skin. The current skin is removed completely.
	 * @param name
	 * @param file
	 * @throws SkinException in case of any illegal state or operation. 
	 * Should be treated as expected with respect to the business logic.
	 * @throws IOException, in case of any IO error. Should be handled as unexpected and unrecovable. 
	 */
	public void updateSkin(String name, InputStream data) throws SkinException, IOException;
	/**
	 * Removes an existing skin.
	 * @param name
	 * @throws SkinException, in case of any illegal state or operation.
	 * Should be treated as expected with respect to the business logic. 
	 * {@link SkinInUseException} if the skin is in use and cannot be deleted.
	 * @throws IOException, in case of any IO error. Should be handled as unexpected and unrecovable. 
	 */
	public void removeSkin(String name) throws SkinException, IOException;
	/**
	 * Find a skin and create a zip file from it.
	 * @param name
	 * @param out TODO
	 * @throws SkinException
	 * @throws IOException
	 */
	public void writeSkinData(String name, OutputStream out) throws SkinException, IOException;

}
