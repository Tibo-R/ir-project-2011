package com.project.irproject.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.project.irproject.shared.SearchDoc;

public class Ranking {

	public static List<SearchDoc> setResultScore(List<SearchDoc> docs) {
		int currentScore = 100;
		for(SearchDoc doc : docs){
			if(currentScore > 0){
				doc.increaseScore(currentScore);
				currentScore--;
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

}
