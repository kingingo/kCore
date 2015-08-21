package me.kingingo.kcore.Disguise.disguises.livings;
import me.kingingo.kcore.Disguise.disguises.DisguiseAnimal;
import me.kingingo.kcore.Disguise.disguises.DisguiseMonster;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class DisguiseSpider extends DisguiseMonster
{
  public DisguiseSpider(Entity entity)
  {
    super(entity);
    this.DataWatcher.a(16, new Byte((byte)0));
  }

  public EntityType GetEntityTypeId()
  {
    return EntityType.SPIDER;
  }
  
  public boolean n() {
	  return (this.DataWatcher.getByte(16) & 0x1) != 0;
  }

  public void a(boolean flag) {
	byte b0 = this.DataWatcher.getByte(16);
		
	if (flag)
	b0 = (byte)(b0 | 0x1);
	else {
	b0 = (byte)(b0 & 0xFFFFFFFE);
	}
		
	this.DataWatcher.watch(16, Byte.valueOf(b0));
	}

  public String getHurtSound()
  {
    return "mob.spider.say";
  }
}