package com.rik.server.datamodel;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Serialized;
import com.rik.server.SDao;
import com.rik.shared.BRep;


public class JsonCache {
	public JsonCache() {
	
	}
	public JsonCache(String permUrl, JsonElement jsonData, Date updated) {
		super();
		this.permUrl = permUrl;
		this.jsonElement = jsonData.toString();
		this.updated = updated;
	}
	@Id
	public String permUrl;
	public String jsonElement;
	public Date updated;
	public Key<JsonCache> getKey(){
		return new Key(JsonCache.class,permUrl);
	}
	public static Key<JsonCache> getKey(String url){
		return new Key(JsonCache.class,url);
	}
	public static JsonElement getJsonElement(String url){
		System.setProperty("http.agent", "trading /u/terracaines"); 
		JsonCache jc = SDao.getJsonCacheDao().get(getKey(url));
		if(jc != null && jc.updated.after(Calendar.getInstance().getTime())){
			JsonParser jp = new JsonParser();
			
			return jp.parse(jc.jsonElement);
		}
		try {
			URL uurl = new URL(url);
			InputStream openStream = uurl.openStream();
			
			JsonReader reader = new JsonReader(new InputStreamReader(openStream));
			JsonParser jp = new JsonParser();
			JsonElement je=jp.parse(reader);
			Calendar cal=Calendar.getInstance();
			cal.add(Calendar.MINUTE, 3);
			SDao.getJsonCacheDao().put(new JsonCache(url, je, cal.getTime()));
			return je;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
