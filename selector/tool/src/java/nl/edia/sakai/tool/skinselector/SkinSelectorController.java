/*
 * #%L
 * Edia Skin Selector Tool
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
package nl.edia.sakai.tool.skinselector;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import nl.edia.sakai.tool.skinmanager.SkinService;
import nl.edia.sakai.tool.util.SakaiUtils;

import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class SkinSelectorController extends SimpleFormController {
	SkinService skinService;

	@Override
	protected ModelAndView onSubmit(Object command) throws Exception {
		SkinSelectValueObject myValueObject = (SkinSelectValueObject) command;
		Map<String, Object> myData = new HashMap<String, Object>();
		if (myValueObject != null && myValueObject.getSkin() != null) {
			String mySkin = myValueObject.getSkin();
			// We use a POST,REDIRECT,GET pattern, so after updating the skin
			// we redirect and will end up at the get, formBackingObject will
			// now return the updated skin as current selected skin.
			boolean isUpdated = updateCurrentSite(mySkin);
			// We use a POST,REDIRECT,GET pattern, we
			// will flag the updated message through the cgi
			// params.
			myData.put("updated", isUpdated);
		}


		return new ModelAndView(getSuccessView(), myData);
	}

	protected boolean updateCurrentSite(String mySkin) throws PermissionException {
		String myCurrentSelectedSkin = getCurrentSelectedSkin();
		if (myCurrentSelectedSkin != null) {
			if (mySkin.equals(myCurrentSelectedSkin)) {
				return false;
			}
		}

		String mySiteId = SakaiUtils.getCurrentSiteId();
		Site mySite = null;
		if (mySiteId != null) {
			try {
				mySite = org.sakaiproject.site.cover.SiteService.getSite(mySiteId);
				mySite.setSkin(mySkin);
				org.sakaiproject.site.cover.SiteService.save(mySite);
			} catch (IdUnusedException e) {
				// Ignore
			}
		}

		return true;

	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		SkinSelectValueObject mySkinSelectValueObject = new SkinSelectValueObject();
		// Before bind, set the current skin
		mySkinSelectValueObject.setSkin(getCurrentSelectedSkin());
		// We use a POST,REDIRECT,GET pattern, we
		// will flag the updated message through the cgi
		// params and pick it up here
		mySkinSelectValueObject.setUpdated("true".equalsIgnoreCase(request.getParameter("updated")));
		return mySkinSelectValueObject;
	}

	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
		Map<String, Object> myData = new HashMap<String, Object>();
		// Into page context: skins
		myData.put("skins", skinService.fetchInstalledSkinNames());
		// Into page context: hasPermission, this var is not a simple permission, but one of 3, see 
		// the source of org.sakaiproject.site.impl.BaseSiteService#save for details
		myData.put("hasPermission", SakaiUtils.hasPermission(SiteService.SECURE_UPDATE_SITE) || SakaiUtils.hasPermission(SiteService.SECURE_UPDATE_SITE_MEMBERSHIP)
		        || SakaiUtils.hasPermission(SiteService.SECURE_UPDATE_GROUP_MEMBERSHIP));
		return myData;
	}

	/**
	 * Get the current skin of the current site.
	 * @return
	 */
	protected String getCurrentSelectedSkin() {
		// Get the current site
		Site myCurrentSite = SakaiUtils.getCurrentSite();
		// Get the skin
		String mySkin = null;
		if (myCurrentSite != null) {
			mySkin = myCurrentSite.getSkin();
			if (mySkin == null) {
				// If the site skin is null, it will fall back to the dafault skin
				mySkin = ServerConfigurationService.getString("skin.default");
			}
		}
		return mySkin;
	}

	public SkinService getSkinService() {
		return skinService;
	}

	public void setSkinService(SkinService skinService) {
		this.skinService = skinService;
	}

	/**
	 * Helper class that passes data between the controller, binder and page forth and back.
	 * @author roland
	 *
	 */
	public static class SkinSelectValueObject {
		/**
		 * The current selected skin 
		 */
		private String skin;

		/**
		 * Thrue if the skin has been updated.
		 */
		private boolean updated = false;

		public String getSkin() {
			return skin;
		}

		public void setSkin(String skin) {
			this.skin = skin;
		}

		public boolean isUpdated() {
			return updated;
		}

		public void setUpdated(boolean updated) {
			this.updated = updated;
		}
	}

}