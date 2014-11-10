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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
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
         * @param id
	 * @return
	 * @throws IOException 
	 * @throws SkinException 
	 */
	public SkinDirectory findSkin(String id) throws SkinException, IOException;
	
	/**
	 * Creates a new skin, with the given name and containing the contents of the file.
	 * @param name
	 * @param data
	 * @param date
         * @param overwrite
	 * @throws SkinException, in case of an existing skin with the same name, or an illegal attempt 
	 * to overwrite an existing skin.
	 * Should be treated as expected with respect to the business logic.
         * @throws IOException, in case of any IO error. Should be handled as unexpected and unrecovable. 
	 */
	public void createSkin(String name, InputStream data, Date date, boolean overwrite) throws SkinException, IOException;
	/**
	 * Updates and overwrites an existing skin. The current skin is removed completely.
	 * @param name
         * @param date
         * @param data
	 * @throws SkinException in case of any illegal state or operation. 
	 * Should be treated as expected with respect to the business logic.
	 * @throws IOException, in case of any IO error. Should be handled as unexpected and unrecovable. 
	 */
	public void updateSkin(String name, Date date, InputStream data) throws SkinException, IOException;
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
