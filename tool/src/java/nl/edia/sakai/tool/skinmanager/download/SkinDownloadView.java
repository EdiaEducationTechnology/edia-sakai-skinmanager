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

import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.edia.sakai.tool.skinmanager.SkinArchiveService;

import org.springframework.web.servlet.View;

public class SkinDownloadView implements View {
	SkinArchiveService skinArchiveService;

	public SkinDownloadView() {}

	public SkinDownloadView(SkinArchiveService skinArchiveService) {
		this.skinArchiveService = skinArchiveService;
	}

	@SuppressWarnings("unchecked")
    public void render(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String myName = (String) model.get("id");
		Number myVersion = (Number) model.get("version");

		setHeaders(response, myName);

		ServletOutputStream myOutputStream = response.getOutputStream();
		if (myVersion != null) {
			skinArchiveService.fetchSkinArchiveData(myName, myVersion.intValue(), myOutputStream);
		}
		else {
			skinArchiveService.fetchSkinArchiveData(myName, myOutputStream);
		}

	}

	/**
	 * @param response
	 * @param myName
	 */
	protected void setHeaders(HttpServletResponse response, String myName) {
		response.setContentType(getContentType());
		response.setHeader("Content-Disposition", "attachment; filename=" + myName + ".zip");
	}

	public String getContentType() {
		return "application/zip";
	}

	public SkinArchiveService getSkinArchiveService() {
		return skinArchiveService;
	}

	public void setSkinArchiveService(SkinArchiveService skinArchiveService) {
		this.skinArchiveService = skinArchiveService;
	}
	
}
