package nl.edia.sakai.tool.skinmanager.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.edia.sakai.tool.skinmanager.ActionNotAlowedException;
import nl.edia.sakai.tool.skinmanager.Permissions;
import nl.edia.sakai.tool.skinmanager.SkinInUseException;
import nl.edia.sakai.tool.skinmanager.SkinArchiveService;
import nl.edia.sakai.tool.skinmanager.SkinException;
import nl.edia.sakai.tool.skinmanager.SkinFileSystemService;
import nl.edia.sakai.tool.skinmanager.SkinPrerequisitesNonFatalException;
import nl.edia.sakai.tool.skinmanager.SkinService;
import nl.edia.sakai.tool.skinmanager.model.SkinArchive;
import nl.edia.sakai.tool.skinmanager.model.SkinDirectory;
import nl.edia.sakai.tool.util.SakaiUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.authz.cover.FunctionManager;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.cover.SiteService;

public class SkinServiceImpl implements SkinService {

	private static final int WAIT_INTERVAL_RETRY = 30 * 1000;

	SkinFileSystemService skinFileSystemService;

	SkinArchiveService skinArchiveService;

	boolean isArchiveLeading = true;
	
	static Log log = LogFactory.getLog(SkinServiceImpl.class);

	private Thread theSyncThread;

	public void createSkin(String name, InputStream data) throws SkinException, IOException {
		checkAction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_CREATE);

		byte[] myFileData = readStream(data);

		skinFileSystemService.createSkin(name, new ByteArrayInputStream(myFileData));
		createArchive(name, myFileData);

