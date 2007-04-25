/*
 * $Author: roland $
 * $Revision: 1309 $
 * $Date: 2007-04-17 00:09:13 +0200 (di, 17 apr 2007) $
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

package nl.edia.sakai.tool.skinmanager.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.cover.ServerConfigurationService;

import nl.edia.sakai.tool.skinmanager.CannotOverwriteException;
import nl.edia.sakai.tool.skinmanager.InvalidPackageException;
import nl.edia.sakai.tool.skinmanager.InvalidSkinDirException;
import nl.edia.sakai.tool.skinmanager.SkinException;
import nl.edia.sakai.tool.skinmanager.SkinFileSystemService;
import nl.edia.sakai.tool.skinmanager.SkinPrerequisitesFatalException;
import nl.edia.sakai.tool.skinmanager.SkinPrerequisitesNonFatalException;
import nl.edia.sakai.tool.skinmanager.model.SkinDirectory;
import nl.edia.sakai.tool.skinmanager.model.SkinFile;

public class SkinFileSystemServiceImpl implements SkinFileSystemService {

	private static final Pattern PATTERN_CSS = Pattern.compile("^[^/]+\\.css$");

	private static final Pattern PATTERN_IMAGES_DIR_CONTENT = Pattern
			.compile("^images/.+\\.(gif|jpg|jpeg|png|tif|tiff)$");

	private static final Pattern PATTERN_IMAGES_DIR_EMPTY = Pattern
			.compile("^images/$");

	Pattern[] includePattern = new Pattern[] { PATTERN_CSS,
			PATTERN_IMAGES_DIR_EMPTY, PATTERN_IMAGES_DIR_CONTENT };

	Pattern[] directoryPattern = new Pattern[] { PATTERN_IMAGES_DIR_EMPTY,
			PATTERN_IMAGES_DIR_CONTENT };

	static final Log log = LogFactory.getLog(SkinFileSystemServiceImpl.class);

	public void createSkin(String name, InputStream data) throws SkinException,
			IOException {

		File file = createTempZip(data);
		File mySkinDir = null;
		boolean isSucceeded = false;
		try {
			validateSkinZip(file);
			mySkinDir = prepareSkinDir(name, false);
			installSkin(mySkinDir, file);
			isSucceeded = true;
		} finally {
			file.delete();
			if (!isSucceeded && mySkinDir != null && mySkinDir.isDirectory()) {
				FileSystemUtils.purge(mySkinDir);
			}
		}
	}

	public List<SkinDirectory> fetchInstalledSkins() throws SkinException,
			IOException {
		File mySkinHome = getSkinHome();
		File[] mySkins = mySkinHome.listFiles(new FileFilter() {

			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		List<SkinDirectory> myFoundSkins = new ArrayList<SkinDirectory>(
				mySkins.length - 1);
		for (int i = 0; i < mySkins.length; i++) {
			File myFile = mySkins[i];
			String myName = myFile.getName();
			if (!"images".equals(myName)) {
				myFoundSkins.add(createSkinValueObject(myFile));
			}

		}
		Collections.sort(myFoundSkins, new Comparator<SkinDirectory>() {

			public int compare(SkinDirectory o1, SkinDirectory o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}

		});
		return myFoundSkins;
	}

	public SkinDirectory findSkin(String id) throws SkinException, IOException {
		return createSkinValueObject(getSkinDir(id));
	}

	public void removeSkin(String name) throws SkinException, IOException {
		removeSkinDir(name);
	}

	public void updateSkin(String name, InputStream data) throws SkinException,
			IOException {
		File file = createTempZip(data);
		try {
			validateSkinZip(file);
			File mySkinDir = prepareSkinDir(name, true);
			installSkin(mySkinDir, file);
		} finally {
			file.delete();
		}
	}

	protected void createFile(ZipEntry zipEntry, File file, ZipFile myZipFile)
			throws IOException {
		String myEntryName = zipEntry.getName();
		boolean isMatched = isValidName(myEntryName);
		if (!isMatched) {
			log.warn("Skipping file entry: " + myEntryName + "");
			return;
		}

		if (PATTERN_IMAGES_DIR_CONTENT.matcher(myEntryName).matches()) {
			File myParentDir = file.getParentFile();
			if (!myParentDir.exists()) {
				myParentDir.mkdir();
			}
		}
		InputStream myInputStream = null;
		OutputStream myOutputStream = null;
		try {
			myInputStream = myZipFile.getInputStream(zipEntry);
			myOutputStream = new FileOutputStream(file);
			byte[] myBuffer = new byte[1024];
			int read;
			while ((read = myInputStream.read(myBuffer)) != -1) {
				myOutputStream.write(myBuffer, 0, read);
			}
		} finally {
			if (myOutputStream != null) {
				myOutputStream.close();
			}
			if (myInputStream != null) {
				myInputStream.close();
			}
		}
	}

	protected boolean isValidName(String myEntryName) {
		boolean isMatched = false;
		for (int i = 0; i < includePattern.length; i++) {
			if (includePattern[i].matcher(myEntryName).matches()) {
				isMatched = true;
			}
		}
		return isMatched;
	}

	protected List<SkinFile> createSkinFileValueObjects(File dir) {
		return createSkinFileValueObjects("", dir);
	}

	protected List<SkinFile> createSkinFileValueObjects(String prefix, File dir) {
		if (dir.isDirectory()) {
			if (prefix.length() > 0) {
				prefix = prefix + "/";
			}
			List<SkinFile> myFileNames = new ArrayList<SkinFile>();
			File[] myChildren = dir.listFiles();
			for (int i = 0; i < myChildren.length; i++) {
				File myFile = myChildren[i];
				if (myFile.isFile()) {
					SkinFile mySkinFile = new SkinFile();
					mySkinFile.setName(prefix + myFile.getName());
					mySkinFile.setSize(myFile.length());
					mySkinFile.setLastModified(new Date(myFile.lastModified()));
					myFileNames.add(mySkinFile);
				} else {
					myFileNames.addAll(createSkinFileValueObjects(prefix
							+ myFile.getName(), myFile));
				}
			}
			return myFileNames;
		} else {
			return Collections.emptyList();
		}
	}

	protected SkinDirectory createSkinValueObject(File file) {
		SkinDirectory mySkinDirectory = new SkinDirectory();
		mySkinDirectory.setName(file.getName());
		mySkinDirectory.setLastModified(new Date(file.lastModified()));
		mySkinDirectory.setFiles(createSkinFileValueObjects(file));
		return mySkinDirectory;
	}

	protected File createTempZip(InputStream data) throws IOException {
		File myTempFile = File.createTempFile("skinmanager", "zip");
		OutputStream myOutputStream = new FileOutputStream(myTempFile);
		byte[] buffer = new byte[1024];
		int read;
		while ((read = data.read(buffer)) != -1) {
			myOutputStream.write(buffer, 0, read);
		}
		myOutputStream.close();
		return myTempFile;
	}

	/**
	 * Check the environment for catalina's base or home directory.
	 * 
	 * @return Catalina's base or home directory.
	 * @throws SkinException
	 */
	protected File getCatalinaHome() throws SkinException {
		String catalina = System.getProperty("catalina.base");
		if (catalina == null) {
			catalina = System.getProperty("catalina.home");
		}

		if (catalina == null) {
			// This is a fatal missing prerequisite, if catalina home is not
			// defined
			// we cannot recover from that
			throw new SkinPrerequisitesFatalException(
					"Cannot find catalina.home nor catalina.base");
		}
		File myCatalinaHome = new File(catalina);
		if (!myCatalinaHome.isDirectory()) {
			// If cataline home is not a directory, there is no way of
			// recovering.
			throw new SkinPrerequisitesFatalException(
					"Catalina home not a directory: '" + myCatalinaHome + "'");
		}

		return myCatalinaHome;
	}

	protected SkinException getMissingResourceException(String name,
			boolean isDirectory) {
		return new InvalidPackageException("Missing "
				+ (isDirectory ? "directory" : "file") + ": '" + name
				+ "' not found!");

	}

	protected SkinException getInvalidSkinDirException(String name,
			boolean isDirectory) {
		return new InvalidSkinDirException("Missing "
				+ (isDirectory ? "directory" : "file") + ": '" + name
				+ "' not found!");

	}

	protected File getSkinDir(String name) throws SkinException, IOException {
		File mySkinHome = getSkinHome();
		if (mySkinHome != null) {
			return new File(mySkinHome, name);
		}
		return null;
	}

	protected File getSkinHome() throws SkinException, IOException {
		String myRepoLoction = ServerConfigurationService
				.getString("skin.repo");
		if (myRepoLoction == null) {
			myRepoLoction = "/library/skin";
		}

		File mySkinHome = null;
		File myCatalina = getCatalinaHome();
		File myWebApp = new File(myCatalina, "webapps");
		if (myWebApp.isDirectory()) {
			File mySkinHomeTmp = new File(myWebApp, myRepoLoction);
			if (mySkinHomeTmp.isDirectory()) {
				mySkinHome = mySkinHomeTmp;
			}
		}
		if (mySkinHome == null) {
			// This is a non-fatal: the skin dir can be unavailable / not
			// deployed yet
			throw new SkinPrerequisitesNonFatalException(
					"Cannot find skin home: '" + myCatalina.getCanonicalPath()
							+ "/webapps/library/skin'");
		}
		File[] listFiles = mySkinHome.listFiles();
		boolean isImagesFound = false;
		boolean isToolBaseCssFound = false;
		for (int i = 0; i < listFiles.length; i++) {
			File myFile = listFiles[i];
			if (myFile.isDirectory() && myFile.getName().equals("images")) {
				isImagesFound = true;
			} else if (myFile.isFile()
					&& myFile.getName().equals("tool_base.css")) {
				isToolBaseCssFound = true;
			}
		}

		if (!isImagesFound) {
			// This is a non-fatal: the skin dir can be not deployed yet
			throw new SkinPrerequisitesNonFatalException(
					"Cannot find required skin images directory: '"
							+ myCatalina.getCanonicalPath()
							+ "/webapps/library/skin/images'");
		}
		if (!isToolBaseCssFound) {
			throw new SkinPrerequisitesNonFatalException(
					"Cannot find required skin tool_base.css: '"
							+ myCatalina.getCanonicalPath()
							+ "/webapps/library/skin/tool_base.css'");

		}
		return mySkinHome;
	}

	protected void handleEntry(ZipEntry myZipEntry, ZipFile myZipFile,
			File skinDir) throws SkinException, IOException {
		String myEntryName = myZipEntry.getName();
		boolean isMatched = isValidName(myEntryName);
		if (!isMatched) {
			log.warn("Skipping file entry: " + myEntryName + "");
			return;
		}
		File myFile = new File(skinDir, myEntryName);
		if (!FileSystemUtils.isParentOf(myFile, skinDir)) {
			throw new SkinException(
					"Error unpacking zip file, attempting to escape from skin dir! '"
							+ myEntryName + "'");
		}
		if (!myFile.exists()) {
			if (myZipEntry.isDirectory()) {
				boolean myMkdirs = myFile.mkdirs();
				if (!myMkdirs) {
					throw new SkinException(
							"Error unpacking zip file, cannot create directory '"
									+ myFile.getCanonicalPath() + "'");
				}
			} else {
				createFile(myZipEntry, myFile, myZipFile);
			}
		} else {
			if (myZipEntry.isDirectory()) {
				if (!myFile.isDirectory()) {
					myFile.delete();
					myFile.mkdirs();
				}
			} else {
				if (myFile.isDirectory()) {
					FileSystemUtils.purge(myFile);
				}
				createFile(myZipEntry, myFile, myZipFile);
			}

		}
	}

	// protected void validateFile(File file) throws SkinException {
	// if (!file.isFile()) {
	// throw new SkinException("Not a file '" + file + "'");
	// }
	// if (!file.canRead()) {
	// throw new SkinException("Cannot read file '" + file + "'");
	// }
	//
	// }

	protected void installSkin(File skinDir, File file) throws SkinException,
			IOException {
		ZipFile myZipFile = null;
		try {
			myZipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> myEntries = myZipFile.entries();
			while (myEntries.hasMoreElements()) {
				ZipEntry myZipEntry = (ZipEntry) myEntries.nextElement();
				handleEntry(myZipEntry, myZipFile, skinDir);
			}
		} catch (ZipException z) {
			throw new SkinException("Error reading zip file", z);
		} finally {
			if (myZipFile != null) {
				try {
					myZipFile.close();
				} catch (IOException e) {
					// ignore
				}
			}

		}
	}

	protected File prepareSkinDir(String name, boolean overWrite)
			throws SkinException, IOException {
		File mySkinDir = getSkinDir(name);
		if (mySkinDir.exists()) {
			if (!mySkinDir.isDirectory()) {
				throw new SkinException("Unexepected file found '" + mySkinDir
						+ "'");
			}
			if (!overWrite) {
				throw new CannotOverwriteException(
						"Cannot overwite existing skin '" + mySkinDir + "'");
			} else {
				validateSkinDir(mySkinDir);
				boolean myDeleted = FileSystemUtils.purge(mySkinDir);
				if (!myDeleted) {
					throw new SkinException("Cannot remove existing skin '"
							+ mySkinDir + "'");
				}
			}
		}

		if (!mySkinDir.exists()) {
			boolean myMakeDir = mySkinDir.mkdirs();
			if (!myMakeDir) {
				throw new SkinException("Cannot create skin directory '"
						+ mySkinDir + "'");
			}
		}

		return mySkinDir;
	}

	protected void removeSkinDir(String name) throws SkinException, IOException {
		File mySkinDir = getSkinDir(name);
		validateSkinDir(mySkinDir);
		boolean myDeleted = FileSystemUtils.purge(mySkinDir);
		if (!myDeleted) {
			throw new SkinException("Couldn't remove skin '" + mySkinDir + "'");
		}

	}

	protected void validateSkinZip(File file) throws SkinException {
		ZipFile myZipFile = null;
		try {
			boolean isToolFound = false;
			boolean isPortalFound = false;
			boolean isImagesFound = false;
			myZipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> myEntries = myZipFile.entries();
			while (myEntries.hasMoreElements()) {
				ZipEntry myZipEntry = (ZipEntry) myEntries.nextElement();
				String myName = myZipEntry.getName();
				if (myName.contains("..") || myName.startsWith("/")) {
					throw new SkinException(
							"Illegal file name found in zip file '" + myName
									+ "'");
				}
				if (!myZipEntry.isDirectory()) {
					if (myName.equals("tool.css")) {
						isToolFound = true;
					} else if (myName.equals("portal.css")) {
						isPortalFound = true;
					} else if (PATTERN_IMAGES_DIR_CONTENT.matcher(myName)
							.matches()) {
						isImagesFound = true;
					}
				} else {
					if (PATTERN_IMAGES_DIR_EMPTY.matcher(myName).matches()) {
						isImagesFound = true;
					}
				}
			}

			if (!isPortalFound) {
				throw getMissingResourceException("portal.css", false);
			}
			if (!isToolFound) {
				throw getMissingResourceException("tool.css", false);
			}
			if (!isImagesFound) {
				throw getMissingResourceException("images", true);
			}
		} catch (ZipException z) {
			throw new SkinException("Error reading zip file", z);
		} catch (IOException e) {
			throw new SkinException("IO Error reading zip file", e);
		} finally {
			if (myZipFile != null) {
				try {
					myZipFile.close();
				} catch (IOException e) {
					// ignore
				}
			}

		}

	}

	public void writeSkinData(String name, OutputStream out)
			throws SkinException, IOException {
		File mySkinDir = getSkinDir(name);
		ZipOutputStream myOut = new ZipOutputStream(out);
		writeSkinZipEntries(mySkinDir, myOut);
		myOut.flush();
		myOut.close();
	}

	protected void writeSkinZipEntries(File dir, ZipOutputStream out)
			throws IOException {
		writeSkinZipEntries("", dir, out);
	}

	protected void writeSkinZipEntries(String prefix, File dir,
			ZipOutputStream out) throws IOException {
		if (dir.isDirectory()) {
			if (prefix.length() > 0) {
				prefix = prefix + "/";
			}
			File[] myChildren = dir.listFiles();
			for (int i = 0; i < myChildren.length; i++) {
				File myFile = myChildren[i];
				if (myFile.isFile()) {
					writeZipEntry(prefix, out, myFile);
				} else {
					ZipEntry mySkinFile = new ZipEntry(prefix
							+ myFile.getName() + "/");
					mySkinFile.setTime(myFile.lastModified());
					out.putNextEntry(mySkinFile);
					out.closeEntry();
					writeSkinZipEntries(prefix + myFile.getName(), myFile, out);
				}
			}
		}
	}

	private void writeZipEntry(String prefix, ZipOutputStream out, File file)
			throws IOException, FileNotFoundException {
		ZipEntry mySkinFile = new ZipEntry(prefix + file.getName());
		mySkinFile.setSize(file.length());
		mySkinFile.setTime(file.lastModified());
		out.putNextEntry(mySkinFile);

		byte[] buf = new byte[1024];
		InputStream in = new FileInputStream(file);
		// Transfer bytes from the file to the ZIP file
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}

		// Complete the entry
		out.closeEntry();
		in.close();
	}
	/**
	 * Checks if a dir is a valid skin directory, this means, a subdir
	 * of the skin directory.
	 * @param dir
	 * @throws SkinException
	 * @throws IOException
	 */
	protected void validateSkinDir(File dir) throws SkinException, IOException {
		File mySkinHome = getSkinHome();
		if (dir.isDirectory() && FileSystemUtils.equals(mySkinHome, dir.getParentFile())) {
			return;
		}
		throw new InvalidSkinDirException("Not a skin directory: " + dir);
	}

}
