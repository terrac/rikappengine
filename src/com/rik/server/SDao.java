package com.rik.server;

import com.rik.server.datamodel.JsonCache;
import com.rik.server.datamodel.RUser;
import com.rik.server.datamodel.LeaderBoard;
import com.rik.server.datamodel.RUserSubredditData;


public class SDao {

	
	public static Dao<RUser> getGUserDao(){
		return new Dao<RUser>(RUser.class);
	}
	
	public static Dao<LeaderBoard> getLeaderBoardDao(){
		return new Dao<LeaderBoard>(LeaderBoard.class);
	}
	
	public static Dao<JsonCache> getJsonCacheDao(){
		return new Dao<JsonCache>(JsonCache.class);
	}
	public static Dao<RUserSubredditData> getRUserSubredditDao(){
		return new Dao<RUserSubredditData>(RUserSubredditData.class);
	}
	
	
	
}
