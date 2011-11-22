package com.project.irproject.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * It Reads and prints any RSS/Atom feed type.
 * <p>
 * @author Alejandro Abdelnur
 *
 */
public class FeedReader {

	public static void main(String[] args) throws IllegalArgumentException, FeedException, IOException {
		URL feedUrl = new URL("http://news.google.com/news?q=lemurs&output=rss");

		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = input.build(new XmlReader(feedUrl));

		System.out.println(feed);

	}
}
