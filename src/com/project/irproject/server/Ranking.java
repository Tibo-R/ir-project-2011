package com.project.irproject.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Date;

import com.project.irproject.shared.SearchDoc;

public class Ranking {
	
	
	
	public static List<SearchDoc> setResultScore(List<SearchDoc> docs) {
		int currentScore = 100;
		for(SearchDoc doc : docs){
			if(currentScore > 0){
				doc.increaseScore(currentScore);
				currentScore = currentScore - 10;
			}
		}
		setRelativeScore(docs);
		return docs;
	}
	
	public static void setRelativeScore(List<SearchDoc> docs) {
		
		
		for(SearchDoc doc : docs){
			if(doc.getType().equals("youtube") || doc.getType().equals("flickr")){
				Date date = doc.getPubliDate();
				Long days_published = days_between_now(date);
				Double views_day = new Double(doc.getNumberView()) / new Double(days_published);
				doc.increaseRelativeScore(views_day);
				
			}
			
		}
		
	}
	
	
	public static List<SearchDoc> getTopResults(List<SearchDoc> docsRetr, int limit) {
		Collections.sort(docsRetr);
		if(limit < docsRetr.size()){
			List<SearchDoc> docs = new ArrayList<SearchDoc>();
			for(int i=0; i< limit; i++){
				docs.add(docsRetr.get(i));
			}
			Collections.sort(docs);
			return docs;
		}
			
		else
			return docsRetr;
		
	}
	
	public static Long days_between_now(Date d1){
		
		Calendar now = Calendar.getInstance();
		Calendar publi = new GregorianCalendar();
		publi.setTime(d1);
		Integer days_between;
		
		return (long)( (now.getTime().getTime() - publi.getTime().getTime()) / (1000 * 60 * 60 * 24));
	}

}
