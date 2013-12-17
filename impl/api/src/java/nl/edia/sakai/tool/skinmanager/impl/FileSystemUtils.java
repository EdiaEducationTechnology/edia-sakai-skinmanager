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
