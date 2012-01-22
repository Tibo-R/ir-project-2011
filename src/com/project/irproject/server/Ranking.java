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
		if(docs.get(0).getType().equals("youtube") || docs.get(0).getType().equals("flickr")){
			int totalViews = 0;
			for(int i=0; i<=10; i++){
				totalViews += docs.get(i).getScore();
			}
			int i = 0;
			for(SearchDoc doc : docs){
				if(i < 10){
					doc.setScore(doc.getScore() / totalViews * 100);
					System.out.println(docs.get(0).getType() + ": " + doc.getScore());
				}
				else
					doc.setScore(0);
				i++;
			}
		}
		else{
			int i = 0;
			for(SearchDoc doc : docs){
				if(i < 10){
					switch (i){
					case 0 : doc.setScore(30); break;
					case 1 : doc.setScore(20); break;
					case 2 : doc.setScore(15); break;
					case 3 : doc.setScore(10); break;
					case 4 : doc.setScore(8); break;
					case 5 : doc.setScore(5); break;
					case 6 : doc.setScore(3); break;
					case 7 : doc.setScore(3); break;
					case 8 : doc.setScore(3); break;
					case 9 : doc.setScore(3); break;
					
					}

				}
				else
					doc.setScore(0);
				i++;
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
				doc.setScore(views_day);

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
			else
				doc.increaseScore(medias.get("none"));
		}

		return docs;
	}


	public static Long days_between_now(Date d1){

		Calendar now = Calendar.getInstance();
		Calendar publi = new GregorianCalendar();
		publi.setTime(d1);
		Integer days_between;

		return (long)( (now.getTime().getTime() - publi.getTime().getTime()) / (1000 * 60 * 60 * 24) + 1);
	}
}
