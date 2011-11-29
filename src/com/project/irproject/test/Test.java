package com.project.irproject.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;

import com.google.gdata.client.Service;
import com.google.gdata.client.youtube.YouTubeQuery;
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
import com.google.gdata.model.gd.Rating;
import com.google.gdata.util.ServiceException;
import com.project.irproject.server.Twitter;

public class Test {

	/**
	 * @param args
	 * @throws ServiceException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, ServiceException {
		Twitter t = new Twitter();
		System.out.println(t.getWordsForExpansion("obama"));
		
	
	}



}
