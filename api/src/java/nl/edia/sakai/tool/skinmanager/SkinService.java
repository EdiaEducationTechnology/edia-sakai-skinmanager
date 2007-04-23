package nl.edia.sakai.tool.skinmanager;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import nl.edia.sakai.tool.skinmanager.model.SkinArchive;
import nl.edia.sakai.tool.skinmanager.model.SkinDirectory;

import org.sakaiproject.site.api.Site;

public interface SkinService {
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
	 * Gets the archive for a given skin
	 */
	public SkinArchive fetchSkinArchive(String name) throws SkinException, IOException;
	
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
	 * Check if a skin is in use
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
	 * Finds the history of a skin.
	 * @param name
	 * @return
	 * @throws ActionNotAlowedException 
	 */
	public List<SkinArchive> findSkinHistory(String name) throws ActionNotAlowedException;
	
	/**
	 * Restores a skin to a given version.
	 * @param name
	 * @param version
	 * @throws ActionNotAlowedException 
	 * @throws IOException 
	 * @throws SkinException 
	 */
	public void restoreSkinHistory(String name, int version) throws ActionNotAlowedException, SkinException, IOException;

}
