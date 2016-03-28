package eu.epicpvp.kcore.Disguise.disguises.livings;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import eu.epicpvp.kcore.Disguise.disguises.DisguiseMonster;

public class DisguiseWither extends DisguiseMonster
{
  public DisguiseWither(Entity entity)
  {
    super(entity);
    this.DataWatcher.a(17, new Integer(0));
    this.DataWatcher.a(18, new Integer(0));
    this.DataWatcher.a(19, new Integer(0));
    this.DataWatcher.a(20, new Integer(0));
  }

  public int cl() {
	  return this.DataWatcher.getInt(20);
  }

  public void r(int i) {
	  this.DataWatcher.watch(20, Integer.valueOf(i));
  }

  public int s(int i) {
	  return this.DataWatcher.getInt(17 + i);
  }

  public void b(int i, int j) {
	  this.DataWatcher.watch(17 + i, Integer.valueOf(j));
  }
  
  public EntityType GetEntityTypeId()
  {
    return EntityType.WITHER;
  }

  public String getHurtSound()
  {
    return "mob.wither.hurt";
  }
}