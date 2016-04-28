package eu.epicpvp.kcore.Teams;

import eu.epicpvp.kcore.Listener.kListener;

public class TeamListener extends kListener{

	private TeamManager teamManager;
	
	public TeamListener(TeamManager teamManager) {
		super(teamManager.getInstance(), "TeamListener");
		this.teamManager=teamManager;
	}

}
