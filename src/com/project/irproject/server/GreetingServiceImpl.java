package com.project.irproject.server;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import twitter4j.MediaEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.project.irproject.client.GreetingService;
import com.project.irproject.shared.FieldVerifier;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
GreetingService {

	public String[] greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		HashMap<String, Integer> weightedLinks = new HashMap<String, Integer>();
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);
		String[] tweetsString = null;
		Twitter twitter = new TwitterFactory().getInstance();
		try {
			int current = 0;
			Query query = new Query(input);
			query.setRpp(20);
			QueryResult result = twitter.search(query);
			List<Tweet> tweets = result.getTweets();
			tweetsString = new String[tweets.size()];
			for (Tweet tweet : tweets) {
				String tmp = "<img src ='" + tweet.getProfileImageUrl() + "'/>";
				if(tweet.getMediaEntities() != null){
					MediaEntity[] medias = tweet.getMediaEntities();
					for (int i=0; i< medias.length; i++) {
						tmp += "<img src ='" + medias[i].getMediaURL() + "'/>";
					}
				}

				if(tweet.getURLEntities() != null){
					URLEntity[] urls = tweet.getURLEntities();
					for (int i=0; i< urls.length; i++) {
						String urlString = urls[i].getExpandedURL().toString();
						if(weightedLinks.containsKey(urlString))
							weightedLinks.put(urlString, weightedLinks.get(urlString)+1);
						else
							weightedLinks.put(urlString, 1);
						LinkedHashMap sortedLinks = Tools.sortHashMapByValuesD(weightedLinks);
						List<String> mapKeys = new ArrayList(sortedLinks.keySet());
						for (String key : mapKeys) {
							Document doc;
							try {
								doc = Jsoup.connect(urls[i].getExpandedURL().toString()).get();
								String title = doc.title();
								tmp += "<div class='title'><a href='" + urls[i].getExpandedURL() + "'>" + title + "</a></div>";
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
//						tmp += "<a href='" + urls[i].getExpandedURL() + "'>" + urls[i].getDisplayURL() + "</a>";
					}
				}

				//				
				//				String text = tweet.getText();
				//				ArrayList<String> links = new ArrayList<String>();
				//				String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
				//				Pattern p = Pattern.compile(regex);
				//				//				Pattern p = Pattern.compile(
				//				//						"(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
				//				//								+ "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
				//				//								+ "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
				//				//								Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
				//				Matcher m = p.matcher(text);
				//				while(m.find()) {
				//					String urlStr = m.group();
				//					if (urlStr.startsWith("(") && urlStr.endsWith(")"))
				//					{
				//						urlStr = urlStr.substring(1, urlStr.length() - 1);
				//					}
				//					Document doc;
				//					try {
				//						doc = Jsoup.connect(urlStr).get();
				//						String title = doc.title();
				//						tmp += "<div class='title'><a href='" + urlStr + "'>" + title + "</a></div>";
				//						Elements images = doc.getElementsByTag("img");
				//						for (Element image : images) {
				//						  String imgSrc = image.attr("src");
				//						  tmp += "<img src='" + imgSrc + "'/>";
				//						}
				//					} catch (IOException e) {
				//						// TODO Auto-generated catch block
				//						e.printStackTrace();
				//					}
				//					
				////					LongUrl u = new LongUrl(urlStr);
				////					tmp += "<div class='title'><a href='" + u.getLongUrl() + "'>" + u.getTitle() + "</a></div>";
				//				}
				tmp += "<div class='tweet'>" + tweet.getText() + "</div>";
				tweetsString[current++] = tmp;
			}
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
		}
		return tweetsString;
		//		return "Hello, " + input + "!<br><br>I am running " + serverInfo
		//				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
	
	
}
