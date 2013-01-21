package com.rik.server;


import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.rik.server.datamodel.JsonCache;
import com.rik.server.datamodel.RUser;
import com.rik.server.datamodel.LeaderBoard;
import com.rik.shared.BRep;
import com.rik.shared.LeaderBoardRep;
import com.rik.shared.RURep;
import com.rik.shared.Rpc;

public class RpcImpl implements Rpc{

	@Override
	public LeaderBoardRep getLeaderBoard(String redditname) {
		//call update with id
		//get 10 users orderd by wealth (the update maintains this)
		// manipulate the data to be displayed as a jsonobject
		// return 
		LeaderBoard lb = LeaderBoard.getLeaderBoard(redditname);
		
		return LeaderBoardRep.getRep(lb);
}

	
	@Override
	public LeaderBoardRep getDailyLeaderBoard(String redditname) {
		LeaderBoard lb = LeaderBoard.getLeaderBoard(redditname);
		
		return LeaderBoardRep.getDailyRep(lb);
}

	public int getScore(String rid,String redditname){
		return getUser(rid).getSubRedditScore(redditname);
	}
	@Override
	public String buy(String rid, BRep ridBought) {
		
	
		//call update with both ids
		// add to list of users that the person owns
		// return error if erroring otherwise return null
		
		//basically just to set the score on the server side
		RUser user = getUser(rid);
		if(user.getBoughtJsonArray().size() > 5){
			return "You can only buy 5 at a time";
		}

		if(Arrays.asList(getBoughtList(rid)).contains(ridBought)){
			return "You cannot hold two of the the same item";
		}

		int score = getScore(ridBought);
		ridBought.score = score-2;
		
		user.buy(ridBought);
		
		return"Bought "+ ridBought.message;
	}

	

	

	@Override
	public BRep[] getBoughtList(String rid) {
		// TODO Auto-generated method stub
//		List<BRep> boughtL = new ArrayList<BRep>();
//		for(BRep b : getUser(rid).getBoughtJsonArray()){
//			boughtL.add(updateBRep(b));
//		}
//		return boughtL.toArray(new BRep[0]);
		return getUser(rid).getBoughtJsonArray().toArray(new BRep[0]);
	}

	@Override
	public BRep getById(String id) {
		return getUpdatedBRep(id);
	}
	
	@Override
	public String sell(String rid, BRep ridSold) {
		ridSold
		= getUpdatedBRep(ridSold.rid);
		RUser user = getUser(rid);
		
		user.sell(ridSold);
		int subRedditScore = user.getSubRedditScore(ridSold.subreddit);
		LeaderBoard lb=LeaderBoard.getLeaderBoard(ridSold.subreddit);
		
		updateLeaderBoard(lb,lb.getRepresentationList(),RURep.getFrom(user,ridSold.subreddit),ridSold, user,subRedditScore);
		if(lb.dailyTime == null||lb.dailyTime.getDate() != Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
			lb.getDailyRepresentationList().clear();
			lb.dailyTime = Calendar.getInstance().getTime();
		}
		
		updateLeaderBoard(lb,lb.getDailyRepresentationList(),RURep.getDailyFrom(user,ridSold.subreddit),ridSold, user,subRedditScore);
		return "Sold successfully.  Your score is now "+subRedditScore;
	}

	public int updateLeaderBoard(LeaderBoard lb, List<RURep> ruRep,RURep ruRepUser, BRep ridSold, RUser user, int subRedditScore) {

		ruRep.remove(ruRepUser);
		if(ruRep.size() < 10||ruRep.get(ruRep.size()-1).score <= subRedditScore){
			ruRep.add(ruRepUser);
			Collections.sort(ruRep,Collections.reverseOrder());
			
			if(ruRep.size() > 10){
				ruRep.remove(ruRep.size());
			}
			SDao.getLeaderBoardDao().put(lb);
		}
		
		return subRedditScore;
	}

	

