package me.kingingo.kcore.Util;

import org.bukkit.entity.Player;

public class UtilEffect {

//	public static void playCircleEffect(Player player){
//		playCircleEffect(player.getLocation().add(0,-1,0));
//	}
	
//	public static void playCircleEffect(Location loc){
//			double radius = 0.001;
//			PacketPlayOutWorldParticles packet;
//			PacketPlayOutWorldParticles packet1;
//        	for(double y = 4; y >= 0; y-=0.002) {
//        		radius+=0.001;
//        		double x = radius * Math.cos(y);
//        		double z = radius * Math.sin(y);
//        		double z1 = radius * Math.cos(225-y);
//        		double x1 = radius * Math.sin(225-y);
//		        packet = new PacketPlayOutWorldParticles("witchMagic", (float) (loc.getX() + x), (float) (loc.getY() + y), (float) (loc.getZ() + z), 0, 0, 0, 0,1);
//		        packet1 = new PacketPlayOutWorldParticles("witchMagic", (float) (loc.getX() + x1), (float) (loc.getY() + y), (float) (loc.getZ() + z1), 0, 0, 0, 0,1);
////        		packet = new WrapperPlayServerWorldParticles();
////        		packet.setX((float) (loc.getX() + x));
////        		packet.setY((float) (loc.getY() + y));
////        		packet.setZ((float) (loc.getZ() + z));
////        		packet.setParticleEffect(ParticleEffect.WITCH_MAGIC);
////        		
////        		packet1 = new WrapperPlayServerWorldParticles();
////        		packet1.setX((float) (loc.getX() + x));
////        		packet1.setY((float) (loc.getY() + y));
////        		packet1.setZ((float) (loc.getZ() + z));
////        		packet1.setParticleEffect(ParticleEffect.WITCH_MAGIC);
//		        for(Player online : UtilServer.getPlayers()) {
//		        	UtilPlayer.sendPacket(online, packet);
//		        	UtilPlayer.sendPacket(online, packet1);
//		        }
//		    }
//	}
}
