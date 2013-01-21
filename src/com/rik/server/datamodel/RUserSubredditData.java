package com.rik.server.datamodel;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;




public class RUserSubredditData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RUserSubredditData() {
	
	}
	@Id
	public Long id;
	
	public RUserSubredditData(String subredditname) {
		super();
		this.subredditname = subredditname;
	}
	String subredditname;
	int score;
	int dailyscore;
	public Date dailyTime;
}
