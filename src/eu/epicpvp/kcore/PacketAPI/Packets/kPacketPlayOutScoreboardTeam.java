package eu.epicpvp.kcore.PacketAPI.Packets;

import java.util.Collection;

import org.bukkit.entity.Player;

import eu.epicpvp.kcore.PacketAPI.kPacket;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeamBase;

public class kPacketPlayOutScoreboardTeam implements kPacket{
	@Getter
	private PacketPlayOutScoreboardTeam packet;
	private String TEAM_NAME = "a";
	private String TEAM_DISPLAYNAME = "b";
	private String TEAM_PREFIX = "c";
	private String TEAM_SUFFIX = "d";
	private String MODE = "h";
	private String PLAYERS = "g";
	private String NAMETAG = "e";
	
	public kPacketPlayOutScoreboardTeam(){
		this.packet=new PacketPlayOutScoreboardTeam();
	}
	
	public kPacketPlayOutScoreboardTeam(PacketPlayOutScoreboardTeam packet){
		this.packet=packet;
	}
	
	public kPacketPlayOutScoreboardTeam(String name,String prefix,int mode,String player){
		packet=new PacketPlayOutScoreboardTeam();
		setDisplayname(name);
		setTeamName(name);
		setPrefix(prefix);
		setMode(mode);
		addPlayer(player);
		setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS);
	}
	
	public kPacketPlayOutScoreboardTeam(String name,String prefix,int mode,Player player){
		packet=new PacketPlayOutScoreboardTeam();
		setDisplayname(name);
		setTeamName(name);
		setPrefix(prefix);
		setMode(mode);
		setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS);
		addPlayer(player);
	}
	
	public kPacketPlayOutScoreboardTeam(String name,String prefix,String suffix,int mode,String player){
		packet=new PacketPlayOutScoreboardTeam();
		setDisplayname(name);
		setTeamName(name);
		setSuffix(suffix);
		setPrefix(prefix);
		setMode(mode);
		setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS);
		addPlayer(player);
	}
	
	public kPacketPlayOutScoreboardTeam(String name,String prefix,String suffix,int mode,Player player){
		packet=new PacketPlayOutScoreboardTeam();
		setDisplayname(name);
		setTeamName(name);
		setSuffix(suffix);
		setPrefix(prefix);
		setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS);
		setMode(mode);
		addPlayer(player);
	}
	
	public ScoreboardTeamBase.EnumNameTagVisibility getNameTagVisibility(){
		return (ScoreboardTeamBase.EnumNameTagVisibility) UtilReflection.getValue(NAMETAG, packet);
	}
	
	public void setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility n){
		UtilReflection.setValue(NAMETAG, packet,n);
	}
	
	public Collection<String> getPlayers(){
		return ((Collection<String>)UtilReflection.getValue(PLAYERS, packet));
	}
	
	public boolean containsPlayer(String player){
		return ((Collection<String>)UtilReflection.getValue(PLAYERS, packet)).contains(player);
	}
	
	public void removePlayer(String player){
		((Collection<String>)UtilReflection.getValue(PLAYERS, packet)).remove(player);
	}
	
	public void addPlayer(String player){
		((Collection<String>)UtilReflection.getValue(PLAYERS, packet)).add(player);
	}
	
	public boolean containsPlayer(Player player){
		return containsPlayer(player.getName());
	}
	
	public void removePlayer(Player player){
		removePlayer(player.getName());
	}
	
	public void addPlayer(Player player){
		addPlayer(player.getName());
	}
	
	public String getPrefix(){
		return (String) UtilReflection.getValue(TEAM_PREFIX, packet);
	}
	
	public void setPrefix(String prefix){
		UtilReflection.setValue(TEAM_PREFIX, packet,prefix);
	}
	
	public String getDisplayname(){
		return (String) UtilReflection.getValue(TEAM_DISPLAYNAME, packet);
	}
	
	public void setDisplayname(String name){
		UtilReflection.setValue(TEAM_DISPLAYNAME, packet,name);
	}
	
	public String getTeamName(){
		return (String) UtilReflection.getValue(TEAM_NAME, packet);
	}
	
	public void setTeamName(String name){
		UtilReflection.setValue(TEAM_NAME, packet,name);
	}
	
	public String getSuffix(){
		return (String) UtilReflection.getValue(TEAM_PREFIX, packet);
	}
	
	public void setSuffix(String suffix){
		UtilReflection.setValue(TEAM_SUFFIX, packet,suffix);
	}
	
	public int getMode(){
		return (int) UtilReflection.getValue(MODE, packet);
	}
	
	public void setMode(int mode){
		UtilReflection.setValue(MODE, packet,mode);
	}
	
	public static class Modes{
    	public static final int TEAM_CREATED = 0;
    	public static final int TEAM_REMOVED = 1;
    	public static final int TEAM_UPDATED = 2;
    	public static final int PLAYERS_ADDED = 3;
    	public static final int PLAYERS_REMOVED = 4;
    }
}
