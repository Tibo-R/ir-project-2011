package com.project.irproject.client;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.project.irproject.shared.SearchDoc;

@RemoteServiceRelativePath("sendResults")
public interface SendResultsService extends RemoteService {

  Boolean sendResults(String query, HashMap<String, List<SearchDoc>> docs);

}
