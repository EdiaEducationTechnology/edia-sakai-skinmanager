package nl.edia.sakai.tool.skinmanager;

import org.springframework.webflow.context.servlet.WebFlow1FlowUrlHandler;
import org.springframework.webflow.core.collection.AttributeMap;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class EdiaWebFlow1FlowUrlHandler extends WebFlow1FlowUrlHandler {

	private static final String DEFAULT_URL_ENCODING_SCHEME = "UTF-8";

	private String urlEncodingScheme = DEFAULT_URL_ENCODING_SCHEME;

	@Override
	public String createFlowExecutionUrl(String flowId, String flowExecutionKey, HttpServletRequest request) {
		StringBuilder url = new StringBuilder();
		url.append(request.getContextPath());
		url.append(request.getServletPath());
		url.append('?');
		this.appendQueryParameter(url, "_flowId", flowId);
		url.append('&');
		this.appendQueryParameter(url, "_flowExecutionKey", flowExecutionKey);
		return url.toString();
	}

	@Override
	public String createFlowDefinitionUrl(String flowId, AttributeMap<?> input, HttpServletRequest request) {
		StringBuilder url = new StringBuilder();
		url.append(request.getContextPath());
		url.append(request.getServletPath());
		url.append('?');
		this.appendQueryParameter(url, "_flowId", flowId);
		if (input != null && !input.isEmpty()) {
			url.append('&');
			this.appendQueryParameters(url, input.asMap());
		}
		return url.toString();
	}

	private <T> void appendQueryParameters(StringBuilder url, Map<String, T> parameters) {
		Iterator<Map.Entry<String, T>> entries = parameters.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, T> entry = entries.next();
			appendQueryParameter(url, entry.getKey(), entry.getValue());
			if (entries.hasNext()) {
				url.append('&');
			}
		}
	}

	private void appendQueryParameter(StringBuilder url, Object key, Object value) {
		String encodedKey = encode(key);
		String encodedValue = encode(value);
		url.append(encodedKey).append('=').append(encodedValue);
	}

	private String encode(Object value) {
		return value != null ? urlEncode(String.valueOf(value)) : "";
	}

	private String urlEncode(String value) {
		try {
			return URLEncoder.encode(String.valueOf(value), urlEncodingScheme);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Cannot url encode " + value);
		}
	}
}
