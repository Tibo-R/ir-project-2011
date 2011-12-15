package com.project.irproject.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchDoc implements IsSerializable, Comparable{

	public static Map<String, SearchDoc> links = new HashMap<String, SearchDoc>();
	public static int currentId = 1;
	
	public int id;
	private String title;
	private String type;
	private String url;
	private String content;
//	private Object source;
	private double score;
	private double relative_score;
	private String summary;
	private int relevant; //-1 = not relevant; 0 = don't know, 1 = relevant
	private Long numberView;
	private Date publiDate;
	
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


	public int getRelevant() {
		return relevant;
	}

	public void setRelevant(int relevant) {
		this.relevant = relevant;
	}

	public SearchDoc(String title, String type, String url) {
		super();
		this.title = title;
		this.type = type;
		this.url = url;
		this.summary = null;
		this.score = 0;
		this.relevant = 0;
		this.id = currentId++;
//		this.source = source;
	}
	
	public SearchDoc() {
		super();
		this.title = "";
		this.type = "";
		this.url = "";
		this.score = 0;
		this.relevant = 0;
		this.id = currentId++;
//		this.source = null;
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
	
	public void increaseScore(double score) {
		this.score += score;
	}
	
	public void increaseRelativeScore(double score){
		this.relative_score += relative_score;
	}


	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public boolean hasSummary() {
		if(summary != null)
			return true;
		return false;
	}

	@Override
	public int compareTo(Object obj) {
        if (obj instanceof SearchDoc) {
        	SearchDoc doc = (SearchDoc) obj;
            if (this.score < doc.getScore())
                return 1;
            else if (this.score > doc.getScore())
                return -1;
        }
		return 0;
	}

	public static int getCurrentId() {
		return currentId;
	}

	public static void setCurrentId(int currentId) {
		SearchDoc.currentId = currentId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public long getNumberView(){
		return numberView;
	}
	
	public void setNumberView(Long number){
		this.numberView = number;
	}
	
	public Date getPubliDate(){
		return publiDate;
	}
	
	public void setPubliDate(Date date){
		this.publiDate=date;
	}
	

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
