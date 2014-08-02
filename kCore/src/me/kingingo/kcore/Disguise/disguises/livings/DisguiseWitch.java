package me.kingingo.kcore.Disguise.disguises.livings;
import me.kingingo.kcore.Disguise.disguises.DisguiseMonster;

import org.bukkit.entity.Entity;

public class DisguiseWitch extends DisguiseMonster
{
  public DisguiseWitch(Entity entity)
  {
    super(entity);

    this.DataWatcher.a(21, Byte.valueOf((byte)0));
  }

  protected int GetEntityTypeId()
  {
    return 66;
  }

  public String getHurtSound()
  {
    return "mob.witch.hurt";
  }

  public void a(boolean flag)
  {
    this.DataWatcher.watch(21, Byte.valueOf((byte)(flag ? 1 : 0)));
  }

  public boolean bT()
  {
    return this.DataWatcher.getByte(21) == 1;
  }
}