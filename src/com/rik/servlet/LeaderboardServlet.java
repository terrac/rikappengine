package com.rik.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.rik.MSUtil;
import com.rik.server.RpcImpl;
import com.rik.shared.LeaderBoardRep;
import com.rik.shared.RURep;

@SuppressWarnings("serial")
public class LeaderboardServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException,ServletException {
		MSession ms=MSUtil.getSession(req,resp,false);
		String redditname="technology";
		if(ms != null){
			redditname = ms.redditname;
		}

		LeaderBoardRep lbr=new RpcImpl().getLeaderBoard(redditname);

		LeaderBoardRep dlbr=new RpcImpl().getDailyLeaderBoard(redditname);
		MSUtil.doNavigation(resp,ms);
resp.getWriter().write("<br>All time");
		
		for( RURep ru :lbr.repL){
			resp.getWriter().write("<br>"+ru);
		}
		resp.getWriter().write("<br><br>Daily");
		
		for( RURep ru :dlbr.repL){
			resp.getWriter().write("<br>"+ru);
		}
	}
}
