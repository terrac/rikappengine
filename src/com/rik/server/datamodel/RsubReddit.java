package com.rik.server.datamodel;


import javax.persistence.Id;


public class RsubReddit {
	public RsubReddit() {
	
	}
	public RsubReddit(String redditname) {
		id = redditname;
	}
	@Id
	public String id;
	
	

}
