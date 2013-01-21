package com.rik.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.rik.MSUtil;
import com.rik.server.RpcImpl;
import com.rik.shared.BRep;
import com.rik.shared.LeaderBoardRep;

@SuppressWarnings("serial")
public class BuyServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException,ServletException {
		MSession ms=MSUtil.getSession(req,resp);
		MSUtil.doNavigation(resp,ms);
		
		if(ms.notLoggedIn()){
			return;
		}
		RpcImpl rpcImpl = new RpcImpl();
		
		String id = req.getParameter("id");
		String message = "";
		if(id != null){
			message=rpcImpl.buy(ms.rid, rpcImpl.getUpdatedBRep(id));
		}
		BRep[] lbr=rpcImpl.getToBuyList(ms.redditname,ms.rid);
		for(BRep b : lbr){
			resp.getWriter().write("<br><a href=/buy?id="+b.rid+">"+b.message+"</a>");
		}
		
		if(id != null){
			resp.getWriter().write("<br>"+message);
		}
	}
}
