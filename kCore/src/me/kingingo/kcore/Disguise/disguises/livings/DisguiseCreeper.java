package me.kingingo.kcore.Disguise.disguises.livings;
import me.kingingo.kcore.Disguise.disguises.DisguiseMonster;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class DisguiseCreeper extends DisguiseMonster
{
  public DisguiseCreeper(Entity entity)
  {
    super(entity);

    this.DataWatcher.a(16, Byte.valueOf((byte)-1));
    this.DataWatcher.a(17, Byte.valueOf((byte)0));
    this.DataWatcher.a(18, Byte.valueOf((byte)0));
  }

  public EntityType GetEntityTypeId()
  {
    return EntityType.CREEPER;
  }

  public boolean IsPowered()
  {
    return this.DataWatcher.getByte(17) == 1;
  }

  public void SetPowered(boolean powered)
  {
    this.DataWatcher.watch(17, Byte.valueOf((byte)(powered ? 1 : 0)));
  }

  public int cm() {
	  return this.DataWatcher.getByte(16);
  }

  public void a(int i) {
	  this.DataWatcher.watch(16, Byte.valueOf((byte)i));
  }
  
  public boolean cn()
  {
    return this.DataWatcher.getByte(18) != 0;
  }

  public void co() {
    this.DataWatcher.watch(18, Byte.valueOf((byte)1));
  }

  protected String getHurtSound()
  {
    return "mob.creeper.say";
  }
}