package com.rik.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.rik.MSUtil;
import com.rik.server.RpcImpl;
import com.rik.shared.BRep;
import com.rik.shared.LeaderBoardRep;

@SuppressWarnings("serial")
public class ChooseServlet extends HttpServlet {
	String[] dl = new String[]{"science","technology","all"};
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException,ServletException {
		
		
		MSession ms=MSUtil.getSession(req,resp);
		String subreddit = req.getParameter("subreddit");
		if(subreddit != null){
			req.getSession().setAttribute("redditname", subreddit);
			ms.redditname = subreddit;
		}
		MSUtil.doNavigation(resp,ms);
		
		if(ms.notLoggedIn()){
			return;
		}
		
		String[] lbr=new RpcImpl().getMySubreddits(ms.rid,ms.cookie);
		ArrayList<String> arrayList = new ArrayList(Arrays.asList(dl));
		arrayList.addAll(Arrays.asList(lbr));
		
		for(String n : arrayList){
			resp.getWriter().write("<br><a href=/choose?subreddit="+n+">"+n+"</a>");
		}
		
	}
}
