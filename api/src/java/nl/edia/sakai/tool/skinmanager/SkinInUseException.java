/*
 * #%L
 * Edia Skin Manager Model API
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

/**
 * Skin is in use by a site. Cannot be deteled.
 * @author Roland
 *
 */
public class SkinInUseException extends SkinException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1670139642975466473L;

	/**
	 * 
	 */
	public SkinInUseException() {
	}

	/**
	 * @param message
	 */
	public SkinInUseException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public SkinInUseException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SkinInUseException(String message, Throwable cause) {
		super(message, cause);
	}

}
