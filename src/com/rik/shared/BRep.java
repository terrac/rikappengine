package com.rik.shared;

import java.io.Serializable;

public class BRep implements Serializable {

	private static final long serialVersionUID = 8070535911881173481L;
	public String rid;
	public String message;
	public int score;
	public String subreddit;

	public BRep() {
	}

	public BRep(String rid, String message, int score, String subreddit) {
		super();
		this.rid = rid;
		this.message = message;
		this.score = score;
		this.subreddit = subreddit;
	}

	public static String getColorCoded(int score) {
		String color = "green";
		if(score < 0){
			color = "red";
		}
		String value = "<font color="+color+">"+score+"</font>";
		return value;
	}
	@Override
	public String toString() {
		return message + " " + score;
	}

	@Override
	public boolean equals(Object obj) {
		return rid.equals(((BRep) obj).rid);
	}
}