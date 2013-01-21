package com.rik.servlet;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sun.org.apache.xml.internal.security.Init;

public class MSession implements Serializable{
	public String modhash;
	public String rid;
	public String cookie;
	public String redditname;
	
	public MSession() {
		// TODO Auto-generated constructor stub
	}
	public static MSession create(HttpServletRequest req) {
		HttpSession hs=req.getSession();
		MSession ms = new MSession();
		ms.modhash=(String) hs.getAttribute("modhash");
		ms.rid=(String) hs.getAttribute("rid");
		ms.cookie=(String) hs.getAttribute("cookie");
		ms.redditname=(String) hs.getAttribute("redditname");
		if(ms.redditname == null){
			ms.redditname ="technology";
		}
		//aoeu
		//initDev(ms);
		return ms;
	}
	
	public static MSession setOnLogin(HttpServletRequest req,String modhash,String rid,String cookie) {
		HttpSession hs=req.getSession(true);
		MSession ms = new MSession();
		hs.setAttribute("modhash",modhash);
		hs.setAttribute("rid",rid);
		hs.setAttribute("cookie",cookie);
		hs.setAttribute("redditname", "technology");
		return ms;
	}

	public boolean notLoggedIn() {
		return modhash == null;
	}
	
	
}
