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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import nl.edia.sakai.tool.skinmanager.model.SkinArchive;
/**
 * Skin archive service, the storage of binary skin data archives (zipped)
 * in the database for archiving puproses.
 * @author Roland
 *
 */
public interface SkinArchiveService {
	/**
	 * <p>
	 * Fetches all active skins.
	 * </p>
	 * @return a list of active skins.
	 */
	public List<SkinArchive> fetchActiveSkinArchives();
	/**
	 * <p>
	 * Finds skin archive by name / id
	 * </p>
	 * @param name name / id of the skin.
	 * @return {@link SkinArchive} if found, else null.
	 */
	public SkinArchive findSkinArchive(String name);

	/**
	 * <p>
	 * Finds skin archive by id / name and version.
	 * </p>
	 * @param name name name / id of the skin.
	 * @param version version to search for.
	 * @return {@link SkinArchive} if found, else null.
	 */
	public SkinArchive findSkinArchive(String name, int version);

	/**
	 * <p>
	 * Finds history of skins ever created, with the same name.
	 * </p>
	 * @param name name name / id of the skin.
	 * @return a list of {@link SkinArchive}s, ordered by version.
	 */
	public List<SkinArchive> findSkinHistory(String name);

	/**
	 * <p>
	 * Creates a record of a skin in the skin archive. The version number is incremented
	 * from the highest known.
	 * </p>
	 * @param name the name / id of the skin
	 * @param file the data of the skin to be archived
	 * @param time the timestamp of the arhive to be created.
	 * @param comment the comment, usually the reason to update.
	 * @return the created instance of {@link SkinArchive}
	 */
	public SkinArchive createSkinArchive(String name, InputStream file, Date time, String comment);
	
	/**
	 * <p>
	 * Gets the data of the skin and writes it to the output string. Please
	 * note that the binary data can only be read/written within an active database
	 * transaction.
	 * This is why the method accepts a output stream and does not return a input stream 
	 * as one would expect.
	 * </p>
	 * @param name
	 * @param out
	 */
	public void fetchSkinArchiveData(String name, OutputStream out);
	
	/**
	 * <p>
	 * Gets the data of a specific version of the skin and writes it to the output string. 
	 * Please note that the binary data can only be read/written within an active database
	 * transaction.
	 * This is why the method accepts a output stream and does not return a input stream 
	 * as one would expect.
	 * </p>
	 * @param name
	 * @param version
	 * @param out
	 */
	public void fetchSkinArchiveData(String name, int version, OutputStream out);
	
	/**
	 * Disables a skin archive, sets the active flag to false, does not purge the 
	 * actual skin archive.
	 * @param name
	 */
	public void removeSkinArchive(String name);
}
