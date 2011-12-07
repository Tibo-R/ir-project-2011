package com.project.irproject.server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class Tools {
	
	 public static String expandShortURL(String address) throws IOException {
	        URL url = new URL(address);
	 
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //using proxy may increase latency
	        connection.setInstanceFollowRedirects(false);
	        connection.connect();
	        String expandedURL = connection.getHeaderField("Location");
	        connection.getInputStream().close();
	        return expandedURL;
	    }
	
//	public static LinkedHashMap<String, Integer> sortHashMapByValuesD(HashMap<?, ?> passedMap) {
//	    List mapKeys = new ArrayList<Object>(passedMap.keySet());
//	    List mapValues = new ArrayList<Object>(passedMap.values());
//	    Collections.sort(mapValues);
//	    Collections.sort(mapKeys);
//	        
//	    LinkedHashMap<String, Integer> sortedMap = 
//	        new LinkedHashMap<String, Integer>();
//	    
//	    Iterator<?> valueIt = mapValues.iterator();
//	    while (valueIt.hasNext()) {
//	        Object val = valueIt.next();
//	        Iterator<?> keyIt = mapKeys.iterator();
//	        
//	        while (keyIt.hasNext()) {
//	            Object key = keyIt.next();
//	            String comp1 = passedMap.get(key).toString();
//	            String comp2 = val.toString();
//	            
//	            if (comp1.equals(comp2)){
//	                passedMap.remove(key);
//	                mapKeys.remove(key);
//	                sortedMap.put((String)key, (Integer)val);
//	                break;
//	            }
//
//	        }
//
//	    }
//	    return sortedMap;
//	}

}
