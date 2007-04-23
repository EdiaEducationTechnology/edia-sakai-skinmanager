/*
 * $Author: roland $
 * $Revision: 1099 $
 * $Date: 2007-04-01 01:57:49 +0200 (zo, 01 apr 2007) $
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
