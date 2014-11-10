package nl.edia.sakai.tool.skinmanager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import nl.edia.sakai.tool.skinmanager.SkinException;
import nl.edia.sakai.tool.skinmanager.impl.FileSystemUtils;
import nl.edia.sakai.tool.skinmanager.impl.SkinFileSystemServiceImpl;

import junit.framework.TestCase;

public class SkinFileSystemServiceImplTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		File mySkinBaseDir = new File("impl/api/test/work");
		if (mySkinBaseDir.exists()) {
			FileSystemUtils.purge(mySkinBaseDir);
		}
		mySkinBaseDir.mkdirs();
		new File(mySkinBaseDir, "webapps/library/skin").mkdirs();
		System.setProperty("catalina.base", "impl/api/test/work");
	}
	
	@Override
	protected void tearDown() throws Exception {
		File mySkinBaseDir = new File("impl/api/test/work");
		if (mySkinBaseDir.exists()) {
			FileSystemUtils.purge(mySkinBaseDir);
		}

	}
	
	public void testInstallSkinFile1() throws SkinException, IOException {
		File myFile = getTestFile();
		
		new SkinFileSystemServiceImpl().createSkin("test", new FileInputStream(myFile));
		try {
			new SkinFileSystemServiceImpl().createSkin("test", new FileInputStream(myFile));
			assertFalse(true);
		} catch (SkinException e) {
			assertFalse(false);
		}
	}

	public void testInstallSkinFile2() throws SkinException, IOException {
		File myFile = getTestFile();
		new SkinFileSystemServiceImpl().createSkin("test", new FileInputStream(myFile));
		new SkinFileSystemServiceImpl().updateSkin("test", new FileInputStream(myFile));
		File myData = File.createTempFile("test", ".zip");
		FileOutputStream myFileOutputStream = new FileOutputStream(myData);
		new SkinFileSystemServiceImpl().writeSkinData("test", myFileOutputStream);
		myFileOutputStream.close();
		new SkinFileSystemServiceImpl().createSkin("test2", new FileInputStream(myData));
		myData.delete();
	}

	public void testInstallSkinFile4() throws SkinException, IOException {
		File myFile = getIndiaFile();
		
		new SkinFileSystemServiceImpl().createSkin("india", new FileInputStream(myFile));
		try {
			new SkinFileSystemServiceImpl().createSkin("india", new FileInputStream(myFile));
			assertFalse(true);
		} catch (SkinException e) {
			assertFalse(false);
		}
	}

	private File getTestFile() {
		File myFile = new File("impl/api/test/skin.zip");
		return myFile;
	}

	private File getIndiaFile() {
		File myFile = new File("impl/api/test/india.zip");
		return myFile;
	}

}
