package me.kingingo.kcore.Disguise.disguises.livings;
import me.kingingo.kcore.Disguise.disguises.DisguiseMonster;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class DisguiseWitch extends DisguiseMonster
{
  public DisguiseWitch(Entity entity)
  {
    super(entity);

    this.DataWatcher.a(21, Byte.valueOf((byte)0));
  }

  public EntityType GetEntityTypeId()
  {
    return EntityType.WITCH;
  }

  public String getHurtSound()
  {
    return "mob.witch.hurt";
  }

  public void a(boolean flag)
  {
    this.DataWatcher.watch(21, Byte.valueOf((byte)(flag ? 1 : 0)));
  }

  public boolean n()
  {
    return this.DataWatcher.getByte(21) == 1;
  }
}