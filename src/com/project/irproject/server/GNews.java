package com.project.irproject.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.project.irproject.shared.SearchDoc;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class GNews implements Source{

	@Override
	public List<SearchDoc> search(String query) {
		URL feedUrl;
		List<SearchDoc> docs = new ArrayList<SearchDoc>();
		try {
			query = java.net.URLEncoder.encode(query, "ISO-8859-1");
			feedUrl = new URL("http://news.google.com/news?q=" + query + "&output=rss&as_qdr=w");
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedUrl));
			for (final Iterator<?> iter = feed.getEntries().iterator(); iter.hasNext();)
			{
				final SyndEntry entry = (SyndEntry) iter.next();
				SearchDoc doc = new SearchDoc(entry.getTitle(), "news", entry.getLink());
				doc.setSummary(entry.getDescription().getValue());
				docs.add(doc);
			}

			return docs;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
