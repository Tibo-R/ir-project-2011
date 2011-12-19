package com.project.irproject.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.project.irproject.shared.SearchDoc;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<HashMap<String, List<SearchDoc>>> asyncCallback)
			throws IllegalArgumentException;
}
