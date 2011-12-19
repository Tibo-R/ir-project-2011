package com.project.irproject.client;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.project.irproject.shared.SearchDoc;

public interface SendResultsServiceAsync {

	void sendResults(String query, HashMap<String, List<SearchDoc>> result,
			AsyncCallback<Boolean> callback);

}
