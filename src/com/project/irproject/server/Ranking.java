package com.project.irproject.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Date;

import com.project.irproject.shared.SearchDoc;

public class Ranking {

	public static List<SearchDoc> setResultScore(List<SearchDoc> docs) {
		int currentScore = 100;
		for(SearchDoc doc : docs){
			if(currentScore > 0){
				doc.increaseScore(currentScore);
				currentScore -= 10;
			}
		}
		return docs;
	}
	
	
	public static List<SearchDoc> setRelativeScore(List<SearchDoc> docs) {
		
		
		for(SearchDoc doc : docs){
			if(doc.getType().equals("youtube") || doc.getType().equals("flickr")){
				Date date = doc.getPubliDate();
				Long days_published = days_between_now(date);
				Double views_day = new Double(doc.getNumberView()) / new Double(days_published);
				doc.increaseRelativeScore(views_day);
				
			}
			
		}
		return docs;
		
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
	
	public static List<SearchDoc> updateWithTwitterWordsScore(Twitter twitterSource, List<SearchDoc> docs, String query) {
		TreeMap<Integer, String> bestWords = twitterSource.getWordsForExpansion(query, 10);
		Map<String, Integer> wordsWithScore = new HashMap<String, Integer>();
//		System.out.println(bestWords);
		int nbTotalWords = 0;
		
		for (Integer key : bestWords.keySet()) {
			nbTotalWords += key;
		}
//		System.out.println("TotalNb = " + nbTotalWords);
		for (Entry<Integer, String> entry : bestWords.entrySet()) {
			double score = (double)entry.getKey()/(double)nbTotalWords*100f;
			wordsWithScore.put(entry.getValue(), (int) Math.round(score));
//			System.out.println("Clé : "+entry.getKey()+" Valeur : "+entry.getValue());
		}
		
		for(SearchDoc doc : docs){
//			System.out.println(doc.getSummary() + "(" + wordsWithScore + ")");
			for (Entry<String, Integer> entry : wordsWithScore.entrySet()) {
				if(doc.getSummary()!= null && doc.getSummary().toLowerCase().contains(entry.getKey())){
					doc.increaseScore(entry.getValue());
//					System.out.println(doc.getTitle() + " : " + entry.getValue());
				}
				if(doc.getTitle()!= null && doc.getTitle().toLowerCase().contains(entry.getKey())){
					doc.increaseScore(2*entry.getValue());
//					System.out.println(doc.getTitle() + " : " + entry.getValue());
				}
			}
			
		}

		return docs;
	}
	
	
	public static List<SearchDoc> updateWithTwitterMediaScore(Twitter twitterSource, List<SearchDoc> docs, String query) {
		Map<String, Double> medias = twitterSource.getMediaFrequency(query);
		
		for(SearchDoc doc : docs){
			if(doc.getType() == "flickr")
				doc.increaseScore(medias.get("image"));
			else if(doc.getType() == "youtube")
				doc.increaseScore(medias.get("video"));
		}

		return docs;
	}
	
	
	public static Long days_between_now(Date d1){
		
		Calendar now = Calendar.getInstance();
		Calendar publi = new GregorianCalendar();
		publi.setTime(d1);
		Integer days_between;
		
		return (long)( (now.getTime().getTime() - publi.getTime().getTime()) / (1000 * 60 * 60 * 24));
	}
}
