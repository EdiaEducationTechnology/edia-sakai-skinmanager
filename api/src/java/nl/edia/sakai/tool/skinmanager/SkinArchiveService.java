package nl.edia.sakai.tool.skinmanager;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import nl.edia.sakai.tool.skinmanager.model.SkinArchive;

public interface SkinArchiveService {
	
	public List<SkinArchive> fetchActiveSkinArchives();
	
	public SkinArchive findSkinArchive(String name);

	public SkinArchive findSkinArchive(String name, int version);

	public List<SkinArchive> findSkinHistory(String name);

	public SkinArchive createSkinArchive(String name, InputStream file, Date time, String comment);
	
	public void fetchSkinArchiveData(String name, OutputStream out);
	
	public void fetchSkinArchiveData(String name, int version, OutputStream out);
	
	public void removeSkinArchive(String name);
}
