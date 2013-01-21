package com.rik.shared;

import java.io.Serializable;

import com.rik.server.datamodel.RUser;

public class RURep implements Comparable<RURep>,Serializable {
	
	private static final long serialVersionUID = 1L;
	public String ruid;
	public String name;
	public int score;
	public String message;
	public RURep() {
		// TODO Auto-generated constructor stub
	}
	public RURep(String ruid, String name, int score,String message) {
		super();
		this.ruid = ruid;
		this.name = name;
		this.score = score;
		this.message = message;
	}
	@Override
	public String toString() {
		return name + " " + score;
	}

	@Override
	public boolean equals(Object obj) {
		return ruid.equals(((RURep) obj).ruid);
	}

	@Override
	public int compareTo(RURep o) {
		// TODO Auto-generated method stub
		return score - o.score;
	}
	public static RURep getFrom(RUser user,String subreddit) {
		return new RURep(user.id, user.displayName, user.getSubRedditScore(subreddit),user.message);
	}
	public static RURep getDailyFrom(RUser user,String subreddit) {
		return new RURep(user.id, user.displayName, user.getDailySubRedditScore(subreddit),user.message);
	}
	
}