		SakaiUtils.spawnModificationEvent(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_CREATE, "skin:" + name);
	}

	public List<SkinDirectory> fetchInstalledSkins() throws SkinException, IOException {
		checkAction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_VIEW);
		return skinFileSystemService.fetchInstalledSkins();
	}

	public SkinArchive fetchSkinArchive(String name) throws SkinException, IOException {
		return skinArchiveService.findSkinArchive(name);
	}

	public List<Site> findSites(String name) {
		List mySites = SiteService.getSites(org.sakaiproject.site.api.SiteService.SelectionType.ACCESS, null, null,
				null, org.sakaiproject.site.api.SiteService.SortType.TITLE_ASC, null);

		List<Site> myUsingSites = new ArrayList<Site>();
		Iterator myIterator = mySites.iterator();
		while (myIterator.hasNext()) {
			Site mySite = (Site) myIterator.next();
			if (equalsName(name, mySite)) {
				myUsingSites.add(mySite);
			}
		}
		return myUsingSites;
	}

	public SkinDirectory findSkin(String id) throws SkinException, IOException {
		checkAction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_VIEW);
		SakaiUtils.spawnEvent(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_VIEW, "skin:" + id);
		return skinFileSystemService.findSkin(id);
	}

	public List<SkinArchive> findSkinHistory(String name) throws ActionNotAlowedException {
		checkAction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_VIEW);
		return skinArchiveService.findSkinHistory(name);
	}

	public SkinArchiveService getSkinArchiveService() {
		return skinArchiveService;
	}

	public SkinFileSystemService getSkinFileSystemService() {
		return skinFileSystemService;
	}

	public void init() throws Exception {
		FunctionManager.registerFunction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_VIEW);
		FunctionManager.registerFunction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_CREATE);
		FunctionManager.registerFunction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_EDIT);
		FunctionManager.registerFunction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_DELETE);
		
		Runnable myTask = new Runnable() {
			public void run() {
				boolean isFinished = false;
				boolean isSuccessFul = false;
				while(!isFinished) {
					try {
						syncDatabaseAndFileSystem();
						isFinished = true;
						isSuccessFul = true;
					} catch (SkinPrerequisitesNonFatalException e) {
						log.warn("Non-fatal problem attempting to init the skin manager service, retrying in " + (WAIT_INTERVAL_RETRY/1000) + " seconds", e);
						synchronized (this) {
							try {
								this.wait(WAIT_INTERVAL_RETRY);
							} catch (InterruptedException e1) {
								isFinished = true;
							}
						}
						isFinished = false;
					} catch (SkinException e) {
						log.error("Fatal problem attempting to init the skin manager service", e);
						isFinished = true;
					} catch (IOException e) {
						log.error("Fatal problem attempting to init the skin manager service", e);
						isFinished = true;
					}
				}
				if (isSuccessFul) {
					log.info("Skin manager service init() ok");
				} else {
					log.warn("Skin manager service init() failed");
				}
			}
		};
		
		theSyncThread = new Thread(myTask, "Skin sync thread");
		// Die if you are the only one...
		theSyncThread.setDaemon(true);
		// Start the thread
		theSyncThread.start();
		
	}
	
	public void destroy() {
		if (theSyncThread != null && theSyncThread.isAlive())  {
			theSyncThread.interrupt();
		}
	}

	protected void syncDatabaseAndFileSystem() throws SkinException, IOException {
		List<String> myInstalledSkinNames = new ArrayList<String>();
		// Add new skins to the archive
		for (SkinDirectory mySkinDirectory : skinFileSystemService.fetchInstalledSkins()) {
			String myName = mySkinDirectory.getName();
			myInstalledSkinNames.add(myName);
			SkinArchive mySkinArchive = skinArchiveService.findSkinArchive(myName);
			if (mySkinArchive == null) {
				ByteArrayOutputStream myOutputStream = new ByteArrayOutputStream();
				skinFileSystemService.writeSkinData(myName, myOutputStream);
				ByteArrayInputStream myInputStream = new ByteArrayInputStream(myOutputStream.toByteArray());
				skinArchiveService.createSkinArchive(myName, myInputStream, mySkinDirectory.getLastModified(), "Syncronzied with filesystem");
				log.info("Skin archive created from filesystem: " + mySkinDirectory.getName());
			}
		}
		// Disable/restore skins that disappeared
		for (SkinArchive myArchive : skinArchiveService.fetchActiveSkinArchives()) {
			String myName = myArchive.getName();
			if (!myInstalledSkinNames.contains(myName)) {
				if (isArchiveLeading) {
					ByteArrayOutputStream myOutputStream = new ByteArrayOutputStream();
					skinArchiveService.fetchSkinArchiveData(myName, myOutputStream);
					skinFileSystemService.createSkin(myName, new ByteArrayInputStream(myOutputStream
							.toByteArray()));
					log.warn("Skin archive missing on filesystem, installed on FS: " + myName);
				} else {
					log.info("Skin archive removed form DB / not on filesystem: " + myName);
					skinArchiveService.removeSkinArchive(myName);
				}
			}
		}
	}

	public boolean isInUse(String name) throws ActionNotAlowedException {
		checkAction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_VIEW);
		List mySites = SiteService.getSites(org.sakaiproject.site.api.SiteService.SelectionType.ACCESS, null, null,
				null, org.sakaiproject.site.api.SiteService.SortType.TITLE_ASC, null);

		Iterator myIterator = mySites.iterator();
		while (myIterator.hasNext()) {
			Site mySite = (Site) myIterator.next();
			if (equalsName(name, mySite)) {
				return true;
			}
		}
		return false;
	}

	public void removeSkin(String name) throws SkinException, IOException {
		checkAction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_DELETE);
		if (!isInUse(name)) {
			skinFileSystemService.removeSkin(name);
			skinArchiveService.removeSkinArchive(name);
			SakaiUtils.spawnModificationEvent(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_DELETE, "skin:" + name);
		} else {
			throw new SkinInUseException("SkinDirectory '" + name + "' is in use and cannot be deleted");
		}

	}

	public void restoreSkinHistory(String name, int version) throws SkinException, IOException {
		checkAction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_EDIT);
		SkinArchive myLatestSkinArchive = skinArchiveService.findSkinArchive(name);
		if (myLatestSkinArchive != null) {
			int myLatestVersion = myLatestSkinArchive.getVersion();
			if (version < myLatestVersion) {
				ByteArrayOutputStream myOutputStream = new ByteArrayOutputStream();
				skinArchiveService.fetchSkinArchiveData(name, version, myOutputStream);
				updateSkin(name, myOutputStream.toByteArray(), "Reverted to version " + version);
			}
		}
	}

	public void setSkinArchiveService(SkinArchiveService skinArchiveService) {
		this.skinArchiveService = skinArchiveService;
	}

	public void setSkinFileSystemService(SkinFileSystemService skinFileSystemService) {
		this.skinFileSystemService = skinFileSystemService;
	}

	public void updateSkin(String name, InputStream data) throws SkinException, IOException {
		updateSkin(name, readStream(data), null);
	}

	protected void checkAction(String myAction) throws ActionNotAlowedException {
		try {
			SakaiUtils.checkPermission(myAction);
		} catch (PermissionException p) {
			throw new ActionNotAlowedException("You are not allowed to execute this action");
		}
	}

	protected boolean equalsName(String name, Site mySite) {
		String mySkin = mySite.getSkin();
		
		String myDefaultSkinName = ServerConfigurationService.getString("skin.default");
		if (myDefaultSkinName == null) {
			myDefaultSkinName = "default";
		}
		if (myDefaultSkinName.equals(name)) {
			return mySkin == null || myDefaultSkinName.equals(mySkin) || "".equals(mySkin);
		}
		return name.equals(mySkin);
	}

	protected byte[] readStream(InputStream data) throws IOException {
		ByteArrayOutputStream myOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int read;
		while ((read = data.read(buffer)) != -1) {
			myOutputStream.write(buffer, 0, read);
		}
		data.close();
		return myOutputStream.toByteArray();

	}

	private void createArchive(String name, byte[] myFileData) throws SkinException, IOException {
		SkinDirectory myNewSkin = skinFileSystemService.findSkin(name);
		skinArchiveService.createSkinArchive(name, new ByteArrayInputStream(myFileData), myNewSkin
				.getLastModified(), "");
	}

	private void updateSkin(String name, byte[] myFileData, String comment) throws ActionNotAlowedException, SkinException,
			IOException {
		checkAction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_EDIT);
		skinFileSystemService.updateSkin(name, new ByteArrayInputStream(myFileData));
		createArchive(name, myFileData);
		SakaiUtils.spawnModificationEvent(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_VIEW, "skin:" + name);
	}

}
