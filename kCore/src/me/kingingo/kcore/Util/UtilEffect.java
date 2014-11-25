package me.kingingo.kcore.Util;

import net.minecraft.server.v1_7_R4.PacketPlayOutWorldParticles;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class UtilEffect {

	public static void playCircleEffect(Player player){
		playCircleEffect(player.getLocation().add(0,-1,0));
	}
	
//	public static void playCircleEffect(Location loc){
//		double radius = 2;
//	    for(double y = 0; y <= 50; y+=0.05) {
//	        double x = radius * Math.cos(y);
//	        double z = radius * Math.sin(y);
//	        UtilParticle.RED_DUST.display(0.0F, 0.0F, 0.0F, 1, 40, new Location(loc.getWorld(),(float) (loc.getX() + x), (float) (loc.getY() + y), (float) (loc.getZ() + z)), 7);
////	        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles("reddust", (float) (loc.getX() + x), (float) (loc.getY() + y), (float) (loc.getZ() + z), 0, 0, 0, 0, 1);
////	        for(Player online : UtilServer.getPlayers()) {
////	            ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
////	        }
//	    }
//	}

//	public static void playCircleEffect(Location loc){
//		double radius = 0.5;
//		PacketPlayOutWorldParticles packet;
//    	for(double y = 0; y <= 3; y+=0.00001) {
//    		double x = radius * Math.cos(y);
//    		double z = radius * Math.sin(y);
//	        packet = new PacketPlayOutWorldParticles("reddust", (float) (loc.getX() + x), (float) (loc.getY() + y), (float) (loc.getZ() + z), 0, 0, 0, 0, 0);
//	        for(Player online : UtilServer.getPlayers()) {
//	            ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
//	        }
//	    }
//	}
	
	public static void playCircleEffect(Location loc){
			double radius = 0.001;
			PacketPlayOutWorldParticles packet;
			PacketPlayOutWorldParticles packet1;
        	for(double y = 4; y >= 0; y-=0.002) {
        		radius+=0.001;
        		double x = radius * Math.cos(y);
        		double z = radius * Math.sin(y);
        		double z1 = radius * Math.cos(225-y);
        		double x1 = radius * Math.sin(225-y);
		        packet = new PacketPlayOutWorldParticles("witchMagic", (float) (loc.getX() + x), (float) (loc.getY() + y), (float) (loc.getZ() + z), 0, 0, 0, 0,1);
		        packet1 = new PacketPlayOutWorldParticles("witchMagic", (float) (loc.getX() + x1), (float) (loc.getY() + y), (float) (loc.getZ() + z1), 0, 0, 0, 0,1);
		        for(Player online : UtilServer.getPlayers()) {
		            ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
		            ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet1);
		        }
		    }
	}
	
//	public static void SNOW(Location loc){
//		double radius = 0.001;
//		PacketPlayOutWorldParticles packet;
//		PacketPlayOutWorldParticles packet1;
//    	for(double y = 4; y >= 0; y-=0.002) {
//    		radius+=0.001;
//    		double x = radius * Math.cos(y);
//    		double z = radius * Math.sin(y);
//    		double z1 = radius * Math.cos(225-y);
//    		double x1 = radius * Math.sin(225-y);
//	        packet = new PacketPlayOutWorldParticles("snowshovel", (float) (loc.getX() + x), (float) (loc.getY() + y), (float) (loc.getZ() + z), 5, 5, 5, 5,2);
//	        packet1 = new PacketPlayOutWorldParticles("snowshovel", (float) (loc.getX() + x1), (float) (loc.getY() + y), (float) (loc.getZ() + z1), 5, 5, 5, 5,2);
//	        for(Player online : UtilServer.getPlayers()) {
//	            ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
//	            ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet1);
//	        }
//	    }
//}
	
}