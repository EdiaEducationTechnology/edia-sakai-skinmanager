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

import nl.edia.sakai.tool.skinmanager.SkinService;

import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class SkinDeleteAction extends AbstractAction {

	SkinService skinService;

	@Override
	protected Event doExecute(RequestContext context) throws Exception {
		skinService.removeSkin(context.getFlowScope().getRequiredString("id"));
		return success();
	}

	public SkinService getSkinService() {
		return skinService;
	}

	public void setSkinService(SkinService skinService) {
		this.skinService = skinService;
	}

}
