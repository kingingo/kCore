package eu.epicpvp.kcore.Disguise.disguises.livings;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import eu.epicpvp.kcore.Disguise.disguises.DisguiseGolem;

public class DisguiseIronGolem extends DisguiseGolem
{
  public DisguiseIronGolem(Entity entity)
  {
    super(entity);

    this.DataWatcher.a(16, Byte.valueOf((byte)0));
  }

  public EntityType GetEntityTypeId()
  {
    return EntityType.IRON_GOLEM;
  }

  public boolean isPlayerCreated() {
	  return (this.DataWatcher.getByte(16) & 0x1) != 0;
  }

  public void setPlayerCreated(boolean flag)
  {
    byte b0 = this.DataWatcher.getByte(16);

    if (flag)
      this.DataWatcher.watch(16, Byte.valueOf((byte)(b0 | 0x1)));
    else
      this.DataWatcher.watch(16, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
  }

  protected String getHurtSound()
  {
    return "mob.irongolem.hit";
  }
}