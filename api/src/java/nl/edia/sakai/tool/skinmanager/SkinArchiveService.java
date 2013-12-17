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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.sakaiproject.site.api.Site;

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
	
	/**
	 * Finds the sites that make use of the given skin
	 * @param isDefault if the skin is the default skin. If so, this will also return all sites with null skins.
	 * 
	 * @param name, name of the skin, not allowed to be null
	 * @return a set of sites that use the skin. Never to be null.
	 */
	public List<Site> findSites(String name, boolean isDefault);
}
