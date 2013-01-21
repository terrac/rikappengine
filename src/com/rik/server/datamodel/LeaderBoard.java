package com.rik.server.datamodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

import com.google.gson.JsonArray;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Serialized;
import com.rik.server.SDao;
import com.rik.shared.BRep;
import com.rik.shared.RURep;

public class LeaderBoard {
	public LeaderBoard() {

	}

	public LeaderBoard(String redditname) {
		id = redditname;
	}

	@Id
	public String id;
	@Serialized
	List<RURep> repList = new ArrayList<RURep>();
	@Serialized
	List<RURep> dailyRepList = new ArrayList<RURep>();
	public Date dailyTime;
	public Key<LeaderBoard> getKey() {
		return new Key(LeaderBoard.class, id);
	}

	public static Key<LeaderBoard> getKey(String rid) {
		Key key = null;;
		try {
			key = new Key(LeaderBoard.class, rid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Key(LeaderBoard.class, "technology");
		}
		return key;
	}

	public List<RURep> getRepresentationList() {
		// TODO Auto-generated method stub
		return repList;
	}
	
	public List<RURep> getDailyRepresentationList() {
		// TODO Auto-generated method stub
		return dailyRepList;
	}

	//holds a list ordered of 10 users
	// on update it takes whatever user is updating and compares the value
	//if the value is above the lowest one then it puts the reddit user in the list
	public void update(String rid) {
		
		
	}

	public static LeaderBoard getLeaderBoard(String redditname) {
		LeaderBoard lb = SDao.getLeaderBoardDao().get(LeaderBoard.getKey(redditname));
		if(lb == null){
			lb = new LeaderBoard(redditname);
			SDao.getLeaderBoardDao().put(lb);
		}
		return lb;
	}

}
