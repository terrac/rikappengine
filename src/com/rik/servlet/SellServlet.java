package com.rik.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.rik.MSUtil;
import com.rik.server.RpcImpl;
import com.rik.shared.BRep;
import com.rik.shared.LeaderBoardRep;

@SuppressWarnings("serial")
public class SellServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException,ServletException {
		MSession ms=MSUtil.getSession(req,resp);
		MSUtil.doNavigation(resp,ms);
		
		if(ms.notLoggedIn()){
			return;
		}
		RpcImpl rpcImpl = new RpcImpl();
		
		String id = req.getParameter("id");
		
		
		BRep[] lbr=rpcImpl.getBoughtList(ms.rid);
		String message =null;
		if(id != null){
			message=rpcImpl.sell(ms.rid, rpcImpl.getUpdatedBRep(id));
			
		}for(BRep b : lbr){
			BRep br=rpcImpl.getById(b.rid);
			resp.getWriter().write("<br><a href=/sell?id="+b.rid+">"+b.message+"</a><font color=grey>"+b.score+"</font> "+BRep.getColorCoded(br.score));
		}
		
		if(id != null){
			resp.getWriter().write("<br>"+message);
			
		}
	}
}
