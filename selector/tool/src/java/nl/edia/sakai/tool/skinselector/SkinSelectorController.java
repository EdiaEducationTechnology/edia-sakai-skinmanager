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

import org.apache.commons.lang.StringUtils;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.portal.api.PortalService;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/*")
public class SkinSelectorController {

	@Autowired
	protected SkinService skinService;

	@Autowired
	protected PortalService portalService;

	@RequestMapping(method = RequestMethod.POST)
	protected ModelAndView onSubmit(@ModelAttribute SkinSelectValueObject command) throws Exception {
		Map<String, Object> myData = new HashMap<String, Object>();
		if (command != null && command.getSkin() != null) {
			String mySkin = command.getSkin();
			// We use a POST,REDIRECT,GET pattern, so after updating the skin
			// we redirect and will end up at the get, formBackingObject will
			// now return the updated skin as current selected skin.
			boolean isUpdated = updateCurrentSite(mySkin);
			// We use a POST,REDIRECT,GET pattern, we
			// will flag the updated message through the cgi
			// params.
			myData.put("updated", isUpdated);
		}


		return new ModelAndView("index.html", myData);
	}

	protected boolean updateCurrentSite(String skin) throws PermissionException {
		skin = processNeoSkinName(skin);
		String currentSelectedSkin = getCurrentSelectedSkin();
		if (currentSelectedSkin != null) {
			if (StringUtils.equals(skin, currentSelectedSkin)) {
				return false;
			}
		}

		String siteId = SakaiUtils.getCurrentSiteId();
		Site site = null;
		if (siteId != null) {
			try {
				site = org.sakaiproject.site.cover.SiteService.getSite(siteId);
				site.setSkin(skin);
				org.sakaiproject.site.cover.SiteService.save(site);
			} catch (IdUnusedException e) {
				// Ignore
			}
		}

		return true;

	}

	private String processNeoSkinName(String skin) {
//		String skinPrefix = portalService.getSkinPrefix();
//		if (StringUtils.startsWith(skin, skinPrefix)) {
//			return StringUtils.removeStart(skin, skinPrefix);
//		}
		return skin;
	}

	@ModelAttribute("command")
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

	@RequestMapping(method = RequestMethod.GET)
	protected ModelAndView referenceData(HttpServletRequest request) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		// Into page context: skins
		data.put("skins", skinService.fetchAvailableSkinNames());
		// Into page context: hasPermission, this var is not a simple permission, but one of 3, see 
		// the source of org.sakaiproject.site.impl.BaseSiteService#save for details
		data.put("hasPermission", SakaiUtils.hasPermission(SiteService.SECURE_UPDATE_SITE) || SakaiUtils.hasPermission(SiteService.SECURE_UPDATE_SITE_MEMBERSHIP)
		        || SakaiUtils.hasPermission(SiteService.SECURE_UPDATE_GROUP_MEMBERSHIP));
		return new ModelAndView("index.html", data);
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