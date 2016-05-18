package eu.epicpvp.kcore.ArchivmentsHandler;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;

@Getter
public class AchievementsHandler extends kListener{

	private JavaPlugin instance;
	private boolean config=true;
	
	public AchievementsHandler(JavaPlugin instance) {
		super(instance, "AchievementsHandler");
		this.instance=instance;
		UtilServer.setAchievementsHandler(this);
	}
	
	public Integer getProgress(Achievement achievement,Player player){
		return getProgress(achievement, UtilPlayer.getPlayerId(player));
	}
	
	public Integer getProgress(Achievement achievement, int playerId){
		if(isConfig()){
			kConfig config = UtilServer.getUserData().getConfig(Integer.valueOf(playerId));
			if(config.isSet("Achievements."+achievement.getName().toLowerCase()+".Progress")){
				return config.getInt("Achievements."+achievement.getName().toLowerCase()+".Progress");
			}
		}else{
			
		}
		return 0;
	}
	
	public void saveProgress(Achievement achievement,Player player){
		saveProgress(achievement, UtilPlayer.getPlayerId(player));
	}
	
	public void saveProgress(Achievement achievement, int playerId){
		if(isConfig()){
			kConfig config = UtilServer.getUserData().getConfig(Integer.valueOf(playerId));
			config.set("Achievements."+achievement.getName().toLowerCase()+".Progress", achievement.getPlayerProgress().get(Integer.valueOf(playerId)));
			config.save();
		}else{
			
		}
	}

}
