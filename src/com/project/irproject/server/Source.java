package com.project.irproject.server;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.project.irproject.shared.SearchDoc;

public interface Source extends IsSerializable{
	
	public List<SearchDoc> search(String query);
	
}
