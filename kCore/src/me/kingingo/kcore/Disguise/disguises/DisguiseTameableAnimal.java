package me.kingingo.kcore.Disguise.disguises;
import java.util.UUID;

import me.kingingo.kcore.Util.UtilPlayer;
import net.minecraft.server.v1_8_R3.EntityLiving;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class DisguiseTameableAnimal extends DisguiseAnimal
{
  public DisguiseTameableAnimal(Entity entity)
  {
    super(entity);

    this.DataWatcher.a(16, Byte.valueOf((byte)0));
    this.DataWatcher.a(17, "");
  }

  public boolean isTamed()
  {
    return (this.DataWatcher.getByte(16) & 0x4) != 0;
  }

  public void setTamed(boolean tamed)
  {
    int i = this.DataWatcher.getByte(16);

    if (tamed)
      this.DataWatcher.watch(16, Byte.valueOf((byte)(i | 0x4)));
    else
      this.DataWatcher.watch(16, Byte.valueOf((byte)(i | 0xFFFFFFFB)));
  }

  public boolean isSitting()
  {
    return (this.DataWatcher.getByte(16) & 0x1) != 0;
  }

  public void setSitting(boolean sitting)
  {
    int i = this.DataWatcher.getByte(16);

    if (sitting)
      this.DataWatcher.watch(16, Byte.valueOf((byte)(i | 0x1)));
    else
      this.DataWatcher.watch(16, Byte.valueOf((byte)(i | 0xFFFFFFFE)));
  }

  public void setOwnerUUID(String uuid)
  {
    this.DataWatcher.watch(17, uuid);
  }

  public String getOwnerUUID()
  {
    return this.DataWatcher.getString(17);
  }
  
  public Player getOwner()
  {
    try {
      UUID uuid = UUID.fromString(getOwnerUUID());
      if (uuid == null || UtilPlayer.isOnline(uuid)) {
        return null;
      }
      return Bukkit.getPlayer(uuid);
      } catch (IllegalArgumentException e) {
    }
    return null;
  }
}