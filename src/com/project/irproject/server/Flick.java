package com.project.irproject.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.SearchParameters;
import com.google.gdata.util.ServiceException;
import com.project.irproject.shared.SearchDoc;
import com.aetrion.flickr.photos.Photo;

public class Flick implements Source {
	
	Flickr flickr;
	PhotosInterface photosInterface;
 
	public Flick(){
		 
		//flickr = new Flickr("4393b8a4b3cc1442db68779bb53c8be9","09ca0ae995443198",new REST());
		flickr = new Flickr("4393b8a4b3cc1442db68779bb53c8be9");
		Flickr.debugStream=false;
		photosInterface = flickr.getPhotosInterface();
	}
	
	@Override
	public List<SearchDoc> search(String query) {
		
		List<SearchDoc> docs = new ArrayList<SearchDoc>();
		SearchParameters param = new SearchParameters();
		param.setText(query);
		param.setSort(SearchParameters.INTERESTINGNESS_DESC);
		PhotoList matching_photos = null;
		
		try{
		  matching_photos = photosInterface.search(param, 20, 1);
		  
		  for(int i = 0 ; i < matching_photos.size() ; i ++){
			Photo photo=(Photo)matching_photos.get(i);
			docs.add(new SearchDoc(photo.getTitle(), "flickr", photo.getUrl()) );
			
		  }
		  return docs;
		}
	    catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
			
		
	}
	
}
