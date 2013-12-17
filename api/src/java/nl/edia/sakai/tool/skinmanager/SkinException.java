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
 * <p>
 * Skin related exception.
 * </p>
 * 
 * @author Roland
 *
 */
public class SkinException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7078857106748207231L;

	public SkinException() {
	}

	public SkinException(String message) {
		super(message);
	}

	public SkinException(Throwable cause) {
		super(cause);
	}

	public SkinException(String message, Throwable cause) {
		super(message, cause);
	}

}
