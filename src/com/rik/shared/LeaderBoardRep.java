package com.rik.shared;


import com.rik.server.datamodel.LeaderBoard;


public class LeaderBoardRep  {
	
	private static final long serialVersionUID = 1L;
	public RURep[] repL;
	
	public static LeaderBoardRep getRep(LeaderBoard lb) {
		LeaderBoardRep lbr = new LeaderBoardRep();
		lbr.repL=lb.getRepresentationList().toArray(new RURep[0]);
		
		return lbr;
	}
	
	public static LeaderBoardRep getDailyRep(LeaderBoard lb) {
		LeaderBoardRep lbr = new LeaderBoardRep();
		lbr.repL=lb.getDailyRepresentationList().toArray(new RURep[0]);
		return lbr;
	}
		
}