package com.project.irproject.shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchDoc implements IsSerializable{

	public static Map<String, SearchDoc> links = new HashMap<String, SearchDoc>();
	
	private String title;
	private String type;
	private String url;
//	private Object source;
	private double score;
	
//	public static SearchDoc addDoc(String type, String url, Object post){
//		if(!links.containsKey(url)){
//			return new SearchDoc(type, url, post);
//		}
//		else{
//			SearchDoc existingDoc = links.get(url);
//			existingDoc.setScore(existingDoc.getScore() + 1);
//			existingDoc.addPost(post);
//			return existingDoc;
//		}
//	}	
//	


	public SearchDoc(String title, String type, String url) {
		super();
		this.title = title;
		this.type = type;
		this.url = url;
//		this.source = source;
	}
	
	public SearchDoc() {
		super();
		this.title = "";
		this.type = "";
		this.url = "";
//		this.source = null;
	}

	private SearchDoc(String type, String url, Object post) {
		super();
		this.type = type;
		this.url = url;
		this.score = 1;
		links.put(url, this);
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public double getScore() {
		return score;
	}


	public void setScore(double score) {
		this.score = score;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



//	public Object getSource() {
//		return source;
//	}
//
//
//
//	public void setSource(Object source) {
//		this.source = source;
//	}
	
	
	

}
