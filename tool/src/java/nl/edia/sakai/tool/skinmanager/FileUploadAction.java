/*
 * $Author: roland $
 * $Revision: 1141 $
 * $Date: 2007-04-04 23:36:44 +0200 (Wed, 04 Apr 2007) $
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

package nl.edia.sakai.tool.skinmanager;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.FileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class FileUploadAction extends AbstractAction {
	private SkinService skinService;

	private String fileParameterName = "file";

	protected Event doExecute(RequestContext context) throws Exception {
		InputStream inputStream = getFileInputStream(context);

		ParameterMap requestParameters = context.getRequestParameters();
		if (inputStream != null) {
			// data was uploaded
			if (context.getAttributes().getBoolean("new")) {

				String mySkinId = requestParameters.getRequired("id");
				skinService.createSkin(mySkinId, inputStream);
			} else {

				String mySkinId = context.getFlowScope()
						.getRequiredString("id");
				skinService.updateSkin(mySkinId, inputStream);
			}
		} else {
			throw new IllegalArgumentException("Required attribute '"
					+ fileParameterName
					+ "' is not present in map; attributes present are ["
					+ requestParameters + "]");
		}
		return success();
	}

	protected InputStream getFileInputStream(RequestContext context) throws IOException {
		InputStream inputStream = null;
		{
			ParameterMap requestParameters = context.getRequestParameters();
			MultipartFile file = requestParameters
					.getMultipartFile(fileParameterName);
			if (file != null) {
				inputStream = file.getInputStream();
			} else {
				Object attribute = ((ServletExternalContext) context
						.getExternalContext()).getRequest().getAttribute(
						fileParameterName);
				if (attribute instanceof FileItem) {
					inputStream = ((FileItem) attribute).getInputStream();
				}
			}
		}
		return inputStream;
	}

	public SkinService getSkinService() {
		return skinService;
	}

	public void setSkinService(SkinService skinService) {
		this.skinService = skinService;
	}

	public String getFileParameterName() {
		return fileParameterName;
	}

	public void setFileParameterName(String fileParameterName) {
		this.fileParameterName = fileParameterName;
	}

}
