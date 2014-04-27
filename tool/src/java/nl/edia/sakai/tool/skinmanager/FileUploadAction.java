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
package nl.edia.sakai.tool.skinmanager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

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
				skinService.createSkin(mySkinId, inputStream, new Date());
			} else {

				String mySkinId = context.getFlowScope()
						.getRequiredString("id");
				skinService.updateSkin(mySkinId, inputStream, new Date());
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
