package org.apollo.backend.method.impl;

import org.apollo.backend.method.Method;
import org.apollo.backend.method.handler.impl.GetServerConfigMethodHandler;
import org.apollo.backend.util.RequestMethod;

/**
 * An {@link Method} for the {@link GetServerConfigMethodHandler}
 * @author Steve
 */
public final class GetFrontendInformationMethod extends Method {

	private RequestMethod request;

	/**
	 * Create a new server config method.
	 */
	public GetFrontendInformationMethod() {
		super();
	}

	@Override
	public String getDisplay() {
		return "";
	}

	@Override
	public String getName() {
		return "getMethods";
	}

	@Override
	public RequestMethod getRequested() {
		return request;
	}

	@Override
	public void setRequested(RequestMethod request) {
		this.request = request;
	}
}
