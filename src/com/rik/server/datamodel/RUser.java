package com.rik.server.datamodel;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;

import com.google.gson.JsonArray;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Serialized;
import com.rik.server.SDao;
import com.rik.shared.BRep;


public class RUser {
	public RUser() {
	
	}
	public RUser(String userId, String nickname) {
		id = userId;
		displayName = nickname;
	}
	@Id
	public String id;
	public String displayName;
	public String message ="";
	//public int score;
	
	public String getDisplayName() {
		return displayName;
	}
	public Key<RUser> getKey(){
		return new Key(RUser.class,id);
	}
	public static Key<RUser> getKey(String rid){
		return new Key(RUser.class,rid);
	}
	@Serialized
	List<BRep> boughtThreads= new ArrayList<BRep>();

	public List<BRep> getBoughtJsonArray() {
		// take the list and put it in a json arraym.
		
		return boughtThreads;
	}
	public void buy(BRep ridBought) {
		boughtThreads.add(ridBought);
		SDao.getGUserDao().put(this);
	}
	public void sell(BRep ridSold) {
		int diff = ridSold.score - boughtThreads.get(boughtThreads.indexOf(ridSold)).score;
		
		addSubRedditScore(ridSold.subreddit, diff);
		boughtThreads.remove(ridSold);
		SDao.getGUserDao().put(this);
	}
	@Serialized
	Map<String,RUserSubredditData> mapRuserSubredditData = new HashMap<String, RUserSubredditData>();

	public void addSubRedditScore(String subreddit,int score){
		RUserSubredditData rsd = getRSD(subreddit);
		rsd.score += score;
		if(rsd.dailyTime == null||rsd.dailyTime.getDate() != Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
			rsd.dailyscore = 0;
			rsd.dailyTime = Calendar.getInstance().getTime();
			
		}
		rsd.dailyscore += score;
		
		SDao.getRUserSubredditDao().put(rsd);
	}
	public RUserSubredditData getRSD(String subreddit) {
		
		RUserSubredditData rsd = mapRuserSubredditData.get(subreddit);
		
		if(rsd == null){
			rsd = new RUserSubredditData(subreddit);
			mapRuserSubredditData.put(subreddit, rsd);
		}

		return rsd;
	}
	
	public int getSubRedditScore(String subreddit){
		return getRSD(subreddit).score;
	}
	public int getDailySubRedditScore(String subreddit){
		return getRSD(subreddit).dailyscore;
	}
	public void setMessage(String text) {
		message = text;
	}
}
