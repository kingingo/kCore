package eu.epicpvp.kcore.Disguise.disguises.livings;
import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import eu.epicpvp.kcore.Disguise.disguises.DisguiseAnimal;

public class DisguiseSheep extends DisguiseAnimal
{
  public DisguiseSheep(Entity entity)
  {
    super(entity);

    this.DataWatcher.a(16, new Byte((byte)0));
  }

  public EntityType GetEntityTypeId()
  {
    return EntityType.SHEEP;
  }

  public boolean isSheared()
  {
    return (this.DataWatcher.getByte(16) & 0x10) != 0;
  }

  public void setSheared(boolean sheared)
  {
    byte b0 = this.DataWatcher.getByte(16);

    if (sheared)
      this.DataWatcher.watch(16, Byte.valueOf((byte)(b0 | 0x10)));
    else
      this.DataWatcher.watch(16, Byte.valueOf((byte)(b0 & 0xFFFFFFEF)));
  }

  public int getColor()
  {
    return this.DataWatcher.getByte(16) & 0xF;
  }

  public void setColor(DyeColor color)
  {
    byte b0 = this.DataWatcher.getByte(16);

    this.DataWatcher.watch(16, Byte.valueOf((byte)(b0 & 0xF0 | color.getWoolData() & 0xF)));
  }
}