package com.rik;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rik.servlet.MSession;

public class MSUtil {
	/**
	 * only 5 threads can be bought at a time
	 * 
	 * when you buy a user you are added onto a list of that user.
	 * 
	 * When that user is accessed then every 30 minutes that user's karma gets
	 * polled on the server side. Then all of the people who have bought that
	 * user will get their rik updated
	 */
	public void updateUser() {

	}

	public static String getCookieValue(HttpServletRequest req, String cookieName,
			String defaultValue) {
		Cookie[] cA = req.getCookies();
		for (int i = 0; i < cA.length; i++) {
			Cookie cookie = cA[i];
			if (cookieName.equals(cookie.getName()))
				return (cookie.getValue());
		}
		return (defaultValue);
	}

	public static MSession getSession(HttpServletRequest req,
			HttpServletResponse resp) throws IOException,ServletException {
		
		return getSession(req, resp,true);
	}
	
	public static MSession getSession(HttpServletRequest req,
			HttpServletResponse resp,boolean requireLogin) throws IOException,ServletException {
		// display login info
		MSession session = MSession.create(req);
		
		if(session.modhash == null&&requireLogin){
			resp.sendRedirect("login.html?redirect="+req.getRequestURI());	
		} 
		
		return session;
	}

	public static void doNavigation(HttpServletResponse resp,MSession ms) throws IOException{
		resp.getWriter().write("<html><body>");
		
		if(ms != null){
			resp.getWriter().write("Current Reddit:"+ms.redditname +" ");
			
		}
		resp.getWriter().write("<a href=/leaderboard>leaderboard</a> ");
		resp.getWriter().write("<a href=/choose>choose</a> ");
		resp.getWriter().write("<a href=/sell>sell</a> ");
		resp.getWriter().write("<a href=/buy>buy</a> ");
		if(ms != null&&ms.modhash != null) {
			resp.getWriter().write("<a href=/login?logout=true>logout</a> ");
		}
		
	}
}
