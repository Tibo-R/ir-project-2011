package com.project.irproject.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.client.youtube.YouTubeQuery.Time;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.geo.impl.GeoRssWhere;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaPlayer;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.data.youtube.YouTubeMediaContent;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YouTubeMediaRating;
import com.google.gdata.data.youtube.YtPublicationState;
import com.google.gdata.data.youtube.YtStatistics;
import com.google.gdata.util.ServiceException;
import com.project.irproject.shared.SearchDoc;

public class Youtube implements Source{
	private YouTubeQuery youtubeQuery;

	public Youtube() {
		super();
		try {
			youtubeQuery = new YouTubeQuery(new URL("http://gdata.youtube.com/feeds/api/videos"));
			// order results by the number of views (most viewed first)
			youtubeQuery.setTime(Time.THIS_WEEK);
			youtubeQuery.setOrderBy(YouTubeQuery.OrderBy.VIEW_COUNT);
			youtubeQuery.setMaxResults(10);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public List<SearchDoc> search(String query) {
		// search for puppies and include restricted content in the search results
		youtubeQuery.setFullTextQuery(query);
		youtubeQuery.setSafeSearch(YouTubeQuery.SafeSearch.NONE);

		YouTubeService service = new YouTubeService("irproject-0.1");
		VideoFeed videoFeed;
		try {
			
			videoFeed = service.query(youtubeQuery, VideoFeed.class);
			System.out.println("got it !");
			return docsFromVideoFeed(videoFeed, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	private List<SearchDoc> docsFromVideoFeed(VideoFeed videoFeed, boolean b) {
		List<SearchDoc> docs = new ArrayList<SearchDoc>();
		for(VideoEntry videoEntry : videoFeed.getEntries() ) {
			docs.add(docFromVideoEntry(videoEntry));
		}
		return docs;
	}

	private SearchDoc docFromVideoEntry(VideoEntry videoEntry) {
		YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup();
		MediaPlayer mediaPlayer = mediaGroup.getPlayer();
		SearchDoc doc = new SearchDoc(videoEntry.getTitle().getPlainText(), "youtube", mediaGroup.getVideoId());
		return doc;
	}

	public static void printVideoEntry(VideoEntry videoEntry, boolean detailed) {
		System.out.println("Title: " + videoEntry.getTitle().getPlainText());

		if(videoEntry.isDraft()) {
			System.out.println("Video is not live");
			YtPublicationState pubState = videoEntry.getPublicationState();
			if(pubState.getState() == YtPublicationState.State.PROCESSING) {
				System.out.println("Video is still being processed.");
			}
			else if(pubState.getState() == YtPublicationState.State.REJECTED) {
				System.out.print("Video has been rejected because: ");
				System.out.println(pubState.getDescription());
				System.out.print("For help visit: ");
				System.out.println(pubState.getHelpUrl());
			}
			else if(pubState.getState() == YtPublicationState.State.FAILED) {
				System.out.print("Video failed uploading because: ");
				System.out.println(pubState.getDescription());
				System.out.print("For help visit: ");
				System.out.println(pubState.getHelpUrl());
			}
		}

		if(videoEntry.getEditLink() != null) {
			System.out.println("Video is editable by current user.");
		}

		if(detailed) {

			YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup();

			System.out.println("Uploaded by: " + mediaGroup.getUploader());

			System.out.println("Video ID: " + mediaGroup.getVideoId());
			System.out.println("Description: " + 
					mediaGroup.getDescription().getPlainTextContent());

			MediaPlayer mediaPlayer = mediaGroup.getPlayer();
			System.out.println("Web Player URL: " + mediaPlayer.getUrl());
			MediaKeywords keywords = mediaGroup.getKeywords();
			System.out.print("Keywords: ");
			for(String keyword : keywords.getKeywords()) {
				System.out.print(keyword + ",");
			}

			GeoRssWhere location = videoEntry.getGeoCoordinates();
			if(location != null) {
				System.out.println("Latitude: " + location.getLatitude());
				System.out.println("Longitude: " + location.getLongitude());
			}

			YtStatistics stats = videoEntry.getStatistics();
			if(stats != null ) {
				System.out.println("View count: " + stats.getViewCount());
			}
			System.out.println();

			System.out.println("\tThumbnails:");
			for(MediaThumbnail mediaThumbnail : mediaGroup.getThumbnails()) {
				System.out.println("\t\tThumbnail URL: " + mediaThumbnail.getUrl());
				System.out.println("\t\tThumbnail Time Index: " +
						mediaThumbnail.getTime());
				System.out.println();
			}

			System.out.println("\tMedia:");
			for(YouTubeMediaContent mediaContent : mediaGroup.getYouTubeContents()) {
				System.out.println("\t\tMedia Location: "+ mediaContent.getUrl());
				System.out.println("\t\tMedia Type: "+ mediaContent.getType());
				System.out.println("\t\tDuration: " + mediaContent.getDuration());
				System.out.println();
			}

			for(YouTubeMediaRating mediaRating : mediaGroup.getYouTubeRatings()) {
				System.out.println("Video restricted in the following countries: " +
						mediaRating.getCountries().toString());
			}
		}
	}

}