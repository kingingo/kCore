package me.kingingo.kcore.Util;
import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R4.PacketPlayOutWorldParticles;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class UtilParticle
{
  public static void PlayParticle(Player player, ParticleType type, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count)
  {
    PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles();

    for (Field field : packet.getClass().getDeclaredFields())
    {
      try
      {
        field.setAccessible(true);
        String fieldName = field.getName();
        String str1;
        switch ((str1 = fieldName).hashCode()) { case 97:
          if (str1.equals("a")) break; break;
        case 98:
          if (str1.equals("b"));
          break;
        case 99:
          if (str1.equals("c"));
          break;
        case 100:
          if (str1.equals("d"));
          break;
        case 101:
          if (str1.equals("e"));
          break;
        case 102:
          if (str1.equals("f"));
          break;
        case 103:
          if (str1.equals("g"));
          break;
        case 104:
          if (str1.equals("h"));
          break;
        case 105:
          if (!str1.equals("i")) {
            field.set(packet, type.particleName);
            field.setFloat(packet, (float)location.getX());
            field.setFloat(packet, (float)location.getY());
            field.setFloat(packet, (float)location.getZ());
            field.setFloat(packet, offsetX);
            field.setFloat(packet, offsetY);
            field.setFloat(packet, offsetZ);
            field.setFloat(packet, speed);
          } else
          {
            field.setInt(packet, count);
          }
          break; }
      }
      catch (Exception e)
      {
        System.out.println(e.getMessage());
        return;
      }
    }

    ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
  }

  public static void PlayParticle(ParticleType type, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count)
  {
    for (Player player : UtilServer.getPlayers())
      PlayParticle(player, type, location, offsetX, offsetY, offsetZ, speed, count);
  }

  public static enum ParticleType
  {
    HUGE_EXPLOSION("hugeexplosion"), 
    LARGE_EXPLODE("largeexplode"), 
    FIREWORKS_SPARK("fireworksSpark"), 
    BUBBLE("bubble"), 
    SUSPEND("suspend"), 
    DEPTH_SUSPEND("depthSuspend"), 
    TOWN_AURA("townaura"), 
    CRIT("crit"), 
    MAGIC_CRIT("magicCrit"), 
    MOB_SPELL("mobSpell"), 
    MOB_SPELL_AMBIENT("mobSpellAmbient"), 
    SPELL("spell"), 
    INSTANT_SPELL("instantSpell"), 
    WITCH_MAGIC("witchMagic"), 
    NOTE("note"), 
    PORTAL("portal"), 
    ENCHANTMENT_TABLE("enchantmenttable"), 
    EXPLODE("explode"), 
    FLAME("flame"), 
    LAVA("lava"), 
    FOOTSTEP("footstep"), 
    SPLASH("splash"), 
    LARGE_SMOKE("largesmoke"), 
    CLOUD("cloud"), 
    RED_DUST("reddust"), 
    SNOWBALL_POOF("snowballpoof"), 
    DRIP_WATER("dripWater"), 
    DRIP_LAVA("dripLava"), 
    SNOW_SHOVEL("snowshovel"), 
    SLIME("slime"), 
    HEART("heart"), 
    ANGRY_VILLAGER("angryVillager"), 
    HAPPY_VILLAGER("happerVillager");

    public String particleName;

    private ParticleType(String particleName)
    {
      this.particleName = particleName;
    }
  }
}