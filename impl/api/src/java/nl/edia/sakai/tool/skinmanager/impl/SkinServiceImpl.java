/*
 * #%L
 * Edia Skin Manager Logic Impl
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
package nl.edia.sakai.tool.skinmanager.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.edia.sakai.tool.skinmanager.ActionNotAlowedException;
import nl.edia.sakai.tool.skinmanager.Permissions;
import nl.edia.sakai.tool.skinmanager.SkinArchiveService;
import nl.edia.sakai.tool.skinmanager.SkinException;
import nl.edia.sakai.tool.skinmanager.SkinFileSystemService;
import nl.edia.sakai.tool.skinmanager.SkinInUseException;
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

public class SkinServiceImpl implements SkinService {

	private static final boolean BOOLEAN_ACTIVE_SYNC = false;
	/**
	 * The amount of time each redeploy inteval runs, only 
	 */
	private static final int INTERVAL_REDEPLOY = 30 * 1000;
	private static final int INTERVAL_WAIT = 10 * 1000;

	SkinFileSystemService skinFileSystemService;

	SkinArchiveService skinArchiveService;

	boolean isArchiveLeading = true;
	
	static Log log = LogFactory.getLog(SkinServiceImpl.class);

	private Thread theSyncThread;

	/* (non-Javadoc)
	 * @see nl.edia.sakai.tool.skinmanager.SkinService#createSkin(java.lang.String, java.io.InputStream)
	 */
	@Override
	public void createSkin(String name, InputStream data) throws SkinException, IOException {
		checkAction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_CREATE);

		byte[] myFileData = readStream(data);
		final Date date = skinArchiveService.fetchSkinArchiveDate(name);

		skinFileSystemService.createSkin(name, new ByteArrayInputStream(myFileData), date, false);
		createArchive(name, myFileData);

		SakaiUtils.createModificationEvent(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_CREATE, "skin:" + name);
	}

	/* (non-Javadoc)
	 * @see nl.edia.sakai.tool.skinmanager.SkinService#fetchInstalledSkins()
	 */
	@Override
	public List<SkinDirectory> fetchInstalledSkins() throws SkinException, IOException {
		checkAction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_VIEW);
		return skinFileSystemService.fetchInstalledSkins();
	}

	/* (non-Javadoc)
	 * @see nl.edia.sakai.tool.skinmanager.SkinService#fetchInstalledSkinNames()
	 */
	@Override
	public List<String> fetchInstalledSkinNames() throws SkinException, IOException {
		// Maybe a bit fanatic, but at least check if the user is supposed to be here
		checkAction(org.sakaiproject.site.api.SiteService.SITE_VISIT);
		List<SkinDirectory> myFetchInstalledSkins = skinFileSystemService.fetchInstalledSkins();
		List<String> myList = new ArrayList<String>(myFetchInstalledSkins.size());
		for (SkinDirectory mySkinDirectory : myFetchInstalledSkins) {
			myList.add(mySkinDirectory.getName());
        }
		return myList;
	}

	/* (non-Javadoc)
	 * @see nl.edia.sakai.tool.skinmanager.SkinService#fetchSkinArchive(java.lang.String)
	 */
	@Override
	public SkinArchive fetchSkinArchive(String name) throws SkinException, IOException {
		return skinArchiveService.findSkinArchive(name);
	}

	/* (non-Javadoc)
	 * @see nl.edia.sakai.tool.skinmanager.SkinService#findSites(java.lang.String)
	 */
    @Override
	public List<Site> findSites(String name) {
		return skinArchiveService.findSites(name, getDefaultSkinName().equals(name));
	}

	/* (non-Javadoc)
	 * @see nl.edia.sakai.tool.skinmanager.SkinService#findSkin(java.lang.String)
	 */
	@Override
	public SkinDirectory findSkin(String id) throws SkinException, IOException {
		checkAction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_VIEW);
		SakaiUtils.createEvent(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_VIEW, "skin:" + id);
		return skinFileSystemService.findSkin(id);
	}

	/* (non-Javadoc)
	 * @see nl.edia.sakai.tool.skinmanager.SkinService#findSkinHistory(java.lang.String)
	 */
	@Override
	public List<SkinArchive> findSkinHistory(String name) throws ActionNotAlowedException {
		checkAction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_VIEW);
		return skinArchiveService.findSkinHistory(name);
	}

	/**
	 * @return
	 */
	public SkinArchiveService getSkinArchiveService() {
		return skinArchiveService;
	}

	/**
	 * @return
	 */
	public SkinFileSystemService getSkinFileSystemService() {
		return skinFileSystemService;
	}

	/**
	 * <p>
	 * Does an initialization of the service.
	 * </p>
	 * @throws Exception
	 */
	public void init() throws Exception {
		FunctionManager.registerFunction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_VIEW);
		FunctionManager.registerFunction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_CREATE);
		FunctionManager.registerFunction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_EDIT);
		FunctionManager.registerFunction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_DELETE);
		
		final int waitIntervalRetry = ServerConfigurationService.getInt("skinmanager.sync.interval", INTERVAL_REDEPLOY);
		final boolean activeSync = ServerConfigurationService.getBoolean("skinmanager.sync.continious", BOOLEAN_ACTIVE_SYNC);
		
		Runnable myTask = new Runnable() {
			@Override
			public void run() {
				boolean initialDeployDone = false;
				while(activeSync || !initialDeployDone) {
					try {
						syncDatabaseAndFileSystem();
						initialDeployDone = true;
						synchronized (SkinServiceImpl.this) {
							try {
								SkinServiceImpl.this.wait(waitIntervalRetry);
							} catch (InterruptedException e1) {
								// Just die.
								return;
							}
						}
					} catch (SkinPrerequisitesNonFatalException e) {
						log.info("Non-fatal problem attempting to init the skin manager service, retrying in " + (INTERVAL_REDEPLOY/1000) + " seconds", e);
					} catch (SkinException e) {
						log.error("Fatal problem attempting to init the skin manager service", e);
						initialDeployDone = true;
					} catch (IOException e) {
						log.error("Fatal problem attempting to init the skin manager service", e);
						initialDeployDone = true;
					}
				}
			}
		};
		
		theSyncThread = new Thread(myTask, "Skin sync thread");
		// Die if you are the only one...
		theSyncThread.setDaemon(true);
		// Start the thread
		theSyncThread.start();
		
	}
	
	/**
	 * 
	 */
	public void destroy() {
		if (theSyncThread != null && theSyncThread.isAlive())  {
			theSyncThread.interrupt();
		}
	}

	protected void syncDatabaseAndFileSystem() throws SkinException, IOException {
		List<String> myInstalledSkinNames = new ArrayList<String>();
		// Add new skins to the archive
		for (SkinDirectory skinDirectory : skinFileSystemService.fetchInstalledSkins()) {
			String name = skinDirectory.getName();
			myInstalledSkinNames.add(name);
			SkinArchive skinArchive = skinArchiveService.findSkinArchive(name);
			if (skinArchive == null) {
				readSkin(name, skinDirectory);
			} else if (shouldOverwriteDeployedSkin(skinDirectory, skinArchive)) {
				final int interval = ServerConfigurationService.getInt("skinmanager.deploy.wait", INTERVAL_WAIT);
				synchronized (SkinServiceImpl.this) {
					try {
						SkinServiceImpl.this.wait(interval);
					} catch (InterruptedException e) {
						return;
					}
				}
				installSkin(name, true);
			}
		}
		// Disable/restore skins that disappeared
		for (SkinArchive myArchive : skinArchiveService.fetchActiveSkinArchives()) {
			String name = myArchive.getName();
			if (!myInstalledSkinNames.contains(name)) {
				if (isArchiveLeading) {
					installSkin(name, false);
				} else {
					log.info("Skin archive removed form DB / not on filesystem: " + name);
					skinArchiveService.removeSkinArchive(name);
				}
			}
		}
	}

	protected boolean shouldOverwriteDeployedSkin(SkinDirectory skinDirectory, SkinArchive skinArchive) {
		boolean overwrite = ServerConfigurationService.getBoolean("skinmanager.deploy.overwrite", false);
		return overwrite && !equals(skinArchive.getLastModified(), skinDirectory.getLastModified());
	}

	private boolean equals( Date a, Date b) {
		if (a == null || b == null) return false;
		long timeA = a.getTime();
		long timeB = b.getTime();
		return Math.abs(timeA - timeB) < 1000;
	}

	private void readSkin(String myName, SkinDirectory mySkinDirectory) throws IOException, SkinException {
		ByteArrayOutputStream myOutputStream = new ByteArrayOutputStream();
		try {
			skinFileSystemService.writeSkinData(myName, myOutputStream);
			ByteArrayInputStream myInputStream = new ByteArrayInputStream(myOutputStream.toByteArray());
			skinArchiveService.createSkinArchive(myName, myInputStream, mySkinDirectory.getLastModified(), "Synchronized with filesystem");
			log.info("Skin archive created from filesystem: " + mySkinDirectory.getName());
		} finally {
			myOutputStream.close();
		}
	}

	/**
	 * @deprecated Use {@link #installSkin(String,boolean)} instead
	 */
	@Deprecated
	private void installSkin(String name) {
		installSkin(name, false);
	}

	private void installSkin(String name, boolean overWrite) {
		try {
			ByteArrayOutputStream myOutputStream = new ByteArrayOutputStream();
			final Date date = skinArchiveService.fetchSkinArchiveDate(name);
			skinArchiveService.fetchSkinArchiveData(name, myOutputStream);
			skinFileSystemService.createSkin(name, new ByteArrayInputStream(myOutputStream
					.toByteArray()), date, overWrite);
			log.warn("Skin archive missing on filesystem, installed on FS: " + name);
		} catch (IOException e) {
			log.warn(e);
		} catch (SkinException e) {
			log.warn(e);
		}
	}

	/* (non-Javadoc)
	 * @see nl.edia.sakai.tool.skinmanager.SkinService#isInUse(java.lang.String)
	 */
	@Override
	public boolean isInUse(String name) throws ActionNotAlowedException {
		checkAction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_VIEW);
		return !findSites(name).isEmpty();
	}

	/* (non-Javadoc)
	 * @see nl.edia.sakai.tool.skinmanager.SkinService#removeSkin(java.lang.String)
	 */
	@Override
	public void removeSkin(String name) throws SkinException, IOException {
		checkAction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_DELETE);
		if (!isInUse(name)) {
			skinFileSystemService.removeSkin(name);
			skinArchiveService.removeSkinArchive(name);
			SakaiUtils.createModificationEvent(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_DELETE, "skin:" + name);
		} else {
			throw new SkinInUseException("SkinDirectory '" + name + "' is in use and cannot be deleted");
		}

	}

	/* (non-Javadoc)
	 * @see nl.edia.sakai.tool.skinmanager.SkinService#restoreSkinHistory(java.lang.String, int)
	 */
	@Override
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

	/**
	 * @param skinArchiveService
	 */
	public void setSkinArchiveService(SkinArchiveService skinArchiveService) {
		this.skinArchiveService = skinArchiveService;
	}

	/**
	 * @param skinFileSystemService
	 */
	public void setSkinFileSystemService(SkinFileSystemService skinFileSystemService) {
		this.skinFileSystemService = skinFileSystemService;
	}

	/* (non-Javadoc)
	 * @see nl.edia.sakai.tool.skinmanager.SkinService#updateSkin(java.lang.String, java.io.InputStream)
	 */
	@Override
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
		
		String myDefaultSkinName = getDefaultSkinName();
		if (myDefaultSkinName.equals(name)) {
			return mySkin == null || myDefaultSkinName.equals(mySkin) || "".equals(mySkin);
		}
		return name.equals(mySkin);
	}

	protected String getDefaultSkinName() {
	    String myDefaultSkinName = ServerConfigurationService.getString("skin.default");
		if (myDefaultSkinName == null) {
			myDefaultSkinName = "default";
		}
	    return myDefaultSkinName;
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

	private void updateSkin(String name, byte[] myFileData, @SuppressWarnings("unused") String comment) throws ActionNotAlowedException, SkinException,
			IOException {
		checkAction(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_EDIT);
		Date date = skinArchiveService.fetchSkinArchiveDate(name);
		skinFileSystemService.updateSkin(name, date, new ByteArrayInputStream(myFileData));
		createArchive(name, myFileData);
		SakaiUtils.createModificationEvent(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_VIEW, "skin:" + name);
	}

}
