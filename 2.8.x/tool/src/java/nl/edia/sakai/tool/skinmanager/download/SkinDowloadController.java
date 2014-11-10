/*
 * #%L
 * Edia Skin Manager Tool
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
package nl.edia.sakai.tool.skinmanager.download;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.edia.sakai.tool.skinmanager.Permissions;
import nl.edia.sakai.tool.skinmanager.SkinArchiveService;
import nl.edia.sakai.tool.util.SakaiUtils;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class SkinDowloadController implements Controller {

	String errorView;
	
	SkinArchiveService skinArchiveService;
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// First check permission
		if (SakaiUtils.hasPermission(Permissions.PERMISSION_EDIA_SAKAI_SKININSTALL_EDIT)) {
			// Get the required id parameter
			String myId = request.getParameter("id");
			if (myId != null) {
				// Create the data map
				Map<String, Object> myData = new HashMap<String, Object>();
				myData.put("id", myId);
				// Try to get the version
				String myVersion = request.getParameter("version");
				if (myVersion != null) {
					try {
						myData.put("version", new Long(myVersion));
					}
					catch (Exception e) {
						// ignore
					}
				}
				// go to skin download view.
				return new ModelAndView(new SkinDownloadView(skinArchiveService), myData);
			}
		}
		return new ModelAndView(errorView);
	}

	public String getErrorView() {
    	return errorView;
    }

	public void setErrorView(String errorView) {
    	this.errorView = errorView;
    }

	public SkinArchiveService getSkinArchiveService() {
    	return skinArchiveService;
    }

	public void setSkinArchiveService(SkinArchiveService skinArchiveService) {
    	this.skinArchiveService = skinArchiveService;
    }

}
