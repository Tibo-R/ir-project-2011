package com.project.irproject.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.util.Set;

import org.json.JSONException;
import org.xml.sax.SAXException;



import com.gmail.yuyang226.flickr.Flickr;
import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.photos.Extras;
import com.gmail.yuyang226.flickr.photos.Photo;
import com.gmail.yuyang226.flickr.photos.PhotoList;
import com.gmail.yuyang226.flickr.photos.PhotosInterface;
import com.gmail.yuyang226.flickr.photos.SearchParameters;
import com.project.irproject.shared.SearchDoc;




public class Flick implements Source {
	
	 
	Flickr flickr;
	PhotosInterface photosInterface;
 
	public Flick(){
		 
		//flickr = new Flickr("4393b8a4b3cc1442db68779bb53c8be9","09ca0ae995443198",new REST());
		flickr = new Flickr("4393b8a4b3cc1442db68779bb53c8be9");
		
		photosInterface = flickr.getPhotosInterface();
	}
	
	@Override
	public List<SearchDoc> search(String query) {
		
		List<SearchDoc> docs = new ArrayList<SearchDoc>();
		SearchParameters param = new SearchParameters();
		
		Calendar calendar = Calendar.getInstance();
		param.setMaxTakenDate(calendar.getTime());
		param.setMaxUploadDate(calendar.getTime());
		param.setExtras(Extras.ALL_EXTRAS);
		
		calendar.roll(Calendar.DAY_OF_YEAR, -7);
		
		param.setText(query);
		param.setSort(SearchParameters.INTERESTINGNESS_DESC);
		param.setMinTakenDate(calendar.getTime());
		param.setMinUploadDate(calendar.getTime());
	
		PhotoList matching_photos = null;
		
		try{
		  matching_photos = photosInterface.search(param, 50, 1);
	
		  
		  for(int i = 0 ; i < matching_photos.size() ; i ++){
			Photo photo=(Photo)matching_photos.get(i);
			SearchDoc doc = new SearchDoc(photo.getTitle(), "flickr", photo.getMediumUrl());
			doc.setNumberView(Long.valueOf(photo.getViews()));
			doc.setPubliDate(photo.getDatePosted());
			String summary = photo.getDescription();
			for(Object tag : photo.getTags()){
				summary += " " + tag.toString();
			}
			doc.setSummary(summary);
			docs.add(doc);
		  }
		  docs = Ranking.setRelativeScore(docs);
		  docs = Ranking.getTopResults(docs, 50);
		  return Ranking.setResultScore(docs);
		}
	    catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
			
		
	}
	
}