	@Override
	public BRep[] getToBuyList(String redditname,String rid) {
		try {
			JsonElement je=JsonCache.getJsonElement("http://www.reddit.com/r/"+redditname+"/new.json?sort=new");
			JsonArray jsonArray = getArray(je);
			List<BRep> bol=getUser(rid).getBoughtJsonArray();
			ArrayList<BRep> list = new ArrayList<BRep>();     
			if (jsonArray != null) { 
			   int len = jsonArray.size();
			   for (int i=0;i<len;i++){
//				if(Math.random() > .6){
//					continue;
//				}
				JsonObject child = getChild(jsonArray, i);
							//need to rename
				BRep brep = updateBRep(child);
				if(bol.contains(brep)){
					continue;
				}
				
				list.add(brep);
				
			   } 
			} 


			
			return list.toArray(new BRep[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	//maby need to expand into a subreddit rep, probably should
	public String[] getMySubreddits(String rid, String modhash){
		List<String> l = new ArrayList<String>();
		JsonElement je=JsonCache.getJsonElement("http://www.reddit.com/reddits/mine.json?"+rid+"="+modhash);
		if(!je.isJsonObject()){
			return new String[0];
		}
		JsonArray jsonArray = getArray(je);
		if (jsonArray != null) { 
			   int len = jsonArray.size();
			   for (int i=0;i<len;i++){
				JsonObject child = getChild(jsonArray, i);
				//l.add(child.get("name").getAsString());
				l.add(child.get("display_name").getAsString());
				
			   }
		}
		return l.toArray(new String[0]);
	}
	@Override
	public void setMessage(String rid,String text) {
		RUser user = getUser(rid);
		user.setMessage(text);
		SDao.getGUserDao().put(user);
	}
	public RUser getUser(String rid) {
		RUser rUser = SDao.getGUserDao().get(RUser.getKey(rid));
		if(rUser == null){
			rUser = new RUser(rid, rid);
			SDao.getGUserDao().put(rUser);
		}
		return rUser;
	}
	public JsonObject getChild(JsonArray jsonArray, int i) {
		JsonObject child = jsonArray.get(i).getAsJsonObject().get("data").getAsJsonObject();
		return child;
	}

	public static JsonArray getArray(JsonElement je) {
		JsonArray jsonArray = je.getAsJsonObject().get("data").getAsJsonObject().get("children").getAsJsonArray();
		return jsonArray;
	}
	public int getScore(BRep ridBought) {
		JsonElement je=JsonCache.getJsonElement("http://www.reddit.com/by_id/"+ridBought.rid+".json");
		JsonArray jsonArray = getArray(je);
		JsonObject jo =getChild(jsonArray, 0);
		int score=jo.get("score").getAsInt();
		return score;
	}
	
	
	public BRep getUpdatedBRep(String id) {
		JsonElement je=JsonCache.getJsonElement("http://www.reddit.com/by_id/"+id+".json");
		JsonArray jsonArray = getArray(je);
		JsonObject jo =getChild(jsonArray, 0);
		
		return updateBRep(jo);
	}
	private BRep updateBRep(JsonObject child) {
		return new BRep(child.get("name").getAsString(), child.get("title").getAsString(), child.get("score").getAsInt(),child.get("subreddit").getAsString());
	}
	
	
	public static void main(String[] args) throws Exception {
//		String username = "username";
//		String password = "password";
//		URL url = new URL("http://www.reddit.com/api/login/"+username+"?api_type=json&user="+username+"&passwd="+password);
//		
//		JsonReader reader = new JsonReader(new InputStreamReader(url.openStream()));
//		JsonParser jp = new JsonParser();
//		JsonElement je=jp.parse(reader);
//		JsonObject jo = je.getAsJsonObject().get("json").getAsJsonObject().get("data").getAsJsonObject();
//		String cookie=jo.get("cookie").getAsString();
//		String modhash=jo.get("modhash").getAsString();
//		
//		reader.close();
	}


	
}
