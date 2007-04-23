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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import nl.edia.sakai.tool.skinmanager.model.SkinArchive;
import nl.edia.sakai.tool.skinmanager.model.SkinDirectory;

import org.sakaiproject.site.api.Site;
/**
 * 
 * @author Roland
 *
 */
public interface SkinService {
	/**
	 * <p>
	 * Gets all installed skins. These are all skins that are installed on the 
	 * file system.
	 * </p>
	 * @return
	 * @throws SkinException, in case of any configuration problem, usually when the skin dir cannot
	 * be found or is unreadable.
	 * Should be treated as expected with respect to the business logic.
	 * @throws IOException, in case of any IO error. Should be handled as unexpected and unrecovable. 
	 */
	public List<SkinDirectory> fetchInstalledSkins() throws SkinException, IOException;
	
	/**
	 * Gets the archive for a given skin
	 */
	public SkinArchive fetchSkinArchive(String name) throws SkinException, IOException;
	
	/**
	 * <p>
	 * Find a skin by id / name
	 * </p>
	 * @param name
	 * @return
	 * @throws IOException 
	 * @throws SkinException 
	 */
	public SkinDirectory findSkin(String id) throws SkinException, IOException;
	
	/**
	 * <p>
	 * Creates a new skin, with the given name and containing the contents of the file.
	 * An archive record of this skin is created. 
	 * </p>
	 * @param name
	 * @param file
	 * @throws SkinException, in case of an existing skin with the same name, or an illegal attempt 
	 * to overwrite an existing skin.
	 * Should be treated as expected with respect to the business logic.
	 * @throws IOException, in case of any IO error. Should be handled as unexpected and unrecovable. 
	 */
	public void createSkin(String name, InputStream data) throws SkinException, IOException;
	/**
	 * <p>
	 * Updates and overwrites an existing skin. The current skin is removed completely from the 
	 * file system and a archive of the new skin is created.
	 * </p>
	 * @param name
	 * @param file
	 * @throws SkinException in case of any illegal state or operation. 
	 * Should be treated as expected with respect to the business logic.
	 * @throws IOException, in case of any IO error. Should be handled as unexpected and unrecovable. 
	 */
	public void updateSkin(String name, InputStream data) throws SkinException, IOException;
	/**
	 * <p>
	 * Removes an existing skin from the file system and makes it invisbile 
	 * to sakai. 
	 * This does not purge the actual  archive, only set the active flag to false. 
	 * This means that the skin archive is  not removed, only made invisible. 
	 * The archive can be restored by creating a skin  with the same name.
	 * </p>
	 * @param name
	 * @throws SkinException, in case of any illegal state or operation.
	 * Should be treated as expected with respect to the business logic. 
	 * {@link SkinInUseException} if the skin is in use and cannot be deleted.
	 * @throws IOException, in case of any IO error. Should be handled as unexpected and unrecovable. 
	 */
	public void removeSkin(String name) throws SkinException, IOException;
	
	/**
	 * <p>
	 * Check if a skin is in use. This method returns true if there is at least a site
	 * that uses this specific skin. Sites without a specific skin set use the default
	 * skin. The default skin is called "default", if not defined in sakai.properties.
	 * This unset skin is also taken in account.
	 * </p>
	 * @param skin
	 * @return true is the skin is used by any site
	 * @throws ActionNotAlowedException 
	 */
	public boolean isInUse(String skin) throws ActionNotAlowedException;
	/**
	 * Returns a list of sites that are used by the skin
	 * @param skin
	 * @return
	 */
	public List<Site> findSites(String skin);
	
	/**
	 * <p>
	 * Finds the history of a skin. Returns the list of archives of a certain skin.
	 * </p>
	 * @param name
	 * @return
	 * @throws ActionNotAlowedException 
	 */
	public List<SkinArchive> findSkinHistory(String name) throws ActionNotAlowedException;
	
	/**
	 * <p>
	 * Restores a skin to a given version. If the version does not exist, this method
	 * dies silently.
	 * </p>
	 * @param name
	 * @param version, the specific version to restore, if the version is invalid, this 
	 * method dies silently.
	 * @throws ActionNotAlowedException 
	 * @throws IOException 
	 * @throws SkinException 
	 */
	public void restoreSkinHistory(String name, int version) throws ActionNotAlowedException, SkinException, IOException;

}
