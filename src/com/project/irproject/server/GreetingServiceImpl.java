package com.project.irproject.server;


import java.util.List;

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

	public List<SearchDoc> greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);
//		Map<String, SearchDoc> docsRetr = null;
//		Twitter twitter = new TwitterFactory().getInstance();
//		try {
//			
//			Query query = new Query(input);
//			query.setRpp(20);
//			QueryResult result = twitter.search(query);
//			docsRetr = getAllDocs(result.getTweets());
//		} catch (TwitterException te) {
//			te.printStackTrace();
//			System.out.println("Failed to search tweets: " + te.getMessage());
//		}
		
		Youtube ytSource = new Youtube();
		List<SearchDoc> docsRetr = ytSource.search(input);
		
		GNews gnSource = new GNews();
		docsRetr.addAll(gnSource.search(input));
		
		return docsRetr;
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
