package com.rik.servlet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.rik.server.RpcImpl;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(LoginServlet.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String logout=req.getParameter("logout");
		if(logout != null){
			req.getSession().invalidate();
			resp.sendRedirect("/leaderboard");	

			return;
		}

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws javax.servlet.ServletException, IOException {
		String username=req.getParameter("username");
		String password=req.getParameter("password");
		URL url = new URL("http://www.reddit.com/api/login/"
				+ username + "?api_type=json&user=" + username
				+ "&passwd=" + password);

		String data = "api_type=json&user=" + username + "&passwd="
				+ password;
		HttpURLConnection ycConnection = null;
		ycConnection = (HttpURLConnection) url.openConnection();
		ycConnection.setRequestMethod("POST");
		ycConnection.setDoOutput(true);
		ycConnection.setUseCaches(false);
		ycConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		ycConnection.setRequestProperty("Content-Length",
				String.valueOf(data.length()));

		DataOutputStream wr = new DataOutputStream(
				ycConnection.getOutputStream());
		wr.writeBytes(data);
		wr.flush();
		wr.close();
		JsonReader reader = new JsonReader(new InputStreamReader(
				ycConnection.getInputStream()));
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(reader);
		
		JsonObject jo = null;
		try {
			jo = je.getAsJsonObject().get("json")
					.getAsJsonObject().get("data").getAsJsonObject();
		} catch (Exception e) {
			resp.getWriter().write(""+je.getAsJsonObject().get("errors"));
			log.severe("Json element:"+je);
			e.printStackTrace();
		}
		String cookie=jo.get("cookie").getAsString();
		String modhash=jo.get("modhash").getAsString();
		String rid=username;
		
		reader.close();

		
		MSession.setOnLogin(req, modhash,rid,cookie);
	};
	
}
