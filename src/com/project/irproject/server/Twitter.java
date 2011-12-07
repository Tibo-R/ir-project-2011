package com.project.irproject.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;

import com.project.irproject.shared.SearchDoc;

public class Twitter implements Source{

	private List<Tweet> tweets = null;
	private String lastQuery = "";
	private int maxRetrieved = 100;
	private int nbPages = 10;

	@Override
	public List<SearchDoc> search(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	private final List<Tweet> getResults(String query){
		if((lastQuery == query) && (tweets != null))
			return tweets;
		else{
			twitter4j.Twitter twitter = new TwitterFactory().getInstance();
			try {
				List<Tweet> allTweets = new ArrayList<Tweet>();
				for(int i=nbPages; i>0; i--){
					Query q = new Query(query);
					q.setRpp(maxRetrieved);
					q.setLang("en");
					q.setPage(i);
					q.setResultType(Query.MIXED);
					QueryResult result = twitter.search(q);
					allTweets.addAll(result.getTweets());
				}
				tweets = allTweets;
				return allTweets;
			} catch (TwitterException te) {
				te.printStackTrace();
				System.out.println("Failed to search tweets: " + te.getMessage());
			}
		}
		return null;
	}
	
	public Map<String, Double> getMediaFrequency(String query){
		return getMediaFrequency(query, false);
	}

	public Map<String, Double> getMediaFrequency(String query, boolean expandAllUrls){

		int totalRetrieved = 0;
		Map<String, Double> results = new HashMap<String, Double>(){
			{
				put("none", 0d);
				put("image", 0d);
				put("video", 0d);
			};
		};
		List<Tweet> tweets = getResults(query);
		totalRetrieved = tweets.size();
		for (Tweet tweet : tweets) {
			if(tweet.getMediaEntities() != null){
				results.put("image", results.get("image") + 1);
			}
			else if(tweet.getURLEntities() != null){
				URLEntity[] urls = tweet.getURLEntities();
				for (int i=0; i< urls.length; i++) {
					URL urlTmp = urls[i].getExpandedURL();
					if(urlTmp != null){
						String url = urlTmp.toString();
						if(expandAllUrls){
							try {
								url = Tools.expandShortURL(url);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						if(url != null){
							System.out.println(url);
							
							if(url.contains("youtu") || url.contains("daily") || url.contains("video"))
								results.put("video", results.get("video") + 1);
							else if(url.contains("img") || url.contains("pic") || url.contains("flickr") || url.contains("photo"))
								results.put("image", results.get("image") + 1);
							else results.put("none", results.get("none") + 1);
						}
						
					}
				}
			}
			else results.put("none", results.get("none") + 1);
		}
		System.out.println(totalRetrieved);
		return results;
	}
	
	public ArrayList<String> getWordsForExpansion(String query){
		ArrayList<String> words = new ArrayList<String>();
		String corpus = getCorpus(query);
		HashMap<String, Integer> occs = getOccurences(corpus);
		if(occs.containsKey(query.toLowerCase()))
			occs.remove(query.toLowerCase());

		SortedSet<Map.Entry<String, Integer>> list = entriesSortedByValues(occs);
		int nbWords = list.size();
		//		for(int i=0; i < 5; i++){
		//			words.add(list.last().getKey());
		//			list.remove(list.last());
		//		}
		//		System.out.println(words);
		int i = 0;
		for(Map.Entry<String, Integer> e : list){
			i++;
			if(i > nbWords - 5){
				words.add(e.getKey());
				System.out.println(e.getKey() + " ("+ e.getValue() + ")");
			}

		}

		//		Set keys = occs.keySet();
		//		Iterator it = keys.iterator();
		//		for(int i = 0; i< 5; i++){
		//			words.add((String) it.next());
		//		}
		return words;
	}

	static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
		SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
				new Comparator<Map.Entry<K,V>>() {
					@Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
						int res = e1.getValue().compareTo(e2.getValue());
						return res != 0 ? res : 1; // Special fix to preserve items with equal values
					}
				}
				);
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}



	private final String getCorpus(String query){

		String corpus = "";
		List<Tweet> tweets = getResults(query);
		for (Tweet tweet : tweets) {
			corpus = corpus.concat(tweet.getText());
			//			System.out.println("text : " + tweet.getText());
		}
		return corpus.toLowerCase();
	}

	private List<String> stopWords = Arrays.asList("'twas","a","able","about","across","after","ain't","all","almost","also","am","among","an","and","any","are","aren't","as","at","be","because","been","but","by","can","can't","cannot","could","could've","couldn't","dear","did","didn't","do","does","doesn't","don't","either","else","ever","every","for","from","get","got","had","has","hasn't","have","he","he'd","he'll","he's","her","hers","him","his","how","how'd","how'll","how's","however","i","i'd","i'll","i'm","i've","if","in","into","is","isn't","it","it's","its","just","least","let","like","likely","may","me","might","might've","mightn't","most","must","must've","mustn't","my","neither","no","nor","not","of","off","often","on","only","or","other","our","own","rather","said","say","says","shan't","she","she'd","she'll","she's","should","should've","shouldn't","since","so","some","than","that","that'll","that's","the","their","them","then","there","there's","these","they","they'd","they'll","they're","they've","this","tis","to","too","twas","us","wants","was","wasn't","we","we'd","we'll","we're","were","weren't","what","what'd","what's","when","when","when'd","when'll","when's","where","where'd","where'll","where's","which","while","who","who'd","who'll","who's","whom","why","why'd","why'll","why's","will","with","won't","would","would've","wouldn't","yet","you","you'd","you'll","you're","you've","your");
	private List<String> punctuation = Arrays.asList(",",".",":","?","!");
	private final HashMap<String,Integer> getOccurences(String corpus){
		//		System.out.println("AVANT : ");
		//		System.out.println(corpus);
		corpus = corpus.replaceAll("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", " ");
		//		corpus = corpus.replaceAll("\\W", "");
		corpus = corpus.replaceAll("\\p{Punct}+", " ");
		//		System.out.println("APRÈS : ");
		//		System.out.println(corpus);
		StringReader stringReader = new StringReader(corpus);
		BufferedReader br = new BufferedReader(stringReader);
		String[] spliter;
		HashMap<String,Integer> tag = new HashMap<String,Integer>();

		StringTokenizer token = new StringTokenizer(corpus);
		while (token.hasMoreTokens()){
			String str = token.nextToken();
			System.out.println(str);
			if(str.length() > 2 && !stopWords.contains(str)){
				if(tag.containsKey(str)){
					Integer oldValue = tag.get(str);
					tag.put(str, oldValue + 1);
				}else{
					tag.put(str, 1);
				}
			}
		}
		//		String ligne;
		//		try {
		//			while ((ligne=br.readLine())!=null){
		//
		//				spliter = ligne.split(" ");
		//				for (int i = 1 ; i < spliter.length ; i++){
		//					if(spliter[i].length() > 2 && !stopWords.contains(spliter[i])){
		//						if(tag.containsKey(spliter[i])){
		//							Integer oldValue = tag.get(spliter[i]);
		//							tag.put(spliter[i], oldValue + 1);
		//						}else{
		//							tag.put(spliter[i], 1);
		//						}
		//					}
		//
		//
		//				}
		//
		//			}
		//		} catch (IOException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		return tag;

	}

}
