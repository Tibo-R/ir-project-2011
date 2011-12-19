package com.project.irproject.server;


import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.project.irproject.client.GreetingService;
import com.project.irproject.shared.SearchDoc;
import com.project.irproject.shared.FieldVerifier;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
GreetingService {

	public HashMap<String, List<SearchDoc>> greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 2 characters long");
		}
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);
		
		Twitter twitterSource = new Twitter();
//		ArrayList<String> wordsForExpansion = twitterSource.getWordsForExpansion(input);
//		
//		for(String s : wordsForExpansion){
//			input += " " + s;
//		}
//		input = escapeHtml(input);
//		System.out.println("Query expanded : " + input);
		
		HashMap<String, List<SearchDoc>> result = new HashMap<String, List<SearchDoc>>();
		
		Youtube ytSource = new Youtube();
		List<SearchDoc> docsRetr = ytSource.search(input);
		
		GNews gnSource = new GNews();
		docsRetr.addAll(gnSource.search(input));
		
		Flick flSource = new Flick();
		docsRetr.addAll(flSource.search(input));
		
		result.put("baseline", Ranking.getTopResults(docsRetr, 20));
		
		docsRetr = Ranking.updateWithTwitterWordsScore(twitterSource, docsRetr, input);
		docsRetr = Ranking.updateWithTwitterMediaScore(twitterSource, docsRetr, input);
		docsRetr = Ranking.setRelativeScore(docsRetr);
		
		result.put("ranked", Ranking.getTopResults(docsRetr, 20));
		
		return result;
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
