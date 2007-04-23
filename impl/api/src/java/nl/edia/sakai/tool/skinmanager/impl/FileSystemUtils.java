/*
 * $Author: roland $
 * $Revision: 1141 $
 * $Date: 2007-04-04 23:36:44 +0200 (wo, 04 apr 2007) $
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
import java.io.IOException;

public class FileSystemUtils {

	public static boolean purge(File dir) {
		File[] myListFiles = dir.listFiles();
		boolean ok = true;
		for (int i = 0; i < myListFiles.length; i++) {
			File myFile = myListFiles[i];
			if (myFile.isDirectory()) {
				if (!purge(myFile)) {
					ok = false;
				}
			} else if (myFile.isFile()) {
				if (!myFile.delete()) {
					ok = false;
				}
			}
		}
		if (!dir.delete()) {
			ok = false;
		}
		return ok;
	}

	public static boolean isParentOf(File child, File parent) throws IOException {
		File myChildParent = child.getParentFile();
		if (myChildParent == null) {
			return false;
		}
		if (equals(parent, myChildParent)) {
			return true;
		}

		return isParentOf(myChildParent, parent);
	}

	public static boolean equals(File a, File b) throws IOException {
		return b.getCanonicalPath().equals(a.getCanonicalPath());
	}
	
}
